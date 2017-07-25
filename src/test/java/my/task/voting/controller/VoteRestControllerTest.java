package my.task.voting.controller;

import my.task.voting.controller.json.JsonUtil;
import my.task.voting.model.Vote;
import my.task.voting.service.VotingService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static my.task.voting.LunchTestData.LUNCH_2;
import static my.task.voting.LunchTestData.getNonexistentLunchId;
import static my.task.voting.TestUtil.setDeadline;
import static my.task.voting.TestUtil.userHttpBasic;
import static my.task.voting.UserTestData.*;
import static my.task.voting.VoteTestData.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + '/';

    @Autowired
    private VotingService votingService;

    @Test
    public void testCreate() throws Exception {
        Vote newVote = getNewVote();

        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.writeValue(newVote))
                .with(userHttpBasic(USER_1)))
                .andDo(print())
                .andExpect(status().isCreated());

        String returnedJSON = action.andReturn().getResponse().getContentAsString();
        Vote returnedVote = JsonUtil.readValue(returnedJSON, Vote.class);
        newVote.setId(returnedVote.getId());

        assertEquals(newVote, returnedVote);
        assertEquals(getExpectedListVotes(true, VOTE_1, VOTE_2, newVote), votingService.getAll());
    }

    @Test
    public void testUpdate() throws Exception {
        setDeadline(votingService, LocalTime.now().plusMinutes(5));

        Vote newVote = getNewVote();

        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.writeValue(newVote))
                .with(userHttpBasic(USER_1)))
                .andDo(print())
                .andExpect(status().isCreated());

        String returnedJSON = action.andReturn().getResponse().getContentAsString();
        Vote returnedVote = JsonUtil.readValue(returnedJSON, Vote.class);
        returnedVote.setLunchId(LUNCH_2.getId());

        mockMvc.perform(put(REST_URL + returnedVote.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(userHttpBasic(USER_1))
                .content(JsonUtil.writeValue(returnedVote)))
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(getExpectedListVotes(true, VOTE_1, VOTE_2, returnedVote), votingService.getAll());
    }

    @Test
    public void testUnacceptableChangeWhenUpdateAfterDeadline() throws Exception {
        setDeadline(votingService, LocalTime.now().minusMinutes(5));

        mockMvc.perform(put(REST_URL + VOTE_2.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(userHttpBasic(USER_2))
                .content(JsonUtil.writeValue(VOTE_2)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.cause", is("ChangeUnacceptableException")))
                .andExpect(jsonPath("$.details[0]", is("Too late for change vote")));
    }

    @Test
    public void testUnacceptableChangeWhenUpdateVoteFromPast() throws Exception {
        mockMvc.perform(put(REST_URL + VOTE_1.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(userHttpBasic(USER_2))
                .content(JsonUtil.writeValue(VOTE_1)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.cause", is("ChangeUnacceptableException")))
                .andExpect(jsonPath("$.details[0]", is("Creating or updating of vote is only allowed for today")));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testRepeatedWhenCreate() throws Exception {
        Vote repeatedVote = new Vote(null, VOTE_2.getVotingDate(), VOTE_2.getUserId(), VOTE_2.getLunchId());

        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.writeValue(repeatedVote))
                .with(userHttpBasic(USER_2)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.cause", is("HsqlException")))
                .andExpect(jsonPath("$.details[0]",
                        is("Only one vote per day for the user is possible")));
    }

    @Test
    public void testNotNewWhenCreate() throws Exception {
        Vote vote = getNewVote();
        vote.setId(getNonexistentVoteId());

        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.writeValue(vote))
                .with(userHttpBasic(USER_2)))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.cause", is("IllegalArgumentException")))
                .andExpect(jsonPath("$.details[0]", containsString("must be new (id=null)")));
    }

    @Test
    public void testNotConsistentIdWhenUpdate() throws Exception {
        setDeadline(votingService, LocalTime.now().plusMinutes(5));

        mockMvc.perform(put(REST_URL + VOTE_1.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(VOTE_2)))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.cause", is("IllegalArgumentException")))
                .andExpect(jsonPath("$.details[0]", containsString("must be with id=" + VOTE_1.getId())));
    }

    @Test
    public void testNotPermissionWhenUpdate() throws Exception {
        setDeadline(votingService, LocalTime.now().plusMinutes(5));

        mockMvc.perform(put(REST_URL + VOTE_2.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(userHttpBasic(USER_1))
                .content(JsonUtil.writeValue(VOTE_2)))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.cause", is("IllegalArgumentException")))
                .andExpect(jsonPath("$.details[0]", is("The specified user id=100002 does not match yours")));
    }


    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + VOTE1_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(VOTE_1.getId())))
                .andExpect(jsonPath("$.votingDate", is(VOTE_1.getVotingDate().toString())))
                .andExpect(jsonPath("$.userId", is(VOTE_1.getUserId())))
                .andExpect(jsonPath("$.lunchId", is(VOTE_1.getLunchId())));
    }

    @Test
    public void testGetUnauth() throws Exception {
        mockMvc.perform(get(REST_URL + VOTE1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetForbidden() throws Exception {
        mockMvc.perform(get(REST_URL + VOTE1_ID)
                .with(userHttpBasic(USER_1)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + getNonexistentLunchId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(VOTE_2.getId())))
                .andExpect(jsonPath("$[1].id", is(VOTE_1.getId())));
    }

    @Test
    public void testGetByUserId() throws Exception {
        mockMvc.perform(get(REST_URL + "by-user").param("user", VOTE_1.getUserId().toString())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id", is(VOTE_1.getId())))
                .andExpect(jsonPath("$[0].votingDate", is(VOTE_1.getVotingDate().toString())))
                .andExpect(jsonPath("$[0].userId", is(VOTE_1.getUserId())))
                .andExpect(jsonPath("$[0].lunchId", is(VOTE_1.getLunchId())));
    }

    @Test
    public void testGetByLunchId() throws Exception {
        mockMvc.perform(get(REST_URL + "by-lunch").param("lunch", VOTE_1.getLunchId().toString())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id", is(VOTE_1.getId())))
                .andExpect(jsonPath("$[0].votingDate", is(VOTE_1.getVotingDate().toString())))
                .andExpect(jsonPath("$[0].userId", is(VOTE_1.getUserId())))
                .andExpect(jsonPath("$[0].lunchId", is(VOTE_1.getLunchId())));
    }
}

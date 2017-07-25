package my.task.voting.service;

import my.task.voting.model.Vote;
import my.task.voting.util.exception.ChangeUnacceptableException;
import my.task.voting.util.exception.NotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalTime;

import static my.task.voting.LunchTestData.LUNCH_2;
import static my.task.voting.TestUtil.setDeadline;
import static my.task.voting.UserTestData.USER_2;
import static my.task.voting.VoteTestData.*;
import static org.junit.Assert.assertEquals;

public class VotingServiceTest extends AbstractServiceTest {

    @Autowired
    private VotingService votingService;

    @Test
    public void testSave() throws Exception {
        Vote newVote = votingService.save(getNewVote());
        assertEquals(getExpectedListVotes(true, VOTE_1, VOTE_2, newVote), votingService.getAll());
    }

    @Test
    public void testUpdate() throws Exception {
        setDeadline(votingService, LocalTime.now().plusMinutes(5));

        Vote newVote = votingService.save(getNewVote());
        newVote.setLunchId(LUNCH_2.getId());
        Vote updatedVote = votingService.save(newVote);
        assertEquals(updatedVote, votingService.get(updatedVote.getId()));
    }

    @Test
    public void testUnacceptableChangeWhenUpdateAfterDeadline() throws Exception {
        thrown.expect(ChangeUnacceptableException.class);
        thrown.expectMessage("Too late for change vote");

        setDeadline(votingService, LocalTime.now().minusMinutes(5));

        Vote newVote = votingService.save(getNewVote());
        newVote.setLunchId(LUNCH_2.getId());
        votingService.save(newVote);
    }

    @Test
    public void testUnacceptableChangeWhenUpdateVoteFromPast() throws Exception {
        thrown.expect(ChangeUnacceptableException.class);
        thrown.expectMessage("Creating or updating of vote is only allowed for today");

        votingService.save(getVoteFromPast());
    }

    @Test
    public void testNotNullWhenSave() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Vote must not be null");

        votingService.save(null);
    }

    @Test
    public void testRepeatedExceptionWhenSave() throws Exception {
        thrown.expect(DataIntegrityViolationException.class);

        votingService.save(getNewVote());
        votingService.save(getRepeatedVote());
    }

    @Test
    public void testGet() throws Exception {
        assertEquals(VOTE_1, votingService.get(VOTE_1.getId()));
    }

    @Test
    public void testNotFoundWhenGet() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Vote with such id doesn't exist");

        votingService.get(getNonexistentVoteId());
    }

    @Test
    public void testGetAll() throws Exception {
        assertEquals(getExpectedListVotes(true, VOTE_1, VOTE_2), votingService.getAll());
    }

    @Test
    public void testGetByUserId() throws Exception {
        assertEquals(getExpectedListVotes(false, VOTE_1, VOTE_2), votingService.getByUserId(USER_2.getId()));
    }

    @Test
    public void testGetByLunchId() throws Exception {
        assertEquals(getExpectedListVotes(false, VOTE_1), votingService.getByLunchId(LUNCH_2.getId()));
    }
}
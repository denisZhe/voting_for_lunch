package my.task.voting.controller;

import my.task.voting.controller.json.JsonUtil;
import my.task.voting.model.Lunch;
import my.task.voting.service.LunchService;
import my.task.voting.to.LunchTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static my.task.voting.LunchTestData.*;
import static my.task.voting.TestUtil.userHttpBasic;
import static my.task.voting.UserTestData.ADMIN;
import static my.task.voting.UserTestData.USER_1;
import static my.task.voting.util.LunchUtil.createTOFromLunchWithMeals;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LunchRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = LunchRestController.REST_URL + '/';

    @Autowired
    private LunchService lunchService;

    @Test
    public void testCreate() throws Exception {
        Lunch newLunch = getNewLunch();
        LunchTO lunchTO = createTOFromLunchWithMeals(newLunch);

        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.writeValue(lunchTO))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isCreated());

        String returnedJSON = action.andReturn().getResponse().getContentAsString();
        Lunch returned = JsonUtil.readValue(returnedJSON, Lunch.class);
        newLunch.setId(returned.getId());
        newLunch.setCreated(LocalDate.now());

        assertEquals(newLunch, returned);
        assertEquals(getExpectedListLunches(LUNCH_1, LUNCH_2, LUNCH_3, newLunch), lunchService.getAll());
    }

    @Test
    public void testUpdate() throws Exception {
        Lunch newLunch = getNewLunch();
        LunchTO lunchTO = createTOFromLunchWithMeals(newLunch);

        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.writeValue(lunchTO))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isCreated());

        String returnedJSON = action.andReturn().getResponse().getContentAsString();
        Lunch returned = JsonUtil.readValue(returnedJSON, Lunch.class);
        returned.setRestaurantName("newRestaurantUpdated");
        LunchTO updatedLunchTO = createTOFromLunchWithMeals(returned);

        mockMvc.perform(put(REST_URL + returned.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updatedLunchTO)))
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(getExpectedListLunches(LUNCH_1, LUNCH_2, LUNCH_3, returned), lunchService.getAll());
    }

    @Test
    public void testUnacceptableChangeWhenUpdate() throws Exception {
        LunchTO lunchTO = createTOFromLunchWithMeals(LUNCH_2);

        mockMvc.perform(put(REST_URL + LUNCH_2.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(lunchTO)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testRepeatedWhenCreate() throws Exception {
        Lunch lunch = getNewLunch();
        lunch.setRestaurantName(LUNCH_3.getRestaurantName());
        LunchTO lunchTO = createTOFromLunchWithMeals(lunch);

        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.writeValue(lunchTO))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.cause", is("HsqlException")))
                .andExpect(jsonPath("$.details[0]",
                        is("Only one lunch with such name of the restaurant is possible for this date")));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testRepeatedMeals() throws Exception {
        LunchTO lunchTO = createTOFromLunchWithMeals(getLunchWithRepeatedMeals());

        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.writeValue(lunchTO))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.cause", is("HsqlException")))
                .andExpect(jsonPath("$.details[0]",
                        is("Dishes with the same name are not allowed in one lunch")));
    }

    @Test
    public void testNotNewWhenCreate() throws Exception {
        Lunch lunch = getNewLunch();
        lunch.setId(getNonexistentLunchId());
        LunchTO lunchTO = createTOFromLunchWithMeals(lunch);

        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.writeValue(lunchTO))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.cause", is("IllegalArgumentException")))
                .andExpect(jsonPath("$.details[0]", containsString("must be new (id=null)")));
    }

    @Test
    public void testNotConsistentIdWhenUpdate() throws Exception {
        LunchTO lunchTO = createTOFromLunchWithMeals(LUNCH_3);

        mockMvc.perform(put(REST_URL + LUNCH_2.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(lunchTO)))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.cause", is("IllegalArgumentException")))
                .andExpect(jsonPath("$.details[0]", containsString("must be with id=" + LUNCH_2.getId())));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + LUNCH_1.getId())
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(getExpectedListLunches(LUNCH_2, LUNCH_3), lunchService.getAll());
    }

    @Test
    public void testUnacceptableChangeWhenDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + LUNCH_2.getId())
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.cause", is("ChangeUnacceptableException")))
                .andExpect(jsonPath("$.details[0]", is("For lunch already voted and it can not be deleted")));

    }

    @Test
    public void testNotFoundWhenDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + getNonexistentLunchId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + LUNCH1_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(LUNCH_1.getId())))
                .andExpect(jsonPath("$.created", is(LUNCH_1.getCreated().toString())))
                .andExpect(jsonPath("$.restaurantName", is("restaurant1")));
    }

    @Test
    public void testGetUnauth() throws Exception {
        mockMvc.perform(get(REST_URL + LUNCH1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetForbidden() throws Exception {
        mockMvc.perform(get(REST_URL + LUNCH1_ID)
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
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(LUNCH_3.getId())))
                .andExpect(jsonPath("$[1].id", is(LUNCH_2.getId())))
                .andExpect(jsonPath("$[2].id", is(LUNCH_1.getId())));
    }

    @Test
    public void testGetByDate() throws Exception {
        mockMvc.perform(get(REST_URL + "by-date").param("date", LUNCH_1.getCreated().toString())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id", is(LUNCH_1.getId())))
                .andExpect(jsonPath("$[0].created", is(LUNCH_1.getCreated().toString())))
                .andExpect(jsonPath("$[0].restaurantName", is("restaurant1")));
    }

    @Test
    public void testNotNullWhenGetByDate() throws Exception {
        mockMvc.perform(get(REST_URL + "by-date").param("date", "")
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.cause", is("IllegalArgumentException")))
                .andExpect(jsonPath("$.details[0]", is("Date must not be null")));
    }

    @Test
    public void testGetByDateWithMeals() throws Exception {
        mockMvc.perform(get(REST_URL + "detailed-by-date").param("date", LUNCH_1.getCreated().toString())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id", is(LUNCH_1.getId())))
                .andExpect(jsonPath("$[0].created", is(LUNCH_1.getCreated().toString())))
                .andExpect(jsonPath("$[0].restaurantName", is("restaurant1")))
                .andExpect(jsonPath("$[0].meals", hasSize(2)))
                .andExpect(jsonPath("$[0].meals[0].id", is(LUNCH_1.getMeals().get(0).getId())))
                .andExpect(jsonPath("$[0].meals[0].created", is(LUNCH_1.getMeals().get(0).getCreated().toString())))
                .andExpect(jsonPath("$[0].meals[0].dishName", is(LUNCH_1.getMeals().get(0).getDishName())))
                .andExpect(jsonPath("$[0].meals[0].price", is(LUNCH_1.getMeals().get(0).getPrice() / 100)))
                .andExpect(jsonPath("$[0].meals[1].id", is(LUNCH_1.getMeals().get(1).getId())));
    }
}

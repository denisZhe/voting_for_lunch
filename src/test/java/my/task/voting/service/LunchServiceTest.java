package my.task.voting.service;

import my.task.voting.model.Lunch;
import my.task.voting.util.exception.ChangeUnacceptableException;
import my.task.voting.util.exception.NotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

import static my.task.voting.LunchTestData.*;
import static org.junit.Assert.assertEquals;

public class LunchServiceTest extends AbstractServiceTest {

    @Autowired
    private LunchService lunchService;

    @Test
    public void testSave() throws Exception {
        Lunch newLunch = lunchService.save(getNewLunch());
        assertEquals(getExpectedListLunches(LUNCH_1, LUNCH_2, LUNCH_3, newLunch), lunchService.getAll());
    }

    @Test
    public void testUpdate() throws Exception {
        Lunch updatedLunch = lunchService.save(getUpdatedLunch());
        assertEquals(getExpectedListLunches(LUNCH_2, LUNCH_3, updatedLunch), lunchService.getAll());
    }

    @Test
    public void testUnacceptableChangeWhenUpdate() throws Exception {
        thrown.expect(ChangeUnacceptableException.class);
        thrown.expectMessage("For lunch already voted and it can not be changed");

        lunchService.save(getUnacceptableChangeLunch());
    }

    @Test
    public void testNotNullWhenSave() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Lunch must not be null");

        lunchService.save(null);
    }

    @Test
    public void testRepeatedWhenSave() throws Exception {
        thrown.expect(DataIntegrityViolationException.class);

        lunchService.save(getNewLunch());
        lunchService.save(getNewLunch());
    }

    @Test
    public void testDelete() throws Exception {
        lunchService.delete(LUNCH_1.getId());
        assertEquals(getExpectedListLunches(LUNCH_2, LUNCH_3), lunchService.getAll());
    }

    @Test
    public void testUnacceptableChangeWhenDelete() throws Exception {
        thrown.expect(ChangeUnacceptableException.class);
        thrown.expectMessage("For lunch already voted and it can not be deleted");

        lunchService.delete(LUNCH_2.getId());
    }

    @Test
    public void testNotFoundWhenDelete() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("The lunch with such id doesn't exist");

        lunchService.delete(getNonexistentLunchId());
    }

    @Test
    public void testGet() throws Exception {
        assertEquals(LUNCH_2, lunchService.get(LUNCH_2.getId()));
    }

    @Test
    public void testNotFoundWhenGet() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("The lunch with such id doesn't exist");

        lunchService.get(getNonexistentLunchId());
    }

    @Test
    public void testGetAll() throws Exception {
        assertEquals(getExpectedListLunches(LUNCH_1, LUNCH_2, LUNCH_3), lunchService.getAll());
    }

    @Test
    public void testGetByDate() throws Exception {
        assertEquals(LUNCH_2, lunchService.getByDate(LUNCH_2.getCreated()).get(0));
    }

    @Test
    public void testByDateWithMeals() throws Exception {
        List<Lunch> persistedLunches = lunchService.getByDateWithMeals(LUNCH_2.getCreated());
        assertEquals(LUNCH_2, persistedLunches.get(0));
        assertEquals(new ArrayList<>(persistedLunches.get(0).getMeals()), LUNCH_2.getMeals());
    }

    @Test
    public void testGetByNullDate() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Date must not be null");

        lunchService.getByDate(null);
    }
}
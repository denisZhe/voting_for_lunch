package my.task.voting.service;

import my.task.voting.model.Lunch;
import my.task.voting.util.ChangeUnacceptableException;
import my.task.voting.util.NotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static my.task.voting.LunchTestData.*;
import static org.junit.Assert.assertEquals;

public class LunchServiceTest extends AbstractServiceTest{

    @Autowired
    private LunchService lunchService;

    @Test
    public void testSave() throws Exception {
        Lunch newLunch = lunchService.save(getNewLunch());
        List<Lunch> expectedLunches = Stream.of(LUNCH_1, LUNCH_2, LUNCH_3, newLunch)
                .sorted(Comparator.comparing(Lunch::getCreated).reversed())
                .collect(Collectors.toList());
        List<Lunch> persistedLunches = lunchService.getAll();
        assertEquals(expectedLunches, persistedLunches);
    }

    @Test
    public void testUpdate() {
        Lunch updatedLunch = lunchService.save(getUpdatedLunch());
        List<Lunch> expectedLunches = Stream.of(LUNCH_2, LUNCH_3, updatedLunch)
                .sorted(Comparator.comparing(Lunch::getCreated).reversed())
                .collect(Collectors.toList());
        List<Lunch> persistedLunches = lunchService.getAll();

        assertEquals(expectedLunches, persistedLunches);
    }

    @Test
    public void testUnacceptableChangeWhenUpdate() {
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
        lunchService.save(getSimpleLunch());
        lunchService.save(getSimpleLunch());
    }

    @Test
    public void testDelete() throws Exception {
        lunchService.delete(LUNCH_1.getId());
        List<Lunch> expectedLunches = Stream.of(LUNCH_2, LUNCH_3)
                .sorted(Comparator.comparing(Lunch::getCreated).reversed())
                .collect(Collectors.toList());
        List<Lunch> persistedLunches = lunchService.getAll();
        assertEquals(expectedLunches, persistedLunches);
    }

    @Test
    public void testUnacceptableChangeWhenDelete() {
        thrown.expect(ChangeUnacceptableException.class);
        thrown.expectMessage("For lunch already voted and it can not be deleted");
        lunchService.delete(LUNCH_2.getId());
    }

    @Test
    public void testNotFoundWhenDelete() {
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
        List<Lunch> expectedLunches = Stream.of(LUNCH_1, LUNCH_2, LUNCH_3)
                .sorted(Comparator.comparing(Lunch::getCreated).reversed())
                .collect(Collectors.toList());
        List<Lunch> persistedLunches = lunchService.getAll();
        assertEquals(expectedLunches, persistedLunches);
    }

    @Test
    public void testGetByDate() throws Exception {
        List<Lunch> persistedLunches = lunchService.getByDate(LUNCH_2.getCreated());
        assertEquals(LUNCH_2, persistedLunches.get(0));
    }

    @Test
    public void testByDateWithMeals() {
        List<Lunch> persistedLunches = lunchService.getByDateWithMeals(LUNCH_2.getCreated());
        assertEquals(LUNCH_2, persistedLunches.get(0));
        assertEquals(new ArrayList<>(persistedLunches.get(0).getMeals()), LUNCH_2.getMeals());
    }
}
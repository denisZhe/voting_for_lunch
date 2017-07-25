package my.task.voting;

import my.task.voting.model.Lunch;
import my.task.voting.model.Meal;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static my.task.voting.model.BaseEntity.START_SEQ;

public class LunchTestData {

    public static final int LUNCH1_ID = START_SEQ + 3;
    private static final int MEAL1_ID = START_SEQ + 6;

    public static final Lunch LUNCH_1 = new Lunch(LUNCH1_ID, LocalDate.of(2017, 6, 19), "restaurant1");
    public static final Lunch LUNCH_2 = new Lunch(LUNCH1_ID + 1, LocalDate.of(2017, 6, 20), "restaurant2");
    public static final Lunch LUNCH_3 = new Lunch(LUNCH1_ID + 2, LocalDate.now(), "restaurant3");

    private static final Meal MEAL1 = new Meal(MEAL1_ID, LocalDate.of(2017, 6, 19), "dish1", 100, LUNCH_1);
    private static final Meal MEAL2 = new Meal(MEAL1_ID + 1, LocalDate.of(2017, 6, 19), "dish2", 100, LUNCH_1);
    private static final Meal MEAL3 = new Meal(MEAL1_ID + 2, LocalDate.of(2017, 6, 20), "dish3", 100, LUNCH_2);
    private static final Meal MEAL4 = new Meal(MEAL1_ID + 3, LocalDate.of(2017, 6, 20), "dish4", 50, LUNCH_2);
    private static final Meal MEAL5 = new Meal(MEAL1_ID + 4, LocalDate.of(2017, 6, 20), "dish5", 150, LUNCH_2);
    private static final Meal MEAL6 = new Meal(MEAL1_ID + 5, LocalDate.now(), "dish6", 155, LUNCH_3);
    private static final Meal MEAL7 = new Meal(MEAL1_ID + 6, LocalDate.now(), "dish7", 155, LUNCH_3);

    static {
        LUNCH_1.setMeals(Arrays.asList(MEAL1, MEAL2));
        LUNCH_2.setMeals(Arrays.asList(MEAL3, MEAL4, MEAL5));
        LUNCH_3.setMeals(Arrays.asList(MEAL6, MEAL7));
    }

    public static Lunch getNewLunch() {
        return new Lunch(null, LocalDate.now(), "newRestaurant");
    }

    public static Lunch getUpdatedLunch() {
        Lunch updatedLunch = new Lunch(LUNCH_1);
        updatedLunch.setRestaurantName("updatedRestaurantName");
        return updatedLunch;
    }

    public static Lunch getUnacceptableChangeLunch() {
        Lunch unacceptableChangeLunch = new Lunch(LUNCH_2);
        unacceptableChangeLunch.setRestaurantName("updatedRestaurantName");
        return unacceptableChangeLunch;
    }

    public static Lunch getLunchWithRepeatedMeals() {
        Lunch lunchWithRepeatedMeals = getNewLunch();
        lunchWithRepeatedMeals.setMeals(
                Arrays.asList(
                        new Meal(null, LocalDate.of(2017, 6, 22), "newMeal", 111, lunchWithRepeatedMeals),
                        new Meal(null, LocalDate.of(2017, 6, 22), "newMeal", 111, lunchWithRepeatedMeals))
        );
        return lunchWithRepeatedMeals;
    }

    public static int getNonexistentLunchId() {
        return 123;
    }

    public static List<Lunch> getExpectedListLunches(Lunch... lunches) {
        return Stream.of(lunches).sorted(Comparator.comparing(Lunch::getCreated).reversed()).collect(Collectors.toList());
    }
}

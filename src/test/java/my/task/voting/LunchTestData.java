package my.task.voting;

import my.task.voting.model.Lunch;
import my.task.voting.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static my.task.voting.model.BaseEntity.START_SEQ;

public class LunchTestData {

    private static final int LUNCH1_ID = START_SEQ + 3;
    private static final int MEAL1_ID = START_SEQ + 6;

    public static final Lunch LUNCH_1 = new Lunch(LUNCH1_ID, LocalDateTime.of(2017, 6, 19, 10, 15), "restaurant1");
    public static final Lunch LUNCH_2 = new Lunch(LUNCH1_ID + 1, LocalDateTime.of(2017, 6, 20, 10, 15), "restaurant2");
    public static final Lunch LUNCH_3 = new Lunch(LUNCH1_ID + 2, LocalDateTime.of(2017, 6, 21, 10, 15), "restaurant3");

    private static final Meal MEAL1 = new Meal(MEAL1_ID, LocalDateTime.of(2017, 6, 19, 10, 0), "dish1", 100, LUNCH_1);
    private static final Meal MEAL2 = new Meal(MEAL1_ID + 1, LocalDateTime.of(2017, 6, 19, 10, 0), "dish2", 100, LUNCH_1);
    private static final Meal MEAL3 = new Meal(MEAL1_ID + 2, LocalDateTime.of(2017, 6, 20, 10, 0), "dish3", 100, LUNCH_2);
    private static final Meal MEAL4 = new Meal(MEAL1_ID + 3, LocalDateTime.of(2017, 6, 20, 10, 0), "dish4", 50, LUNCH_2);
    private static final Meal MEAL5 = new Meal(MEAL1_ID + 4, LocalDateTime.of(2017, 6, 20, 10, 0), "dish5", 150, LUNCH_2);
    private static final Meal MEAL6 = new Meal(MEAL1_ID + 5, LocalDateTime.of(2017, 6, 21, 10, 0), "dish6", 155, LUNCH_3);
    private static final Meal MEAL7 = new Meal(MEAL1_ID + 6, LocalDateTime.of(2017, 6, 21, 10, 0), "dish7", 155, LUNCH_3);

    static {
        LUNCH_1.setMeals(new HashSet<>(Arrays.asList(MEAL1, MEAL2)));
        LUNCH_2.setMeals(new HashSet<>(Arrays.asList(MEAL3, MEAL4, MEAL5)));
        LUNCH_3.setMeals(new HashSet<>(Arrays.asList(MEAL6, MEAL7)));
    }

    public static Lunch getNewLunch() {
        Lunch newLunch = new Lunch(null, LocalDateTime.of(2017, 6, 22, 11, 15), "newRestaurant");
        Set<Meal> meals = new HashSet<>(
                Arrays.asList(
                        new Meal(null, LocalDateTime.of(2017, 6, 22, 11, 0), "newMeal", 111, newLunch),
                        new Meal(null, LocalDateTime.of(2017, 6, 22, 11, 0), "newMeal2", 222, newLunch)
                )
        );
        newLunch.setMeals(meals);
        return newLunch;
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

    public static int getNonexistentLunchId() {
        return 123;
    }
}

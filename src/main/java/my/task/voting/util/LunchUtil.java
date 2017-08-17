package my.task.voting.util;

import my.task.voting.model.Lunch;
import my.task.voting.model.Meal;
import my.task.voting.to.LunchTO;
import my.task.voting.to.MealTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class LunchUtil {
    public static Lunch createLunchFromTOWithMeals(LunchTO lunchTO) {
        Lunch lunch = new Lunch(lunchTO.getId(), LocalDate.now(), lunchTO.getRestaurantName());
        if (lunchTO.getMeals() != null && !lunchTO.getMeals().isEmpty()) {
            lunch.setMeals(lunchTO.getMeals().stream()
                    .map(mealTO -> new Meal(
                            mealTO.getId(),
                            LocalDate.now(),
                            mealTO.getDishName(),
                            mealTO.getPrice() * 100, // cents are discarded
                            lunch))
                    .collect(Collectors.toList())
            );
        }
        return lunch;
    }

    public static LunchTO createTOFromLunch(Lunch lunch) {
        return new LunchTO(lunch.getId(), lunch.getCreated(), lunch.getRestaurantName());
    }

    public static LunchTO createTOFromLunchWithMeals(Lunch lunch) {
        LunchTO lunchTO = new LunchTO(lunch.getId(), lunch.getCreated(), lunch.getRestaurantName());
        if (lunch.getMeals() != null && !lunch.getMeals().isEmpty()) {
            lunchTO.setMeals(lunch.getMeals().stream()
                    .map(meal -> new MealTO(
                            meal.getId(),
                            meal.getCreated(),
                            meal.getDishName(),
                            meal.getPrice() / 100)) // cents are discarded
                    .collect(Collectors.toList())
            );
        }
        return lunchTO;
    }

    public static List<LunchTO> createListTOFromLunchesWithMeals(List<Lunch> lunches) {
        return lunches.stream()
                .map(LunchUtil::createTOFromLunchWithMeals)
                .collect(Collectors.toList());
    }

    public static List<LunchTO> createListTOFromLunches(List<Lunch> lunches) {
        return lunches.stream()
                .map(lunch -> new LunchTO(lunch.getId(), lunch.getCreated(), lunch.getRestaurantName()))
                .collect(Collectors.toList());
    }
}

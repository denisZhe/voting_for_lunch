package my.task.voting.util;

import my.task.voting.model.Lunch;
import my.task.voting.model.Meal;
import my.task.voting.to.LunchTO;
import my.task.voting.to.MealTO;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class LunchUtil {
    public static Lunch createLunchFromTO(LunchTO lunchTO) {
        Lunch lunch = new Lunch(lunchTO.getId(), LocalDate.now(), lunchTO.getRestaurantName());
        if (lunchTO.getMeals() != null && !lunchTO.getMeals().isEmpty()) {
            lunch.setMeals(lunchTO.getMeals().stream()
                    .map(mealTO -> new Meal(
                            mealTO.getId(),
                            LocalDate.now(),
                            mealTO.getDishName(),
                            mealTO.getPrice(),
                            lunch))
                    .collect(Collectors.toList())
            );
        }
        return lunch;
    }

    public static LunchTO createTOFromLunch(Lunch lunch) {
        LunchTO lunchTO = new LunchTO();
        lunchTO.setId(lunch.getId());
        lunchTO.setRestaurantName(lunch.getRestaurantName());
        if (lunch.getMeals() != null && !lunch.getMeals().isEmpty()) {
            lunchTO.setMeals(lunch.getMeals().stream()
                    .map(meal -> {
                        MealTO mealTO = new MealTO();
                        mealTO.setId(meal.getId());
                        mealTO.setDishName(meal.getDishName());
                        mealTO.setPrice(meal.getPrice());
                        return mealTO;
                    })
                    .collect(Collectors.toList())
            );
        }
        return lunchTO;
    }
}

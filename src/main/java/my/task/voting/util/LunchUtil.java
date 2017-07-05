package my.task.voting.util;

import my.task.voting.model.Lunch;
import my.task.voting.model.Meal;
import my.task.voting.to.LunchTO;

import java.time.LocalDate;
import java.util.stream.Collectors;

import static my.task.voting.util.ValidationUtil.checkRepeatedMeal;

public class LunchUtil {
    public static Lunch createNewLunchFromTO(LunchTO lunchTO) {
        Lunch lunch = new Lunch(lunchTO.getId(), LocalDate.now(), lunchTO.getRestaurantName());
        if (!lunchTO.getMeals().isEmpty()) {
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
        checkRepeatedMeal(lunch);
        return lunch;
    }
}

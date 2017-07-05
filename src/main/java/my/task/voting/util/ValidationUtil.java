package my.task.voting.util;

import my.task.voting.model.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ValidationUtil {
    public static void checkNew(BaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void checkIdConsistent(BaseEntity entity, int id) {
//      http://stackoverflow.com/a/32728226/548473
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }

    public static void checkRepeatedMeal(Lunch lunch) {
        Set<Meal> mealSet = new HashSet<>(lunch.getMeals());
        if (mealSet.size() != lunch.getMeals().size()) {
            throw new IllegalArgumentException("Meals in the lunch must be unique");
        }
    }

    public static void checkUserPermissionForCreateOrUpdateVote(Vote vote, User user) {
        if (!Objects.equals(vote.getUserId(), user.getId())) {
            throw new NotFoundException("Vote with such id doesn't exist for this user");
        }
    }
}

package my.task.voting.util;

import my.task.voting.model.BaseEntity;
import my.task.voting.model.User;
import my.task.voting.model.Vote;

import java.util.Objects;

public class ValidationUtil {
    public static void checkNew(BaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    //      http://stackoverflow.com/a/32728226/548473
    public static void checkIdConsistent(BaseEntity entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }

    public static void checkUserPermissionForCreateOrUpdateVote(Vote vote, User user) {
        if (!Objects.equals(vote.getUserId(), user.getId())) {
            throw new IllegalArgumentException("The specified user id=" + vote.getUserId() + " does not match yours");
        }
    }

    //    http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }
}

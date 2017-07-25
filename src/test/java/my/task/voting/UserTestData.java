package my.task.voting;

import my.task.voting.model.Type;
import my.task.voting.model.User;

import java.time.LocalDateTime;

import static my.task.voting.model.BaseEntity.START_SEQ;

public class UserTestData {

    private static final int USER1_ID = START_SEQ;

    public static final User ADMIN = new User(USER1_ID,
            LocalDateTime.of(2017, 6, 15, 12, 30),
            Type.ROLE_ADMIN, "Admin", "admin@gmail.com", "adminpass");

    public static final User USER_1 = new User(USER1_ID +1,
            LocalDateTime.of(2017, 6, 16, 12, 30),
            Type.ROLE_USER, "User", "user@gmail.com", "password");

    public static final User USER_2 = new User(USER1_ID +2,
            LocalDateTime.of(2017, 6, 17, 12, 30),
            Type.ROLE_USER, "User2", "user2@gmail.com", "password2");

    public static User getNewUser() {
        return new User(null,
                LocalDateTime.of(2017, 6, 25, 12, 30),
                Type.ROLE_USER, "newUser", "newuser@gmail.com", "newpassword") ;
    }

    public static User getUpdatedUser() {
        User updatedUser = new User(USER_1);
        updatedUser.setName("updatedName");
        updatedUser.setEmail("updated@gmail.com");
        return updatedUser;
    }

    public static int getNonexistentUserId() {
        return 123;
    }
}

package my.task.voting.repository;

import my.task.voting.model.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    // returns false if user does not exist
    boolean delete(int id);

    // returns null if user does not exist
    User get(int id);

    // returns the users list ordered by registered date desc
    List<User> getAll();

    // returns null if user does not exist
    User getByEmail(String email);
}

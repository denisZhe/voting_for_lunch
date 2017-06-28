package my.task.voting.service;

import my.task.voting.model.User;
import my.task.voting.util.ChangeUnacceptableException;
import my.task.voting.util.NotFoundException;

import java.util.List;

public interface UserService {

    User save(User user);

    void delete(int id) throws NotFoundException, ChangeUnacceptableException;

    User get(int id) throws NotFoundException;

    List<User> getAll();
}

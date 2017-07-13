package my.task.voting.service;

import my.task.voting.model.User;
import my.task.voting.util.exception.ChangeUnacceptableException;
import my.task.voting.util.exception.NotFoundException;

import java.util.List;

public interface UserService {

    User save(User user);

    void delete(int id) throws NotFoundException, ChangeUnacceptableException, IllegalArgumentException;

    User get(int id) throws NotFoundException;

    List<User> getAll();

    User getByEmail(String email) throws NotFoundException, IllegalArgumentException;
}

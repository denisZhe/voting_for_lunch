package my.task.voting.service;

import my.task.voting.model.User;
import my.task.voting.util.ChangeUnacceptableException;
import my.task.voting.util.NotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static my.task.voting.UserTestData.*;
import static org.junit.Assert.assertEquals;

public class UserServiceTest extends AbstractServiceTest{

    @Autowired
    private UserService userService;

    @Test
    public void testSave() throws Exception {
        User newUser = userService.save(getNewUser());
        List<User> expectedUsers = Stream.of(ADMIN, USER_1, USER_2, newUser)
                .sorted(Comparator.comparing(User::getRegistered).reversed())
                .collect(Collectors.toList());
        List<User> persistedUsers = userService.getAll();
        assertEquals(expectedUsers, persistedUsers);
    }

    @Test
    public void testUpdate() throws Exception {
        User updatedUser = userService.save(getUpdatedUser());
        List<User> expectedUsers = Stream.of(ADMIN, USER_2, updatedUser)
                .sorted(Comparator.comparing(User::getRegistered).reversed())
                .collect(Collectors.toList());
        List<User> persistedUsers = userService.getAll();
        assertEquals(expectedUsers, persistedUsers);
    }

    @Test
    public void testDelete() throws Exception {
        userService.delete(USER_1.getId());
        List<User> expectedUsers = Stream.of(ADMIN, USER_2)
                .sorted(Comparator.comparing(User::getRegistered).reversed())
                .collect(Collectors.toList());
        List<User> persistedUsers = userService.getAll();
        assertEquals(expectedUsers, persistedUsers);
    }

    @Test
    public void testUnacceptableChangeWhenDelete() {
        thrown.expect(ChangeUnacceptableException.class);
        thrown.expectMessage("The user has already voted and can not be deleted");
        userService.delete(USER_2.getId());
    }

    @Test
    public void testNotFoundWhenDelete() {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("The user with such id doesn't exist");
        userService.delete(getNonexistentUserId());
    }

    @Test
    public void testGet() throws Exception {
        assertEquals(USER_2, userService.get(USER_2.getId()));
    }

    @Test
    public void testNotFoundWhenGet() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("The user with such id doesn't exist");
        userService.get(getNonexistentUserId());
    }

    @Test
    public void testGetAll() throws Exception {
        List<User> expectedUsers = Stream.of(ADMIN, USER_1, USER_2)
                .sorted(Comparator.comparing(User::getRegistered).reversed())
                .collect(Collectors.toList());
        List<User> persistedUsers = userService.getAll();
        assertEquals(expectedUsers, persistedUsers);
    }

    @Test
    public void testGetByEmail() throws Exception {
        assertEquals(USER_2, userService.getByEmail(USER_2.getEmail()));
    }
}
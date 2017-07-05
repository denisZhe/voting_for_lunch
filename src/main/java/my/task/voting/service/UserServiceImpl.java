package my.task.voting.service;

import my.task.voting.model.User;
import my.task.voting.repository.UserRepository;
import my.task.voting.repository.VotesRepository;
import my.task.voting.util.ChangeUnacceptableException;
import my.task.voting.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VotesRepository votesRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(int id) throws NotFoundException, ChangeUnacceptableException {
        if (!votesRepository.getByUserId(id).isEmpty()) {
            throw new ChangeUnacceptableException("The user has already voted and can not be deleted");
        }
        if (!userRepository.delete(id)) {
            throw new NotFoundException("The user with such id doesn't exist");
        }
    }

    @Override
    public User get(int id) throws NotFoundException {
        User user = userRepository.get(id);
        if (user == null) {
            throw new NotFoundException("The user with such id doesn't exist");
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getByEmail(String email) throws NotFoundException {
        Assert.notNull(email, "Email must not be null");
        User user = userRepository.getByEmail(email);
        if (user == null) {
            throw new NotFoundException("The user with such email doesn't exist");
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("The user with such email doesn't exist");
        }
        return user;
    }
}

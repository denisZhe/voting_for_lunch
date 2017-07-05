package my.task.voting.service;

import my.task.voting.model.Lunch;
import my.task.voting.repository.LunchRepository;
import my.task.voting.repository.VotesRepository;
import my.task.voting.util.ChangeUnacceptableException;
import my.task.voting.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

@Service
public class LunchServiceImpl implements LunchService {

    @Autowired
    private LunchRepository lunchRepository;

    @Autowired
    private VotesRepository votesRepository;

    @Override
    @Transactional
    public Lunch save(Lunch lunch) throws ChangeUnacceptableException {
        Assert.notNull(lunch, "Lunch must not be null");
        if (lunch.getId() != null && !votesRepository.getByLunchId(lunch.getId()).isEmpty()) {
            throw new ChangeUnacceptableException("For lunch already voted and it can not be changed");
        } else {
            return lunchRepository.save(lunch);
        }
    }

    @Override
    @Transactional
    public void delete(int id) throws NotFoundException, ChangeUnacceptableException {
        if (!votesRepository.getByLunchId(id).isEmpty()) {
            throw new ChangeUnacceptableException("For lunch already voted and it can not be deleted");
        }
        if (!lunchRepository.delete(id)) {
            throw new NotFoundException("The lunch with such id doesn't exist");
        }
    }

    @Override
    public Lunch get(int id) throws NotFoundException {
        Lunch lunch = lunchRepository.get(id);
        if (lunch == null) {
            throw new NotFoundException("The lunch with such id doesn't exist");
        }
        return lunch;
    }

    @Override
    public List<Lunch> getAll() {
        return lunchRepository.getAll();
    }

    @Override
    public List<Lunch> getByDate(LocalDate date) {
        Assert.notNull(date, "Date must not be null");
        return lunchRepository.getByDate(date);
    }

    @Override
    public List<Lunch> getByDateWithMeals(LocalDate date) {
        Assert.notNull(date, "Date must not be null");
        return lunchRepository.getByDateWithMeals(date);
    }
}

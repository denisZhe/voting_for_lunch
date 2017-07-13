package my.task.voting.service;

import my.task.voting.model.Vote;
import my.task.voting.repository.VotesRepository;
import my.task.voting.util.exception.ChangeUnacceptableException;
import my.task.voting.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class VotingServiceImpl implements VotingService {

    private static final LocalTime DEADLINE = LocalTime.of(11, 0);

    private final VotesRepository votesRepository;

    @Autowired
    public VotingServiceImpl(VotesRepository votesRepository) {
        this.votesRepository = votesRepository;
    }

    @Override
    @Transactional
    public Vote save(Vote vote) throws ChangeUnacceptableException {
        Assert.notNull(vote, "Vote must not be null");
        if (!vote.getVotingDate().isEqual(LocalDate.now())) {
            throw new IllegalArgumentException("Creating or updating of vote is only allowed for today");
        }
        if (!vote.isNew() && votesRepository.get(vote.getId()) != null && (LocalTime.now().isAfter(DEADLINE))) {
            throw new ChangeUnacceptableException("Too late for change vote");
        }
        return votesRepository.save(vote);
    }

    @Override
    public Vote get(int id) throws NotFoundException {
        Vote vote = votesRepository.get(id);
        if (vote == null) {
            throw new NotFoundException("Vote with such id doesn't exist");
        }
        return vote;
    }

    @Override
    public List<Vote> getAll() {
        return votesRepository.getAll();
    }

    @Override
    public List<Vote> getByUserId(int userId) {
        return votesRepository.getByUserId(userId);
    }

    @Override
    public List<Vote> getByLunchId(int lunchId) {
        return votesRepository.getByLunchId(lunchId);
    }
}

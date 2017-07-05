package my.task.voting.service;

import my.task.voting.model.Vote;
import my.task.voting.repository.VotesRepository;
import my.task.voting.util.ChangeUnacceptableException;
import my.task.voting.util.NotFoundException;
import my.task.voting.util.RepeatedVoteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (LocalDate.now().isAfter(vote.getVotingDate()) ||
                vote.getId() != null && votesRepository.get(vote.getId()) != null && (LocalTime.now().isAfter(DEADLINE))) {
            throw new ChangeUnacceptableException("Too late for change vote");
        }
        Vote todayVoteForUser = votesRepository.getByUserId(vote.getUserId()).stream()
                .filter(v -> v.getVotingDate().isEqual(LocalDate.now()))
                .findAny()
                .orElse(null);
        if (vote.getId() == null && todayVoteForUser != null) {
            throw new RepeatedVoteException("Only one vote per day for the user is possible");
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

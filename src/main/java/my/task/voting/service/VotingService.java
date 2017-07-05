package my.task.voting.service;

import my.task.voting.model.Vote;
import my.task.voting.util.ChangeUnacceptableException;
import my.task.voting.util.NotFoundException;

import java.util.List;

public interface VotingService {

    Vote save(Vote vote) throws ChangeUnacceptableException;

    Vote get(int id) throws NotFoundException;

    List<Vote> getAll();

    List<Vote> getByUserId(int userId);

    List<Vote> getByLunchId(int lunchId);
}

package my.task.voting.repository;

import my.task.voting.model.Vote;

import java.util.List;

public interface VotesRepository {

    Vote save(Vote vote);

    // returns null if vote does not exist
    Vote get(int id);

    // returns the votes list ordered by date desc
    List<Vote> getAll();

    List<Vote> getByUserId(int userId);

    List<Vote> getByLunchId(int lunchId);
}

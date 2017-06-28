package my.task.voting.repository;

import my.task.voting.model.Lunch;

import java.time.LocalDate;
import java.util.List;

public interface LunchRepository {

    Lunch save(Lunch lunch);

    // returns false if lunch does not exist
    boolean delete(int id);

    // returns null if lunch does not exist
    Lunch get(int id);

    // returns the lunch list ordered by date desc
    List<Lunch> getAll();

    List<Lunch> getByDate(LocalDate date);

    List<Lunch> getByDateWithMeals(LocalDate date);
}

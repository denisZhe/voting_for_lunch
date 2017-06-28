package my.task.voting.service;

import my.task.voting.model.Lunch;
import my.task.voting.util.ChangeUnacceptableException;
import my.task.voting.util.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface LunchService {

    Lunch save(Lunch lunch) throws ChangeUnacceptableException;

    void delete(int id) throws NotFoundException, ChangeUnacceptableException;

    Lunch get(int id) throws NotFoundException;

    List<Lunch> getAll();

    List<Lunch> getByDate(LocalDate date);

    List<Lunch> getByDateWithMeals(LocalDate date);
}

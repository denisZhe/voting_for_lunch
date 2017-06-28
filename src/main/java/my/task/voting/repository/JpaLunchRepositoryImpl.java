package my.task.voting.repository;

import my.task.voting.model.Lunch;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaLunchRepositoryImpl implements LunchRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Lunch save(Lunch lunch) {
        if (lunch.isNew()) {
            em.persist(lunch);
            return lunch;
        } else {
            return em.merge(lunch);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return em.createNamedQuery(Lunch.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public Lunch get(int id) {
        return em.find(Lunch.class, id);
    }

    @Override
    public List<Lunch> getAll() {
        return em.createNamedQuery(Lunch.ALL_SORTED, Lunch.class)
                .getResultList();
    }

    @Override
    public List<Lunch> getByDate(LocalDate date) {
        return em.createNamedQuery(Lunch.GET_BY_DATE, Lunch.class)
                .setParameter("startDate", LocalDateTime.of(date, LocalTime.MIN))
                .setParameter("endDate", LocalDateTime.of(date, LocalTime.MAX))
                .getResultList();
    }

    @Override
    public List<Lunch> getByDateWithMeals(LocalDate date) {
        return em.createNamedQuery(Lunch.GET_BY_DATE_WITH_MEALS, Lunch.class)
                .setParameter("startDate", LocalDateTime.of(date, LocalTime.MIN))
                .setParameter("endDate", LocalDateTime.of(date, LocalTime.MAX))
                .getResultList();
    }
}

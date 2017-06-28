package my.task.voting.repository;

import my.task.voting.model.Vote;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaVotesRepositoryImpl implements VotesRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Vote save(Vote vote) {
        if (vote.isNew()) {
            em.persist(vote);
            return vote;
        } else {
            return em.merge(vote);
        }
    }

    @Override
    public Vote get(int id) {
        return em.find(Vote.class, id);
    }

    @Override
    public List<Vote> getAll() {
        return em.createNamedQuery(Vote.ALL_SORTED, Vote.class)
                .getResultList();
    }

    @Override
    public List<Vote> getByUserId(int userId) {
        return em.createNamedQuery(Vote.ALL_BY_USER, Vote.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Vote> getByLunchId(int lunchId) {
        return em.createNamedQuery(Vote.ALL_BY_LUNCH, Vote.class)
                .setParameter("lunchId", lunchId)
                .getResultList();
    }
}

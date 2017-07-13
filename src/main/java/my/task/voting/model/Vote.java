package my.task.voting.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NamedQueries({
        @NamedQuery(name = Vote.ALL_SORTED, query = "SELECT v FROM Vote v ORDER BY v.votingDate DESC"),
        @NamedQuery(name = Vote.ALL_BY_USER, query = "SELECT v FROM Vote v WHERE v.userId = :userId"),
        @NamedQuery(name = Vote.ALL_BY_LUNCH, query = "SELECT v FROM Vote v WHERE v.lunchId = :lunchId"),
        @NamedQuery(name = Vote.DELETE, query = "DELETE FROM Vote v WHERE v.id = :id")
})
@Entity
@Table(name = "votes")
public class Vote extends BaseEntity {

    public static final String ALL_SORTED = "Vote.getAll";
    public static final String ALL_BY_USER = "Vote.getByUserId";
    public static final String ALL_BY_LUNCH = "Vote.getByLunchId";
    public static final String DELETE = "Vote.delete";

    @Column(name = "votingDate")
    @NotNull
    private LocalDate votingDate = LocalDate.now();

    //    https://stackoverflow.com/a/6312018
    @Column(name = "userId")
    @NotNull
    private Integer userId;

    @Column(name = "lunchId")
    @NotNull
    private Integer lunchId;

    public Vote() {
    }

    public Vote(Integer id, LocalDate votingDate, Integer userId, Integer lunchId) {
        super(id);
        this.votingDate = votingDate;
        this.userId = userId;
        this.lunchId = lunchId;
    }

    public LocalDate getVotingDate() {
        return votingDate;
    }

    public void setVotingDate(LocalDate votingDate) {
        this.votingDate = votingDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLunchId() {
        return lunchId;
    }

    public void setLunchId(Integer lunchId) {
        this.lunchId = lunchId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vote)) return false;
        if (!super.equals(o)) return false;

        Vote vote = (Vote) o;

        if (getVotingDate() != null ? !getVotingDate().equals(vote.getVotingDate()) : vote.getVotingDate() != null)
            return false;
        if (getUserId() != null ? !getUserId().equals(vote.getUserId()) : vote.getUserId() != null) return false;
        return getLunchId() != null ? getLunchId().equals(vote.getLunchId()) : vote.getLunchId() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getVotingDate() != null ? getVotingDate().hashCode() : 0);
        result = 31 * result + (getUserId() != null ? getUserId().hashCode() : 0);
        result = 31 * result + (getLunchId() != null ? getLunchId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + getId() +
                ", votingDate=" + votingDate +
                ", userId=" + userId +
                ", lunchId=" + lunchId +
                '}';
    }
}

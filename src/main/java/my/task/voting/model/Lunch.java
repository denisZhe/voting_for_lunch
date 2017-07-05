package my.task.voting.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = Lunch.ALL_SORTED, query = "SELECT l FROM Lunch l ORDER BY l.created DESC"),
        @NamedQuery(name = Lunch.DELETE, query = "DELETE FROM Lunch l WHERE l.id = :id"),
        @NamedQuery(name = Lunch.GET_BY_DATE,
                query = "SELECT l FROM Lunch l WHERE l.created = :created"),
        @NamedQuery(name = Lunch.GET_BY_DATE_WITH_MEALS,
                query = "SELECT DISTINCT l FROM Lunch l LEFT JOIN FETCH l.meals WHERE l.created = :created")
})
@Entity
@Table(name = "lunches")
public class Lunch extends BaseEntity {

    public static final String ALL_SORTED = "Lunch.getAll";
    public static final String DELETE = "Lunch.delete";
    public static final String GET_BY_DATE = "Lunch.getByDate";
    public static final String GET_BY_DATE_WITH_MEALS = "Lunch.getByDateWithMeals";

    @Column(name = "created")
    @NotNull
    private LocalDate created;

    @Column(name = "restaurantName")
    @NotBlank
    private String restaurantName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lunch", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JsonManagedReference
    private List<Meal> meals;

    public Lunch() {
    }

    public Lunch(Lunch lunch) {
        this(lunch.getId(), lunch.getCreated(), lunch.getRestaurantName(), lunch.getMeals());
    }

    public Lunch(Integer id, LocalDate created, String restaurantName) {
        this(id, created, restaurantName, null);
    }

    public Lunch(Integer id, LocalDate created, String restaurantName, List<Meal> meals) {
        super(id);
        this.created = created;
        this.restaurantName = restaurantName;
        this.meals = meals;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lunch)) return false;
        if (!super.equals(o)) return false;

        Lunch lunch = (Lunch) o;

        if (!getCreated().equals(lunch.getCreated())) return false;
        return getRestaurantName().equals(lunch.getRestaurantName());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getCreated().hashCode();
        result = 31 * result + getRestaurantName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Lunch{" +
                "id=" + getId() +
                ", created=" + created +
                ", restaurantName='" + restaurantName + '\'' +
                ", meals=" + meals +
                '}';
    }
}

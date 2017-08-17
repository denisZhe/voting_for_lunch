package my.task.voting.to;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

public class LunchTO {

    private Integer id;

    private LocalDate created;

    @NotBlank
    private String restaurantName;

    @Valid
    private List<MealTO> meals;

    public LunchTO() {
    }

    public LunchTO(Integer id, LocalDate created, String restaurantName) {
        this.id = id;
        this.created = created;
        this.restaurantName = restaurantName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<MealTO> getMeals() {
        return meals;
    }

    public void setMeals(List<MealTO> meals) {
        this.meals = meals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LunchTO)) return false;

        LunchTO lunchTO = (LunchTO) o;

        if (getId() != null ? !getId().equals(lunchTO.getId()) : lunchTO.getId() != null) return false;
        if (getRestaurantName() != null ? !getRestaurantName().equals(lunchTO.getRestaurantName()) : lunchTO.getRestaurantName() != null)
            return false;
        return getMeals() != null ? getMeals().equals(lunchTO.getMeals()) : lunchTO.getMeals() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getRestaurantName() != null ? getRestaurantName().hashCode() : 0);
        result = 31 * result + (getMeals() != null ? getMeals().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LunchTO{" +
                "id=" + id +
                ", restaurantName='" + restaurantName + '\'' +
                ", meals=" + meals +
                '}';
    }
}

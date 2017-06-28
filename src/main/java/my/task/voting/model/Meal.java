package my.task.voting.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "meals")
public class Meal extends BaseEntity {

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "dishName")
    @NotBlank
    private String dishName;

    @Column(name = "price")
    @NotNull
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lunchId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Lunch lunch;

    public Meal() {
    }

    public Meal(Integer id, LocalDateTime created, String dishName, int price, Lunch lunch) {
        super(id);
        this.created = created;
        this.dishName = dishName;
        this.price = price;
        this.lunch = lunch;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Lunch getLunch() {
        return lunch;
    }

    public void setLunch(Lunch lunch) {
        this.lunch = lunch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meal)) return false;
        if (!super.equals(o)) return false;

        Meal meal = (Meal) o;

        if (getPrice() != meal.getPrice()) return false;
        if (!getCreated().equals(meal.getCreated())) return false;
        if (!getDishName().equals(meal.getDishName())) return false;
        return getLunch().equals(meal.getLunch());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getCreated().hashCode();
        result = 31 * result + getDishName().hashCode();
        result = 31 * result + getPrice();
        result = 31 * result + getLunch().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + getId() +
                ", created=" + created +
                ", dishName='" + dishName + '\'' +
                ", price=" + price +
                '}';
    }
}

package my.task.voting.to;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class MealTO {

    private Integer id;

    private LocalDate created;

    @NotBlank
    private String dishName;

    @NotNull
    private Integer price;

    public MealTO() {
    }

    public MealTO(Integer id, LocalDate created, String dishName, Integer price) {
        this.id = id;
        this.created = created;
        this.dishName = dishName;
        this.price = price;
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

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MealTO)) return false;

        MealTO mealTO = (MealTO) o;

        return getDishName() != null ? getDishName().equals(mealTO.getDishName()) : mealTO.getDishName() == null;
    }

    @Override
    public int hashCode() {
        return getDishName() != null ? getDishName().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MealTO{" +
                "id=" + id +
                ", dishName='" + dishName + '\'' +
                ", price=" + price +
                '}';
    }
}

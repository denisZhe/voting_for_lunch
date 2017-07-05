package my.task.voting.to;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class MealTO {

    private Integer id;

    @NotEmpty
    private String dishName;

    @NotNull
    private Integer price;

    public MealTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

        return getDishName().equals(mealTO.getDishName());
    }

    @Override
    public int hashCode() {
        return getDishName().hashCode();
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

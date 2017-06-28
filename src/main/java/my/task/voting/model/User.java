package my.task.voting.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NamedQueries({
        @NamedQuery(name = User.ALL_SORTED, query = "SELECT u FROM User u ORDER BY u.registered DESC"),
        @NamedQuery(name = User.DELETE, query = "DELETE FROM User u WHERE u.id = :id")
})
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    public static final String ALL_SORTED = "User.getAll";
    public static final String DELETE = "User.delete";

    @Column(name = "registered")
    private LocalDateTime registered;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Type type;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "email")
    @NotBlank
    @Email
    private String email;

    @Column(name = "password")
    @NotBlank
    @Length(min = 6)
    private String password;

    public User() {
    }

    public User(User user) {
        this(user.getId(), user.registered, user.getType(), user.getName(), user.getEmail(), user.getPassword());
    }

    public User(Integer id, LocalDateTime registered, Type type, String name, String email, String password) {
        super(id);
        this.registered = registered;
        this.type = type;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        if (!getRegistered().equals(user.getRegistered())) return false;
        if (getType() != user.getType()) return false;
        if (!getName().equals(user.getName())) return false;
        return getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getRegistered().hashCode();
        result = 31 * result + getType().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getEmail().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "registered=" + registered +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

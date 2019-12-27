package entities;

import util.PasswordManager;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@MappedSuperclass
public abstract class User implements Serializable {
    @Id
    protected String username;

    @Version
    private int version;

    @NotNull
    protected String password;

    @NotNull
    protected String name;

    @NotNull
    @Email
    protected String email;

    protected LocalDate birthday;

    @Version
    private int version;

    public User() {
    }

    public User(String username, String password, String name, String email, LocalDate birthday) {
        this.username = username;
        this.password = hashPassword(password);
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    public static String hashPassword(String password) {
        return PasswordManager.hashPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return username;
    }
}

package dtos;

import java.io.Serializable;

public abstract class UserDTO implements Serializable {
    protected String username;

    protected String password;

    protected String name;

    protected String email;

    private String birthday;

    public UserDTO() {
    }

    public UserDTO(String username, String name, String email, String birthday) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
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
        this.password = password;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}

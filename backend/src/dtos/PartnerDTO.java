package dtos;

public class PartnerDTO extends UserDTO {
    private String username;

    private String password;

    private String name;

    private String email;

    private String birthday;

    public PartnerDTO(String username, String name, String email, String birthday) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    public PartnerDTO() {
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
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

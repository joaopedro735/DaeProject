package entities;

import util.PasswordManager;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@MappedSuperclass
public abstract class User implements Serializable {
    @Id
    protected String username;

    @NotNull
    protected String password;

    @NotNull
    protected String name;

    @NotNull
    @Email
    protected String email;

    public User() {
    }

    public User(String username, String password, String name, String email) {
        this.username = username;
        this.password = hashPassword(password);
        this.name = name;
        this.email = email;
    }

    public static String hashPassword(String password) {
//        String encoded = null;
//
//        try {
//            ByteBuffer passwdBuffer =
//                    Charset.defaultCharset().encode(CharBuffer.wrap(password));
//            byte[] passwdBytes = passwdBuffer.array();
//            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
//            encoded = argon2.hash(12, 65536, 1, passwdBytes);
//            boolean success1 = argon2.verify(encoded, passwdBytes);
//            System.out.println(success1 ? "Success" : "Failure");
//        } catch (Exception ex) {
//            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return encoded;
        //return PasswordManager.hashPassword(password.toCharArray());
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

    @Override
    public String toString() {
        return username;
    }
}

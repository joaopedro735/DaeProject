package ejbs;

import entities.User;
import util.PasswordManager;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "UserEJB")
public class UserBean {
    @PersistenceContext
    private EntityManager em;

    public UserBean() {
    }

    public User authenticate(final String username, final String password) throws Exception {
        User user = em.find(User.class, username);
        //System.out.println("1:" + user.getPassword().equals(User.hashPassword(password)));
        //System.out.println("2:" + PasswordManager.verifyPassword(user.getPassword(), password.toCharArray()));
        if (user != null && PasswordManager.verifyPassword(user.getPassword(), password)) {
            return user;
        }
        throw new Exception("Failed logging in with username '" + username + "': unknown username or wrong password");
    }
}

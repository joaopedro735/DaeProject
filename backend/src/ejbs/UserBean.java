package ejbs;

import entities.Administrator;
import entities.User;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;
import util.PasswordManager;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

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

    public void remove(String username) {
        try {
            User user = em.find(User.class, username);
            if (user == null) {
                throw new MyEntityNotFoundException("User with username '" + username + "' not found.");
            }
            if(user!=null) {
                em.remove(user);
            }
        } catch(Exception e){
            throw new EJBException("ERROR_REMOVING_USER", e);
        }
    }

    public User find(String username) {
        try {
            return em.find(User.class, username);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_USER", e);
        }
    }
}

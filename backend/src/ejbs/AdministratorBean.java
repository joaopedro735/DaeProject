package ejbs;

import entities.Administrator;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless(name = "AdministradorEJB")
public class AdministratorBean {
    @PersistenceContext
    private EntityManager em;


    public AdministratorBean() {
    }

    public Administrator create(String username, String password, String nome, String email) throws MyEntityAlreadyExistsException, MyConstraintViolationException {
        if (find(username) != null) {
            throw new MyEntityAlreadyExistsException("Username '" + username + "' already exists");
        }

        try {
            Administrator admin = new Administrator(username, password, nome, email);
            em.persist(admin);
            return admin;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public void remove(String username){
        try {
            Administrator administrator = em.find(Administrator.class, username);
            if (administrator == null) {
                throw new MyEntityNotFoundException("Administrator with username '" + username + "' not found.");
            }
            if(administrator!=null) {
                em.remove(administrator);
            }
        } catch(Exception e){
            throw new EJBException("ERROR_REMOVING_ADMINISTRATOR", e);
        }
    }

    public Administrator update(String username, String password, String name, String email) throws MyEntityNotFoundException {
        try {
            Administrator administrator = em.find(Administrator.class, username);

            if (administrator == null) {
                throw new MyEntityNotFoundException("Administrator with username '" + username + "' not found.");
            }
            em.lock(administrator, LockModeType.OPTIMISTIC);
            administrator.setPassword(password);
            administrator.setName(name);
            administrator.setEmail(email);
            em.merge(administrator);
            return administrator;
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_ADMINISTRATOR", e);
        }
    }

    public List<Administrator> all() {
        try {
            return (List<Administrator>) em.createNamedQuery("getAllAdministrators").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_ADMINISTRATORS", e);
        }
    }

    public Administrator find(String username) {
        try {
            return em.find(Administrator.class, username);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_ADMINISTRATOR", e);
        }
    }
}

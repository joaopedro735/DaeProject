package ejbs;

import entities.Athlete;
import entities.Type;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless(name = "TypeEJB")
public class TypeBean {
    @PersistenceContext
    private EntityManager em;

    public TypeBean() {
    }

    public Type create(String name) throws MyEntityAlreadyExistsException, MyConstraintViolationException {
        if (find(name) != null) {
            throw new MyEntityAlreadyExistsException("Type named '" + name + "' already exists");
        }

        try {
            Type type = new Type(name);
            em.persist(type);
            return type;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public List<Athlete> all() {
        try {
            return (List<Athlete>) em.createNamedQuery("getAllAthletes").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_ATHLETES", e);
        }
    }

    public Athlete update(String username, String password, String name, String email) throws MyEntityNotFoundException {
        try {
            Athlete athlete = em.find(Athlete.class, username);
            if (athlete == null) {
                throw new MyEntityNotFoundException("Athlete with username '" + username + "' not found.");
            }

            em.lock(athlete, LockModeType.OPTIMISTIC);
            athlete.setPassword(password);
            athlete.setName(name);
            athlete.setEmail(email);

            em.merge(athlete);
            return athlete;
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_ATHLETE", e);
        }
    }

    //TODO REMOVE (SOFT DELETE)
    public void remove(String username){
        try {
            Athlete athlete = find(username);
            System.out.println(athlete.getUsername());
            if (athlete == null) {
                throw new MyEntityNotFoundException("Athlete with username '" + username + "' not found.");
            }

            if(athlete!=null) {
                em.remove(athlete);
            }
        } catch(Exception e){
            throw new EJBException("ERROR_REMOVING_ATHLETE", e);
        }
    }

    public Athlete find(String username) {
        try {
            return em.find(Athlete.class, username);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_ATHLETE", e);
        }
    }
}

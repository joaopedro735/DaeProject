package ejbs;

import entities.Administrator;
import entities.Athlete;
import entities.Partner;
import entities.Sport;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless(name = "AthleteEJB")
public class AthleteBean {
    //TODO: Composition??
    @PersistenceContext
    private EntityManager em;

    @EJB
    private SportBean sportBean;


    public AthleteBean() {
    }

    public Athlete create(String username, String password, String nome, String email, String birthday) throws MyEntityAlreadyExistsException, MyConstraintViolationException {
        if (find(username) != null) {
            throw new MyEntityAlreadyExistsException("Username '" + username + "' already exists");
        }

        try {
            DateTimeFormatter birthdayFormat= DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate birthdayDate = LocalDate.parse(birthday, birthdayFormat);
            Athlete athlete = new Athlete(username, password, nome, email,birthdayDate);
            em.persist(athlete);
            return athlete;
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

    public Athlete update(String username, String password, String name, String email, String birthday) throws MyEntityNotFoundException {
        try {
            Athlete athlete = em.find(Athlete.class, username);
            if (athlete == null) {
                throw new MyEntityNotFoundException("Athlete with username '" + username + "' not found.");
            }

            DateTimeFormatter birthdayFormat= DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate birthdayDate = LocalDate.parse(birthday, birthdayFormat);
            em.lock(athlete, LockModeType.OPTIMISTIC);
            athlete.setPassword(password);
            athlete.setName(name);
            athlete.setEmail(email);
            athlete.setBirthday(birthdayDate);

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

    public List<Athlete> findBySearch(String toSearch) {
        try {
            return (List<Athlete>) em.createNamedQuery("getAthletesByNameSearch").setParameter("name", "%" + toSearch + "%").getResultList();
        }catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_ATHLETES_BY_NAME_SEARCH", e);
        }
    }
}

package ejbs;

import entities.Athlete;
import entities.Sport;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.Utils;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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

    public Athlete find(String username) {
        try {
            return em.find(Athlete.class, username);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_ATHLETE", e);
        }
    }

}

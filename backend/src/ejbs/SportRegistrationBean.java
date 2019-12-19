package ejbs;

import entities.Athlete;
import entities.SportRegistration;
import entities.Sport;
import entities.TimeTable;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.Collection;

@Stateless(name = "PracticedSportEJB")
public class SportRegistrationBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    AthleteBean athleteBean;

    @EJB
    SportBean sportBean;



    public SportRegistrationBean() {
    }

    public SportRegistration create(String athleteUsername, int sportCode, Collection<TimeTable> timeTables) throws MyEntityAlreadyExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        /*TODO: chefk if exists
        if (find(username) != null) {
            throw new MyEntityAlreadyExistsException("Username '" + username + "' already exists");
        }*/
        try {
            Sport sport = sportBean.find(sportCode);
            boolean timeTableExists = sport.timeTablesExists(timeTables);
            System.out.println(sport.getName());
            System.out.println(timeTables.stream().findFirst().get().getDay());
            System.out.println(timeTableExists);
            if (!timeTableExists) {
                throw new MyEntityNotFoundException("Selected time tables doesn't belong to '" + sport.getName() + "'");
            }
            Athlete athlete = athleteBean.find(athleteUsername);
            if (athlete == null) {
                throw new MyEntityNotFoundException("Athlete with username '" + athleteUsername + "' not found.");
            }

            SportRegistration sportRegistration = new SportRegistration();
            sportRegistration.setSport(sport);
            sportRegistration.setAthlete(athlete);
            sportRegistration.addTimeTables(timeTables);
            em.persist(sportRegistration);
            return sportRegistration;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }
}

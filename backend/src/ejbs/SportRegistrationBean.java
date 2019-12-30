package ejbs;

import entities.Athlete;
import entities.Sport;
import entities.SportRegistration;
import entities.TimeTable;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.List;

@Stateless(name = "PracticedSportEJB")
public class SportRegistrationBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    AthleteBean athleteBean;
    @EJB
    SportBean sportBean;

    @EJB
    ProductBean productBean;

    @EJB
    SportSubscriptionPriceListBean sportSubscriptionPriceListBean;



    public SportRegistrationBean() {
    }

    public SportRegistration create(String athleteUsername, int sportCode, Collection<TimeTable> timeTables) throws MyEntityAlreadyExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        //TODO: check if exists
        if (find(sportCode, athleteUsername) != null) {
            throw new MyEntityAlreadyExistsException("Atlhete '" + athleteUsername + "' already is signed up on the selected sport");
        }
        try {
            Sport sport = sportBean.find(sportCode);
            boolean timeTableExists = sport.timeTablesExists(timeTables);
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


            float value = sportSubscriptionPriceListBean.findValue(sportCode);
            //TODO subsitutir id 6 para alterar dinâmicamente
            productBean.create(6, "Registration of Athlete: " + athleteUsername + "in sport: " + sportRegistration.getSport().getName(),
                   value, sportRegistration.getId(), SportRegistration.class.getName());
            //TODO Criar Purchase!


            return sportRegistration;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    public List<SportRegistration> all() {
        try {
            return (List<SportRegistration>) em.createNamedQuery("SportRegistration.getAllSportRegistration").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_SPORT_REGISTRATIONS", e);
        }
    }

    public SportRegistration find(int id) {
        try {
            return em.find(SportRegistration.class, id);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_SPORT_REGISTRATION", e);
        }
    }

    public SportRegistration find(int code, String athleteUsername) {
        try {
            System.out.println(code + athleteUsername);
            return (SportRegistration) em.createNamedQuery("getSportRegistrationByUsernameAndSportCode").setParameter("username", athleteUsername).setParameter("code", code).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_SPORT_REGISTRATION", e);
        }
    }

    public void remove(int code) {
        try {
            SportRegistration sportRegistration = find(code);
            if (sportRegistration == null) {
                throw new MyEntityNotFoundException("Sport Registration with code '" + code + "' not found.");
            }
            em.remove(sportRegistration);
        } catch (Exception e) {
            throw new EJBException("ERROR_REMOVING_SPORT_REGISTRATION ---->" + e.getMessage(), e);
        }
    }
}

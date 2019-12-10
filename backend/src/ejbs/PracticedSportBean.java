package ejbs;

import entities.Administrator;
import entities.Athlete;
import entities.PracticedSport;
import entities.Sport;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.Utils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless(name = "PracticedSportEJB")
public class PracticedSportBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    AthleteBean athleteBean;

    @EJB
    SportBean sportBean;



    public PracticedSportBean() {
    }

    public PracticedSport create(String athleteUsername, int sportCode) throws MyEntityAlreadyExistsException, MyConstraintViolationException {
        /*TODO: chefk if exists
        if (find(username) != null) {
            throw new MyEntityAlreadyExistsException("Username '" + username + "' already exists");
        }*/
        Sport sport = sportBean.find(sportCode);
        Athlete athlete = athleteBean.find(athleteUsername);

        try {
            PracticedSport practicedSport = new PracticedSport(athlete, sport);
            em.persist(practicedSport);
            return practicedSport;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }
}

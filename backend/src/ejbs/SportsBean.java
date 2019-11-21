package ejbs;

import entities.Sport;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.Utils;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless(name = "SportsEJB")
public class SportsBean {
    @PersistenceContext
    private EntityManager em;

    public SportsBean() {
    }

    public Sport create(String name) throws MyEntityAlreadyExistsException, MyConstraintViolationException {
        try {
            Sport sport = new Sport();
            sport.setName(name);
            em.persist(sport);
            return sport;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public Sport find(int code) {
        try {
            return em.find(Sport.class, code);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_SPORTS", e);
        }
    }
}

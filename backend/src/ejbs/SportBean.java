package ejbs;

import entities.Athlete;
import entities.Partner;
import entities.Sport;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.Utils;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless(name = "SportEJB")
public class SportBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    AthleteBean athleteBean;

    @EJB
    PartnerBean partnerBean;

    public SportBean() {
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

    public void enrollPartner(String username, int sportsCode) {
        try {
            Partner partner = partnerBean.find(username);
            Sport sport = find(sportsCode);

            sport.addPartner(partner);
            partner.addSport(sport);
        } catch (Exception e) {
            throw new EJBException("ERROR_ENROLL_PARTNER", e);
        }
    }

    public void enrollAthlete(String username, int sportsCode) {
        try {
            Athlete athlete = athleteBean.find(username);
            Sport sport = find(sportsCode);

            sport.addAthlete(athlete);
            athlete.addAthleteSport(sport);
        } catch (Exception e) {
            throw new EJBException("ERROR_ENROLL_ATHLETE", e);
        }
    }
}

package ejbs;

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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless(name = "PartnerEJB")
public class PartnerBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private SportBean sportBean;

    public PartnerBean() {
    }

    public Partner create(String username, String password, String nome, String email, String birthday) throws MyEntityAlreadyExistsException, MyConstraintViolationException {
        if (find(username) != null) {
            throw new MyEntityAlreadyExistsException("Username '" + username + "' already exists");
        }

        try {
            DateTimeFormatter birthdayFormat= DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate birthdayDate = LocalDate.parse(birthday,birthdayFormat);
            Partner partner = new Partner(username, password, nome, email, birthdayDate);
            em.persist(partner);
            return partner;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public List<Partner> all() {
        try {
            return (List<Partner>) em.createNamedQuery("getAllPartners").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_PARTNERS", e);
        }
    }

    public Partner find(String username) {
        try {
            return em.find(Partner.class, username);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_PARTNER", e);
        }
    }

    public void enroll(String username, int sportsCode) {
        try {
            Partner partner = find(username);
            Sport sport = sportBean.find(sportsCode);

            sport.addPartner(partner);
            partner.addSport(sport);
        } catch (Exception e) {
            throw new EJBException("ERROR_ENROLL_STUDENT", e);
        }
    }
}

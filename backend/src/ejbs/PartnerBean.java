package ejbs;

import entities.Partner;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.Utils;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless(name = "PartnerEJB")
public class PartnerBean {
    @PersistenceContext
    private EntityManager em;

    public PartnerBean() {
    }

    public Partner create(String username, String password, String nome, String email) throws MyEntityAlreadyExistsException, MyConstraintViolationException {
        if (find(username) != null) {
            throw new MyEntityAlreadyExistsException("Username '" + username + "' already exists");
        }

        try {
            Partner partner = new Partner(username, password, nome, email);
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
}

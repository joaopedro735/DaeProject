package ejbs;

import entities.Administrator;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "AdministradorEJB")
public class AdministratorBean {
    @PersistenceContext
    private EntityManager em;

    public AdministratorBean() {
    }

    public Administrator create(String username, String password, String nome, String email) {
        Administrator admin = new Administrator(username, password, nome, email);
        em.persist(admin);
        return admin;
    }

    public List<Administrator> all() {
        try {
            return (List<Administrator>) em.createNamedQuery("getAllAdministrators").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_STUDENTS", e);
        }
    }
}

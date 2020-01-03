package ejbs;

import entities.Graduation;
import entities.Sport;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless(name = "GraduationEJB")
public class GraduationBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private SportBean sportBean;

    public GraduationBean() {
    }

    public Graduation create(String name, int sportCode) throws MyEntityAlreadyExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        try {
            Sport sport = sportBean.find(sportCode);
            if (sport == null) {
                throw new MyEntityNotFoundException("Sport with code '" + sportCode + "' not found");
            }
            Graduation graduation = new Graduation();
            graduation.setName(name);
            graduation.setSport(sport);
            if (sport.graduationExists(graduation)) {
                throw new MyEntityAlreadyExistsException("Graduation with name '" + name + "' already exists in sport '" + sport.getName() + "'");
            }
            sport.addGraduation(graduation);
            em.persist(graduation);
            return graduation;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw e;
        }
    }

    public Graduation find(int id) {
        try {
            return em.find(Graduation.class, id);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_GRADUATION", e);
        }
    }
}

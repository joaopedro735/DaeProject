package ejbs;

import entities.Rank;
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

@Stateless(name = "RankEJB")
public class RankBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private SportBean sportBean;

    public RankBean() {
    }

    public Rank create(String name, int sportCode) throws MyEntityAlreadyExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        try {
            Sport sport = sportBean.find(sportCode);
            if (sport == null) {
                throw new MyEntityNotFoundException("Sport with code '" + sportCode + "' not found");
            }
            //todo: verify if rank already exists in
            Rank rank = new Rank();
            rank.setName(name);
            rank.setSport(sport);
            sport.addRank(rank);
            em.persist(rank);
            return rank;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public Rank find(int id) {
        try {
            return em.find(Rank.class, id);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_RANK", e);
        }
    }
}

package ejbs;

import entities.Sport;
import entities.SubscriptionPriceListSport;
import entities.Trainer;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Stateless(name = "SportSubscriptionPriceListEJB")
public class SportSubscriptionPriceListBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    SportBean sportBean;

    public SportSubscriptionPriceListBean() {
    }

    //TODO Create get all

    public SubscriptionPriceListSport create(int sportCode, float price) throws MyEntityNotFoundException, MyConstraintViolationException {
        try {
            Sport sport = sportBean.find(sportCode);
            if(sport == null)
                throw new MyEntityNotFoundException("Can't find sport with id '" + sportCode + "'");

            SubscriptionPriceListSport subscriptionPriceListSport = new SubscriptionPriceListSport(sport, price);
            em.persist(subscriptionPriceListSport);
            return subscriptionPriceListSport;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public SubscriptionPriceListSport update(int sportCode, float price) throws MyEntityNotFoundException, MyConstraintViolationException {
        try {
            Sport sport = sportBean.find(sportCode);
            if(sport == null)
                throw new MyEntityNotFoundException("Can't find sport with id '" + sportCode + "'");
            SubscriptionPriceListSport subscriptionPriceListSport = em.find(SubscriptionPriceListSport.class, sportCode);
            if (subscriptionPriceListSport == null)
                throw new MyEntityNotFoundException("Can't find sportSubscriptionPriceListBean with id '" + sportCode + "'");

            em.lock(subscriptionPriceListSport, LockModeType.OPTIMISTIC);
            subscriptionPriceListSport.setPrice(price);
            subscriptionPriceListSport.setSport(sport);
            em.merge(subscriptionPriceListSport);

            return subscriptionPriceListSport;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public void remove(int subscriptionPriceLS) throws MyEntityNotFoundException {
        try {
            SubscriptionPriceListSport subscriptionPriceListSport = em.find(SubscriptionPriceListSport.class, subscriptionPriceLS);
            if(subscriptionPriceListSport == null){
                throw new MyEntityNotFoundException("Entity Not found");
            }
            em.remove(subscriptionPriceLS);

        }catch (ConstraintViolationException e){
            throw new EJBException("SubscriptionPriceListSport not found: " + e.getMessage());
        }
    }
    public float findValue(int sportCode) throws MyConstraintViolationException, MyEntityNotFoundException {
        try {
            SubscriptionPriceListSport subscriptionPriceListSport = em.find(SubscriptionPriceListSport.class, sportCode);
            if(subscriptionPriceListSport == null)
                throw new MyEntityNotFoundException("Entity of sportSubscriptionPriceList not found");

            return subscriptionPriceListSport.getPrice();

        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }
}

package ejbs;

import entities.Sport;
import entities.SubscriptionPriceListSport;
import entities.Trainer;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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

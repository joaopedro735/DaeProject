package ejbs;

import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@Stateless(name = "PurchaseBeanEJB")
public class PurchaseBean {
    @PersistenceContext
    private EntityManager em;

    public PurchaseBean() {
    }

    public List<Payment> all() {
        try {
            return (List<Payment>) em.createNamedQuery("getAllPurchases").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_PURCHASES", e);
        }
    }

    public Purchase create(Set<ProductPurchase> productPurchases, User user, float totalEuros) throws MyConstraintViolationException {
        try {
            Purchase purchase = new Purchase(productPurchases, totalEuros, user);
            em.persist(purchase);
            return purchase;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public Purchase update(int id, Set<Payment> payments, Set<ProductPurchase> productPurchases, User user, float totalEuros) throws MyConstraintViolationException, MyEntityNotFoundException {
        try{
            Purchase purchase= em.find(Purchase.class, id);
            if(purchase == null){
                throw new MyEntityNotFoundException("Purchase entity with id " + id + " not found");
            }

            em.lock(purchase, LockModeType.OPTIMISTIC);
            purchase.setPaymentList(payments);
            purchase.setProductPurchases(productPurchases);
            purchase.setUser(user);
            purchase.setTotalEuros(totalEuros);
            em.merge(purchase);

            return purchase;
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public void remove (int id) throws MyEntityNotFoundException, MyConstraintViolationException {
        try{
            Purchase purchase = em.find(Purchase.class, id);
            if(purchase == null){
                throw new MyEntityNotFoundException("Purchase entity with id " + id + " not found");
            }
            //TODO SEE IF IT HAS RELATIONS AND THEN IF NOT REMOVE IT
            em.remove(purchase);

        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }
}

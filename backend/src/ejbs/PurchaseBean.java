package ejbs;

import entities.*;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Stateless(name = "PurchaseBeanEJB")
public class PurchaseBean {
    @EJB
    UserBean userBean;
    @PersistenceContext
    private EntityManager em;

    public PurchaseBean() {
    }

    public List<Purchase> all() {
        try {
            return (List<Purchase>) em.createNamedQuery("getAllPurchases").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_PURCHASES", e);
        }
    }

    public Purchase create(Set<ProductPurchase> productPurchases, String username) throws MyEntityNotFoundException, MyConstraintViolationException {
        try {
            User user = userBean.find(username);
            if (user == null) {
                throw new MyEntityNotFoundException("Username + " + username + " not found!");
            }
            BigDecimal totalEmEuros = productPurchases.stream()
                    .map(ProductPurchase::total)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            System.out.println("valoremeuros:" + totalEmEuros);
//            Purchase purchase = new Purchase();
//            purchase.setUser(user);
//            purchase.addProductPurchases(productPurchases);
//            purchase.setTotalEuros(totalEmEuros);
            Purchase purchase = new Purchase(totalEmEuros, user);
            purchase.addProductPurchases(productPurchases);
            em.persist(purchase);
            return purchase;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public Purchase update(int id, Set<Payment> payments, Set<ProductPurchase> productPurchases, User user, BigDecimal totalEuros) throws MyConstraintViolationException, MyEntityNotFoundException {
        try {
            Purchase purchase = em.find(Purchase.class, id);
            if (purchase == null) {
                throw new MyEntityNotFoundException("Purchase entity with id " + id + " not found");
            }

            em.lock(purchase, LockModeType.OPTIMISTIC);
            purchase.setPaymentList(payments);
            purchase.setProductPurchases(productPurchases);
            purchase.setUser(user);
            purchase.setTotalEuros(totalEuros);
            em.merge(purchase);

            return purchase;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public void remove(int id) throws MyEntityNotFoundException, MyConstraintViolationException {
        try {
            Purchase purchase = em.find(Purchase.class, id);
            if (purchase == null) {
                throw new MyEntityNotFoundException("Purchase entity with id " + id + " not found");
            }
            //TODO SEE IF IT HAS RELATIONS AND THEN IF NOT REMOVE IT
            em.remove(purchase);

        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public Purchase find(int id) {
        try {
            return em.find(Purchase.class, id);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_PURCHASE", e);
        }
    }
}

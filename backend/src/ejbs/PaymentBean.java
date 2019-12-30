package ejbs;

import entities.Payment;
import entities.Product;
import entities.ProductPurchase;
import entities.State;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

@Stateless(name = "PaymentEJB")
public class PaymentBean {
    @PersistenceContext
    private EntityManager em;

    public PaymentBean() {
    }

    public List<Payment> all() {
        try {
            return (List<Payment>) em.createNamedQuery("getAllPayments").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_PAYMENTS", e);
        }
    }

    public Payment create(LocalDate paymentDate, State state, LocalDate limiteDate, String paymentMethod) throws MyConstraintViolationException {
        try {
            Payment payment = new Payment(paymentDate, limiteDate, state, paymentMethod);
            em.persist(payment);
            return payment;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public Payment update(int id, LocalDate paymentDate, State state, LocalDate limiteDate, String paymentMethod) throws MyConstraintViolationException, MyEntityNotFoundException {
        try{
            Payment payment = em.find(Payment.class, id);
            if(payment == null){
                throw new MyEntityNotFoundException("Payment entity with id " + id + " not found");
            }

            em.lock(payment, LockModeType.OPTIMISTIC);
            payment.setDatePayment(paymentDate);
            payment.setLimitDayPayment(limiteDate);
            payment.setState(state);
            payment.setPaymentMethod(paymentMethod);
            em.merge(payment);

            return payment;
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public void remove (int id) throws MyEntityNotFoundException, MyConstraintViolationException {
        try{
            Payment payment = em.find(Payment.class, id);
            if(payment == null){
                throw new MyEntityNotFoundException("Payment entity with id " + id + " not found");
            }
            //TODO SEE IF IT HAS RELATIONS AND THEN IF NOT REMOVE IT
            em.remove(payment);

        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }
}

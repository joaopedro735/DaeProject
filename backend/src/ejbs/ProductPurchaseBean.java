package ejbs;

import entities.Athlete;
import entities.Payment;
import entities.Product;
import entities.ProductPurchase;
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

@Stateless(name = "ProductPurchaseEJB")
public class ProductPurchaseBean {
    @PersistenceContext
    private EntityManager em;

    public ProductPurchaseBean() {
    }

    public List<Payment> all() {
        try {
            return (List<Payment>) em.createNamedQuery("getAllProductPurchases").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_PRODUCTS_PURCHASES", e);
        }
    }

    public ProductPurchase create(Product product, String unity, int quantity) throws MyConstraintViolationException {
        try {
            ProductPurchase productPurchase = new ProductPurchase(product, unity, quantity);
            em.persist(productPurchase);
            return productPurchase;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public ProductPurchase update(int id, String unity, int quantity, Product product) throws MyConstraintViolationException, MyEntityNotFoundException {
            try{
                ProductPurchase productPurchase = em.find(ProductPurchase.class, id);
                if(productPurchase == null){
                    throw new MyEntityNotFoundException("ProductPurchase entity with id " + id + " not found");
                }

                em.lock(productPurchase, LockModeType.OPTIMISTIC);
                productPurchase.setProduct(product);
                productPurchase.setQuantity(quantity);
                productPurchase.setUnity(unity);
                em.merge(productPurchase);

                return productPurchase;
            }catch (ConstraintViolationException e){
                throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
            }
    }

    public void remove (int id) throws MyEntityNotFoundException, MyConstraintViolationException {
        try{
            ProductPurchase productPurchase = em.find(ProductPurchase.class, id);
            if(productPurchase == null){
                throw new MyEntityNotFoundException("ProductPurchase entity with id " + id + " not found");
            }
            //TODO SEE IF IT HAS RELATIONS AND THEN IF NOT REMOVE IT
            em.remove(productPurchase);

        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

}

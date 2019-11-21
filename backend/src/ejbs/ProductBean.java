package ejbs;

import entities.Product;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless(name = "ProductEJB")
public class ProductBean {
    @PersistenceContext
    private EntityManager em;

    public ProductBean() {
    }

    public Product create(String type, String description, float value) throws MyEntityAlreadyExistsException, MyConstraintViolationException {
        if (find(type) != null) {
            throw new MyEntityAlreadyExistsException("Type '" + type + "' already exists");
        }

        try {
            Product product = new Product(type, description, value);
            em.persist(product);
            return product;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public void remove(String type){
        try {
            Product product = em.find(Product.class, type);
            if (product == null) {
                throw new MyEntityNotFoundException("Product with type'" + type+ "' not found.");
            }
            if(product!=null) {
                em.remove(product);
            }
        } catch(Exception e){
            throw new EJBException("ERROR_REMOVING_PRODUCT", e);
        }
    }

    public Product update(String type, String description, float value) throws MyEntityNotFoundException {
        try {
            Product product = em.find(Product.class, type);

            if (product == null) {
                throw new MyEntityNotFoundException("Product with type '" + type + "' not found.");
            }
            em.lock(product, LockModeType.OPTIMISTIC);
            product.setType(type);
            product.setDescription(description);
            product.setValue(value);
            em.merge(product);
            return product;
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_PRODUCT", e);
        }
    }

    public List<Product> all() {
        try {
            return (List<Product>) em.createNamedQuery("getAllProducts").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_PRODUCTS", e);
        }
    }

    public Product find(String type) {
        try {
            return em.find(Product.class, type);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_PRODUCT", e);
        }
    }

}

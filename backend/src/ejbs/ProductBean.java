package ejbs;

import entities.Product;
import entities.Type;
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

    public Product create(int typeCode, String description, float value) throws MyEntityAlreadyExistsException, MyConstraintViolationException {
        //TODO:
        /*if (find(type) != null) {
            throw new MyEntityAlreadyExistsException("Type '" + type + "' already exists");
        }*/
        Type type = new Type(); // = find()
        try {
            Product product = new Product(type, description, value);
            em.persist(product);
            return product;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public void remove(int id){
        try {
            Product product = em.find(Product.class, id);
            if (product == null) {
                throw new MyEntityNotFoundException("Product with id'" + id + "' not found.");
            }
            em.remove(product);
        } catch(Exception e){
            throw new EJBException("ERROR_REMOVING_PRODUCT", e);
        }
    }

    public Product update(int id,String typeCode, String description, float value) throws MyEntityNotFoundException {
        try {
            Product product = em.find(Product.class, id);

            if (product == null) {
                throw new MyEntityNotFoundException("Product with id '" + id + "' not found.");
            }
            Type type = new Type();//TODO: find type and check if exists
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

    public Product find(int id) {
        try {
            return em.find(Product.class, id);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_PRODUCT", e);
        }
    }

}

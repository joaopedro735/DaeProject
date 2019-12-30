package ejbs;

import entities.Product;
import entities.Sport;
import entities.SportRegistration;
import entities.Type;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJB;
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

    @EJB
    private TypeBean typeBean;

    public ProductBean() {
    }

    public Product create(int typeCode, String description, float value, Integer originalId, String className) throws MyEntityAlreadyExistsException, MyConstraintViolationException, MyEntityNotFoundException {
       //TODO See if the product already exists
        Type type = typeBean.find(typeCode);
        String tableName;
        Product product;
        if ( type == null) {
            throw new MyEntityNotFoundException("Type '" + type + "' doesn't exists");
        }

        try {
            product = new Product(type, description, value, Product.class.getName());
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

    public Product update(int id,int typeCode, String description, float value) throws MyEntityNotFoundException {
        try {
            Product product = em.find(Product.class, id);
            Type type = typeBean.find(typeCode);
            if (product == null) {
                throw new MyEntityNotFoundException("Product with id '" + id + "' not found.");
            }
            if (type == null){
                throw new MyEntityNotFoundException("Type with id '" + id + "' not found. ");
            }
            Product productNewVersion = new Product(product.getId(), type, description, value, product.getTableName());
            em.persist(productNewVersion);
            return productNewVersion;
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

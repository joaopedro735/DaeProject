package ejbs;

import entities.Product;
import entities.Type;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public Product create(int typeCode, String description, float value) throws MyEntityNotFoundException, MyConstraintViolationException, MyEntityAlreadyExistsException {
        return this.create(typeCode, description, value, null, Product.class.getName(), null);
    }

    public Product create(int typeCode, String description, float value, Integer originalId, String className, Integer relatedId) throws MyEntityAlreadyExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        //TODO See if the product already exists
        Type type = typeBean.find(typeCode);
        Product product;
        if (type == null) {
            throw new MyEntityNotFoundException("Type '" + typeCode + "' doesn't exists");
        }

        try {
            product = new Product(originalId, type, description, value, className, relatedId);
            em.persist(product);
            return product;

        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw e;
        }
    }

    public void remove(int id) {
        try {
            Product product = em.find(Product.class, id);
            if (product == null) {
                throw new MyEntityNotFoundException("Product with id'" + id + "' not found.");
            }
            em.remove(product);
        } catch (Exception e) {
            throw new EJBException("ERROR_REMOVING_PRODUCT", e);
        }
    }

    public Product update(int id, int typeCode, String description, float value) throws MyEntityNotFoundException {
        try {
            Product product = em.find(Product.class, id);
            Type type = typeBean.find(typeCode);
            if (product == null) {
                throw new MyEntityNotFoundException("Product with id '" + id + "' not found.");
            }
            if (type == null) {
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

    public Product findByTableNameAndTypeAndRelatedId(String tableName, int typeId, int relatedId) {
        try {
            return (Product) em.createNamedQuery("Products.getProductByTableNameAndTypeAndRelatedId")
                    .setParameter("tableName", tableName)
                    .setParameter("typeId", typeId)
                    .setParameter("relatedId", relatedId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_PRODUCT", e);
        }
    }
}

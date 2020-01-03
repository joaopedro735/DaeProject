package ejbs;

import entities.Sport;
import entities.Type;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless(name = "TypeEJB")
public class TypeBean {
    @PersistenceContext
    private EntityManager em;

    public TypeBean() {
    }


    //TODO See if the find work for the name ;)
    public Type create(String name) throws MyEntityAlreadyExistsException, MyConstraintViolationException {
        if (findByName(name) != null) {
            throw new MyEntityAlreadyExistsException("Type named '" + name + "' already exists");
        }
        try {
            Type type = new Type(name);
            em.persist(type);
            return type;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public List<Type> all() {
        try {
            return (List<Type>) em.createNamedQuery("getAllTypes").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_TYPES", e);
        }
    }

    public Type update(int id, String newName) throws MyEntityNotFoundException {
        try {
            Type type = find(id);
            if (type == null) {
                throw new MyEntityNotFoundException("Type with id '" + id + "' not found.");
            }

            em.lock(type, LockModeType.OPTIMISTIC);
            type.setName(newName);

            em.merge(type);
            return type;
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_TYPE", e);
        }
    }

    //TODO REMOVE (SOFT DELETE)
    public void remove(int id){
        try {
            Type type = find(id);
            if (type == null) {
                throw new MyEntityNotFoundException("Type with id '" + id + "' not found.");
            }

            if(type!=null) {
                em.remove(type);
            }
        } catch(Exception e){
            throw new EJBException("ERROR_REMOVING_TYPE", e);
        }
    }

    public Type find(int id) {
        try {
            return em.find(Type.class, id);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_TYPE", e);
        }
    }

    public Type findByName(String name) {
        try {
            return (Type) em.createNamedQuery("Type.getTypeByName").setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_TYPE", e);
        }
    }
}

package ejbs;

import entities.Sport;
import entities.Trainer;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless(name = "TrainerEJB")
public class TrainerBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private SportBean sportBean;

    public TrainerBean() {
    }

    public Trainer create(String username, String password, String nome, String email, String birthday) throws MyEntityAlreadyExistsException, MyConstraintViolationException {
        if (find(username) != null) {
            throw new MyEntityAlreadyExistsException("Username '" + username + "' already exists");
        }

        try {
            DateTimeFormatter birthdayFormat= DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate birthdayDate = LocalDate.parse(birthday, birthdayFormat);
            Trainer trainer = new Trainer(username, password, nome, email, birthdayDate);
            em.persist(trainer);
            return trainer;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public void remove(String username){
        try {
            Trainer trainer = em.find(Trainer.class, username);
            if (trainer == null) {
                throw new MyEntityNotFoundException("Trainer with username '" + username + "' not found.");
            }
            em.remove(trainer);
        } catch(Exception e){
            throw new EJBException("ERROR_REMOVING_TRAINER", e);
        }
    }

    public Trainer update(String username, String password, String name, String email) throws MyEntityNotFoundException {
        try {
            Trainer trainer = em.find(Trainer.class, username);

            if (trainer == null) {
                throw new MyEntityNotFoundException("Trainer with username '" + username + "' not found.");
            }
            em.lock(trainer, LockModeType.OPTIMISTIC);
            trainer.setPassword(password);
            trainer.setName(name);
            trainer.setEmail(email);
            em.merge(trainer);
            return trainer;
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_TRAINER", e);
        }
    }

    public List<Trainer> all() {
        try {
            return (List<Trainer>) em.createNamedQuery("getAllTrainers").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_TRAINERS", e);
        }
    }

    public Trainer find(String username) {
        try {
            return em.find(Trainer.class, username);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_TRAINER", e);
        }
    }

    public void enroll(String username, int sportsCode) {
        try {
            Trainer trainer = find(username);
            Sport sport = sportBean.find(sportsCode);

            sport.addTrainer(trainer);
            trainer.addSport(sport);
        } catch (Exception e) {
            throw new EJBException("ERROR_ENROLL_ATHLETE", e);
        }
    }
}

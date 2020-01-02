package ejbs;

import dtos.TimeTableDTO;
import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Stateless(name = "SportEJB")
public class SportBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private AthleteBean athleteBean;

    @EJB
    private PartnerBean partnerBean;

    @EJB
    private TrainerBean trainerBean;

    @EJB
    private SportRegistrationBean sportRegistrationBean;

    @EJB
    private TimeTableBean timeTableBean;

    @EJB
    private RankBean rankBean;

    public SportBean() {
    }

    public Sport create(String name) throws MyEntityAlreadyExistsException, MyConstraintViolationException {
        try {
            if (findByName(name) != null) {
                throw new MyEntityAlreadyExistsException("Sport with name '" + name + "' already exists");
            }
            Sport sport = new Sport();
            sport.setName(name);
            em.persist(sport);
            return sport;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public Sport find(int code) {
        try {
            return em.find(Sport.class, code);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_SPORT", e);
        }
    }

    public Sport findByName(String name) {
        try {
            return (Sport) em.createNamedQuery("getSportByName").setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_SPORT", e);
        }
    }

    public List<Sport> all() {
        try {
            return (List<Sport>) em.createNamedQuery("getAllSports").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_SPORTS", e);
        }
    }

    public Sport update(int code, String name) throws MyEntityNotFoundException {
        try {
            Sport sport = find(code);
            if (sport == null) {
                throw new MyEntityNotFoundException("Sport with code '" + code + "' not found.");
            }
            em.lock(sport, LockModeType.OPTIMISTIC);
            sport.setName(name);
            em.merge(sport);
            return sport;
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_SPORT", e);
        }
    }

    public void addTimetable(int sportCode, int timeTableId) throws MyEntityNotFoundException {
        try {
            Sport sport = find(sportCode);
            if (sport == null) {
                throw new MyEntityNotFoundException("Sport with code '" + sportCode + "' not found.");
            }
            TimeTable timeTable = timeTableBean.find(timeTableId);
            if (timeTable == null) {
                throw new MyEntityNotFoundException("Time table with id '" + timeTableId + "' not found");
            }
            sport.addTimeTable(timeTable);
            timeTable.setSport(sport);
            em.persist(sport);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("ERROR_ADD_TIMETABLE");
        }
    }

    public void removeTimetable(int sportCode, int timeTableId) throws MyEntityNotFoundException {
        try {
            //TODO: is this right?
            Sport sport = find(sportCode);
            if (sport == null) {
                throw new MyEntityNotFoundException("Sport with code '" + sportCode + "' not found.");
            }
            TimeTable timeTable = timeTableBean.find(timeTableId);
            if (timeTable == null) {
                throw new MyEntityNotFoundException("Time table with id '" + timeTableId + "' not found");
            }
            sport.removeTimeTable(timeTable);
        } catch (MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("ERROR_REMOVE_TIMETABLE");
        }
    }

    public void enrollPartner(String username, int sportsCode) {
        try {
            Partner partner = partnerBean.find(username);
            Sport sport = find(sportsCode);

            sport.addPartner(partner);
            partner.addSport(sport);
        } catch (Exception e) {
            throw new EJBException("ERROR_ENROLL_PARTNER", e);
        }
    }

    public void unrollPartner(String username, int sportsCode) {
        try {
            Partner partner = partnerBean.find(username);
            Sport sport = find(sportsCode);

            sport.removePartner(partner);
            partner.removeSport(sport);
        } catch (Exception e) {
            throw new EJBException("ERROR_UNROLL_PARTNER", e);
        }
    }

    public SportRegistration enrollAthlete(String username, int sportsCode, Collection<TimeTable> timeTables) throws MyEntityAlreadyExistsException, MyEntityNotFoundException {
        try {
            Athlete athlete = athleteBean.find(username);
            Sport sport = find(sportsCode);
            SportRegistration sportRegistration = sportRegistrationBean.create(username, sportsCode, timeTables);
            //TODO: do we need this?
//          boolean contains = sport.getAthletes().contains(athlete);
//          if (contains) {
//              return false;
//          }

            sport.addAthlete(sportRegistration);
            athlete.addAthleteSport(sportRegistration);
            return sportRegistration;
        } catch (MyEntityAlreadyExistsException | MyEntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("ERROR_ENROLL_ATHLETE", e);
        }
    }

    public void unrollAthlete(String username, int sportsCode) {
        try {
            Athlete athlete = athleteBean.find(username);
            Sport sport = find(sportsCode);

            sport.removeAthlete(athlete);
            //TODO: athlete.removeAthleteSport(sport);
        } catch (Exception e) {
            throw new EJBException("ERROR_UNROLL_ATHLETE", e);
        }
    }

    public void enrollTrainer(String username, int sportsCode) {
        try {
            Trainer trainer = trainerBean.find(username);
            Sport sport = find(sportsCode);

            sport.addTrainer(trainer);
            trainer.addSport(sport);
        } catch (Exception e) {
            throw new EJBException("ERROR_ENROLL_TRAINER", e);
        }
    }

    public void unrollTrainer(String username, int sportsCode) {
        try {
            Trainer trainer = trainerBean.find(username);
            Sport sport = find(sportsCode);

            sport.removeTrainer(trainer);
            trainer.removeSport(sport);
        } catch (Exception e) {
            throw new EJBException("ERROR_UNROLL_TRAINER", e);
        }
    }

}

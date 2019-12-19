package ejbs;

import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.Utils;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.List;

@Stateless(name = "SportEJB")
public class SportBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    AthleteBean athleteBean;

    @EJB
    PartnerBean partnerBean;

    @EJB
    TrainerBean trainerBean;

    @EJB
    SportRegistrationBean sportRegistrationBean;

    @EJB
    TimeTableBean timeTableBean;

    public SportBean() {
    }

    public Sport create(String name) throws MyEntityAlreadyExistsException, MyConstraintViolationException {
        try {
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
            throw new EJBException("ERROR_FINDING_SPORTS", e);
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
//TODO:            em.lock(sport, LockModeType.OPTIMISTIC);
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

    public boolean enrollAthlete(String username, int sportsCode, Collection<TimeTable> timeTables) {
        try {
            Athlete athlete = athleteBean.find(username);
            Sport sport = find(sportsCode);
            SportRegistration sportRegistration = sportRegistrationBean.create(username, sportsCode, timeTables);
            //boolean contains = sport.getAthletes().contains(athlete);
//            if (contains) {
//                return false;
//            }
            sport.addAthlete(sportRegistration);
          //TODO:  athlete.addAthleteSport(sport);
            athlete.addAthleteSport(sportRegistration);
            return true;
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

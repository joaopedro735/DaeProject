package ejbs;

import entities.Sport;
import entities.TimeTable;
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
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Stateless(name = "TimeTableBeanEJB")
public class TimeTableBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    SportBean sportBean;

    public TimeTableBean() {
    }

    public TimeTable create(DayOfWeek day, LocalTime start, LocalTime end, int sportCode) throws MyEntityAlreadyExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        try {
            Sport sport = sportBean.find(sportCode);
            if (sport == null) {
                throw new MyEntityNotFoundException("Sport with code '" + sportCode + "' not found.");
            }
            TimeTable timeTable = new TimeTable(day, start, end, sport);
            em.persist(timeTable);
            return timeTable;
        }catch (MyEntityNotFoundException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }
    }

    public TimeTable find(int id) {
        try {
            return em.find(TimeTable.class, id);
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_TIMETABLE", e);
        }
    }

    public List<TimeTable> find(List<Integer> ids) {
        try {
            String joinedIds = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
            return (List<TimeTable>) em.createNamedQuery("TimeTable.getByIds").setParameter("ids", ids).getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_FINDING_TIMETABLE", e);
        }
    }

    public List<TimeTable> all() {
        try {
            return (List<TimeTable>) em.createNamedQuery("TimeTable.getAll").getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_SPORTS", e);
        }
    }
}

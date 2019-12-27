package ejbs;

import entities.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Startup
@Singleton(name = "ConfigEJB")
public class ConfigBean {

    @EJB
    AdministratorBean administratorBean;

    @EJB
    PartnerBean partnerBean;

    @EJB
    AthleteBean athleteBean;

    @EJB
    SportBean sportBean;

    @EJB
    TrainerBean trainerBean;

    @EJB
    TimeTableBean timeTableBean;

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    public ConfigBean() {
    }

    @PostConstruct
    private void populateDB() {
        System.out.println("Seed DB");
        try {

            //region Users
            Administrator admin = administratorBean.create("admin", "secret123", "Administrador", "email@a","15/03/1990");
            Partner partner = partnerBean.create("partner", "secret123", "Partner", "email@partnet.net","20/04/1997");
            Athlete athlete = athleteBean.create("athlete", "secret123", "Athlete", "email@athlete.net", "12/07/1974");
            Athlete athlete2 = athleteBean.create("athlete2", "secret123", "Athlete2", "email@athlete2.net","02/12/1980");
            Trainer trainer = trainerBean.create("trainer", "secret123", "Trainer", "email@trainer.net","29/08/1992");
            //endregion

            //region Sports
            Sport judo = sportBean.create("Judo");
            Sport basquetebol = sportBean.create("Basquetebol");
            //endregion

            //region TimeTables
            TimeTable timeTableJudo = timeTableBean.create(DayOfWeek.MONDAY, LocalTime.of(9, 30), LocalTime.of(11,00), judo.getCode());
            TimeTable timeTableJudo1 = timeTableBean.create(DayOfWeek.FRIDAY, LocalTime.of(9, 30), LocalTime.of(11,00), judo.getCode());
            TimeTable timeTableBasquet = timeTableBean.create(DayOfWeek.THURSDAY, LocalTime.of(9, 30), LocalTime.of(11,00), basquetebol.getCode());
            sportBean.addTimetable(judo.getCode(),timeTableJudo.getId());
            sportBean.addTimetable(judo.getCode(), timeTableJudo1.getId());
            sportBean.addTimetable(basquetebol.getCode(), timeTableBasquet.getId());
            Collection<TimeTable> timeTablesJudo = new LinkedHashSet<>();
            Collection<TimeTable> timeTablesBasquet = new LinkedHashSet<>();
            timeTablesJudo.add(timeTableJudo);
            timeTablesBasquet.add(timeTableBasquet);
            //endregion
            /*System.out.println(judo.timeTableExists(timeTableJudo));
            System.out.println(judo.timeTablesExists(timeTablesJudo));*/

            //region ENROLL
            sportBean.enrollAthlete("athlete", judo.getCode(), timeTablesJudo);
            sportBean.enrollAthlete("athlete", basquetebol.getCode(), timeTablesBasquet);
            sportBean.enrollAthlete("athlete2", basquetebol.getCode(), timeTablesBasquet);
            sportBean.enrollPartner("partner", judo.getCode());
            trainerBean.enroll("trainer", judo.getCode());
            trainerBean.enroll("trainer", basquetebol.getCode());
            //endregion

            //region ETC...
            /*System.out.println(timeTableJudo.getDuration());
            System.out.println(athlete.getPracticedSports().toString());
            System.out.println(basquetebol.getAhtletes().toString());*/
            for (User user :
                    athleteBean.all()) {
                System.out.println(user.getName() + " is " + user.getAge() + " years old");
            }
            //System.out.println(judo.getTimeTables().stream().map(s->s.getDay()).collect(Collectors.joining()));
            /* System.out.println(basquetebol.getPartners().stream().filter(o->(o instanceof Athlete)).findAny().toString());
            //System.out.println(basquetebol.getPartners().toString());
            System.out.println(athlete.getAthleteSports().toString());
            System.out.println(athlete.getSports().toString());*/
            //endregion

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}

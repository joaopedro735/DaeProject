package ejbs;

import entities.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    public ConfigBean() {
    }

    @PostConstruct
    private void populateDB() {
        System.out.println("Seed DB");
        try {
            Administrator admin = administratorBean.create("admin", "secret123", "Administrador", "email@a");
            Partner partner = partnerBean.create("partner", "secret123", "Partner", "email@partnet.net");
            Athlete athlete = athleteBean.create("athlete", "secret123", "Athlete", "email@athlete.net");
            Athlete athlete2 = athleteBean.create("athlete2", "secret123", "Athlete2", "email@athlete2.net");
            Trainer trainer = trainerBean.create("trainer", "secret123", "Trainer", "email@trainer.net");
            Sport judo = sportBean.create("Judo");
            Sport basquetebol = sportBean.create("Basquetebol");
            System.out.println(athlete instanceof Partner);
            System.out.println(partner instanceof Athlete);
            System.out.println(admin instanceof User);
            sportBean.enrollAthlete("athlete", judo.getCode());
            sportBean.enrollPartner("athlete", basquetebol.getCode());
            sportBean.enrollAthlete("athlete2", basquetebol.getCode());
            sportBean.enrollPartner("partner", judo.getCode());
            trainerBean.enroll("trainer", judo.getCode());
            trainerBean.enroll("trainer", basquetebol.getCode());

           /* System.out.println(basquetebol.getPartners().stream().filter(o->(o instanceof Athlete)).findAny().toString());
            //System.out.println(basquetebol.getPartners().toString());
            System.out.println(athlete.getAthleteSports().toString());
            System.out.println(athlete.getSports().toString());*/

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}

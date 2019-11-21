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
    SportsBean sportsBean;

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
            Sport judo = sportsBean.create("Judo");
            Sport basquetebol = sportsBean.create("Basquetebol");
            System.out.println(athlete instanceof Partner);
            System.out.println(partner instanceof Athlete);
            System.out.println(admin instanceof User);
            athleteBean.enroll("athlete", judo.getCode());
            athleteBean.enroll("athlete", basquetebol.getCode());
            athleteBean.enroll("athlete2", basquetebol.getCode());
            partnerBean.enroll("partner", judo.getCode());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}

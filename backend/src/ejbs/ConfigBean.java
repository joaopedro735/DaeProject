package ejbs;

import com.lambdaworks.crypto.SCryptUtil;
import entities.Administrator;
import entities.Athlete;
import entities.Partner;
import entities.User;

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
            System.out.println(athlete instanceof Partner);
            System.out.println(partner instanceof Athlete);
            System.out.println(admin instanceof User);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}

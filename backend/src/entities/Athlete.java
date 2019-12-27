package entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAthletes",
                query = "SELECT a FROM Athlete a ORDER BY a.name"
        )
})
public class Athlete extends Partner {
    //@ManyToMany(mappedBy = "practicedBy", targetEntity = Sport.class,  fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "athlete")
    private Set<SportRegistration> mySportRegistrations;

    @Transient
    public List<Sport> getPracticedSports() {
        return mySportRegistrations.stream().map(SportRegistration::getSport).collect(Collectors.toList());
    }

    public Athlete() {
        this.sports = new LinkedHashSet<>();
    }

    public Athlete(String username, String password, String name, String email, LocalDate birthday) {
        super(username, password, name, email, birthday);
        this.mySportRegistrations = new LinkedHashSet<>();
    }

    public Set<SportRegistration> getMySportRegistrations() {
        return mySportRegistrations;
    }

    public void addAthleteSport(SportRegistration sportRegistration) {
        this.mySportRegistrations.add(sportRegistration);
        this.addSport(sportRegistration.getSport());
    }

    public void removeAthleteSport(SportRegistration sportRegistration) {
        this.mySportRegistrations.remove(sportRegistration);
        //this.removeSport(practicedSport);
    }
}

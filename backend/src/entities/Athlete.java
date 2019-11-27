package entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAthletes",
                query = "SELECT a FROM Athlete a ORDER BY a.name"
        )
})
public class Athlete extends Partner {
    @ManyToMany(mappedBy = "athletes", fetch = FetchType.EAGER)
    private Set<Sport> athleteSports;

    public Athlete() {
        this.sports = new LinkedHashSet<>();
    }

    public Athlete(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.athleteSports = new LinkedHashSet<>();
    }

    public Set<Sport> getAthleteSports() {
        return athleteSports;
    }

    public void addAthleteSport(Sport sport) {
        this.athleteSports.add(sport);
        this.addSport(sport);
    }

    public void removeAthleteSport(Sport sport) {
        this.athleteSports.remove(sport);
        this.removeSport(sport);
    }
}

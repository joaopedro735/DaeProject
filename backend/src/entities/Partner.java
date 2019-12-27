package entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllPartners",
                query = "SELECT p FROM Partner p ORDER BY p.name"
        )
})
public class Partner extends User {
    @ManyToMany(mappedBy = "partners", fetch = FetchType.EAGER)
    protected Set<Sport> sports;

    public Partner() {
        this.sports = new LinkedHashSet<>();
    }

    public Partner(String username, String password, String name, String email, LocalDate birthday) {
        super(username, password, name, email, birthday);
        this.sports = new LinkedHashSet<>();
    }

    public void addSport(Sport sport) {
        this.sports.add(sport);
    }

    public void removeSport(Sport sport) {
        this.sports.remove(sport);
    }

    public Set<Sport> getSports() {
        return sports;
    }
}

package entities;

import javax.persistence.*;
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

    public Partner(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.sports = new LinkedHashSet<>();
    }

    public void addSport(Sport sport) {
        this.sports.add(sport);
    }

    public Set<Sport> getSports() {
        return sports;
    }
}

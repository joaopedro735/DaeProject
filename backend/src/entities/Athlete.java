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
    @ManyToMany(mappedBy = "trainers", fetch = FetchType.EAGER)
    private Set<Sport> sports;

    public Athlete() {
        this.sports = new LinkedHashSet<>();
    }

    public Athlete(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.sports = new LinkedHashSet<>();
    }
}

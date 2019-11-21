package entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAthletes",
                query = "SELECT a FROM Athlete a ORDER BY a.name"
        )
})
public class Athlete extends Partner {
    public Athlete() {
    }

    public Athlete(String username, String password, String name, String email) {
        super(username, password, name, email);
    }
}

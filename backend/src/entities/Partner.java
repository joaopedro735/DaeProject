package entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllPartners",
                query = "SELECT p FROM Partner p ORDER BY p.name"
        )
})
public class Partner extends User {
    public Partner() {
    }

    public Partner(String username, String password, String name, String email) {
        super(username, password, name, email);
    }
}

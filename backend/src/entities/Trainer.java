package entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllTrainers",
                query = "SELECT t FROM Trainer t ORDER BY t.name"
        )
})
public class Trainer extends User {
}

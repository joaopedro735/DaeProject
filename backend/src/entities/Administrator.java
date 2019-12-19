package entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.time.LocalDate;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAdministrators",
                query = "SELECT a FROM Administrator a ORDER BY a.name" //JPQL
        )
})
public class Administrator extends User {
    public Administrator() {
    }

    public Administrator(String username, String password, String name, String email, LocalDate birthday) {
        super(username, password, name, email, birthday);
    }
}

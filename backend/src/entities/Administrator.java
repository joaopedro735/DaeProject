package entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAdministrators",
                query = "SELECT a FROM Administrator a ORDER BY a.name" //JPQL
        ),
        @NamedQuery(
                name = "getAdministratorsByNameSearch",
                query = "SELECT a FROM Administrator a where upper(a.name) LIKE upper(:name) ORDER BY a.name"
        )
})
public class Administrator extends User {
    public Administrator() {

    }

    public Administrator(String username, String password, String name, String email) {
        super(username, password, name, email);
    }
}

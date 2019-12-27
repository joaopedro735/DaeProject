package entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllTrainers",
                query = "SELECT t FROM Trainer t ORDER BY t.name"
        ),
        @NamedQuery(
                name = "getTrainersByNameSearch",
                query = "SELECT t FROM Trainer t where upper(t.name) LIKE upper(:name) ORDER BY t.name"
        )
})
public class Trainer extends User {
    @ManyToMany(mappedBy = "trainers", fetch = FetchType.EAGER)
    private Set<Sport> sports;

    public Trainer() {
            this.sports = new LinkedHashSet<>();
    }

    public Trainer(String username, String password, String name, String email, LocalDate birthday) {
        super(username, password, name, email, birthday);
        this.sports = new LinkedHashSet<>();
    }

    public void addSport(Sport sport) {
        this.sports.add(sport);
    }

    public void removeSport(Sport sport) {
        this.sports.remove(sport);
    }
}

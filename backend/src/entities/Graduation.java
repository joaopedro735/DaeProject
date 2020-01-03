package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "GRADUATIONS")
public class Graduation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    @ManyToOne
    private Sport sport;

    public Graduation() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Graduation)) return false;
        Graduation that = (Graduation) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(sport, that.sport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sport);
    }
}

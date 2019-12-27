package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class TaughtSport implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    SportSeason sportSeason;

    @OneToOne
    Trainer trainer;

    @OneToMany
    Set<TimeTable> timeTables;
}

package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class TaughtSport implements Serializable {
    //todo:
    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    Trainer trainer;

    @OneToMany
    Set<TimeTable> timeTables;
}

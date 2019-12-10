package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "PRACTICED_SPORTS")
public class PracticedSport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @ManyToOne
    @JoinColumn(name = "SPORT_CODE", nullable = false)
    private Sport sport;

    @ManyToOne
    @JoinColumn()
    private Rank rank;

    @ManyToOne
    private Graduation graduation;

    @ManyToOne
    @JoinColumn(name = "ATLHETE_USERNAME", nullable = false)
    private Athlete athlete;

    //private TimeTable timetable;

    public PracticedSport() {
    }

    public PracticedSport(Athlete athlete, Sport sport) {
        this.athlete = athlete;
        this.sport = sport;
    }

    public int getId() {
        return id;
    }

    public Sport getSport() {
        return sport;
    }

    public Rank getRank() {
        return rank;
    }

    public Graduation getGraduation() {
        return graduation;
    }

    public Athlete getAthlete() {
        return athlete;
    }
}

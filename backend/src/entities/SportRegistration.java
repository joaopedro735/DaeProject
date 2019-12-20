package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "SPORT_REGISTRATION")
@NamedQueries({
        @NamedQuery(
                name = "getAllSportRegistration",
                query = "SELECT s FROM SportRegistration s ORDER BY s.sport.name"
        ),
        @NamedQuery(
                name = "getSportRegistrationByUsernameAndSportCode",
                query = "SELECT s FROM SportRegistration s WHERE s.athlete.username = :username AND s.sport.code = :code"
        )
})
public class SportRegistration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @ManyToOne
    @JoinColumn(name = "SPORT_CODE", nullable = false)
    private Sport sport;

    @ManyToOne
    @JoinColumn(name = "ATHLETE_USERNAME", nullable = false)
    private Athlete athlete;

    @ManyToOne
    private Rank rank;

    @ManyToOne
    private Graduation graduation;

    @OneToMany
    private Set<TimeTable> timeTables;

    //DateSignUp
    //@Temporal(TemporalType.TIMESTAMP)
    //private Date createdOn;
    private LocalDateTime createdOn;

    //EpocaDesportiva

    public SportRegistration() {
        this.timeTables = new LinkedHashSet<>();
        this.createdOn = LocalDateTime.now();
    }

    public SportRegistration(Athlete athlete, Sport sport, Rank rank, Graduation graduation) {
        this();
        this.athlete = athlete;
        this.sport = sport;
        this.rank = rank;
        this.graduation = graduation;
    }

    public int getId() {
        return id;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Graduation getGraduation() {
        return graduation;
    }

    public void setGraduation(Graduation graduation) {
        this.graduation = graduation;
    }

    public void addTimeTables(TimeTable timeTable) {
        this.timeTables.add(timeTable);
    }

    public void addTimeTables(Collection<TimeTable> timeTables) {
        this.timeTables.addAll(timeTables);
    }

    public void removeTimeTable(TimeTable timeTable) {
        this.timeTables.remove(timeTable);
    }

    public Set<TimeTable> getTimeTables() {
        return timeTables;
    }

}

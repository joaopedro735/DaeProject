package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
public class SportSeason implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private Sport sport;

    @OneToOne
    private Season season;

    public SportSeason() {
    }

    public SportSeason(Sport sport, Season season) {
        this.sport = sport;
        this.season = season;
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

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }
}

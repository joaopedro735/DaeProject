package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Season {
    @Id
    @GeneratedValue
    private int id;

    private String season;

    //TODO: do we need this?
    //private String description;

    public Season() {
    }

    public Season(String season) {
        this.season = season;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}

package entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAthletes",
                query = "SELECT a FROM Athlete a ORDER BY a.name"
        )
})
public class Athlete extends Partner {
    //@ManyToMany(mappedBy = "practicedBy", targetEntity = Sport.class,  fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "athlete")
    private Set<PracticedSport> practicedSports;

    @Transient
    public List<Sport> getPracticedSport() {
        return practicedSports.stream().map(PracticedSport::getSport).collect(Collectors.toList());
    }

    public Athlete() {
        this.sports = new LinkedHashSet<>();
    }

    public Athlete(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.practicedSports = new LinkedHashSet<>();
    }

    public Set<PracticedSport> getPracticedSports() {
        return practicedSports;
    }

    public void addAthleteSport(PracticedSport practicedSport) {
        this.practicedSports.add(practicedSport);
        this.addSport(practicedSport.getSport());
    }

    public void removeAthleteSport(PracticedSport practicedSport) {
        this.practicedSports.remove(practicedSport);
        //this.removeSport(practicedSport);
    }
}

package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllSports",
                query = "SELECT s FROM Sport s ORDER BY s.name"
        ),
        @NamedQuery(
                name = "getSportByName", query = "SELECT s from Sport s WHERE s.name = :name")
})
@Table(name = "SPORTS", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
public class Sport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int code;

    @NotNull
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "SPORTS_RANKS",
            joinColumns = @JoinColumn(name = "SPORT_CODE", referencedColumnName = "CODE"),
            inverseJoinColumns = @JoinColumn(name = "RANK_CODE", referencedColumnName = "CODE")
    )
    private Set<Rank> ranks;

  //  private TimeTable timeTable;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "SPORTS_PARTNERS",
            joinColumns = @JoinColumn(name = "SPORT_CODE", referencedColumnName = "CODE"),
            inverseJoinColumns = @JoinColumn(name = "PARTNER_USERNAME", referencedColumnName = "USERNAME"))
    private Set<Partner> partners;

    @ManyToMany
    @JoinTable(
            name = "SPORTS_ATHLETES",
            joinColumns = @JoinColumn(name = "SPORT_CODE", referencedColumnName = "CODE"),
            inverseJoinColumns = @JoinColumn(name = "ATHLETE_USERNAME", referencedColumnName = "USERNAME"))
    private Set<Athlete> athletes;

    @ManyToMany
    @JoinTable(
            name = "SPORTS_TRAINERS",
            joinColumns = @JoinColumn(name = "SPORT_CODE", referencedColumnName = "CODE"),
            inverseJoinColumns = @JoinColumn(name = "TRAINER_USERNAME", referencedColumnName = "USERNAME"))
    private Set<Trainer> trainers;

    @OneToMany(mappedBy = "sport")
    private Set<PracticedSport> practicedBy;

   @Version
   private int version;

    public Sport() {
     //   this.ranks = new LinkedHashSet<>();
        this.partners = new LinkedHashSet<>();
        this.trainers = new LinkedHashSet<>();
        this.athletes = new LinkedHashSet<>();
        this.practicedBy = new LinkedHashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void addPartner(Partner partner) {
        this.partners.add(partner);
    }

    public void removePartner(Partner partner) {
        this.partners.remove(partner);
    }

    public void addAthlete(PracticedSport practicedSport) {
        this.partners.add(practicedSport.getAthlete());
        //this.athletes.add(athlete);
        this.practicedBy.add(practicedSport);
    }

    public void removeAthlete(Athlete athlete) {
        this.partners.remove(athlete);
        this.athletes.remove(athlete);
    }

    public void addTrainer(Trainer trainer) {
        this.trainers.add(trainer);
    }

    public void removeTrainer(Trainer trainer) {
        this.trainers.remove(trainer);
    }

    public Set<Partner> getPartners() {
        return partners;
    }

    public Set<Athlete> getAthletes() {
        return athletes;
    }

    public Set<Trainer> getTrainers() {
        return trainers;
    }

    @Override
    public String toString() {
        return name;
    }
}

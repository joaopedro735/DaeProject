package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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

    @OneToMany(mappedBy = "sport",fetch = FetchType.LAZY)
    private Set<Rank> ranks;

    @OneToMany(mappedBy = "sport",fetch = FetchType.LAZY)
    private Set<Graduation> graduations;

    @OneToMany(mappedBy = "sport", fetch = FetchType.LAZY)
    private Set<TimeTable> timeTables;

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
    private Set<SportRegistration> practicedBy;

   @Version
   private int version;

    public Sport() {
        this.ranks = new LinkedHashSet<>();
        this.graduations = new LinkedHashSet<>();
        this.partners = new LinkedHashSet<>();
        this.trainers = new LinkedHashSet<>();
        this.athletes = new LinkedHashSet<>();
        this.practicedBy = new LinkedHashSet<>();
        this.timeTables = new LinkedHashSet<>();
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

    public void addAthlete(SportRegistration sportRegistration) {
        this.partners.add(sportRegistration.getAthlete());
        //this.athletes.add(athlete);
        this.practicedBy.add(sportRegistration);
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

    public void addTimeTable(TimeTable timeTable) { this.timeTables.add(timeTable);}

    public void removeTimeTable(TimeTable timeTable) { this.timeTables.remove(timeTable);}

    public void addRank(Rank rank) {
        this.ranks.add(rank);
    }

    public void removeRank(Rank rank) {
        this.ranks.remove(rank);
    }

    public void addGraduation(Graduation graduation) {
        this.graduations.add(graduation);
    }

    public void removeGraduation(Graduation graduation) {
        this.graduations.remove(graduation);
    }

    @Transient
    public List<Athlete> getAhtletes() {
        return practicedBy.stream().map(SportRegistration::getAthlete).collect(Collectors.toList());
    }

    public Set<Partner> getPartners() {
        return partners;
    }

    //TODO: remove
    /*public Set<Athlete> getAthletes() {
        return athletes;
    }*/

    public Set<Trainer> getTrainers() {
        return trainers;
    }

    public Set<TimeTable> getTimeTables() {
        return timeTables;
    }

    @Transient
    public boolean timeTablesExists(Collection<TimeTable> timeTables) {
        return this.timeTables.containsAll(timeTables);
    }

    @Transient
    public boolean timeTableExists(TimeTable timeTable) {
        return this.timeTables.contains(timeTable);
    }

    @Override
    public String toString() {
        return name;
    }
}

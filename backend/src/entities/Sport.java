package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "SPORTS", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
public class Sport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int code;

    private String name;

    private List<Rank> ranks;

    private TimeTable timeTable;

    @ManyToMany
    @JoinTable(
            name = "SPORTS_PARTNERS",
            joinColumns = @JoinColumn(name = "SPORTS_CODE", referencedColumnName = "CODE"),
            inverseJoinColumns = @JoinColumn(name = "PARTNER_USERNAME", referencedColumnName = "USERNAME"))
    private Set<Partner> partners;

    @ManyToMany
    @JoinTable(
            name = "SPORTS_TRAINERS",
            joinColumns = @JoinColumn(name = "SPORTS_CODE", referencedColumnName = "CODE"),
            inverseJoinColumns = @JoinColumn(name = "TRAINER_USERNAME", referencedColumnName = "USERNAME"))
    private Set<Trainer> trainers;

    public Sport() {
        this.partners = new LinkedHashSet<>();
        this.trainers = new LinkedHashSet<>();

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
}

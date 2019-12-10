package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GRADUATIONS")
public class Graduation {
    @Id
    private int id;

    private String name;

    public Graduation() {
    }
}

package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class TimeTable implements Serializable {
    @Id
    private long id;


    public TimeTable() {
    }

}

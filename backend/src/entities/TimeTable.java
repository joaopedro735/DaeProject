package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "TIMETABLES")
public class TimeTable implements Serializable {
    @Id
    private long id;


    public TimeTable() {
    }

}

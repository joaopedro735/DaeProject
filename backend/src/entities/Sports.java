package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Entity
public class Sports implements Serializable {
    @Id
    private String name;

    private List<Rank> ranks;

    private TimeTable timeTable;

    private List<Partner> partners;

    private List<Trainer> trainers;

    public Sports() {
    }


}

package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "RANKS")
public class Rank implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

}

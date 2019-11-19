package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Entity
public class Sports implements Serializable {
    @Id
    private String name;

    private List<Rank> escaloes;

    private TimeTable timeTable;

    private List<Socio> socios;

    private List<Trainer> treinadores;

    public Sports() {
    }


}

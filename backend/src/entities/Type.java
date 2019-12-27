package entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "TYPES")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    @OneToMany(mappedBy = "type")
    Set<Product> products;

    public Type() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

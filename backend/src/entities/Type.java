package entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="TYPES")
@NamedQueries({
        @NamedQuery(
                name = "getAllTypes",
                query = "SELECT a FROM Type a ORDER BY a.name"
        ),
        @NamedQuery(
                name = "Type.getTypeByName",
                query = "SELECT t FROM Type t WHERE UPPER(t.name) = UPPER(:name)"
        )
})


public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    protected String name;

    @Version
    private int version;

    @OneToMany(mappedBy = "type")
    Set<Product> products;

    public Type(String name) {
        this.name = name;
    }

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

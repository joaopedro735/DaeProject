package entities;

import com.sun.istack.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Entity
@Table(name = "PRODUCTS")
@NamedQueries({
        @NamedQuery(
                name = "getAllProducts",
                query = "SELECT p FROM Product p ORDER BY p.id" //JPQL
        ),
        @NamedQuery(
                name = "getProductsByNameSearch",
                query = "SELECT a FROM Product a where upper(a.description) LIKE upper(:name) ORDER BY a.description"
        )
})
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @NotNull
    protected Type type;

    @NotNull
    protected String description;

    @NotNull
    protected float value;

    @Nullable
    protected Integer originalId;

    @Version
    private int version;

    @NotNull
    protected String tableName;

    public Product() {
    }

    public Product(Type type, String description, float value, String tableName) {
        this.originalId = null;
        this.type = type;
        this.description = description;
        this.value = value;
        this.tableName = tableName;
    }

    public Product(int originalId, Type type, String description, float value, String tableName) {
        this.originalId = originalId;
        this.type = type;
        this.description = description;
        this.value = value;
        this.tableName = tableName;
    }

    public int getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Integer originalId) {
        this.originalId = originalId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setOriginalId(int originalId) {
        this.originalId = originalId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}

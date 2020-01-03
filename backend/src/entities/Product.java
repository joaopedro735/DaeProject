package entities;

import com.sun.istack.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProducts",
                query = "SELECT p FROM Product p " +
                        "WHERE p.id NOT IN (SELECT p1.originalId FROM Product p1 WHERE p1.originalId IS NOT NULL) " +
                        "ORDER BY p.id"
        ),
        @NamedQuery(
        name = "Products.getLatestProductByTableNameAndTypeAndRelatedId",
        query = "SELECT p FROM Product p " +
                "WHERE p.id = (SELECT MAX(p1.id) " +
                              "FROM Product p1 " +
                              "WHERE UPPER(p1.tableName) = UPPER(:tableName) AND p1.type.id = :typeId AND p1.relatedId = :relatedId " +
                              "GROUP BY p1.relatedId, p1.type.id, p1.tableName)"),
        @NamedQuery(
                name = "getProductsByNameSearch",
                query = "SELECT a FROM Product a where upper(a.description) LIKE upper(:name) ORDER BY a.description"
        ),
        @NamedQuery(
                name = "Products.getByIds",
                query = "SELECT p FROM Product p WHERE p.id IN (:ids) ORDER BY p.description"
        )
})
@Table(name = "PRODUCTS", uniqueConstraints = @UniqueConstraint(columnNames = {"ORIGINAL_ID", "TYPE_ID", "TABLE_NAME", "RELATED_ID"}))
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "TYPE_ID")
    protected Type type;

    @NotNull
    protected String description;

    protected @NotNull BigDecimal value;

    @Nullable
    @Column(name = "ORIGINAL_ID")
    protected Integer originalId;

    @Version
    private int version;

    @NotNull
    @Column(name = "TABLE_NAME")
    protected String tableName;

    @Column(name = "RELATED_ID")
    protected Integer relatedId;

    public Product() {
    }

    public Product(Type type, String description, BigDecimal value, String tableName) {
        this.originalId = null;
        this.type = type;
        this.description = description;
        this.value = value;
        this.tableName = tableName;
    }

    public Product(Integer originalId, Type type, String description, BigDecimal value, String tableName) {
        this.originalId = originalId;
        this.type = type;
        this.description = description;
        this.value = value;
        this.tableName = tableName;
    }

    public Product(Integer originalId, Type type, String description, @NotNull BigDecimal value, String tableName, Integer relatedId) {
        this.originalId = originalId;
        this.type = type;
        this.description = description;
        this.value = value;
        this.tableName = tableName;
        this.relatedId = relatedId;
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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(@NotNull BigDecimal value) {
        this.value = value;
    }

    public int getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(int relatedId) {
        this.relatedId = relatedId;
    }
}

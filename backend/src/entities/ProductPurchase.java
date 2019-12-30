package entities;

import javax.persistence.*;


@NamedQueries({
        @NamedQuery(
                name = "getAllPayments",
                query = "SELECT p FROM ProductPurchase p ORDER BY p.id"
        )
})
@Entity
@Table(name = "PRODUCT_PURCHASE")
public class ProductPurchase {
    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private Product product;

    private String unity;

    private int quantity;

    public ProductPurchase() {
    }

    public ProductPurchase(Product product, String unity, int quantity){
        this.product = product;
        this.unity = unity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }
}



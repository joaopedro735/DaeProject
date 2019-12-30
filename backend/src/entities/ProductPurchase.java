package entities;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT_PURCHASE")
public class ProductPurchase {
    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private Product product;

    private String unity;

    public ProductPurchase() {
    }

    public ProductPurchase(Product product, String unity){
        this.product = product;
        this.unity = unity;
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



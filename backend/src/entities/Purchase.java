package entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@NamedQueries({
        @NamedQuery(
                name = "getAllPurchases",
                query = "SELECT p FROM Purchase p ORDER BY p.id"
        )
})
@Entity
@Table(name = "PURCHASES")
public class Purchase {


    @Id
    @GeneratedValue
    private int id;

    @OneToMany
    private Set<Payment> paymentList;

    @OneToMany()
    private Set<ProductPurchase> productPurchases;

    @Column(name = "PURCHASE_DATE")
    private LocalDateTime purchaseDate;

    @OneToOne
    private User user;

    @Column(name = "TOTAL_EUROS",precision = 19, scale = 2)
    private BigDecimal totalEuros;

    @Version
    private int version;

    public Purchase() {
        this.purchaseDate = LocalDateTime.now();
        this.paymentList = new LinkedHashSet<>();
        this.productPurchases = new LinkedHashSet<>();
    }

    public Purchase(BigDecimal totalEuros, User user){
        this();
        this.user = user;
        this.totalEuros = totalEuros;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void addPaymentToPaymentList(Payment p){
        this.paymentList.add(p);
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getTotalEuros() {
        return totalEuros;
    }

    public void setTotalEuros(BigDecimal totalEuros) {
        this.totalEuros = totalEuros;
    }

    public Set<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(Set<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public Set<ProductPurchase> getProductPurchases() {
        return productPurchases;
    }

    public void setProductPurchases(Set<ProductPurchase> productPurchases) {
        this.productPurchases = productPurchases;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addProductPurchase(ProductPurchase productPurchase) {
        productPurchases.add(productPurchase);
    }

    public void addProductPurchases(Set<ProductPurchase> productPurchases) {
        this.productPurchases.addAll(productPurchases);
    }
}

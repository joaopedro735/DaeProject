package entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @OneToMany(cascade = CascadeType.PERSIST)
    private Set<ProductPurchase> productPurchases;

    private LocalDate purchaseDate;

    @OneToOne
    private User user;

    @Column(precision = 19, scale = 2)
    private BigDecimal totalEuros;

    public Purchase() {
        this.purchaseDate = LocalDate.now();
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

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
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

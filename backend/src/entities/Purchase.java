package entities;

import javax.ejb.Local;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany
    @JoinTable
    private Set<ProductPurchase> productPurchases;

    private LocalDate purchaseDate;

    @OneToOne
    private User user;

    private float totalEuros;

    public Purchase() {
    }

    public Purchase(Set<ProductPurchase> productPurchases, float totalEuros, User user){
        this.paymentList = new LinkedHashSet<>();
        this.user = user;
        this.productPurchases = productPurchases;
        this.totalEuros = totalEuros;
        this.purchaseDate = LocalDate.now();
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public float getTotalEuros() {
        return totalEuros;
    }

    public void setTotalEuros(float totalEuros) {
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
}

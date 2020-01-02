package dtos;

import entities.Payment;
import entities.ProductPurchase;
import entities.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

public class PurchaseDTO {
    private int id;
    private ArrayList productPurchasesIDs ;
    private ArrayList paymentIDs;
    private ProductPurchaseDTO[] productPurchases;
    private LocalDate purchaseDate;
    private String username;
    private float totalEuros;

    public PurchaseDTO() {
    }

    public PurchaseDTO(int id, ArrayList paymentListIDs, ArrayList productPurchasesIDs, LocalDate purchaseDate, String username, float totalEuros) {
        this.id = id;
        this.paymentIDs = paymentListIDs;
        this.productPurchasesIDs = productPurchasesIDs;
        this.purchaseDate = purchaseDate;
        this.username = username;
        this.totalEuros = totalEuros;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList getProductPurchasesIDs() {
        return productPurchasesIDs;
    }

    public void setProductPurchasesIDs(ArrayList productPurchasesIDs) {
        this.productPurchasesIDs = productPurchasesIDs;
    }

    public ArrayList getPaymentIDs() {
        return paymentIDs;
    }

    public void setPaymentIDs(ArrayList paymentIDs) {
        this.paymentIDs = paymentIDs;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getTotalEuros() {
        return totalEuros;
    }

    public void setTotalEuros(float totalEuros) {
        this.totalEuros = totalEuros;
    }

    public ProductPurchaseDTO[] getProductPurchases() {
        return productPurchases;
    }

    public void setProductPurchases(ProductPurchaseDTO[] productPurchases) {
        this.productPurchases = productPurchases;
    }
}

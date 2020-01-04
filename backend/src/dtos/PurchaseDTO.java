package dtos;

public class PurchaseDTO {
    private int id;
    private ProductPurchaseDTO[] productPurchases;
    private String purchaseDate;
    private PaymentDTO paymentDTO;
    private String username;
    private Double totalEuros;

    public PurchaseDTO() {
    }

    public PurchaseDTO(int id, String purchaseDate, String username, Double totalEuros) {
        this.id = id;
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


    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getTotalEuros() {
        return totalEuros;
    }

    public void setTotalEuros(Double totalEuros) {
        this.totalEuros = totalEuros;
    }

    public ProductPurchaseDTO[] getProductPurchases() {
        return productPurchases;
    }

    public void setProductPurchases(ProductPurchaseDTO[] productPurchases) {
        this.productPurchases = productPurchases;
    }

}

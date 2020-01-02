package dtos;

import entities.Product;


public class ProductPurchaseDTO {
    private int id;

    private Product product;

    private String unity;

    private int quantity;

    public ProductPurchaseDTO(int id, Product product, String unity, int quantity) {
        this.id = id;
        this.product = product;
        this.unity = unity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

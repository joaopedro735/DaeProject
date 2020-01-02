package dtos;

import entities.Product;


public class ProductPurchaseDTO {
    private int id;

    private ProductDTO product;

    private String unity;

    private int quantity;

    public ProductPurchaseDTO() {
    }

    public ProductPurchaseDTO(int id, int productTypeCode, String productTypeName, String productDescription, String unity, int quantity) {
        this.id = id;
        this.product = new ProductDTO();
        this.unity = unity;
        this.quantity = quantity;
        product.setTypeCode(productTypeCode);
        product.setTypeName(productTypeName);
        product.setDescription(productDescription);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
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

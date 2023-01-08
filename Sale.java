public class Sale {

    private String productId;
    private int quantity;
    private float price;

    public Sale(String productId, int quantity, float price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }

    
}

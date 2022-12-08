package com.sabre.interview.model;

public class OrderItem {
    private double price;
    private ProductItem productItem;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = Math.round(price * 100) / 100.0;
    }

    public ProductItem getProduct() {
        return productItem;
    }

    public void setProduct(ProductItem productItem) {
        this.productItem = productItem;
    }
}

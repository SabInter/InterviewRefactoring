package com.sabre.interview.model;

import java.util.Objects;

public class OrderItem {
    private double price;
    private ProductItem productItem;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItem productItem) {
        this.productItem = productItem;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Double.compare(price, orderItem.price) == 0 && Objects.equals(productItem, orderItem.productItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, productItem);
    }
}

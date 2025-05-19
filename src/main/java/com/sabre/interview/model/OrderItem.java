package com.sabre.interview.model;

import org.javamoney.moneta.Money;

import java.util.Objects;

public class OrderItem {
    private Money price;
    private ProductItem productItem;

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
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
        return Objects.equals(price, orderItem.price) && Objects.equals(productItem, orderItem.productItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, productItem);
    }
}

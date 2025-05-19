package com.sabre.interview.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private String orderId;
    private List<OrderItem> orderItems = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();
    private OrderStatus status;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId) && Objects.equals(orderItems, order.orderItems) && Objects.equals(payments, order.payments) && status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderItems, payments, status);
    }
}

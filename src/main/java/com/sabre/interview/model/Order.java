package com.sabre.interview.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Order {
    private String orderId;
    private List<OrderItem> orderItems = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();
    private OrderStatus status;
}

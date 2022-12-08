package com.sabre.interview.model;

import lombok.Data;

@Data
public class OrderItem {
    private double price;
    private ProductItem productItem;
}

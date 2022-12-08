package com.sabre.interview.model;

import lombok.Data;

@Data
public class Payment {
    String paymentMethod;
    double paymentAmount;
}

package com.sabre.interview.model;

import org.javamoney.moneta.Money;

import java.util.Objects;

public class Payment {
    String paymentMethod;
    Money paymentAmount;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Money getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Money paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(paymentMethod, payment.paymentMethod) && Objects.equals(paymentAmount, payment.paymentAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentMethod, paymentAmount);
    }
}

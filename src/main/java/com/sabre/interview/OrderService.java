package com.sabre.interview;

import com.sabre.interview.infrastructure.CreditCardExternalPaymentSystem;
import com.sabre.interview.infrastructure.VoucherPaymentSystem;
import com.sabre.interview.infrastructure.ExternalReservationSystem;
import com.sabre.interview.infrastructure.InMemoryDatabase;
import com.sabre.interview.model.*;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import javax.money.NumberValue;
import java.util.List;
import java.util.UUID;

public class OrderService {

    private final ExternalReservationSystem externalReservationSystem;
    private final InMemoryDatabase inMemoryDatabase;
    private final CreditCardExternalPaymentSystem creditCardExternalPaymentSystem;
    private final VoucherPaymentSystem voucherPaymentSystem;

    public OrderService(ExternalReservationSystem externalReservationSystem, InMemoryDatabase inMemoryDatabase, CreditCardExternalPaymentSystem creditCardExternalPaymentSystem, VoucherPaymentSystem voucherPaymentSystem) {
        this.externalReservationSystem = externalReservationSystem;
        this.inMemoryDatabase = inMemoryDatabase;
        this.creditCardExternalPaymentSystem = creditCardExternalPaymentSystem;
        this.voucherPaymentSystem = voucherPaymentSystem;
    }

    public Order createOrder(List<OrderItem> orderItems){
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.getOrderItems().addAll(orderItems);

        for (OrderItem orderItem: orderItems){
            externalReservationSystem.reserveProduct(orderItem.getProductItem());
        }

        updateOrderStatus(order);

        inMemoryDatabase.insertOrUpdate(order.getOrderId(), order);
        return order;
    }

    public Order addOrderItem(String orderId, OrderItem orderItem) {
        Order order = inMemoryDatabase.retrieve(orderId);
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);

        updateOrderStatus(order);

        inMemoryDatabase.insertOrUpdate(order.getOrderId(), order);
        return order;
    }

    public Order addPayment(String orderId, Payment payment) {
        Order order = inMemoryDatabase.retrieve(orderId);
        List<Payment> payments = order.getPayments();
        payments.add(payment);
        order.setPayments(payments);
        if("CREDIT_CARD".equals(payment.getPaymentMethod())){
            creditCardExternalPaymentSystem.collectPayment(payment);
        }
        if("VOUCHER_PAYMENT".equals(payment.getPaymentMethod())){
            voucherPaymentSystem.collectPayment(payment);
        }

        updateOrderStatus(order);

        inMemoryDatabase.insertOrUpdate(order.getOrderId(), order);
        return order;
    }

    /**
     * If sum of payments is equal or exceeds sum of prices of
     * all order items set order status to FULLY_PAID
     * Otherwise set it to RESERVED
     */
    private static void updateOrderStatus(Order order) {
        order.setStatus(OrderStatus.RESERVED);

        // Calculate the sum of order item prices
        Money totalPrice = Money.of(0, Monetary.getCurrency("USD"));
        for (int i = 0; i < order.getOrderItems().size(); i++) {
            Money tmpPrice = order.getOrderItems().get(i).getPrice();
            totalPrice = totalPrice.add(tmpPrice);
        }

        // Calculate the sum of payment amounts
        Money paidAmount = Money.of(0, Monetary.getCurrency("USD"));
        for (int i = 0; i < order.getPayments().size(); i++) {
            Money tmpPrice = order.getPayments().get(i).getPaymentAmount();
            paidAmount = paidAmount.add(tmpPrice);
        }

        if (paidAmount.isGreaterThanOrEqualTo(totalPrice)) {
            order.setStatus(OrderStatus.FULLY_PAID);
        }
    }
}

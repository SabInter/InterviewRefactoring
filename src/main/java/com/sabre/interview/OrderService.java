package com.sabre.interview;

import com.sabre.interview.infrastructure.CreditCardExternalPaymentSystem;
import com.sabre.interview.infrastructure.VoucherPaymentSystem;
import com.sabre.interview.infrastructure.ExternalReservationSystem;
import com.sabre.interview.infrastructure.InMemoryDatabase;
import com.sabre.interview.model.*;

import java.util.List;
import java.util.UUID;

public class OrderService {

    private ExternalReservationSystem externalReservationSystem;
    private InMemoryDatabase inMemoryDatabase;
    private CreditCardExternalPaymentSystem creditCardExternalPaymentSystem;
    private VoucherPaymentSystem voucherPaymentSystem;

    public void setExternalReservationSystem(ExternalReservationSystem externalReservationSystem) {
        this.externalReservationSystem = externalReservationSystem;
    }

    public void setInMemoryDatabase(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    public void setCreditCardExternalPaymentSystem(CreditCardExternalPaymentSystem creditCardExternalPaymentSystem) {
        this.creditCardExternalPaymentSystem = creditCardExternalPaymentSystem;
    }

    public void setVoucherPaymentSystem(VoucherPaymentSystem voucherPaymentSystem) {
        this.voucherPaymentSystem = voucherPaymentSystem;
    }

    public Order createOrder(List<OrderItem> orderItems){
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.getOrderItems().addAll(orderItems);

        for (OrderItem orderItem: orderItems){
            externalReservationSystem.reserveProduct(orderItem.getProduct());
        }

        // Updating order status - RESERVED - 1, FULLY_PAID - 2
        OrderStatus reservedOrder = new OrderStatus();
        reservedOrder.setStatusId(1);
        order.setStatus(reservedOrder);

        inMemoryDatabase.insertOrUpdate(order.getOrderId(), order);
        return order;
    }

    public Order createOrderWithPayment(List<OrderItem> orderItems, Payment payment) {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.getOrderItems().addAll(orderItems);
        order.getPayments().add(payment);

        // Updating order status - RESERVED - 1, FULLY_PAID - 2
        OrderStatus reservedOrder = new OrderStatus();
        reservedOrder.setStatusId(1);
        order.setStatus(reservedOrder);

        double totalPrice = 0;
        for (int i = 0; i < order.getOrderItems().size(); i++) {
            double tmpPrice = order.getOrderItems().get(i).getPrice();
            totalPrice += tmpPrice;
        }

        if (payment.getPaymentAmount() >= totalPrice) {
            OrderStatus paidOrder = new OrderStatus();
            paidOrder.setStatusId(2);
            order.setStatus(paidOrder);
        }

        inMemoryDatabase.insertOrUpdate(order.getOrderId(), order);
        return order;
    }

    public Order addOrderItem(String orderId, OrderItem orderItem) {
        Order order = inMemoryDatabase.retrieve(orderId);
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);

        // Updating order status - RESERVED - 1, FULLY_PAID - 2
        OrderStatus reservedOrder = new OrderStatus();
        reservedOrder.setStatusId(1);
        order.setStatus(reservedOrder);

        double totalPrice = 0;
        for (int i = 0; i < order.getOrderItems().size(); i++) {
            double tmpPrice = order.getOrderItems().get(i).getPrice();
            totalPrice += tmpPrice;
        }

        double paidAmount = 0;
        for (int i = 0; i < order.getPayments().size(); i++) {
            double tmpPrice = order.getPayments().get(i).getPaymentAmount();
            paidAmount += tmpPrice;
        }

        if (paidAmount >= totalPrice) {
            OrderStatus paidOrder = new OrderStatus();
            paidOrder.setStatusId(2);
            order.setStatus(paidOrder);
        }

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

        // Updating order status - RESERVED - 1, FULLY_PAID - 2
        OrderStatus reservedOrder = new OrderStatus();
        reservedOrder.setStatusId(1);
        order.setStatus(reservedOrder);

        double totalPrice = 0;
        for (int i = 0; i < order.getOrderItems().size(); i++) {
            double tmpPrice = order.getOrderItems().get(i).getPrice();
            totalPrice += tmpPrice;
        }

        double paidAmount = 0;
        for (int i = 0; i < order.getPayments().size(); i++) {
            double tmpPrice = order.getPayments().get(i).getPaymentAmount();
            paidAmount += tmpPrice;
        }

        if (paidAmount >= totalPrice) {
            OrderStatus paidOrder = new OrderStatus();
            paidOrder.setStatusId(2);
            order.setStatus(paidOrder);
        }

        inMemoryDatabase.insertOrUpdate(order.getOrderId(), order);
        return order;
    }
}

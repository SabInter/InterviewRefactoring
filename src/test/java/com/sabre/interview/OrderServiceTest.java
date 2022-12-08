package com.sabre.interview;

import com.sabre.interview.infrastructure.CreditCardExternalPaymentSystem;
import com.sabre.interview.infrastructure.ExternalReservationSystem;
import com.sabre.interview.infrastructure.InMemoryDatabase;
import com.sabre.interview.infrastructure.VoucherPaymentSystem;
import com.sabre.interview.model.Order;
import com.sabre.interview.model.OrderItem;
import com.sabre.interview.model.OrderStatus;
import com.sabre.interview.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest {
    OrderService orderService;

    @BeforeEach
    public void setUp(){
        orderService = new OrderService();
        orderService.setInMemoryDatabase(new InMemoryDatabase());
        orderService.setExternalReservationSystem(new ExternalReservationSystem());
        orderService.setCreditCardExternalPaymentSystem(new CreditCardExternalPaymentSystem());
        orderService.setVoucherPaymentSystem(new VoucherPaymentSystem());
    }

    @Test
    void orderServiceCreateTest(){
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(10.12);
        OrderStatus reserved = new OrderStatus();
        reserved.setStatusId(1);

        Order result = orderService.createOrder(List.of(orderItem));

        //TODO: How to test UUID???
        assertThat(result.getStatus()).isEqualTo(reserved);
        assertThat(result.getOrderItems()).isEqualTo(List.of(orderItem));
    }

    @Test
    void orderServiceAddOrderItem(){
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(10.12);
        OrderStatus reserved = new OrderStatus();
        reserved.setStatusId(1);

        Order result = orderService.createOrder(List.of(orderItem));

        OrderItem newOrderItem = new OrderItem();
        orderItem.setPrice(10.00);

        Order newResult = orderService.addOrderItem(result.getOrderId(), newOrderItem);

        assertThat(newResult.getStatus()).isEqualTo(reserved);
        assertThat(newResult.getOrderItems()).isEqualTo(List.of(orderItem, newOrderItem));
    }

    @Test
    void orderServiceAddPayment(){
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(10.12);
        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setStatusId(2);

        Order result = orderService.createOrder(List.of(orderItem));

        Payment payment = new Payment();
        payment.setPaymentMethod("CREDIT");
        payment.setPaymentAmount(10.12);

        Order newResult = orderService.addPayment(result.getOrderId(), payment);

        assertThat(newResult.getStatus()).isEqualTo(paidStatus);
        assertThat(newResult.getOrderItems()).containsAll(List.of(orderItem));
        assertThat(newResult.getPayments()).containsAll(List.of(payment));
    }
}

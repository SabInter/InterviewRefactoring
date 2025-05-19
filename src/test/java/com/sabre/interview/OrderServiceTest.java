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

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest {
    OrderService orderService;

    @BeforeEach
    public void setUp(){
        orderService = new OrderService(new ExternalReservationSystem(), new InMemoryDatabase(), new CreditCardExternalPaymentSystem(), new VoucherPaymentSystem());
    }

    @Test
    void orderServiceCreateTest(){
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(10.12);

        Order result = orderService.createOrder(Arrays.asList(orderItem));

        //TODO: How to test UUID???
        assertThat(result.getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(result.getOrderItems()).isEqualTo(Arrays.asList(orderItem));
    }

    @Test
    void orderServiceAddOrderItem(){
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(10.12);

        Order result = orderService.createOrder(Arrays.asList(orderItem));

        OrderItem newOrderItem = new OrderItem();
        orderItem.setPrice(10.00);

        Order newResult = orderService.addOrderItem(result.getOrderId(), newOrderItem);

        assertThat(newResult.getStatus()).isEqualTo(OrderStatus.RESERVED);
        assertThat(newResult.getOrderItems()).isEqualTo(Arrays.asList(orderItem, newOrderItem));
    }

    @Test
    void orderServiceAddPayment(){
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(10.12);

        Order result = orderService.createOrder(Arrays.asList(orderItem));

        Payment payment = new Payment();
        payment.setPaymentMethod("CREDIT");
        payment.setPaymentAmount(10.12);

        Order newResult = orderService.addPayment(result.getOrderId(), payment);

        assertThat(newResult.getStatus()).isEqualTo(OrderStatus.FULLY_PAID);
        assertThat(newResult.getOrderItems()).containsAll(Arrays.asList(orderItem));
        assertThat(newResult.getPayments()).containsAll(Arrays.asList(payment));
    }
}

package com.sabre.interview.infrastructure;

import com.sabre.interview.model.Order;

import java.util.HashMap;
import java.util.Map;

//TODO: In memory DB created for purpose of exercise
// Call to real DB will be here
public class InMemoryDatabase {
    Map<String, Order> inMemoryDB = new HashMap<>();

    public void insertOrUpdate(String orderId, Order order){
        inMemoryDB.put(orderId, order);
    }

    public Order retrieve(String orderId){
        return inMemoryDB.get(orderId);
    }
}

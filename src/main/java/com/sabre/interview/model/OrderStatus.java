package com.sabre.interview.model;

import java.util.Objects;

public class OrderStatus {

    int statusId;

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        if(statusId <= 0 || statusId > 2){
            throw new RuntimeException("Invalid com.sabre.interview.Order Status");
        }
        this.statusId = statusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatus that = (OrderStatus) o;
        return statusId == that.statusId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusId);
    }
}

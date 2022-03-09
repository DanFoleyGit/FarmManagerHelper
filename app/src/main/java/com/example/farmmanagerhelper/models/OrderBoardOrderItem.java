package com.example.farmmanagerhelper.models;

public class OrderBoardOrderItem {
    String name;
    String quantity;
    boolean orderComplete;

    public OrderBoardOrderItem(String name, String quantity, boolean orderComplete) {
        this.name = name;
        this.quantity = quantity;
        this.orderComplete = orderComplete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public boolean isOrderComplete() {
        return orderComplete;
    }

    public void setOrderComplete(boolean orderComplete) {
        this.orderComplete = orderComplete;
    }
}

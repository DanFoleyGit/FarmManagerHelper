package com.example.farmmanagerhelper.models;

public class Order {
    String orderID;
    String orderDate;
    String customer;
    String product;
    String quantity;
    boolean orderComplete;

    public Order(String orderID, String orderDate, String customer, String product, String quantity, boolean orderComplete) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.orderComplete = orderComplete;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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

package com.example.farmmanagerhelper.models;

public class Customer {
    String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Customer(String customerName) {
        this.customerName = customerName;
    }
}

package com.example.farmmanagerhelper.models;

public class Product {
    String productName;
    String customerItBelongsTo;
    String productID;

    public Product(String productName, String customerItBelongsTo, String productID) {
        this.productName = productName;
        this.customerItBelongsTo = customerItBelongsTo;
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCustomerItBelongsTo() {
        return customerItBelongsTo;
    }

    public void setCustomerItBelongsTo(String customerItBelongsTo) {
        this.customerItBelongsTo = customerItBelongsTo;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

}

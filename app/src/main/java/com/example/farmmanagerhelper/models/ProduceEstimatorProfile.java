package com.example.farmmanagerhelper.models;

public class ProduceEstimatorProfile {
    String profileName;
    String profileID;
    String weightOfRawUnit;
    String weightOfFinishedProduct;
    String numProductsPerFinishedUnit;
    String marginForWastage;

    public ProduceEstimatorProfile(String profileName, String profileID, String weightOfRawUnit, String weightOfFinishedProduct, String numProductsPerFinishedUnit, String marginForWastage) {
        this.profileName = profileName;
        this.profileID = profileID;
        this.weightOfRawUnit = weightOfRawUnit;
        this.weightOfFinishedProduct = weightOfFinishedProduct;
        this.numProductsPerFinishedUnit = numProductsPerFinishedUnit;
        this.marginForWastage = marginForWastage;
    }

    public ProduceEstimatorProfile() {
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getWeightOfRawUnit() {
        return weightOfRawUnit;
    }

    public void setWeightOfRawUnit(String weightOfRawUnit) {
        this.weightOfRawUnit = weightOfRawUnit;
    }

    public String getWeightOfFinishedProduct() {
        return weightOfFinishedProduct;
    }

    public void setWeightOfFinishedProduct(String weightOfFinishedProduct) {
        this.weightOfFinishedProduct = weightOfFinishedProduct;
    }

    public String getNumProductsPerFinishedUnit() {
        return numProductsPerFinishedUnit;
    }

    public void setNumProductsPerFinishedUnit(String numProductsPerFinishedUnit) {
        this.numProductsPerFinishedUnit = numProductsPerFinishedUnit;
    }

    public String getMarginForWastage() {
        return marginForWastage;
    }

    public void setMarginForWastage(String marginForWastage) {
        this.marginForWastage = marginForWastage;
    }
}

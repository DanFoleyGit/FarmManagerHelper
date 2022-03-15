package com.example.farmmanagerhelper.models;

public class ShippingCalculatorProfile {
    String profileName;
    String profileID;
    String profilePreferredBoxesPerPallet;
    String profileMaximumBoxesPerPallet;

    public ShippingCalculatorProfile(String profileName, String profileID, String profilePreferredBoxesPerPallet, String profileMaximumBoxesPerPallet) {
        this.profileName = profileName;
        this.profileID = profileID;
        this.profilePreferredBoxesPerPallet = profilePreferredBoxesPerPallet;
        this.profileMaximumBoxesPerPallet = profileMaximumBoxesPerPallet;
    }

    public ShippingCalculatorProfile()
    {

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

    public String getProfilePreferredBoxesPerPallet() {
        return profilePreferredBoxesPerPallet;
    }

    public void setProfilePreferredBoxesPerPallet(String profilePreferredBoxesPerPallet) {
        this.profilePreferredBoxesPerPallet = profilePreferredBoxesPerPallet;
    }

    public String getProfileMaximumBoxesPerPallet() {
        return profileMaximumBoxesPerPallet;
    }

    public void setProfileMaximumBoxesPerPallet(String profileMaximumBoxesPerPallet) {
        this.profileMaximumBoxesPerPallet = profileMaximumBoxesPerPallet;
    }
}

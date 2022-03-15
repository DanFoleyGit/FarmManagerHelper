package com.example.farmmanagerhelper.models;

import java.util.ArrayList;

public class Farm {
    public String farmName;
    public String farmPasscode;
    public String managerID;
    public String usersInFarm;
    public String customers;
    public String shippingCalculatorProfiles;
    public String produceEstimatorProfiles;

    public Farm(String farmName, String farmPasscode, String managerID, String usersInFarm, String customers, String shippingCalculatorProfiles, String produceEstimatorProfiles) {
        this.farmName = farmName;
        this.farmPasscode = farmPasscode;
        this.managerID = managerID;
        this.usersInFarm = usersInFarm;
        this.customers = customers;
        this.shippingCalculatorProfiles = shippingCalculatorProfiles;
        this.produceEstimatorProfiles = produceEstimatorProfiles;
    }
}

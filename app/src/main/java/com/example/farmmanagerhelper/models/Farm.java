package com.example.farmmanagerhelper.models;

public class Farm {
    String farmName;
    String farmPasscode;
    String farmID;
    String managerID;
    String orderBoardID;
    String timeTableID;

    public Farm(String farmName, String farmPasscode, String farmID, String managerID,
                    String orderBoardID, String timeTableID)
    {
        this.farmName = farmName;
        this.farmPasscode = farmPasscode;
        this.farmID = farmID;
        this.managerID = managerID;
        this.orderBoardID = orderBoardID;
        this.timeTableID = timeTableID;
    }
}

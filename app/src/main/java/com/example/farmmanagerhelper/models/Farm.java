package com.example.farmmanagerhelper.models;

import java.util.ArrayList;

public class Farm {
    public String farmName;
    public String farmPasscode;
    public String farmID;
    public String managerID;
    public String orderBoardID;
    public String timeTableID;
    public String usersInFarm;

    public Farm(String farmName, String farmPasscode, String farmID, String managerID,
                    String orderBoardID, String timeTableID, String usersInFarm)
    {
        this.farmName = farmName;
        this.farmPasscode = farmPasscode;
        this.farmID = farmID;
        this.managerID = managerID;
        this.orderBoardID = orderBoardID;
        this.timeTableID = timeTableID;
        this.usersInFarm = usersInFarm;
    }
}

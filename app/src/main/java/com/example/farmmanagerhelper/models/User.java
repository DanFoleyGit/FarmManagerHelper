package com.example.farmmanagerhelper.models;

public class User {

    public String userId;
    public String firstName;
    public String lastName;
    public String email;
    public String UserTableFarmId;

    public User(String userId, String firstName, String lastName, String email, String UserTableFarmId){
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.UserTableFarmId = UserTableFarmId;
        //this.userId = userId;
    }

}

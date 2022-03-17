package com.example.farmmanagerhelper;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.farmmanagerhelper.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;


/*
UserServices provides validation and Firebase Functionality to Login and RegisterAccount
 */
public class UserServices {
    // check passwords match

    public static boolean checkPasswords(EditText pWord, EditText pWordCon)
    {
        Log.d("password is ", pWord.getText().toString());
        Log.d("password confirm is ", pWordCon.getText().toString());
        if(!pWord.getText().toString().equals(pWordCon.getText().toString()))
        {
            return false;
        }

        return true;
    }

    public static boolean checkPasswordLength(EditText pWord)
    {
        //check the length of password
        if(pWord.getText().toString().length() < 8)
        {
            return false;
        }

        return true;
    }

    public static boolean checkEmailAndpasswordAreNotEmpty(EditText email, EditText pWord)
    {
        if(TextUtils.isEmpty(email.getText().toString()) ||
                TextUtils.isEmpty(pWord.getText().toString()) )
        {
            return false;
        }

        return true;
    }

    public static boolean checkFieldsAreNotEmpty(EditText email, EditText pWord, EditText pWordConf)
    {
        if(TextUtils.isEmpty(email.getText().toString()) ||
                TextUtils.isEmpty(pWord.getText().toString()) ||
                TextUtils.isEmpty(pWordConf.getText().toString()))
        {
            return false;
        }

        return true;
    }



    // Check if the password is in email format allowing for text.text@mailprovider.co.uk
    public static boolean validateEmail(EditText email)
    {
        String regex = "^^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        if (email.getText().toString().matches("^^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
        {
            return true;
        }

        return false;

    }

    //signs user out
    public static void SignOutAccount(){
        Log.d("Signing out","User signed out");
        FirebaseAuth.getInstance().signOut();
    }


    // Sets the users farm back to "none and changes the value of currentlyInFarm to false in
    // farm_table/farmName/usersInFarm/userId/currentlyInFarm
    //
    public static void leaveFarm() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // get the farm id
        //
        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());


        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();
                Log.d("OrdersBoardServices updateOrderBoardWithDate", "farm Id is " + farmId);


                Log.d("farmname is "," "+ farmId);

                DatabaseReference dbUsersInFarmRef = DatabaseManager.getUsersInFarmDocumentByFarmName(farmId);

                DatabaseManager.setFarmInUserToNone(dbRef);


                Log.d("farmname is "," "+ dbUsersInFarmRef);


                dbUsersInFarmRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DatabaseManager.setCurrentlyInFarmToFalseForUser(dbUsersInFarmRef, currentUser.getUid());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("error", error.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }
}

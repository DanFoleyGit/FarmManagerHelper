package com.example.farmmanagerhelper;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.farmmanagerhelper.models.Farm;
import com.example.farmmanagerhelper.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseManager {
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference();

    // other variables
    String stringIndicatingUserIsNotInFarm = "none";

    public static boolean addUserToDatabase(User user) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();


        // add user object to the table making the user ID the childnode/ primary key
        dbRef.child("users").child(user.userId).setValue(user);

        return true;
    }

    public static boolean addFarmToDatabase(Farm farm)
    {
        return true;
    }

    // checks the user id is in the database and checks if the user id is in a farm. returns true
    // if it is and false if not
    public static DatabaseReference getDatabaseRefForUserId(FirebaseUser firebaseUser)
    {

        // variable
        String stringIndicatingUserIsNotInFarm = "none";

        // https://stackoverflow.com/questions/67832715/how-to-retrieve-specific-data-from-firebase-realtime-database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference().child("users").child(firebaseUser.getUid());

        return dbRef;

        // check i the user is in the database
//        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists())
//                {
//                    String farmId = snapshot.child("UserTableFarmId").getValue().toString();
//
//                    if(farmId.equals(stringIndicatingUserIsNotInFarm))
//                    {
//                        Log.d("UserTableFarmId is ", snapshot.child("UserTableFarmId").getValue().toString());
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }
}

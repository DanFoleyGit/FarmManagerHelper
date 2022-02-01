package com.example.farmmanagerhelper;

import android.widget.Toast;

import com.example.farmmanagerhelper.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference();

    public static boolean addUserToDatabase(FirebaseUser firebaseUser, User user) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();

        dbRef.child("users").child(firebaseUser.getUid()).setValue(user);

        return true;
    }
}

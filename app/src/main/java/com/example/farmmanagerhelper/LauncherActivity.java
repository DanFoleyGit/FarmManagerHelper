package com.example.farmmanagerhelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LauncherActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Boolean inFarm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        // check if the user is logged. If they are go to the login activity and close main activity
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Log.d("LauncherActivity", "Checking user to open correct activity");

        // allow current user to be null and open login screen on new startup
        //
        if(currentUser == null)
        {
            Log.d("LauncherActivity", "User reference is null");
            Intent intent = new Intent(LauncherActivity.this, com.example.farmmanagerhelper.LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            checkUserIsInFarm(currentUser);
        }
    }

    private void checkUserIsInFarm(FirebaseUser currentUser) {

        // get dbref for user id
        DatabaseReference dbRef = DatabaseManager.getDatabaseRefForUserId(currentUser);

        // variables
        String stringIndicatingUserIsNotInFarm = "none";

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();
                Log.d("LauncherActivity UserTableFarmId is ", farmId);

                if(farmId.equals(stringIndicatingUserIsNotInFarm))
                {
                    Log.d("LauncherActivity", "User is not in a farm");
                    Intent intent = new Intent(LauncherActivity.this, com.example.farmmanagerhelper.JoinFarm.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Log.d("LauncherActivity", "User is not in a farm");
                    Intent intent = new Intent(LauncherActivity.this, com.example.farmmanagerhelper.MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }
}
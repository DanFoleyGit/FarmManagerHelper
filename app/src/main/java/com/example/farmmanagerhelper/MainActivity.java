package com.example.farmmanagerhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // UI
        Button JoinActivity = findViewById((R.id.JoinActivity));
        TextView UserEmail = findViewById(R.id.txtMainActivityLoggedInAsUserX);

        // check if the user is logged. If they are go to the login activity and close main activity
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            UserEmail.setText("Hello " + currentUser.getEmail());

        }
        // check if the user is in a farm
        if(checkUserIsInFarm(currentUser))
        {
            Log.d("MainActivity", "User checked and is in farm");
            Toast toast = Toast.makeText(MainActivity.this, "User checked", Toast.LENGTH_SHORT);
            toast.show();
        }

        else{
            Intent intent = new Intent(MainActivity.this, com.example.farmmanagerhelper.LoginActivity.class);
            startActivity(intent);
            finish();
        }


        JoinActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this, com.example.farmmanagerhelper.JoinFarm.class);
                startActivity(intent);
                finish();


            }
        });


    }

    private boolean checkUserIsInFarm(FirebaseUser currentUser) {

        // get dbref for user id
        DatabaseReference dbRef = DatabaseManager.getDatabaseRefForUserId(currentUser);

        // variables
        String stringIndicatingUserIsNotInFarm = "none";

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();
                Log.d("UserTableFarmId is ", snapshot.child("UserTableFarmId").getValue().toString());

                    if(farmId.equals(stringIndicatingUserIsNotInFarm))
                    {
                        Intent intent = new Intent(MainActivity.this, com.example.farmmanagerhelper.JoinFarm.class);
                        startActivity(intent);
                        finish();

                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
        return true;
    }

    // Menu Icon in top left
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.menu,menu );
        return super.onCreateOptionsMenu ( menu );

    }

    // Logout user from user inputs in the action bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            // set up as a case satement to allow extra options if needed
            case R.id.MenuLogout:
                // sign out user

                UserServices.SignOutAccount();
                Toast.makeText(MainActivity.this, "User signed out",
                        Toast.LENGTH_SHORT).show();

                // start login intent
                Intent intent = new Intent(MainActivity.this, com.example.farmmanagerhelper.LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }
        return true;
    }
}
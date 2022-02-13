package com.example.farmmanagerhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class JoinFarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_farm);

        Button joinFarmButton = findViewById(R.id.buttonJoinFarm);
        Button createFarmActivity = findViewById(R.id.buttonOpenCreateFarmActivity);


        joinFarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addUserToFarm();
            }
        });

        createFarmActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinFarm.this, com.example.farmmanagerhelper.CreateFarm.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addUserToFarm() {
        // UI
        EditText joinFarmName = findViewById(R.id.editTextJoinFarmName);
        EditText joinFarmPassCode = findViewById(R.id.editTextJoinFarmPasscode);
        TextView joinFarmErrorMessage = findViewById(R.id.joinFarmErrorMessage);


        //firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        boolean isValid = true;

        Log.d("JoinFarm validation :","Beginning");
        while(isValid) {

            isValid = FarmService.InputsNotEmpty(joinFarmName.getText().toString(),joinFarmPassCode.getText().toString());
            if(!isValid)
            {
                Log.d("JoinFarm validation :","empty inputs");
                joinFarmErrorMessage.setText("Inputs must be filled in");
                break;
            }

            isValid = (FarmService.InputsDontContainWhiteSpaces(joinFarmName.getText().toString(),joinFarmPassCode.getText().toString()));
            if(!isValid)
            {
                Log.d("CreateFarm validation :","name or passcode has whitepsaces");
                joinFarmErrorMessage.setText("Name and Passcode cant have spaces");
                break;
            }

            if(isValid)
            {
                // add user
                Log.d("JoinFarm validation"," passed");

                DatabaseReference dbRef = DatabaseManager.getDatabaseReference();

                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("farm_table").child(joinFarmName.getText().toString()).exists())
                        {
                            Log.d("JoinFarm found farm in db :", joinFarmName.getText().toString());

                            // get snapshot of database and add user to it
                            if(snapshot.child("farm_table").child(joinFarmName.getText().toString()).child("farmPasscode").getValue().toString()
                                    .equals(joinFarmPassCode.getText().toString()))
                            {
                                // add user to farm and and farm name to user
                                Log.d("JoinFarm confirmed passcode and adding user to farm in db :", joinFarmName.getText().toString());

                                DatabaseManager.AddFarmNameToUserAndUserToFarm(joinFarmName.getText().toString(),firebaseUser.getUid());
                                // Go to mainActivity
                                startActivity(new Intent(JoinFarm.this, MainActivity.class));
                                finish();
                            }
                            else
                            {
                                Log.d("JoinFarm passCode was wrong for the farm :", joinFarmPassCode.getText().toString());
                            }
                        }
                        else{
                            Log.d("JoinFarm did not find farm in db :", joinFarmName.getText().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("createFarm error", error.toString());
                    }
                });

                joinFarmErrorMessage.setText("Farm name or pass code is incorrect");

                break;
            }
        }


    }
}
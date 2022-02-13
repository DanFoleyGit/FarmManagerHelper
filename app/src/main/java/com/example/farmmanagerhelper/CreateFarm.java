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

import com.example.farmmanagerhelper.models.Farm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class CreateFarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_farm);

        // UI
        Button createFarmButton = findViewById(R.id.buttonCreateFarm);
        Button joinFarmActivity = findViewById(R.id.buttonJoinFarmActivity);


        createFarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addFarmToDatabase();

            }
        });

        joinFarmActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateFarm.this, com.example.farmmanagerhelper.JoinFarm.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void addFarmToDatabase() {
        // UI
        EditText createFarmName = findViewById(R.id.editTextCreateFarmName);
        EditText createFarmPassCode = findViewById(R.id.editTextCreateFarmPasscode);
        TextView createFarmErrorMessage = findViewById(R.id.createFarmErrorMessage);


        //firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        // model
        Farm farm;

        boolean isValid = true;

        Log.d("CreateFarm validation :","Beginning");
        while(isValid)
        {
            isValid = FarmService.InputsNotEmpty(createFarmName.getText().toString(),createFarmPassCode.getText().toString());
            if(!isValid)
            {
                Log.d("CreateFarm validation :","empty inputs");
                createFarmErrorMessage.setText("Inputs must be filled in");
                break;
            }
            createFarmErrorMessage.setText("");

            Log.d("CreateFarm check if inputs have whitespaces:", createFarmName.getText().toString());

            isValid = (FarmService.InputsDontContainWhiteSpaces(createFarmName.getText().toString(),createFarmPassCode.getText().toString()));
            if(!isValid)
            {
                Log.d("CreateFarm validation :","name or passcode has whitepsaces");
                createFarmErrorMessage.setText("Name and Passcode cant have spaces");
                break;
            }

            // create farm object with current user id set as manager, and adding the users name to the list of users
            farm = new Farm(createFarmName.getText().toString(),createFarmPassCode.getText().toString(),
                    "0001",firebaseUser.getUid(),"0001","0001", firebaseUser.getUid());

            if(isValid)
            {

                Log.d("CreateFarm validation :","passed");

                // DatabaseReference dbRef = DatabaseManager.getDatabaseRefForFarmName(createFarmName.getText().toString());

                DatabaseReference dbRef = DatabaseManager.getDatabaseReference();



                Farm finalFarm = farm;
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(!snapshot.child("farm_table").child(createFarmName.getText().toString()).exists()) {

                            Log.d("CreateFarm adding farm to database and user :", createFarmName.getText().toString());
                            //call db stuff
                            DatabaseManager.addFarmToDatabase(finalFarm);
                            DatabaseManager.AddFarmNameToUserAndUserToFarm(finalFarm.farmName, finalFarm.managerID);

                            // Go to mainActivity
                            startActivity(new Intent(CreateFarm.this, MainActivity.class));
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("createFarm error", error.toString());
                    }
                });

                Log.d("CreateFarm farm already exists :", createFarmName.getText().toString());

                createFarmErrorMessage.setText("Farm already exists");
                break;
            }

        }


    }

    private boolean checkIfFarmAlreadyExist(String farmNameFromInput, Farm farm) {

        Log.d("CreateFarm at checkFarmDoesNotAlreadyExist with :", farmNameFromInput);

        // get dbref for the farm name from the farm table
        DatabaseReference dbRef = DatabaseManager.getDatabaseReference();

        // get key will return null if it does not exist ////////////////////////
        Log.d("CreateFarm dbref :", dbRef.getKey());

        if(dbRef.child("farm_table").child(farm.farmName) == null)
        {
            Log.d("dbref is null:", farmNameFromInput);
            return true;
        }
        else
        {
            Log.d("dbref exists:", farmNameFromInput);
            return false;
        }

    }
}
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmmanagerhelper.models.Farm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class CreateFarm extends AppCompatActivity {


    //firebase
    FirebaseAuth mAuth = null;
    FirebaseUser firebaseUser = null;

    Button createFarmButton = null;
    Button joinFarmActivity = null;
    EditText createFarmName = null;
    EditText createFarmPassCode = null;
    TextView createFarmErrorMessage = null;
    ProgressBar progressIconCreateFarm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_farm);

        //firebase
        //
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        // UI
        //
        createFarmButton = findViewById(R.id.buttonCreateFarm);
        joinFarmActivity = findViewById(R.id.buttonJoinFarmActivity);
        createFarmName = findViewById(R.id.editTextCreateFarmName);
        createFarmPassCode = findViewById(R.id.editTextCreateFarmPasscode);
        createFarmErrorMessage = findViewById(R.id.createFarmErrorMessage);
        progressIconCreateFarm = findViewById(R.id.progressIconCreateFarm);


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

        // model
        //
        Farm farm;

        boolean isValid = true;

        // set UI to loading state
        //
        loadingState();

        Log.d("CreateFarm validation :","Beginning");
        while(isValid)
        {
            isValid = FarmService.InputsNotEmpty(createFarmName.getText().toString(),createFarmPassCode.getText().toString());
            if(!isValid)
            {
                Log.d("CreateFarm validation :","empty inputs");
                createFarmErrorMessage.setText("Inputs must be filled in");
                activeState();
                break;
            }
            createFarmErrorMessage.setText("");

            Log.d("CreateFarm check if inputs have whitespaces:", createFarmName.getText().toString());

            isValid = (FarmService.InputsDontContainWhiteSpaces(createFarmName.getText().toString(),createFarmPassCode.getText().toString()));
            if(!isValid)
            {
                Log.d("CreateFarm validation :","name or passcode has whitepsaces");
                createFarmErrorMessage.setText("Name and Passcode cant have spaces");
                activeState();
                break;
            }
            createFarmErrorMessage.setText("");

            // create farm object with current user id set as manager, and adding the users name to the list of users
            //
            farm = new Farm(createFarmName.getText().toString(),createFarmPassCode.getText().toString(),
                    firebaseUser.getUid(),"","", "","");

            if(isValid)
            {
                Log.d("CreateFarm validation :","passed");

                DatabaseReference dbRef = DatabaseManager.getDatabaseReference();

                Farm finalFarm = farm;
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(!snapshot.child("farm_table").child(createFarmName.getText().toString()).exists()) {

                            Log.d("CreateFarm adding farm to database and user :", createFarmName.getText().toString());
                            //call db manager to perform updates to farm_table and the user
                            DatabaseManager.addFarmToDatabase(finalFarm);

                            // Go to mainActivity
                            startActivity(new Intent(CreateFarm.this, MainActivity.class));
                            finish();
                        }
                        else
                        {
                            createFarmErrorMessage.setText("Farm already exists.");
                            activeState();
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

    // Menu Icon in top right
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.menu_logout_only,menu );
        return super.onCreateOptionsMenu ( menu );

    }

    // Logout user options in the action bar
    //
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            // set up as a case statement to allow extra options if needed
            case R.id.MenuLogout:
                // sign out user

                UserServices.SignOutAccount();
                Toast.makeText(CreateFarm.this, "User signed out",
                        Toast.LENGTH_SHORT).show();

                // start login intent
                Intent intent = new Intent(CreateFarm.this, com.example.farmmanagerhelper.LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }
        return true;
    }


    // Disable the buttons to open new views
    //
    @Override
    protected void onPause() {
        super.onPause();
        loadingState();
    }

    // Enable the buttons
    //
    @Override
    protected void onResume() {
        super.onResume();
        activeState();
    }

    // Disable the buttons to open new views
    //
    private void loadingState()
    {
        createFarmButton.setEnabled(false);
        joinFarmActivity.setEnabled(false);
        createFarmName.setEnabled(false);
        createFarmPassCode.setEnabled(false);

        progressIconCreateFarm.setVisibility(View.VISIBLE);
    }

    // Enable the buttons
    //
    private void activeState()
    {
        createFarmButton.setEnabled(true);
        joinFarmActivity.setEnabled(true);
        createFarmName.setEnabled(true);
        createFarmPassCode.setEnabled(true);

        progressIconCreateFarm.setVisibility(View.GONE);
    }

}
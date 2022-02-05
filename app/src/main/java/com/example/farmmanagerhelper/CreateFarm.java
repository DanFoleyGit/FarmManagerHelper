package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.farmmanagerhelper.models.Farm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

                //String errormessage = addFarmToDatabase();

                Intent intent = new Intent(CreateFarm.this, com.example.farmmanagerhelper.MainActivity.class);
                startActivity(intent);
                finish();
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

    private String addFarmToDatabase() {
        // UI
        EditText createFarmName = findViewById(R.id.editTextCreateFarmName);
        EditText createFarmPasscode = findViewById((R.id.editTextCreateFarmPasscode));

        //firebase
        FirebaseAuth mAuth = null;
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        // model
        Farm farm;

        String firebaseMsg = null;

        if(!TextUtils.isEmpty(createFarmName.getText().toString()) ||
                !TextUtils.isEmpty(createFarmPasscode.getText().toString()))
        {
            Toast.makeText(CreateFarm.this, "Creating Farm.",
                    Toast.LENGTH_SHORT).show();
            //farm = new Farm(createFarmName,createFarmPasscode,001,firebaseUser.getUid(),0001,0001);
            //DatabaseManager.addFarmToDatabase();

            // create timetable

            //create orders board
        }
        return firebaseMsg;

    }
}
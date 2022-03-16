package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProduceEstimator extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Context context = this;

    Button buttonProduceEstimatorCalculate = null;
    Button buttonProduceEstimatorEditProfiles = null;
    EditText editTextProduceEstimatorQuantity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produce_estimator);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        buttonProduceEstimatorCalculate = findViewById(R.id.buttonProduceEstimatorCalculate);
        buttonProduceEstimatorEditProfiles = findViewById(R.id.buttonProduceEstimatorEditProfiles);
        editTextProduceEstimatorQuantity = findViewById(R.id.editTextProduceEstimatorQuantity);

        ToolServices.makeOptionsButtonVisibleForManager(currentUser, buttonProduceEstimatorEditProfiles);


        buttonProduceEstimatorEditProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProduceEstimator.this, ProduceEstimatorManagerProfiles.class));

            }
        });

    }
}
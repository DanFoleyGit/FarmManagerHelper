package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

                Intent intent = new Intent(JoinFarm.this, com.example.farmmanagerhelper.MainActivity.class);
                startActivity(intent);
                finish();
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
}
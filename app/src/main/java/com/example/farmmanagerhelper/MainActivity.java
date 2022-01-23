package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // heres a comment
        // Repo tests

        Button LoginActivity = findViewById((R.id.LoginActivity));

        LoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class activityClass = com.example.farmmanagerhelper.LoginActivity.class;
                Intent intent = new Intent(MainActivity.this, com.example.farmmanagerhelper.LoginActivity.class);

                startActivity(intent);
            }
        });

//        // takes in a class passed to it and opens it
//        private void openActivity(Class activityClass) {
//            Intent intent = new Intent(this, activityClass);
//            startActivity(intent);
//        }
    }
}
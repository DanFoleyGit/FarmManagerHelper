package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // heres a comment
        // Repo tests

        Button LoginActivity = findViewById((R.id.LoginActivity));

        Button signOut = findViewById(R.id.btnMainSignout);

        LoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class activityClass = com.example.farmmanagerhelper.LoginActivity.class;
                Intent intent = new Intent(MainActivity.this, com.example.farmmanagerhelper.LoginActivity.class);

                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserServices.SignOutAccount();

                Toast.makeText(MainActivity.this, "User signed out",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
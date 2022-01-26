package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // UI
        Button LoginActivity = findViewById((R.id.LoginActivity));
        Button signOut = findViewById(R.id.btnMainSignout);

        LoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth = FirebaseAuth.getInstance();

                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null){
                    //reload();

                    Toast toast = Toast.makeText(MainActivity.this, "User logged in already", Toast.LENGTH_SHORT);
                    toast.show();

                }
                else{
                    Intent intent = new Intent(MainActivity.this, com.example.farmmanagerhelper.LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

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
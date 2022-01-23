package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // FireBase
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        // UI components
        Button openRegisterAccountActivity = findViewById((R.id.btnRegisterNewUserAcitivty));
        Button logUserIn = findViewById(R.id.btnLoginUser);

        openRegisterAccountActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class activityClass = com.example.farmmanagerhelper.RegisterAccountActivity.class;
                Intent intent = new Intent(LoginActivity.this, activityClass);

                startActivity(intent);
            }
        });


        logUserIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call validation

                // call login user
            }
        });

    }
}
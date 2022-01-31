package com.example.farmmanagerhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
        TextView UserEmail = findViewById(R.id.txtMainActivityLoggedInAsUserX);

        // check if the user is logged. If they are go to the login activity and close main activity
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();

            Toast toast = Toast.makeText(MainActivity.this, "User logged in already", Toast.LENGTH_SHORT);
            toast.show();
            UserEmail.setText("Logged in as\n" + currentUser.getEmail());

        }
        else{
            Intent intent = new Intent(MainActivity.this, com.example.farmmanagerhelper.LoginActivity.class);
            startActivity(intent);
            finish();
        }


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


    }

    // Menu Icon in top left
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.menu,menu );
        return super.onCreateOptionsMenu ( menu );

    }

    // Logout user from user inputs in the action bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            // set up as a case satement to allow extra options if needed
            case R.id.MenuLogout:
                // sign out user
                UserServices.SignOutAccount();
                Toast.makeText(MainActivity.this, "User signed out",
                        Toast.LENGTH_SHORT).show();

                // start login intent
                Intent intent = new Intent(MainActivity.this, com.example.farmmanagerhelper.LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }
        return true;
    }
}
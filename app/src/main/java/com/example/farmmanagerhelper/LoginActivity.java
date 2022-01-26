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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class LoginActivity extends AppCompatActivity {

    //firebase auth
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // FireBase
        mAuth = FirebaseAuth.getInstance();

        // UI button components
        Button openRegisterAccountActivity = findViewById((R.id.btnRegisterNewUserAcitivty));
        Button logUserIn = findViewById(R.id.btnLoginUser);

        // listener to open registration view
        openRegisterAccountActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class activityClass = com.example.farmmanagerhelper.RegisterAccountActivity.class;
                Intent intent = new Intent(LoginActivity.this, activityClass);

                startActivity(intent);
            }
        });


        //log in user button
        logUserIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccount();
            }
        });

    }

    // check if the user is already logged in.
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();

            Toast toast = Toast.makeText(this, "User logged in already", Toast.LENGTH_SHORT);
            toast.show();

            // launch main menu
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void loginAccount()
        {
            // variables
            boolean isValid = true;

            // UI text components and error message
            EditText loginEmail = findViewById(R.id.EditTextLoginEmail);
            EditText loginPassword =findViewById(R.id.EditTextLoginPassword);
            TextView loginErrorMsg = findViewById(R.id.txtLoginErrorMsg);

            // call validation
            while(isValid)
            {
                isValid = UserServices.checkEmailAndpasswordAreNotEmpty(loginEmail, loginPassword);
                if(isValid == false)
                {
                    loginErrorMsg.setText("All fields Are Required");
                    break;
                }

                loginErrorMsg.setText("");

                isValid = UserServices.validateEmail(loginEmail);
                if(isValid == false)
                {
                    loginErrorMsg.setText("Email not in correct format");
                    break;
                }

                loginErrorMsg.setText("");

                isValid = UserServices.checkPasswordLength(loginPassword);
                if(isValid == false)
                {
                    loginErrorMsg.setText("Passwords must be at least 8 characters");
                    break;
                }

                loginErrorMsg.setText("");

                // call login user
                if(isValid)
                {

                    // sign in user with firebase
                    LoginUserToFirebaseAuth(loginEmail.getText().toString(), loginPassword.getText().toString(), loginErrorMsg);
                    break;
                }
            }
        }

        // uses FireBases Login with email and password to log user in and sends it to the main activity
        private void LoginUserToFirebaseAuth(String email, String password, TextView LoginErrorMsg)
        {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // clear error message
                            LoginErrorMsg.setText("");

                            // Go to mainactivity
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            LoginErrorMsg.setText("Failed to login");
                        }
                    }
                });
        }

}
package com.example.farmmanagerhelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class LoginActivity extends AppCompatActivity {

    //firebase auth
    private FirebaseAuth mAuth;

    EditText loginEmail = null;
    EditText loginPassword = null;
    TextView loginErrorMsg = null;
    Button buttonOpenRegisterAccountActivity = null;
    Button buttonLogUserIn = null;
    ProgressBar progressIconLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // FireBase
        mAuth = FirebaseAuth.getInstance();

        // UI text components and error message
        //
        loginEmail = findViewById(R.id.EditTextLoginEmail);
        loginPassword = findViewById(R.id.EditTextLoginPassword);
        loginErrorMsg = findViewById(R.id.txtLoginErrorMsg);
        buttonOpenRegisterAccountActivity = findViewById((R.id.btnRegisterNewUserAcitivty));
        buttonLogUserIn = findViewById(R.id.btnLoginUser);
        progressIconLogin = findViewById(R.id.progressIconLogin);

        // listener to open registration view
        buttonOpenRegisterAccountActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class activityClass = com.example.farmmanagerhelper.RegisterAccountActivity.class;
                Intent intent = new Intent(LoginActivity.this, activityClass);

                startActivity(intent);
            }
        });


        //log in user button
        buttonLogUserIn.setOnClickListener(new View.OnClickListener() {
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

            loadingState();

            // call validation
            while(isValid)
            {
                isValid = UserServices.checkEmailAndPasswordAreNotEmpty(loginEmail.getText().toString(),
                        loginPassword.getText().toString());
                if(isValid == false)
                {
                    loginErrorMsg.setText("All fields Are Required");
                    activeState();
                    break;
                }
                loginErrorMsg.setText("");

                isValid = UserServices.validateEmail(loginEmail.getText().toString());
                if(isValid == false)
                {
                    loginErrorMsg.setText("Email not in correct format");
                    activeState();
                    break;
                }
                loginErrorMsg.setText("");

                isValid = UserServices.checkPasswordLength(loginPassword.getText().toString());
                if(isValid == false)
                {
                    loginErrorMsg.setText("Passwords must be at least 8 characters");
                    activeState();
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

                            // Go to mainactivity
                            startActivity(new Intent(LoginActivity.this, LauncherActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            LoginErrorMsg.setText("Failed to login");
                            activeState();
                        }
                    }
                });
        }


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
        buttonLogUserIn.setEnabled(false);
        buttonOpenRegisterAccountActivity.setEnabled(false);

        progressIconLogin.setVisibility(View.VISIBLE);
    }

    // Enable the buttons
    //
    private void activeState()
    {
        buttonLogUserIn.setEnabled(true);
        buttonOpenRegisterAccountActivity.setEnabled(true);

        progressIconLogin.setVisibility(View.GONE);
    }

}
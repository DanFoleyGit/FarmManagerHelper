package com.example.farmmanagerhelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.farmmanagerhelper.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterAccountActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        // UI components
        Button registerAccountButton = findViewById(R.id.btnRegisterAccount);


        registerAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount();

            }
        });
    }

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
            startActivity(new Intent(RegisterAccountActivity.this, MainActivity.class));
            finish();
        }
    }

    private void registerAccount(){

        // Variables
        boolean isValid = true;

        // UI
        EditText registerEmail = findViewById(R.id.txtRegisterEmail);
        EditText registerPassword = findViewById(R.id.txtRegisterAccountPassword);
        EditText registerPasswordConfirm = findViewById(R.id.txtRegisterAccountPasswordConfirm);
        EditText registerFirstName = findViewById(R.id.txtRegisterAccountFname);
        EditText registerLastName = findViewById(R.id.txtRegisterAccountLname);

        TextView RegisterAccountErrorMsg = findViewById(R.id.txtRegisterAccountErrorMsg);


        while(isValid) {
            isValid = UserServices.checkFieldsAreNotEmpty(registerEmail, registerPassword, registerPasswordConfirm);
            if(isValid == false)
            {
                RegisterAccountErrorMsg.setText("All fields Are Required");
                break;
            }

            RegisterAccountErrorMsg.setText("");

            isValid = UserServices.validateEmail(registerEmail);
            if(isValid == false)
            {
                RegisterAccountErrorMsg.setText("Email not in correct format");
                break;
            }

            RegisterAccountErrorMsg.setText("");

            isValid = UserServices.checkPasswordLength(registerPassword);
            if(isValid == false)
            {
                RegisterAccountErrorMsg.setText("Passwords must be at least 8 characters");
                break;
            }

            RegisterAccountErrorMsg.setText("");


            isValid = UserServices.checkPasswords(registerPassword, registerPasswordConfirm);
            if(isValid == false)
            {
                RegisterAccountErrorMsg.setText("Passwords not the same");
                break;
            }

            RegisterAccountErrorMsg.setText("");

            if(isValid)
            {
                // add user to firebase authentification
                Log.d("VALIDATION:", "success");

                // create user
                User user = new User(registerFirstName.getText().toString(),
                        registerLastName.getText().toString(),
                        registerEmail.getText().toString());

                addUserToFirebaseAuth(registerEmail.getText().toString(), registerPassword.getText().toString(),
                        RegisterAccountErrorMsg, user);

                break;
            }

        }

    }

    private void addUserToFirebaseAuth(String email, String password, TextView RegisterAccountErrorMsg, User user) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {

                        if (task.isSuccessful())
                        {
                            Log.d("", "createUserWithEmail:success");
                            FirebaseUser FirebaseUser = mAuth.getCurrentUser();

                            // clear error message
                            RegisterAccountErrorMsg.setText("");


                            // add user to database

                            addUserToDatabase(FirebaseUser, user);

                            // Go to mainActivity
                            startActivity(new Intent(RegisterAccountActivity.this, MainActivity.class));
                            finish();

                        }
                        else
                            {
                            // If sign in fails, display a message to the user.
                            Log.w("", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            // set error message
                            RegisterAccountErrorMsg.setText("Error Creating Account");

                        }
                    }
                });
    }

    private void addUserToDatabase(FirebaseUser firebaseUser, User user) {

        dbRef.child("users").child(firebaseUser.getUid()).setValue(user);

        Toast toast = Toast.makeText(this, "Creating Account", Toast.LENGTH_SHORT);
        toast.show();

    }

}
package com.example.farmmanagerhelper;

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

public class RegisterAccountActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

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

            // TODO
            // launch main menu
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

        TextView errorMsg = findViewById(R.id.txtRegisterAccountErrorMsg);

        while(isValid) {
            isValid = UserServices.checkFieldsAreNotEmpty(registerEmail, registerPassword, registerPasswordConfirm);
            if(isValid == false)
            {
                errorMsg.setText("All fields Are Required");
                break;
            }

            errorMsg.setText("");

            isValid = UserServices.validateEmail(registerEmail);
            if(isValid == false)
            {
                errorMsg.setText("Email not in correct format");
                break;
            }

            errorMsg.setText("");

            isValid = UserServices.checkPasswordLength(registerPassword);
            if(isValid == false)
            {
                errorMsg.setText("Passwords must be at least 8 characters");
                break;
            }

            errorMsg.setText("");


            isValid = UserServices.checkPasswords(registerPassword, registerPasswordConfirm);
            if(isValid == false)
            {
                errorMsg.setText("Passwords not the same");
                break;
            }

            errorMsg.setText("");

            if(isValid)
            {
                Log.d("VALIDATION:", "success");


                mAuth.createUserWithEmailAndPassword(registerEmail.getText().toString(), registerPassword.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Toast.makeText(RegisterAccountActivity.this, "Validation Success in onComplete",
//                                Toast.LENGTH_SHORT).show();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // add user to database
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });

                break;
            }

        }

    }

    private void addUserToDatabase(FirebaseUser firebaseUser,EditText registerEmail, EditText registerFirstName, EditText registerLastName) {

        // create user
        User user = new User(registerFirstName.getText().toString(),
                registerFirstName.getText().toString(),
                registerEmail.getText().toString());

        mDatabase.child("users").child(firebaseUser.getUid()).setValue(user);

        Toast toast = Toast.makeText(this, "Creating Account", Toast.LENGTH_SHORT);
        toast.show();

    }

}
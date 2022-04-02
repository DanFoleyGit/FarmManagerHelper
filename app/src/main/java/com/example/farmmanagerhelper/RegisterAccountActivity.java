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

    // UI
    EditText editTextRegisterEmail = null;
    EditText editTextRegisterPassword = null;
    EditText editTextRegisterPasswordConfirm = null;
    EditText editTextRegisterFirstName = null;
    EditText editTextRegisterLastName = null;
    TextView textViewRegisterAccountErrorMsg = null;
    Button buttonRegisterAccountButton = null;

    ProgressBar progressIconRegisterAccount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        // UI
        editTextRegisterEmail = findViewById(R.id.txtRegisterEmail);
        editTextRegisterPassword = findViewById(R.id.txtRegisterAccountPassword);
        editTextRegisterPasswordConfirm = findViewById(R.id.txtRegisterAccountPasswordConfirm);
        editTextRegisterFirstName = findViewById(R.id.txtRegisterAccountFname);
        editTextRegisterLastName = findViewById(R.id.txtRegisterAccountLname);

        textViewRegisterAccountErrorMsg = findViewById(R.id.txtRegisterAccountErrorMsg);

        buttonRegisterAccountButton = findViewById(R.id.btnRegisterAccount);

        progressIconRegisterAccount = findViewById(R.id.progressIconRegisterAccount);

        buttonRegisterAccountButton.setOnClickListener(new View.OnClickListener() {
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
        loadingState();

        while(isValid) {
            isValid = UserServices.checkFieldsAreNotEmpty(editTextRegisterEmail.getText().toString(),
                    editTextRegisterPassword.getText().toString(), editTextRegisterPasswordConfirm.getText().toString());
            if(isValid == false)
            {
                textViewRegisterAccountErrorMsg.setText("All fields Are Required");
                activeState();
                break;
            }
            textViewRegisterAccountErrorMsg.setText("");

            isValid = GeneralServices.checkStringDoesNotContainForwardSlashCharacter(editTextRegisterFirstName.getText().toString());
            if (!isValid) {
                Log.d("ManagerOrderBoardSettings validation :", "Forward slash found in string");
                activeState();
                textViewRegisterAccountErrorMsg.setText("Can not use \" / \" in inputs");
                break;
            }
            textViewRegisterAccountErrorMsg.setText("");

            isValid = GeneralServices.checkStringDoesNotContainForwardSlashCharacter(editTextRegisterLastName.getText().toString());
            if (!isValid) {
                Log.d("ManagerOrderBoardSettings validation :", "Forward slash found in string");
                activeState();
                textViewRegisterAccountErrorMsg.setText("Can not use \" / \" in inputs");
                break;
            }
            textViewRegisterAccountErrorMsg.setText("");

            isValid = UserServices.validateEmail(editTextRegisterEmail.getText().toString());
            if(isValid == false)
            {
                textViewRegisterAccountErrorMsg.setText("Email not in correct format");
                activeState();
                break;
            }
            textViewRegisterAccountErrorMsg.setText("");

            isValid = UserServices.checkPasswordLength(editTextRegisterPassword.getText().toString());
            if(isValid == false)
            {
                textViewRegisterAccountErrorMsg.setText("Passwords must be at least 8 characters");
                activeState();
                break;
            }
            textViewRegisterAccountErrorMsg.setText("");

            isValid = UserServices.checkPasswords(editTextRegisterPassword.getText().toString(), editTextRegisterPasswordConfirm.getText().toString());
            if(isValid == false)
            {
                textViewRegisterAccountErrorMsg.setText("Passwords not the same");
                activeState();
                break;
            }


            if(isValid)
            {
                // add user to firebase authentication
                Log.d("VALIDATION:", "success");

                // create user
                //
                User user = new User(null, editTextRegisterFirstName.getText().toString(),
                        editTextRegisterLastName.getText().toString(),
                        editTextRegisterEmail.getText().toString(), "none");


                addUserToFirebaseAuthAndDatabase(editTextRegisterEmail.getText().toString(), editTextRegisterPassword.getText().toString(),
                        textViewRegisterAccountErrorMsg, user);

                break;
            }
        }
    }

    private boolean checkInputsForForwardSlashes(String firstName, String lastName) {
        boolean  valid = true;

        valid = GeneralServices.checkStringDoesNotContainForwardSlashCharacter(firstName);

        valid = GeneralServices.checkStringDoesNotContainForwardSlashCharacter(lastName);

         return valid;
    }

    private void addUserToFirebaseAuthAndDatabase(String email, String password, TextView RegisterAccountErrorMsg, User user) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            Log.d("", "createUserWithEmail:success");


                            // clear error message
                            RegisterAccountErrorMsg.setText("");

                            // assign userid to user object
                            // create user
                            FirebaseUser firebaseUser = mAuth.getCurrentUser(); // user hasnt been created yet
                            user.userId = firebaseUser.getUid();

                            // add user to database
                            Toast toast = Toast.makeText(RegisterAccountActivity.this, "Creating Account", Toast.LENGTH_SHORT);
                            toast.show();

                            if(!DatabaseManager.addUserToDatabase(user))
                            {
                                toast = Toast.makeText(RegisterAccountActivity.this, "Error", Toast.LENGTH_SHORT);
                                toast.show();
                            }

                            // Go to mainActivity
                            startActivity(new Intent(RegisterAccountActivity.this, LauncherActivity.class));
                            finish();

                        }
                        else
                            {
                            // If sign in fails, display a message to the user.
                            Log.w("", "createUserWithEmail:failure", task.getException());

                            // set error message
                            RegisterAccountErrorMsg.setText("Error Creating Account");

                            // set UI components back to active
                            //
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
        editTextRegisterEmail.setEnabled(false);
        editTextRegisterPassword.setEnabled(false);
        editTextRegisterPasswordConfirm.setEnabled(false);
        editTextRegisterFirstName.setEnabled(false);
        editTextRegisterLastName.setEnabled(false);
        buttonRegisterAccountButton.setEnabled(false);

        progressIconRegisterAccount.setVisibility(View.VISIBLE);
    }

    // Enable the buttons
    //
    private void activeState()
    {
        editTextRegisterEmail.setEnabled(true);
        editTextRegisterPassword.setEnabled(true);
        editTextRegisterPasswordConfirm.setEnabled(true);
        editTextRegisterFirstName.setEnabled(true);
        editTextRegisterLastName.setEnabled(true);
        buttonRegisterAccountButton.setEnabled(true);

        progressIconRegisterAccount.setVisibility(View.GONE);
    }

}
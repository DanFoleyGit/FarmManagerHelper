package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        // UI components
        Button registerAccountButton = findViewById(R.id.btnRegisterAccount);


        registerAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount();

            }
        });
    }

    private void registerAccount(){

        // Variables
        boolean isValid = true;

        // UI
        EditText registerEmail = findViewById(R.id.txtRegisterEmail);
        EditText registerPassword = findViewById(R.id.txtRegisterAccountPassword);
        EditText registerPasswordConfirm = findViewById(R.id.txtRegisterAccountPasswordConfirm);

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
                Toast toast = Toast.makeText(this, "Creating Account", Toast.LENGTH_SHORT);
                toast.show();
                break;
            }

        }

    }

}
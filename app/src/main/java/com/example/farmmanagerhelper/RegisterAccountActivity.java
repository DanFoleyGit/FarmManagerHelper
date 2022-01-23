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


        // check the passwords match

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
            isValid = checkFieldsAreNotEmpty(registerEmail, registerPassword, registerPasswordConfirm);
            if(isValid == false)
            {
                errorMsg.setText("All fields Are Required");
                break;
            }

            errorMsg.setText("");


            isValid = checkPasswords(registerPassword, registerPasswordConfirm);
            if(isValid == false)
            {
                break;
            }

            isValid = validateEmail(registerEmail);
            if(isValid == false)
            {
                break;
            }

            if(isValid)
            {
                Toast toast = Toast.makeText(this, "Creating Account", Toast.LENGTH_SHORT);
                toast.show();
                break;
            }

        }

    }

    private boolean checkFieldsAreNotEmpty(EditText email, EditText pWord, EditText pWordConf)
    {
        if(TextUtils.isEmpty(email.getText().toString()) ||
                TextUtils.isEmpty(pWord.getText().toString()) ||
                TextUtils.isEmpty(pWordConf.getText().toString()))
        {
            return false;
        }

        return true;
    }

    // check passwords match
    private boolean checkPasswords(EditText pWord, EditText pWordCon)
    {
        Log.d("password is ", pWord.getText().toString());
        Log.d("password confirm is ", pWordCon.getText().toString());
        if(!pWord.getText().toString().equals(pWordCon.getText().toString()))
        {
            Toast toast = Toast.makeText(this, "Passwords not the same", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        //check the length of password
        if(pWord.getText().toString().length() < 8)
        {
            Toast toast = Toast.makeText(this, "Password is too short. Needs to have at least 8 characters.", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        // TODO
        // add Regex to check if the account has upper and lowercase and a number

        return true;
    }

    // Check if the password is in email format allowing for text.text@mailprovider.co.uk
    private boolean validateEmail(EditText email)
    {
        String regex = "^^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        if (email.getText().toString().matches("^^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
        {
            return true;
        }
        Toast toast = Toast.makeText(this, "Email not in correct format", Toast.LENGTH_SHORT);
        toast.show();
        return false;

    }
}
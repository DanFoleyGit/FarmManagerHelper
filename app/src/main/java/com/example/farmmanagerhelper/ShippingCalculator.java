package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ShippingCalculator extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Context context = this;

    private Spinner spinnerShippingCalculatorProductProfile;
    private EditText editTextShippingCalcOrderQuantity;
    private Button buttonShippingCalcPerformCalc;
    private Button buttonShippingCalcEditProfiles;
    private TextView textViewShippingCalcFullPalletsTag;
    private TextView textViewShippingCalcFullPalletsQuantity;
    private TextView textViewShippingCalcRemainderPalletQuantityTag;
    private TextView textViewShippingCalcRemainderPalletsQuantity;
    private TextView ShippingCalcErrorMsg = null;
    private TextView TextInputLayoutDummyShippingCalcSpinnerText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_calclator);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // UI
        //
        spinnerShippingCalculatorProductProfile = findViewById(R.id.spinnerShippingCalculatorProductProfile);
        editTextShippingCalcOrderQuantity = findViewById(R.id.editTextShippingCalcOrderQuantity);
        buttonShippingCalcPerformCalc = findViewById(R.id.buttonShippingCalcPerformCalc);
        buttonShippingCalcEditProfiles = findViewById(R.id.buttonShippingCalcEditProfiles);
//        textViewShippingCalcFullPalletsTag = findViewById(R.id.textViewShippingCalcFullPalletsTag);
        textViewShippingCalcFullPalletsQuantity = findViewById(R.id.textViewShippingCalcFullPalletsQuantity);
//        textViewShippingCalcRemainderPalletQuantityTag = findViewById(R.id.textViewShippingCalcRemainderPalletQuantityTag);
        textViewShippingCalcRemainderPalletsQuantity = findViewById(R.id.textViewShippingCalcRemainderPalletsQuantity);
        TextInputLayoutDummyShippingCalcSpinnerText = findViewById(R.id.TextInputLayoutDummyShippingCalcSpinnerText);
        ShippingCalcErrorMsg = findViewById(R.id.ShippingCalcErrorMsg);

        buttonShippingCalcEditProfiles.setEnabled(false);
        TextInputLayoutDummyShippingCalcSpinnerText.setEnabled(false);


        ToolServices.makeOptionsButtonVisibleForManager(currentUser, buttonShippingCalcEditProfiles);

        ToolServices.updateSpinnerWithShippingCalcProfiles(spinnerShippingCalculatorProductProfile, context);

        buttonShippingCalcPerformCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isValid = true;

                while (isValid) {


                    isValid = GeneralServices.checkIfSpinnerIsNull(spinnerShippingCalculatorProductProfile);
                    if (!isValid) {
                        Log.d("ShippingCalculator validation :", "No profile name");
                        ShippingCalcErrorMsg.setText("There are no profiles for this farm yet.");
                        break;
                    }
                    ShippingCalcErrorMsg.setText("");

                    isValid = GeneralServices.checkEditTextIsNotNull(editTextShippingCalcOrderQuantity.getText().toString());
                    if (!isValid) {
                        Log.d("ShippingCalculator validation :", "No quantity entered");
                        ShippingCalcErrorMsg.setText("You must enter a quantity first.");

                        break;
                    }
                    ShippingCalcErrorMsg.setText("");

                    if(isValid)
                    {

                        ToolServices.getProfileClassAndCallPerformShippingCalc(Integer.parseInt(editTextShippingCalcOrderQuantity.getText().toString()),
                                spinnerShippingCalculatorProductProfile.getSelectedItem().toString(),textViewShippingCalcFullPalletsQuantity,
                                textViewShippingCalcRemainderPalletsQuantity);
                        break;
                    }
                }
            }
        });

        buttonShippingCalcEditProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShippingCalculator.this, ShippingCalculatorManagerProfiles.class));

            }
        });

        spinnerShippingCalculatorProductProfile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editTextShippingCalcOrderQuantity.setText("");
                textViewShippingCalcFullPalletsQuantity.setText("_ pallets by __ units");
                textViewShippingCalcRemainderPalletsQuantity.setText("_ pallets by __");

                if(spinnerShippingCalculatorProductProfile.getSelectedItem() != null)
                {
                    TextInputLayoutDummyShippingCalcSpinnerText.setText(" ");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ToolServices.updateSpinnerWithShippingCalcProfiles(spinnerShippingCalculatorProductProfile, context);

    }
}
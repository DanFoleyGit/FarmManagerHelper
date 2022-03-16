package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.nfc.TagLostException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmmanagerhelper.models.ProduceEstimatorProfile;
import com.google.android.material.textfield.TextInputLayout;

public class ProduceEstimatorManagerProfiles extends AppCompatActivity {

    Context context = this;

    TextInputLayout editTextProduceEstimatorProfileNameLayout = null;
    EditText editTextProduceEstimatorProfileName = null;
    TextInputLayout editTextProduceEstimatorProfileSpinnerLayout= null;
    Spinner spinnerProduceEstimatorProfileSpinner = null;
    TextInputLayout editTextProduceEstimatorWeightRawUnitsLayout = null;
    EditText editTextProduceEstimatorWeightRawUnits = null;
    TextInputLayout editTextProduceEstimatorWeightOfFinishedProductsLayout = null;
    EditText editTextProduceEstimatorWeightOfFinishedProducts = null;
    TextInputLayout editTextProduceEstimatorProductsPerFinishedUnitLayout = null;
    EditText editTextProduceEstimatorProductsPerFinishedUnit = null;
    TextInputLayout editTextProduceEstimatorProductWasteMarginLayout = null;
    EditText editTextProduceEstimatorProductWasteMargin = null;

    RadioButton radioProduceEstimatorAddShippingProfile = null;
    RadioButton radioProduceEstimatorUpdateShippingProfile = null;
    RadioButton radioProduceEstimatorDeleteShippingCalcProfile = null;
    RadioButton radioConfirmDeleteProduceEstimatorProfile = null;

    TextView ProduceEstimatorProfileErrorMsg = null;
    Button buttonProduceEstimatorProfileSubmit = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produce_estimator_manager_profiles);

        editTextProduceEstimatorProfileNameLayout = findViewById(R.id.editTextProduceEstimatorProfileNameLayout);
        editTextProduceEstimatorProfileName = findViewById(R.id.editTextProduceEstimatorProfileName);
        editTextProduceEstimatorProfileSpinnerLayout = findViewById(R.id.editTextProduceEstimatorProfileSpinnerLayout);
        spinnerProduceEstimatorProfileSpinner = findViewById(R.id.spinnerProduceEstimatorProfileSpinner);
        editTextProduceEstimatorWeightRawUnitsLayout = findViewById(R.id.editTextProduceEstimatorWeightRawUnitsLayout);
        editTextProduceEstimatorWeightRawUnits = findViewById(R.id.editTextProduceEstimatorWeightRawUnits);
        editTextProduceEstimatorWeightOfFinishedProductsLayout = findViewById(R.id.editTextProduceEstimatorWeightOfFinishedProductsLayout);
        editTextProduceEstimatorWeightOfFinishedProducts = findViewById(R.id.editTextProduceEstimatorWeightOfFinishedProducts);
        editTextProduceEstimatorProductsPerFinishedUnitLayout = findViewById(R.id.editTextProduceEstimatorProductsPerFinishedUnitLayout);
        editTextProduceEstimatorProductsPerFinishedUnit = findViewById(R.id.editTextProduceEstimatorProductsPerFinishedUnit);
        editTextProduceEstimatorProductWasteMarginLayout = findViewById(R.id.editTextProduceEstimatorProductWasteMarginLayout);
        editTextProduceEstimatorProductWasteMargin = findViewById(R.id.editTextProduceEstimatorProductWasteMargin);

        radioProduceEstimatorAddShippingProfile = findViewById(R.id.radioProduceEstimatorAddShippingProfile);
        radioProduceEstimatorUpdateShippingProfile = findViewById(R.id.radioProduceEstimatorUpdateShippingProfile);
        radioProduceEstimatorDeleteShippingCalcProfile = findViewById(R.id.radioProduceEstimatorDeleteShippingCalcProfile);
        radioConfirmDeleteProduceEstimatorProfile = findViewById(R.id.radioConfirmDeleteProduceEstimatorProfile);

        ProduceEstimatorProfileErrorMsg = findViewById(R.id.ProduceEstimatorProfileErrorMsg);
        buttonProduceEstimatorProfileSubmit = findViewById(R.id.buttonProduceEstimatorProfileSubmit);

        // update the spinner for profiles
        //
        ToolServices.updateSpinnerWithProduceEstimatorProfiles(spinnerProduceEstimatorProfileSpinner,context);


        buttonProduceEstimatorProfileSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean isValid = true;

                while(isValid) {
                    // if its an update or delete, Validate the spinner
                    // If its an add, validate the EditText for name.
                    //
                    if(radioProduceEstimatorUpdateShippingProfile.isChecked() || radioProduceEstimatorDeleteShippingCalcProfile.isChecked())
                    {
                        isValid = GeneralServices.checkIfSpinnerIsNull(spinnerProduceEstimatorProfileSpinner);
                        ProduceEstimatorProfileErrorMsg.setText("You need to add a new profile first.");

                    }
                    else if(radioProduceEstimatorAddShippingProfile.isChecked())
                    {
                        isValid = GeneralServices.checkEditTextIsNotNull(editTextProduceEstimatorProfileName.getText().toString());
                        ProduceEstimatorProfileErrorMsg.setText("Please add a profile name.");
                    }

                    if (!isValid) {
                        Log.d("ProduceEstimatorManagerProfiles validation :", "No profile name");
                        break;
                    }
                    ProduceEstimatorProfileErrorMsg.setText("");

                    if(!radioProduceEstimatorDeleteShippingCalcProfile.isChecked()) {
                        isValid = GeneralServices.checkEditTextIsNotNull(editTextProduceEstimatorWeightRawUnits.getText().toString());
                        if (!isValid) {
                            Log.d("ProduceEstimatorManagerProfiles validation :", "No raw unit weight ");
                            ProduceEstimatorProfileErrorMsg.setText("Please add a raw unit weight.");
                            break;
                        }
                        ProduceEstimatorProfileErrorMsg.setText("");

                        isValid = GeneralServices.checkEditTextIsNotNull(editTextProduceEstimatorWeightOfFinishedProducts.getText().toString());
                        if (!isValid) {
                            Log.d("ProduceEstimatorManagerProfiles validation :", "No finished product weight ");
                            ProduceEstimatorProfileErrorMsg.setText("Please add the weight of the finished product in grams.");
                            break;
                        }
                        ProduceEstimatorProfileErrorMsg.setText("");

                        isValid = GeneralServices.checkEditTextIsNotNull(editTextProduceEstimatorProductsPerFinishedUnit.getText().toString());
                        if (!isValid) {
                            Log.d("ProduceEstimatorManagerProfiles validation :", "No products per finished unit ");
                            ProduceEstimatorProfileErrorMsg.setText("Please add the number of products per finished unit.");
                            break;
                        }
                        ProduceEstimatorProfileErrorMsg.setText("");

                        isValid = GeneralServices.checkEditTextIsNotNull(editTextProduceEstimatorProductWasteMargin.getText().toString());
                        if (!isValid) {
                            Log.d("ProduceEstimatorManagerProfiles validation :", "No margin for waste ");
                            ProduceEstimatorProfileErrorMsg.setText("Please add margin as a percentage to account for expected wastage. Example 5% or 10%.");
                            break;
                        }
                        ProduceEstimatorProfileErrorMsg.setText("");
                    }

                    if(radioProduceEstimatorDeleteShippingCalcProfile.isChecked())
                    {
                        if(!radioConfirmDeleteProduceEstimatorProfile.isChecked())
                        {
                            isValid = false;
                            Log.d("ShippingCalculatorManagerProfiles validation :", "Confirmation check note completed");
                            ProduceEstimatorProfileErrorMsg.setText("Please Check The Delete Confirm Button.");
                            break;
                        }
                    }

                    if (isValid) {
//                        Toast.makeText(ProduceEstimatorManagerProfiles.this, "Adding New Profile ", Toast.LENGTH_SHORT).show();

                        ProduceEstimatorProfile profile = new ProduceEstimatorProfile(editTextProduceEstimatorProfileName.getText().toString(), null,
                                editTextProduceEstimatorWeightRawUnits.getText().toString(), editTextProduceEstimatorWeightOfFinishedProducts.getText().toString(),
                                editTextProduceEstimatorProductsPerFinishedUnit.getText().toString(), editTextProduceEstimatorProductWasteMargin.getText().toString());

                        if(radioProduceEstimatorAddShippingProfile.isChecked())
                        {
                            ToolServices.addNewProduceEstimatorProfile(profile,context);
                            // update the spinner for profiles
                            //
                            ToolServices.updateSpinnerWithProduceEstimatorProfiles(spinnerProduceEstimatorProfileSpinner,context);
                        }

                        if(radioProduceEstimatorUpdateShippingProfile.isChecked())
                        {
                            Toast.makeText(ProduceEstimatorManagerProfiles.this, "Updating Profile ", Toast.LENGTH_SHORT).show();
                            ToolServices.updateProduceEstimatorProfile(profile,context);

                        }

                        if(radioProduceEstimatorDeleteShippingCalcProfile.isChecked())
                        {
                            Toast.makeText(ProduceEstimatorManagerProfiles.this, "Deleting Profile ", Toast.LENGTH_SHORT).show();
                            ToolServices.deleteProduceEstimatorProfile(spinnerProduceEstimatorProfileSpinner.getSelectedItem().toString(),context);

                            // update the spinner for profiles
                            //
                            ToolServices.updateSpinnerWithProduceEstimatorProfiles(spinnerProduceEstimatorProfileSpinner,context);

                        }

                        // reset UI components to add new Profile mode
                        //
                        editTextProduceEstimatorProfileSpinnerLayout.setVisibility(View.GONE);
                        editTextProduceEstimatorProfileName.setEnabled(true);
                        editTextProduceEstimatorProfileName.setText("");
                        editTextProduceEstimatorWeightRawUnits.setText("");
                        editTextProduceEstimatorWeightOfFinishedProducts.setText("");
                        editTextProduceEstimatorProductsPerFinishedUnit.setText("");
                        editTextProduceEstimatorProductWasteMargin.setText("");

                        //reset radio buttons
                        //
                        radioProduceEstimatorDeleteShippingCalcProfile.setChecked(false);
                        radioProduceEstimatorUpdateShippingProfile.setChecked(false);
                        radioProduceEstimatorAddShippingProfile.setChecked(true);


                        break;
                    }
                }

            }
        });

        spinnerProduceEstimatorProfileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ToolServices.updateProduceEstimatorParametersForProfileSelected(spinnerProduceEstimatorProfileSpinner.getSelectedItem().toString(), context,
                        editTextProduceEstimatorWeightRawUnits, editTextProduceEstimatorWeightOfFinishedProducts,
                        editTextProduceEstimatorProductsPerFinishedUnit,editTextProduceEstimatorProductWasteMargin);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        radioProduceEstimatorAddShippingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextProduceEstimatorProfileSpinnerLayout.setVisibility(View.GONE);
                editTextProduceEstimatorProfileName.setEnabled(true);
                editTextProduceEstimatorProfileName.setText("");
                editTextProduceEstimatorWeightRawUnits.setText("");
                editTextProduceEstimatorWeightOfFinishedProducts.setText("");
                editTextProduceEstimatorProductsPerFinishedUnit.setText("");
                editTextProduceEstimatorProductWasteMargin.setText("");
            }
        });

        radioProduceEstimatorUpdateShippingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextProduceEstimatorProfileName.setEnabled(false);
                editTextProduceEstimatorProfileName.setText("");
                ToolServices.updateSpinnerWithProduceEstimatorProfiles(spinnerProduceEstimatorProfileSpinner,context);
                editTextProduceEstimatorProfileSpinnerLayout.setVisibility(View.VISIBLE);

                //set name to " " to make the banner smaller. This will be rest when add radio is called
                //
                editTextProduceEstimatorProfileName.setText(" ");

            }
        });

        radioProduceEstimatorDeleteShippingCalcProfile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(radioProduceEstimatorDeleteShippingCalcProfile.isChecked())
                {
                    // hide the form and make only the spinner available
                    //
                    editTextProduceEstimatorProfileName.setEnabled(false);
                    editTextProduceEstimatorWeightRawUnitsLayout.setVisibility(View.GONE);
                    editTextProduceEstimatorWeightOfFinishedProductsLayout.setVisibility(View.GONE);
                    editTextProduceEstimatorProductsPerFinishedUnitLayout.setVisibility(View.GONE);
                    editTextProduceEstimatorProductWasteMarginLayout.setVisibility(View.GONE);
                    editTextProduceEstimatorProfileName.setText("");
                    editTextProduceEstimatorWeightRawUnits.setText("");
                    editTextProduceEstimatorWeightOfFinishedProducts.setText("");
                    editTextProduceEstimatorProductsPerFinishedUnit.setText("");
                    editTextProduceEstimatorProductWasteMargin.setText("");

                    editTextProduceEstimatorProfileName.setActivated(true);

                    editTextProduceEstimatorProfileSpinnerLayout.setVisibility(View.VISIBLE);
                    radioConfirmDeleteProduceEstimatorProfile.setVisibility(View.VISIBLE);
                }
                else
                {
                    editTextProduceEstimatorProfileSpinnerLayout.setVisibility(View.GONE);
                    radioConfirmDeleteProduceEstimatorProfile.setVisibility(View.GONE);
                    radioConfirmDeleteProduceEstimatorProfile.setChecked(false);

                    editTextProduceEstimatorProfileName.setEnabled(true);
                    editTextProduceEstimatorWeightRawUnitsLayout.setVisibility(View.VISIBLE);
                    editTextProduceEstimatorWeightOfFinishedProductsLayout.setVisibility(View.VISIBLE);
                    editTextProduceEstimatorProductsPerFinishedUnitLayout.setVisibility(View.VISIBLE);
                    editTextProduceEstimatorProductWasteMarginLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // Function needed for radio buttons to work
    //
    public void onRadioButtonClicked(View view) {

    }
}
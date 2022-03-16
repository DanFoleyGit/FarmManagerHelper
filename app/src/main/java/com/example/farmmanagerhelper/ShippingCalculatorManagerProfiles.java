package com.example.farmmanagerhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

import com.example.farmmanagerhelper.models.ShippingCalculatorProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ShippingCalculatorManagerProfiles extends AppCompatActivity {

    private TextView textViewShippingCalcProfileNameTag = null;
    private TextView textViewShippingCalcProfilePrefBoxesTag = null;
    private TextView textViewShippingCalcProfileMaxBoxesTag = null;
    private EditText editTextShippingCalcProfileName = null;
    private EditText editTextShippingCalcProfilePrefBoxes = null;
    private EditText editTextShippingCalcProfileMaxBoxes = null;
    private Button  buttonShippingCalcAddProfile = null;
    private TextView ShippingCalcProfileErrorMsg = null;
    private Spinner spinnerShippingCalcProfileNames = null;
    private RadioButton radioAddShippingProfile = null;
    private RadioButton radioUpdateShippingProfile = null;
    private RadioButton radioDeleteShippingCalcProfile = null;
    private RadioButton radioConfirmDeleteShippingCalcProfile = null;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_calculator_manager_profiles);

        textViewShippingCalcProfileNameTag = findViewById(R.id.textViewViewShippingCalcProfileNameTag);
        textViewShippingCalcProfilePrefBoxesTag = findViewById(R.id.textViewShippingCalcProfilePrefBoxesTag);
        textViewShippingCalcProfileMaxBoxesTag =findViewById(R.id.textViewShippingCalcProfileMaxBoxesTag);

        editTextShippingCalcProfileName = findViewById(R.id.editTextTextShippingCalcProfileName);
        editTextShippingCalcProfilePrefBoxes = findViewById(R.id.editTextTextShippingCalcProfilePrefBoxes);
        editTextShippingCalcProfileMaxBoxes = findViewById(R.id.editTextTextShippingCalcProfileMaxBoxes);

        radioAddShippingProfile = findViewById(R.id.radioAddShippingProfile);
        radioUpdateShippingProfile = findViewById(R.id.radioUpdateShippingProfile);
        radioDeleteShippingCalcProfile = findViewById(R.id.radioDeleteShippingCalcProfile);
        radioConfirmDeleteShippingCalcProfile = findViewById(R.id.radioConfirmDeleteShippingCalcProfile);

        buttonShippingCalcAddProfile = findViewById(R.id.buttonShippingCalcAddProfile);
        ShippingCalcProfileErrorMsg = findViewById(R.id.ShippingCalcProfileErrorMsg);
        spinnerShippingCalcProfileNames = findViewById(R.id.spinnerShippingCalcProfileNames);

        // update the list
        //
        ToolServices.updateSpinnerWithShippingCalcProfiles(spinnerShippingCalcProfileNames, context);

        setListenerForNewProductProfiles();

        buttonShippingCalcAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean isValid = true;

                Log.d("ShippingCalculatorManagerProfiles validation :", "Beginning");


                while (isValid) {

                    // check if the user is updating or adding a new profile.
                    // If they are adding ensure the editText is not empty for the profile name.
                    // if it is an update or deleting ensure that the spinner has data.
                    //
                    if(radioUpdateShippingProfile.isChecked() || radioConfirmDeleteShippingCalcProfile.isChecked())
                    {
                        isValid = GeneralServices.checkIfSpinnerIsNull(spinnerShippingCalcProfileNames);
                        ShippingCalcProfileErrorMsg.setText("You need to add a new profile first.");
                    }
                    else if(radioAddShippingProfile.isChecked())
                    {
                        isValid = GeneralServices.checkEditTextIsNotNull(editTextShippingCalcProfileName.getText().toString());
                        ShippingCalcProfileErrorMsg.setText("Please fill in all details");
                    }

                    if (!isValid) {
                        Log.d("ShippingCalculatorManagerProfiles validation :", "No profile name");
                        break;
                    }
                    ShippingCalcProfileErrorMsg.setText("");


                    // If the user is adding or updating a new profile, validate the fields
                    //
                    if(radioAddShippingProfile.isChecked())
                    {
                        isValid = GeneralServices.checkEditTextIsNotNull(editTextShippingCalcProfilePrefBoxes.getText().toString());
                        if (!isValid) {
                            Log.d("ShippingCalculatorManagerProfiles validation :", "empty preferred box input");
                            ShippingCalcProfileErrorMsg.setText("Please enter the preferred number of boxes you would like per pallet.");
                            break;
                        }
                        ShippingCalcProfileErrorMsg.setText("");

                        isValid = GeneralServices.checkEditTextIsNotNull(editTextShippingCalcProfileMaxBoxes.getText().toString());
                        if (!isValid) {
                            Log.d("ShippingCalculatorManagerProfiles validation :", "empty maximum box input");
                            ShippingCalcProfileErrorMsg.setText("Please enter the maximum number of boxes you would like per pallet.");
                            break;
                        }
                        ShippingCalcProfileErrorMsg.setText("");

                        isValid = GeneralServices.checkFirstIntIsNotGreaterThanSecondInt(Integer.parseInt(editTextShippingCalcProfilePrefBoxes.getText().toString()),
                                Integer.parseInt(editTextShippingCalcProfileMaxBoxes.getText().toString()));
                        if (!isValid) {
                            Log.d("ShippingCalculatorManagerProfiles validation :", "Preferred Boxes is greater than Maximum Boxes");
                            ShippingCalcProfileErrorMsg.setText("Maximum boxes per pallet must be more than the preferred boxes per pallet.");
                            break;
                        }
                        ShippingCalcProfileErrorMsg.setText("");
                    }

                    if(radioDeleteShippingCalcProfile.isChecked())
                    {
                        if(!radioConfirmDeleteShippingCalcProfile.isChecked())
                        {
                            isValid = false;
                            Log.d("ShippingCalculatorManagerProfiles validation :", "Confirmation check note completed");
                            ShippingCalcProfileErrorMsg.setText("Please Check The Delete Confirm Button.");
                            break;
                        }
                    }


                    if (isValid) {

                        // the case when adding a new profile
                        //
                        if(radioAddShippingProfile.isChecked())
                        {
                            Log.d("ShippingCalculatorManagerProfiles validation :", "adding Shipping Profile");
                            // Make the profile object with the EditText name typed by the user
                            //
                            ShippingCalculatorProfile profile = new ShippingCalculatorProfile(editTextShippingCalcProfileName.getText().toString(), null,
                                    editTextShippingCalcProfilePrefBoxes.getText().toString(), editTextShippingCalcProfileMaxBoxes.getText().toString());

                            ToolServices.addNewShippingCalcProfile(profile, context);

                            // update spinners
                            //
                            ToolServices.updateSpinnerWithShippingCalcProfiles(spinnerShippingCalcProfileNames, context);
                        }

                        // when updating an existing profile
                        //
                        else if(radioUpdateShippingProfile.isChecked())
                        {
                            Log.d("ShippingCalculatorManagerProfiles validation :", "updating Shipping Profile");

                            // Make the profile object with the EditText name typed by the user
                            //
                            ShippingCalculatorProfile profile = new ShippingCalculatorProfile(spinnerShippingCalcProfileNames.getSelectedItem().toString(), null,
                                    editTextShippingCalcProfilePrefBoxes.getText().toString(), editTextShippingCalcProfileMaxBoxes.getText().toString());

                            ToolServices.updateShippingCalcProfile(profile,context);

                        }

                        // Deleting a profile
                        //
                        else if(radioDeleteShippingCalcProfile.isChecked())
                        {
                            Log.d("ShippingCalculatorManagerProfiles validation :", "Deleting Shipping Profile");
                            Toast.makeText(context, "Deleting Profile" , Toast.LENGTH_SHORT).show();

                            ToolServices.deleteShippingCalcProfile(spinnerShippingCalcProfileNames.getSelectedItem().toString(), context);

                            // By checking the delete radio button to false, its listener will reset
                            // all of the form fields.
                            //
                            radioDeleteShippingCalcProfile.setChecked(false);

                            // update spinners
                            //
                            ToolServices.updateSpinnerWithShippingCalcProfiles(spinnerShippingCalcProfileNames, context);

                        }
                        // If no radio button is selected
                        //
                        else
                        {
                            ShippingCalcProfileErrorMsg.setText("Please Select One of The Options above");
                        }

                        // reset to add new profile state
                        //
                        editTextShippingCalcProfileName.setText("");
                        editTextShippingCalcProfilePrefBoxes.setText("");
                        editTextShippingCalcProfileMaxBoxes.setText("");
                        radioAddShippingProfile.setChecked(true);
                        spinnerShippingCalcProfileNames.setVisibility(View.GONE);
                        editTextShippingCalcProfileName.setVisibility(View.VISIBLE);

                        break;

                    }
                }
            }
        });

        radioAddShippingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextShippingCalcProfileName.setVisibility(View.VISIBLE);
                editTextShippingCalcProfileName.setText("");
                editTextShippingCalcProfilePrefBoxes.setText("");
                editTextShippingCalcProfileMaxBoxes.setText("");

                spinnerShippingCalcProfileNames.setVisibility(View.GONE);
            }
        });

        radioUpdateShippingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextShippingCalcProfileName.setVisibility(View.GONE);
                spinnerShippingCalcProfileNames.setVisibility(View.VISIBLE);
                ToolServices.updateSpinnerWithShippingCalcProfiles(spinnerShippingCalcProfileNames, context);
            }
        });

        // function listens for a change in this radio button. The radio button can be checked by a
        // user or triggered after a delete when radioDeleteShippingCalcProfile is set to false.
        // When the delete button is pressed, remove all UI elements from the main form.
        // The dropdown is then made visible and a delete confirm button is also added.
        // If its un checked, it will do the opposite
        //
        radioDeleteShippingCalcProfile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(radioDeleteShippingCalcProfile.isChecked())
                {
                    // hide the main form and make the spinner and confirm delete button visible
                    //
                    editTextShippingCalcProfileName.setVisibility(View.GONE);
                    editTextShippingCalcProfilePrefBoxes.setVisibility(View.GONE);
                    editTextShippingCalcProfileMaxBoxes.setVisibility(View.GONE);
                    textViewShippingCalcProfileNameTag.setVisibility(View.GONE);
                    textViewShippingCalcProfilePrefBoxesTag.setVisibility(View.GONE);
                    textViewShippingCalcProfileMaxBoxesTag.setVisibility(View.GONE);
                    editTextShippingCalcProfileName.setText("");
                    editTextShippingCalcProfilePrefBoxes.setText("");
                    editTextShippingCalcProfileMaxBoxes.setText("");

                    radioConfirmDeleteShippingCalcProfile.setVisibility(View.VISIBLE);
                    spinnerShippingCalcProfileNames.setVisibility(View.VISIBLE);

                }
                else
                {
                    // show form and hide confirm while resetting it
                    //
                    spinnerShippingCalcProfileNames.setVisibility(View.GONE);
                    radioConfirmDeleteShippingCalcProfile.setVisibility(View.GONE);
                    radioConfirmDeleteShippingCalcProfile.setChecked(false);

                    editTextShippingCalcProfileName.setVisibility(View.VISIBLE);
                    editTextShippingCalcProfilePrefBoxes.setVisibility(View.VISIBLE);
                    editTextShippingCalcProfileMaxBoxes.setVisibility(View.VISIBLE);
                    textViewShippingCalcProfileNameTag.setVisibility(View.VISIBLE);
                    textViewShippingCalcProfilePrefBoxesTag.setVisibility(View.VISIBLE);
                    textViewShippingCalcProfileMaxBoxesTag.setVisibility(View.VISIBLE);
                }
            }
        });

        // When Item is selected get the parameters from the database to allow the user to easily update them
        //
        spinnerShippingCalcProfileNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ToolServices.updateShippingCalcParametersForProfileSelected(spinnerShippingCalcProfileNames.getSelectedItem().toString(), context,
                        editTextShippingCalcProfilePrefBoxes, editTextShippingCalcProfileMaxBoxes);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // Listens to a update or change to data in the shippingCalculatorProfiles document in the users farm
    // farm. If there is a write to it, this listener will call an update to the dropdown list of profiles
    //
    private void setListenerForNewProductProfiles() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();
                DatabaseReference dbShippingCalcRef =  DatabaseManager.getShippingCalcProfilesTableDatabaseReferenceByFarmName(farmId);
                dbShippingCalcRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ToolServices.updateSpinnerWithShippingCalcProfiles(spinnerShippingCalcProfileNames, context);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("error", error.toString());
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // Function needed for radio buttons to work
    //
    public void onRadioButtonClicked(View view) {

    }
}
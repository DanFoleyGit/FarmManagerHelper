package com.example.farmmanagerhelper;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.farmmanagerhelper.models.Order;
import com.example.farmmanagerhelper.models.ProduceEstimatorProfile;
import com.example.farmmanagerhelper.models.ShippingCalculatorProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolServices {

    public static void makeOptionsButtonVisibleForManager(FirebaseUser currentUser, Button ManagerOnlyButton) {

        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();

                DatabaseReference dbFarmRef =  DatabaseManager.getFarmDatabaseReferenceByName(farmId);

                dbFarmRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (currentUser.getUid().equals(snapshot.child("managerID").getValue().toString()))
                        {
                            ManagerOnlyButton.setVisibility(View.VISIBLE);
                            ManagerOnlyButton.setEnabled(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("error", error.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    public static void addNewShippingCalcProfile(ShippingCalculatorProfile profile, Context context) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();

                DatabaseReference dbShippingCalcRef =  DatabaseManager.getShippingCalcProfilesTableDatabaseReferenceByFarmName(farmId);

                dbShippingCalcRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String keyFromPush = "";

                        // Check if the profile already exists, and if it does not add it.
                        //
                        if (snapshot.child(profile.getProfileName()).exists()) {
                            Toast.makeText(context, "Profile already exists: "+ profile.getProfileName(), Toast.LENGTH_SHORT).show();
                            Log.d("ToolServices :", "Profile already exists: "+ profile.getProfileName()  );

                        }
                        else
                        {
                            Toast.makeText(context, "Adding Profile", Toast.LENGTH_SHORT).show();
                            Log.d("ToolServices :", "Adding Profile: "+ profile.getProfileName() );

                            keyFromPush = dbShippingCalcRef.push().getKey();
                            profile.setProfileID(keyFromPush);
                            DatabaseManager.addNewShippingCalcProfileToDatabase(profile, dbShippingCalcRef);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("error", error.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    // gets the list of product profiles that the user can use to edit parameters
    //
    public static void updateSpinnerWithShippingCalcProfiles(Spinner spinner, Context context) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();

                Log.d("ToolServices updateSpinnerWithShippingCalcProfiles", "farm Id is " + farmId);

                DatabaseReference dbShippingCalcRef =  DatabaseManager.getShippingCalcProfilesTableDatabaseReferenceByFarmName(farmId);

                dbShippingCalcRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String profile = "";
                        List<String> ProfileNames = new ArrayList<String>();

                        // get the farm id
                        //
                        Log.d("ToolServices updateSpinnerWithShippingCalcProfiles", "FarmId is " + farmId);

                        // get the profiles that is stored in the shippingCalculatorProfiles table
                        // and add the strings to a list.
                        //
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            // add the profile to the list
                            //
                            profile = ds.getKey();
                            ProfileNames.add(profile);

                        }

                        Log.d("ToolServices updateSpinnerWithShippingCalcProfiles", "Profile list " + ProfileNames);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item,ProfileNames);

                        // create a new adapter which takes a list of profiles to display
                        //
                        ArrayAdapter<String> spinnerUsersAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ProfileNames);
                        spinner.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("error", error.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    // gets a the order object that is selected in the spinner and assigns the preferred boxes per pallet
    // and the maximum boxes per pallet to the EditText objects. this makes it easy for the user to edit
    // the parameters they already have.
    //
    public static void updateShippingCalcParametersForProfileSelected(String selectedProfile, Context context,
                                                                      EditText editTextTextShippingCalcProfilePrefBoxes,
                                                                      EditText editTextTextShippingCalcProfileMaxBoxes) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();

                Log.d("ToolServices updateParametersForProfileSelected", "farm Id is " + farmId);

                DatabaseReference dbShippingCalcRef =  DatabaseManager.getShippingCalcProfilesTableDatabaseReferenceByFarmName(farmId);

                dbShippingCalcRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ShippingCalculatorProfile profile = null;

                        if(snapshot.child(selectedProfile).exists()) {
                            // Get the class for that profile and assign it to the text view objects
                            //
                            profile = snapshot.child(selectedProfile).getValue(ShippingCalculatorProfile.class);
                            editTextTextShippingCalcProfilePrefBoxes.setText(profile.getProfilePreferredBoxesPerPallet());
                            editTextTextShippingCalcProfileMaxBoxes.setText(profile.getProfileMaximumBoxesPerPallet());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("error", error.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    public static void updateShippingCalcProfile(ShippingCalculatorProfile newProfile, Context context) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();

                DatabaseReference dbShippingCalcRef =  DatabaseManager.getShippingCalcProfilesTableDatabaseReferenceByFarmName(farmId);

                dbShippingCalcRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // Check if the profile already exists, and if it does not add it.
                        //
                        if (snapshot.child(newProfile.getProfileName()).exists()) {
                            Toast.makeText(context, "Updating Profile", Toast.LENGTH_SHORT).show();

                            // create a hash map
                            //
                            Map<String, Object> postValues = new HashMap<String,Object>();

                            // add the updated values to the hash map and the column they belong too
                            //
                            postValues.put("profileMaximumBoxesPerPallet", newProfile.getProfileMaximumBoxesPerPallet());
                            postValues.put("profilePreferredBoxesPerPallet", newProfile.getProfilePreferredBoxesPerPallet());

                            // Call database manager to perform the update.
                            //
                            DatabaseManager.updateShippingCalcProfileInDatabase(newProfile, dbShippingCalcRef,postValues);
                        }
                        else
                        {
                            Toast.makeText(context, "Profile does not exist to update.", Toast.LENGTH_SHORT).show();
                            Log.d("ToolServices :", "Error Profile does not exist to update");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("error", error.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    public static void deleteShippingCalcProfile(String profileName, Context context) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String farmId = snapshot.child("UserTableFarmId").getValue().toString();

                    Log.d("ToolServices deleteShippingCalcProfile", "farm Id is " + farmId);

                    DatabaseReference dbShippingCalcRef =  DatabaseManager.getShippingCalcProfilesTableDatabaseReferenceByFarmName(farmId);

                    dbShippingCalcRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.child(profileName).exists()) {
                                Toast.makeText(context, "Deleting Profile", Toast.LENGTH_SHORT).show();

                                DatabaseManager.deleteShippingCalcProfileFromDatabase(profileName,dbShippingCalcRef);
                            }
                            else
                            {
                                Toast.makeText(context, "Profile not Found", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("error", error.toString());
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("error", error.toString());
                }
            });
    }

    // This function takes the quantity of profiles, the name of the profile and the UI elements to update after
    // the class is gotten. It then calls performShippingCalcAndUpdateUI() to perform the calculation and update the UI
    // components that are passed to it.
    //
    public static void getProfileClassAndCallPerformShippingCalc(int quantityToCalculate, String profileName,
                                                                 TextView textViewShippingCalcFullPalletsQuantity,
                                                                 TextView textViewShippingCalcRemainderPalletsQuantity) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();

                Log.d("ToolServices getProfileClassAndCallPerformShippingCalc", "farm Id is " + farmId);

                DatabaseReference dbShippingCalcRef =  DatabaseManager.getShippingCalcProfilesTableDatabaseReferenceByFarmName(farmId);

                dbShippingCalcRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ShippingCalculatorProfile profile = null;

                        // Get the class for that profile and assign it to the text view objects
                        //
                        profile = snapshot.child(profileName).getValue(ShippingCalculatorProfile.class);

                        performShippingCalcAndUpdateUI(quantityToCalculate,profile,
                                textViewShippingCalcFullPalletsQuantity,textViewShippingCalcRemainderPalletsQuantity);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("error", error.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    // Algorithm that decides the best way to distribute crates over pallets for transport.
    // It takes the quantity of products and finds the preferred amount of units per pallet.
    // Then divides the preferred units per pallets by quantity.
    //
    public static void performShippingCalcAndUpdateUI(int quantityToCalculate, ShippingCalculatorProfile profile,
                                                      TextView textViewShippingCalcFullPalletsQuantity,
                                                      TextView textViewShippingCalcRemainderPalletsQuantity) {

        int quantity = quantityToCalculate;
        int pBoxes = Integer.parseInt(profile.getProfilePreferredBoxesPerPallet());
        int mBoxes = Integer.parseInt(profile.getProfileMaximumBoxesPerPallet());
        int fullPallets = 0;
        int remainderPallet = 0;
        int margin = 0;

        margin = mBoxes - pBoxes;
        fullPallets = quantity/pBoxes;
        remainderPallet = quantity %pBoxes;

        if(remainderPallet <= margin)
        {
            remainderPallet = pBoxes +remainderPallet;
            fullPallets--;
        }

        if(remainderPallet == pBoxes)
        {
            fullPallets++;
            textViewShippingCalcFullPalletsQuantity.setText( fullPallets + " pallets by " + pBoxes+ " units.");
        }
        else
        {
            textViewShippingCalcFullPalletsQuantity.setText( fullPallets + " pallets by " + pBoxes+ " units.");
            textViewShippingCalcRemainderPalletsQuantity.setText("1 pallet by " + remainderPallet);
        }
    }

    //

    public static void addNewProduceEstimatorProfile(ProduceEstimatorProfile profile, Context context) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmName = snapshot.child("UserTableFarmId").getValue().toString();

                DatabaseReference dbShippingCalcRef = DatabaseManager.getProduceEstimatorProfilesTableDatabaseReferenceByFarmName(farmName);

                dbShippingCalcRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String keyFromPush = "";

                        // Check if the profile already exists, and if it does not add it.
                        //
                        if (snapshot.child(profile.getProfileName()).exists()) {
                            Toast.makeText(context, "Profile already exists: " + profile.getProfileName(), Toast.LENGTH_SHORT).show();
                            Log.d("ToolServices addNewProduceEstimatorProfile:", "Profile already exists: " + profile.getProfileName());

                        } else {
                            Toast.makeText(context, "Adding Profile", Toast.LENGTH_SHORT).show();
                            Log.d("ToolServices addNewProduceEstimatorProfile:", "Adding Profile: " + profile.getProfileName());

                            keyFromPush = dbShippingCalcRef.push().getKey();
                            profile.setProfileID(keyFromPush);
                            DatabaseManager.addNewProduceEstimatorProfileToDatabase(profile, dbShippingCalcRef);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("error", error.toString());
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    public static void updateSpinnerWithProduceEstimatorProfiles(Spinner spinner, Context context) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();

                Log.d("ToolServices updateSpinnerWithProduceEstimatorProfiles", "farm Id is " + farmId);

                DatabaseReference dbProduceEstimatorRef =  DatabaseManager.getProduceEstimatorProfilesTableDatabaseReferenceByFarmName(farmId);

                dbProduceEstimatorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String profile = "";
                        List<String> ProfileNames = new ArrayList<String>();

                        // get the farm id
                        //
                        Log.d("ToolServices updateSpinnerWithProduceEstimatorProfiles", "FarmId is " + farmId);

                        // get the profiles that is stored in the shippingCalculatorProfiles table
                        // and add the strings to a list.
                        //
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            // add the profile to the list
                            //
                            profile = ds.getKey();
                            ProfileNames.add(profile);

                        }

                        Log.d("ToolServices updateSpinnerWithProduceEstimatorProfiles", "Profile list " + ProfileNames);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item,ProfileNames) {

                            public View getView(int position, View convertView, ViewGroup parent) {

                                View v = super.getView(position, convertView, parent);

                                ((TextView) v).setTextSize(16);

                                return v;

                            }

                            public View getDropDownView(int position, View convertView,ViewGroup parent) {

                                View v = super.getDropDownView(position, convertView,parent);

                                ((TextView) v).setGravity(Gravity.CENTER);

                                return v;

                            }

                        };

                        // create a new adapter which takes a list of profiles to display
                        //
//                        ArrayAdapter<String> spinnerUsersAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ProfileNames);
                        spinner.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("error", error.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    // takes the selected item and runs a query to get that Items parameters. It then assigns the
    // values of that parameter to the UI components to allow the user to quickly edit a profile.
    //
    public static void updateProduceEstimatorParametersForProfileSelected(String selectedProfile, Context context,
                                                                          EditText editTextProduceEstimatorWeightRawUnits,
                                                                          EditText editTextProduceEstimatorWeightOfFinishedProducts,
                                                                          EditText editTextProduceEstimatorProductsPerFinishedUnit,
                                                                          EditText editTextProduceEstimatorProductWasteMargin) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();

                Log.d("ToolServices updateParametersForProfileSelected", "farm Id is " + farmId);

                DatabaseReference dbProduceEstimatorRef =  DatabaseManager.getProduceEstimatorProfilesTableDatabaseReferenceByFarmName(farmId);

                dbProduceEstimatorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ProduceEstimatorProfile profile = null;

                        if(snapshot.child(selectedProfile).exists()) {
                            // Get the class for that profile and assign it to the text view objects
                            //
                            profile = snapshot.child(selectedProfile).getValue(ProduceEstimatorProfile.class);
                            editTextProduceEstimatorWeightRawUnits.setText(profile.getWeightOfRawUnit());
                            editTextProduceEstimatorWeightOfFinishedProducts.setText(profile.getWeightOfFinishedProduct());
                            editTextProduceEstimatorProductsPerFinishedUnit.setText(profile.getNumProductsPerFinishedUnit());
                            editTextProduceEstimatorProductWasteMargin.setText(profile.getMarginForWastage());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("error", error.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    // takes a new profile
    public static void updateProduceEstimatorProfile(ProduceEstimatorProfile newProfile, Context context) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();

                DatabaseReference dbProduceEstimatorRef =  DatabaseManager.getProduceEstimatorProfilesTableDatabaseReferenceByFarmName(farmId);

                dbProduceEstimatorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // Check if the profile already exists, and if it does not add it.
                        //

                        if (snapshot.child(newProfile.getProfileName()).exists()) {
                            Log.d("ToolServices updateProduceEstimatorProfile:", "Updating Profile");


                            // create a hash map
                            //
                            Map<String, Object> postValues = new HashMap<String,Object>();

                            // add the updated values to the hash map and the column they belong too
                            //
                            postValues.put("marginForWastage", newProfile.getMarginForWastage());
                            postValues.put("numProductsPerFinishedUnit", newProfile.getNumProductsPerFinishedUnit());
                            postValues.put("weightOfFinishedProduct", newProfile.getWeightOfFinishedProduct());
                            postValues.put("weightOfRawUnit", newProfile.getWeightOfRawUnit());


                            // Call database manager to perform the update.
                            //
                            DatabaseManager.updateProduceEstimatorProfileInDatabase(newProfile, dbProduceEstimatorRef,postValues);
                        }
                        else
                        {
                            Toast.makeText(context, "Profile does not exist to update.", Toast.LENGTH_SHORT).show();
                            Log.d("ToolServices updateProduceEstimatorProfile:", "Error Profile does not exist to update");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("error", error.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    // takes a profile name and deletes it from the database.
    //
    public static void deleteProduceEstimatorProfile(String profileName, Context context) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();

                Log.d("ToolServices deleteShippingCalcProfile", "farm Id is " + farmId);

                DatabaseReference dbProduceEstimatorRef =  DatabaseManager.getProduceEstimatorProfilesTableDatabaseReferenceByFarmName(farmId);

                dbProduceEstimatorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child(profileName).exists()) {
                            Toast.makeText(context, "Deleting Profile", Toast.LENGTH_SHORT).show();

                            DatabaseManager.deleteProduceEstimatorFromDatabase(profileName,dbProduceEstimatorRef);
                        }
                        else
                        {
                            Toast.makeText(context, "Profile not Found", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("error", error.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    public static void getProfileClassAndCallPerformProduceEstimation(int quantityToCalculate, String profileName, TextView textViewProduceEstimatorResult) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();

                Log.d("ToolServices getProfileClassAndCallPerformShippingCalc", "farm Id is " + farmId);

                DatabaseReference dbProduceEstimatorRef =  DatabaseManager.getProduceEstimatorProfilesTableDatabaseReferenceByFarmName(farmId);

                dbProduceEstimatorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ProduceEstimatorProfile profile = null;

                        // Get the class for that profile and assign it to the text view objects
                        //
                        profile = snapshot.child(profileName).getValue(ProduceEstimatorProfile.class);

                        performProduceEstimationAndUpdateUI(quantityToCalculate,profile,
                                textViewProduceEstimatorResult);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("error", error.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    // function takes a quantity of finished products that are ordered and returns the number of
    // units that need to be harvested to meet this order.
    //
    // It takes the quantity from the user and the profile set by the manager in the database.
    // parses the parameters from the profile into variables as integers or floats.
    // The Margin for waste is turned into a percentage using string concat allowed by stringent
    // validation.
    public static void performProduceEstimationAndUpdateUI(int quantityToCalculate, ProduceEstimatorProfile profile, TextView textViewProduceEstimatorResult) {

        // Decimal Formatting
        //
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);


        // Convert the margin of waste to a percentage
        //
        String convertMarginToPercentage = null;
        if(profile.getMarginForWastage().length() > 1)
        {
            convertMarginToPercentage = "1." + profile.getMarginForWastage();
        }
        else
        {
            convertMarginToPercentage = "1.0" + profile.getMarginForWastage();
        }

        // Assign Data to variables
        //
        float numRawUnit = 0;
        float wRawUnit = Integer.parseInt(profile.getWeightOfRawUnit());
        float wFinProd = Integer.parseInt(profile.getWeightOfFinishedProduct());
        float numProdPerFinCrate = Integer.parseInt(profile.getNumProductsPerFinishedUnit());
        float margin = Float.parseFloat(convertMarginToPercentage) ;
        Log.d("wRawUnit", " " + wRawUnit);
        Log.d("wFinProd", " " + wFinProd);
        Log.d("numProdPerFinCrate", " " + numProdPerFinCrate);
        Log.d("margin", " " + margin);



        // Perform Calculation
        //
        numRawUnit = ((((wFinProd * numProdPerFinCrate)* quantityToCalculate) * margin )/wRawUnit);

        // Format Result
        String result = df.format(numRawUnit)+ " units needed to make " + quantityToCalculate + "\n"  + profile.getProfileName()+ ".";

        textViewProduceEstimatorResult.setText(result);

    }
}



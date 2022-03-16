package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class ProduceEstimatorManagerProfiles extends AppCompatActivity {

    EditText editTextProduceEstimatorQuantityLayout = null;
    EditText editTextProduceEstimatorQuantity = null;
    EditText editTextProduceEstimatorWeightRawUnitsLayout = null;
    EditText editTextProduceEstimatorWeightRawUnits = null;
    EditText editTextProduceEstimatorWeightOfFinishedProductsLayout = null;
    EditText editTextProduceEstimatorWeightOfFinishedProducts = null;
    EditText editTextProduceEstimatorProductsPerFinishedUnitLayout = null;
    EditText editTextProduceEstimatorProductsPerFinishedUnit = null;
    EditText editTextProduceEstimatorProductWasteMarginLayout = null;
    EditText editTextProduceEstimatorProductWasteMargin = null;

    RadioButton radioProduceEstimatorAddShippingProfile = null;
    RadioButton radioProduceEstimatorUpdateShippingProfile = null;
    RadioButton radioProduceEstimatorDeleteShippingCalcProfile = null;

    Button buttonProduceEstimatorProfileSubmit = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produce_estimator_manager_profiles);

        editTextProduceEstimatorQuantityLayout = findViewById(R.id.editTextProduceEstimatorQuantityLayout);
        editTextProduceEstimatorQuantity = findViewById(R.id.editTextProduceEstimatorQuantity);
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

        buttonProduceEstimatorProfileSubmit = null;
    }

    // Function needed for radio buttons to work
    //
    public void onRadioButtonClicked(View view) {

    }
}
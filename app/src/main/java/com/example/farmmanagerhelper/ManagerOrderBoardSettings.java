package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.farmmanagerhelper.models.Customer;
import com.example.farmmanagerhelper.models.Order;
import com.example.farmmanagerhelper.models.Product;

import java.util.Calendar;

public class ManagerOrderBoardSettings extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_order_board_settings);

        EditText formEditTextDatePicker = findViewById(R.id.EditTextManagerOrderBoardSettingsDatePicker);
        Spinner formSpinnerCustomers = findViewById(R.id.spinnerManagerOrderBoardSettingsCustomer);
        Spinner formSpinnerProducts = findViewById(R.id.SpinnerManagerOrderBoardSettingsProduct);
        EditText formEditTextProductQuantity = findViewById(R.id.editTextProductQuantity);
        Button buttonAddNewOrder =  findViewById(R.id.buttonManagerOrderSettingsAddNewOrder);
        Button buttonAddNewCustomer = findViewById(R.id.buttonManagerOrderSettingsAddNewCustomerWindow);
        Button buttonAddNewProduct = findViewById(R.id.buttonManagerOrderSettingsAddNewProduct);
        TextView formErrorMsg = findViewById(R.id.formErrorMsg);

        //Add customer UI components
        View viewAddNewCustomerDialog = findViewById(R.id.AddNewCustomerDialog);
        ImageButton imageButtonCloseNewCustomerWindow = findViewById(R.id.imageButtonCloseNewCustomerWindow);
        EditText EditTextAddNewCustomerName = findViewById(R.id.EditTextNewCustomerName);
        Button buttonManagerOrderSettingsAddNewCustomerToDatabase = findViewById(R.id.buttonManagerOrderSettingsAddNewCustomerToDatabase);
        TextView textViewAddCustomerErrorMsg = findViewById(R.id.managerOrderBoardAddCustomerErrorMsg);

        //Add Product to customer UI components
        View viewAddNewProductDialog = findViewById(R.id.AddNewProductDialog);
        ImageButton imageButtonCloseNewProductWindow = findViewById(R.id.imageButtonCloseNewProductWindow);
        Spinner spinnerAddProductCustomerName = findViewById(R.id.spinnerAddProductCustomerName);
        EditText EditTextAddNewProductName = findViewById(R.id.EditTextNewProductName);
        Button buttonManagerOrderSettingsAddNewProductToDatabase = findViewById(R.id.buttonManagerOrderSettingsAddNewProductToDatabase);
        TextView textViewAddProductErrorMsg = findViewById(R.id.managerOrderBoardAddProductErrorMsg);

        // set up the dialog box to give dates in dd/MM/yy
        //
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);

                // update editTextDatePicker to show new date
                //
                TimetableServices.updateLabel(formEditTextDatePicker, myCalendar);
            }
        };

        // set on click listener for calender popup.
        //
        formEditTextDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ManagerOrderBoardSettings.this, date ,
                        myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // set up spinners to act as dropdown lists
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.blank_array, android.R.layout.simple_spinner_item);

        // set the spinner to drop down and add the list of times to it
        //
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        formSpinnerCustomers.setAdapter(spinnerAdapter);
        formSpinnerProducts.setAdapter(spinnerAdapter);

        // Onclick listener for add new order button. Validates inputs
        //
        buttonAddNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isValid = true;

                Log.d("ManagerOrderBoardSettings validation :","Beginning");
                while(isValid) {
                    isValid = GeneralServices.checkEditTextIsNotNull(formEditTextDatePicker.getText().toString());
                    if (!isValid) {
                        Log.d("ManagerOrderBoardSettings validation :", "Date Picker is null");
                        formErrorMsg.setText("Date must not be null. This is a system error.");
                        break;
                    }
                    formErrorMsg.setText("");

                    isValid = GeneralServices.checkIfSpinnerIsNull(formSpinnerCustomers);
                    if (!isValid) {
                        Log.d("ManagerOrderBoardSettings validation :", "No customers selected");
                        formErrorMsg.setText("You must add a customer. Click Add New Customer button below");
                        break;
                    }
                    formErrorMsg.setText("");

                    isValid = GeneralServices.checkIfSpinnerIsNull(formSpinnerProducts);
                    if (!isValid) {
                        Log.d("ManagerOrderBoardSettings validation :", "No products selected");
                        formErrorMsg.setText("You must add a Product for " + formSpinnerCustomers.getSelectedItem()+
                                ". Click Add New Product button below");
                        break;
                    }
                    formErrorMsg.setText("");

                    isValid = GeneralServices.checkEditTextIsNotNull(formEditTextProductQuantity.getText().toString());
                    if (!isValid) {
                        Log.d("ManagerOrderBoardSettings validation :", "No quantity selected");
                        formErrorMsg.setText("Enter a quantity");
                        break;
                    }
                    formErrorMsg.setText("");

                    if(isValid)
                    {
                        Order order = new Order(null, formEditTextDatePicker.getText().toString(),
                                formSpinnerCustomers.getSelectedItem().toString(), formSpinnerProducts.getSelectedItem().toString(),
                                formEditTextProductQuantity.getText().toString(), false);

                        OrdersBoardServices.AddOrder(order,context);
                        // reset the quantity value and product spinner
                        //
                        formEditTextProductQuantity.setText("");
                        formSpinnerProducts.setSelection(0);
                        formErrorMsg.setText("");
                        break;
                    }

                }
            }
        });

        // Listeners for adding new customer window
        // This listener disables inputs on all other editTexts and dropdowns and makes the view
        // to add new customers visible
        //
        buttonAddNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // disable components for adding item
                //
                buttonAddNewOrder.setVisibility(View.GONE);
                buttonAddNewCustomer.setVisibility((View.GONE));
                buttonAddNewProduct.setVisibility((View.GONE));
                formEditTextDatePicker.setEnabled(false);
                formSpinnerCustomers.setEnabled(false);
                formSpinnerProducts.setEnabled(false);
                formEditTextProductQuantity.setEnabled(false);

                //enable components for
                //
                viewAddNewCustomerDialog.setVisibility(View.VISIBLE);
                viewAddNewCustomerDialog.setElevation(10);
                EditTextAddNewCustomerName.setVisibility(View.VISIBLE);
                textViewAddCustomerErrorMsg.setVisibility(View.VISIBLE);

            }
        });

        imageButtonCloseNewCustomerWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enable components for adding new customer
                //
                viewAddNewCustomerDialog.setVisibility(View.GONE);
                viewAddNewCustomerDialog.setElevation(-2);
                EditTextAddNewCustomerName.setVisibility(View.GONE);
                textViewAddCustomerErrorMsg.setVisibility(View.GONE);

                // disable components for adding item
                //
                buttonAddNewOrder.setVisibility(View.VISIBLE);
                buttonAddNewCustomer.setVisibility((View.VISIBLE));
                buttonAddNewProduct.setVisibility((View.VISIBLE));
                formEditTextDatePicker.setEnabled(true);
                formSpinnerCustomers.setEnabled(true);
                formSpinnerProducts.setEnabled(true);
                formEditTextProductQuantity.setEnabled(true);

                // reset customer name field
                EditTextAddNewCustomerName.setText("");
            }
        });

        buttonManagerOrderSettingsAddNewCustomerToDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Customer customer = null;
                boolean isValid = true;

                Log.d("ManagerOrderBoardSettings validation :","Beginning");

                while(isValid) {
                    isValid = GeneralServices.checkEditTextIsNotNull(EditTextAddNewCustomerName.getText().toString());
                    if (!isValid) {
                        Log.d("ManagerOrderBoardSettings validation :", "empty inputs");
                        textViewAddCustomerErrorMsg.setText("Input a Customer Name.");
                        break;
                    }
                    textViewAddCustomerErrorMsg.setText("");

                    if(isValid)
                    {

                        customer = new Customer(EditTextAddNewCustomerName.getText().toString());

                        // add customer
                        OrdersBoardServices.addCustomerToFarmOrderBoard(context, customer,formSpinnerCustomers,spinnerAddProductCustomerName);


                        //enable components for adding new customer
                        //
                        viewAddNewCustomerDialog.setVisibility(View.GONE);
                        viewAddNewCustomerDialog.setElevation(-2);
                        EditTextAddNewCustomerName.setVisibility(View.GONE);
                        textViewAddCustomerErrorMsg.setVisibility(View.GONE);

                        // disable components for adding item
                        //
                        buttonAddNewOrder.setVisibility(View.VISIBLE);
                        buttonAddNewCustomer.setVisibility((View.VISIBLE));
                        buttonAddNewProduct.setVisibility((View.VISIBLE));
                        formEditTextDatePicker.setEnabled(true);
                        formSpinnerCustomers.setEnabled(true);
                        formSpinnerProducts.setEnabled(true);
                        formEditTextProductQuantity.setEnabled(true);

                        // reset customer name field
                        EditTextAddNewCustomerName.setText("");

                        break;
                    }
                }
            }
        });

        // Buttons to open/close the add product to customer dialog
        buttonAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // disable components for adding item
                //
                buttonAddNewOrder.setVisibility(View.GONE);
                buttonAddNewCustomer.setVisibility((View.GONE));
                buttonAddNewProduct.setVisibility((View.GONE));
                formEditTextDatePicker.setEnabled(false);
                formSpinnerCustomers.setEnabled(false);
                formSpinnerProducts.setEnabled(false);
                formEditTextProductQuantity.setEnabled(false);

                //enable components for
                //
                viewAddNewProductDialog.setVisibility(View.VISIBLE);
                viewAddNewProductDialog.setElevation(10);
                spinnerAddProductCustomerName.setVisibility(View.VISIBLE);
                EditTextAddNewProductName.setVisibility(View.VISIBLE);
                textViewAddProductErrorMsg.setVisibility(View.VISIBLE);
            }
        });

        imageButtonCloseNewProductWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //enable components for adding new customer
                //
                viewAddNewProductDialog.setVisibility(View.GONE);
                viewAddNewProductDialog.setElevation(-2);
                spinnerAddProductCustomerName.setVisibility(View.GONE);
                EditTextAddNewProductName.setVisibility(View.GONE);
                textViewAddProductErrorMsg.setVisibility(View.GONE);

                // disable components for adding item
                //
                buttonAddNewOrder.setVisibility(View.VISIBLE);
                buttonAddNewCustomer.setVisibility((View.VISIBLE));
                buttonAddNewProduct.setVisibility((View.VISIBLE));
                formEditTextDatePicker.setEnabled(true);
                formSpinnerCustomers.setEnabled(true);
                formSpinnerProducts.setEnabled(true);
                formEditTextProductQuantity.setEnabled(true);

                // reset Product name field
                EditTextAddNewProductName.setText("");
            }
        });

        // Validate inputs and add anew product to the selected customer.
        //
        buttonManagerOrderSettingsAddNewProductToDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // add Product to customer
                //
                Boolean isValid = true;

                Log.d("ManagerOrderBoardSettings validation :","Beginning");

                while(isValid) {

                    isValid = GeneralServices.checkIfSpinnerIsNull(spinnerAddProductCustomerName);
                    if (!isValid) {
                        Log.d("ManagerOrderBoardSettings validation :", "No customers found");
                        textViewAddProductErrorMsg.setText("You must first add customers before you can add a product.");
                        break;
                    }
                    textViewAddProductErrorMsg.setText("");


                    isValid = GeneralServices.checkEditTextIsNotNull(EditTextAddNewProductName.getText().toString());
                    if (!isValid) {
                        Log.d("ManagerOrderBoardSettings validation :", "empty inputs");
                        textViewAddProductErrorMsg.setText("Input a Customer Name.");
                        break;
                    }
                    textViewAddProductErrorMsg.setText("");

                    if(isValid)
                    {
                        Log.d("ManagerOrderBoardSettings validation :", "adding product");

                        Product product = new Product(EditTextAddNewProductName.getText().toString(),
                                spinnerAddProductCustomerName.getSelectedItem().toString(),
                                null);

                        // add customer and update dropdown list
                        //
                        OrdersBoardServices.addNewProductToCustomers(product, context, formSpinnerProducts);

                        //enable components for adding new customer
                        //
                        viewAddNewProductDialog.setVisibility(View.GONE);
                        viewAddNewProductDialog.setElevation(-2);
                        spinnerAddProductCustomerName.setVisibility(View.GONE);
                        EditTextAddNewProductName.setVisibility(View.GONE);
                        textViewAddProductErrorMsg.setVisibility(View.GONE);

                        // disable components for adding item
                        //
                        buttonAddNewOrder.setVisibility(View.VISIBLE);
                        buttonAddNewCustomer.setVisibility((View.VISIBLE));
                        buttonAddNewProduct.setVisibility((View.VISIBLE));
                        formEditTextDatePicker.setEnabled(true);
                        formSpinnerCustomers.setEnabled(true);
                        formSpinnerProducts.setEnabled(true);
                        formEditTextProductQuantity.setEnabled(true);

                        // reset customer name field
                        EditTextAddNewProductName.setText("");

                        break;
                    }
                }

            }
        });

        formSpinnerCustomers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // update the products dropdown to get the products for that customer
                //
                OrdersBoardServices.UpdateSpinnerWithProductNames(formSpinnerCustomers.getSelectedItem().toString(),context,formSpinnerProducts);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Search dB to populate customers and products spinners
        //
        OrdersBoardServices.UpdateSpinnerWithCustomerNames(formSpinnerCustomers,context);
        OrdersBoardServices.UpdateSpinnerWithCustomerNames(spinnerAddProductCustomerName,context);
        GeneralServices.SetDateToTodaysDate(formEditTextDatePicker,myCalendar);
    }
}
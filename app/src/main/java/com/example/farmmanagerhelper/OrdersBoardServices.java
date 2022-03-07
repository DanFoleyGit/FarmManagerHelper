package com.example.farmmanagerhelper;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.farmmanagerhelper.models.Customer;
import com.example.farmmanagerhelper.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersBoardServices {

    // check if the user is the manager and if they are not, make the button visible to open
    // ManagerOrdersBoardOptions activity
    //
    public static void makeOpenEditOrdersActivityVisibleForManagers(Button buttonOpenManagerOrdersBoardOptions) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef = DatabaseManager.getDatabaseReference();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // get farmId from currentUser
                String farmId = snapshot.child("users").child(currentUser.getUid()).child("UserTableFarmId").getValue().toString();

                // Check if the currentUser Id matches the farms farmManager Value and make button visible
                //
                if (currentUser.getUid().equals(snapshot.child("farm_table").child(farmId).child("managerID").getValue().toString())) {
                    buttonOpenManagerOrdersBoardOptions.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void addCustomerToFarmOrderBoard(Context context, Customer customer, Spinner mobSpinnerCustomers) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef = DatabaseManager.getDatabaseReference();
        Log.d("OrderBardServices :", "In Function:" );

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // get farmId from currentUser
                String farmId = snapshot.child("users").child(currentUser.getUid()).child("UserTableFarmId").getValue().toString();

                // Check if the customer already exists, and if it does not add it.
                //


                if (snapshot.child("farm_table").child(farmId).child("customers").child(customer.getCustomerName()).exists()) {
                    Toast.makeText(context, "Customer already exists: "+ customer.getCustomerName(), Toast.LENGTH_SHORT).show();
                    Log.d("OrderBardServices :", "Customer already exists: "+ customer.getCustomerName()  );

                }
                else
                {
                    Toast.makeText(context, "Adding customer", Toast.LENGTH_SHORT).show();
                    Log.d("OrderBardServices :", "Adding Customer: "+ customer.getCustomerName()  );
                    DatabaseManager.addNewCustomerToFarmTable(customer,farmId);
                    UpdateSpinnerWithCustomerNames(mobSpinnerCustomers,context);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // checks that a product does not exist in a customers table. if it does not it will add the
    // product to that customer and then call UpdateSpinnerWithProductNames to update the porducts
    // dropdown menu.
    //
    public static void addNewProductToCustomers(Product product, Context context, Spinner spinnerAddOrder, Spinner spinnerAddProductPopUp) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("OrdersBoardServices ", "Updating Spinner ");

        DatabaseReference dbRef = DatabaseManager.getDatabaseReference();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // get farmId from currentUser
                String farmId = snapshot.child("users").child(currentUser.getUid()).child("UserTableFarmId").getValue().toString();

                // Check if the product already exists, and if it does, dont add it.
                //
                if (snapshot.child("farm_table").child(farmId).child("customers").child(product.getCustomerItBelongsTo())
                        .child(product.getProductName()).exists()) {
                    Toast.makeText(context, "Product already exists: "+ product.getProductName(), Toast.LENGTH_SHORT).show();
                    Log.d("OrderBardServices :", "Customer already exists: "+ product.getProductName()  );

                }
                else
                {
                    Toast.makeText(context, "Adding customer", Toast.LENGTH_SHORT).show();
                    Log.d("OrderBardServices :", "Adding Customer: "+ product.getProductName()  );

                    // add customer object to database
                    DatabaseManager.addNewProductToCustomerTable(product,farmId);

                    // once its added, it can update the two spinners. One in the main UI to add order
                    // and one in the pop up to add a product
                    //
                    UpdateSpinnerWithProductNames(product.getCustomerItBelongsTo(), context, spinnerAddOrder);
                    UpdateSpinnerWithProductNames(product.getCustomerItBelongsTo(), context, spinnerAddProductPopUp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    // Get a list of customers from the database and populate the spinner
    //
    public static void UpdateSpinnerWithCustomerNames(Spinner spinner, Context context) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("OrdersBoardServices ", "Updating Spinner Customer");

        DatabaseReference dbRef = DatabaseManager.getDatabaseReference();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("users").child(currentUser.getUid()).child("UserTableFarmId").getValue().toString();
                String cust = "";
                List<String> customers = new ArrayList<String>();

                // get the farm id
                //
                Log.d("OrdersBoardServices ", "FarmId is " + farmId);

                // get the customers that is stored in the customers and add them to a list to
                // set as values for the drop down menu
                //
                for (DataSnapshot ds : snapshot.child("farm_table").child(farmId).child("customers").getChildren()) {
                    // add the users to the list
                    cust = ds.getKey();
                    customers.add(cust);
                }

                // create a new adapter which takes a list of takes customers to display
                //
                ArrayAdapter<String> spinnerUsersAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, customers);
                spinner.setAdapter(spinnerUsersAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    // get a list of all the products
    static void UpdateSpinnerWithProductNames(String customer, Context context, Spinner spinner) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("OrdersBoardServices ", "Updating Spinner Product");

        DatabaseReference dbRef = DatabaseManager.getDatabaseReference();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("users").child(currentUser.getUid()).child("UserTableFarmId").getValue().toString();
                String prod = "";
                List<String> productsList = new ArrayList<String>();

                // get the farm id
                //
                Log.d("OrdersBoardServices ", "FarmId is " + farmId);

                // get the products that is stored in the customer table that is gotten from the product
                // object and add them to a list.
                // needs to filter out the customerName field.
                // set as values for the drop down menu
                //
                for (DataSnapshot ds : snapshot.child("farm_table").child(farmId).child("customers")
                        .child(customer).getChildren()) {

                    if(!ds.getKey().equals("customerName"))
                    {
                        // add the products to the list
                        //
                        prod = ds.getKey();
                        productsList.add(prod);
                    }

                }

                Log.d("OrdersBoardServices ", "ProductList is " + productsList);


                // create a new adapter which takes a list of products to display
                //
                ArrayAdapter<String> spinnerUsersAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, productsList);
                spinner.setAdapter(spinnerUsersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }



}

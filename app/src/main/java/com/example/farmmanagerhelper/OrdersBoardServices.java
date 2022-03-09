package com.example.farmmanagerhelper;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.farmmanagerhelper.models.Customer;
import com.example.farmmanagerhelper.models.Order;
import com.example.farmmanagerhelper.models.OrderBoardOrderItem;
import com.example.farmmanagerhelper.models.Product;
import com.example.farmmanagerhelper.models.TimeSlot;
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
                Log.d("error", error.toString());
            }
        });
    }

    public static void addCustomerToFarmOrderBoard(Context context, Customer customer, Spinner formSpinnerCustomers, Spinner spinnerAddProductCustomerName) {
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
                    UpdateSpinnerWithCustomerNames(formSpinnerCustomers,context);
                    UpdateSpinnerWithCustomerNames(spinnerAddProductCustomerName,context);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }


    // checks that a product does not exist in a customers table. if it does not it will add the
    // product to that customer and then call UpdateSpinnerWithProductNames to update the porducts
    // dropdown menu.
    //
    public static void addNewProductToCustomers(Product product, Context context, Spinner spinnerAddOrder) {
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
                    //UpdateSpinnerWithProductNames(product.getCustomerItBelongsTo(), context, spinnerAddProductPopUp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
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
        Log.d("OrdersBoardServices UpdateSpinnerWithProductNames", "Updating Spinner Product");

        DatabaseReference dbRef = DatabaseManager.getDatabaseReference();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("users").child(currentUser.getUid()).child("UserTableFarmId").getValue().toString();
                String prod = "";
                List<String> productsList = new ArrayList<String>();

                // get the farm id
                //
                Log.d("OrdersBoardServices UpdateSpinnerWithProductNames", "FarmId is " + farmId);

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

                Log.d("OrdersBoardServices UpdateSpinnerWithProductNames", "ProductList is " + productsList);


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

     // TODO if zero is entered as quantity, find the match and delete it


    // Adds order to the customer/product/orders with a unique id. Checks to see if an order for that
    // product exists already and if it does it replaces it. If not it adds the new order. This way
    // for each product and date, only 1 order can exist. It will either be added or updated.
    //
    public static void AddOrder(Order order, Context context) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();

                Log.d("OrdersBoardServices addOrder", "farm Id is " + farmId);

                DatabaseReference dbFarmRef =  DatabaseManager.getFarmDatabaseReferenceByName(farmId);

                dbFarmRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String keyFromPush = "";
                        boolean writeNewOrder = true;

                        for (DataSnapshot ds : snapshot.child("customers").child(order.getCustomer())
                                .child(order.getProduct()).child("orders").getChildren()) {


                            Log.d("OrdersBoardServices addOrder", ds.toString());


                            //check if the order date exists already
                            if (ds.child("orderDate").getValue().toString().equals(order.getOrderDate())) {
                                //replace order
                                Log.d("OrdersBoardServices addOrder", " replacing order " +order.getProduct() + " on "+ order.getOrderDate());
                                Toast.makeText(context, "Updating Order " + order.getOrderDate(), Toast.LENGTH_SHORT).show();

                                order.setOrderID(ds.getKey());
                                DatabaseManager.replaceOrderForCustomer(order,farmId,dbFarmRef);

                                writeNewOrder = false;
                                break;

                            }
                        }
                        if(writeNewOrder)
                        {
                            //write new order
                            Log.d("OrdersBoardServices addOrder", " Writing new order");

                            //get unique key
                            //
                            keyFromPush = dbFarmRef.child("customers").child(order.getCustomer())
                                    .child(order.getProduct()).push().getKey();
                            order.setOrderID(keyFromPush);

                            DatabaseManager.addOrderToCustomer(order,farmId,dbFarmRef);

                            Toast.makeText(context, "Adding Order", Toast.LENGTH_SHORT).show();

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

    public static void testOrderboard(Context context, ListView listView) {
        OrderBoardOrderItem item1 = new OrderBoardOrderItem("The Shop",null,false);
        OrderBoardOrderItem item11 = new OrderBoardOrderItem("item 1","10",false);
        OrderBoardOrderItem item2 = new OrderBoardOrderItem("item2","20",false);
        OrderBoardOrderItem item3 = new OrderBoardOrderItem("item 3","180",false);

        OrderBoardOrderItem item4 = new OrderBoardOrderItem("The Other Shop",null,false);
        OrderBoardOrderItem item5 = new OrderBoardOrderItem("item 1","120",false);
        OrderBoardOrderItem item6 = new OrderBoardOrderItem("item 2","90",false);
        OrderBoardOrderItem item7 = new OrderBoardOrderItem("item 3","180",false);
        
        ArrayList<OrderBoardOrderItem> orders = new ArrayList<>();

        orders.add(item1);
        orders.add(item11);
        orders.add(item2);
        orders.add(item3);
        orders.add(item4);
        orders.add(item5);
        orders.add(item6);
        orders.add(item7);

        OrdersBoardListAdapter adapter = new OrdersBoardListAdapter(context,R.layout.orders_board_row,orders);
        listView.setAdapter(adapter);

    }

    // gets thefarm id and then gets a database reference for the customer table in that farm.
    // loops through all the customers, each time looping through their products. If the product date
    // matches the date provided, its details are added to a list of OrderBoardOrderItem. Once all that customers Items are
    // added, it creates a header for the next customer and repeats until it has all the orders for that
    // day. The object is then passed to the OrdersBoardListAdapter to display to the user.
    //
    public static void updateOrderBoardWithDate(String date, Context context, ListView listView) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // get the farm id
        //
        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

        ArrayList<OrderBoardOrderItem> orderBoard = new ArrayList<>();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();
                Log.d("OrdersBoardServices updateOrderBoardWithDate", "farm Id is " + farmId);

                // get the customer table for that farm.
                //
                DatabaseReference dbCustomerRef = DatabaseManager.getCustomerTableDatabaseReferenceByFarmName(farmId);

                dbCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // loop through all customers
                        //
                        for (DataSnapshot dsCust : snapshot.getChildren()) {
                            Log.d("OrdersBoardServices updateOrderBoardWithDate", "Searching Customer " + dsCust.getKey());


                            OrderBoardOrderItem customerHeader = new OrderBoardOrderItem(dsCust.getKey(),null, false);
                            orderBoard.add(customerHeader);

                            // loop through products
                            for (DataSnapshot dsProd : snapshot.child(dsCust.getKey()).getChildren()) {
                                Log.d("OrdersBoardServices updateOrderBoardWithDate", "Searching Product " + dsProd.getKey());

                                // loop through products
                                for (DataSnapshot dsOrder : snapshot.child(dsCust.getKey()).child(dsProd.getKey()).child("orders").getChildren()) {
                                    Log.d("OrdersBoardServices updateOrderBoardWithDate", "Searching orders " + dsOrder.getKey());

                                    // get Order in object
                                    Order order = dsOrder.getValue(Order.class);

                                    // If the order object's date matches the date provided, copy its details and add it to teh
                                    if(order.getOrderDate().equals(date))
                                    {
                                        OrderBoardOrderItem item = new OrderBoardOrderItem(order.getProduct(),order.getQuantity(),order.isOrderComplete());
                                        orderBoard.add(item);
                                    }

                                }
                            }
                        }

                        OrdersBoardListAdapter adapter = new OrdersBoardListAdapter(context,R.layout.orders_board_row,orderBoard);
                        listView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }
}

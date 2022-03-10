package com.example.farmmanagerhelper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.farmmanagerhelper.models.Customer;
import com.example.farmmanagerhelper.models.Farm;
import com.example.farmmanagerhelper.models.Order;
import com.example.farmmanagerhelper.models.Product;
import com.example.farmmanagerhelper.models.TimetableTask;
import com.example.farmmanagerhelper.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseManager {
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference();

    // other variables
    String stringIndicatingUserIsNotInFarm = "none";

    public static DatabaseReference getDatabaseReference()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);
        DatabaseReference dbRef = database.getReference();

        return dbRef;
    }

    public static DatabaseReference getUsersTableDatabaseReference(String userId)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);
        DatabaseReference dbRef = database.getReference().child("users").child(userId);

        return dbRef;
    }

    public static DatabaseReference getFarmDatabaseReferenceByName(String farmName)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);
        DatabaseReference dbRef = database.getReference().child("farm_table").child(farmName);

        return dbRef;
    }

    public static DatabaseReference getCustomerTableDatabaseReferenceByFarmName(String farmName)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);
        DatabaseReference dbRef = database.getReference().child("farm_table").child(farmName).child("customers");

        return dbRef;
    }

    public static boolean addUserToDatabase(User user)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();

        // add user object to the table making the user ID the childnode/ primary key
        dbRef.child("users").child(user.userId).setValue(user);

        return true;
    }

    // adds new farm to database. the adds the user to the farm list and adds the farm name to the
    // user
    public static boolean addFarmToDatabase(Farm farm)
    {
        Log.d("DatabaseManager adding farm to db :", farm.farmName);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();

        // add user object to the table making the user ID the child node/ primary key
        dbRef.child("farm_table").child(farm.farmName).setValue(farm);
        Log.d("Database access: "," Created new farm table" );

        // might need to search for the farm, and thn add the user in farm to the list
        // as a new child node
        dbRef.child("farm_table").child(farm.farmName).child("usersInFarm").child(farm.managerID).setValue(farm.managerID);

        // add farm to user
        AddFarmNameToUserAndUserToFarm(farm.farmName,farm.managerID);
        //add
        // create timetable
        // create order board table

        return true;
    }

    public static void AddFarmNameToUserAndUserToFarm(String farmName, String userID)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();

        dbRef.child("users").child(userID).child("UserTableFarmId").setValue(farmName);

        // get the users name and add it as the value for the node id
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String firstName = snapshot.child("users").child(userID).child("firstName").getValue().toString();
                String lastName = snapshot.child("users").child(userID).child("lastName").getValue().toString();
                String fullName = firstName + " " + lastName;

                //dbRef.child("farm_table").child(farmName).child("usersInFarm").child(userID).setValue(fullName);
                dbRef.child("farm_table").child(farmName).child("usersInFarm").child(userID);
                dbRef.child("farm_table").child(farmName).child("usersInFarm").child(userID).child("name").setValue(fullName);

                Log.d("DatabaseManager AddFarmNameToUserAndUserToFarm",fullName + " added to farm");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DatabaseManager AddFarmNameToUserAndUserToFarm","Error adding user to farm");

            }
        });

    }

    // example of how to use this
    // DatabaseManager.removeFarmNameFromUserAndUserFromFarm(farm.farmName,firebaseUser.getUid());
    public static void removeFarmNameFromUserAndUserFromFarm(String farmName, String userID)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();


        dbRef.child("farm_table").child(farmName).child("usersInFarm").child(userID).removeValue();
        dbRef.child("users").child(userID).child("UserTableFarmId").setValue("none");
        Log.d("Database access: "," Removed user: " +userID+ " from farm : "+ farmName );
    }

    // checks the user id is in the database and checks if the user id is in a farm. returns true
    // if it is and false if not
    public static DatabaseReference getDatabaseRefForUserId(FirebaseUser firebaseUser)
    {

        // https://stackoverflow.com/questions/67832715/how-to-retrieve-specific-data-from-firebase-realtime-database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference().child("users").child(firebaseUser.getUid());

        return dbRef;

    }

    public static DatabaseReference getDatabaseRefForFarmName(String farmNameFromInput)
    {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference().child("farm_table").child(farmNameFromInput);
        return dbRef;
    }

    public static void AddNewTaskToUserInFarmTimeTable(String userId, String farmId, TimetableTask task) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();

        // add new task to the user in usersInFarm giving the unique key as the identifier
        dbRef.child("farm_table").child(farmId).child("usersInFarm").child(userId).child("timetable").child(task.getTaskID()).setValue(task);
        Log.d("Database write: ","New task added to " +userId+ " from farm : "+ farmId );

    }

    public static void addNewCustomerToFarmTable(Customer customer, String farmId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();

        dbRef.child("farm_table").child(farmId).child("customers").child(customer.getCustomerName()).setValue(customer);

        Log.d("Database write: ","New customer " + customer.getCustomerName()+ " from farm : "+ farmId );

    }

    public static void addNewProductToCustomerTable(Product product, String farmId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();

        dbRef.child("farm_table").child(farmId).child("customers").child(product.getCustomerItBelongsTo())
                .child(product.getProductName()).setValue(product);

        Log.d("Database write: ","New customer " + product.getProductName()+ " from farm : "+ farmId );
    }

    // takes the farm database reference and adds the order to the customer
    //
    public static void addOrderToCustomer(Order order, String farmId, DatabaseReference dbFarmRef) {

        dbFarmRef.child("customers").child(order.getCustomer()).child(order.getProduct()).child("orders").child(order.getOrderID()).setValue(order);

        Log.d("Database write addOrderToCustomer","New order " + order.getOrderID() + " from farm : "+ farmId );

    }

    // deletes the previous order and writes the new order in its place. It uses a an on success listener
    // to wait for the delete and then rewrite the new order with the same id
    //
    public static void replaceOrderForCustomer(Order order, String farmId, DatabaseReference dbFarmRef) {

        dbFarmRef.child("customers").child(order.getCustomer()).child(order.getProduct()).child("orders")
                .child(order.getOrderID()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dbFarmRef.child("customers").child(order.getCustomer()).child(order.getProduct()).child("orders").child(order.getOrderID()).setValue(order);
                Log.d("Database write addOrderToCustomer","Updating " + order.getOrderID() + " in farm : "+ farmId );
            }
        });
    }


    // Takes a farm reference and navigates to customer and removes the order provided
    //
    public static void deleteOrderForCustomer(Order order, String farmId, DatabaseReference dbFarmRef) {
        dbFarmRef.child("customers").child(order.getCustomer()).child(order.getProduct()).child("orders")
                .child(order.getOrderID()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Database write deleteOrderForCustomer","Deleted " + order.getOrderID() + " in farm "+ farmId +" successfully.");
            }
        });
    }


    // Takes a database reference and of the customer table for the farm and an Object. It navigates down to the
    // the object and and sets the value of orderComplete in the database to correspond with the Order object
    //
    public static void changeOrderCompleteStatusValue(DatabaseReference dbCustomerRef, Order order) {
        dbCustomerRef.child(order.getCustomer()).child(order.getProduct()).child("orders").child(order.getOrderID()).child("orderComplete")
                .setValue(order.isOrderComplete()).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            Log.d("Database write deleteOrderForCustomer","Updated " + order.getOrderID() + " to " + order.isOrderComplete());
        }
    });
    }
}

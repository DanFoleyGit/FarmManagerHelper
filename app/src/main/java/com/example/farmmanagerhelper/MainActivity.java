package com.example.farmmanagerhelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.farmmanagerhelper.models.TimeSlot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Context context = this;

    // Firebase
    private FirebaseAuth mAuth;

    Boolean isManager = false;
    Button openTimetableActivity = null;
    Button openOrdersBoard = null;
    Button openShippingCalculator = null;
    Button openProduceEstimator = null;
    ListView listView = null;
    TextView UserEmail = null;
    ProgressBar loadingIcon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // UI
        openTimetableActivity = findViewById(R.id.buttonOpenTimetableActivity);
        openOrdersBoard = findViewById(R.id.buttonOpenOrdersActivity);
        openShippingCalculator = findViewById(R.id.buttonOpenShioppingCalculatorActivity);
        openProduceEstimator = findViewById(R.id.buttonOpenProduceEstimator);
        UserEmail = findViewById(R.id.txtMainActivityLoggedInAsUserX);
        loadingIcon = findViewById(R.id.progressIconMainActivity);

        listView = (ListView) findViewById(R.id.MainActivityTimeTableListView);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        UserEmail.setText( currentUser.getEmail());

        // load the timetable and set it the current hour
        //
        loadTimeTable(currentUser);

        // check if the user is a manager or not for opening the timetable activity
        //
        checkIsUserManager(currentUser);




        // Listeners for buttons on home screen
        //
        openTimetableActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // need to check if the user is a manager or normal staff
                if(isManager)
                {
                    // open manager timetable activity, without closing the main activity so user can use the back

                    startActivity(new Intent(MainActivity.this, ManagerTimetable.class));
                }
                else
                {
                    // open timetable activity, without closing the main activity so user can use the back
                    //
                    startActivity(new Intent(MainActivity.this, StaffTimetable.class));
                }
            }
        });

        openOrdersBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // need to check if the user is a manager or normal staff
                if(isManager)
                {
                    // open Manager orders board activity, without closing the main activity so user can use the back
                    startActivity(new Intent(MainActivity.this, OrdersBoard.class));
                }
                else
                {
                    // open orders board activity, without closing the main activity so user can use the back
                    startActivity(new Intent(MainActivity.this, OrdersBoard.class));

                }
//                openOrdersBoardForUserType(currentUser);
            }
        });

        openShippingCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ShippingCalculator.class));

            }
        });

        openProduceEstimator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProduceEstimator.class));
            }
        });


    }

    // Loads the time table for the user and sets the position to the current hour.
    //
    private void loadTimeTable(FirebaseUser currentUser) {

        // get current date, load template and call updateMainMenuTimeTableWithTodaysDate to get data
        //
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
        String todaysDate = dateFormat.format(new Date());

        ArrayList<TimeSlot> timetable = TimetableServices.getTimeSlotTemplate();

        TimetableListAdapter timetableAdapter = new TimetableListAdapter(context, R.layout.timetable_time_slot, timetable);
        listView.setAdapter(timetableAdapter);

        TimetableServices.updateMainMenuTimeTableWithTodaysDate(todaysDate, listView, context);

        // Set listener for updates to users timetable
        //
        setListenerForNewTasks(currentUser,todaysDate,context,listView);
    }

    // Listener set on that users timetable document. If it is updated it will call updateMainMenuTimeTableWithTodaysDate
    // to refresh their timetable in realtime
    //
    private void setListenerForNewTasks(FirebaseUser currentUser, String todaysDate, Context context, ListView listView) {

        DatabaseReference dbRef = DatabaseManager.getUsersTableDatabaseReference(currentUser.getUid());

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String farmId = snapshot.child("UserTableFarmId").getValue().toString();
                DatabaseReference dbUsersInFarmRef =  DatabaseManager.getUsersInFarmDocumentByFarmName(farmId);

                // set a listener on that users ID to trigger when it is updated with tasks
                //
                dbUsersInFarmRef.child(currentUser.getUid()).child("timetable").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        TimetableServices.updateMainMenuTimeTableWithTodaysDate(todaysDate, listView, context);
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

    //checks if the user is manager and if the user is sets isManager to true
    //
    private void checkIsUserManager(FirebaseUser currentUser)
    {
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
                            isManager = true;
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




    // Menu Icon in top right
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.menu,menu );
        return super.onCreateOptionsMenu ( menu );

    }

    // Logout user and leave farm options in the action bar
    //
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            // set up as a case statement to allow extra options if needed
            case R.id.MenuLogout:
                // sign out user

                UserServices.SignOutAccount();
                Toast.makeText(MainActivity.this, "User signed out",
                        Toast.LENGTH_SHORT).show();

                // start login intent
                Intent intent = new Intent(MainActivity.this, com.example.farmmanagerhelper.LoginActivity.class);
                startActivity(intent);
                finish();
                break;

            // set up as a case statement to allow extra options if needed
            case R.id.MenuLeaveFarm:
                // sign out user

                UserServices.leaveFarm();
                Toast.makeText(MainActivity.this, "User signed out",
                        Toast.LENGTH_SHORT).show();

                // start JoinFarm intent
                startActivity(new Intent(MainActivity.this, JoinFarm.class));
                finish();
                break;

        }
        return true;
    }

    // Disable the buttons to open new views
    //
    @Override
    protected void onPause() {
        super.onPause();
        openTimetableActivity.setEnabled(false);
        openOrdersBoard.setEnabled(false);
        openShippingCalculator.setEnabled(false);
        openProduceEstimator.setEnabled(false);
        loadingIcon.setVisibility(View.VISIBLE);
    }

    // Enable the buttons
    //
    @Override
    protected void onPostResume() {
        super.onPostResume();
        openTimetableActivity.setEnabled(true);
        openOrdersBoard.setEnabled(true);
        openShippingCalculator.setEnabled(true);
        openProduceEstimator.setEnabled(true);
        loadingIcon.setVisibility(View.GONE);

    }
}
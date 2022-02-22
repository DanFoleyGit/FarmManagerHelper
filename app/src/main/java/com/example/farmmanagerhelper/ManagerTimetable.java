package com.example.farmmanagerhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmmanagerhelper.models.TimeSlot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


// create list view
// list item with time slot, name, description
// create object of times containing time in half hour increments with name, description, start time end time, colour
// initilise the list of time slot objects with name, description start/endtime being null
// do db call to get times
// add them into object
// display list
// customize colours



public class ManagerTimetable extends AppCompatActivity {


    final Calendar myCalendar = Calendar.getInstance();
    private static final String TAG = "ManagerTimetable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_timetable);
        // UI elements
        Button buttonSubmit = findViewById(R.id.ManagerTimeTableSubmitButton);
        Spinner spinnerStartTime = findViewById(R.id.ManagerTimetableSpinnerStartTime);
        Spinner spinnerEndTime = findViewById(R.id.ManagerTimetableSpinnerEndTime);
        Spinner spinnerUsersNames = findViewById(R.id.ManagerTimetableSpinnerUsersNames);
        TextView errorMessage = findViewById(R.id.ManagerTimetableErrorMsg);


        // set up the listView
        ListView listView = (ListView) findViewById(R.id.ManagerTimeTableListView);

        // get template with times
        ArrayList<TimeSlot> timetable = TimetableServices.getTimeSlotTemplate();


        // create custom list timetableAdapter
        TimetableListAdapter timetableAdapter = new TimetableListAdapter(this, R.layout.timetable_time_slot, timetable);
        listView.setAdapter(timetableAdapter);

        // set up the date picker pop up
        EditText editTextDatePicker = findViewById(R.id.ManagerTimeTableDatePicker);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel(editTextDatePicker, myCalendar);
            }
        };

        // set on click listener for calander popup
        editTextDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ManagerTimetable.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // set up spinners to act as dropdown lists
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.Times_array, android.R.layout.simple_spinner_item);

        // set the spinner to drop down
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartTime.setAdapter(spinnerAdapter);
        spinnerEndTime.setAdapter(spinnerAdapter);

        // get users in an array and add it to the spinner
        getUsersFromFarmTableForManagerTimeTable(spinnerUsersNames);





        // onclick method for submitting the form
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startTime = spinnerStartTime.getSelectedItem().toString();
                String endTime = spinnerEndTime.getSelectedItem().toString();
                String date = editTextDatePicker.getText().toString();
                int intStartTime = 0;
                int intEndTime = 0;



                intStartTime = TimetableServices.convertStringTimeInputToInt(startTime);
                intEndTime = TimetableServices.convertStringTimeInputToInt(endTime);

                Log.d("ManagerTimetable int startTime", String.valueOf(intStartTime));


                boolean isValid = true;

                Log.d("ManagerTimeTableValidation validation :","Beginning");
                while(isValid) {

                    isValid = TimetableServices.checkValuesAreNotNull(date);
                    if (!isValid) {
                        Log.d("ManagerTimetable validation :", "empty inputs");
                        errorMessage.setText("Inputs must be filled in");
                        break;
                    }
                    errorMessage.setText("");

                    isValid = TimetableServices.checkStartAndEndTimesAreChronological(intStartTime,intEndTime);

                    if (!isValid)
                    {
                        Log.d("ManagerTimetable validation :", "start and end time in wrong order");
                        errorMessage.setText("Start Time must come before end time.");
                        break;
                    }
                    errorMessage.setText("");

                    if (isValid) {
                        Toast.makeText(ManagerTimetable.this, "Heres the selected " + date + " " + startTime + " " + endTime + " " + intStartTime + " " + intEndTime,
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

            }
        });


    }

    // Gets the managers userId, then gets the list of users from the farm and adds their names into
    // a spinner list that is displayed to the manager
    private void getUsersFromFarmTableForManagerTimeTable(Spinner spinnerUsersNames) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("ManagerTimetable ", "userId is " + currentUser.getUid());

        // get dbref for user id
        DatabaseReference dbRef = DatabaseManager.getDatabaseReference();

        List<String> usersByName = new ArrayList<String>();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get the farm id
                String farmId = snapshot.child("users").child(currentUser.getUid()).child("UserTableFarmId").getValue().toString();
                Log.d("ManagerTimetable ", "FarmId is " + farmId);

                // get the users names that is stored in the farm table
                for(DataSnapshot ds : snapshot.child("farm_table").child(farmId).child("usersInFarm").getChildren())
                {
                    // add the users to the list
                    String name = ds.getValue().toString();
                    usersByName.add(name);
                }

                // create a new adapter which takes a list of users and takes usersByName to display
                ArrayAdapter<String> spinnerUsersAdapter = new ArrayAdapter<String>(ManagerTimetable.this, android.R.layout.simple_spinner_dropdown_item, usersByName);
                spinnerUsersNames.setAdapter(spinnerUsersAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    // updates the EditText after the user has selected the date they want.
    private void updateLabel(EditText datePicker, Calendar myCalandar) {
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
        datePicker.setText(dateFormat.format(myCalandar.getTime()));
    }


}

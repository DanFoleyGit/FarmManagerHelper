package com.example.farmmanagerhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.farmmanagerhelper.models.TimeSlot;
import com.example.farmmanagerhelper.models.TimetableTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class StaffTimetable extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_timetable);

        EditText editTextDatePicker = findViewById(R.id.StaffTimeTableDatePicker);

        // set up the listView
        ListView listView = (ListView) findViewById(R.id.ManagerTimeTableListView);

        // get template with times
        ArrayList<TimeSlot> timetable = TimetableServices.getTimeSlotTemplate();


        //set date up date picker
        // start by setting the edit text to today's date
        TimetableServices.SetDateToTodaysDate(editTextDatePicker, myCalendar);

        // set up the dialog box to give dates in dd/MM/yy
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                // update editTextDatePicker to show new date
                TimetableServices.updateLabel(editTextDatePicker, myCalendar);

                // update the timetable
                getDatesTasksAndUpdateTimeTable(editTextDatePicker.getText().toString(),listView);
            }
        };

        // set on click listener for calender popup
        editTextDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(StaffTimetable.this, date ,
                        myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // update the timetable with todays date that is set in the editText via onCreate
        getDatesTasksAndUpdateTimeTable(editTextDatePicker.getText().toString(),listView);

    }

    private void getDatesTasksAndUpdateTimeTable(String date, ListView listView) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // get dbref for user id
        DatabaseReference dbRef = DatabaseManager.getDatabaseReference();
        ArrayList<TimeSlot> timetable = TimetableServices.getTimeSlotTemplate();


        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get the farm id
                String farmId = snapshot.child("users").child(currentUser.getUid()).child("UserTableFarmId").getValue().toString();
                Log.d("Staff Timetable ", "Adding timetable to user called");

                // get user id's from the usersInFarmTable
                for (DataSnapshot ds : snapshot.child("farm_table").child(farmId).child("usersInFarm")
                        .child(currentUser.getUid()).child("timetable").getChildren()) {
                    // add the users to the list

                    if(date.equals(ds.child("taskDate").getValue().toString()))
                    {
                        //add task to list
                        Toast toast = Toast.makeText(StaffTimetable.this, "Found a match", Toast.LENGTH_SHORT);
                        toast.show();

                        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                        TimetableTask task = ds.getValue(TimetableTask.class); // change this to add each paramter manually and tidy up declarations
                        Log.d("Staff Timetable ", "TimeTableTask " + task.getTaskName() + "starts at " + task.getTaskStartTime());


                        // This algorithm loops through the Timetable containing a list of time slots in 30 minute
                        // increments. If the start time of a task matches the timeslot as it loops, it will set that
                        // slots task name to its task name. It will then call a function findLegnthOfTask() which takes
                        // the task start time and end time, subtracts teh start time from the end time and divides that
                        // by 30 to find out how many slots it is on for. It will then loop X amount of time and set
                        // the timeslots to that task for the length of that task.
                        for(int i =0; i<timetable.size(); i++)
                        {
                            TimeSlot currentSlot = timetable.get(i);
                            if(currentSlot.getTimeSlotTime().equals(task.getTaskStartTime()))
                            {
                                currentSlot.setTimeSlotName(task.getTaskName());

                                int numberOfSlotsNeededForTask = TimetableServices.findLengthOfTask(task);

                                for(int j = i; j < i + numberOfSlotsNeededForTask; j++)
                                {
                                    TimeSlot nextSlotsForTaskDuration = timetable.get(j);
                                    nextSlotsForTaskDuration.setTimeSlotName(task.getTaskName());

                                }
                            }
                        }
                        // create custom list timetableAdapter
                        TimetableListAdapter timetableAdapter = new TimetableListAdapter(StaffTimetable.this, R.layout.timetable_time_slot, timetable);
                        listView.setAdapter(timetableAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }


}
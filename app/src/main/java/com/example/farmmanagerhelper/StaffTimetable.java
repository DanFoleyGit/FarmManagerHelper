package com.example.farmmanagerhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StaffTimetable extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_timetable);
        Context context = this;

        EditText editTextDatePicker = findViewById(R.id.StaffTimeTableDatePicker);
        ImageButton buttonForwardDate = findViewById(R.id.StaffTimeTableForwardDateButton);
        ImageButton buttonBackDate = findViewById(R.id.StaffTimeTableBackDateButton);


        // set up the listView
        //
        ListView listView = (ListView) findViewById(R.id.ManagerTimeTableListView);

        // get template with times
        //
        ArrayList<TimeSlot> timetable = TimetableServices.getTimeSlotTemplate();


        //set date up date picker
        // start by setting the edit text to today's date
        //
        TimetableServices.SetDateToTodaysDate(editTextDatePicker, myCalendar);

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
                TimetableServices.updateLabel(editTextDatePicker, myCalendar);

                // update the timetable
                //
                TimetableServices.getDatesTasksAndUpdateTimeTable(editTextDatePicker.getText().toString(), listView, context);
            }
        };

        // set on click listener for calender popup.
        //
        editTextDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(StaffTimetable.this, date ,
                        myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Adjust the date forward and call update the timetable
        buttonForwardDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.UK);
                    Calendar c = Calendar.getInstance();

                try {
                    c.setTime(dateFormat.parse(editTextDatePicker.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c.add(Calendar.DATE,1);
                editTextDatePicker.setText(dateFormat.format(c.getTime()));

                TimetableServices.getDatesTasksAndUpdateTimeTable(editTextDatePicker.getText().toString(), listView, context);
            }
        });

        // Adjust the date forward and call update the timetable
        buttonBackDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.UK);
                Calendar c = Calendar.getInstance();

                try {
                    c.setTime(dateFormat.parse(editTextDatePicker.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c.add(Calendar.DATE,-1);
                editTextDatePicker.setText(dateFormat.format(c.getTime()));

                TimetableServices.getDatesTasksAndUpdateTimeTable(editTextDatePicker.getText().toString(),listView, context);
            }
        });

        // update the timetable with todays date that is set in the editText via onCreate.
        //
        TimetableServices.getDatesTasksAndUpdateTimeTable(editTextDatePicker.getText().toString(), listView, context);

    }

    private void getDatesTasksAndUpdateTimeTable(String date, ListView listView) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // get dbref for user id
        //
        DatabaseReference dbRef = DatabaseManager.getDatabaseReference();
        ArrayList<TimeSlot> timetable = TimetableServices.getTimeSlotTemplate();


        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get the farm id
                //
                String farmId = snapshot.child("users").child(currentUser.getUid()).child("UserTableFarmId").getValue().toString();
                int count = 0;

                // get user id's from the usersInFarmTable
                //
                if(snapshot.child("farm_table").child(farmId).child("usersInFarm").child(currentUser.getUid()).child("timetable").hasChildren())
                {
                    for (DataSnapshot ds : snapshot.child("farm_table").child(farmId).child("usersInFarm")
                            .child(currentUser.getUid()).child("timetable").getChildren()) {

                        // add the users to the list
                        //
                        if(date.equals(ds.child("taskDate").getValue().toString()))
                        {
                            count++;
                            Log.d("Staff Timetable ", "Count is : " + count);

                            //add task to list
                            //
                            TimetableTask task = ds.getValue(TimetableTask.class);
                            Log.d("Staff Timetable ", "TimeTableTask " + task.getTaskName() + "starts at " + task.getTaskStartTime());

                            // This algorithm loops through the Timetable containing a list of time slots in 30 minute
                            // increments. If the start time of a task matches the timeslot as it loops, it will set that
                            // slots task name to its task name. It will then call a function findLegnthOfTask() which takes
                            // the task start time and end time, subtracts the start time from the end time and divides that
                            // by 30 to find out how many slots it is on for. It will then loop X amount of time and set
                            // the timeslots to that task for the length of that task.
                            //
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
                            //
                            TimetableListAdapter timetableAdapter = new TimetableListAdapter(StaffTimetable.this, R.layout.timetable_time_slot, timetable);
                            listView.setAdapter(timetableAdapter);

                        }

                    }

                    // If there are no tasks for the date, reset the timetable to be blank
                    //
                    if(count<1)
                    {
                        Toast.makeText(StaffTimetable.this, "No Tasks assigned for " + date, Toast.LENGTH_SHORT).show();
                        Log.d("Staff Timetable ", "No Tasks assigned for " + date);

                        ArrayList<TimeSlot> blankTimetable = TimetableServices.getTimeSlotTemplate();
                        TimetableListAdapter timetableAdapter = new TimetableListAdapter(StaffTimetable.this, R.layout.timetable_time_slot, blankTimetable);
                        listView.setAdapter(timetableAdapter);
                    }

                }
                else
                {
                    // if there are no tasks found for that date, give user the feedback
                    //Toast.makeText(StaffTimetable.this, "No Tasks assigned for " + date, Toast.LENGTH_SHORT).show();
                    Log.d("Staff Timetable ", "TimeTableTask user Does not have a timetable.");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }


}
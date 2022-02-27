package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.example.farmmanagerhelper.models.TimeSlot;
import com.example.farmmanagerhelper.models.TimetableTask;

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



        TimetableTask task = new TimetableTask("1", "Pick Block 1",
                "Johnny test", "27/02/22",
                null, "05:00", "06:00", false, null);

        TimetableTask task2 = new TimetableTask("1", "Pick Block 2",
                "Johnny test", "27/02/22",
                null, "05:30", "10:00", false, null);

        // get template with times
        ArrayList<TimeSlot> timetable = TimetableServices.getTimeSlotTemplate();

        // This block of code loops through the Timetable containing a list of time slots in 30 minute
        // increments. If the start time of a task matches the timeslot as it loops, it will set that
        // slots task name to its task name. It will then call a function findLegnthOfTask which takes
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
        TimetableListAdapter timetableAdapter = new TimetableListAdapter(this, R.layout.timetable_time_slot, timetable);
        listView.setAdapter(timetableAdapter);

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
                TimetableServices.updateLabel(editTextDatePicker, myCalendar);
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
    }


}
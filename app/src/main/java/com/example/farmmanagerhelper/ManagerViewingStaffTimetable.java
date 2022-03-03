package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.farmmanagerhelper.models.TimeSlot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.function.ToDoubleBiFunction;

public class ManagerViewingStaffTimetable extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_viewing_staff_timetable);

        EditText MvtEditTextDatePicker = findViewById(R.id.ManagerViewingStaffTimetableEditTextDatePicker);
        Spinner MvtSpinnerUsersNames = findViewById(R.id.ManagerViewStaffTimetableSpinnerUsersNames);
        Context context = this;

        // set up spinners to act as dropdown lists
        //
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.Times_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        TimetableServices.getUsersNamesFromFarmTableForManagerTimeTable(MvtSpinnerUsersNames, context);

        // set up the listView
        //
        ListView listView = (ListView) findViewById(R.id.ManagerTimeTableListView);

        // get template with times
        //
        ArrayList<TimeSlot> timetable = TimetableServices.getTimeSlotTemplate();


        //set date up date picker
        // start by setting the edit text to today's date
        //
        TimetableServices.SetDateToTodaysDate(MvtEditTextDatePicker, myCalendar);

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
                TimetableServices.updateLabel(MvtEditTextDatePicker, myCalendar);

                // update the timetable
                //
                TimetableServices.getDatesTasksAndUpdateTimeTableWithName(MvtEditTextDatePicker.getText().toString(),
                        MvtSpinnerUsersNames.getSelectedItem().toString(), listView, context);
            }
        };

        // set on click listener for calender popup
        MvtEditTextDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ManagerViewingStaffTimetable.this, date ,
                        myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // set an on click method for the spinner containing the names of users in the farm.
        // When the names are loaded from the server, this method is called automatically to load
        // the first users timetable
        MvtSpinnerUsersNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TimetableServices.getDatesTasksAndUpdateTimeTableWithName(MvtEditTextDatePicker.getText().toString(),
                        MvtSpinnerUsersNames.getSelectedItem().toString(), listView, context);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//  TODO
        // Using an on ItemClick Listener can allow managers to edit and set single hour tasks
        //
        // Get the time, create pop up window to add endtime and name using selected as the start
        // time
        // https://stackoverflow.com/questions/5944987/how-to-create-a-popup-window-popupwindow-in-android
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "Time is: " + timetable.get(i).getTimeSlotTime(), Toast.LENGTH_SHORT).show();
                Log.d("ManagerViewingStaffTimetable", "Time is: " + timetable.get(i).getTimeSlotTime());

            }
        });

        // create custom list timetableAdapter
        //
        TimetableListAdapter timetableAdapter = new TimetableListAdapter(this, R.layout.timetable_time_slot, timetable);
        listView.setAdapter(timetableAdapter);

    }
}
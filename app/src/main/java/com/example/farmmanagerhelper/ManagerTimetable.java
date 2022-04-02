package com.example.farmmanagerhelper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.farmmanagerhelper.models.TimetableTask;

import java.util.Calendar;


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
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_timetable);
        // UI elements
        Button buttonSubmit = findViewById(R.id.ManagerTimeTableSubmitButton);
        Button buttonViewStaff = findViewById(R.id.managerTimeTableViewStaffButton);
        Spinner spinnerStartTime = findViewById(R.id.ManagerTimetableSpinnerStartTime);
        Spinner spinnerEndTime = findViewById(R.id.ManagerTimetableSpinnerEndTime);
        Spinner spinnerUsersNames = findViewById(R.id.ManagerTimetableSpinnerUsersNames);
        EditText editTextDatePicker = findViewById(R.id.ManagerTimeTableDatePicker);
        EditText editTextTaskName = findViewById(R.id.editTextManagerTimetableTaskName);
        TextView errorMessage = findViewById(R.id.ManagerTimetableErrorMsg);
        Context context = this;


        // Setting the edit text to today's date
        TimetableServices.SetDateToTodaysDate(editTextDatePicker, myCalendar);

        // set up the date picker pop up
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                TimetableServices.updateLabel(editTextDatePicker, myCalendar);
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

        // set the spinner to drop down and add the list of times to it
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartTime.setAdapter(spinnerAdapter);
        spinnerEndTime.setAdapter(spinnerAdapter);

        // get users in an array and add it to the spinner
        TimetableServices.getUsersNamesFromFarmTableForManagerTimeTable(spinnerUsersNames,context);

        // Button to Open the view staff timetable view.
        buttonViewStaff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ManagerTimetable.this, com.example.farmmanagerhelper.ManagerViewingStaffTimetable.class);
                    startActivity(intent);
                }
            });

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

                    isValid = TimetableServices.checkValuesAreNotNull(date,editTextTaskName.getText().toString());
                    if (!isValid) {
                        Log.d("ManagerTimetable validation :", "empty inputs");
                        errorMessage.setText("Inputs must be filled in");
                        break;
                    }
                    errorMessage.setText("");


                    isValid = GeneralServices.checkStringDoesNotContainForwardSlashCharacter(editTextTaskName.getText().toString());
                    if (!isValid) {
                        Log.d("ManagerTimetable validation :", "Forward slash found in string");
                        errorMessage.setText("Can not use \" / \" in inputs");
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

                    isValid = TimetableServices.checkTimesAreNotTheSame(intStartTime,intEndTime);
                    if (!isValid)
                    {
                        Log.d("ManagerTimetable validation :", "User entered the same times for start and end time");
                        errorMessage.setText("Task Must be at least 30 minutes long.");
                        break;
                    }
                    errorMessage.setText("");

                    isValid = TimetableServices.checkUserIsSelected(spinnerUsersNames);
                    if (!isValid)
                    {
                        Log.d("ManagerTimetable validation :", "start and end time in wrong order");
                        errorMessage.setText("A user must be selected to assign task to.\n This may be a connection based issue.");
                        break;
                    }
                    errorMessage.setText("");

                    if (isValid) {

                        // Create task and write task to users timetable.
                        // task id will be generated by getting its paretns id and using PushKey() to increment it
                        TimetableTask task = new TimetableTask("1", editTextTaskName.getText().toString(),
                                spinnerUsersNames.getSelectedItem().toString(), date,
                                null, startTime, endTime, false, null);

                        TimetableServices.writeTaskTaskToUser(task, context);

                        // reset task name
                        editTextTaskName.setText("");
                        // set start time to the end time of previous task
                        spinnerStartTime.setSelection(spinnerEndTime.getSelectedItemPosition());
                        // set end time to next time slot
                        spinnerEndTime.setSelection(spinnerEndTime.getSelectedItemPosition()+1);
                        break;
                    }
                }
            }
        });
    }


}

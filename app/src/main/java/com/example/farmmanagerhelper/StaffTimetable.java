package com.example.farmmanagerhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.farmmanagerhelper.models.TimeSlot;
import com.example.farmmanagerhelper.models.TimetableTask;
import com.google.android.material.datepicker.MaterialDatePicker;
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
        //
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TimetableServices.getDatesTasksAndUpdateTimeTable(editTextDatePicker.getText().toString(), listView, context);

            }
        });

        // update the timetable with todays date that is set in the editText via onCreate.
        //
        TimetableServices.getDatesTasksAndUpdateTimeTable(editTextDatePicker.getText().toString(), listView, context);

        // Set listener for updates to users timetable
        //
        setListenerForNewTasks(editTextDatePicker,context,listView);


    }

    // Listener set on that users timetable document. If it is updated it will call getDatesTasksAndUpdateTimeTable
    // to refresh their timetable in realtime
    //
    private void setListenerForNewTasks(EditText editTextDatePicker, Context context, ListView listView) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

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
                        TimetableServices.getDatesTasksAndUpdateTimeTable(editTextDatePicker.getText().toString(), listView, context);
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

}
package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OrdersBoard extends AppCompatActivity {

    //get calander instance for datePicker
    //
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_board);

        Context context = this;

        // initialise UI components. buttonOpenManagerOrdersBoardOptions button has its visibility
        // set to GONE in layout XML.
        EditText editTextDatePicker = findViewById(R.id.ordersBoardDatePicker);
        ImageButton buttonForwardDate = findViewById(R.id.ordersBoardForwardDateButton);
        ImageButton buttonBackDate = findViewById(R.id.ordersBoardBackDateButton);
        Button buttonOpenManagerOrdersBoardOptions = findViewById(R.id.openManagerOrdersBoardOptions);

        // check if the user is manager and allow them to use the button to access manager only
        // settings
        OrdersBoardServices.makeOpenEditOrdersActivityVisibleForManagers(buttonOpenManagerOrdersBoardOptions);

        // SetDate to todays date
        //
        GeneralServices.SetDateToTodaysDate(editTextDatePicker,myCalendar);

        // set up listview
        //
        ListView listView = (ListView) findViewById(R.id.OrdersBoardListView);



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
            }
        };

        // set on click listener for calender popup.
        //
        editTextDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(OrdersBoard.this, date ,
                        myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Adjust the date forward and call update OrdersBoard
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

            }
        });

        buttonOpenManagerOrdersBoardOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OrdersBoard.this, "Open activity", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(OrdersBoard.this, ManagerOrderBoardSettings.class));


            }
        });

        OrdersBoardServices.testOrderboard(context,listView);

    }
}
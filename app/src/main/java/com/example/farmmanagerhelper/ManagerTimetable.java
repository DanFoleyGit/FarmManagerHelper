package com.example.farmmanagerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.farmmanagerhelper.models.TimeSlot;

import java.util.ArrayList;


// create list view
// list item with time slot, name, description
// create object of times containing time in half hour increments with name, description, start time end time, colour
// initilise the list of time slot objects with name, description start/endtime being null
// do db call to get times
// add them into object
// display list
// customize colours



public class ManagerTimetable extends AppCompatActivity {

    private static final String TAG = "ManagerTimetable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_timetable);

        ListView mListView = (ListView)findViewById(R.id.ManagerTimeTableListView);

        TimeSlot t0400 = new TimeSlot("04:00","test","test",null,null, false, null);
        TimeSlot t0430 = new TimeSlot("04:30","test",null,null,null, false, null);
        TimeSlot t0500 = new TimeSlot("05:00",null,null,null,null, false, null);
        TimeSlot t0530 = new TimeSlot("05:30",null,null,null,null, false, null);
        TimeSlot t0600 = new TimeSlot("06:00",null,null,null,null, false, null);

        ArrayList<TimeSlot> timetable = new ArrayList<>();

        timetable.add(t0400);
        timetable.add(t0430);
        timetable.add(t0500);
        timetable.add(t0530);
        timetable.add(t0600);

        // create custom list adapter
        TimetableListAdapter adapter = new TimetableListAdapter(this, R.layout.timetable_time_slot,timetable);
        mListView.setAdapter(adapter);


    }


}

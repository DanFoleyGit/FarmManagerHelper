package com.example.farmmanagerhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.farmmanagerhelper.models.TimeSlot;

import java.util.ArrayList;


public class TimetableListAdapter extends ArrayAdapter<TimeSlot> {

    private Context mContext;
    private int mResource;

    public TimetableListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<TimeSlot> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get all variables

        String timeSlotTime = getItem(position).getTimeSlotTime();
        String timeSlotName = getItem(position).getTimeSlotName();
        String timeSlotDescription = getItem(position).getTimeSlotDescription();
        String timeSlotStartTime = getItem(position).getTimeSlotStartTime();
        String timeSlotEndTime = getItem(position).getTimeSlotEndTime();
        boolean timeSlotCompleteStatus = getItem(position).isTimeSlotCompleteStatus();
        String timeSltColor = getItem(position).getTimeSlotColor();

        // create the tableSlot object for one row of the list
        TimeSlot timeslot = new TimeSlot(timeSlotTime,timeSlotName,timeSlotDescription,timeSlotStartTime,
                timeSlotEndTime, timeSlotCompleteStatus, timeSltColor);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        // ok to pass parent as there will only be 24 hours in half hour increment
        convertView = inflater.inflate(mResource, parent, false);

        // get the textview of the elements in the timeSLot layout
        TextView textViewTimeSlotTime = (TextView) convertView.findViewById(R.id.TextViewTimeSlotTime);
        TextView textViewTimeSlotName = (TextView) convertView.findViewById(R.id.TextViewTimeSlotTaskName);
        TextView textViewTimeSlotDescription = (TextView) convertView.findViewById(R.id.TextViewTimeSlotDescription);

        // set the text
        textViewTimeSlotTime.setText(timeSlotTime);
        textViewTimeSlotName.setText(timeSlotName);
        textViewTimeSlotDescription.setText(timeSlotDescription);


        return convertView;
    }
}

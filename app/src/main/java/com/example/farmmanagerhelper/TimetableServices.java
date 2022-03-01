package com.example.farmmanagerhelper;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.farmmanagerhelper.models.TimeSlot;
import com.example.farmmanagerhelper.models.TimetableTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimetableServices {

    public static ArrayList<TimeSlot> getTimeSlotTemplate()
    {

        TimeSlot t0400 = new TimeSlot("04:00",null,null,null,null, false, null);
        TimeSlot t0430 = new TimeSlot("04:30",null,null,null,null, false, null);
        TimeSlot t0500 = new TimeSlot("05:00",null,null,null,null, false, null);
        TimeSlot t0530 = new TimeSlot("05:30",null,null,null,null, false, null);
        TimeSlot t0600 = new TimeSlot("06:00",null,null,null,null, false, null);
        TimeSlot t0630 = new TimeSlot("06:30",null,null,null,null, false, null);

        TimeSlot t0700 = new TimeSlot("07:00",null,null,null,null, false, null);
        TimeSlot t0730 = new TimeSlot("07:30",null,null,null,null, false, null);
        TimeSlot t0800 = new TimeSlot("08:00",null,null,null,null, false, null);
        TimeSlot t0830 = new TimeSlot("08:30",null,null,null,null, false, null);
        TimeSlot t0900 = new TimeSlot("09:00",null,null,null,null, false, null);
        TimeSlot t0930 = new TimeSlot("09:30",null,null,null,null, false, null);

        TimeSlot t1000 = new TimeSlot("10:00",null,null,null,null, false, null);
        TimeSlot t1030 = new TimeSlot("10:30",null,null,null,null, false, null);
        TimeSlot t1100 = new TimeSlot("11:00",null,null,null,null, false, null);
        TimeSlot t1130 = new TimeSlot("11:30",null,null,null,null, false, null);
        TimeSlot t1200 = new TimeSlot("12:00",null,null,null,null, false, null);
        TimeSlot t1230 = new TimeSlot("12:30",null,null,null,null, false, null);

        TimeSlot t1300 = new TimeSlot("13:00",null,null,null,null, false, null);
        TimeSlot t1330 = new TimeSlot("13:30",null,null,null,null, false, null);
        TimeSlot t1400 = new TimeSlot("14:00",null,null,null,null, false, null);
        TimeSlot t1430 = new TimeSlot("14:30",null,null,null,null, false, null);
        TimeSlot t1500 = new TimeSlot("15:00",null,null,null,null, false, null);
        TimeSlot t1530 = new TimeSlot("15:30",null,null,null,null, false, null);

        TimeSlot t1600 = new TimeSlot("16:00",null,null,null,null, false, null);
        TimeSlot t1630 = new TimeSlot("16:30",null,null,null,null, false, null);
        TimeSlot t1700 = new TimeSlot("17:00",null,null,null,null, false, null);
        TimeSlot t1730 = new TimeSlot("17:30",null,null,null,null, false, null);
        TimeSlot t1800 = new TimeSlot("18:00",null,null,null,null, false, null);
        TimeSlot t1830 = new TimeSlot("18:30",null,null,null,null, false, null);

        TimeSlot t1900 = new TimeSlot("19:00",null,null,null,null, false, null);
        TimeSlot t1930 = new TimeSlot("19:30",null,null,null,null, false, null);
        TimeSlot t2000 = new TimeSlot("20:00",null,null,null,null, false, null);
        TimeSlot t2030 = new TimeSlot("20:30",null,null,null,null, false, null);
        TimeSlot t2100 = new TimeSlot("21:00",null,null,null,null, false, null);
        TimeSlot t2130 = new TimeSlot("21:30",null,null,null,null, false, null);

        TimeSlot t2200 = new TimeSlot("22:00",null,null,null,null, false, null);
        TimeSlot t2230 = new TimeSlot("22:30",null,null,null,null, false, null);
        TimeSlot t2300 = new TimeSlot("23:00",null,null,null,null, false, null);
        TimeSlot t2330 = new TimeSlot("23:30",null,null,null,null, false, null);
        TimeSlot t0000 = new TimeSlot("00:00",null,null,null,null, false, null);



        ArrayList<TimeSlot> timetable = new ArrayList<>();

        timetable.add(t0400);
        timetable.add(t0430);
        timetable.add(t0500);
        timetable.add(t0530);
        timetable.add(t0600);
        timetable.add(t0630);

        timetable.add(t0700);
        timetable.add(t0730);
        timetable.add(t0800);
        timetable.add(t0830);
        timetable.add(t0900);
        timetable.add(t0930);

        timetable.add(t1000);
        timetable.add(t1030);
        timetable.add(t1100);
        timetable.add(t1130);
        timetable.add(t1200);
        timetable.add(t1230);

        timetable.add(t1300);
        timetable.add(t1330);
        timetable.add(t1400);
        timetable.add(t1430);
        timetable.add(t1500);
        timetable.add(t1530);

        timetable.add(t1600);
        timetable.add(t1630);
        timetable.add(t1700);
        timetable.add(t1730);
        timetable.add(t1800);
        timetable.add(t1830);

        timetable.add(t1900);
        timetable.add(t1930);
        timetable.add(t1900);
        timetable.add(t1930);
        timetable.add(t2000);
        timetable.add(t2030);

        timetable.add(t2100);
        timetable.add(t2130);
        timetable.add(t2200);
        timetable.add(t2230);
        timetable.add(t2300);
        timetable.add(t2330);
        timetable.add(t0000);

        return timetable;
    }

    // takes a time in 04:00 format and returns it as an Integer 0400
    public static int convertStringTimeInputToInt(String time) {
        String test = time.substring(0, 2);
        String test2 = time.substring(4, 5);
        String newTime = time.substring(0, 2) + time.substring(3,5);
        Log.d("test", test);
        Log.d("test2", test2);
        Log.d("convertStringTimeInputToInt", newTime);
        return Integer.parseInt(newTime);
    }

    // takes the start and end time and returns how many time slot that task needs in the timetable.
    // the function must convert half hours as 0830 to be 0850 so that it can evenly divide by 50 to get half.
    // as the functions only purpose is to return the number of slots, it doesnt need to know the exact times.

    public static int findLengthOfTask(TimetableTask task) {
        int startTimeInt = TimetableServices.convertStringTimeInputToInt(task.getTaskStartTime());
        int endTimeInt = TimetableServices.convertStringTimeInputToInt(task.getTaskEndTime());

        // if the time is given as a half hour, add 20 to it to allow it to be divided by 50
        if((endTimeInt % 50) != 0)
        {
            endTimeInt = endTimeInt +20;
        }

        if((startTimeInt % 50) != 0)
        {
            startTimeInt = startTimeInt +20;
        }

        int numberOfSlotsNeeded = (endTimeInt - startTimeInt ) / 50;
        Log.d("StaffTimetable","endtimeInt: " + endTimeInt + " , startTimeInt: " + startTimeInt);

        Log.d("StaffTimetable","number of slots needed:" + numberOfSlotsNeeded);
        return numberOfSlotsNeeded;
    }

    public static boolean checkValuesAreNotNull(String date, String taskName) {
        if(TextUtils.isEmpty(date) || TextUtils.isEmpty(taskName))
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    // updates the EditText after the user has selected the date they want.
    public static void updateLabel(EditText datePicker, Calendar myCalandar) {
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
        datePicker.setText(dateFormat.format(myCalandar.getTime()));
    }

    public static void SetDateToTodaysDate(EditText editTextDatePicker, Calendar myCalendar) {
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
        String todaysDate = dateFormat.format(new Date());
        editTextDatePicker.setText(todaysDate);
    }

    public static boolean checkStartAndEndTimesAreChronological(int intStartTime, int intEndTime) {
        if(intStartTime > intEndTime)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean checkUserIsSelected(Spinner spinnerUsersNames) {
        if(    spinnerUsersNames.getSelectedItem() == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean checkTimesAreNotTheSame(int intStartTime, int intEndTime) {
        if(intStartTime == intEndTime)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

}

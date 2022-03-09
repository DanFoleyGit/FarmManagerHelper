package com.example.farmmanagerhelper;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GeneralServices {

    // updates the EditText after the user has selected the date they want.
    //
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

    public static boolean checkEditTextIsNotNull(String string) {
        if(TextUtils.isEmpty(string) )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    // This function checks if the spinner is blank and as the database result returns no results
    public static Boolean checkIfSpinnerIsNull(Spinner spinner) {
        if(spinner.getSelectedItem() == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


}

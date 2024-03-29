package com.eduattendanceapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog.*;
import android.app.DatePickerDialog;
import android.app.Dialog;


public class AttendanceDatePickerFragment extends DialogFragment implements  DatePickerDialog.OnDateSetListener{
    String formattedDateString;
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, dayOfMonth);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		formattedDateString = sdf.format(c.getTime());
		Date formattedDate;
		try {
			formattedDate = sdf.parse(formattedDateString);
			((HomeActivity)getActivity()).setDateFromAttendancePicker(formattedDate);
			Log.d("Selected date", formattedDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}	
	

	 @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the current date as the default date in the picker
	        final Calendar c = Calendar.getInstance();
	        int year = c.get(Calendar.YEAR);
	        int month = c.get(Calendar.MONTH);
	        int day = c.get(Calendar.DAY_OF_MONTH);

	        // Create a new instance of DatePickerDialog and return it
	        return new DatePickerDialog(getActivity(), this, year, month, day);
	    }
	 
	 

}

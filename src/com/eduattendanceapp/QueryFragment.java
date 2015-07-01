package com.eduattendanceapp;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class QueryFragment extends Fragment{

	private DatePicker datePicker;
	private DBController dbcontroller;
	private ListView myList;
	private boolean morningAttendance = true;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view =  inflater.inflate(R.layout.query_fragment,container, false);
		//datePicker = (DatePicker)view.findViewById(R.id.datePickerAttendance);
		dbcontroller = new DBController(view.getContext());
		myList = (ListView)view.findViewById(android.R.id.list);
		
		
		
		
		
		return view;
		
	}
	public void displayAttendance(View v){
		//datePicker.setVisibility(View.GONE);
		//String date  = getDateFromPicker();
	
		myList.setAdapter(null);
		
		if(!((HomeActivity)getActivity()).date.isEmpty()){
			if(dbcontroller.checkForHoliday(((HomeActivity)getActivity()).date)){
				Toast.makeText(getActivity(), "Selected date was a holiday", Toast.LENGTH_SHORT).show();
			}
			else{
				ArrayList<HashMap<String, String>> attendanceList =  dbcontroller.getAttendanceForDate(((HomeActivity)getActivity()).date,morningAttendance);
				if(attendanceList.size()!=0){
					Log.d("disp-attendance", attendanceList.toString());
					Log.d("disp-attendance", "attendance display");
					
					ListAdapter adapter = new SimpleAdapter( getActivity(),attendanceList, R.layout.view_user_entry, new String[] { "rollNo","studentName"}, new int[] {R.id.rollNo, R.id.studentName});
					myList.setAdapter(adapter);
				}
				else{
					Toast.makeText(getActivity(), "No attendance recorded for this date", Toast.LENGTH_SHORT).show();
				}
			}
		}
		else{
			Toast.makeText(getActivity(), "Please select date.", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void selectDateForAttendance(View v){
		DialogFragment newFragment = new AttendanceDatePickerFragment();
	    newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
	}
	
	public void selectTypeForAttendance(View v){
		boolean on = ((ToggleButton)v).isChecked();
		if(on){
			morningAttendance = false;
			Log.d("toggleClick", "morning attendance");
		}
		else{
			morningAttendance = true;
			Log.d("toggleClick", "Noon Attendance");
		}
	}
}

package com.eduattendanceapp;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class QueryFragment extends Fragment{

	private DatePicker datePicker;
	private DBController dbcontroller;
	private ListView myList;
	private SharedPreferences sharedPref;
	private boolean morningAttendance = true;
	private TextView dateDisplayText;
	private String currentAttendanceDate;
	private DateFormat displayFormat;
	private String lastDoneDate;
	private View view;
	private LinearLayout datePickerContainer;
	private LinearLayout summaryLayout;
	private RelativeLayout warningSignContainer;
	private TextView warningText;
	private RadioGroup attendanceTypeSelector;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		view =  inflater.inflate(R.layout.query_fragment,container, false);
		//datePicker = (DatePicker)view.findViewById(R.id.datePickerAttendance);
		dbcontroller = new DBController(view.getContext());
		myList = (ListView)view.findViewById(R.id.presentList);
		summaryLayout = (LinearLayout)view.findViewById(R.id.summaryLayout);
		warningSignContainer = (RelativeLayout)view.findViewById(R.id.warningContainer);
		warningText = (TextView)view.findViewById(R.id.warningText);
		datePickerContainer = (LinearLayout)view.findViewById(R.id.datePickerContainer);
		dateDisplayText = (TextView)view.findViewById(R.id.dateDisplayText);
		sharedPref= getActivity().getApplicationContext()
				.getSharedPreferences(getActivity().getString(R.string.preference_registration), Context.MODE_PRIVATE);
		lastDoneDate = sharedPref.getString("morningDate", null);
		attendanceTypeSelector = (RadioGroup)view.findViewById(R.id.attendanceTypeSelector);
		//Log.d("lastDoneDate", lastDoneDate);
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		displayFormat = new SimpleDateFormat("dd-MM-yyyy");
		if(lastDoneDate==null || lastDoneDate.isEmpty()){
			attendanceNotAvailable(false);
		}
		else{
			try {
				Date lastAttendanceRecordedDate = format.parse(lastDoneDate);
				dateDisplayText.setText(displayFormat.format(lastAttendanceRecordedDate));
				displayAttendance(lastDoneDate);
				
				attendanceTypeSelector.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					String currentDate;
					@Override
					public void onCheckedChanged(RadioGroup arg0, int checkedId) {
						try {
							Date displayedDate = displayFormat.parse((String) 
									((TextView)view.findViewById(R.id.dateDisplayText)).getText());
							currentDate = format.format(displayedDate);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						switch(checkedId){
						case(R.id.presentButton):
							displayAttendance(currentDate);
						case(R.id.absentButton):
							Log.d("absent-d", "absentCalled");
							displayAbsentList(currentDate);
						}
						
					}
				});
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return view;
		
	}
	
	
	private void displayAbsentList(String date){
		Log.d("Absentcalled", "AbsentList");
		if(date!=null && !date.isEmpty()){
			if(dbcontroller.checkForHoliday(date)){
				Toast.makeText(getActivity(), "Selected date was a hoiday", Toast.LENGTH_SHORT).show();
			}
			else{
				attendanceAvailable();
				ArrayList<HashMap<String,String>> attendanceList = 
						dbcontroller.getAbsentStudentsForDate(date);
				int presentNumber = dbcontroller.getPresentStudentsCountForDate(date);
				Log.d("presentNumber", presentNumber+"");
				int allStudentsNumber = dbcontroller.getAllStudentsForDate(date);
				if(attendanceList.size()!=0){
					//Log.d("disp-attendance", attendanceList.toString());
					//Log.d("disp-attendance", "attendance display");
					((TextView)view.findViewById(R.id.presentStudentsNumber)).setText(presentNumber+"");
					((TextView)view.findViewById(R.id.absentStudentsNumber)).setText((allStudentsNumber
							-presentNumber)+"");
					
					ListAdapter adapter = new SimpleAdapter( getActivity(),attendanceList,
							R.layout.view_user_entry, new String[] { "rollNumber","studentName"}, 
							new int[] {R.id.rollNo, R.id.studentName});
					myList.setAdapter(adapter);
				}
				else{
					attendanceNotAvailable(false);
				}
			}
		}
	}
	public void displayAttendance(String date){
		Log.d("displayPresentAttendanceList", "present "+date);
		myList.setAdapter(null);
		if(date!=null && !date.isEmpty()){
			if(dbcontroller.checkForHoliday(date)){
				Toast.makeText(getActivity(), "Selected date was a hoiday", Toast.LENGTH_SHORT).show();
			}
			else{
				attendanceAvailable();
				ArrayList<HashMap<String,String>> attendanceList = 
						dbcontroller.getAttendanceForDate(date, morningAttendance);
				int presentNumber = dbcontroller.getPresentStudentsCountForDate(date);
				Log.d("presentNumber", presentNumber+"");
				int allStudentsNumber = dbcontroller.getAllStudentsForDate(date);
				if(attendanceList.size()!=0){
					//Log.d("disp-attendance", attendanceList.toString());
					//Log.d("disp-attendance", "attendance display");
					((TextView)view.findViewById(R.id.presentStudentsNumber)).setText(presentNumber+"");
					((TextView)view.findViewById(R.id.absentStudentsNumber)).setText((allStudentsNumber
							-presentNumber)+"");
					
					ListAdapter adapter = new SimpleAdapter( getActivity(),attendanceList,
							R.layout.view_user_entry, new String[] { "rollNumber","studentName"}, 
							new int[] {R.id.rollNo, R.id.studentName});
					myList.setAdapter(adapter);
				}
				else{
					attendanceNotAvailable(false);
				}
			}
		}
		
	}
	
		
	public void selectPreviousDate(View v){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy");
		String currentDisplayedDate = (String) ((TextView)view.findViewById(R.id.dateDisplayText)).getText();
		try {
			if(currentDisplayedDate!=null && !currentDisplayedDate.isEmpty()){
				Date currentDate = displayFormat.parse(currentDisplayedDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentDate);
				cal.add(Calendar.DAY_OF_YEAR, -1);
				Date oneDayBefore = cal.getTime();
				dateDisplayText.setText(displayFormat.format(oneDayBefore));
				displayAttendance(format.format(oneDayBefore));
			}
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void selectNextDate(View v){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy");
		String currentDisplayedDate = (String) ((TextView)view.findViewById(R.id.dateDisplayText)).getText();
		try {
			if(currentDisplayedDate!=null && !currentDisplayedDate.isEmpty()){
				Date currentDate = displayFormat.parse(currentDisplayedDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentDate);
				cal.add(Calendar.DAY_OF_YEAR, +1);
				Date oneDayAfter = cal.getTime();
				dateDisplayText.setText(displayFormat.format(oneDayAfter));
				displayAttendance(format.format(oneDayAfter));
			}
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void selectDateForAttendance(View v){
		DialogFragment newFragment = new AttendanceDatePickerFragment();
		newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
	    
	}
	public void displayAttendanceFromCalendar(Date date){
		Date dateForAttendanceDisplay =date;
		DateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat databaseFormat  = new SimpleDateFormat("yyyy-MM-dd");
	    
	    if(dateForAttendanceDisplay!=null){
	    	dateDisplayText.setText(displayFormat.format(dateForAttendanceDisplay));
	    	displayAttendance(databaseFormat.format(dateForAttendanceDisplay));
	    }
	}
	private void attendanceAvailable(){
		warningSignContainer.setVisibility(View.VISIBLE);
		myList.setVisibility(View.VISIBLE);
		summaryLayout.setVisibility(View.VISIBLE);
	}
	
	private void attendanceNotAvailable(boolean ifHoliday){
		if(ifHoliday){
			
		}
		else{
			warningText.setText("Attendance not available for this day");
		}
		myList.setVisibility(View.GONE);
		summaryLayout.setVisibility(View.GONE);
		warningSignContainer.setVisibility(View.VISIBLE);
	}
}

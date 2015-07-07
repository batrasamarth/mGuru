package com.eduattendanceapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

public class HomeFragment extends Fragment {
	
	TextView schoolDescription;
	TextView classDescription;
	TextView dateTimeView;
	TextView numberStudents;
	
	SharedPreferences sharedPref;
	DBController dbController;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		View currentView= inflater.inflate(R.layout.home_fragment, container, false);
		
		schoolDescription = (TextView)currentView.findViewById(R.id.schoolDesc);
		classDescription = (TextView)currentView.findViewById(R.id.classDesc);
		dateTimeView = (TextView)currentView.findViewById(R.id.dateTimeView);
		numberStudents  = (TextView)currentView.findViewById(R.id.numberStudents);
		
		sharedPref= getActivity().getApplicationContext().getSharedPreferences(getActivity().getString(R.string.preference_registration), Context.MODE_PRIVATE);
		String schoolName=sharedPref.getString("schoolId","");
		String className=sharedPref.getString("classId", "");
		schoolDescription.setText("School:"+schoolName);
		classDescription.setText("Class:"+className);
		
		SimpleDateFormat df =  new SimpleDateFormat("EEE, d MMM yyyy");
		String date = df.format(Calendar.getInstance().getTime());
		dateTimeView.setText(date);
		
		dbController = new DBController(getActivity().getApplicationContext());
		int numStudents =0;
		try{
			numStudents =dbController.getStudentCount();
			Log.d("Direction", "student count retreived");
			numberStudents.setText(numStudents+" students registered");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			numberStudents.setText(numStudents+" students registered");
			
			
		}
		
				
				
		return currentView;
	}

}
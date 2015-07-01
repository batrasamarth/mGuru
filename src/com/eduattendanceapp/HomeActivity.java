package com.eduattendanceapp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

public class HomeActivity extends FragmentActivity{
	
	private static final int HOME = 0;
	private static final int STUDENTS = 1;
	private static final int ATTENDANCE =2;
	private static final int QUERY =3;
	private static final int FRAGMENT_COUNT = QUERY+1;
	
	
	public boolean[] attendanceBooleanCheck;
	public ArrayList<Integer> attendancePresentList;
	public ArrayList<Integer> totalAttendanceList;
	private Fragment[] fragments = new Fragment[4];
	public String date="";
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		FragmentManager fm = getSupportFragmentManager();
        fragments[STUDENTS] = (StudentsFragment) fm.findFragmentById(R.id.StudentsFragment);
        fragments[HOME] = (HomeFragment)fm.findFragmentById(R.id.HomeFragment);
        fragments[ATTENDANCE] = (AttendanceFragment)fm.findFragmentById(R.id.AttendanceFragment);
        fragments[QUERY] = (QueryFragment)fm.findFragmentById(R.id.QueryFragment);
        FragmentTransaction transaction = fm.beginTransaction();
        for(int i=0; i<FRAGMENT_COUNT; i++){
        	transaction.hide(fragments[i]);
        }
        transaction.show(fragments[HOME]);
        transaction.commit();
        /*
        fragments[SPLASH] = splashFragment;
        fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
        fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);
        fragments[FRIENDS] = fm.findFragmentById(R.id.FriendsFragment);
		*/
        
        RadioButton radioButton;
        radioButton = (RadioButton)findViewById(R.id.btnHome);
        radioButton.setChecked(true);
        radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
        radioButton = (RadioButton)findViewById(R.id.btnStudents);
        radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
        radioButton = (RadioButton)findViewById(R.id.btnAttendance);
        radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
        radioButton = (RadioButton)findViewById(R.id.btnQuery);
        radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
        
        
	}
	
	private CompoundButton.OnCheckedChangeListener btnNavBarOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked){
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				for(int i=0; i<FRAGMENT_COUNT; i++){
					ft.hide(fragments[i]);
					ft.addToBackStack(null);
				}
				if(buttonView.getText().equals("Home")){
					
					ft.show(fragments[HOME]);
				}
				if(buttonView.getText().equals("Students")){
					
					ft.show(fragments[STUDENTS]);
				}
				if(buttonView.getText().equals("Attendance")){
					
					ft.show(fragments[ATTENDANCE]);
				}
				if(buttonView.getText().equals("Query")){
					
					ft.show(fragments[QUERY]);
				}
				ft.commit();
			}
			
		}
	};
	//Direct the method addUser as mentioned in students fragment
	public void addUser(View v){
		((StudentsFragment) fragments[STUDENTS]).addUser(v);
	}
	public void syncAttendance(View v){
		((AttendanceFragment)fragments[ATTENDANCE]).syncAttendance(v);
	}
	public void syncStudents(View v){
		((StudentsFragment) fragments[STUDENTS]).syncStudents(v);
	}
	public void onToggleClick(View v){
		((AttendanceFragment)fragments[ATTENDANCE]).onToggleClick(v);
	}
	public void submitAttendance(View v){
		((AttendanceFragment) fragments[ATTENDANCE]).submitAttendance(v);
	}
	public void displayAttendance(View v){
		((QueryFragment) fragments[QUERY]).displayAttendance(v);
	}
	public void selectDateForAttendance(View v){
		((QueryFragment) fragments[QUERY]).selectDateForAttendance(v);
	}
	
	public void selectTypeForAttendance(View v){
		((QueryFragment) fragments[QUERY]).selectTypeForAttendance(v);
	}
	public void declareHoliday(View v){
		((AttendanceFragment)fragments[ATTENDANCE]).declareHoliday(v);
	}
}

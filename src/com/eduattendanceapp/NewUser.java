package com.eduattendanceapp;

import java.util.HashMap;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class NewUser extends FragmentActivity  {
	
	EditText rollNumber;
	EditText firstName;
	EditText lastName;
	EditText admissionNumber;
	RadioGroup gender;
	String dateOfBirth;
	
	public boolean dateSet=false;
	
	
	DBController controller = new DBController(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_user);
		rollNumber = (EditText) findViewById(R.id.rollNumber);
		firstName = (EditText) findViewById(R.id.firstName);
		lastName = (EditText) findViewById(R.id.lastName);
		gender = (RadioGroup)findViewById(R.id.genderBox);
		admissionNumber = (EditText) findViewById(R.id.admissionNumber);
	}
	
	/**
	 * Called when Save button is clicked 
	 * @param view
	 */
	public void addNewUser(View view) {
		HashMap<String, String> queryValues = new HashMap<String, String>();
		
		if(rollNumber.getText().toString()!=null && rollNumber.getText().toString().trim().length() != 0){
			if(firstName.getText().toString()!=null && firstName.getText().toString().trim().length()!=0){
				if(lastName.getText().toString()!=null && lastName.getText().toString().trim().length()!=0){
					if(!(gender.getCheckedRadioButtonId()==-1)){
						if(dateSet){
							if(admissionNumber.getText().toString()!=null && admissionNumber.getText().toString().trim().length()!=0){
								
								queryValues.put("rollNo", rollNumber.getText().toString().trim());
								queryValues.put("firstName", firstName.getText().toString().trim());
								queryValues.put("lastName",lastName.getText().toString().trim());
								queryValues.put("dateOfBirth", ((TextView)findViewById(R.id.dateDisplayText)).getText().toString().trim().substring(9));
								queryValues.put("admissionNumber", admissionNumber.getText().toString().trim());
								
								int checkedId = gender.getCheckedRadioButtonId();
								View radiobutton = gender.findViewById(checkedId);
								if(((RadioButton)radiobutton).getText().equals("Male")){
									queryValues.put("gender", "M");
								}
								else{
									queryValues.put("gender","F");
								}
								controller.insertUser(queryValues);
								this.callHomeActivity(view);
							}
							else{
								Toast.makeText(getApplicationContext(), "Admission Number Field Empty",Toast.LENGTH_SHORT).show();
							}
						}
						else{
							Toast.makeText(getApplicationContext(), "Set Date before proceeding",Toast.LENGTH_SHORT).show();
						}
					}
					else{
						Toast.makeText(getApplicationContext(), "Please specify student's gender.",Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(getApplicationContext(), "Last Name field empty",Toast.LENGTH_SHORT).show();
				}
			}
			else{
				Toast.makeText(getApplicationContext(), "First Name field empty",Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Toast.makeText(getApplicationContext(), "Roll Number field empty", Toast.LENGTH_SHORT).show();
		}
		
			
		
	}
	
    /**
     * Navigate to Home Screen 
     * @param view
     */
	public void callHomeActivity(View view) {
		Intent objIntent = new Intent(getApplicationContext(),
				MainActivity.class);
		startActivity(objIntent);
	}

	/**
	 * Called when Cancel button is clicked
	 * @param view
	 */
	public void cancelAddUser(View view) {
		this.callHomeActivity(view);
	}
	/**
	 * Method to show the date picker dialog on button click 
	 * @param v
	 */
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new datePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "datePicker");
	}
	 
}

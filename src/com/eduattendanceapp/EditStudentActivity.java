package com.eduattendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EditStudentActivity extends FragmentActivity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_student);
		Intent iin = getIntent();
		Bundle b = iin.getExtras();
		if(b!=null){
			String rollN = (String)b.get("rollNo");
		}
	}
}

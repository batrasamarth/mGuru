package com.eduattendanceapp;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends Activity {
	//DB Class to perform DB related operations
	SharedPreferences sharedPref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		TextView schoolLabel = (TextView)findViewById(R.id.schoolLabel);
		schoolLabel.setText(this.getString(R.string.schoolID));
		TextView classLabel = (TextView)findViewById(R.id.classLabel);
		classLabel.setText(this.getString(R.string.classID));
		
		
		
		TextView networkInfo = (TextView)findViewById(R.id.networkInfo);
		networkInfo.setText("Please ensure the internet is connected!");
		
		Context context = getApplicationContext();
		sharedPref = context.getSharedPreferences(context.getString(R.string.preference_registration), Context.MODE_PRIVATE);
		checkForLoginScreen();
		
	}
	public void connectToAppEngine(View view){
		
		EditText schoolInput = (EditText)findViewById(R.id.schoolInput);
		EditText classInput = (EditText)findViewById(R.id.classInput);
		if(schoolInput.getText().toString().isEmpty()&&classInput.getText().toString().isEmpty()){
			Toast.makeText(getApplicationContext(), "Please fill appropriate values for class and school", Toast.LENGTH_LONG).show();
		}
		else{
			SharedPreferences.Editor  editor = sharedPref.edit();
			editor.putString("schoolId", schoolInput.getText().toString());
			editor.putString("classId", classInput.getText().toString());
			editor.commit();
			Toast.makeText(getApplicationContext(), "Registration done with school id"+schoolInput.getText()+" class id "+
			classInput.getText(), Toast.LENGTH_LONG).show();
			checkForLoginScreen();
		}
		
	}
	
	private void checkForLoginScreen(){
		if(!(sharedPref.getString("schoolId", "").isEmpty()&&sharedPref.getString("classId", "").isEmpty())){
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
		}
	}
	
}

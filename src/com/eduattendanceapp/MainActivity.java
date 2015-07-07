package com.eduattendanceapp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends Activity {
	//DB Class to perform DB related operations
	SharedPreferences sharedPref;
	private ProgressDialog addPeopleDialog;
	private DBController controller;
	private SharedPreferences.Editor  editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		controller  = new DBController(this);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		TextView schoolLabel = (TextView)findViewById(R.id.schoolLabel);
		schoolLabel.setText(this.getString(R.string.userNamePrompt));
		TextView classLabel = (TextView)findViewById(R.id.classLabel);
		classLabel.setText(this.getString(R.string.passwordPrompt));
		
		
		
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
			Toast.makeText(getApplicationContext(), "Please fill appropriate userName and password", Toast.LENGTH_LONG).show();
		}
		else{
			editor = sharedPref.edit();
			String userName = schoolInput.getText().toString();
			String password = classInput.getText().toString();
			//editor.putString("schoolId", schoolInput.getText().toString());
			//editor.putString("classId", classInput.getText().toString());
			RequestQueue queue = Volley.newRequestQueue(this);
			String url = "http://1-dot-attendance-edumust.appspot.com/loginusingidandpassword?id="+
					userName.toLowerCase()+"&password="+password;
			StringRequest stringRequest = new StringRequest(Request.Method.GET, url, 
					new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							try {
								JSONObject jsonObject = new JSONObject(response);
								JSONArray jsonArray = jsonObject.getJSONArray("students");
								String schoolName = jsonObject.getString("schoolId");
								String schoolId = jsonObject.getString("schoolId");
								String className = jsonObject.getString("className");
								editor.putString("schoolId", schoolId);
								editor.putString("classId", className);
								editor.commit();
								Log.d("Inserting classId and schoolId", schoolId+" : "+className);
								for(int i=0; i<jsonArray.length(); i++){
									HashMap<String,String> userValues = new HashMap<String,String>();
									JSONObject tempObj = jsonArray.getJSONObject(i);
									userValues.put("studentKey", tempObj.getString("key"));
									userValues.put("givenName", tempObj.getString("givenName"));
									userValues.put("lastName", tempObj.getString("lastName"));
									userValues.put("rollNumber", tempObj.getString("rollNumber"));
									userValues.put("gender", tempObj.getString("gender"));
									//userValues.put("grNumber", tempObj.getString("grNumber"));
									//userValues.put("motherName", tempObj.getString("motherName"));
									Log.d("adding-student",tempObj.getString("key")+
											" "+tempObj.getString("givenName")+
											" "+tempObj.getString("lastName")+
											" "+tempObj.getString("rollNumber")+
											" "+ tempObj.getString("gender"));
									controller.insertUser(userValues);
								Log.d("Response", Integer.toString(jsonArray.length()) + " "+ schoolName );
							     checkForLoginScreen();
								}
							}
							
								catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							
						}
					
					}, new Response.ErrorListener() {
							@Override
						    public void onErrorResponse(VolleyError error) {
						        Log.d("error-reponse","That didn't work!");
						    }
					}
			);
			queue.add(stringRequest);
			
			editor.commit();
			
		}
		
	}
	
	private void checkForLoginScreen(){
		if(!(sharedPref.getString("schoolId", "").isEmpty()&&sharedPref.getString("classId", "").isEmpty())){
			Log.d("sign in done", "sign in");
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
		}
		else{
			Log.d("not signed in", "no sign in proper");
		}
	}
	
	private class PopulateStudentList extends AsyncTask<String, Integer, Long> {
	     protected Long doInBackground(String... strings) {
	         //int count = urls.length;
	    	 
	    	 /*
	    	 
	    	 long returnVal = 0;
	    	 String jsonString = strings[0];
	         JSONObject jsonObject;
	         JSONArray studentsArrayJs = null;
	         try {
				jsonObject = new JSONObject(jsonString);
				Log.d("json-caught", jsonObject.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         */
	         /*
			try {
				
				
				
				String schoolId = jsonObject.getString("schoolId");
				String className = jsonObject.getString("className");
				editor.putString("schoolId", schoolId);
				editor.putString("className", className);
				Log.d("Inserting classId and schoolId", schoolId+" : "+className);
				studentsArrayJs = jsonObject.getJSONArray("students");
				for(int i=0; i<studentsArrayJs.length(); i++){
					HashMap<String,String> userValues = new HashMap<String,String>();
					JSONObject tempObj = studentsArrayJs.getJSONObject(i);
					userValues.put("studentKey", tempObj.getString("studentKey"));
					userValues.put("givenName", tempObj.getString("givenName"));
					userValues.put("lastName", tempObj.getString("lastName"));
					userValues.put("rollNumber", tempObj.getString("rollNumber"));
					userValues.put("gender", tempObj.getString("gender"));
					userValues.put("grNumber", tempObj.getString("grNumber"));
					userValues.put("motherName", tempObj.getString("motherName"));
					Log.d("adding-student",tempObj.getString("studentKey")+
							" "+tempObj.getString("givenName")+
							" "+tempObj.getString("lastName")+
							" "+tempObj.getString("rollNumber")+
							" "+ tempObj.getString("gender")+
							" "+  tempObj.getString("grNumber")+
							" "+ tempObj.getString("motherName"));
					controller.insertUser(userValues);
					
				
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "Invalid login", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			
			
	         Log.d("Array JS",studentsArrayJs.toString());
	         
	         returnVal=1;
	         return returnVal;
	     	*/
	    	 return (long) 1;
	     }
	     
	     protected void onProgressUpdate(Integer... progress) {
	         
	     }

	     protected void onPostExecute(Long result) {
	        // showDialog("Downloaded " + result + " bytes");
	     }
	 }
	
	
}

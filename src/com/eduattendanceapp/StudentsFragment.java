package com.eduattendanceapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class StudentsFragment extends Fragment{
	
	DBController controller;
	
	//Progress Dialog Object
	ProgressDialog prgDialog;
	View currentView;
	ListView myList;
	SharedPreferences sharedPref;
	String schoolName;
	String className;
	Button syncButton;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		currentView =  inflater.inflate(R.layout.login_layout, container,false);
		controller = new DBController(currentView.getContext());
		sharedPref= getActivity().getApplicationContext().getSharedPreferences(getActivity().getString(R.string.preference_registration), Context.MODE_PRIVATE);
		schoolName=sharedPref.getString("schoolId","");
		className=sharedPref.getString("classId", "");
		((TextView)currentView.findViewById(R.id.textView1)).setText("Class: "+className);
		
		ArrayList<HashMap<String, String>> userList =  controller.getAllUsers();
		if(userList.size()!=0){
			//Set the User Array list in ListView
			ListAdapter adapter = new SimpleAdapter( getActivity(),userList, R.layout.view_user_entry, new String[] { "rollNumber","studentName"}, new int[] {R.id.rollNo, R.id.studentName});
			myList = (ListView)currentView.findViewById(android.R.id.list);
			myList.setAdapter(adapter);
			myList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Toast.makeText(getActivity(), ((TextView)arg1.findViewById(R.id.studentName)).getText(), Toast.LENGTH_SHORT).show();
					/*
					Intent editStudent = new Intent(currentView.getContext(), EditStudentActivity.class);
					editStudent.putExtra("rollNo", ((TextView)arg1.findViewById(R.id.rollNo)).getText() );
					startActivity(editStudent);
					*/
				}
			});
			//Display Sync status of SQLite DB
			Toast.makeText(getActivity(), controller.getSyncStatus(), Toast.LENGTH_LONG).show();
			syncButton = (Button)currentView.findViewById(R.id.syncButton);
			if(controller.studentSyncStatus()){
				syncButton.setEnabled(false);
				syncButton.setTextColor(Color.GREEN);
			}
			else{
				syncButton.setEnabled(true);
				syncButton.setTextColor(Color.RED);
			}
		}
		//Initialize Progress Dialog properties
		
		
		
		return currentView;
		
	}
	
		
	public void syncStudents(View v){
		ArrayList<HashMap<String, String>> studentsList = controller.composeNonUpdateStudentList();
		if(studentsList==null || studentsList.size()==0){
			return;
		}
		//new EndpointsTask(studentsList, schoolName, className).execute(getActivity().getApplicationContext());
		syncButton.setEnabled(false);
		syncButton.setTextColor(Color.GREEN);
	}
	/*
	public class EndpointsTask extends AsyncTask<Context, Integer, String>{
		private ArrayList<HashMap<String, String>> students;
		private String school;
		private String className;
		ProgressDialog pDialog;
		public EndpointsTask(ArrayList<HashMap<String, String>> stud,String sch, String cls){
			students = stud;
			school = sch;
			className = cls;
		}
		
		@Override
		  protected void onPreExecute() 
		  {
		    super.onPreExecute();
		    pDialog = new ProgressDialog(getActivity());
		    pDialog.setMessage("Working ...");
		    pDialog.setIndeterminate(false);
		    pDialog.setCancelable(false);
		    pDialog.show();
		}

		
		@Override
		protected String doInBackground(Context... params) {
			Studentendpoint.Builder endpointBuilder = new Studentendpoint.Builder(AndroidHttp.newCompatibleTransport(),
		              new JacksonFactory(),
		              new HttpRequestInitializer() {
		              public void initialize(HttpRequest httpRequest) { }
		              });
			Studentendpoint endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
			try{
				for(HashMap<String, String> temp :students){
					Student stu =new Student();
					stu.setStudentId(school+className+temp.get("rollNo"));
					stu.setRollNumber(Integer.parseInt(temp.get("rollNo")));
					stu.setFirstName(temp.get("firstName"));
					stu.setLastName(temp.get("lastName"));
					stu.setGender(temp.get("gender"));
					stu.setDob(temp.get("dateOfBirth"));
					stu.setSchoolName(school);
					stu.setClassName(className);
					stu.setAdmissionNumber(Integer.parseInt(temp.get("admissionNumber")));
					Student result = endpoint.insertStudent(stu).execute();
					controller.updateSyncStatus(temp.get("rollNo").toString(),"yes");
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
			
			return "done"+" ";
		}
		protected void onPostExecute(String result) 
		{

		   //Do something with result and close the progress dialog

		   pDialog.dismiss();

		}
		
	}
	*/
}

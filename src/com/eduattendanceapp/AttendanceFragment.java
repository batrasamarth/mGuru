package com.eduattendanceapp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.eduattendanceapp.attendanceobjectendpoint.Attendanceobjectendpoint;
import com.eduattendanceapp.attendanceobjectendpoint.model.AttendanceObject;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;


public class AttendanceFragment extends Fragment{
	private final static int MORNING=0;
	private final static int NOON=1;
	
	DBController controller;
	View currentView;
	ListView myList;
	SharedPreferences sharedPref;
	ArrayList<HashMap<String, String>> userList;
	SimpleDateFormat df;
	String date;
	TextView syncStatusText;
	String schoolName;
	String className;
	RelativeLayout listContainer;
	RelativeLayout warningContainer;
	boolean morningAttendance = true;
	boolean attendanceEnabled[] = new boolean[]{true,true};
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState){
		sharedPref= getActivity().getApplicationContext()
				.getSharedPreferences(getActivity().getString(R.string.preference_registration), Context.MODE_PRIVATE);
		schoolName=sharedPref.getString("schoolId","");
		className=sharedPref.getString("classId", "");
		currentView =  inflater.inflate(R.layout.attendance_fragment, container, false);
		
		listContainer = (RelativeLayout)currentView.findViewById(R.id.relativeLayout2);
		warningContainer = (RelativeLayout)currentView.findViewById(R.id.attendanceCoveredLayout);
		df =  new SimpleDateFormat("yyyy-MM-dd");
		date =  df.format(Calendar.getInstance().getTime());
		syncStatusText = (TextView) currentView.findViewById(R.id.SyncStatus);
		String lastDoneMorningDate = sharedPref.getString("morningDate", null);
		
		//Check whether the morning attendance has been done for today's date
		//Use this check to activate or deactivate morning attendance for today
		if(lastDoneMorningDate!=null && !lastDoneMorningDate.isEmpty()){
			if(lastDoneMorningDate.equals(date)){
				//currentView =  inflater.inflate(R.layout.attendance_done_fragment, container, false);
				attendanceAlreadyDone();
				attendanceEnabled[MORNING]=false;
			}
		}
		else{
			attendanceEnabled[MORNING]=true;
		}
		//Check whether noon attendance has been taken for today's date or not
		//Use this check to activate/deactivate noon attendance
		String lastDoneNoonDate = sharedPref.getString("noonDate", null);
		if(lastDoneNoonDate!=null && !lastDoneNoonDate.isEmpty()){
			if(lastDoneNoonDate.equals(date)){
				//currentView =  inflater.inflate(R.layout.attendance_done_fragment, container, false);
				attendanceEnabled[NOON]=false;
			}
		}
		else{
			attendanceEnabled[NOON]=true;
		}
		
		
		controller =  new DBController(currentView.getContext());
		
		
		if(controller.getMorningAttendanceSyncStatus()){
			syncStatusText.setText("Synced");
			//syncStatusText.setTextColor(Color.GREEN);
		}
		else{
			
			syncStatusText.setText("Not synced");
			syncStatusText.setTextColor(Color.RED);
			if(isNetworkAvailable()){
				syncAttendance();
				if(controller.getMorningAttendanceSyncStatus()&&controller.getNoonAttendanceSyncStatus()){
					syncStatusText.setText("Synced");
					syncStatusText.setTextColor(Color.GREEN);
				}
			}
			
		}
		
		
		userList = controller.getAllUsers();
		
		((HomeActivity)getActivity()).attendanceBooleanCheck = new boolean[userList.size()];
		((HomeActivity)getActivity()).attendancePresentList = new ArrayList<Integer>();
		
		
		
		if(userList.size()!=0){
			//Set the User Array list in ListView
			ListAdapter adapter = new AttendanceListAdapter( getActivity(),
					userList, R.layout.fill_student_attendance, 
					new String[] { "rollNumber","studentName"}, 
					new int[] {R.id.rollNo, R.id.studentName});
			myList=(ListView)currentView.findViewById(android.R.id.list);
			myList.setAdapter(adapter);
			//Display Sync status of SQLite DB	
		}

	
		return currentView;
	}
	
	public void markAllPresent(View v){
		
	}
	
	public void declareHoliday(View v){
		if(attendanceEnabled[MORNING]&&attendanceEnabled[NOON]){
			
			new AlertDialog.Builder(getActivity()).setTitle("Declare holiday")
			.setMessage("Are you sure you want to declare "+date+" as a holiday")
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					controller.addHoliday(date);
					SharedPreferences.Editor editor = sharedPref.edit();
					//editor.putString("date", date);
					editor.putString("morningDate", date);
					
					editor.commit();
					
					
					attendanceAlreadyDone();
					
					
					Toast.makeText(getActivity(),date+" has been declared as a holiday.", Toast.LENGTH_SHORT).show();
					attendanceEnabled[MORNING] = false;
					attendanceEnabled[NOON] = false;
					
					Log.d("submitAttendance@AttendanceFrag", "Holiday recorded");
					
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			}).show();

			
		}
		else{
			Toast.makeText(getActivity(), "Attendance already done for today.", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void attendanceAlreadyDone(){
		listContainer.setVisibility(View.GONE);
		Button submitButton = (Button) currentView.findViewById(R.id.submitButton);
		submitButton.setVisibility(View.GONE);
		Button declareHoliday = (Button)currentView.findViewById(R.id.declareHolidayButton);
		declareHoliday.setVisibility(View.GONE);
		((TextView)currentView.findViewById(R.id.warningText)).setText("Attendance submitted for today");
		warningContainer.setVisibility(View.VISIBLE);
		
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public void onToggleClick(View v){
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
	
	public void submitAttendance(View v){
		
		
			if(attendanceEnabled[MORNING]){
				if(((HomeActivity)getActivity()).totalAttendanceList==null){
					Toast.makeText(getActivity(), "Please add students first!", Toast.LENGTH_SHORT).show();
				}
				else{
					if(((HomeActivity)getActivity()).totalAttendanceList.size()!=userList.size()){
						Toast.makeText(getActivity(),"Please fill complete attendance",	Toast.LENGTH_SHORT).show();
						Log.d("attendancePresent", ((HomeActivity)getActivity()).attendancePresentList.size()+"");
						Log.d("userList", userList.size()+"");	
					}
					else{
						new AlertDialog.Builder(getActivity()).setTitle("Submit attendance")
						.setMessage("Are you sure you want to submit attendance for "+date)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								controller.insertMorningAttendance(((HomeActivity)getActivity()).attendancePresentList,
										date);
								SharedPreferences.Editor editor = sharedPref.edit();
								editor.putString("morningDate", date);
								
								editor.commit();
								
								attendanceAlreadyDone();
								
								Toast.makeText(getActivity(), "Morning Attendance recorded for "+date, 
										Toast.LENGTH_SHORT).show();
								attendanceEnabled[MORNING] = false;
								Log.d("submitAttendance@AttendanceFrag", "last morning recorded date set");
								
							}
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								
							}
						}).show();
						
					}
				}
			}
			else{
				Toast.makeText(getActivity(), "Morning Attendance already done for today.", Toast.LENGTH_SHORT).show();
			}
		
		
		/*
		else{
			if(attendanceEnabled[NOON]){
				if(((HomeActivity)getActivity()).totalAttendanceList==null){
					Toast.makeText(getActivity(), "Please add students first!", Toast.LENGTH_SHORT).show();
				}
				else{
					if(((HomeActivity)getActivity()).totalAttendanceList.size()!=userList.size()){
						Toast.makeText(getActivity(),"Please fill complete attendance",	Toast.LENGTH_SHORT).show();
						Log.d("attendancePresent", ((HomeActivity)getActivity()).attendancePresentList.size()+"");
						Log.d("userList", userList.size()+"");	
					}
					else{
						new AlertDialog.Builder(getActivity()).setTitle("Noon Atttendance")
						.setMessage("Are you sure you want to submit Noon Attendance for "+date)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								controller.insertNoonAttendance(((HomeActivity)getActivity()).attendancePresentList, date);
								SharedPreferences.Editor editor = sharedPref.edit();
								editor.putString("noonDate", date);
								
								editor.commit();
								Toast.makeText(getActivity(), "Noon Attendance recorded for "+date, Toast.LENGTH_SHORT).show();
								attendanceEnabled[NOON] = false;
								Log.d("submitAttendance@AttendanceFrag", "last recorded date set noon");
						
								
							}
						}).setNegativeButton("No",new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								
							}
						}).show();
					}
				}
			}
		
			else{
				Toast.makeText(getActivity(), "Noon Attendance already done for today.", Toast.LENGTH_SHORT).show();
			}
		}
		
		
		*/
		
		/*
		View listv;
		RadioGroup attendance;
	
		presentStudents = new ArrayList<Integer>();
		for(int i=0; i<myList.getCount(); i++){
			Log.d("i-debug", i+"");
			listv = myList.getChildAt(i);
			attendance = (RadioGroup)listv.findViewById(R.id.attendanceBox);
			int checkedButtonId=attendance.getCheckedRadioButtonId();
			
			if(checkedButtonId==-1){
				Toast.makeText(getActivity(), "Fill attendance for roll no: "+((TextView)listv.findViewById(R.id.rollNo)).getText().toString(),Toast.LENGTH_LONG).show();
				break;
			}
			else{
				Log.d("Selected",((TextView)listv.findViewById(R.id.rollNo)).getText().toString());
			
				if(((RadioButton)attendance.findViewById(checkedButtonId)).getText().equals("Present")){
				
					presentStudents.add( Integer.parseInt(((TextView)listv.findViewById(R.id.rollNo)).getText().toString()));
				}
			
			}
			
		}
		*/
	}
	
	public void syncAttendance(){
		ArrayList<HashMap<String, String>> studentsListMorning = controller.composeMorningNonUpdateList();
		
		if(studentsListMorning==null){
			return;
		}
		if(studentsListMorning.size()>0){
			new MorningEndpointsTask(studentsListMorning, 
					schoolName, className, "morning").execute(getActivity().getApplicationContext());
		}
		
		/*
		//Updating the noon attendance for the day
		ArrayList<HashMap<String, String>> studentsListnoon = controller.composeNoonNonUpdateList();
		if(studentsListnoon.size()>0){
			Log.d("haha", "ha");
			new MorningEndpointsTask(studentsListnoon, schoolName, 
					className, "noon").execute(getActivity().getApplicationContext());
		
		}
		
			syncButton.setEnabled(false);
			syncButton.setTextColor(Color.GREEN);
		*/
		
	}
	
	
		public class MorningEndpointsTask extends AsyncTask<Context, Integer, String>{
		private ArrayList<HashMap<String, String>> students;
		private String school;
		private String className;
		private String type;
		ProgressDialog pDialog;
		public MorningEndpointsTask(ArrayList<HashMap<String, String>> stud,String sch, String cls,String ty){
			students = stud;
			school = sch;
			className = cls;
			type =ty;
		}
		
		@Override
		  protected void onPreExecute() 
		  {
		    super.onPreExecute();
		    pDialog = new ProgressDialog(getActivity());
		    pDialog.setMessage("Working "+type+" attendance...");
		    pDialog.setIndeterminate(false);
		    pDialog.setCancelable(false);
		    pDialog.show();
		}

		
		@Override
		protected String doInBackground(Context... params) {
			
			Attendanceobjectendpoint.Builder endpointBuilder = new Attendanceobjectendpoint.Builder(AndroidHttp.newCompatibleTransport(),
		              new JacksonFactory(),
		              new HttpRequestInitializer() {
		              public void initialize(HttpRequest httpRequest) { }
		              });
			Attendanceobjectendpoint endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
			
			try{
				for(HashMap<String, String> temp :students){
					AttendanceObject atd= new AttendanceObject();
					atd.setAttendanceId(school+className+type+temp.get("rollNumber")+temp.get("date"));
					atd.setSchoolName(school);
					atd.setClassName(className);
					atd.setType(type);
					atd.setRollNumber(Integer.parseInt(temp.get("rollNumber")));
					atd.setDate(temp.get("date"));
					AttendanceObject atd2 = endpoint.insertAttendanceObject(atd).execute();
					Log.d("Update message", atd2.toString());
					if(type.equals("morning")&& (atd2 !=null) && !(atd2.isEmpty()) ){
						controller.updateMorningSyncStatus(temp.get("rollNumber").toString(),
								temp.get("date").toString(),"yes");
						Log.d("update in progress", "update in progress for"+temp.get("rollNumber").toString());
					}
					else{
						Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
						return "error";
					}
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
	
	
}

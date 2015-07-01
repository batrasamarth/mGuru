package com.eduattendanceapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AttendanceListAdapter extends SimpleAdapter {
	private Activity context;
	private LayoutInflater vi ;
	private HashMap<Integer,String> selectedRows;
	
	public AttendanceListAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.context = (Activity)context;
		selectedRows = new HashMap<Integer, String>();
		((HomeActivity)context).totalAttendanceList = new ArrayList<Integer>();
	}
	
	public View getView(int position, View convertView , ViewGroup parent){
		final View v  = super.getView(position, convertView, parent);
		final int index= position;
		RadioGroup attendanceGroup = (RadioGroup)v.findViewById(R.id.attendanceBox);
		if(selectedRows.containsKey(position)){
			if(selectedRows.get(position).equals("p")){
				((RadioButton)v.findViewById(R.id.presentButton)).setChecked(true);
				Log.d("recycle", "present-check");
			}
			else{
				((RadioButton)v.findViewById(R.id.absentButton)).setChecked(true);
				Log.d("recycle", "absent-check");
			}
		}
		attendanceGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				if(!((HomeActivity)context).totalAttendanceList.contains(Integer.parseInt(((TextView)v.findViewById(R.id.rollNo)).getText().toString()))){
					((HomeActivity)context).totalAttendanceList.add(
						Integer.parseInt(((TextView)v.findViewById(R.id.rollNo)).getText().toString()));
						notifyDataSetChanged();
				}
				switch(checkedId){
				case(R.id.presentButton):
					Log.d("adapter-debug", ((TextView)v.findViewById(R.id.rollNo)).getText().toString());
					selectedRows.put(index, "p");
					((HomeActivity)context).attendancePresentList.add(Integer.parseInt(((TextView)v.findViewById(R.id.rollNo)).getText().toString()));
					break;
				case(R.id.absentButton):
					Log.d("adapter-debug","Absent");
					if(((HomeActivity)context).attendancePresentList.contains(Integer.parseInt(((TextView)v.findViewById(R.id.rollNo)).getText().toString()))){
						((HomeActivity)context).attendancePresentList.remove(Integer.valueOf(((TextView)v.findViewById(R.id.rollNo)).getText().toString()));
					}
					selectedRows.put(index, "a");
				default:
					selectedRows.put(index, "n");
					break;
				}
				
			}
			
		});
	
		return v;	
	}
	public int getViewTypeCount() {                 

	    return getCount();
	}

	@Override
	public int getItemViewType(int position) {

	    return position;
	}
	
	/*
	public View getView(int position, View convertView , ViewGroup parent){
		final View view = super.getView(position, convertView, parent);
		if(false){
			RadioButton prsntButton = (RadioButton)view.findViewById(R.id.presentButton);
			prsntButton.setChecked(false);
			RadioButton absntButton = (RadioButton)view.findViewById(R.id.absentButton);
			absntButton.setChecked(false);
			firstDisplay++;
		}
		RadioGroup attendanceGroup = (RadioGroup)view.findViewById(R.id.attendanceBox);
		return view;
	}
	*/
}

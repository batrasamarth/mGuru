package com.eduattendanceapp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DBController  extends SQLiteOpenHelper {
	
	private String date;
	
	public DBController(Context applicationcontext) {
        super(applicationcontext, "androidsqlite.db", null, 1);
    }
	//Creates Table
	@Override
	public void onCreate(SQLiteDatabase database) {
		String query,query2,query3,query4;
		query = "CREATE TABLE students ( rollNo INTEGER PRIMARY KEY, " +
				" firstName TEXT, " +
				" lastName TEXT, " +
				" gender TEXT, " +
				" dateOfBirth TEXT, " +
				" admissionNumber TEXT, " +
				" updateStatus TEXT)";
		query2 = "CREATE TABLE morningAttendance (rollNo INTEGER," +
				"date TEXT," +
				"updateStatus TEXT)";
		
		query3 = "CREATE TABLE noonAttendance (rollNo INTEGER," +
				"date TEXT," +
				"updateStatus TEXT)";
		query4 = "CREATE TABLE holiday(date TEXT," +
				"updateStatus TEXT)";
		
        database.execSQL(query);
        database.execSQL(query2);
        database.execSQL(query3);
        database.execSQL(query4);
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query,query2,query3,query4;
		query = "DROP TABLE IF EXISTS students";
		query2= "DROP TABLE IF EXISTS morningAttendance";
		query4= "DROP TABLE IF EXISTS noonAttendance";
		query3="DROP TABLE IF EXISTS holiday";
		database.execSQL(query);
		database.execSQL(query2);
		database.execSQL(query3);
		database.execSQL(query4);
        onCreate(database);
	}
	
	
	public void addHoliday(String date){
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("date", date);
		values.put("updateStatus", "no");
		
		database.insert("holiday", null, values);
		
		Log.d("addHoliday@dbcontroller", date+" added to holidays");
		database.close();
	}
	
	
	public void insertMorningAttendance(ArrayList<Integer> presentStudents, String date){
		SQLiteDatabase database = this.getWritableDatabase();
		for(int rollNo:presentStudents){
			ContentValues values = new ContentValues();
			values.put("rollNo", rollNo);
			values.put("date", date);
			values.put("updateStatus", "no");
			database.insert("morningAttendance", null, values);
			Log.d("attendance-debug",rollNo+" "+date);
		}
		database.close();
	}
	
	public void insertNoonAttendance(ArrayList<Integer> presentStudents, String date){
		SQLiteDatabase database = this.getWritableDatabase();
		for(int rollNo:presentStudents){
			ContentValues values = new ContentValues();
			values.put("rollNo", rollNo);
			values.put("date", date);
			values.put("updateStatus", "no");
			database.insert("noonAttendance", null, values);
			Log.d("attendance-debug",rollNo+" "+date);
		}
		database.close();
	}
	
	/**
	 * Inserts User into SQLite DB
	 * @param queryValues
	 */
	public void insertUser(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("rollNo", queryValues.get("rollNo"));
		values.put("firstName", queryValues.get("firstName"));
		values.put("lastName", queryValues.get("lastName"));
		values.put("gender",queryValues.get("gender"));
		values.put("dateOfBirth",queryValues.get("dateOfBirth"));
		values.put("admissionNumber",queryValues.get("admissionNumber"));
		values.put("updateStatus", "no");
		database.insert("students", null, values);
		Log.d("DB-insert", "Db insert operation");
		database.close();
	}
	
	/**
	 * Returns count of students from students database
	 * @return int 
	 */
	public int getStudentCount(){
		String query;
		int number = 0;
		SQLiteDatabase database = this.getWritableDatabase();
		query = "SELECT COUNT(*) FROM students";
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.moveToFirst())
		{
		    number = cursor.getInt(0);
		}
		cursor.close();
		database.close();
		return number;
	}
	
	public boolean checkForHoliday(String date){
		String query;
		int num =0;
		SQLiteDatabase database = this.getWritableDatabase();
		query = "SELECT COUNT(*) FROM holiday WHERE date="+"\'"+date+"\'";
		Cursor cursor = database.rawQuery(query, null);
		if(cursor.moveToFirst()){
			num=cursor.getInt(0);
			Log.d("check",cursor.getInt(0)+"");
		}
		if (num==0){
			return false;
		}
		else return true;
	}
	
	public ArrayList<HashMap<String, String>> getAttendanceForDate(String date, boolean ifMorning){
		ArrayList<HashMap<String, String>> attendanceList = new ArrayList<HashMap<String,String>>();
		SQLiteDatabase database = this.getWritableDatabase();
		String extractQuery = "";
		if(ifMorning){
			extractQuery = "SELECT DISTINCT x.rollNo, x.firstName, x.lastName FROM students x" +
					" INNER JOIN (SELECT * FROM morningAttendance WHERE date ="+"\'"+date+"\') y" +
							" ON x.rollNo = y.rollNo";
			Log.d("extract", extractQuery);
		}
		else{
			extractQuery = "SELECT DISTINCT x.rollNo, x.firstName, x.lastName FROM students x" +
					" INNER JOIN (SELECT * FROM noonAttendance WHERE date ="+"\'"+date+"\') y" +
							" ON x.rollNo = y.rollNo";
			Log.d("extract", extractQuery);
		}
		Cursor cursor = database.rawQuery(extractQuery, null);
		if (cursor.moveToFirst()){
			do{
				HashMap<String, String> entry = new HashMap<String, String>();
				entry.put("rollNo", cursor.getString(0));
				entry.put("studentName", cursor.getString(1)+" "+cursor.getString(2));
				attendanceList.add(entry);
			}while(cursor.moveToNext());
			
		}
		cursor.close();
		database.close();
		return attendanceList;
	}
	/**
	 * Get list of Users from SQLite DB as Array List
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getAllUsers() {
		SQLiteDatabase database = this.getWritableDatabase();
	    
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM students ORDER BY rollNo";
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("rollNo", cursor.getString(0));
	        	map.put("studentName", cursor.getString(1)+" "+cursor.getString(2));
	        	
                wordList.add(map);
	        } while (cursor.moveToNext());
	    }
	    database.close();
	    return wordList;
	}
	
	public ArrayList<HashMap<String, String>> composeMorningNonUpdateList(){
		ArrayList<HashMap<String, String>> morningList = new ArrayList<HashMap<String,String>>();
		String selectQuery = "SELECT * FROM morningAttendance where updateStatus = '"+"no"+"'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()){
			do{
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("rollNo",cursor.getString(0));
				map.put("date", cursor.getString(1));
				Log.d("composeMorningNonUpdateList",cursor.getString(0)+" "
	        			+cursor.getString(1));
				morningList.add(map);
			}while(cursor.moveToNext());
		}
		database.close();
		return morningList;
	}
	public ArrayList<HashMap<String, String>> composeNoonNonUpdateList(){
		ArrayList<HashMap<String, String>> noonList = new ArrayList<HashMap<String,String>>();
		String selectQuery = "SELECT * FROM noonAttendance where updateStatus = '"+"no"+"'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()){
			do{
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("rollNo",cursor.getString(0));
				map.put("date", cursor.getString(1));
				Log.d("composeNoonNonUpdateList",cursor.getString(0)+" "
	        			+cursor.getString(1));
				noonList.add(map);
			}while(cursor.moveToNext());
		}
		database.close();
		return noonList;
	}
	
	/**
	 * Compose JSON out of SQLite records
	 * @return
	 */
	public ArrayList<HashMap<String, String>> composeNonUpdateStudentList(){
		ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM students where updateStatus = '"+"no"+"'";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("rollNo", cursor.getString(0));
	        	map.put("firstName", cursor.getString(1));
	        	map.put("lastName", cursor.getString(2));
	        	map.put("gender", cursor.getString(3));
	        	map.put("dateOfBirth", cursor.getString(4));
	        	map.put("admissionNumber", cursor.getString(5));
	        	Log.d("composeNonUpdateStudentList",cursor.getString(0)+" "
	        			+cursor.getString(1)+" "
	        			+cursor.getString(2)+" "
	        			+cursor.getString(3)+" "
	        			+ cursor.getString(4)+" "
	        			+cursor.getString(5));
	        	studentList.add(map);
	        } while (cursor.moveToNext());
	    }
	    database.close();
		
		return studentList;
	}
	public boolean getMorningAttendanceSyncStatus(){
		if(this.morningAttendanceSyncCount()==0){
			return true;
		}
		else{
			return false;
		}
	}
	public boolean getNoonAttendanceSyncStatus(){
		if(this.noonAttendanceSyncCount()==0){
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * Get Sync status of SQLite
	 * @return
	 */
	public String getSyncStatus(){
	    String msg = null;
	    if(this.dbSyncCount() == 0){
	    	msg = "SQLite and Remote MySQL DBs are in Sync!";
	    }else{
	    	msg = "DB Sync needed\n";
	    }
	    return msg;
	}
	public boolean studentSyncStatus(){
		if(this.dbSyncCount() == 0){
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
	/**
	 * Get SQLite records that are yet to be Synced
	 * @return
	 */
	public int dbSyncCount(){
		int count = 0;
		String selectQuery = "SELECT  * FROM students where updateStatus = '"+"no"+"'";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    count = cursor.getCount();
	    database.close();
		return count;
	}
	public int morningAttendanceSyncCount(){
		int count=0;
		String selectQuery = "SELECT * FROM morningAttendance where updateStatus = '"+"no"+"'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		count = cursor.getCount();
		database.close();
		return count;
	}
	public int noonAttendanceSyncCount(){
		int count=0;
		String selectQuery = "SELECT * FROM noonAttendance where updateStatus = '"+"no"+"'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		count = cursor.getCount();
		database.close();
		return count;
	}
	
	/**
	 * Update Sync status against each User ID
	 * @param id
	 * @param status
	 */
	public void updateSyncStatus(String id, String status){
		SQLiteDatabase database = this.getWritableDatabase();	 
		String updateQuery = "Update students set updateStatus = '"+ status +"' where rollNo="+"'"+ id +"'";
		Log.d("query",updateQuery);		
		database.execSQL(updateQuery);
		database.close();
	}
	public void updateMorningSyncStatus(String rollNo, String date, String status){
		SQLiteDatabase database = this.getWritableDatabase();	 
		String updateQuery = "Update morningAttendance set updateStatus = '"+ status +"' where rollNo="+"'"+ rollNo +"'"
				+" and date="+"'"+date+"'";
		Log.d("query",updateQuery);		
		database.execSQL(updateQuery);
		database.close();
	}
	public void updateNoonSyncStatus(String rollNo, String date, String status){
		SQLiteDatabase database = this.getWritableDatabase();	 
		String updateQuery = "Update noonAttendance set updateStatus = '"+ status +"' where rollNo="+"'"+ rollNo +"'"
				+" and date="+"'"+date+"'";
		Log.d("query",updateQuery);		
		database.execSQL(updateQuery);
		database.close();
	}
}

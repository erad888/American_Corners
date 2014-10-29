package com.dosamericancorner.home;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
//import net.sqlcipher.database.*;
import android.database.sqlite.SQLiteDatabase;

public class LoginDataBaseAdapter 
{
		public static final int NAME_COLUMN = 1;
		static final String tableName="LOGIN";
		// TODO: Create public field for each column in your table.
		// SQL Statement to create a new database.
		static final String USER_TABLE_CREATE = "create table "+"LOGIN"+
		                             "( " +"ID"+" integer primary key autoincrement,"+ "USERNAME  text,PASSWORD text); ";
		// Variable to hold the database instance
		public  SQLiteDatabase db;
		// Context of the application using the database.
		private final Context context;
		// Database open/upgrade helper
		private DataBaseHelper dbHelper;
		public  LoginDataBaseAdapter(Context _context) 
		{
			context = _context;
			dbHelper = new DataBaseHelper(context);
		}
		public  LoginDataBaseAdapter open() throws SQLException 
		{
			db = dbHelper.getWritableDatabase();
			return this;
		}
		public void close() 
		{
			db.close();
		}

		public  SQLiteDatabase getDatabaseInstance()
		{
			return db;
		}

		public void insertEntry(String userName,String password)
		{
	       ContentValues newValues = new ContentValues();
			// Assign values for each row.
			newValues.put("USERNAME", userName);
			newValues.put("PASSWORD",password);
			
			// Insert the row into your table
			db.insert("LOGIN", null, newValues);
			///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
		}
		public int deleteEntry(String UserName)
		{
			//String id=String.valueOf(ID);
		    String where="USERNAME=?";
		    int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{UserName}) ;
	       // Toast.makeText(context, "Number of Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
	        return numberOFEntriesDeleted;
		}	
		public String getSingleEntry(String userName)
		{
			Cursor cursor=db.query("LOGIN", null, "USERNAME=?", new String[]{userName}, null, null, null);
	        if(cursor.getCount()<1) // UserName Not Exist
	        {
	        	cursor.close();
	        	return "NOT EXIST";
	        }
		    cursor.moveToFirst();
			String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
			cursor.close();
			return password;				
		}
		public void  updateEntry(String userName,String password)
		{
			// Define the updated row content.
			ContentValues updatedValues = new ContentValues();
			// Assign values for each row.
			updatedValues.put("USERNAME", userName);
			updatedValues.put("PASSWORD",password);
			
	        String where="USERNAME = ?";
		    db.update("LOGIN",updatedValues, where, new String[]{userName});			   
		}
		public int getCount()
		{
			//Query
			String query = "select USERNAME from LOGIN";
			Cursor cursor = db.rawQuery(query, null);
			if(cursor.getCount()<1) // title Not Exist
			{
			 	cursor.close();
			  	return 0;
			}
			int count = cursor.getCount();
			cursor.close();
			return count;
		}
		public boolean isValid(String username, String password)
		{
			//Query
			String query = "select USERNAME and PASSWORD from LOGIN where USERNAME = ? and PASSWORD = ?";
			Cursor cursor = db.rawQuery(query, new String[] {username, password});
			if(cursor.getCount()<1) // Not Found
			{
			 	cursor.close();
			  	return false;
			}
			return true;
		}
		public ArrayList<String> getUsernames()
		{
			ArrayList<String> usernames = new ArrayList<String>();
			//Query
			String query = "select USERNAME from LOGIN";
			Cursor cursor = db.rawQuery(query, null);
			if(cursor.getCount()<1) // Not Found
			{
			 	cursor.close();
			 	usernames.add("No Users Found");
			  	return usernames;
			}
			while(cursor.moveToNext()) {
				usernames.add(cursor.getString(cursor.getColumnIndex("USERNAME")));
			}
			return usernames;
		}
}


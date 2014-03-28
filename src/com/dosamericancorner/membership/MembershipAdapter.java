package com.dosamericancorner.membership;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dosamericancorner.home.DataBaseHelper;

public class MembershipAdapter {
	public static final int NAME_COLUMN = 1;
	static final String tableName="MEMBERSHIP";
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.
	public static final String MEMBERSHIP_TABLE_CREATE = "create table "+"MEMBERSHIP"
			+ "( " +"ID"+" integer primary key autoincrement,"
			+ "FIRST_NAME text,"
			+ "LAST_NAME text,"
			+ "BIRTHDAY text,"
			+ "MEMBER_ID text,"
			+ "EMAIL text,"
			+ "CHECKOUT_COUNT integer,"
			+ "KARMA_PTS integer,"
			+ "NOTES text); ";
	// Variable to hold the database instance
	public  SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;
	public  MembershipAdapter(Context _context) 
	{
		context = _context;
		dbHelper = new DataBaseHelper(context);
	}
	public  MembershipAdapter open() throws SQLException 
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
	
	public void insertEntry(String FIRSTNAME, String LASTNAME, String BIRTHDAY,
			String MEMBER_ID, String EMAIL, int CHECKOUT_COUNT, int KARMA_PTS,
			String NOTES)
	{
       ContentValues newValues = new ContentValues();
		// Assign values for each row.
       if(FIRSTNAME.equals(null))
    	   newValues.put("FIRST_NAME", "");
       else
       		newValues.put("FIRST_NAME", FIRSTNAME);
       if(LASTNAME.equals(null))
    	   newValues.put("LAST_NAME", "");
       else
    	   newValues.put("LAST_NAME", LASTNAME);
       if(BIRTHDAY.equals(null))
       		newValues.put("BIRTHDAY", "");
       else
    	   newValues.put("BIRTHDAY", BIRTHDAY);
       if(MEMBER_ID.equals(null))
       		newValues.put("MEMBER_ID", "");
       else
    	   newValues.put("MEMBER_ID", MEMBER_ID);
       if(EMAIL.equals(null))
       		newValues.put("EMAIL", "");
       else
    	   newValues.put("EMAIL", EMAIL);
       if(CHECKOUT_COUNT==0)
       		newValues.put("CHECKOUT_COUNT", 0);
       else
    	   newValues.put("CHECKOUT_COUNT", CHECKOUT_COUNT);
       if(KARMA_PTS==0)
       		newValues.put("KARMA_PTS", 0);
       else
    	   newValues.put("KARMA_PTS", KARMA_PTS);
       if(NOTES.equals(null))
       		newValues.put("NOTES", "");
       else
    	   newValues.put("NOTES", NOTES);
		
		// Insert the row into your table
		db.insert("MEMBERSHIP", null, newValues);
		///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
	}
	
	public void updateEntry(String FIRSTNAME, String LASTNAME, String BIRTHDAY,
			String MEMBER_ID, String EMAIL, int CHECKOUT_COUNT, int KARMA_PTS,
			String NOTES) {
		ContentValues newValues = new ContentValues();
		
		if(FIRSTNAME.equals(null))
	    	   newValues.put("FIRST_NAME", "");
	       else
	       		newValues.put("FIRST_NAME", FIRSTNAME);
	       if(LASTNAME.equals(null))
	    	   newValues.put("LAST_NAME", "");
	       else
	    	   newValues.put("LAST_NAME", LASTNAME);
	       if(BIRTHDAY.equals(null))
	       		newValues.put("BIRTHDAY", "");
	       else
	    	   newValues.put("BIRTHDAY", BIRTHDAY);
	       if(EMAIL.equals(null))
	       		newValues.put("EMAIL", "");
	       else
	    	   newValues.put("EMAIL", EMAIL);
	       if(CHECKOUT_COUNT==0)
	       		newValues.put("CHECKOUT_COUNT", 0);
	       else
	    	   newValues.put("CHECKOUT_COUNT", CHECKOUT_COUNT);
	       if(KARMA_PTS==0)
	       		newValues.put("KARMA_PTS", 0);
	       else
	    	   newValues.put("KARMA_PTS", KARMA_PTS);
	       if(NOTES.equals(null))
	       		newValues.put("NOTES", "");
	       else
	    	   newValues.put("NOTES", NOTES);
			
	       db.update("MEMBERSHIP", newValues, "MEMBER_ID = "+MEMBER_ID, null);
	}
	
	public int deleteEntry(String MEMBER_ID)
	{
		String query = "select ID from MEMBERSHIP where MEMBER_ID = ?";
		Cursor cursor = db.rawQuery(query, new String[] {MEMBER_ID});
        if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return 0;
        }
        cursor.moveToFirst();
		String ID = ((Integer)cursor.getInt(cursor.getColumnIndex("ID"))).toString();
		cursor.close();
		
	    String where="ID = ? and MEMBER_ID = ?";
	    int numberOFEntriesDeleted= db.delete("Membership", where, new String[]{ID, MEMBER_ID}) ;
       // Toast.makeText(context, "Number of Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
	}
	
	public int countMembers()
	{
		int count = 0;
		String query = "select * from MEMBERSHIP";
		Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return 0;
        }
        count = cursor.getCount();
		return count;
	}
	
	public boolean memberIdFound(String search) {
		String MEMBERID = search;
		//Query
		String query = "select MEMBERID from MEMBERSHIP where MEMBERID = ?";
		Cursor cursor = db.rawQuery(query, new String[] {MEMBERID});
		if(cursor.getCount()<1) // title Not Exist
        {
        	cursor.close();
        	return false;
        }
		else {
			cursor.close();
			return true;
		}
	}

}
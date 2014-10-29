package com.dosamericancorner.membership;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
//import net.sqlcipher.database.*;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

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
	public static SQLiteDatabase db;
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
		//Toast.makeText(context, "Member Is Successfully Saved", Toast.LENGTH_LONG).show();
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
			
	       //db.update(table, values, whereClause, whereArgs)
	       db.update("MEMBERSHIP", newValues, "MEMBER_ID = ?", new String[] {MEMBER_ID});
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
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	return 0;
		}
		while(cursor.moveToNext())
		{
			count++;
		}
		cursor.close();
		return count;
	}
	
	public int countMatchingMembers(String search)
	{
		//Query
		String query = "select * from MEMBERSHIP where FIRST_NAME + LAST_NAME = ? or FIRST_NAME LIKE ? or LAST_NAME LIKE ?" +
				" or BIRTHDAY = ? or MEMBER_ID = ? or EMAIL = ? or NOTES LIKE ?";
		Cursor cursor = db.rawQuery(query, new String[] {search, search, search, search, search, search, search});
        if(cursor.getCount()<1) // title Not Exist
        {
        	cursor.close();
        	return 0;
        }
        int count = 0;
        while(cursor.moveToNext())
        	count++;
		cursor.close();
		return count;
	}
	
	public boolean memberIdFound(String MEMBERID) {
		//Query
		String query = "select * from MEMBERSHIP where MEMBER_ID = ?";
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
	
	// 0 = first name
	// 1 = last name
	// 2 = birthday
	// 3 = member Id
	// 4 = email
	// 5 = checkout count
	// 6 = karma pts
	// 7 = notes
	public String[][] getAll()
	{
		int counter = 0;
		int total = countMembers();
		String[][] entry ;
		if(total == 0)
			entry = new String[1][8];
		else
			entry = new String[total][8];
		//Query
		String query = "select * from MEMBERSHIP";
		Cursor cursor = db.rawQuery(query, null);
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	for(int i = 0; i < 8; i++)
		 		entry[0][i] = "Not Found";
		  	return entry;
		}
		while(cursor.moveToNext())
		{
			//combine data into one array
			entry[counter][0] = cursor.getString(cursor.getColumnIndex("FIRST_NAME"));
			entry[counter][1] = cursor.getString(cursor.getColumnIndex("LAST_NAME"));
			entry[counter][2] = cursor.getString(cursor.getColumnIndex("BIRTHDAY"));
			entry[counter][3] = cursor.getString(cursor.getColumnIndex("MEMBER_ID"));
			entry[counter][4] = cursor.getString(cursor.getColumnIndex("EMAIL"));
			entry[counter][5] = ((Integer)cursor.getInt(cursor.getColumnIndex("CHECKOUT_COUNT"))).toString();
			entry[counter][6] = ((Integer)cursor.getInt(cursor.getColumnIndex("KARMA_PTS"))).toString();
			entry[counter][7] = cursor.getString(cursor.getColumnIndex("NOTES"));
			counter++;
		}
		cursor.close();
		return entry;
	}
	
	// 0 = first name
	// 1 = last name
	// 2 = birthday
	// 3 = member Id
	// 4 = email
	// 5 = checkout count
	// 6 = karma pts
	// 7 = notes
	public String[][] getAllMatching(String search)
	{
		int counter = 0;
		int total = countMatchingMembers(search);
		String[][] entry ;
		if(total == 0)
			entry = new String[1][8];
		else
			entry = new String[total][8];
		//Query
		String query = "select * from MEMBERSHIP where FIRST_NAME + ' ' + LAST_NAME = ? or FIRST_NAME LIKE ? or LAST_NAME LIKE ?" +
				" or BIRTHDAY = ? or MEMBER_ID = ? or EMAIL = ? or NOTES LIKE ?";
		Cursor cursor = db.rawQuery(query, new String[] {search, search, search, search, search, search, search});
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	for(int i = 0; i < 8; i++)
		 		entry[0][i] = "Not Found";
		  	return entry;
		}
		while(cursor.moveToNext())
		{
			//combine data into one array
			entry[counter][0] = cursor.getString(cursor.getColumnIndex("FIRST_NAME"));
			entry[counter][1] = cursor.getString(cursor.getColumnIndex("LAST_NAME"));
			entry[counter][2] = cursor.getString(cursor.getColumnIndex("BIRTHDAY"));
			entry[counter][3] = cursor.getString(cursor.getColumnIndex("MEMBER_ID"));
			entry[counter][4] = cursor.getString(cursor.getColumnIndex("EMAIL"));
			entry[counter][5] = ((Integer)cursor.getInt(cursor.getColumnIndex("CHECKOUT_COUNT"))).toString();
			entry[counter][6] = ((Integer)cursor.getInt(cursor.getColumnIndex("KARMA_PTS"))).toString();
			entry[counter][7] = cursor.getString(cursor.getColumnIndex("NOTES"));
			counter++;
		}
		cursor.close();
		return entry;
	}
	
	public String[] getMatchingMember(String memberID)
	{
		String[] entry = new String[8];
		//Query
		String query = "select * from MEMBERSHIP where MEMBER_ID = ?";
		Cursor cursor = db.rawQuery(query, new String[] {memberID});
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	for(int i = 0; i < 8; i++)
		 		entry[i] = "Not Found";
		  	return entry;
		}
		while(cursor.moveToNext())
		{
			//combine data into one array
			entry[0] = cursor.getString(cursor.getColumnIndex("FIRST_NAME"));
			entry[1] = cursor.getString(cursor.getColumnIndex("LAST_NAME"));
			entry[2] = cursor.getString(cursor.getColumnIndex("BIRTHDAY"));
			entry[3] = cursor.getString(cursor.getColumnIndex("MEMBER_ID"));
			entry[4] = cursor.getString(cursor.getColumnIndex("EMAIL"));
			entry[5] = ((Integer)cursor.getInt(cursor.getColumnIndex("CHECKOUT_COUNT"))).toString();
			entry[6] = ((Integer)cursor.getInt(cursor.getColumnIndex("KARMA_PTS"))).toString();
			entry[7] = cursor.getString(cursor.getColumnIndex("NOTES"));
		}
		cursor.close();
		return entry;
	}
	
	public int getKarmaCount(String memberID)
	{
		//Query
		String query = "select KARMA_PTS from MEMBERSHIP where MEMBER_ID = ?";
		Cursor cursor = db.rawQuery(query, new String[] {memberID});
        if(cursor.getCount()<1) // title Not Exist
        {
        	cursor.close();
        	return 0;
        }
	    cursor.moveToFirst();
		int count = cursor.getInt(cursor.getColumnIndex("KARMA_PTS"));
		cursor.close();
		return count;				
	}
	
	public void increaseKarma(String memberID)
	{
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put("KARMA_PTS", getKarmaCount(memberID)+1);
		
        String where="MEMBER_ID = ?";
	    db.update("MEMBERSHIP",updatedValues, where, new String[]{memberID});
	}
	
	public void decreaseKarma(String memberID)
	{
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put("KARMA_PTS", getKarmaCount(memberID)-1);
		
        String where="MEMBER_ID = ?";
	    db.update("MEMBERSHIP",updatedValues, where, new String[]{memberID});
	}
	
	public Boolean isMemberExist(String search)
	{
		//Query
		String query = "select * from MEMBERSHIP where MEMBER_ID = ?";
		Cursor cursor = db.rawQuery(query, new String[] {search});
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		  	return false;
		}
		cursor.close();
		return true;
	}
	
	public Boolean isMemberMatching(String Name, String memberID)
	{
		String firstName = Name.substring(0, Name.indexOf(" "));
		String lastName = Name.substring(Name.indexOf(" ")+1,Name.length());
		System.out.println("isMemberMatching -> firstName: '"+firstName+"'");
		System.out.println("isMemberMatching -> lastName: '"+lastName+"'");
		//Query
		String query = "select * from MEMBERSHIP where FIRST_NAME = ? and LAST_NAME = ? and MEMBER_ID = ?";
		Cursor cursor = db.rawQuery(query, new String[] {firstName, lastName, memberID});
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		  	return false;
		}
		cursor.close();
		return true;
	}

}
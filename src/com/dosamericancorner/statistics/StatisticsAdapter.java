package com.dosamericancorner.statistics;

import java.util.Calendar;

import com.dosamericancorner.debug.debug;
import com.dosamericancorner.home.DataBaseHelper;
import com.dosamericancorner.login.BuildConfig;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
//import net.sqlcipher.database.*;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StatisticsAdapter {
	public static final int NAME_COLUMN = 1;
	static final String tableName="STATISTICS";
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.
	public static final String STATISTICS_TABLE_CREATE = "create table "+"STATISTICS"
			+ "( " +"ID"+" integer primary key autoincrement,"
			+ "ISBN text,"
			+ "YEAR text,"
			+ "MONTH text,"
			+ "COUNT integer); ";
	// Variable to hold the database instance
	public  SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;
	public  StatisticsAdapter(Context _context) 
	{
		context = _context;
		dbHelper = new DataBaseHelper(context);
	}
	public  StatisticsAdapter open() throws SQLException 
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
	
	public void insertEntry(String ISBN, int year, int month, int count)
	{
       ContentValues newValues = new ContentValues();
		// Assign values for each row.
       	newValues.put("ISBN", ISBN);
       	newValues.put("YEAR", ((Integer)year).toString());
       	if(month>=10)
       		newValues.put("MONTH", ((Integer)month).toString());
       	else
       		newValues.put("MONTH", "0"+((Integer)month).toString());
		newValues.put("COUNT", count);
		
		// Insert the row into your table
		db.insert("STATISTICS", null, newValues);
		///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
	}
	
	public int deleteEntry(String ISBN, int year, int month)
	{
		String query = "select ID from STATISTICS where ISBN = ? and YEAR = ? and MONTH = ?";
		Cursor cursor = db.rawQuery(query, new String[] {ISBN, ((Integer)year).toString(), ((Integer)month).toString()});
        if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return 0;
        }
        cursor.moveToFirst();
		String ID = ((Integer)cursor.getInt(cursor.getColumnIndex("ID"))).toString();
		cursor.close();
		
	    String where="ID = ? and ISBN = ? and YEAR = ? and MONTH = ?";
	    int numberOFEntriesDeleted= db.delete("STATISTICS", where, new String[]{ID, ISBN, ((Integer)year).toString(), ((Integer)month).toString()}) ;
       // Toast.makeText(context, "Number of Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
	}
	
	public int getNumEntriesByISBN(String ISBN, int startYear, int startMonth, int endYear, int endMonth)
	{
		int count = 0;
		int year = startYear;
		int month = startMonth;
		String query = "select * from STATISTICS where ISBN = ? and YEAR = ? and MONTH = ?";
		while(year <= endYear && month <= endMonth)
		{
			Cursor cursor = db.rawQuery(query, new String[] {ISBN, ((Integer)year).toString(), ((Integer)month).toString()});
	        if(cursor.getCount()<1) // ISBN Not Found in given month and year
	        	cursor.close();
	        else
	        	count++;
	        cursor.close();
	        month++;
	        if(month > 12)
	        {
	        	year++;
	        	month = 1;
	        }
		}
        
		return count;
	}
	
	public String[][] getEntriesByISBN(String ISBN, int startYear, int startMonth, int endYear, int endMonth)
	{
		int entriesCount = getNumEntriesByISBN(ISBN, startYear, startMonth, endYear, endMonth);
		String [][] entries = new String [entriesCount][3];
		int count = 0;
		int year = startYear;
		int month = startMonth;
		String query = "select * from STATISTICS where ISBN = ? and YEAR = ? and MONTH = ?";
		while(year <= endYear && month <= endMonth)
		{
			Cursor cursor = db.rawQuery(query, new String[] {ISBN, ((Integer)year).toString(), ((Integer)month).toString()});
	        if(cursor.getCount()<1) // ISBN Not Found in given month and year
	        	cursor.close();
	        else
	        {
	        	entries[count][0] = ((Integer)year).toString();
	        	entries[count][1] = ((Integer)month).toString();
	        	entries[count][2] = ((Integer)cursor.getInt(cursor.getColumnIndex("COUNT"))).toString();
	        	cursor.close();
	        }
	        month++;
	        if(month > 12)
	        {
	        	year++;
	        	month = 1;
	        }
	        count++;
		}
		
		return entries;
	}
	
	public int getNumEntriesByDate(int startYear, int startMonth, int endYear, int endMonth)
	{
		int count = 0;
		String sYear = ((Integer)startYear).toString();
		String sMonth;
		if(startMonth >= 10)
			sMonth = ((Integer)startMonth).toString();
		else
			sMonth = "0"+((Integer)startMonth).toString();
		String eYear = ((Integer)endYear).toString();
		String eMonth;
		if(endMonth >= 10)
			eMonth = ((Integer)endMonth).toString();
		else
			eMonth = "0"+((Integer)endMonth).toString();
		String sDate = sYear+sMonth;
		String eDate = eYear+eMonth;
		String query = "select COUNT from STATISTICS where YEAR || MONTH between ? and ?";
		if (BuildConfig.DEBUG)
		    Log.d(debug.LOG, query);
		
		Cursor cursor=db.rawQuery(query, new String[]{sDate,eDate});
        if(cursor.getCount()>=1) // At least 1 items found in given month and year
        	count+=cursor.getCount();
        while(cursor.moveToNext())
        {
        	if (BuildConfig.DEBUG)
    		    Log.d(debug.LOG, "Found Here");
        }
        cursor.close();  
        if (BuildConfig.DEBUG)
		    Log.d(debug.LOG, "count="+count);
        
		return count;
	}
	
	public String[][] getEntriesByDate(int startYear, int startMonth, int endYear, int endMonth)
	{
		int entriesCount = getNumEntriesByDate(startYear, startMonth, endYear, endMonth);
		if (BuildConfig.DEBUG)
		    Log.d(debug.LOG, "============ getEntriesByDate ================");
		if (BuildConfig.DEBUG)
		    Log.d(debug.LOG, "entriesCount="+entriesCount);
		String [][] entries = new String [entriesCount][4];
		int index = 0;
		String sYear = ((Integer)startYear).toString();
		String sMonth;
		if(startMonth >= 10)
			sMonth = ((Integer)startMonth).toString();
		else
			sMonth = "0"+((Integer)startMonth).toString();
		String eYear = ((Integer)endYear).toString();
		String eMonth;
		if(endMonth >= 10)
			eMonth = ((Integer)endMonth).toString();
		else
			eMonth = "0"+((Integer)endMonth).toString();
		String sDate = sYear+sMonth;
		String eDate = eYear+eMonth;
		String query = "select * from STATISTICS where YEAR || MONTH between ? and ?";
		if (BuildConfig.DEBUG)
		    Log.d(debug.LOG, query);
		
		Cursor cursor=db.rawQuery(query, new String[]{sDate,eDate});
    	while(cursor.moveToNext())
    	{
        	if (BuildConfig.DEBUG)
        	{
        		Log.d(debug.LOG, "year != endYear");
			    Log.d(debug.LOG, "ISBN="+cursor.getString(cursor.getColumnIndex("ISBN")));
			    Log.d(debug.LOG, "year="+sYear+" | month="+sMonth);
			    Log.d(debug.LOG, "COUNT="+cursor.getInt(cursor.getColumnIndex("COUNT")));
        	}
        	entries[index][0] = cursor.getString(cursor.getColumnIndex("ISBN"));
        	entries[index][1] = sYear;
        	entries[index][2] = sMonth;
        	entries[index][3] = ((Integer)cursor.getInt(cursor.getColumnIndex("COUNT"))).toString();
        	index++;
    	}
    	cursor.close();
    	
    	if (BuildConfig.DEBUG)
    	{
    		Log.d(debug.LOG, "-=-=-=-=-=-=-");
    		index = 0;
    		while(index < entriesCount)
    		{
    			Log.d(debug.LOG, "entries["+index+"][0]="+entries[index][0]);
    			Log.d(debug.LOG, "entries["+index+"][1]="+entries[index][1]);
    			Log.d(debug.LOG, "entries["+index+"][2]="+entries[index][2]);
    			Log.d(debug.LOG, "entries["+index+"][3]="+entries[index][3]);
    			index++;
    		}
    		Log.d(debug.LOG, "~~~~~~~~~~~~");
    	}
		
		return entries;
	}
	
	// numItemsInStat returns the total number of items on loan
	public int numItemsInStat()
	{
		int count = 0;
		//Query
		String query = "select *  from STATISTICS";
		Cursor cursor = db.rawQuery(query, null);
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	return 0;
		}
		count = cursor.getCount();
		cursor.close();
		return count;
	}
	
	/* 
	 * getAll returns an array of all entries in the table.
	 * Values are returned in a String array based on the following order:
	 * 1. ISBN
	 * 2. YEAR
	 * 3. MONTH
	 * 4. COUNT
	 */
	public String[][] getAll()
	{
		int position = 0;
		int totalEntries = numItemsInStat();
		if(totalEntries==0)
			totalEntries=1;
		String[][] entry = new String[totalEntries][4];

		//Query
		String query = "select ISBN,YEAR,MONTH,COUNT from STATISTICS";
		Cursor cursor = db.rawQuery(query, null);
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	for(int i = 0; i < 4; i++)
		 		entry[0][i] = "Not Found";
		  	return entry;
		}
		else {
			while(cursor.moveToNext())
			{
				//put data into respective variable
				String ISBN = cursor.getString(cursor.getColumnIndex("ISBN"));
				String YEAR = cursor.getString(cursor.getColumnIndex("YEAR"));
				String MONTH = cursor.getString(cursor.getColumnIndex("MONTH"));
				String COUNT = cursor.getString(cursor.getColumnIndex("COUNT"));
				//combine variables into one array
				entry[position][0] = ISBN;
				entry[position][1] = YEAR;
				entry[position][2] = MONTH;
				entry[position][3] = COUNT;
				position++;
			}
		}
		cursor.close();
		return entry;
	}
	
	public int getYear(String ISBN)
	{
		//Query
		String query = "select YEAR from STATISTICS where ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {ISBN});
        if(cursor.getCount()<1) // title Not Exist
        {
        	cursor.close();
        	return 0;
        }
	    cursor.moveToFirst();
		int year = cursor.getInt(cursor.getColumnIndex("YEAR"));
		cursor.close();
		return year;				
	}
	
	public int getMonth(String ISBN)
	{
		//Query
		String query = "select MONTH from STATISTICS where ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {ISBN});
        if(cursor.getCount()<1) // title Not Exist
        {
        	cursor.close();
        	return 0;
        }
	    cursor.moveToFirst();
		int month = cursor.getInt(cursor.getColumnIndex("MONTH"));
		cursor.close();
		return month;				
	}
	
	public int getCount(String ISBN)
	{
		//Query
		String query = "select COUNT from STATISTICS where ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {ISBN});
        if(cursor.getCount()<1) // title Not Exist
        {
        	cursor.close();
        	return 0;
        }
	    cursor.moveToFirst();
		int count = cursor.getInt(cursor.getColumnIndex("COUNT"));
		cursor.close();
		return count;				
	}
	
	public boolean isExisting(String ISBN)
	{
		//define today's date
		Calendar curDate = Calendar.getInstance();
		String curYear = ((Integer)curDate.get(Calendar.YEAR)).toString();
		String curMonth = ((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
		String curDay = ((Integer)curDate.get(Calendar.DAY_OF_MONTH)).toString();
		if(curDate.get(Calendar.MONTH)+1 < 10)
			curMonth = "0"+((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
		if(curDate.get(Calendar.DAY_OF_MONTH) < 10)
			curDay = "0"+((Integer)(curDate.get(Calendar.DAY_OF_MONTH))).toString();
		String currentDate = curYear+"-"+curMonth+"-"+curDay;
		
		//Query
		String query = "select * from STATISTICS where ISBN = ? and YEAR = ? and MONTH = ?";
		Cursor cursor = db.rawQuery(query, new String[] {ISBN, curYear, curMonth});
        if(cursor.getCount()<1) // title Not Exist
        {
        	cursor.close();
        	return false;
        }
		cursor.close();
		return true;
	}
	
	public void  increaseCount(String ISBN)
	{
		//define today's date
		Calendar curDate = Calendar.getInstance();
		String curYear = ((Integer)curDate.get(Calendar.YEAR)).toString();
		String curMonth = ((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
		String curDay = ((Integer)curDate.get(Calendar.DAY_OF_MONTH)).toString();
		if(curDate.get(Calendar.MONTH)+1 < 10)
			curMonth = "0"+((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
		if(curDate.get(Calendar.DAY_OF_MONTH) < 10)
			curDay = "0"+((Integer)(curDate.get(Calendar.DAY_OF_MONTH))).toString();
		String currentDate = curYear+"-"+curMonth+"-"+curDay;
		
		if(isExisting(ISBN))
		{
			// Define the updated row content.
			ContentValues updatedValues = new ContentValues();
			// Assign values for each row.
			updatedValues.put("COUNT", getCount(ISBN)+1);
			
	        String where="ISBN = ?  and YEAR = ? and MONTH = ?";
		    db.update("STATISTICS",updatedValues, where, new String[]{ISBN, curYear, curMonth});
		}
		else
		{
			insertEntry(ISBN, Integer.parseInt(curYear), Integer.parseInt(curMonth), 1);
		}
	}
	
}

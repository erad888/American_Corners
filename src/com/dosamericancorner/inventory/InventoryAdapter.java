package com.dosamericancorner.inventory;

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
import android.widget.Toast;

public class InventoryAdapter {

	public static final int NAME_COLUMN = 1;
	static final String tableName="INVENTORY";
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.
	public static final String INVENTORY_TABLE_CREATE = "create table "+"INVENTORY"
			+ "( " +"ID"+" integer primary key autoincrement,"
			+ "ISBN text,"
			+ "TITLE text,"
			+ "AUTHOR text,"
			+ "PUBLISH_DATE integer,"
			+ "CALL_NUMBER text,"
			+ "AVAILABLE_COUNT integer,"
			+ "INVENTORY_COUNT integer,"
			+ "DUE_PERIOD integer,"
			+ "COUNT integer); ";
	// Variable to hold the database instance
	public static  SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;
	public  InventoryAdapter(Context _context) 
	{
		context = _context;
		dbHelper = new DataBaseHelper(context);
	}
	public  InventoryAdapter open() throws SQLException 
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

	public void insertEntry(String title, String author, String ISBN, int publishdate, String callNumber, 
			int inventorycount, int dueperiod)
	{
		int zero = 0;
       ContentValues newValues = new ContentValues();
		// Assign values for each row.
        newValues.put("ISBN", ISBN);
		newValues.put("TITLE", title);
		newValues.put("AUTHOR", author);
		newValues.put("PUBLISH_DATE", publishdate);
		newValues.put("CALL_NUMBER", callNumber);
		newValues.put("AVAILABLE_COUNT", inventorycount);
		newValues.put("INVENTORY_COUNT", inventorycount);
		newValues.put("DUE_PERIOD", dueperiod);
		newValues.put("COUNT", zero);
		
		// Insert the row into your table
		db.insert("INVENTORY", null, newValues);
		///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
	}
	public int deleteEntry(String title, String ISBN)
	{
		//String id=String.valueOf(ID);
	    String where="TITLE=? and ISBN=?";
	    int numberOFEntriesDeleted= db.delete("INVENTORY", where, new String[]{title, ISBN}) ;
       // Toast.makeText(context, "Number of Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
	}
	public int deleteAllEntries()
	{
		//String id=String.valueOf(ID);
	    String where="";
	    int numberOFEntriesDeleted= db.delete("INVENTORY", where, null) ;
       // Toast.makeText(context, "Number of Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
	}
	
	/* 
	 * getInventoryEntry returns a single entry in the table based on a search by title and/or ISBN
	 * Values are returned in a String array based on the following order:
	 * 1. ISBN
	 * 2. Title
	 * 3. Author
	 * 4. Publish Date
	 * 5. Call Number
	 * 6. Available Inventory Count
	 * 7. Inventory Count
	 * 8. Default Due Period
	 * 9. Checkout Count
	 */
	public static String[] getInventoryEntry(String title, String ISBN)
	{
		String[] entry = new String[9];
		//Wild card Syntax
		title = "%"+title+"%";
		ISBN = "%"+ISBN+"%";
		//Query
		String query = "select * from INVENTORY where TITLE LIKE ? or ISBN LIKE ?";
		Cursor cursor = db.rawQuery(query, new String[] {title, ISBN});
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	for(int i = 0; i < 9; i++)
		 		entry[i] = "Not Found";
		  	return entry;
		}
		cursor.moveToFirst();
		//put data into respective variable
		int publish = cursor.getInt(cursor.getColumnIndex("PUBLISH_DATE"));
		String publishdate = ((Integer)publish).toString();
		String author = cursor.getString(cursor.getColumnIndex("AUTHOR"));
		String callNumber = cursor.getString(cursor.getColumnIndex("CALL_NUMBER"));
		int available = cursor.getInt(cursor.getColumnIndex("AVAILABLE_COUNT"));
		String availablecount = ((Integer)available).toString();
		int inventory = cursor.getInt(cursor.getColumnIndex("INVENTORY_COUNT"));
		String inventorycount = ((Integer)inventory).toString();
		int due = cursor.getInt(cursor.getColumnIndex("DUE_PERIOD"));
		String dueperiod = ((Integer)due).toString();
		int checkoutcount = cursor.getInt(cursor.getColumnIndex("COUNT"));
		String count = ((Integer)checkoutcount).toString();
		if(title.equals("%%"))
			title = cursor.getString(cursor.getColumnIndex("TITLE"));
		if(ISBN.equals("%%"))
			ISBN = cursor.getString(cursor.getColumnIndex("ISBN"));
		//combine variables into one array
		entry[0] = ISBN;
		entry[1] = title;
		entry[2] = author;
		entry[3] = publishdate;
		entry[4] = callNumber;
		entry[5] = availablecount;
		entry[6] = inventorycount;
		entry[7] = dueperiod;
		entry[8] = count;
		cursor.close();
		return entry;
	}
	
	/* 
	 * getInventoryByISBN returns a single entry in the table based on a search by ISBN
	 * Values are returned in a String array based on the following order:
	 * 1. ISBN
	 * 2. Title
	 * 3. Author
	 * 4. Publish Date
	 * 5. Call Number
	 * 6. Available Inventory Count
	 * 7. Inventory Count
	 * 8. Default Due Period
	 * 9. Checkout Count
	 */
	public static String[] getInventoryByISBN(String ISBN)
	{
		String[] entry = new String[9];
		//Query
		String query = "select * from INVENTORY where ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {ISBN});
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	for(int i = 0; i < 9; i++)
		 		entry[i] = "Not Found";
		  	return entry;
		}
		cursor.moveToFirst();
		//put data into respective variable
		int publish = cursor.getInt(cursor.getColumnIndex("PUBLISH_DATE"));
		String publishdate = ((Integer)publish).toString();
		String title = cursor.getString(cursor.getColumnIndex("TITLE"));
		String author = cursor.getString(cursor.getColumnIndex("AUTHOR"));
		String callNumber = cursor.getString(cursor.getColumnIndex("CALL_NUMBER"));
		int available = cursor.getInt(cursor.getColumnIndex("AVAILABLE_COUNT"));
		String availablecount = ((Integer)available).toString();
		int inventory = cursor.getInt(cursor.getColumnIndex("INVENTORY_COUNT"));
		String inventorycount = ((Integer)inventory).toString();
		int due = cursor.getInt(cursor.getColumnIndex("DUE_PERIOD"));
		String dueperiod = ((Integer)due).toString();
		int checkoutcount = cursor.getInt(cursor.getColumnIndex("COUNT"));
		String count = ((Integer)checkoutcount).toString();
		//combine variables into one array
		entry[0] = ISBN;
		entry[1] = title;
		entry[2] = author;
		entry[3] = publishdate;
		entry[4] = callNumber;
		entry[5] = availablecount;
		entry[6] = inventorycount;
		entry[7] = dueperiod;
		entry[8] = count;
		cursor.close();
		return entry;
	}
	
	// ---------------- Individual Getters --------------------
	
	public static int getTotalCount()
	{
		int count = 0;
		//Query
		String query = "select * from INVENTORY";
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
	
	public static String[][] getAll()
	{
		int counter = 0;
		int total = getTotalCount();
		String[][] entry ;
		if(total == 0)
			entry = new String[1][9];
		else
			entry = new String[total][9];
		//Query
		String query = "select * from INVENTORY";
		Cursor cursor = db.rawQuery(query, null);
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	for(int i = 0; i < 9; i++)
		 		entry[0][i] = "Not Found";
		  	return entry;
		}
		//put data into respective variable
		int publish, available, inventory, due, checkoutcount;
		String publishdate, title, author, ISBN, callNumber, availablecount, inventorycount, dueperiod, count;
		while(cursor.moveToNext())
		{
			//put data into respective variable
			publish = cursor.getInt(cursor.getColumnIndex("PUBLISH_DATE"));
			publishdate = ((Integer)publish).toString();
			title = cursor.getString(cursor.getColumnIndex("TITLE"));
			author = cursor.getString(cursor.getColumnIndex("AUTHOR"));
			ISBN = cursor.getString(cursor.getColumnIndex("ISBN"));
			callNumber = cursor.getString(cursor.getColumnIndex("CALL_NUMBER"));
			available = cursor.getInt(cursor.getColumnIndex("AVAILABLE_COUNT"));
			availablecount = ((Integer)available).toString();
			inventory = cursor.getInt(cursor.getColumnIndex("INVENTORY_COUNT"));
			inventorycount = ((Integer)inventory).toString();
			due = cursor.getInt(cursor.getColumnIndex("DUE_PERIOD"));
			dueperiod = ((Integer)due).toString();
			checkoutcount = cursor.getInt(cursor.getColumnIndex("COUNT"));
			count = ((Integer)checkoutcount).toString();
			//combine variables into one array
			entry[counter][0] = ISBN;
			entry[counter][1] = title;
			entry[counter][2] = author;
			entry[counter][3] = publishdate;
			entry[counter][4] = callNumber;
			entry[counter][5] = availablecount;
			entry[counter][6] = inventorycount;
			entry[counter][7] = dueperiod;
			entry[counter][8] = count;
			counter++;
		}
		cursor.close();
		return entry;
	}
	
	public static String getISBN(String title, String author, int publishDate, String callNumber)
	{
		title = "%"+title+"%";
		author = "%"+author+"%";
		String date = "%"+publishDate+"%";
		callNumber = "%"+callNumber+"%";
		//Query
		String query = "select ISBN from INVENTORY where TITLE LIKE ? or AUTHOR LIKE ? or PUBLISH_DATE LIKE ? or CALL_NUMBER LIKE ?";
		Cursor cursor = db.rawQuery(query, new String[] {title, author, date, callNumber});
		if(cursor.getCount()<1) // title Not Exist
        {
        	cursor.close();
        	return "not found";
        }
	    cursor.moveToFirst();
		String isbn = cursor.getString(cursor.getColumnIndex("ISBN"));
		cursor.close();
		return isbn;
	}
	
	public static String getTitleByISBN(String ISBN)
	{
		String entry = new String();
		if (BuildConfig.DEBUG)
			Log.d(debug.LOG, "getTitleByISBN -> ISBN="+ISBN);
		//Query
		String query = "select * from INVENTORY where ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {ISBN});
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	entry = "Not Found";
		  	return entry;
		}
		cursor.moveToFirst();
		//put data into respective variable
		entry = cursor.getString(cursor.getColumnIndex("TITLE"));
		cursor.close();
		return entry;
	}
	
	public static String getAuthorByISBN(String ISBN)
	{
		String entry = new String();
		//Query
		String query = "select * from INVENTORY where ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {ISBN});
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	entry = "Not Found";
		  	return entry;
		}
		cursor.moveToFirst();
		//put data into respective variable
		entry = cursor.getString(cursor.getColumnIndex("AUTHOR"));
		cursor.close();
		return entry;
	}
	
	public static String getCallNumberByISBN(String ISBN)
	{
		String entry = new String();
		//Query
		String query = "select * from INVENTORY where ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {ISBN});
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	entry = "Not Found";
		  	return entry;
		}
		cursor.moveToFirst();
		//put data into respective variable
		entry = cursor.getString(cursor.getColumnIndex("CALL_NUMBER"));
		cursor.close();
		return entry;
	}
	
	public static String getPublishYearByISBN(String ISBN)
	{
		String entry = new String();
		//Query
		String query = "select * from INVENTORY where ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {ISBN});
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	entry = "Not Found";
		  	return entry;
		}
		cursor.moveToFirst();
		//put data into respective variable
		entry = cursor.getString(cursor.getColumnIndex("PUBLISH_DATE"));
		cursor.close();
		return entry;
	}
	
	public static int getNumSearch(String search)
	{
		String title = "%"+search+"%";
		String author = "%"+search+"%";
		String ISBN = search;
		int count = 0;
		//Query
		String query = "select ISBN from INVENTORY where TITLE LIKE ? or AUTHOR LIKE ? or ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {title, author, ISBN});
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
	
	public static String[] getSearchISBN(String search)
	{
		String title = "%"+search+"%";
		String author = "%"+search+"%";
		String ISBN = search;
		int numEntries = getNumSearch(search);
		int position = 0;
		String[] isbn = new String[numEntries];
		//Query
		String query = "select ISBN from INVENTORY where TITLE LIKE ? or AUTHOR LIKE ? or ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {title, author, ISBN});
		if(cursor.getCount()<1) // title Not Exist
        {
        	cursor.close();
        	isbn = new String[1];
        	isbn[0] = "not found";
        	return isbn;
        }
		while(cursor.moveToNext())
		{
			isbn[position] = cursor.getString(cursor.getColumnIndex("ISBN"));
			position++;
		}
		cursor.close();
		return isbn;
	}
	
	public static int getNumSearchEntries(String search)
	{
		int count = 0;
		//Query
		String query = "select TITLE from INVENTORY where ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {search});
		if(cursor.getCount()<1) // title Not Exist
        {
        	cursor.close();
        	return 0;
        }
		while(cursor.moveToNext()) {
			count++;
		}
		cursor.close();
		return count;
	}
	
	public String getTitleAndAuthorByISBN(String ISBN)
	{
		int entriesFound = getNumSearchEntries(ISBN);
		if(entriesFound==0)
			entriesFound = 1;
		String searchEntry;
		//Query
		String query = "select * from INVENTORY where ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {ISBN});
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	searchEntry = "Not Found";
		  	return searchEntry;
		}
		cursor.moveToFirst();
		//put data into respective variable
		String title = cursor.getString(cursor.getColumnIndex("TITLE"));
		String author = cursor.getString(cursor.getColumnIndex("AUTHOR"));
		//combine variables into one String
		searchEntry = title + " / " + author;
		//close cursor and return
		cursor.close();
		return searchEntry;
	}
	
	public String[] getInventoryEntriesByISBN(String search, String ISBN)
	{
		String[] searchEntry = new String [9];
		//Query
		String query = "select * from INVENTORY where ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {ISBN});
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	for(int i = 0; i < 9; i++)
		 		searchEntry[i] = "Not Found";
		  	return searchEntry;
		}
		cursor.moveToFirst();
		
		//put data into respective variable
		int publish = cursor.getInt(cursor.getColumnIndex("PUBLISH_DATE"));
		String publishdate = ((Integer)publish).toString();
		String title = cursor.getString(cursor.getColumnIndex("TITLE"));
		String author = cursor.getString(cursor.getColumnIndex("AUTHOR"));
		String callNumber = cursor.getString(cursor.getColumnIndex("CALL_NUMBER"));
		int available = cursor.getInt(cursor.getColumnIndex("AVAILABLE_COUNT"));
		String availablecount = ((Integer)available).toString();
		int inventory = cursor.getInt(cursor.getColumnIndex("INVENTORY_COUNT"));
		String inventorycount = ((Integer)inventory).toString();
		int due = cursor.getInt(cursor.getColumnIndex("DUE_PERIOD"));
		String dueperiod = ((Integer)due).toString();
		int checkoutcount = cursor.getInt(cursor.getColumnIndex("COUNT"));
		String count = ((Integer)checkoutcount).toString();
		//combine variables into one array
		searchEntry[0] = ISBN;
		searchEntry[1] = title;
		searchEntry[2] = author;
		searchEntry[3] = publishdate;
		searchEntry[4] = callNumber;
		searchEntry[5] = availablecount;
		searchEntry[6] = inventorycount;
		searchEntry[7] = dueperiod;
		searchEntry[8] = count;

		cursor.close();
		return searchEntry;
	}
	
	public int getCount(String ISBN)
	{
		//Query
		String query = "select COUNT from INVENTORY where ISBN = ?";
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
	
	public int getInventoryCount(String ISBN)
	{
		//Query
		String query = "select AVAILABLE_COUNT from INVENTORY where ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {ISBN});
        if(cursor.getCount()<1) // title Not Exist
        {
        	cursor.close();
        	return 0;
        }
	    cursor.moveToFirst();
		int count = cursor.getInt(cursor.getColumnIndex("INVENTORY_COUNT"));
		cursor.close();
		return count;				
	}
	
	public int getAvailableCount(String ISBN)
	{
		//Query
		String query = "select AVAILABLE_COUNT from INVENTORY where ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {ISBN});
        if(cursor.getCount()<1) // title Not Exist
        {
        	cursor.close();
        	return 0;
        }
	    cursor.moveToFirst();
		int count = cursor.getInt(cursor.getColumnIndex("AVAILABLE_COUNT"));
		cursor.close();
		return count;				
	}
	
	public boolean isFoundInInventory(String search) 
	{
		String title = "%"+search+"%";
		String author = "%"+search+"%";
		String ISBN = search;
		//Query
		String query = "select ISBN from INVENTORY where TITLE LIKE ? or AUTHOR LIKE ? or ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {title, author, ISBN});
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
	
	public boolean isbnFoundInInventory(String search) 
	{
		String ISBN = search;
		//Query
		String query = "select ISBN from INVENTORY where ISBN = ?";
		Cursor cursor = db.rawQuery(query, new String[] {ISBN});
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
	
	public void  increaseCount(String ISBN)
	{
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put("COUNT", getCount(ISBN)+1);
		updatedValues.put("AVAILABLE_COUNT", getAvailableCount(ISBN)-1);
		
        String where="ISBN = ?";
	    db.update("INVENTORY",updatedValues, where, new String[]{ISBN});
	}
	
	public void  decreaseCount(String ISBN)
	{
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put("COUNT", getCount(ISBN)-1);
		
        String where="ISBN = ?";
	    db.update("INVENTORY",updatedValues, where, new String[]{ISBN});			   
	}
	
	public void  increaseAvailable(String ISBN)
	{
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put("AVAILABLE_COUNT", getAvailableCount(ISBN)+1);
		
        String where="ISBN = ?";
	    db.update("INVENTORY",updatedValues, where, new String[]{ISBN});
	}
	
	public void  decreaseAvailable(String ISBN)
	{
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put("AVAILABLE_COUNT", getAvailableCount(ISBN)-1);
		
        String where="ISBN = ?";
	    db.update("INVENTORY",updatedValues, where, new String[]{ISBN});
	}
	
}

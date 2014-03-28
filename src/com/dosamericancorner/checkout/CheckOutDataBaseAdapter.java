package com.dosamericancorner.checkout;

import java.util.Calendar;

import com.dosamericancorner.home.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CheckOutDataBaseAdapter {

	public static final int NAME_COLUMN = 1;
	static final String tableName="CHECKOUT";
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.
	public static final String CHECKOUT_TABLE_CREATE = "create table "+"CHECKOUT"
			+ "( " +"ID"+" integer primary key autoincrement,"
			+ "USER text,"
			+ "CHECKOUT_INDIVIDUAL text,"
			+ "MEMBER_ID text,"
			+ "ISBN text,"
			+ "CHECKOUT_DATE text,"
			+ "DUE_DATE text); ";
	// Variable to hold the database instance
	public static  SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;
	public  CheckOutDataBaseAdapter(Context _context) 
	{
		context = _context;
		dbHelper = new DataBaseHelper(context);
	}
	public  CheckOutDataBaseAdapter open() throws SQLException 
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

	public void insertEntry(String user, String CheckoutIndividual, String MemberID, String ISBN, String checkoutDate, String dueDate)
	{
       ContentValues newValues = new ContentValues();
		// Assign values for each row.
       	newValues.put("USER", user);
       	newValues.put("CHECKOUT_INDIVIDUAL", CheckoutIndividual);
       	newValues.put("MEMBER_ID", MemberID);
		newValues.put("ISBN", ISBN);
		newValues.put("CHECKOUT_DATE", checkoutDate);
		newValues.put("DUE_DATE", dueDate);
		
		// Insert the row into your table
		db.insert("CHECKOUT", null, newValues);
		///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
	}
	public int deleteEntry(String checkoutIndividual, String MemberID, String ISBN, String checkoutDate, String dueDate)
	{
		String query = "select ID from CHECKOUT where CHECKOUT_INDIVIDUAL = ? and MEMBER_ID = ? and ISBN = ? and CHECKOUT_DATE = ? and DUE_DATE = ?";
		Cursor cursor = db.rawQuery(query, new String[] {checkoutIndividual, MemberID, ISBN, checkoutDate, dueDate});
        if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return 0;
        }
        cursor.moveToFirst();
		String ID = ((Integer)cursor.getInt(cursor.getColumnIndex("ID"))).toString();
		cursor.close();
		
	    String where="ID = ? and CHECKOUT_INDIVIDUAL = ? and MEMBER_ID = ? and ISBN = ? and CHECKOUT_DATE = ? and DUE_DATE = ?";
	    int numberOFEntriesDeleted= db.delete("CHECKOUT", where, new String[]{ID, checkoutIndividual, MemberID, ISBN, checkoutDate, dueDate}) ;
       // Toast.makeText(context, "Number of Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
	}
	public int deleteItem(String checkoutIndividual, String MemberID, String ISBN)
	{
		String query = "select ID from CHECKOUT where CHECKOUT_INDIVIDUAL = ? and MEMBER_ID = ? and ISBN = ? ";
		Cursor cursor = db.rawQuery(query, new String[] {checkoutIndividual, MemberID, ISBN});
        if(cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return 0;
        }
        cursor.moveToFirst();
		String ID = ((Integer)cursor.getInt(cursor.getColumnIndex("ID"))).toString();
		cursor.close();
		
	    String where="ID = ? and CHECKOUT_INDIVIDUAL = ? and MEMBER_ID = ? and ISBN = ? ";
	    int numberOFEntriesDeleted= db.delete("CHECKOUT", where, new String[]{ID, checkoutIndividual, MemberID, ISBN}) ;
       // Toast.makeText(context, "Number of Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
	}
	
	/* 
	 * getCheckoutEntry returns a single entry in the table based on a search by ISBN
	 * Values are returned in a String array based on the following order:
	 * 1. User
	 * 2. Checkout Individual
	 * 3. Member ID
	 * 4. ISBN
	 * 5. Checkout Date
	 * 6. Due Date
	 */
	public String[] getCheckoutEntry(String ISBN)
	{
		String[] entry = new String[6];

		//Query
		String query = "select USER,CHECKOUT_INDIVIDUAL,MEMBER_ID,CHECKOUT_DATE,DUE_DATE from CHECKOUT where ISBN = ? ";
		String[] selectionArgs = new String[] {ISBN};
		Cursor cursor = db.rawQuery(query, selectionArgs);
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	for(int i = 0; i < 6; i++)
		 		entry[i] = "Not Found";
		  	return entry;
		}
		cursor.moveToFirst();
		
		//put data into respective variable
		String checkoutIndividual = cursor.getString(cursor.getColumnIndex("CHECKOUT_INDIVIDUAL"));
		String memberID = cursor.getString(cursor.getColumnIndex("MEMBER_ID"));
		String user = cursor.getString(cursor.getColumnIndex("USER"));
		String checkoutDate = cursor.getString(cursor.getColumnIndex("CHECKOUT_DATE"));
		String dueDate = cursor.getString(cursor.getColumnIndex("DUE_DATE"));
		
		//combine variables into one array
		entry[0] = user;
		entry[1] = checkoutIndividual;
		entry[2] = memberID;
		entry[3] = ISBN;
		entry[4] = checkoutDate;
		entry[5] = dueDate;
		cursor.close();
		return entry;
	}
	
	public int getNumMatchingEntries(String ISBN)
	{
		int count = 0;
		//Query
		String query = "select * from CHECKOUT where ISBN = ? ";
		String[] selectionArgs = new String[] {ISBN};
		Cursor cursor = db.rawQuery(query, selectionArgs);
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
	
	/* 
	 * getMatchingCheckoutEntries returns an array of entries in the table based on ISBN
	 * Values are returned in a String array based on the following order:
	 * 1. User
	 * 2. Checkout Individual
	 * 3. Member ID
	 * 4. ISBN
	 * 5. Checkout Date
	 * 6. Due Date
	 */
	public String[][] getMatchingCheckoutEntries(String ISBN)
	{
		int position = 0;
		int totalEntries = getNumMatchingEntries(ISBN);
		if(totalEntries==0)
			totalEntries=1;
		String[][] entry = new String[totalEntries][6];

		//Query
		String query = "select USER,CHECKOUT_INDIVIDUAL,MEMBER_ID,CHECKOUT_DATE,DUE_DATE from CHECKOUT where ISBN = ? ";
		String[] selectionArgs = new String[] {ISBN};
		Cursor cursor = db.rawQuery(query, selectionArgs);
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	for(int i = 0; i < 6; i++)
		 		entry[position][i] = "Not Found";
		  	return entry;
		}
		else {
			while(cursor.moveToNext())
			{
				//put data into respective variable
				String checkoutIndividual = cursor.getString(cursor.getColumnIndex("CHECKOUT_INDIVIDUAL"));
				String memberID = cursor.getString(cursor.getColumnIndex("MEMBER_ID"));
				String user = cursor.getString(cursor.getColumnIndex("USER"));
				String checkoutDate = cursor.getString(cursor.getColumnIndex("CHECKOUT_DATE"));
				String dueDate = cursor.getString(cursor.getColumnIndex("DUE_DATE"));
				
				//combine variables into one array
				entry[position][0] = user;
				entry[position][1] = checkoutIndividual;
				entry[position][2] = memberID;
				entry[position][3] = ISBN;
				entry[position][4] = checkoutDate;
				entry[position][5] = dueDate;
				position++;
			}
		}
		cursor.close();
		return entry;
	}
	
	/* 
	 * getAllEntries returns an array of all entries in the table.
	 * Values are returned in a String array based on the following order:
	 * 1. User
	 * 2. Checkout Individual
	 * 3. Member ID
	 * 4. ISBN
	 * 5. Checkout Date
	 * 6. Due Date
	 */
	public String[][] getAllEntries()
	{
		int position = 0;
		int totalEntries = numItemsOnLoan();
		if(totalEntries==0)
			totalEntries=1;
		String[][] entry = new String[totalEntries][6];

		//Query
		String query = "select USER,CHECKOUT_INDIVIDUAL,MEMBER_ID,ISBN,CHECKOUT_DATE,DUE_DATE from CHECKOUT";
		Cursor cursor = db.rawQuery(query, null);
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	for(int i = 0; i < 6; i++)
		 		entry[0][i] = "Not Found";
		  	return entry;
		}
		else {
			while(cursor.moveToNext())
			{
				//put data into respective variable
				String checkoutIndividual = cursor.getString(cursor.getColumnIndex("CHECKOUT_INDIVIDUAL"));
				String memberID = cursor.getString(cursor.getColumnIndex("MEMBER_ID"));
				String user = cursor.getString(cursor.getColumnIndex("USER"));
				String ISBN = cursor.getString(cursor.getColumnIndex("ISBN"));
				String checkoutDate = cursor.getString(cursor.getColumnIndex("CHECKOUT_DATE"));
				String dueDate = cursor.getString(cursor.getColumnIndex("DUE_DATE"));
				
				//combine variables into one array
				entry[position][0] = user;
				entry[position][1] = checkoutIndividual;
				entry[position][2] = memberID;
				entry[position][3] = ISBN;
				entry[position][4] = checkoutDate;
				entry[position][5] = dueDate;
				position++;
			}
		}
		cursor.close();
		return entry;
	}
	
	/* 
	 * getMatchingCheckoutEntries returns an array of entries in the table based on ISBN, Individual, and Member ID
	 * Values are returned in a String array based on the following order:
	 * 1. Checkout Date
	 * 2. Due Date
	 */
	public String[] findCheckoutEntry(String ISBN, String Individual, String MemberID)
	{
		String[] entry = new String[2];

		//Query
		String query = "select CHECKOUT_DATE,DUE_DATE from CHECKOUT where ISBN = ? and CHECKOUT_INDIVIDUAL = ? and MEMBER_ID = ? ";
		String[] selectionArgs = new String[] {ISBN, Individual, MemberID};
		Cursor cursor = db.rawQuery(query, selectionArgs);
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	for(int i = 0; i < 2; i++)
		 		entry[i] = "Not Found";
		  	return entry;
		}
		else {
			cursor.moveToFirst();
				//put data into respective variable
				String checkoutDate = cursor.getString(cursor.getColumnIndex("CHECKOUT_DATE"));
				String dueDate = cursor.getString(cursor.getColumnIndex("DUE_DATE"));
				
				//combine variables into one array
				entry[0] = checkoutDate;
				entry[1] = dueDate;
		}
		cursor.close();
		return entry;
	}
	
	/* 
	 * getOverdueEntries returns an array of all entries in the table that are overdue.
	 * Values are returned in a String array based on the following order:
	 * 1. User
	 * 2. Checkout Individual
	 * 3. Member ID
	 * 4. ISBN
	 * 5. Checkout Date
	 * 6. Due Date
	 */
	public String[][] getOverdueEntries()
	{
		int position = 0;
		int totalEntries = numOverdueItems();
		if(totalEntries==0)
			totalEntries=1;
		String[][] entry = new String[totalEntries][6];

		//define Report Start date
 		Calendar curDate = Calendar.getInstance();
 		Calendar defaultStart = Calendar.getInstance();
 		defaultStart.set(curDate.get(Calendar.YEAR), curDate.get(Calendar.MONTH)-1, curDate.get(Calendar.DAY_OF_MONTH));
 		String curYear = ((Integer)curDate.get(Calendar.YEAR)).toString();
 		String curMonth = ((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
 		String curDay = ((Integer)curDate.get(Calendar.DAY_OF_MONTH)).toString();
 		if(curDate.get(Calendar.MONTH)+1 < 10)
 			curMonth = "0"+((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
 		if(curDate.get(Calendar.DAY_OF_MONTH) < 10)
 			curDay = "0"+((Integer)(curDate.get(Calendar.DAY_OF_MONTH))).toString();
 		String currentDate = curYear+"-"+curMonth+"-"+curDay;
 		int startMonth = (defaultStart.get(Calendar.MONTH)+1);
 		int startYear = defaultStart.get(Calendar.YEAR);
		
		//Query
		String query = "select USER,CHECKOUT_INDIVIDUAL,MEMBER_ID,ISBN,CHECKOUT_DATE,DUE_DATE from CHECKOUT where DUE_DATE > ?";
		Cursor cursor = db.rawQuery(query, new String[]{currentDate});
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	for(int i = 0; i < 6; i++)
		 		entry[0][i] = "Not Found";
		  	return entry;
		}
		else {
			while(cursor.moveToNext())
			{
				//put data into respective variable
				String checkoutIndividual = cursor.getString(cursor.getColumnIndex("CHECKOUT_INDIVIDUAL"));
				String memberID = cursor.getString(cursor.getColumnIndex("MEMBER_ID"));
				String user = cursor.getString(cursor.getColumnIndex("USER"));
				String ISBN = cursor.getString(cursor.getColumnIndex("ISBN"));
				String checkoutDate = cursor.getString(cursor.getColumnIndex("CHECKOUT_DATE"));
				String dueDate = cursor.getString(cursor.getColumnIndex("DUE_DATE"));
				
				//combine variables into one array
				entry[position][0] = user;
				entry[position][1] = checkoutIndividual;
				entry[position][2] = memberID;
				entry[position][3] = ISBN;
				entry[position][4] = checkoutDate;
				entry[position][5] = dueDate;
				position++;
			}
		}
		cursor.close();
		return entry;
	}
	
	// numItemsOnLoan returns the total number of items on loan
	public int numItemsOnLoan()
	{
		int count = 0;
		//Query
		String query = "select *  from CHECKOUT";
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
	
	public int numOverdueItems()
	{
		int count = 0;
		//Query
		String query = "select *  from CHECKOUT";
		Cursor cursor = db.rawQuery(query, null);
		if(cursor.getCount()<1) // title Not Exist
		{
		 	cursor.close();
		 	return 0;
		}
		// Get due date
		Calendar curDate = Calendar.getInstance();
		String curYear = ((Integer)curDate.get(Calendar.YEAR)).toString();
		String curMonth = ((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
		String curDay = ((Integer)curDate.get(Calendar.DAY_OF_MONTH)).toString();
		if(curDate.get(Calendar.MONTH)+1 < 10)
			curMonth = "0"+((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
		String todayDate = curYear+"-"+curMonth+"-"+curDay;
		while(cursor.moveToNext())
		{
			String dueDate = cursor.getString(cursor.getColumnIndex("DUE_DATE"));
			if(dueDate.compareTo(todayDate) < 1)
				count++;
		}
		cursor.close();
		return count;
	}
	
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
		String[][] entry;
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
	
}

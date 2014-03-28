package com.dosamericancorner.home;

import com.dosamericancorner.search.*;
import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.inventory.*;
import com.dosamericancorner.statistics.*;
import com.dosamericancorner.membership.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper
{	
	// Database Version
		private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "database.db";
		
	// ============================ End Variables ===========================
	
	public DataBaseHelper(Context context, String name, CursorFactory factory, int version) 
    {
	           super(context, name, factory, version);
	}
	
	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// Called when no database exists in disk and the helper class needs
	// to create a new one.
	@Override
	public void onCreate(SQLiteDatabase _db) 
	{
			_db.execSQL(LoginDataBaseAdapter.USER_TABLE_CREATE);
			_db.execSQL(CheckOutDataBaseAdapter.CHECKOUT_TABLE_CREATE);
			_db.execSQL(InventoryAdapter.INVENTORY_TABLE_CREATE);
			_db.execSQL(StatisticsAdapter.STATISTICS_TABLE_CREATE);
			_db.execSQL(MembershipAdapter.MEMBERSHIP_TABLE_CREATE);
	}
	// Called when there is a database version mismatch meaning that the version
	// of the database on disk needs to be upgraded to the current version.
	@Override
	public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) 
	{
			// Log the version upgrade.
			Log.w("TaskDBAdapter", "Upgrading from version " +_oldVersion + " to " +_newVersion + ", which will destroy all old data");
	
	
			// Upgrade the existing database to conform to the new version. Multiple
			// previous versions can be handled by comparing _oldVersion and _newVersion
			// values.
			// on upgrade drop older tables
			_db.execSQL("DROP TABLE IF EXISTS " + LoginDataBaseAdapter.USER_TABLE_CREATE);
			_db.execSQL("DROP TABLE IF EXISTS " + CheckOutDataBaseAdapter.CHECKOUT_TABLE_CREATE);
			_db.execSQL("DROP TABLE IF EXISTS " + InventoryAdapter.INVENTORY_TABLE_CREATE);
			_db.execSQL("DROP TABLE IF EXISTS " + StatisticsAdapter.STATISTICS_TABLE_CREATE);
			_db.execSQL("DROP TABLE IF EXISTS " + MembershipAdapter.MEMBERSHIP_TABLE_CREATE);

			// Create a new one.
			onCreate(_db);
	}
	

}

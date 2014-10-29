package com.dosamericancorner.home;

import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.inventory.*;
import com.dosamericancorner.statistics.*;
import com.dosamericancorner.membership.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import net.sqlcipher.database.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteOpenHelper;
import java.io.File;
import android.os.Environment;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper
{	
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "database.db";
	
	// Database Password
	private static final String DB_PASSWORD = "americanCornersPassword";
		
	// ============================ End Variables ===========================
	
	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		/*
		SQLiteDatabase.loadLibs(context);
		File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"american_corners");
        if(!fileDir.exists()){
			try{
				fileDir.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        File databaseFile = new File(fileDir + File.separator + DATABASE_NAME);
        databaseFile.mkdirs();
        databaseFile.delete();
        SQLiteDatabase _db = SQLiteDatabase.openOrCreateDatabase(databaseFile, DB_PASSWORD, null);
        _db.execSQL(LoginDataBaseAdapter.USER_TABLE_CREATE);
		_db.execSQL(CheckOutDataBaseAdapter.CHECKOUT_TABLE_CREATE);
		_db.execSQL(InventoryAdapter.INVENTORY_TABLE_CREATE);
		_db.execSQL(StatisticsAdapter.STATISTICS_TABLE_CREATE);
		_db.execSQL(MembershipAdapter.MEMBERSHIP_TABLE_CREATE);
		
		System.out.println("Tables have been created");
		*/
	}

	/*
	public SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
		File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"american_corners");
        File databaseFile = new File(fileDir + File.separator + DATABASE_NAME);
        //SQLiteDatabase.openDatabase(path, password, factory, flags);
		SQLiteDatabase _db = SQLiteDatabase.openOrCreateDatabase(databaseFile, DB_PASSWORD, null);
		return _db;
	}
	*/
	
	
	// Called when no database exists in disk and the helper class needs
	// to create a new one.
	
	@Override
	public void onCreate(SQLiteDatabase  _db) 
	{
		//File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"american_corners");
        //File databaseFile = new File(fileDir + File.separator + DATABASE_NAME);
        //SQLiteDatabase.openDatabase(path, password, factory, flags);
		//SQLiteDatabase _db = SQLiteDatabase.openOrCreateDatabase(databaseFile, DB_PASSWORD, null);
			_db.execSQL(LoginDataBaseAdapter.USER_TABLE_CREATE);
			_db.execSQL(CheckOutDataBaseAdapter.CHECKOUT_TABLE_CREATE);
			_db.execSQL(InventoryAdapter.INVENTORY_TABLE_CREATE);
			_db.execSQL(StatisticsAdapter.STATISTICS_TABLE_CREATE);
			_db.execSQL(MembershipAdapter.MEMBERSHIP_TABLE_CREATE);
	}
	// Called when there is a database version mismatch meaning that the version
	// of the database on disk needs to be upgraded to the current version.
	@Override
	public void onUpgrade(SQLiteDatabase  _db, int _oldVersion, int _newVersion) 
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

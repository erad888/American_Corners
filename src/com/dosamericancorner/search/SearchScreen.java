package com.dosamericancorner.search;

import java.util.Calendar;

import com.dosamericancorner.login.R;
import com.dosamericancorner.membership.ManageMemberScreen;
import com.dosamericancorner.options.HelpScreen;
import com.dosamericancorner.options.InventoryOptionScreen;
import com.dosamericancorner.options.SettingScreen;
import com.dosamericancorner.reports.ReportsByDateScreen;
import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.checkout.CheckoutScreen;
import com.dosamericancorner.home.*;
import com.dosamericancorner.inventory.*;
import com.dosamericancorner.statistics.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

public class SearchScreen  extends Activity 
{
	EditText inputSearch;
	ImageButton btnEnter;
	String[] isbnArray;
	CheckOutDataBaseAdapter CheckOutDataBaseAdapter;
	InventoryAdapter InventoryAdapter;
	Spinner spnr;
	String[] menuOptions = {
			"",
			"Manage Inventory",
            "Manage Members",
            "Settings",
            "Help",
            "Sign Off"
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.search);
	     
	    // create a instance of SQLite Database
	     CheckOutDataBaseAdapter=new CheckOutDataBaseAdapter(this);
	     CheckOutDataBaseAdapter=CheckOutDataBaseAdapter.open();
	     InventoryAdapter=new InventoryAdapter(this);
	     InventoryAdapter=InventoryAdapter.open();
	     
	     Intent intent = getIntent();
	     final String userName = intent.getExtras().getString("username");
	     
		// Get References of Views
		inputSearch=(EditText)findViewById(R.id.inputSearch);
		
		
		spnr = (Spinner)findViewById(R.id.spinnerMenu);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.menu_spinner_item, menuOptions);
        spnr.setAdapter(adapter);
        spnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                            int arg2, long arg3) {
                        int position = spnr.getSelectedItemPosition();
                        if(menuOptions[position].equals("Manage Inventory"))
                        {
                        	Intent i=new Intent(SearchScreen.this,InventoryOptionScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Manage Members"))
                        {
                        	Intent i=new Intent(SearchScreen.this,ManageMemberScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Settings"))
                        {
                        	Intent i=new Intent(SearchScreen.this,SettingScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Help"))
                        {
                        	Intent i=new Intent(SearchScreen.this,HelpScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Sign Off"))
                        {
                        	Intent intentHome=new Intent(SearchScreen.this,HomeActivity.class);
        				  	startActivity(intentHome);
        				  	finish();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
            );
		
			
	    // Set OnClick Listener on Checkout button 
		btnEnter=(ImageButton)findViewById(R.id.imageButtonSearch);
		btnEnter.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			String search=inputSearch.getText().toString();
			
			// check if all of the fields are vacant
			if(search.equals(""))
			{
					Toast.makeText(getApplicationContext(), "Search Field Vaccant.", Toast.LENGTH_LONG).show();
					return;
			}
			else if(search.equals("Open-Sesame")) //=================== ONLY FOR TESTING ===================================
			{
				addExtraStuff();
			}
			else
			{
				String empty = "";
				isbnArray = com.dosamericancorner.inventory.InventoryAdapter.getSearchISBN(search);
				if(isbnArray[0].equals("not found")) {
					Toast.makeText(getApplicationContext(), "Item not found.", Toast.LENGTH_LONG).show();
					Intent i = new Intent(SearchScreen.this, InventoryAddScreen.class);
			        i.putExtra("username",userName);
			        startActivity(i);
				  	finish();
				}
				else {
				    Intent i = new Intent(SearchScreen.this, SearchResultsScreen.class);
			        i.putExtra("username",userName);
			        i.putExtra("searchQuery",search);
			        i.putExtra("isbnArray", isbnArray);
			        startActivity(i);
				  	finish();
				} 
			}
		}
	});

		
	    // ============== Bottom row of buttons ==============

	      // Home Button
	      Button bh = (Button) findViewById(R.id.btnHomeBottom);
	      bh.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
					Intent intentHome=new Intent(SearchScreen.this,HomeScreen.class);
					intentHome.putExtra("username",userName);
					startActivity(intentHome);
				  	finish();
	    		  
	    	  }
	      });
	      
	      // Search Button
	      Button bs = (Button) findViewById(R.id.btnSearchBottom);
	      bs.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		// do nothing since user is already on home screen
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(SearchScreen.this, ReportsByDateScreen.class);
	        	 i.putExtra("username",userName);
		         i.putExtra("startMonth",0);
		         i.putExtra("startYear", 0);
		         i.putExtra("endMonth", 0);
		         i.putExtra("endYear", 0);
		         startActivity(i);
				  	finish();
	         } 
	      });
	      
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	    // Close The Database
		InventoryAdapter.close();
		CheckOutDataBaseAdapter.close();
	}
	
	private void addExtraStuff()
	{
		InventoryAdapter=new InventoryAdapter(this);
	    InventoryAdapter=InventoryAdapter.open();
		
		String title = "Washington: A Life";
		String author = "Ron Chernow";
		String isbn = "0143119966";
		int date = 2011;
		String callNumber = "Biography";
		int inventoryCount = 3;
		int duePeriod = 14;
		Calendar curDate = Calendar.getInstance();
		InventoryAdapter.insertEntry(title, author, isbn, date, callNumber, inventoryCount, duePeriod);
		title = "Pond Memories";
		author = "Annie McClain";
		isbn = "1462066755";
		date = 2011;
		callNumber = "Fiction";
		inventoryCount = 2;
		duePeriod = 14;
		InventoryAdapter.insertEntry(title, author, isbn, date, callNumber, inventoryCount, duePeriod);
		title = "The Healing";
		author = "Jonathan Odell";
		isbn = "0307744566";
		date = 2011;
		callNumber = "Fiction";
		inventoryCount = 4;
		duePeriod = 14;
		InventoryAdapter.insertEntry(title, author, isbn, date, callNumber, inventoryCount, duePeriod);
		title = "Good As Gold";
		author = "Joseph Heller";
		isbn = "0684839741";
		date = 1997;
		callNumber = "Fiction";
		inventoryCount = 2;
		duePeriod = 14;
		InventoryAdapter.insertEntry(title, author, isbn, date, callNumber, inventoryCount, duePeriod);
		title = "The Round House";
		author = "Louise Erdrich";
		isbn = "0062065246";
		date = 2012;
		callNumber = "Fiction";
		inventoryCount = 3;
		duePeriod = 21;
		InventoryAdapter.insertEntry(title, author, isbn, date, callNumber, inventoryCount, duePeriod);
		title = "Ethnic America: A History";
		author = "Thomas Sowell";
		isbn = "0465020747";
		date = 1981;
		callNumber = "History";
		inventoryCount = 5;
		duePeriod = 10;
		InventoryAdapter.insertEntry(title, author, isbn, date, callNumber, inventoryCount, duePeriod);
		title = "The Last Van Gogh";
		author = "Alyson Richman";
		isbn = "042521267";
		date = 2006;
		callNumber = "Fiction";
		inventoryCount = 2;
		duePeriod = 14;
		InventoryAdapter.insertEntry(title, author, isbn, date, callNumber, inventoryCount, duePeriod);
		title = "What Saves Us";
		author = "Bruce Weigl";
		isbn = "0810150131";
		date = 2012;
		callNumber = "Poetry";
		inventoryCount = 2;
		duePeriod = 20;
		InventoryAdapter.insertEntry(title, author, isbn, date, callNumber, inventoryCount, duePeriod);
		title = "Overdue";
		author = "Overdue";
		isbn = "Overdue";
		date = 2012;
		callNumber = "Overdue";
		inventoryCount = 5;
		duePeriod = -1;
		InventoryAdapter.insertEntry(title, author, isbn, date, callNumber, inventoryCount, duePeriod);
	}
	
}

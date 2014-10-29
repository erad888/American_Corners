package com.dosamericancorner.inventory;

import java.util.Calendar;

import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.checkout.CheckoutScreen;
import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.login.R;
import com.dosamericancorner.login.R.id;
import com.dosamericancorner.login.R.layout;
import com.dosamericancorner.membership.ManageMemberScreen;
import com.dosamericancorner.options.HelpScreen;
import com.dosamericancorner.options.InventoryOptionScreen;
import com.dosamericancorner.options.SettingScreen;
import com.dosamericancorner.reports.ReportsByDateScreen;
import com.dosamericancorner.search.SearchResultsScreen;
import com.dosamericancorner.search.SearchScreen;
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
import android.widget.Toast;

public class InventoryAddScreen  extends Activity 
{
	EditText inputTitle,inputAuthor,inputISBN,inputDate,inputCallNumber,inputInventoryCount,inputDuePeriod;
	Button buttonInventoryAdd, buttonBack;
	InventoryAdapter InventoryAdapter;
	StatisticsAdapter StatisticsAdapter;
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
	     setContentView(R.layout.inventoryadd);
	     
	     // get Instance  of Database Adapter																										
	    InventoryAdapter=new InventoryAdapter(this);
	    InventoryAdapter=InventoryAdapter.open();
	    StatisticsAdapter=new StatisticsAdapter(this);
	    StatisticsAdapter=StatisticsAdapter.open();
	     
	     Intent intent = getIntent();
	     final String userName = intent.getExtras().getString("username");
	     
		// Get References of Views
		inputTitle=(EditText)findViewById(R.id.inputTitle);
		inputAuthor=(EditText)findViewById(R.id.inputAuthor);
		inputISBN=(EditText)findViewById(R.id.inputISBN);
		inputDate=(EditText)findViewById(R.id.inputDate);
		inputCallNumber=(EditText)findViewById(R.id.inputCallNumber);
		inputInventoryCount=(EditText)findViewById(R.id.inputInventoryCount);
		inputDuePeriod=(EditText)findViewById(R.id.inputDuePeriod);
		 
	     // Get The Reference Of Buttons
	     buttonInventoryAdd=(Button)findViewById(R.id.buttonInventoryAdd);
	     buttonBack=(Button)findViewById(R.id.buttonBack);
			
	    // Set OnClick Listener on Inventory button 
	    buttonInventoryAdd.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			// Get resources
			String title=inputTitle.getText().toString();
			String author=inputAuthor.getText().toString();
			String isbn=inputISBN.getText().toString();
			Integer date = null;
			Integer inventoryCount = null;
			Integer duePeriod = null;
			if(inputDate.getText().toString().length()>0)
				date=Integer.parseInt(inputDate.getText().toString());
			String callNumber=inputCallNumber.getText().toString();
			if(inputInventoryCount.getText().toString().length()>0)
				inventoryCount=Integer.parseInt(inputInventoryCount.getText().toString());
			if(inputDuePeriod.getText().toString().length()>0)
				duePeriod=Integer.parseInt(inputDuePeriod.getText().toString());
			
			// check if any of the fields are vacant 
			if(title.equals("") || author.equals("") || callNumber.equals("") || isbn.equals("")
					|| date==null || inventoryCount==null || duePeriod==null)
				{
					Toast.makeText(getApplicationContext(), "Field Vaccant.", Toast.LENGTH_LONG).show();
					return;
				}
				else
				{
					// Get due date
					Calendar curDate = Calendar.getInstance();
					Calendar due = Calendar.getInstance();
					due.add(Calendar.DATE, duePeriod);
					String curYear = ((Integer)curDate.get(Calendar.YEAR)).toString();
					String curMonth = ((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
					String curDay = ((Integer)curDate.get(Calendar.DAY_OF_MONTH)).toString();
	        		if(curDate.get(Calendar.MONTH)+1 < 10)
	        			curMonth = "0"+((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
					String checkoutDate = curYear+"-"+curMonth+"-"+curDay;
					String dueYear = ((Integer)due.get(Calendar.YEAR)).toString();
					String dueMonth = ((Integer)(due.get(Calendar.MONTH)+1)).toString();
					String dueDay = ((Integer)due.get(Calendar.DAY_OF_MONTH)).toString();
	        		if(due.get(Calendar.MONTH)+1 < 10)
	        			dueMonth = "0"+((Integer)(due.get(Calendar.MONTH)+1)).toString();
					String dueDate = dueYear+"-"+dueMonth+"-"+dueDay;
					
					// Save the Data in Database
					InventoryAdapter.insertEntry(title, author, isbn, date, callNumber, inventoryCount, duePeriod);
					StatisticsAdapter.insertEntry(isbn, curDate.get(Calendar.YEAR), (curDate.get(Calendar.MONTH)+1), 0);

					Toast.makeText(getApplicationContext(), "New Item Added Successfully.", Toast.LENGTH_LONG).show();
					
					Intent i = new Intent(InventoryAddScreen.this, InventoryAddSuccessScreen.class);
					i.putExtra("username",userName);
					i.putExtra("title",title);
					i.putExtra("author",author);
					i.putExtra("isbn",isbn);
					i.putExtra("date",date);
					i.putExtra("callNumber",callNumber);
					i.putExtra("inventoryCount",inventoryCount);
					i.putExtra("duePeriod",duePeriod);
					i.putExtra("checkoutDate",checkoutDate);
					i.putExtra("dueDate",dueDate);
					startActivity(i);
				  	finish();
				  	
				}
			}
		});
	    
	    buttonBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(InventoryAddScreen.this, SearchScreen.class);
		        i.putExtra("username",userName);
		        startActivity(i);
			  	finish();
			}
	    });
	    
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
	                        	Intent i=new Intent(InventoryAddScreen.this,InventoryOptionScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Manage Members"))
	                        {
	                        	Intent i=new Intent(InventoryAddScreen.this,ManageMemberScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Settings"))
	                        {
	                        	Intent i=new Intent(InventoryAddScreen.this,SettingScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Help"))
	                        {
	                        	Intent i=new Intent(InventoryAddScreen.this,HelpScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Sign Off"))
	                        {
	                        	Intent intentHome=new Intent(InventoryAddScreen.this,HomeActivity.class);
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
		
	    // ============== Bottom row of buttons ==============

	      // Home Button
	      Button bh = (Button) findViewById(R.id.btnHomeBottom);
	      bh.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
					Intent intentHome=new Intent(InventoryAddScreen.this,HomeScreen.class);
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
	        	 Intent i = new Intent(InventoryAddScreen.this, ReportsByDateScreen.class);
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
	}
	
}
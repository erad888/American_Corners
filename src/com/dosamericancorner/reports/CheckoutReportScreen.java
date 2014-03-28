package com.dosamericancorner.reports;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.customlistview.CheckoutItem;
import com.dosamericancorner.customlistview.CustomCheckoutAdapter;
import com.dosamericancorner.debug.debug;
import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.inventory.InventoryAdapter;
import com.dosamericancorner.login.BuildConfig;
import com.dosamericancorner.login.R;
import com.dosamericancorner.membership.ManageMemberScreen;
import com.dosamericancorner.options.HelpScreen;
import com.dosamericancorner.options.InventoryOptionScreen;
import com.dosamericancorner.options.SettingScreen;
import com.dosamericancorner.search.SearchScreen;
import com.dosamericancorner.statistics.StatisticsAdapter;

public class CheckoutReportScreen extends Activity
{
	// ADAM ADDED TEXTVIEW IDS
	TextView titleSortButton, authorSortButton, checkOutIndividualSortButton,
	memberIdSortButton, checkoutDateSortButton, dueDateSortButton;
	
	EditText inputSearch;
	ImageButton btnEnter;
	String isbn, reportStartDate, reportEndDate;
	ArrayList<CheckoutItem> reportArray = new ArrayList<CheckoutItem>();
	CheckOutDataBaseAdapter CheckOutDataBaseAdapter;
	CustomCheckoutAdapter adapter;
	StatisticsAdapter StatisticsAdapter;
	InventoryAdapter InventoryAdapter;
	String[][] Entries;
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
	     setContentView(R.layout.checkoutreport);
	     
	    // create a instance of SQLite Database
	     CheckOutDataBaseAdapter=new CheckOutDataBaseAdapter(this);
	     CheckOutDataBaseAdapter=CheckOutDataBaseAdapter.open();
	     StatisticsAdapter=new StatisticsAdapter(this);
	     StatisticsAdapter=StatisticsAdapter.open();
	     InventoryAdapter=new InventoryAdapter(this);
	     InventoryAdapter=InventoryAdapter.open();
	     
	     Intent intent = getIntent();
	     final String userName = intent.getExtras().getString("username");
	     final int type = intent.getExtras().getInt("type");
	     
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
 		reportStartDate = curYear+"-"+curMonth+"-"+curDay;
 		int startMonth = (defaultStart.get(Calendar.MONTH)+1);
 		int startYear = defaultStart.get(Calendar.YEAR);
	     
 		TextView startDateText = (TextView)findViewById(R.id.startDateText);
	    startDateText.setText(reportStartDate);
 		if(type == 2)
 		{
 			TextView checkoutReportHeader = (TextView)findViewById(R.id.checkoutReportHeader);
 			checkoutReportHeader.setText("Overdue Items as of ");
 			checkoutReportHeader.setTextColor(Color.parseColor("#ff2626"));
 			startDateText.setTextColor(Color.parseColor("#ff2626"));
 		}
	     
	     int numEntries = CheckOutDataBaseAdapter.numItemsOnLoan();
	     String[][] records = CheckOutDataBaseAdapter.getAllEntries();
	     Entries = new String[numEntries][6];
	     if (BuildConfig.DEBUG)
			    Log.d(debug.LOG, "numEntries="+numEntries);

	     String curISBN = new String();
	     
		// Set up search array
	    for(int i = 0; i < numEntries; i++)
	    {
	    	if (BuildConfig.DEBUG)
			    Log.d(debug.LOG, "records["+i+"][0]="+records[i][0]);
	    	curISBN = records[i][3];
	    	Entries[i][0] = InventoryAdapter.getTitleByISBN(curISBN);
	    	Entries[i][1] = InventoryAdapter.getAuthorByISBN(curISBN);
	    	Entries[i][2] = records[i][1];
	    	Entries[i][3] = records[i][2];
	    	Entries[i][4] = records[i][4];
	    	Entries[i][5] = records[i][5];
	    	if(type == 2)
	    	{
	    		if(Entries[i][5].compareTo(reportStartDate) < 1)
	    			reportArray.add(new CheckoutItem(curISBN,Entries[i]));
	    	}
	    	else
	    		reportArray.add(new CheckoutItem(curISBN,Entries[i]));
	    	if (BuildConfig.DEBUG)
	    	{
			    Log.d(debug.LOG, "Entries["+i+"][0]="+Entries[i][0]);
			    Log.d(debug.LOG, "Entries["+i+"][1]="+Entries[i][1]);
			    Log.d(debug.LOG, "Entries["+i+"][2]="+Entries[i][2]);
			    Log.d(debug.LOG, "Entries["+i+"][3]="+Entries[i][3]);
			    Log.d(debug.LOG, "Entries["+i+"][4]="+Entries[i][4]);
			    Log.d(debug.LOG, "Entries["+i+"][5]="+Entries[i][5]);
	    	}
	    }

	    // SORT BOOKS BUTTONS, ADAM ADDED
	    titleSortButton = (TextView) findViewById(R.id.titleSortButton);
	    authorSortButton = (TextView) findViewById(R.id.authorSortButton);
	    checkOutIndividualSortButton = (TextView) findViewById(R.id.checkOutIndividualSortButton);
	    memberIdSortButton = (TextView) findViewById(R.id.memberIdSortButton);
	    checkoutDateSortButton = (TextView) findViewById(R.id.checkoutDateSortButton);
	    dueDateSortButton = (TextView) findViewById(R.id.dueDateSortButton);
	    
		// add data in custom adapter
		adapter = new CustomCheckoutAdapter(this, R.layout.checkoutlist_row, reportArray);
        ListView dataList = (ListView) findViewById(R.id.checkoutlist_row);
        dataList.setAdapter(adapter);
        
        // ADAM ADDED CLICKABLE TEXTVIEW
        titleSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				adapter.filter(0);
				adapter.notifyDataSetChanged();
			}
	    	
	    });
	    
	    authorSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.filter(1);
				adapter.notifyDataSetChanged();
			}
	    	
	    });
	    
	    checkOutIndividualSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.filter(2);
				adapter.notifyDataSetChanged();
			}
	    	
	    });
	    
	    memberIdSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.filter(3);
				adapter.notifyDataSetChanged();
			}
	    	
	    });
	    
	    checkoutDateSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.filter(4);
				adapter.notifyDataSetChanged();
			}
	    	
	    });
	    
	    dueDateSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.filter(5);
				adapter.notifyDataSetChanged();
			}
	    	
	    });
        
        // On Click ========================================================
        dataList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	
                // Then you start a new Activity via Intent
                //Intent i = new Intent();
                //i.setClass(CheckoutReportScreen.this, CheckoutScreen.class);
                //i.putExtra("username",userName);
                //startActivity(i);
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
	                        	Intent i=new Intent(CheckoutReportScreen.this,InventoryOptionScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	                        }
	                        if(menuOptions[position].equals("Manage Members"))
	                        {
	                        	Intent i=new Intent(CheckoutReportScreen.this,ManageMemberScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	                        }
	                        if(menuOptions[position].equals("Settings"))
	                        {
	                        	Intent i=new Intent(CheckoutReportScreen.this,SettingScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	                        }
	                        if(menuOptions[position].equals("Help"))
	                        {
	                        	Intent i=new Intent(CheckoutReportScreen.this,HelpScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	                        }
	                        if(menuOptions[position].equals("Sign Off"))
	                        {
	                        	Intent intentHome=new Intent(CheckoutReportScreen.this,HomeActivity.class);
	        				  	startActivity(intentHome);
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
					Intent intentHome=new Intent(CheckoutReportScreen.this,HomeScreen.class);
					intentHome.putExtra("username",userName);
					startActivity(intentHome);
	    		  
	    	  }
	      });
	      
	      // Search Button
	      Button bs = (Button) findViewById(R.id.btnSearchBottom);
	      bs.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		  	Intent i = new Intent(CheckoutReportScreen.this, SearchScreen.class);
			        i.putExtra("username",userName);
			        startActivity(i);
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(CheckoutReportScreen.this, CheckoutReportScreen.class);
		         i.putExtra("username",userName);
		         i.putExtra("startMonth",0);
		         i.putExtra("startYear", 0);
		         i.putExtra("endMonth", 0);
		         i.putExtra("endYear", 0);
		         startActivity(i);
	         } 
	      });
	      
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	    // Close The Database
		StatisticsAdapter.close();
		CheckOutDataBaseAdapter.close();
	}
	
}
package com.dosamericancorner.search;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.checkout.CheckoutScreen;
import com.dosamericancorner.customlistview.CustomAdapter;
import com.dosamericancorner.customlistview.InventoryItem;
import com.dosamericancorner.debug.debug;
import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.inventory.InventoryAdapter;
import com.dosamericancorner.inventory.InventoryAddScreen;
import com.dosamericancorner.login.BuildConfig;
import com.dosamericancorner.login.R;
import com.dosamericancorner.membership.ManageMemberScreen;
import com.dosamericancorner.options.HelpScreen;
import com.dosamericancorner.options.InventoryOptionScreen;
import com.dosamericancorner.options.SettingScreen;
import com.dosamericancorner.reports.ReportsByDateScreen;

public class SearchResultsScreen extends Activity
{
	EditText inputSearch;
	ImageButton btnEnter;
	String isbn;
	ArrayList<InventoryItem> searchArray = new ArrayList<InventoryItem>();
    CustomAdapter adapter;
	CheckOutDataBaseAdapter CheckOutDataBaseAdapter;
	InventoryAdapter InventoryAdapter;
	String Entries[][];
	Spinner spnr;
	String[] menuOptions = {
			"",
			"Manage Inventory",
            "Manage Members",
            "Settings",
            "Help",
            "Sign Off"
    };
	
	// ADAM'S ATTRIBUTES
	int currentCount = 9;
	int newCurrentCount;
	ProgressDialog pdLoadFeed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.searchresults);
	     
	    // create a instance of SQLite Database
	     CheckOutDataBaseAdapter=new CheckOutDataBaseAdapter(this);
	     CheckOutDataBaseAdapter=CheckOutDataBaseAdapter.open();
	     InventoryAdapter=new InventoryAdapter(this);
	     InventoryAdapter=InventoryAdapter.open();
	     
	     Intent intent = getIntent();
	     final String userName = intent.getExtras().getString("username");
	     final String searchQuery = intent.getExtras().getString("searchQuery");
	     final String[] isbnArray = intent.getExtras().getStringArray("isbnArray");
	     
	     TextView searchKeyword = (TextView)findViewById(R.id.searchKeyWord);
	     searchKeyword.setText(searchQuery);
	     
	     if (BuildConfig.DEBUG)
	     {
		    Log.d(debug.LOG, "isbnArray.length="+isbnArray.length);
	     }
	     
		// Set up search array
	    // ADAM CHANGED IT TO ONLY LOAD 10 FIRST
	    Entries = new String[isbnArray.length][9];
	    searchArray = new ArrayList<InventoryItem>();
	    
	    for(int i = 0; i < currentCount; i++)
	    {
	    	if(currentCount<isbnArray.length-1) {
	    		searchArray.add(new InventoryItem(isbnArray[i], InventoryAdapter.getTitleAndAuthorByISBN(isbnArray[i])));
	    		Entries[i] = InventoryAdapter.getInventoryEntriesByISBN(searchQuery, isbnArray[i]);
	    	}
	    }

		// add data in custom adapter
		adapter = new CustomAdapter(this, R.layout.list, searchArray);
        ListView dataList = (ListView) findViewById(R.id.list);
        
        // ADAM ADDED ONSCROLL LISTENER FOR PAGINATION
        dataList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisible, int visibleCount, int totalCount) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onScrollStateChanged(AbsListView listView, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
					if(listView.getLastVisiblePosition() >= listView.getCount() - 1) {
						pdLoadFeed = ProgressDialog.show(SearchResultsScreen.this, "", "Loading books...", true, false);
						newCurrentCount = currentCount+10;
						currentCount++;
						 for(int i=currentCount; i < newCurrentCount; i++) {
						    	searchArray.add(new InventoryItem(isbnArray[i], InventoryAdapter.getTitleAndAuthorByISBN(isbnArray[i])));
						    	Entries[i] = InventoryAdapter.getInventoryEntriesByISBN(searchQuery, isbnArray[i]);
						 }
						 currentCount=newCurrentCount;
						 adapter.notifyDataSetChanged();
						 pdLoadFeed.dismiss();
					}
				}
			}
        	
        });
        
        dataList.setAdapter(adapter);
        
        // On Click ========================================================
        dataList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	String searchEntries[] = new String[9];
            	for(int a = 0; a < 9; a++)
            		searchEntries[a] = Entries[position][a];
            	
                // Then you start a new Activity via Intent
                Intent i = new Intent();
                i.setClass(SearchResultsScreen.this, CheckoutScreen.class);
                i.putExtra("username",userName);
        		i.putExtra("title",searchEntries[1]);
        		i.putExtra("author",searchEntries[2]);
        		i.putExtra("isbn",searchEntries[0]);
        		i.putExtra("date",Integer.parseInt(searchEntries[3]));
        		i.putExtra("callNumber",searchEntries[4]);
        		i.putExtra("inventoryCount",Integer.parseInt(searchEntries[5]));
        		i.putExtra("duePeriod",Integer.parseInt(searchEntries[7]));
        		i.putExtra("isbnArray", isbnArray);
        		// Get due date
        		Calendar curDate = Calendar.getInstance();
        		Calendar due = Calendar.getInstance();
        		due.add(Calendar.DATE, Integer.parseInt(searchEntries[7]));
        		String curYear = ((Integer)curDate.get(Calendar.YEAR)).toString();
        		String curMonth = ((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
        		String curDay = ((Integer)curDate.get(Calendar.DAY_OF_MONTH)).toString();
        		if(curDate.get(Calendar.MONTH)+1 < 10)
        			curMonth = "0"+((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
        		if(curDate.get(Calendar.DAY_OF_MONTH) < 10)
        			curDay = "0"+((Integer)(curDate.get(Calendar.DAY_OF_MONTH))).toString();
        		String checkoutDate = curYear+"-"+curMonth+"-"+curDay;
        		String dueYear = ((Integer)due.get(Calendar.YEAR)).toString();
        		String dueMonth = ((Integer)(due.get(Calendar.MONTH)+1)).toString();
        		String dueDay = ((Integer)due.get(Calendar.DAY_OF_MONTH)).toString();
        		if(due.get(Calendar.MONTH)+1 < 10)
        			dueMonth = "0"+((Integer)(due.get(Calendar.MONTH)+1)).toString();
        		if(due.get(Calendar.DAY_OF_MONTH) < 10)
        			dueDay = "0"+((Integer)(due.get(Calendar.DAY_OF_MONTH))).toString();
        		String dueDate = dueYear+"-"+dueMonth+"-"+dueDay;
        		i.putExtra("checkoutDate",checkoutDate);
        		i.putExtra("dueDate",dueDate);
        		i.putExtra("searchKey",searchQuery);
                startActivity(i);
			  	finish();
            }
        });
        
        // Get References of Views
     		inputSearch=(EditText)findViewById(R.id.inputSearch);
     			
     	    // Set OnClick Listener on Top Search button 
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
     			else
     			{
     				String searchKeyword = inputSearch.getText().toString();
     				String isbnSearch[] = InventoryAdapter.getSearchISBN(searchKeyword);
     				if(isbnSearch[0].equals("not found")) {
     					Toast.makeText(getApplicationContext(), "Item not found.", Toast.LENGTH_LONG).show();
     					Intent i = new Intent(SearchResultsScreen.this, InventoryAddScreen.class);
     			        i.putExtra("username",userName);
     			        startActivity(i);
    				  	finish();
     				}
     				else {
     				    Intent i = new Intent(SearchResultsScreen.this, SearchResultsScreen.class);
     			        i.putExtra("username",userName);
     			        i.putExtra("searchQuery",search);
    			        i.putExtra("isbnArray", isbnSearch);
     			        startActivity(i);
    				  	finish();
     				} 
     			}
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
	                        	Intent i=new Intent(SearchResultsScreen.this,InventoryOptionScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Manage Members"))
	                        {
	                        	Intent i=new Intent(SearchResultsScreen.this,ManageMemberScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Settings"))
	                        {
	                        	Intent i=new Intent(SearchResultsScreen.this,SettingScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Help"))
	                        {
	                        	Intent i=new Intent(SearchResultsScreen.this,HelpScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Sign Off"))
	                        {
	                        	Intent intentHome=new Intent(SearchResultsScreen.this,HomeActivity.class);
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
					Intent intentHome=new Intent(SearchResultsScreen.this,HomeScreen.class);
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
	        	 Intent i = new Intent(SearchResultsScreen.this, ReportsByDateScreen.class);
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
	
}

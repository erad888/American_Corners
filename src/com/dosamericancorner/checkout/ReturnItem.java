package com.dosamericancorner.checkout;

import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.inventory.*;
import com.dosamericancorner.login.R;
import com.dosamericancorner.membership.ManageMemberScreen;
import com.dosamericancorner.options.HelpScreen;
import com.dosamericancorner.options.InventoryOptionScreen;
import com.dosamericancorner.options.SettingScreen;
import com.dosamericancorner.reports.ReportsByDateScreen;
import com.dosamericancorner.search.SearchResultsScreen;
import com.dosamericancorner.search.SearchScreen;
import com.dosamericancorner.statistics.StatisticsAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class ReturnItem extends Activity 
{
	ImageButton btnEnter;
	EditText inputSearch, inputISBN, inputIndividual, inputMemberID;
	Button buttonReturn, buttonBack;
	InventoryAdapter InventoryAdapter;
	CheckOutDataBaseAdapter CheckOutDataBaseAdapter;
	StatisticsAdapter StatisticsAdapter;
	String isbn, individual, memberID;
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
	     setContentView(R.layout.return_item);
	     
	     // get Instance  of Database Adapter																										
	    InventoryAdapter=new InventoryAdapter(this);
	    InventoryAdapter=InventoryAdapter.open();
		CheckOutDataBaseAdapter=new CheckOutDataBaseAdapter(this);
		CheckOutDataBaseAdapter=CheckOutDataBaseAdapter.open();
		StatisticsAdapter=new StatisticsAdapter(this);
	    StatisticsAdapter=StatisticsAdapter.open();
	     
	     Intent intent = getIntent();
	     final String userName = intent.getExtras().getString("username");
	     
	     inputISBN = (EditText)findViewById(R.id.editISBN);
	     inputIndividual = (EditText)findViewById(R.id.editIndividual);
	     inputMemberID = (EditText)findViewById(R.id.editMemberID);
		 
	     // Get The Reference Of Buttons
	     buttonReturn=(Button)findViewById(R.id.buttonReturn);
	     buttonBack=(Button)findViewById(R.id.buttonBack);
	     
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
	 				String isbnSearch[] = com.dosamericancorner.inventory.InventoryAdapter.getSearchISBN(searchKeyword);
	 				if(isbnSearch[0].equals("not found")) {
	 					Toast.makeText(getApplicationContext(), "Item not found.", Toast.LENGTH_LONG).show();
	 					Intent i = new Intent(ReturnItem.this, InventoryAddScreen.class);
	 			        i.putExtra("username",userName);
	 			        startActivity(i);
    				  	finish();
	 				}
	 				else {
	 				    Intent i = new Intent(ReturnItem.this, SearchResultsScreen.class);
	 			        i.putExtra("username",userName);
	 			        i.putExtra("searchQuery",search);
				        i.putExtra("isbnArray", isbnSearch);
	 			        startActivity(i);
    				  	finish();
	 				} 
	 			}
	 		}
	 	});
			
	    // Set OnClick Listener on New Item button 
	    buttonReturn.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
				isbn = inputISBN.getText().toString();
				individual = inputIndividual.getText().toString();
				memberID = inputMemberID.getText().toString();
				if(isbn.equals("") || individual.equals("") || memberID.equals("")) {
					Toast.makeText(getApplicationContext(), "1 or More Required Inputs Missing", Toast.LENGTH_LONG).show();
				}
				else
				{
					String[] entry = CheckOutDataBaseAdapter.findCheckoutEntry(isbn, individual, memberID);
					if(entry[0].equals("Not Found"))
					{
						Toast.makeText(getApplicationContext(), "No Such Entry Found", Toast.LENGTH_LONG).show();
					}
					else
					{
						String[] item = com.dosamericancorner.inventory.InventoryAdapter.getInventoryByISBN(isbn);
						// Confirm Item Return
						Intent i = new Intent(ReturnItem.this, ReturnConfirm.class);
						i.putExtra("username",userName);
						i.putExtra("CheckoutDate", entry[0]);
						i.putExtra("dueDate", entry[1]);
						i.putExtra("isbn", isbn);
						i.putExtra("title", item[1]);
						i.putExtra("author", item[2]);
						i.putExtra("date", item[3]);
						i.putExtra("callNumber", item[4]);
						i.putExtra("checkoutIndividual", individual);
						i.putExtra("memberID", memberID);
						startActivity(i);
    				  	finish();
					}
				}
			}
		});
	    
	    buttonBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Back to Home Screen
				Intent i = new Intent(ReturnItem.this, HomeScreen.class);
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
                        	Intent i=new Intent(ReturnItem.this,InventoryOptionScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Manage Members"))
                        {
                        	Intent i=new Intent(ReturnItem.this,ManageMemberScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Settings"))
                        {
                        	Intent i=new Intent(ReturnItem.this,SettingScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Help"))
                        {
                        	Intent i=new Intent(ReturnItem.this,HelpScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Sign Off"))
                        {
                        	Intent intentHome=new Intent(ReturnItem.this,HomeActivity.class);
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
					Intent intentHome=new Intent(ReturnItem.this,HomeScreen.class);
					intentHome.putExtra("username",userName);
					startActivity(intentHome);
				  	finish();
	    		  
	    	  }
	      });
	      
	      // Search Button
	      Button bs = (Button) findViewById(R.id.btnSearchBottom);
	      bs.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for SearchScreen  and Start The Activity
					Intent i=new Intent(ReturnItem.this,SearchScreen.class);
					i.putExtra("username",userName);
					startActivity(i);
				  	finish();
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(ReturnItem.this, ReportsByDateScreen.class);
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
		StatisticsAdapter.close();
		CheckOutDataBaseAdapter.close();
		InventoryAdapter.close();
	}
	
}
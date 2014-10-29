package com.dosamericancorner.checkout;

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
import android.widget.TextView;
import android.widget.Toast;

import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.inventory.InventoryAdapter;
import com.dosamericancorner.inventory.InventoryAddScreen;
import com.dosamericancorner.login.R;
import com.dosamericancorner.membership.*;
import com.dosamericancorner.options.HelpScreen;
import com.dosamericancorner.options.InventoryOptionScreen;
import com.dosamericancorner.options.SettingScreen;
import com.dosamericancorner.reports.ReportsByDateScreen;
import com.dosamericancorner.search.SearchResultsScreen;
import com.dosamericancorner.statistics.StatisticsAdapter;

public class CheckoutScreen  extends Activity 
{
	EditText inputCheckoutIndividual, inputMemberID, inputSearch;
	Button buttonCheckoutNow, buttonBack;
	ImageButton btnEnter;
	StatisticsAdapter StatisticsAdapter;
	InventoryAdapter InventoryAdapter;
	CheckOutDataBaseAdapter CheckOutDataBaseAdapter;
	MembershipAdapter MembershipAdapter;
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
	     setContentView(R.layout.checkout);
	     
	     // get Instance  of Database Adapter	
	    StatisticsAdapter=new StatisticsAdapter(this);
		StatisticsAdapter=StatisticsAdapter.open();
	    InventoryAdapter=new InventoryAdapter(this);
	    InventoryAdapter=InventoryAdapter.open();
		CheckOutDataBaseAdapter=new CheckOutDataBaseAdapter(this);
		CheckOutDataBaseAdapter=CheckOutDataBaseAdapter.open();
		MembershipAdapter=new MembershipAdapter(this);
	    MembershipAdapter=MembershipAdapter.open();
	     
		final String previous = "Checkout";
	     Intent intent = getIntent();
	     final String userName = intent.getExtras().getString("username");
	     final String title = intent.getExtras().getString("title");
	     final String author = intent.getExtras().getString("author");
	     final String isbn = intent.getExtras().getString("isbn");
	     final int date = intent.getExtras().getInt("date");
	     final String callNumber = intent.getExtras().getString("callNumber");
	     final int inventoryCount = intent.getExtras().getInt("inventoryCount");
	     final int duePeriod = intent.getExtras().getInt("duePeriod");
	     final String checkoutDate = intent.getExtras().getString("checkoutDate");
	     final String dueDate = intent.getExtras().getString("dueDate");
	     final String searchQuery = intent.getExtras().getString("searchKey");
	     final String[] isbnArray = intent.getExtras().getStringArray("isbnArray");
	     
		// --- Get References of Views
		// Set ISBN
		TextView isbnView =(TextView)findViewById(R.id.textISBN);
		isbnView.setText(isbn);
		// Set Title
		TextView titleView =(TextView)findViewById(R.id.textTitle);
		titleView.setText(title);
		// Set Author
		TextView authorView =(TextView)findViewById(R.id.textAuthor);
		authorView.setText(author);
		// Set Publish Date
		TextView dateView =(TextView)findViewById(R.id.textPublishDate);
		dateView.setText(((Integer)date).toString());
		// Set Call Number
		TextView callNumberView =(TextView)findViewById(R.id.textCallNumber);
		callNumberView.setText(callNumber);
		// Set Inventory Count
		TextView inventoryCountView =(TextView)findViewById(R.id.textAvailable);
		inventoryCountView.setText(((Integer)inventoryCount).toString());
		// Set Due Period
		TextView duePeriodView =(TextView)findViewById(R.id.textDuePeriod);
		duePeriodView.setText(((Integer)duePeriod).toString());
		// Set Due Date
		TextView dueDateView =(TextView)findViewById(R.id.textDueDate);
		dueDateView.setText(dueDate);
		
		inputCheckoutIndividual=(EditText)findViewById(R.id.inputCheckoutIndividual);
		inputMemberID=(EditText)findViewById(R.id.inputMemberID);
		 
	    // Get The Reference Of Buttons
	    buttonCheckoutNow=(Button)findViewById(R.id.buttonCheckoutNow);
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
 					Intent i = new Intent(CheckoutScreen.this, InventoryAddScreen.class);
 			        i.putExtra("username",userName);
 			        startActivity(i);
 				}
 				else {
 				    Intent i = new Intent(CheckoutScreen.this, SearchResultsScreen.class);
 			        i.putExtra("username",userName);
 			        i.putExtra("searchQuery",search);
			        i.putExtra("isbnArray", isbnSearch);
 			        startActivity(i);
 				} 
 			}
 		}
 	});
			
	    // Set OnClick Listener on Inventory button 
	    buttonCheckoutNow.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
				// Get resources
				String checkoutIndividual = inputCheckoutIndividual.getText().toString();
				String memberID = inputMemberID.getText().toString();
				
				if(inventoryCount < 1)
				{
					Toast.makeText(getApplicationContext(), "It appears this item has 0 inventory.", Toast.LENGTH_LONG).show();
 					return;
				}
				// check if all of the fields are vacant
				else if(checkoutIndividual.equals("") || memberID.equals(""))
	 			{
	 					Toast.makeText(getApplicationContext(), "Vaccant Fields.", Toast.LENGTH_LONG).show();
	 					return;
	 			}
	 			else
	 			{
	 				if(! MembershipAdapter.isMemberExist(memberID))
	 				{
	 					Toast.makeText(getApplicationContext(), "Member ID Invalid.", Toast.LENGTH_LONG).show();
	 					return;
	 				}
	 				else if(! MembershipAdapter.isMemberMatching(checkoutIndividual, memberID))
	 				{
	 					Toast.makeText(getApplicationContext(), "Checkout Individual does not match Member ID.", Toast.LENGTH_LONG).show();
	 					return;
	 				}
	 				else
	 				{
		 				//CheckOutDataBaseAdapter.insertEntry(user, CheckoutIndividual, MemberID, ISBN, checkoutDate, dueDate)
						CheckOutDataBaseAdapter.insertEntry(userName, checkoutIndividual, memberID, isbn, checkoutDate, dueDate);
						InventoryAdapter.increaseCount(isbn);
						String[] member = MembershipAdapter.getMatchingMember(memberID);
						int checkoutCount = Integer.parseInt(member[5])+1;
						int karmaPoint = Integer.parseInt(member[6]);
						MembershipAdapter.updateEntry(member[0], member[1], member[2], memberID, member[4], checkoutCount, 
								karmaPoint, member[7]);
						StatisticsAdapter.increaseCount(isbn);
		
						Intent i = new Intent(CheckoutScreen.this, CheckoutSuccessScreen.class);
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
						i.putExtra("checkoutIndividual", checkoutIndividual);
						i.putExtra("memberID", memberID);
						i.putExtra("previous", previous);
						i.putExtra("searchQuery", searchQuery);
						i.putExtra("isbnArray", isbnArray);
						startActivity(i);
	 				}
	 			}
			}
		});
	    
	    buttonBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(CheckoutScreen.this, SearchResultsScreen.class);
		        i.putExtra("username",userName);
		        i.putExtra("searchQuery",searchQuery);
		        i.putExtra("isbnArray", isbnArray);
		        startActivity(i);
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
                        	Intent i=new Intent(CheckoutScreen.this,InventoryOptionScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Manage Members"))
                        {
                        	Intent i=new Intent(CheckoutScreen.this,ManageMemberScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Settings"))
                        {
                        	Intent i=new Intent(CheckoutScreen.this,SettingScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Help"))
                        {
                        	Intent i=new Intent(CheckoutScreen.this,HelpScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Sign Off"))
                        {
                        	Intent intentHome=new Intent(CheckoutScreen.this,HomeActivity.class);
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
					Intent intentHome=new Intent(CheckoutScreen.this,HomeScreen.class);
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
	        	 Intent i = new Intent(CheckoutScreen.this, ReportsByDateScreen.class);
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
		CheckOutDataBaseAdapter.close();
		InventoryAdapter.close();
		StatisticsAdapter.close();
		MembershipAdapter.close();
	}
}

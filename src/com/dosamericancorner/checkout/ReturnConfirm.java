package com.dosamericancorner.checkout;

import java.util.Calendar;
import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.inventory.*;
import com.dosamericancorner.checkout.CheckoutScreen;
import com.dosamericancorner.login.R;
import com.dosamericancorner.login.R.id;
import com.dosamericancorner.login.R.layout;
import com.dosamericancorner.membership.ManageMemberScreen;
import com.dosamericancorner.membership.MembershipAdapter;
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
import android.widget.TextView;
import android.widget.Toast;

public class ReturnConfirm extends Activity 
{
	ImageButton btnEnter;
	EditText inputCheckoutIndividual, inputSearch;
	Button buttonReturn, buttonBack;
	InventoryAdapter InventoryAdapter;
	CheckOutDataBaseAdapter CheckOutDataBaseAdapter;
	StatisticsAdapter StatisticsAdapter;
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
	     setContentView(R.layout.return_confirm);
	     
	     // get Instance  of Database Adapter																										
	    InventoryAdapter=new InventoryAdapter(this);
	    InventoryAdapter=InventoryAdapter.open();
		CheckOutDataBaseAdapter=new CheckOutDataBaseAdapter(this);
		CheckOutDataBaseAdapter=CheckOutDataBaseAdapter.open();
		StatisticsAdapter=new StatisticsAdapter(this);
	    StatisticsAdapter=StatisticsAdapter.open();
		MembershipAdapter=new MembershipAdapter(this);
	    MembershipAdapter=MembershipAdapter.open();
	     
	     Intent intent = getIntent();
	     final String userName = intent.getExtras().getString("username");
	     final String title = intent.getExtras().getString("title");
	     final String author = intent.getExtras().getString("author");
	     final String isbn = intent.getExtras().getString("isbn");
	     final int date = intent.getExtras().getInt("date");
	     final String callNumber = intent.getExtras().getString("callNumber");
	     final String checkoutDate = intent.getExtras().getString("checkoutDate");
	     final String dueDate = intent.getExtras().getString("dueDate");
	     final String checkoutIndividual = intent.getExtras().getString("checkoutIndividual");
	     final String memberID = intent.getExtras().getString("memberID");
	     
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
		// Set Checkout Date
		TextView checkoutDateView =(TextView)findViewById(R.id.textCheckoutDate);
		checkoutDateView.setText(checkoutDate);
		// Set Due Date
		TextView dueDateView =(TextView)findViewById(R.id.textDueDate);
		dueDateView.setText(dueDate);
		// Set Checkout Individual
		TextView checkoutIndividualView =(TextView)findViewById(R.id.textCheckoutIndividual);
		checkoutIndividualView.setText(checkoutIndividual);
		// Set Checkout Individual
		TextView memberIDView =(TextView)findViewById(R.id.textMemberID);
		memberIDView.setText(memberID);
		 
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
	 					Intent i = new Intent(ReturnConfirm.this, InventoryAddScreen.class);
	 			        i.putExtra("username",userName);
	 			        startActivity(i);
    				  	finish();
	 				}
	 				else {
	 				    Intent i = new Intent(ReturnConfirm.this, SearchResultsScreen.class);
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

			//define today's date
			Calendar curDate = Calendar.getInstance();
			String curYear = ((Integer)curDate.get(Calendar.YEAR)).toString();
			String curMonth = ((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
			String curDay = ((Integer)curDate.get(Calendar.DAY_OF_MONTH)).toString();
			if(curDate.get(Calendar.MONTH)+1 < 10)
				curMonth = "0"+((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
			if(curDate.get(Calendar.DAY_OF_MONTH) < 10)
				curDay = "0"+((Integer)(curDate.get(Calendar.DAY_OF_MONTH))).toString();
			String currentDate = curYear+"-"+curMonth+"-"+curDay;
			
				CheckOutDataBaseAdapter.deleteItem(checkoutIndividual, memberID, isbn);
				InventoryAdapter.increaseAvailable(isbn);
				StatisticsAdapter.increaseCount(isbn);
				if(dueDate.compareTo(currentDate) < 1)
					MembershipAdapter.decreaseKarma(memberID);
				else
					MembershipAdapter.increaseKarma(memberID);
				// Back to Home Screen
				Intent i = new Intent(ReturnConfirm.this, HomeScreen.class);
				i.putExtra("username",userName);
				startActivity(i);
			  	finish();
			}
		});
	    
	    buttonBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Back to Return Screen
				Intent i = new Intent(ReturnConfirm.this, ReturnItem.class);
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
                        	Intent i=new Intent(ReturnConfirm.this,InventoryOptionScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Manage Members"))
                        {
                        	Intent i=new Intent(ReturnConfirm.this,ManageMemberScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Settings"))
                        {
                        	Intent i=new Intent(ReturnConfirm.this,SettingScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Help"))
                        {
                        	Intent i=new Intent(ReturnConfirm.this,HelpScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Sign Off"))
                        {
                        	Intent intentHome=new Intent(ReturnConfirm.this,HomeActivity.class);
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
					Intent intentHome=new Intent(ReturnConfirm.this,HomeScreen.class);
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
					Intent i=new Intent(ReturnConfirm.this,SearchScreen.class);
					i.putExtra("username",userName);
					startActivity(i);
				  	finish();
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(ReturnConfirm.this, ReportsByDateScreen.class);
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
		MembershipAdapter.close();
	}
	
}
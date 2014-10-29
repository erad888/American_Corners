package com.dosamericancorner.home;

import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.checkout.CheckoutScreen;
import com.dosamericancorner.checkout.ReturnItem;
import com.dosamericancorner.reports.CheckoutReportScreen;
import com.dosamericancorner.reports.ReportsByDateScreen;
import com.dosamericancorner.search.*;
import com.dosamericancorner.statistics.StatisticsAdapter;
import com.dosamericancorner.inventory.*;
import com.dosamericancorner.login.R;
import com.dosamericancorner.membership.*;
import com.dosamericancorner.options.HelpScreen;
import com.dosamericancorner.options.InventoryOptionScreen;
import com.dosamericancorner.options.SettingScreen;

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

public class HomeScreen extends Activity {
	HomeActivity ob;
	EditText inputCheckoutIndividual, inputMemberID, inputSearch;
	Button buttonItemsOnLoanCurrently, buttonItemsOnLoan, buttonItemsOverdue, buttonNumMembers, buttonReturnItem;
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
	
	   public void onCreate(Bundle savedInstanceState)
	   {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.homescreen);
	      
		   // get Instance  of Database Adapter	
	      	MembershipAdapter=new MembershipAdapter(this);
		    MembershipAdapter=MembershipAdapter.open();
		    StatisticsAdapter=new StatisticsAdapter(this);
			StatisticsAdapter=StatisticsAdapter.open();
		    InventoryAdapter=new InventoryAdapter(this);
		    InventoryAdapter=InventoryAdapter.open();
			CheckOutDataBaseAdapter=new CheckOutDataBaseAdapter(this);
			CheckOutDataBaseAdapter=CheckOutDataBaseAdapter.open();
	      
	      Intent intent = getIntent();
	      final String userName = "Default User";
		  //TextView user =(TextView)findViewById(R.id.user);
		  //user.setText(userName);
	      
	      buttonItemsOnLoanCurrently = (Button)findViewById(R.id.btnItemsOnLoanCurrently);
	      buttonItemsOnLoan = (Button)findViewById(R.id.btnItemsOnLoan);
	      buttonItemsOverdue = (Button)findViewById(R.id.btnItemsOverdue);
	      buttonReturnItem = (Button) findViewById(R.id.btnReturnItem);
	      buttonNumMembers = (Button)findViewById(R.id.btnNumMembers);
	      
	      // Set Text
	      buttonItemsOnLoan.setText("     Items on Loan: "+CheckOutDataBaseAdapter.numItemsOnLoan());
	      System.out.println("     Items on Loan: "+CheckOutDataBaseAdapter.numItemsOnLoan());
	      buttonItemsOverdue.setText("     Items Overdue: "+CheckOutDataBaseAdapter.numOverdueItems());
	      System.out.println("     Items Overdue: "+CheckOutDataBaseAdapter.numOverdueItems());
	      buttonNumMembers.setText("     Total: "+MembershipAdapter.countMembers());
	      System.out.println("     Total: "+MembershipAdapter.countMembers());
	      
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
	                        	Intent i=new Intent(HomeScreen.this,InventoryOptionScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Manage Members"))
	                        {
	                        	Intent i=new Intent(HomeScreen.this,ManageMemberScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Settings"))
	                        {
	                        	Intent i=new Intent(HomeScreen.this,SettingScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Help"))
	                        {
	                        	Intent i=new Intent(HomeScreen.this,HelpScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Sign Off"))
	                        {
	                        	Intent intentHome=new Intent(HomeScreen.this,HomeActivity.class);
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
		  
	      // Card Buttons
          buttonItemsOnLoanCurrently.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
		        	 /// Create Intent for Items on Loan Report  and Start The Activity
				  	Intent i=new Intent(HomeScreen.this,CheckoutReportScreen.class);
				  	i.putExtra("username",userName);
				  	i.putExtra("type",1);
					startActivity(i);
				  	finish();
		         } 
	      });
	        
	      buttonItemsOnLoan.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
		        	 /// Create Intent for Items on Loan Report  and Start The Activity
				  	Intent i=new Intent(HomeScreen.this,CheckoutReportScreen.class);
				  	i.putExtra("username",userName);
				  	i.putExtra("type",1);
					startActivity(i);
				  	finish();
		         } 
	      });
	      
	      buttonItemsOverdue.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
		        	 /// Create Intent for Items Overdue Report  and Start The Activity
				  	Intent i=new Intent(HomeScreen.this,CheckoutReportScreen.class);
				  	i.putExtra("username",userName);
				  	i.putExtra("type",2);
					startActivity(i);
				  	finish();
		         } 
	      });
	      
	      buttonReturnItem.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
		        	 /// Create Intent for Return Item  and Start The Activity
				  	Intent i=new Intent(HomeScreen.this,ReturnItem.class);
				  	i.putExtra("username",userName);
					startActivity(i);
				  	finish();
		         } 
	      });
	      
	      buttonNumMembers.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
		        	 /// Create Intent for Member Table and Start The Activity
				  	Intent i=new Intent(HomeScreen.this,MemberTableScreen.class);
				  	i.putExtra("username",userName);
					startActivity(i);
				  	finish();
		         } 
	      });

	      // Home Button
	      Button bh = (Button) findViewById(R.id.btnHomeBottom);
	      bh.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		  // do nothing since user is already on home screen
	    	  }
	      });
	      
	      // Search Button
	      Button bs = (Button) findViewById(R.id.btnSearchBottom);
	      bs.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
		        /// Create Intent for SearchScreen  and Start The Activity
				Intent i=new Intent(HomeScreen.this,SearchScreen.class);
				i.putExtra("username",userName);
				startActivity(i);
			  	finish();
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(HomeScreen.this, ReportsByDateScreen.class);
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
	   
	   public void setOb( HomeActivity obA){
	    this.ob=obA;
	   }
}

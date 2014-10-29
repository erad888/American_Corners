package com.dosamericancorner.options;

import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.checkout.CheckoutScreen;
import com.dosamericancorner.reports.CheckoutReportScreen;
import com.dosamericancorner.reports.ReportsByDateScreen;
import com.dosamericancorner.search.*;
import com.dosamericancorner.statistics.StatisticsAdapter;
import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.inventory.*;
import com.dosamericancorner.login.R;
import com.dosamericancorner.membership.*;

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

public class InventoryOptionScreen extends Activity {
	EditText inputCheckoutIndividual, inputMemberID, inputSearch;
	Button buttonAddNewItem, buttonModifyItem, buttonSyncItems, buttonBackup;
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
	      setContentView(R.layout.inventory_option);
	      
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
	      final String userName = intent.getExtras().getString("username");
		  //TextView user =(TextView)findViewById(R.id.user);
		  //user.setText(userName);
	      
	      buttonAddNewItem = (Button)findViewById(R.id.btnAddNewItem);
	      buttonModifyItem = (Button)findViewById(R.id.btnModifyItem);
	      buttonSyncItems = (Button)findViewById(R.id.btnSyncInventory);
	      buttonBackup = (Button)findViewById(R.id.btnBackup);
	      
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
	                        	Intent i=new Intent(InventoryOptionScreen.this,InventoryOptionScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Manage Members"))
	                        {
	                        	Intent i=new Intent(InventoryOptionScreen.this,ManageMemberScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Settings"))
	                        {
	                        	Intent i=new Intent(InventoryOptionScreen.this,SettingScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Help"))
	                        {
	                        	Intent i=new Intent(InventoryOptionScreen.this,HelpScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Sign Off"))
	                        {
	                        	Intent intentHome=new Intent(InventoryOptionScreen.this,HomeActivity.class);
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
		  
	      buttonAddNewItem.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
					Intent i=new Intent(InventoryOptionScreen.this,InventoryAddScreen.class);
					i.putExtra("username",userName);
					startActivity(i);
				  	finish();
	    	  }
	      });
	      
	      buttonModifyItem.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
					Intent i=new Intent(InventoryOptionScreen.this,InventoryModifyScreen.class);
					i.putExtra("username",userName);
					startActivity(i);
				  	finish();
	    	  }
	      });
	      
	      buttonSyncItems.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
					Intent i=new Intent(InventoryOptionScreen.this,InventorySyncScreen.class);
					i.putExtra("username",userName);
					i.putExtra("source","0");
					startActivity(i);
				  	finish();
	    	  }
	      });
	        
	      buttonBackup.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
					Intent i=new Intent(InventoryOptionScreen.this,DataBackupScreen.class);
					i.putExtra("username",userName);
					startActivity(i);
				  	finish();
	    	  }
	      });


	      // Home Button
	      Button bh = (Button) findViewById(R.id.btnHomeBottom);
	      bh.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
					Intent i=new Intent(InventoryOptionScreen.this,HomeScreen.class);
					i.putExtra("username",userName);
					startActivity(i);
				  	finish();
	    	  }
	      });
	      
	      // Search Button
	      Button bs = (Button) findViewById(R.id.btnSearchBottom);
	      bs.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
		        /// Create Intent for SearchScreen  and Start The Activity
				Intent i=new Intent(InventoryOptionScreen.this,SearchScreen.class);
				i.putExtra("username",userName);
				startActivity(i);
			  	finish();
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(InventoryOptionScreen.this, ReportsByDateScreen.class);
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

}
package com.dosamericancorner.options;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.checkout.CheckoutScreen;
import com.dosamericancorner.customlistview.CheckoutItem;
import com.dosamericancorner.customlistview.CustomCheckoutAdapter;
import com.dosamericancorner.customlistview.CustomErrorAdapter;
import com.dosamericancorner.customlistview.ErrorItem;
import com.dosamericancorner.debug.debug;
import com.dosamericancorner.reports.CheckoutReportScreen;
import com.dosamericancorner.reports.ReportsByDateScreen;
import com.dosamericancorner.search.*;
import com.dosamericancorner.statistics.StatisticsAdapter;
import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.inventory.*;
import com.dosamericancorner.linkedlist.itemLink;
import com.dosamericancorner.login.BuildConfig;
import com.dosamericancorner.login.R;
import com.dosamericancorner.membership.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SyncSuccessScreen extends Activity {
	TextView textNumSuccess, textNumError;
	CustomErrorAdapter errorAdapter;
	ArrayList<ErrorItem> reportArray = new ArrayList<ErrorItem>();
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
	      setContentView(R.layout.sync_success);
	      
		   // get Instance  of Database Adapter	
	      	MembershipAdapter=new MembershipAdapter(this);
		    MembershipAdapter=MembershipAdapter.open();
		    StatisticsAdapter=new StatisticsAdapter(this);
			StatisticsAdapter=StatisticsAdapter.open();
		    InventoryAdapter=new InventoryAdapter(this);
		    InventoryAdapter=InventoryAdapter.open();
			CheckOutDataBaseAdapter=new CheckOutDataBaseAdapter(this);
			CheckOutDataBaseAdapter=CheckOutDataBaseAdapter.open();
	      
			// Get References of Views
		      textNumSuccess=(TextView)findViewById(R.id.textNumSuccess);
		      textNumError = (TextView)findViewById(R.id.textNumError);
			
	      Intent intent = getIntent();
	      final String userName = intent.getExtras().getString("username");
	      int numSuccess= intent.getExtras().getInt("numSuccess");
	      int numError= intent.getExtras().getInt("numError");
	      @SuppressWarnings("unchecked")
	      ArrayList<errorItem> errorList = (ArrayList<errorItem>) intent.getSerializableExtra("errorList");
		  textNumSuccess.setText("Items Successfully Synced: "+numSuccess);
		  textNumError.setText("Data Errors Found: "+numError);
	      
	      
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
	                        	Intent i=new Intent(SyncSuccessScreen.this,InventoryOptionScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Manage Members"))
	                        {
	                        	Intent i=new Intent(SyncSuccessScreen.this,ManageMemberScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Settings"))
	                        {
	                        	Intent i=new Intent(SyncSuccessScreen.this,SettingScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Help"))
	                        {
	                        	Intent i=new Intent(SyncSuccessScreen.this,HelpScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Sign Off"))
	                        {
	                        	Intent intentHome=new Intent(SyncSuccessScreen.this,HomeActivity.class);
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


		     
			// Set up array
	        String[] errors = new String[6];
		    for(int i = 0; i < errorList.size(); i++)
		    {
		    	errors = new String[6];
		    	errors[0] = errorList.get(i).getTitle();
		    	errors[1] = errorList.get(i).getAuthor();
		    	errors[2] = ((Integer)errorList.get(i).getDate()).toString();
		    	errors[3] = errorList.get(i).getISBN();
		    	errors[4] = ((Integer)errorList.get(i).getQuantity()).toString();
		    	errors[5] = errorList.get(i).getTags();
		    	reportArray.add(i, new ErrorItem(i, errors));
		    }
			// add data in custom adapter
			errorAdapter = new CustomErrorAdapter(this, R.layout.errorlist_row, reportArray);
	        ListView dataList = (ListView) findViewById(R.id.errorlist_row);
	        dataList.setAdapter(errorAdapter);
	        


	      // Home Button
	      Button bh = (Button) findViewById(R.id.btnHomeBottom);
	      bh.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
					Intent i=new Intent(SyncSuccessScreen.this,HomeScreen.class);
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
				Intent i=new Intent(SyncSuccessScreen.this,SearchScreen.class);
				i.putExtra("username",userName);
				startActivity(i);
			  	finish();
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(SyncSuccessScreen.this, ReportsByDateScreen.class);
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
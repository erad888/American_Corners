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
import com.dosamericancorner.options.InventoryOptionScreen;

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

public class SettingScreen extends Activity {
	EditText inputCheckoutIndividual, inputMemberID, inputSearch;
	Button buttonAddNewUser, buttonModifyUser, buttonAbout;
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
	      setContentView(R.layout.setting_option);
	      
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
	      
	      buttonAddNewUser = (Button)findViewById(R.id.btnAddNewUser);
	      buttonModifyUser = (Button)findViewById(R.id.btnModifyUser);
	      buttonAbout = (Button)findViewById(R.id.btnAbout);
	      
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
	                        	Intent i=new Intent(SettingScreen.this,InventoryOptionScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	                        }
	                        if(menuOptions[position].equals("Manage Members"))
	                        {
	                        	Intent i=new Intent(SettingScreen.this,ManageMemberScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	                        }
	                        if(menuOptions[position].equals("Settings"))
	                        {
	                        	Intent i=new Intent(SettingScreen.this,SettingScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	                        }
	                        if(menuOptions[position].equals("Help"))
	                        {
	                        	Intent i=new Intent(SettingScreen.this,HelpScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	                        }
	                        if(menuOptions[position].equals("Sign Off"))
	                        {
	                        	Intent intentHome=new Intent(SettingScreen.this,HomeActivity.class);
	        				  	startActivity(intentHome);
	                        }
	                    }
	                    @Override
	                    public void onNothingSelected(AdapterView<?> arg0) {
	                        // TODO Auto-generated method stub
	                    }
	                }
	            );
		  
	      buttonAddNewUser.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
					Intent i=new Intent(SettingScreen.this,InventoryAddScreen.class);
					i.putExtra("username",userName);
					startActivity(i);
	    	  }
	      });
	        


	      // Home Button
	      Button bh = (Button) findViewById(R.id.btnHomeBottom);
	      bh.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
					Intent i=new Intent(SettingScreen.this,HomeScreen.class);
					i.putExtra("username",userName);
					startActivity(i);
	    	  }
	      });
	      
	      // Search Button
	      Button bs = (Button) findViewById(R.id.btnSearchBottom);
	      bs.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
		        /// Create Intent for SearchScreen  and Start The Activity
				Intent i=new Intent(SettingScreen.this,SearchScreen.class);
				i.putExtra("username",userName);
				startActivity(i);
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(SettingScreen.this, ReportsByDateScreen.class);
	        	 i.putExtra("username",userName);
		         i.putExtra("startMonth",0);
		         i.putExtra("startYear", 0);
		         i.putExtra("endMonth", 0);
		         i.putExtra("endYear", 0);
		         startActivity(i);
	         } 
	      });
	   }

}
package com.dosamericancorner.membership;

import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.inventory.InventoryAdapter;
import com.dosamericancorner.login.R;
import com.dosamericancorner.options.HelpScreen;
import com.dosamericancorner.options.InventoryOptionScreen;
import com.dosamericancorner.options.SettingScreen;
import com.dosamericancorner.reports.ReportsByDateScreen;
import com.dosamericancorner.search.SearchScreen;
import com.dosamericancorner.statistics.StatisticsAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class MemberEdit extends Activity{
	TextView textType;
	EditText inputFirstName,inputLastName,inputBirthday,inputMemberId,inputEmail,inputNotes;
	Button buttonMemberAdd, buttonBack;
	MembershipAdapter MembershipAdapter;
	String FirstName, LastName, Birthday, MemberID, Email, Notes;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memberadd);
		
		MembershipAdapter=new MembershipAdapter(this);
	    MembershipAdapter=MembershipAdapter.open();
		
		Intent intent = getIntent();
	    final String userName = intent.getExtras().getString("username");
	    FirstName = intent.getExtras().getString("FirstName");
	    LastName = intent.getExtras().getString("LastName");
	    Birthday = intent.getExtras().getString("Birthday");
	    MemberID = intent.getExtras().getString("MemberID");
	    Email = intent.getExtras().getString("Email");
	    Notes = intent.getExtras().getString("Notes");
	    final int NumCheckouts = intent.getExtras().getInt("NumCheckouts");
	    final int KarmaPts = intent.getExtras().getInt("KarmaPts");
		
	    textType = (TextView)findViewById(R.id.textType);
	    textType.setText("Edit Existing Member");
	    
	    buttonMemberAdd = (Button)findViewById(R.id.buttonMemberAdd);
	    buttonBack = (Button)findViewById(R.id.buttonBack);
	    
	    buttonMemberAdd.setText("Edit Member");
	    
	    inputFirstName = (EditText)findViewById(R.id.inputFirstName);
	    inputLastName = (EditText)findViewById(R.id.inputLastName);
	    inputBirthday = (EditText)findViewById(R.id.inputBirthday);
	    inputMemberId = (EditText)findViewById(R.id.inputMemberId);
	    inputEmail = (EditText)findViewById(R.id.inputEmail);
	    inputNotes = (EditText)findViewById(R.id.inputNotes);
	    
	    inputFirstName.setText(FirstName);
	    inputLastName.setText(LastName);
	    inputBirthday.setText(Birthday);
	    inputMemberId.setText(MemberID);
	    inputEmail.setText(Email);
	    inputNotes.setText(Notes);
		
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
                        	Intent i=new Intent(MemberEdit.this,InventoryOptionScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Manage Members"))
                        {
                        	Intent i=new Intent(MemberEdit.this,ManageMemberScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Settings"))
                        {
                        	Intent i=new Intent(MemberEdit.this,SettingScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Help"))
                        {
                        	Intent i=new Intent(MemberEdit.this,HelpScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Sign Off"))
                        {
                        	Intent intentHome=new Intent(MemberEdit.this,HomeActivity.class);
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
        
        buttonMemberAdd.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		  FirstName = inputFirstName.getText().toString();
	    		  LastName = inputLastName.getText().toString();
	    		  Birthday = inputBirthday.getText().toString();
	    		  Email = inputEmail.getText().toString();
	    		  Notes = inputNotes.getText().toString();
	    		  
	    		  if(FirstName.equals("") || LastName.equals("") || Birthday.equals("") || Email.equals(""))
	    		  {
						Toast.makeText(getApplicationContext(), "Field Vaccant.", Toast.LENGTH_LONG).show();
						return;
	    		  }
	    		  else
	    		  {
	    			  
	    			// Alert Dialog to confirm Return All Items
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								MemberEdit.this);
				 
							// set title
							alertDialogBuilder.setTitle("Confirm Edit");
				 
							// set dialog message
							alertDialogBuilder
								.setMessage("Note that you cannot modify Member ID.")
								.setCancelable(false)
								.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// if this button is clicked, return all current items
										MembershipAdapter.updateEntry(FirstName, LastName, Birthday, MemberID, Email, NumCheckouts, KarmaPts, Notes);
							    		  Toast.makeText(MemberEdit.this, "Member Successfully Modified", Toast.LENGTH_LONG).show();
							    		  
							    		  // Then you start a new Activity via Intent
						                    Intent i = new Intent();
						                    i.setClass(MemberEdit.this, MemberDetails.class);
						                    i.putExtra("username",userName);
						                    i.putExtra("FirstName",FirstName);
						                    i.putExtra("LastName",LastName);
						                    i.putExtra("Birthday",Birthday);
						                    i.putExtra("MemberID",MemberID);
						                    i.putExtra("Email",Email);
						                    i.putExtra("Notes",Notes);
						                    i.putExtra("NumCheckouts",NumCheckouts); // type int
						                    i.putExtra("KarmaPts",KarmaPts); // type int
						                    startActivity(i);
				        				  	finish();
						                    dialog.cancel();
									}
								  })
								.setNegativeButton("No",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// if this button is clicked, just close the dialog box and do nothing
										dialog.cancel();
									}
								});
				 
								// create alert dialog
								AlertDialog alertDialog = alertDialogBuilder.create();
				 
								// show dialog
								alertDialog.show();
	    			  
		    		  
	    		  }
	    	  }
	      });
        
     // ============== Bottom row of buttons ==============

	      // Home Button
	      Button bh = (Button) findViewById(R.id.btnHomeBottom);
	      bh.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
					Intent intentHome=new Intent(MemberEdit.this,HomeScreen.class);
					intentHome.putExtra("username",userName);
					startActivity(intentHome);
				  	finish();
	    		  
	    	  }
	      });
	      
	      // Search Button
	      Button bs = (Button) findViewById(R.id.btnSearchBottom);
	      bs.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		  	Intent i = new Intent(MemberEdit.this, SearchScreen.class);
			        i.putExtra("username",userName);
			        startActivity(i);
				  	finish();
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(MemberEdit.this, ReportsByDateScreen.class);
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
		// TODO Auto-generated method stub
		super.onDestroy();
		MembershipAdapter.close();
	}

}

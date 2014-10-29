package com.dosamericancorner.membership;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.googlecode.jcsv.CSVStrategy;
//import au.com.bytecode.opencsv.CSVReader;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.CSVEntryParser;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;

import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.login.R;
import com.dosamericancorner.options.HelpScreen;
import com.dosamericancorner.options.InventoryOptionScreen;
import com.dosamericancorner.options.InventorySyncScreen;
import com.dosamericancorner.options.SettingScreen;
import com.dosamericancorner.options.SyncSuccessScreen;
import com.dosamericancorner.options.errorItem;
import com.dosamericancorner.options.InventorySyncScreen.EntryParser;
import com.dosamericancorner.reports.ReportsByDateScreen;
import com.dosamericancorner.search.SearchScreen;

public class MemberSyncScreen extends Activity{
	private ProgressDialog progressDialog;
	EditText inputSource;
	Button buttonSyncMember, buttonNewSource;
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
	
	String[][] addNew(String[][] oldString, String[] newString)
	{
		String[][] tempString = Arrays.copyOf(oldString, oldString.length + 1); // New array with row size of old array + 1

		tempString[oldString.length] = new String[oldString.length]; // Initializing the new row

		// Copying data from newString array to the tempString array
		for (int i = 0; i < newString.length; i++) {
		    tempString[tempString.length-1][i] = newString[i];
		}
		
		return tempString;
	}
	
	//EntryParser
	   public class EntryParser implements CSVEntryParser<String[]> {
	        public String[] parseEntry(String... data) {
	            return data;
	        }
	    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_sync);
		
		MembershipAdapter = new MembershipAdapter(this);
		MembershipAdapter = MembershipAdapter.open();
		
		Intent intent = getIntent();
	      final String userName = intent.getExtras().getString("username");
	      String source = intent.getExtras().getString("source");
		  TextView sourceText =(TextView)findViewById(R.id.textMemberLocation);
		  if(source.equals("0"))
			  source = "/Download/member.csv";
		  sourceText.setText("Please ensure the CSV file containing your member data is located in "+source);
		  final String location = source;
		  
		  // Get References of Views
	      inputSource=(EditText)findViewById(R.id.textNewSource);
	      buttonSyncMember = (Button)findViewById(R.id.btnSyncMembers);
	      buttonNewSource = (Button)findViewById(R.id.btnNewSource);
	      
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
	                        	Intent i=new Intent(MemberSyncScreen.this,InventoryOptionScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Manage Members"))
	                        {
	                        	Intent i=new Intent(MemberSyncScreen.this,ManageMemberScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Settings"))
	                        {
	                        	Intent i=new Intent(MemberSyncScreen.this,SettingScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Help"))
	                        {
	                        	Intent i=new Intent(MemberSyncScreen.this,HelpScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Sign Off"))
	                        {
	                        	Intent intentHome=new Intent(MemberSyncScreen.this,HomeActivity.class);
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
	        
	        buttonSyncMember.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ArrayList<errorItem> errorList = new ArrayList<errorItem>();
		    		int successCounter = 0;
		    		  int errorCounter = 0;
		    		  String[][] errors = new String[1][8];
		    		  String[] line = new String[8];
		    		  try {
		    			  int firstNameCol = -1, lastNameCol = -1, birthdayCol = -1, memberIdCol = -1, emailCol = -1, checkoutCountCol = -1, karmaPtsCol=-1, notesCol=-1;
			    		  //CSVReader reader = new CSVReader(new FileReader(new File(Environment.getExternalStorageDirectory().getPath()+location)));
			    		  
			    		  InputStream is = new ByteArrayInputStream((Environment.getExternalStorageDirectory().getPath()+location).getBytes());
			  	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
			  	        CSVReader<String[]> reader = new CSVReaderBuilder<String[]>(
			  	                br)
			  	                .strategy(new CSVStrategy('\t', '\b', '#', true, true))
			  	                .entryParser(new EntryParser()).build();
			    		  
			    		  String [] nextLine;
			    		    nextLine = reader.readNext();
			    		    int index = 0;
			    		    int lineLength = nextLine.length;
			    		    while(firstNameCol < 0 && index < lineLength)
			    		    {
			    		    	if(nextLine[index].contains("FIRSTNAME"))
			    		    		firstNameCol = index;
			    		    	index++;
			    		    }
			    		    index = 0;
			    		    while(lastNameCol < 0 && index < lineLength)
			    		    {
			    		    	if(nextLine[index].contains("LASTNAME"))
			    		    		lastNameCol = index;
			    		    	index++;
			    		    }
			    		    index = 0;
			    		    while(birthdayCol < 0 && index < lineLength)
			    		    {
			    		    	if(nextLine[index].contains("BIRTHDAY"))
			    		    		birthdayCol = index;
			    		    	index++;
			    		    }
			    		    index = 0;
			    		    while(memberIdCol < 0 && index < lineLength)
			    		    {
			    		    	if(nextLine[index].contains("MEMBERID"))
			    		    		memberIdCol = index;
			    		    	index++;
			    		    }
			    		    index = 0;
			    		    while(emailCol < 0 && index < lineLength)
			    		    {
			    		    	if(nextLine[index].contains("EMAIL"))
			    		    		emailCol = index;
			    		    	index++;
			    		    }
			    		    index = 0;
			    		    while(checkoutCountCol < 0 && index < lineLength)
			    		    {
			    		    	if(nextLine[index].contains("CHECKOUTCOUNT"))
			    		    		checkoutCountCol = index;
			    		    	index++;
			    		    }
			    		    index = 0;
			    		    while(karmaPtsCol < 0 && index < lineLength)
			    		    {
			    		    	if(nextLine[index].contains("KARMAPTS"))
			    		    		karmaPtsCol = index;
			    		    	index++;
			    		    }
			    		    index = 0;
			    		    while(notesCol < 0 && index < lineLength)
			    		    {
			    		    	if(nextLine[index].contains("NOTES"))
			    		    		notesCol = index;
			    		    	index++;
			    		    }
			    		    int counter = 0;
			    		    while ((nextLine = reader.readNext()) != null) {
			    		    	 // nextLine[] is an array of values from the line
			    		    	/*
			    		    	 * nextLine[firstNameCol] = FIRSTNAME
			    		    	 * nextLine[lastNameCol] = LASTNAME
			    		    	 * nextLine[birthdayCol] = BIRTHDAY
			    		    	 * nextLine[memberIdCol] = MEMBERID
			    		    	 * nextLine[emailCol] = EMAIL
			    		    	 * nextLine[checkoutCountCol] = CHECKOUTCOUNT
			    		    	 * nextLine[karmaPtsCol] = KARMAPTS
			    		    	 * nextLine[notesCol] = NOTES
			    		    	 */
			    		    	lineLength = nextLine.length;
			    		    	if(nextLine[memberIdCol].contains("0") || nextLine[memberIdCol].contains("1") 
				    		    				|| nextLine[memberIdCol].contains("2") || nextLine[memberIdCol].contains("3")
				    		    				|| nextLine[memberIdCol].contains("4") || nextLine[memberIdCol].contains("5")
				    		    				|| nextLine[memberIdCol].contains("6") || nextLine[memberIdCol].contains("7")
				    		    				|| nextLine[memberIdCol].contains("8") || nextLine[memberIdCol].contains("9"))
					    		    	{
			    		    				if(MembershipAdapter.memberIdFound(nextLine[memberIdCol])== false) {
			    		    					if(firstNameCol < lineLength && nextLine[firstNameCol].length() > 0)
			    		    						line[0] = nextLine[firstNameCol];
			    		    					else
			    		    						line[0] = "*ERROR*";
			    		    					if(lastNameCol < lineLength && nextLine[lastNameCol].length() > 0)
			    		    						line[1] = nextLine[lastNameCol];
			    		    					else
			    		    						line[1] = "*ERROR*";
			    		    					if(birthdayCol < lineLength && nextLine[birthdayCol].length() > 0)
			    		    						line[2] = nextLine[birthdayCol];
			    		    					else
			    		    						line[2] = "*ERROR*";
			    		    					if(memberIdCol < lineLength && nextLine[emailCol].length() > 0)
						    		    			line[3] = nextLine[memberIdCol];
			    		    					else
			    		    						line[3] = "*ERROR*";
			    		    					if(emailCol < lineLength && nextLine[emailCol].length() > 0)
			    		    						line[4] = nextLine[emailCol];
			    		    					else
			    		    						line[4] = "*ERROR*";
			    		    					if(nextLine[checkoutCountCol].contains("0") || nextLine[checkoutCountCol].contains("1") 
						    		    				|| nextLine[checkoutCountCol].contains("2") || nextLine[checkoutCountCol].contains("3")
						    		    				|| nextLine[checkoutCountCol].contains("4") || nextLine[checkoutCountCol].contains("5")
						    		    				|| nextLine[checkoutCountCol].contains("6") || nextLine[checkoutCountCol].contains("7")
						    		    				|| nextLine[checkoutCountCol].contains("8") || nextLine[checkoutCountCol].contains("9"))
						    		    			line[5] = nextLine[checkoutCountCol];
			    		    					else
			    		    						line[5] = "-9999";
			    		    					if(nextLine[karmaPtsCol].contains("0") || nextLine[karmaPtsCol].contains("1") 
						    		    				|| nextLine[karmaPtsCol].contains("2") || nextLine[karmaPtsCol].contains("3")
						    		    				|| nextLine[karmaPtsCol].contains("4") || nextLine[karmaPtsCol].contains("5")
						    		    				|| nextLine[karmaPtsCol].contains("6") || nextLine[karmaPtsCol].contains("7")
						    		    				|| nextLine[karmaPtsCol].contains("8") || nextLine[karmaPtsCol].contains("9"))
						    		    			line[6] = nextLine[karmaPtsCol];
			    		    					else
			    		    						line[6] = "-9999";
			    		    					if(notesCol < lineLength && nextLine[notesCol].length() > 0)
			    		    						line[7] = nextLine[notesCol];
			    		    					else
			    		    						line[7] = "*ERROR*";
			    		    					
			    		    					if(line[0].equals("*ERROR*") || line[1].equals("*ERROR*")
						    		    				|| line[3].equals("-9999")|| line[5].equals("-9999")
						    		    				|| line[2].equals("*ERROR*")|| line[4].equals("*ERROR*")
						    		    				|| line[6].equals("-9999")|| line[7].equals("*ERROR*"))
			    		    						
						    		    		{
						    		    			errorList.add(new errorItem(line[0], line[1], Integer.parseInt(line[2]), 
						    		    					line[3], Integer.parseInt(line[4]), line[5]));
						    		    			errorCounter++;
						    		    		}
						    		    		else
						    		    		{
						    		    			MembershipAdapter.insertEntry(line[0], line[1], line[2], line[3], line[4],
						    		    					Integer.parseInt(line[5]), Integer.parseInt(line[6]), line[7]);
						    		    			successCounter++;
						    		    		}
			    		    				}
					    		    	}
			    		    	counter++;
			    		    }
		    		  } catch (FileNotFoundException e) {
		    			  e.printStackTrace();
		    		  } catch (IOException e) {
		    			  e.printStackTrace();
		    		  }
		    		  
		    		  progressDialog.dismiss();
		    		  Intent i = new Intent(MemberSyncScreen.this, SyncSuccessScreen.class);
		    		  i.putExtra("username",userName);
						i.putExtra("numSuccess",successCounter);
						i.putExtra("numError", errorCounter);
						i.putExtra("errorList", errorList);
						startActivity(i);
    				  	finish();
				}
	  
	        });
	        
	        buttonNewSource.setOnClickListener(new View.OnClickListener() {
		    	  public void onClick(View arg0) {
		    		  	String source=inputSource.getText().toString();
		   			
						// check if all of the fields are vacant
						if(source.equals(""))
						{
								Toast.makeText(getApplicationContext(), "Source Field Vaccant.", Toast.LENGTH_LONG).show();
								return;
						}
						else
						{
							/// Create Intent for HomeScreen  and Start The Activity
							Intent i=new Intent(MemberSyncScreen.this,MemberSyncScreen.class);
							i.putExtra("username",userName);
							i.putExtra("source",source);
							startActivity(i);
        				  	finish();
						}
		    	  }
		      });
	        // Home Button
		      Button bh = (Button) findViewById(R.id.btnHomeBottom);
		      bh.setOnClickListener(new View.OnClickListener() {
		    	  public void onClick(View arg0) {
		    		/// Create Intent for HomeScreen  and Start The Activity
						Intent i=new Intent(MemberSyncScreen.this,HomeScreen.class);
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
					Intent i=new Intent(MemberSyncScreen.this,SearchScreen.class);
					i.putExtra("username",userName);
					startActivity(i);
				  	finish();
			         } 
		      });
		      
		      // Reports Button
		      Button br = (Button) findViewById(R.id.btnReportsBottom);
		      br.setOnClickListener(new View.OnClickListener() {
		         public void onClick(View arg0) {
		        	 Intent i = new Intent(MemberSyncScreen.this, ReportsByDateScreen.class);
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
	
	



package com.dosamericancorner.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.dosamericancorner.customlistview.CheckoutItem;
import com.dosamericancorner.customlistview.CustomCheckoutAdapter;
import com.dosamericancorner.debug.debug;
import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.inventory.InventoryAdapter;
import com.dosamericancorner.inventory.InventoryEntryConverter;
import com.dosamericancorner.login.BuildConfig;
import com.dosamericancorner.login.R;
import com.dosamericancorner.membership.ItemManagement;
import com.dosamericancorner.membership.ManageMemberScreen;
import com.dosamericancorner.membership.MemberDetails;
import com.dosamericancorner.membership.MemberEdit;
import com.dosamericancorner.membership.MembershipAdapter;
import com.dosamericancorner.options.DataBackupScreen;
import com.dosamericancorner.options.HelpScreen;
import com.dosamericancorner.options.InventoryOptionScreen;
import com.dosamericancorner.options.SettingScreen;
import com.dosamericancorner.search.SearchScreen;
import com.dosamericancorner.statistics.StatisticsAdapter;
import com.googlecode.jcsv.writer.CSVWriter;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;

public class CheckoutReportScreen extends Activity
{
	private ProgressDialog progressDialog;
	// ADAM ADDED TEXTVIEW IDS
	TextView titleSortButton, authorSortButton, checkOutIndividualSortButton,
	memberIdSortButton, checkoutDateSortButton, dueDateSortButton;
	
	EditText inputSearch;
	Button export;
	String isbn, reportStartDate, reportEndDate;
	ArrayList<CheckoutItem> reportArray = new ArrayList<CheckoutItem>();
	CheckOutDataBaseAdapter CheckOutDataBaseAdapter;
	CustomCheckoutAdapter adapter;
	StatisticsAdapter StatisticsAdapter;
	InventoryAdapter InventoryAdapter;
	MembershipAdapter MembershipAdapter;
	String[][] Entries;
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
	     setContentView(R.layout.checkoutreport);
	     
	    // create a instance of SQLite Database
	     CheckOutDataBaseAdapter=new CheckOutDataBaseAdapter(this);
	     CheckOutDataBaseAdapter=CheckOutDataBaseAdapter.open();
	     StatisticsAdapter=new StatisticsAdapter(this);
	     StatisticsAdapter=StatisticsAdapter.open();
	     InventoryAdapter=new InventoryAdapter(this);
	     InventoryAdapter=InventoryAdapter.open();
	     MembershipAdapter=new MembershipAdapter(this);
		 MembershipAdapter=MembershipAdapter.open();
	     
	     Intent intent = getIntent();
	     final String userName = intent.getExtras().getString("username");
	     final int type = intent.getExtras().getInt("type");
	     
	    //define Report Start date
 		Calendar curDate = Calendar.getInstance();
 		Calendar defaultStart = Calendar.getInstance();
 		defaultStart.set(curDate.get(Calendar.YEAR), curDate.get(Calendar.MONTH)-1, curDate.get(Calendar.DAY_OF_MONTH));
 		String curYear = ((Integer)curDate.get(Calendar.YEAR)).toString();
 		String curMonth = ((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
 		String curDay = ((Integer)curDate.get(Calendar.DAY_OF_MONTH)).toString();
 		if(curDate.get(Calendar.MONTH)+1 < 10)
 			curMonth = "0"+((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
 		if(curDate.get(Calendar.DAY_OF_MONTH) < 10)
 			curDay = "0"+((Integer)(curDate.get(Calendar.DAY_OF_MONTH))).toString();
 		reportStartDate = curYear+"-"+curMonth+"-"+curDay;
 		final int startMonth = (defaultStart.get(Calendar.MONTH)+1);
 		final int startYear = defaultStart.get(Calendar.YEAR);
	     
 		TextView startDateText = (TextView)findViewById(R.id.startDateText);
	    startDateText.setText(reportStartDate);
 		if(type == 2)
 		{
 			TextView checkoutReportHeader = (TextView)findViewById(R.id.checkoutReportHeader);
 			checkoutReportHeader.setText("Overdue Items as of ");
 			checkoutReportHeader.setTextColor(Color.parseColor("#ff2626"));
 			startDateText.setTextColor(Color.parseColor("#ff2626"));
 		}
	     
	     int numEntries = CheckOutDataBaseAdapter.numItemsOnLoan();
	     final String[][] records = CheckOutDataBaseAdapter.getAllEntries();
	     Entries = new String[numEntries][6];
	     if (BuildConfig.DEBUG)
			    Log.d(debug.LOG, "numEntries="+numEntries);

	     String curISBN = new String();
	     
		// Set up search array
	    for(int i = 0; i < numEntries; i++)
	    {
	    	if (BuildConfig.DEBUG)
			    Log.d(debug.LOG, "records["+i+"][0]="+records[i][0]);
	    	curISBN = records[i][3];
	    	Entries[i][0] = InventoryAdapter.getTitleByISBN(curISBN);
	    	Entries[i][1] = InventoryAdapter.getAuthorByISBN(curISBN);
	    	Entries[i][2] = records[i][1];
	    	Entries[i][3] = records[i][2];
	    	Entries[i][4] = records[i][4];
	    	Entries[i][5] = records[i][5];
	    	if(type == 2)
	    	{
	    		if(Entries[i][5].compareTo(reportStartDate) < 1)
	    			reportArray.add(new CheckoutItem(curISBN,Entries[i]));
	    	}
	    	else
	    		reportArray.add(new CheckoutItem(curISBN,Entries[i]));
	    	if (BuildConfig.DEBUG)
	    	{
			    Log.d(debug.LOG, "Entries["+i+"][0]="+Entries[i][0]);
			    Log.d(debug.LOG, "Entries["+i+"][1]="+Entries[i][1]);
			    Log.d(debug.LOG, "Entries["+i+"][2]="+Entries[i][2]);
			    Log.d(debug.LOG, "Entries["+i+"][3]="+Entries[i][3]);
			    Log.d(debug.LOG, "Entries["+i+"][4]="+Entries[i][4]);
			    Log.d(debug.LOG, "Entries["+i+"][5]="+Entries[i][5]);
	    	}
	    }
	    
	    export = (Button)findViewById(R.id.saveReport);

	    // SORT BOOKS BUTTONS, ADAM ADDED
	    titleSortButton = (TextView) findViewById(R.id.titleSortButton);
	    authorSortButton = (TextView) findViewById(R.id.authorSortButton);
	    checkOutIndividualSortButton = (TextView) findViewById(R.id.checkOutIndividualSortButton);
	    memberIdSortButton = (TextView) findViewById(R.id.memberIdSortButton);
	    checkoutDateSortButton = (TextView) findViewById(R.id.checkoutDateSortButton);
	    dueDateSortButton = (TextView) findViewById(R.id.dueDateSortButton);
	    
		// add data in custom adapter
		adapter = new CustomCheckoutAdapter(this, R.layout.checkoutlist_row, reportArray);
        ListView dataList = (ListView) findViewById(R.id.checkoutlist_row);
        dataList.setAdapter(adapter);
        
        // Export data
        export.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		// Alert Dialog to confirm Export
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						CheckoutReportScreen.this);
		 
					// set title
					alertDialogBuilder.setTitle("Confirm");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage("Export Current Report?")
						.setCancelable(false)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// get user input and set it to result
								// create new CSV and send CSV via email
							    	 progressDialog = ProgressDialog.show(CheckoutReportScreen.this, "", "Loading...");
						    		  File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"americancorners");
						              if(!fileDir.exists()){
						    			try{
						    				fileDir.mkdir();
						    			} catch (Exception e) {
						    				e.printStackTrace();
						    			}
						              }
						              
						           // === Backup Checkouts to CSV
						              File cfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"americancorners"+File.separator+"checkout_"+startMonth+"-"+startYear+"-v1"+".csv");
						              if(!cfile.exists()){
						    			try {
						    				cfile.createNewFile();
						    				// Write to New File
						    		    	  try {
						    		    		  Writer out = new FileWriter(cfile);
						    		    		  CSVWriter<String[]> writer = new CSVWriterBuilder<String[]>(out).entryConverter(new InventoryEntryConverter()).build();
						    		    		  writer.write(new String[]{"'TITLE'","'AUTHOR'","'CHECKOUT_INDIVIDUAL'","'MEMBER_ID'","'CHECKOUT_DATE'","'DUE_DATE'"});
						    		    		  for(int j = 0; j < records.length; j++)
						    		    		  {
						    		    			  writer.write(records[j]);
						    		    		  }
						    		    		  writer.close();
						    				  } catch (FileNotFoundException e) {
						    					  // TODO Auto-generated catch block
						    					  e.printStackTrace();
						    				  } catch (IOException e) {
						    					// TODO Auto-generated catch block
						    					e.printStackTrace();
						    				  }
						    			} catch (IOException e) {
						    				e.printStackTrace();
						    			}
						              }
						              else
						              {
						            	  int count = 2;
							              // Make new version
						            	  File cfileNew = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"americancorners"+File.separator+"checkout_"+startMonth+"-"+startYear+"-v"+count+".csv");
								    	  // Make New File
						            	  while(cfileNew.exists())
					            		  {
						            		  count++;
						            		  cfileNew = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"americancorners"+File.separator+"checkout_"+startMonth+"-"+startYear+"-v"+count+".csv");
					            		  }
						            	  if(!cfileNew.exists()) {
									  			try {
									  				cfileNew.createNewFile();
									  			} catch (IOException e) {
									  				e.printStackTrace();
									  			}
								    	  }
								    	  // Write to New File
								    	  try {
								    		  Writer out = new FileWriter(cfileNew);
					    		    		  CSVWriter<String[]> writer = new CSVWriterBuilder<String[]>(out).entryConverter(new InventoryEntryConverter()).build();
					    		    		  writer.write(new String[]{"'TITLE'","'AUTHOR'","'CHECKOUT_INDIVIDUAL'","'MEMBER_ID'","'CHECKOUT_DATE'","'DUE_DATE'"});
					    		    		  for(int j = 0; j < records.length; j++)
					    		    		  {
					    		    			  writer.write(records[j]);
					    		    		  }
					    		    		  writer.close();
					    		    		  cfile = cfileNew;
										  } catch (FileNotFoundException e) {
											  // TODO Auto-generated catch block
											  e.printStackTrace();
										  } catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										  }
						              } // end of Checkout save to CSV
									    		  
						              Uri u1  =   null;
						              u1  =   Uri.fromFile(cfile);

						              // email files
						              Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
						              sendIntent.putExtra(Intent.EXTRA_SUBJECT, "American Corners: Checkout Report Backup");
						              ArrayList<Uri> uris = new ArrayList<Uri>();
						              uris.add(u1);
						              sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
						              sendIntent.setType("text/html");
						              startActivity(sendIntent);
			        				  	finish();
						              
						    		  progressDialog.dismiss();

						    		  Toast.makeText(getApplicationContext(), "Email Sending", Toast.LENGTH_LONG).show();
							    
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
        });
        
        // ADAM ADDED CLICKABLE TEXTVIEW
        titleSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				adapter.filter(0);
				adapter.notifyDataSetChanged();
			}
	    	
	    });
	    
	    authorSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.filter(1);
				adapter.notifyDataSetChanged();
			}
	    	
	    });
	    
	    checkOutIndividualSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.filter(2);
				adapter.notifyDataSetChanged();
			}
	    	
	    });
	    
	    memberIdSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.filter(3);
				adapter.notifyDataSetChanged();
			}
	    	
	    });
	    
	    checkoutDateSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.filter(4);
				adapter.notifyDataSetChanged();
			}
	    	
	    });
	    
	    dueDateSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.filter(5);
				adapter.notifyDataSetChanged();
			}
	    	
	    });
        
        // On Click ========================================================
        dataList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	
            	// Then you start a new Activity via Intent
                Intent i = new Intent();
                i.setClass(CheckoutReportScreen.this, ItemManagement.class);
                i.putExtra("username",userName);
                // 0 = title
         	    // 1 = author
         	    // 2 = checkout individual
         	    // 3 = member Id
         	    // 4 = checkout date
        		// 5 = due date
                i.putExtra("Title",adapter.getItem(position).getDetails()[0]);
                i.putExtra("Author",adapter.getItem(position).getDetails()[1]);
                i.putExtra("CheckoutIndividual",adapter.getItem(position).getDetails()[2]);
                i.putExtra("MemberID",adapter.getItem(position).getDetails()[3]);
                i.putExtra("CheckoutDate",adapter.getItem(position).getDetails()[4]);
                i.putExtra("DueDate",adapter.getItem(position).getDetails()[5]);
                i.putExtra("ISBN", adapter.getItem(position).getisbn());
                String[] member = MembershipAdapter.getMatchingMember(adapter.getItem(position).getDetails()[3]);
                i.putExtra("FirstName",member[0]);
                i.putExtra("LastName",member[1]);
                i.putExtra("Birthday",member[2]);
                i.putExtra("Email",member[4]);
                i.putExtra("Notes",member[7]);
                i.putExtra("NumCheckouts",Integer.parseInt(member[5])); // type int
                i.putExtra("KarmaPts",Integer.parseInt(member[6])); // type int
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
	                        	Intent i=new Intent(CheckoutReportScreen.this,InventoryOptionScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Manage Members"))
	                        {
	                        	Intent i=new Intent(CheckoutReportScreen.this,ManageMemberScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Settings"))
	                        {
	                        	Intent i=new Intent(CheckoutReportScreen.this,SettingScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Help"))
	                        {
	                        	Intent i=new Intent(CheckoutReportScreen.this,HelpScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Sign Off"))
	                        {
	                        	Intent intentHome=new Intent(CheckoutReportScreen.this,HomeActivity.class);
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
					Intent intentHome=new Intent(CheckoutReportScreen.this,HomeScreen.class);
					intentHome.putExtra("username",userName);
					startActivity(intentHome);
				  	finish();
	    		  
	    	  }
	      });
	      
	      // Search Button
	      Button bs = (Button) findViewById(R.id.btnSearchBottom);
	      bs.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		  	Intent i = new Intent(CheckoutReportScreen.this, SearchScreen.class);
			        i.putExtra("username",userName);
			        startActivity(i);
				  	finish();
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(CheckoutReportScreen.this, CheckoutReportScreen.class);
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
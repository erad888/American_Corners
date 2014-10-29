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
import com.dosamericancorner.customlistview.CustomReportAdapter;
import com.dosamericancorner.customlistview.ReportsItem;
import com.dosamericancorner.debug.debug;
import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.inventory.InventoryAdapter;
import com.dosamericancorner.inventory.InventoryEntryConverter;
import com.dosamericancorner.login.BuildConfig;
import com.dosamericancorner.login.R;
import com.dosamericancorner.membership.ManageMemberScreen;
import com.dosamericancorner.options.HelpScreen;
import com.dosamericancorner.options.InventoryOptionScreen;
import com.dosamericancorner.options.SettingScreen;
import com.dosamericancorner.search.SearchScreen;
import com.dosamericancorner.statistics.StatisticsAdapter;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;
import com.googlecode.jcsv.writer.CSVWriter;

public class ReportsByDateScreen extends Activity
{
	private ProgressDialog progressDialog;
	TextView titleSortButton, authorSortButton, callNumberSortButton,
	publishYearSortButton, checkOutCountSortButton;
	
	EditText inputSearch;
	ImageButton btnEnter;
	Button export;
	String isbn, reportStartDate, reportEndDate;
	ArrayList<ReportsItem> reportArray = new ArrayList<ReportsItem>();
	int startYear, startMonth, endYear, endMonth, pos, index;
	CheckOutDataBaseAdapter CheckOutDataBaseAdapter;
	CustomReportAdapter adapter;
	StatisticsAdapter StatisticsAdapter;
	InventoryAdapter InventoryAdapter;
	String[][] Entries;
	Spinner spnr, startYearSpinner, startMonthSpinner, endYearSpinner, endMonthSpinner;
	String[] menuOptions = {
			"",
			"Manage Inventory",
            "Manage Members",
            "Settings",
            "Help",
            "Sign Off"
    };
	Integer[] yearOptions = { 2014, 2015, 2016, 2017, 2018, 2019, 2020};
	Integer[] monthOptions = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.reportbydate);
	     
	    // create a instance of SQLite Database
	     CheckOutDataBaseAdapter=new CheckOutDataBaseAdapter(this);
	     CheckOutDataBaseAdapter=CheckOutDataBaseAdapter.open();
	     StatisticsAdapter=new StatisticsAdapter(this);
	     StatisticsAdapter=StatisticsAdapter.open();
	     InventoryAdapter=new InventoryAdapter(this);
	     InventoryAdapter=InventoryAdapter.open();
	     
	     Intent intent = getIntent();
	     final String userName = intent.getExtras().getString("username");
	     startMonth = intent.getExtras().getInt("startMonth");
	     startYear = intent.getExtras().getInt("startYear");
	     endMonth = intent.getExtras().getInt("endMonth");
	     endYear = intent.getExtras().getInt("endYear");
	     
	     //define Report Start and End Dates
	     if(startYear == 0)
	     {
	    	// Get due date
     		Calendar curDate = Calendar.getInstance();
     		Calendar defaultStart = Calendar.getInstance();
     		defaultStart.set(curDate.get(Calendar.YEAR), curDate.get(Calendar.MONTH)-1, curDate.get(Calendar.DAY_OF_MONTH));
     		String curYear = ((Integer)curDate.get(Calendar.YEAR)).toString();
     		String curMonth = ((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
     		String curDay = ((Integer)curDate.get(Calendar.DAY_OF_MONTH)).toString();
     		if(curDate.get(Calendar.MONTH)+1 < 10)
     			curMonth = "0"+((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
     		String defaultYear = ((Integer)defaultStart.get(Calendar.YEAR)).toString();
     		String defaultMonth = ((Integer)(defaultStart.get(Calendar.MONTH)+1)).toString();
     		String defaultDay = ((Integer)defaultStart.get(Calendar.DAY_OF_MONTH)).toString();
     		if(defaultStart.get(Calendar.MONTH)+1 < 10)
     			defaultMonth = "0"+((Integer)(defaultStart.get(Calendar.MONTH)+1)).toString();
     		reportEndDate = curMonth+"/"+curYear;
     		reportStartDate = defaultMonth+"/"+defaultYear;
     		startMonth = (defaultStart.get(Calendar.MONTH)+1);
     		startYear = defaultStart.get(Calendar.YEAR);
     		endMonth = (curDate.get(Calendar.MONTH)+1);
     		endYear = curDate.get(Calendar.YEAR);
	     }
	     else
	     {
	    	 reportStartDate = startMonth+"/"+startYear;
	    	 reportEndDate = endMonth+"/"+endYear;
	     }
	     
	     TextView startDateText = (TextView)findViewById(R.id.startDateText);
	     startDateText.setText(reportStartDate);
	     TextView endDateText = (TextView)findViewById(R.id.endDateText);
	     endDateText.setText(reportEndDate);
	     
	     int numEntries = StatisticsAdapter.getNumEntriesByDate(startYear, startMonth, endYear, endMonth);
	     String[][] records = StatisticsAdapter.getEntriesByDate(startYear, startMonth, endYear, endMonth);
	     Entries = new String[numEntries][5];
	     if (BuildConfig.DEBUG)
			    Log.d(debug.LOG, "numEntries="+numEntries);

	     String curISBN = new String();
	     
		// Set up search array
	    for(int i = 0; i < numEntries; i++)
	    {
	    	if (BuildConfig.DEBUG)
			    Log.d(debug.LOG, "records["+i+"][0]="+records[i][0]);
	    	curISBN = records[i][0];
	    	Entries[i][0] = InventoryAdapter.getTitleByISBN(curISBN);
	    	Entries[i][1] = InventoryAdapter.getAuthorByISBN(curISBN);
	    	Entries[i][2] = InventoryAdapter.getCallNumberByISBN(curISBN);
	    	Entries[i][3] = InventoryAdapter.getPublishYearByISBN(curISBN);
	    	Entries[i][4] = records[i][3];
	    	reportArray.add(new ReportsItem(curISBN,Entries[i]));
	    	if (BuildConfig.DEBUG)
	    	{
			    Log.d(debug.LOG, "Entries["+i+"][0]="+Entries[i][0]);
			    Log.d(debug.LOG, "Entries["+i+"][1]="+Entries[i][1]);
			    Log.d(debug.LOG, "Entries["+i+"][2]="+Entries[i][2]);
			    Log.d(debug.LOG, "Entries["+i+"][3]="+Entries[i][3]);
			    Log.d(debug.LOG, "Entries["+i+"][4]="+Entries[i][4]);
	    	}
	    }
	    
	    // Export button
	    export = (Button)findViewById(R.id.btnExportReport);
	    
	 // SORT BOOKS BUTTONS, ADAM ADDED
	    titleSortButton = (TextView) findViewById(R.id.titleSortButton);
	    authorSortButton = (TextView) findViewById(R.id.authorSortButton);
	    callNumberSortButton = (TextView) findViewById(R.id.callNumberSortButton);
	    publishYearSortButton = (TextView) findViewById(R.id.publishYearSortButton);
	    checkOutCountSortButton = (TextView) findViewById(R.id.checkOutCountSortButton);
	   

		// add data in custom adapter
		adapter = new CustomReportAdapter(this, R.layout.listview_row, reportArray);
        ListView dataList = (ListView) findViewById(R.id.listview_row);
        dataList.setAdapter(adapter);
        
     // Export data
        export.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		// Alert Dialog to confirm Export
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						ReportsByDateScreen.this);
		 
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
							    	 progressDialog = ProgressDialog.show(ReportsByDateScreen.this, "", "Loading...");
						    		  File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"americancorners");
						              if(!fileDir.exists()){
						    			try{
						    				fileDir.mkdir();
						    			} catch (Exception e) {
						    				e.printStackTrace();
						    			}
						              }
						              
						           // === Backup Report to CSV
						              File cfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"americancorners"+File.separator+"reportbydate_"+startMonth+"-"+startYear+"_to_"+endMonth+"-"+endYear+"-v1.csv");
						              if(!cfile.exists()){
						    			try {
						    				cfile.createNewFile();
						    				// Write to New File
						    		    	  try {
						    		    		  Writer out = new FileWriter(cfile);
						    		    		  CSVWriter<String[]> writer = new CSVWriterBuilder<String[]>(out).entryConverter(new InventoryEntryConverter()).build();
						    		    		  writer.write(new String[]{"'TITLE'","'AUTHOR'","'CALL_NUMBER'","'PUBLISH_YEAR'","'CHECKOUT_COUNT'"});
						    		    		  for(int j = 0; j < Entries.length; j++)
						    		    		  {
						    		    			  writer.write(Entries[j]);
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
						            	  File cfileNew = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"americancorners"+File.separator+"reportbydate_"+startMonth+"-"+startYear+"_to_"+endMonth+"-"+endYear+"-v"+count+".csv");
						            	  // Make New File
						            	  while(cfileNew.exists())
					            		  {
						            		  count++;
						            		  cfileNew = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"americancorners"+File.separator+"reportbydate_"+startMonth+"-"+startYear+"_to_"+endMonth+"-"+endYear+"-v"+count+".csv");
					            		  }
								    	  if(!cfileNew.exists()){
									  			try {
									  				cfile.createNewFile();
									  			} catch (IOException e) {
									  				e.printStackTrace();
									  			}
								    	  }
								    	  // Write to New File
								    	  try {
								    		  Writer out = new FileWriter(cfileNew);
					    		    		  CSVWriter<String[]> writer = new CSVWriterBuilder<String[]>(out).entryConverter(new InventoryEntryConverter()).build();
					    		    		  writer.write(new String[]{"'TITLE'","'AUTHOR'","'CALL_NUMBER'","'PUBLISH_YEAR'","'CHECKOUT_COUNT'"});
					    		    		  for(int j = 0; j < Entries.length; j++)
					    		    		  {
					    		    			  writer.write(Entries[j]);
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
						              sendIntent.putExtra(Intent.EXTRA_SUBJECT, "American Corners: Report By Date Backup -> "+startMonth+"-"+startYear+" to "+endMonth+"-"+endYear);
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
        
        // ADAM ADDED
        titleSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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
        
        callNumberSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.filter(2);
				adapter.notifyDataSetChanged();
			}
        	
        });
        
        publishYearSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.filter(3);
				adapter.notifyDataSetChanged();
			}
        	
        });
        
        checkOutCountSortButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.filter(4);
				adapter.notifyDataSetChanged();
			}
        	
        });
        
        // On Click ========================================================
        dataList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	
                // Then you start a new Activity via Intent
                //Intent i = new Intent();
                //i.setClass(ReportsByDateScreen.this, CheckoutScreen.class);
                //i.putExtra("username",userName);
                //startActivity(i);
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
                        	Intent i=new Intent(ReportsByDateScreen.this,InventoryOptionScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Manage Members"))
                        {
                        	Intent i=new Intent(ReportsByDateScreen.this,ManageMemberScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Settings"))
                        {
                        	Intent i=new Intent(ReportsByDateScreen.this,SettingScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Help"))
                        {
                        	Intent i=new Intent(ReportsByDateScreen.this,HelpScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Sign Off"))
                        {
                        	Intent intentHome=new Intent(ReportsByDateScreen.this,HomeActivity.class);
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
        
        // Start Year Spinner
        startYearSpinner = (Spinner)findViewById(R.id.spinnerStartYear);
        ArrayAdapter<Integer> startYearAdapter = new ArrayAdapter<Integer>(
                this, R.layout.menu_spinner_item, yearOptions);
        startYearSpinner.setAdapter(startYearAdapter);
        index = 0;
        pos = -1;
        while(index < yearOptions.length)
        {
        	if(yearOptions[index].equals(startYear))
        		pos = index;
        	index++;
        }
        if(index >= yearOptions.length && pos == -1)
        	pos = 0;
        startYearSpinner.setSelection(pos);
        startYearSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                            int arg2, long arg3) {
                        int position = startYearSpinner.getSelectedItemPosition();
                        startYear = yearOptions[position];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
            );
        
        // Start Month Spinner
        startMonthSpinner = (Spinner)findViewById(R.id.spinnerStartMonth);
        ArrayAdapter<Integer> startMonthAdapter = new ArrayAdapter<Integer>(
                this, R.layout.menu_spinner_item, monthOptions);
        startMonthSpinner.setAdapter(startMonthAdapter);
        index = 0;
        pos = -1;
        while(index < monthOptions.length)
        {
        	if(monthOptions[index].equals(startMonth))
        		pos = index;
        	index++;
        }
        if(index >= monthOptions.length && pos == -1)
        	pos = 0;
        startMonthSpinner.setSelection(pos);
        startMonthSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                            int arg2, long arg3) {
                        int position = startMonthSpinner.getSelectedItemPosition();
                        startMonth = monthOptions[position];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
            );
        
        // End Year Spinner
        endYearSpinner = (Spinner)findViewById(R.id.spinnerEndYear);
        ArrayAdapter<Integer> endYearAdapter = new ArrayAdapter<Integer>(
                this, R.layout.menu_spinner_item, yearOptions);
        endYearSpinner.setAdapter(endYearAdapter);
        index = 0;
        pos = -1;
        while(index < yearOptions.length)
        {
        	if(yearOptions[index].equals(endYear))
        		pos = index;
        	index++;
        }
        if(index >= yearOptions.length && pos == -1)
        	pos = 0;
        endYearSpinner.setSelection(pos);
        endYearSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                            int arg2, long arg3) {
                        int position = endYearSpinner.getSelectedItemPosition();
                        endYear = yearOptions[position];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
            );
        
        // End Month Spinner
        endMonthSpinner = (Spinner)findViewById(R.id.spinnerEndMonth);
        ArrayAdapter<Integer> endMonthAdapter = new ArrayAdapter<Integer>(
                this, R.layout.menu_spinner_item, monthOptions);
        endMonthSpinner.setAdapter(endMonthAdapter);
        index = 0;
        pos = -1;
        while(index < monthOptions.length)
        {
        	if(monthOptions[index].equals(endMonth))
        		pos = index;
        	index++;
        }
        if(index >= monthOptions.length && pos == -1)
        	pos = 0;
        endMonthSpinner.setSelection(pos);
        endMonthSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                            int arg2, long arg3) {
                        int position = endMonthSpinner.getSelectedItemPosition();
                        endMonth = monthOptions[position];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
            );
        
        // Generate New Report
        Button newReport = (Button) findViewById(R.id.btnNewReport);
        newReport.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for a new ReportsByDate and Start The Activity
					Intent i=new Intent(ReportsByDateScreen.this,ReportsByDateScreen.class);
					i.putExtra("username",userName);
					i.putExtra("startMonth", startMonth);
					i.putExtra("startYear", startYear);
					i.putExtra("endMonth", endMonth);
					i.putExtra("endYear", endYear);
					startActivity(i);
				  	finish();
	    		  
	    	  }
	      });
		
	    // ============== Bottom row of buttons ==============

	      // Home Button
	      Button bh = (Button) findViewById(R.id.btnHomeBottom);
	      bh.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
					Intent intentHome=new Intent(ReportsByDateScreen.this,HomeScreen.class);
					intentHome.putExtra("username",userName);
					startActivity(intentHome);
				  	finish();
	    		  
	    	  }
	      });
	      
	      // Search Button
	      Button bs = (Button) findViewById(R.id.btnSearchBottom);
	      bs.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		  	Intent i = new Intent(ReportsByDateScreen.this, SearchScreen.class);
			        i.putExtra("username",userName);
			        startActivity(i);
				  	finish();
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(ReportsByDateScreen.this, ReportsByDateScreen.class);
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
	}
	
}
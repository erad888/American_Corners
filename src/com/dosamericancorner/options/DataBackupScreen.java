package com.dosamericancorner.options;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.checkout.CheckoutScreen;
import com.dosamericancorner.reports.CheckoutReportScreen;
import com.dosamericancorner.reports.ReportsByDateScreen;
import com.dosamericancorner.search.*;
import com.dosamericancorner.statistics.StatisticsAdapter;
import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.inventory.*;
import com.dosamericancorner.linkedlist.itemLink;
import com.dosamericancorner.linkedlist.itemLinkedList;
import com.dosamericancorner.login.R;
import com.dosamericancorner.membership.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.CSVEntryParser;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;
import com.googlecode.jcsv.writer.CSVWriter;

public class DataBackupScreen extends Activity {
	private ProgressDialog progressDialog;
	Button buttonBackup;
	StatisticsAdapter StatisticsAdapter;
	InventoryAdapter InventoryAdapter;
	CheckOutDataBaseAdapter CheckOutDataBaseAdapter;
	MembershipAdapter MembershipAdapter;
	String [][] inventory, checkout, statistics, membership;
	Spinner spnr;
	String[] menuOptions = {
			"",
			"Manage Inventory",
            "Manage Members",
            "Settings",
            "Help",
            "Sign Off"
    };
	
	   @SuppressWarnings("static-access")
	public void onCreate(Bundle savedInstanceState)
	   {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.data_backup);
	      
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
	      
	      // Get References of Views

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
	                        	Intent i=new Intent(DataBackupScreen.this,InventoryOptionScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Manage Members"))
	                        {
	                        	Intent i=new Intent(DataBackupScreen.this,ManageMemberScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Settings"))
	                        {
	                        	Intent i=new Intent(DataBackupScreen.this,SettingScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Help"))
	                        {
	                        	Intent i=new Intent(DataBackupScreen.this,HelpScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Sign Off"))
	                        {
	                        	Intent intentHome=new Intent(DataBackupScreen.this,HomeActivity.class);
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
	        
	        // Store all databases locally
	        inventory = InventoryAdapter.getAll();
	        checkout = CheckOutDataBaseAdapter.getAllEntries();
	        statistics = StatisticsAdapter.getAll();
		  
	      buttonBackup.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		//
	    		  progressDialog = ProgressDialog.show(DataBackupScreen.this, "", "Loading...");
	    		  File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"americancorners");
	              if(!fileDir.exists()){
	    			try{
	    				fileDir.mkdir();
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
	              }
	              
	              // === Backup Inventory to CSV
	              File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"americancorners"+File.separator+"inventory.csv");
	              if(!file.exists()){
	    			try {
	    				file.createNewFile();
	    				// Write to New File
	    		    	  try {
	    		    		  Writer out = new FileWriter(file);
	    		    		  CSVWriter<String[]> writer = new CSVWriterBuilder<String[]>(out).entryConverter(new InventoryEntryConverter()).build();
	    		    		  //CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(file)));
	    		    		  writer.write(new String[]{"'ISBN'","'TITLE'","'AUTHOR'","'PUBLISH_DATE'","'CALL_NUMBER'","'AVAILABLE_COUNT'","'INVENTORY_COUNT'","'DUE_PERIOD'","'COUNT'"});
	    		    		  for(int j = 0; j < inventory.length; j++)
	    		    		  {
	    		    			  writer.write(inventory[j]);
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
		              // Delete Old File
			    	  file.delete();
			    	  // Make New File
			    	  if(!file.exists()){
				  			try {
				  				file.createNewFile();
				  			} catch (IOException e) {
				  				e.printStackTrace();
				  			}
			    	  }
			    	  // Write to New File
			    	  try {
			    		  Writer out = new FileWriter(file);
			    		  CSVWriter<String[]> writer = new CSVWriterBuilder<String[]>(out).entryConverter(new InventoryEntryConverter()).build();
    		    		  //CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(file)));
    		    		  writer.write(new String[]{"'ISBN'","'TITLE'","'AUTHOR'","'PUBLISH_DATE'","'CALL_NUMBER'","'AVAILABLE_COUNT'","'INVENTORY_COUNT'","'DUE_PERIOD'","'COUNT'"});
			    		  for(int j = 0; j < inventory.length; j++)
    		    		  {
    		    			  writer.write(inventory[j]);
    		    		  }
    		    		  writer.close();
					  } catch (FileNotFoundException e) {
						  // TODO Auto-generated catch block
						  e.printStackTrace();
					  } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					  }
	              } // end of Inventory save to CSV
	              
	           // === Backup Checkouts to CSV
	              File cfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"americancorners"+File.separator+"checkout.csv");
	              if(!cfile.exists()){
	    			try {
	    				cfile.createNewFile();
	    				// Write to New File
	    		    	  try {
	    		    		  Writer out = new FileWriter(cfile);
	    		    		  CSVWriter<String[]> writer = new CSVWriterBuilder<String[]>(out).entryConverter(new InventoryEntryConverter()).build();
	    		    		  writer.write(new String[]{"'USER'","'CHECKOUT_INDIVIDUAL'","'MEMBER_ID'","'ISBN'","'CHECKOUT_DATE'","'DUE_DATE'"});
	    		    		  for(int j = 0; j < checkout.length; j++)
	    		    		  {
	    		    			  writer.write(checkout[j]);
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
		              // Delete Old File
			    	  cfile.delete();
			    	  // Make New File
			    	  if(!cfile.exists()){
				  			try {
				  				cfile.createNewFile();
				  			} catch (IOException e) {
				  				e.printStackTrace();
				  			}
			    	  }
			    	  // Write to New File
			    	  try {
			    		  Writer out = new FileWriter(cfile);
    		    		  CSVWriter<String[]> writer = new CSVWriterBuilder<String[]>(out).entryConverter(new InventoryEntryConverter()).build();
    		    		  writer.write(new String[]{"'USER'","'CHECKOUT_INDIVIDUAL'","'MEMBER_ID'","'ISBN'","'CHECKOUT_DATE'","'DUE_DATE'"});
    		    		  for(int j = 0; j < checkout.length; j++)
    		    		  {
    		    			  writer.write(checkout[j]);
    		    		  }
    		    		  writer.close();
					  } catch (FileNotFoundException e) {
						  // TODO Auto-generated catch block
						  e.printStackTrace();
					  } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					  }
	              } // end of Checkout save to CSV
	              
	           // === Backup Statistics to CSV
	              File sfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"americancorners"+File.separator+"statistics.csv");
	              if(!sfile.exists()){
	    			try {
	    				sfile.createNewFile();
	    				// Write to New File
	    		    	  try {
	    		    		  Writer out = new FileWriter(sfile);
	    		    		  CSVWriter<String[]> writer = new CSVWriterBuilder<String[]>(out).entryConverter(new InventoryEntryConverter()).build();
	    		    		  writer.write(new String[]{"'ISBN'","'YEAR'","'MONTH'","'COUNT'"});
	    		    		  for(int j = 0; j < statistics.length; j++)
	    		    		  {
	    		    			  writer.write(statistics[j]);
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
		              // Delete Old File
			    	  sfile.delete();
			    	  // Make New File
			    	  if(!sfile.exists()){
				  			try {
				  				sfile.createNewFile();
				  			} catch (IOException e) {
				  				e.printStackTrace();
				  			}
			    	  }
			    	  // Write to New File
			    	  try {
			    		  Writer out = new FileWriter(sfile);
    		    		  CSVWriter<String[]> writer = new CSVWriterBuilder<String[]>(out).entryConverter(new InventoryEntryConverter()).build();
    		    		  writer.write(new String[]{"'USER'","'CHECKOUT_INDIVIDUAL'","'MEMBER_ID'","'ISBN'","'CHECKOUT_DATE'","'DUE_DATE'"});
    		    		  for(int j = 0; j < statistics.length; j++)
    		    		  {
    		    			  writer.write(statistics[j]);
    		    		  }
    		    		  writer.close();
					  } catch (FileNotFoundException e) {
						  // TODO Auto-generated catch block
						  e.printStackTrace();
					  } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					  }
	              } // end of Statistics save to CSV
				    		  
	              Uri u1  =   null;
	              Uri u2  =   null;
	              Uri u3  =   null;
	              Uri u4  =   null;
	              u1  =   Uri.fromFile(file);
	              u2  =   Uri.fromFile(cfile);
	              u3  =   Uri.fromFile(sfile);

	              // email files
	              Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
	              sendIntent.putExtra(Intent.EXTRA_SUBJECT, "American Corners Backup");
	              ArrayList<Uri> uris = new ArrayList<Uri>();
	              uris.add(u1);
	              uris.add(u2);
	              uris.add(u3);
	              sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
	              sendIntent.setType("text/html");
	              startActivity(sendIntent);
				  	finish();
	              
	    		  progressDialog.dismiss();

	    		  Toast.makeText(getApplicationContext(), "Email Sending", Toast.LENGTH_LONG).show();
	    		  
	    	  }
	      });
	        


	      // Home Button
	      Button bh = (Button) findViewById(R.id.btnHomeBottom);
	      bh.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
					Intent i=new Intent(DataBackupScreen.this,HomeScreen.class);
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
				Intent i=new Intent(DataBackupScreen.this,SearchScreen.class);
				i.putExtra("username",userName);
				startActivity(i);
			  	finish();
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(DataBackupScreen.this, ReportsByDateScreen.class);
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
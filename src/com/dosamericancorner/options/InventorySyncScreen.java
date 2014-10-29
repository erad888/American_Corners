package com.dosamericancorner.options;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.reports.ReportsByDateScreen;
import com.dosamericancorner.search.*;
import com.dosamericancorner.statistics.StatisticsAdapter;
import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.inventory.*;
import com.dosamericancorner.login.R;
import com.dosamericancorner.membership.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.googlecode.jcsv.CSVStrategy;
//import au.com.bytecode.opencsv.CSVReader;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.CSVEntryParser;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;

public class InventorySyncScreen extends Activity {
	EditText inputSource, inputUserName, inputPassword;
	Button buttonSyncInventory, buttonNewSource;
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
	ArrayList<Integer> toDeleteIds = new ArrayList<Integer>();
	//SearchHandler searchHandler;
	//BookListAdapter adapter;
	
	int PROGRESS_LOGGED_IN = 3;
    int PROGRESS_SUCCESS = 4;
    int PROGRESS_LOGIN_FAIL = 5;
    
    HttpContext localContext;
	
	/*String[][] addNew(String[][] oldString, String[] newString)
	{
		String[][] tempString = Arrays.copyOf(oldString, oldString.length + 1); // New array with row size of old array + 1

		tempString[oldString.length] = new String[oldString.length]; // Initializing the new row

		// Copying data from newString array to the tempString array
		for (int i = 0; i < newString.length; i++) {
		    tempString[tempString.length-1][i] = newString[i];
		}
		
		return tempString;
	}*/
	
	   public void onCreate(Bundle savedInstanceState)
	   {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.inventory_sync);
	      
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
	      String source = intent.getExtras().getString("source");
		  //TextView sourceText =(TextView)findViewById(R.id.textInventoryLocation);
		  //if(source.equals("0"))
			  source = "/Download/LibraryThing_export.csv";
		  // Get References of Views
	      inputUserName=(EditText)findViewById(R.id.textUserName);
	      inputPassword=(EditText)findViewById(R.id.textPassword);
	      buttonSyncInventory = (Button)findViewById(R.id.btnSyncInventory);
	      
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
	                        	Intent i=new Intent(InventorySyncScreen.this,InventoryOptionScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Manage Members"))
	                        {
	                        	Intent i=new Intent(InventorySyncScreen.this,ManageMemberScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Settings"))
	                        {
	                        	Intent i=new Intent(InventorySyncScreen.this,SettingScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Help"))
	                        {
	                        	Intent i=new Intent(InventorySyncScreen.this,HelpScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Sign Off"))
	                        {
	                        	Intent intentHome=new Intent(InventorySyncScreen.this,HomeActivity.class);
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
		  
	      buttonSyncInventory.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		//
	    		  
	    		  /*
	    		  progressDialog = ProgressDialog.show(InventorySyncScreen.this, "", "Loading...");
	    		  			ArrayList<errorItem> errorList = new ArrayList<errorItem>();
	    		    		int successCounter = 0;
				    		  int errorCounter = 0;
				    		  String[][] errors = new String[1][6];
				    		  String[] line = new String[6];
				    		  try {
				    			  int titleCol = -1, authorCol = -1, dateCol = -1, isbnCol = -1, quantityCol = -1, tagCol = -1;
					    		  CSVReader reader = new CSVReader(new FileReader(new File(Environment.getExternalStorageDirectory().getPath()+location)));
					    		  String [] nextLine;
					    		    nextLine = reader.readNext();
					    		    int index = 0;
					    		    int lineLength = nextLine.length;
					    		    while(titleCol < 0 && index < lineLength)
					    		    {
					    		    	if(nextLine[index].contains("TITLE"))
					    		    		titleCol = index;
					    		    	index++;
					    		    }
					    		    index = 0;
					    		    while(authorCol < 0 && index < lineLength)
					    		    {
					    		    	if(nextLine[index].contains("AUTHOR"))
					    		    		authorCol = index;
					    		    	index++;
					    		    }
					    		    index = 0;
					    		    while(dateCol < 0 && index < lineLength)
					    		    {
					    		    	if(nextLine[index].contains("DATE"))
					    		    		dateCol = index;
					    		    	index++;
					    		    }
					    		    index = 0;
					    		    while(isbnCol < 0 && index < lineLength)
					    		    {
					    		    	if(nextLine[index].contains("ISBN"))
					    		    		isbnCol = index;
					    		    	index++;
					    		    }
					    		    index = 0;
					    		    while(quantityCol < 0 && index < lineLength)
					    		    {
					    		    	if(nextLine[index].contains("COPIES"))
					    		    		quantityCol = index;
					    		    	index++;
					    		    }
					    		    index = 0;
					    		    while(tagCol < 0 && index < lineLength)
					    		    {
					    		    	if(nextLine[index].contains("TAGS"))
					    		    		tagCol = index;
					    		    	index++;
					    		    }
					    		    int counter = 0;
					    		    while ((nextLine = reader.readNext()) != null) {
					    		        // nextLine[] is an array of values from the line
					    		    	// * nextLine[titleCol] = TITLE
					    		    	// * nextLine[authorCol] = AUTHOR
					    		    	// * nextLine[dateCol] = DATE
					    		    	// * nextLine[isbnCol] = ISBN
					    		    	// * nextLine[quantityCol] = QUANTITY
					    		    	// * nextLine[tagCol] = TAGS
					    		    	lineLength = nextLine.length;
					    		    	if(nextLine[isbnCol].contains("0") || nextLine[isbnCol].contains("1") 
				    		    				|| nextLine[isbnCol].contains("2") || nextLine[isbnCol].contains("3")
				    		    				|| nextLine[isbnCol].contains("4") || nextLine[isbnCol].contains("5")
				    		    				|| nextLine[isbnCol].contains("6") || nextLine[isbnCol].contains("7")
				    		    				|| nextLine[isbnCol].contains("8") || nextLine[isbnCol].contains("9"))
					    		    	{
					    		    	
						    		    	if(InventoryAdapter.isbnFoundInInventory(nextLine[isbnCol]) == false)
						    		    	{
						    		    		
						    		    		if(nextLine[dateCol].contains("0") || nextLine[dateCol].contains("1") 
						    		    				|| nextLine[dateCol].contains("2") || nextLine[dateCol].contains("3")
						    		    				|| nextLine[dateCol].contains("4") || nextLine[dateCol].contains("5")
						    		    				|| nextLine[dateCol].contains("6") || nextLine[dateCol].contains("7")
						    		    				|| nextLine[dateCol].contains("8") || nextLine[dateCol].contains("9"))
						    		    			line[2] = nextLine[dateCol];
						    		    		else
						    		    			line[2]="-9999";
						    		    		if(nextLine[quantityCol].contains("0") || nextLine[quantityCol].contains("1") 
						    		    				|| nextLine[quantityCol].contains("2") || nextLine[quantityCol].contains("3")
						    		    				|| nextLine[quantityCol].contains("4") || nextLine[quantityCol].contains("5")
						    		    				|| nextLine[quantityCol].contains("6") || nextLine[quantityCol].contains("7")
						    		    				|| nextLine[quantityCol].contains("8") || nextLine[quantityCol].contains("9"))
						    		    			line[4] = nextLine[quantityCol];
						    		    		else
						    		    			line[4]="-9999";
						    		    		if(tagCol < lineLength && nextLine[tagCol].length() > 0)
						    		    			line[5] = nextLine[tagCol];
						    		    		else
						    		    			line[5] = "*ERROR*";
						    		    		if(titleCol < lineLength && nextLine[titleCol].length() > 0)
						    		    			line[0] = nextLine[titleCol];
						    		    		else
						    		    			line[0] = "*ERROR*";
						    		    		if(authorCol < lineLength && nextLine[authorCol].length() > 0)
						    		    			line[1] = nextLine[authorCol];
						    		    		else
						    		    			line[1] = "*ERROR*";
						    		    		if(isbnCol < lineLength && nextLine[isbnCol].length() > 0)
						    		    			line[3] = nextLine[isbnCol];
						    		    		else
						    		    			line[3] = "*ERROR*";
						    		    		
						    		    		if(line[0].equals("*ERROR*") || line[1].equals("*ERROR*")
						    		    				|| line[3].equals("*ERROR*")|| line[5].equals("*ERROR*")
						    		    				|| line[2].equals("-9999")|| line[4].equals("-9999"))
						    		    		{
						    		    			errorList.add(new errorItem(line[0], line[1], Integer.parseInt(line[2]), 
						    		    					line[3], Integer.parseInt(line[4]), line[5]));
						    		    			errorCounter++;
						    		    		}
						    		    		else
						    		    		{
						    		    			InventoryAdapter.insertEntry(line[0], line[1], line[3], Integer.parseInt(line[2]),
						    		    					line[5], Integer.parseInt(line[4]), 14);
						    		    			successCounter++;
						    		    		}
						    		    	}
					    		    	}
					    		    	counter++;
					    		    	
					    		   }
				    		  } catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				    		  
				    		  progressDialog.dismiss();
				    		  
				    		  Intent i=new Intent(InventorySyncScreen.this,SyncSuccessScreen.class);
								i.putExtra("username",userName);
								i.putExtra("numSuccess",successCounter);
								i.putExtra("numError", errorCounter);
								i.putExtra("errorList", errorList);
								startActivity(i);
								*/
	    		  new LTLoginDownload().execute(null, null, null);
	    		  
	    	  }
	      });
	        


	      // Home Button
	      Button bh = (Button) findViewById(R.id.btnHomeBottom);
	      bh.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
					Intent i=new Intent(InventorySyncScreen.this,HomeScreen.class);
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
				Intent i=new Intent(InventorySyncScreen.this,SearchScreen.class);
				i.putExtra("username",userName);
				startActivity(i);
			  	finish();
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(InventorySyncScreen.this, ReportsByDateScreen.class);
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
	   
	   //EntryParser
	   public class EntryParser implements CSVEntryParser<String[]> {
	        public String[] parseEntry(String... data) {
	            return data;
	        }
	    }
	   
	   // importBooksFromDownload
	   public void importBooksFromDownload(String downloadedContent) {
	        InputStream is = new ByteArrayInputStream(downloadedContent.getBytes());
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        CSVReader<String[]> csvReader = new CSVReaderBuilder<String[]>(
	                br)
	                .strategy(new CSVStrategy('\t', '\b', '#', true, true))
	                .entryParser(new EntryParser()).build();

	        /*List<String[]> csvData = null;
	        try {
	            csvData = csvReader.readAll();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }*/
	        
	        
	        // _________--------------------------________________________________
	        
	        
	        ArrayList<errorItem> errorList = new ArrayList<errorItem>();
    		int successCounter = 0;
    		  int errorCounter = 0;
    		  String[] line = new String[6];
    		  try {
    			  int titleCol = 1, authorCol = 2, dateCol = 6, isbnCol = 7, quantityCol = 27, tagCol = 22;
	    		  //CSVReader reader = new CSVReader(new FileReader(new File(Environment.getExternalStorageDirectory().getPath()+location)));
	    		  String [] nextLine;
	    		    nextLine = csvReader.readNext();
	    		    int index = 0;
	    		    int lineLength = nextLine.length;
	    		    System.out.println("=== First Row");
	    		    while(index < lineLength)
	    		    {
	    		    	System.out.print(index+":"+nextLine[index]+", ");
	    		    	index++;
	    		    }
	    		    System.out.println();
	    		    index = 0;
	    		    while(titleCol < 0 && index < lineLength)
	    		    {
	    		    	if(nextLine[index].contains("title"))
	    		    		titleCol = index;
	    		    	index++;
	    		    }
	    		    index = 0;
	    		    while(authorCol < 0 && index < lineLength)
	    		    {
	    		    	if(nextLine[index].contains("author1"))
	    		    		authorCol = index;
	    		    	index++;
	    		    }
	    		    index = 0;
	    		    while(dateCol < 0 && index < lineLength)
	    		    {
	    		    	if(nextLine[index].contains("date"))
	    		    		dateCol = index;
	    		    	index++;
	    		    }
	    		    index = 0;
	    		    while(isbnCol < 0 && index < lineLength)
	    		    {
	    		    	if(nextLine[index].contains("ISBNs"))
	    		    		isbnCol = index;
	    		    	index++;
	    		    }
	    		    index = 0;
	    		    while(quantityCol < 0 && index < lineLength)
	    		    {
	    		    	if(nextLine[index].contains("copies"))
	    		    		quantityCol = index;
	    		    	index++;
	    		    }
	    		    index = 0;
	    		    while(tagCol < 0 && index < lineLength)
	    		    {
	    		    	if(nextLine[index].contains("tags"))
	    		    		tagCol = index;
	    		    	index++;
	    		    }
	    		    while ((nextLine = csvReader.readNext()) != null) {
	    		        // nextLine[] is an array of values from the line
	    		    	// * nextLine[titleCol] = TITLE
	    		    	// * nextLine[authorCol] = AUTHOR
	    		    	// * nextLine[dateCol] = DATE
	    		    	// * nextLine[isbnCol] = ISBN
	    		    	// * nextLine[quantityCol] = QUANTITY
	    		    	// * nextLine[tagCol] = TAGS
	    		    	lineLength = nextLine.length;
	    		    	System.out.println("---");
		    		    while(index < lineLength)
		    		    {
		    		    	System.out.print(nextLine[index]+", ");
		    		    	index++;
		    		    }
		    		    System.out.println();
	    		    	if(nextLine[isbnCol].contains("0") || nextLine[isbnCol].contains("1") 
    		    				|| nextLine[isbnCol].contains("2") || nextLine[isbnCol].contains("3")
    		    				|| nextLine[isbnCol].contains("4") || nextLine[isbnCol].contains("5")
    		    				|| nextLine[isbnCol].contains("6") || nextLine[isbnCol].contains("7")
    		    				|| nextLine[isbnCol].contains("8") || nextLine[isbnCol].contains("9"))
	    		    	{
	    		    	
		    		    	if(InventoryAdapter.isbnFoundInInventory(nextLine[isbnCol]) == false)
		    		    	{
		    		    		
		    		    		if(nextLine[dateCol].contains("0") || nextLine[dateCol].contains("1") 
		    		    				|| nextLine[dateCol].contains("2") || nextLine[dateCol].contains("3")
		    		    				|| nextLine[dateCol].contains("4") || nextLine[dateCol].contains("5")
		    		    				|| nextLine[dateCol].contains("6") || nextLine[dateCol].contains("7")
		    		    				|| nextLine[dateCol].contains("8") || nextLine[dateCol].contains("9"))
		    		    			line[2] = nextLine[dateCol];
		    		    		else
		    		    			line[2] = "-9999";
		    		    		if(nextLine[quantityCol].contains("0") || nextLine[quantityCol].contains("1") 
		    		    				|| nextLine[quantityCol].contains("2") || nextLine[quantityCol].contains("3")
		    		    				|| nextLine[quantityCol].contains("4") || nextLine[quantityCol].contains("5")
		    		    				|| nextLine[quantityCol].contains("6") || nextLine[quantityCol].contains("7")
		    		    				|| nextLine[quantityCol].contains("8") || nextLine[quantityCol].contains("9"))
		    		    			line[4] = nextLine[quantityCol];
		    		    		else
		    		    			line[4]="-9999";
		    		    		if(tagCol < lineLength && nextLine[tagCol].length() > 0)
		    		    			line[5] = nextLine[tagCol];
		    		    		else
		    		    			line[5] = "*ERROR*";
		    		    		if(titleCol < lineLength && nextLine[titleCol].length() > 0)
		    		    			line[0] = nextLine[titleCol];
		    		    		else
		    		    			line[0] = "*ERROR*";
		    		    		if(authorCol < lineLength && nextLine[authorCol].length() > 0)
		    		    			line[1] = nextLine[authorCol];
		    		    		else
		    		    			line[1] = "*ERROR*";
		    		    		if(isbnCol < lineLength && nextLine[isbnCol].length() > 0)
		    		    			line[3] = nextLine[isbnCol];
		    		    		else
		    		    			line[3] = "*ERROR*";
		    		    		
		    		    		if(line[0].equals("*ERROR*") || line[1].equals("*ERROR*")
		    		    				|| line[3].equals("*ERROR*")|| line[5].equals("*ERROR*")
		    		    				|| line[2].equals("-9999")|| line[4].equals("-9999"))
		    		    		{
		    		    			errorList.add(new errorItem(line[0], line[1], Integer.parseInt(line[2]), 
		    		    					line[3], Integer.parseInt(line[4]), line[5]));
		    		    			errorCounter++;
		    		    			InventoryAdapter.insertEntry(line[0], line[1], line[3], Integer.parseInt(line[2]),
		    		    					line[5], Integer.parseInt(line[4]), 14);
		    		    			successCounter++;
		    		    		}
		    		    		else
		    		    		{
		    		    			InventoryAdapter.insertEntry(line[0], line[1], line[3], Integer.parseInt(line[2]),
		    		    					line[5], Integer.parseInt(line[4]), 14);
		    		    			successCounter++;
		    		    		}
		    		    	}
	    		    	}
	    		    	
	    		   }
    		  } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		  
    		  Intent i=new Intent(InventorySyncScreen.this,SyncSuccessScreen.class);
				i.putExtra("username","username");
				i.putExtra("numSuccess",successCounter);
				i.putExtra("numError", errorCounter);
				i.putExtra("errorList", errorList);
				startActivity(i);
			  	finish();
	        
	        // =================================================================================================
	        
	        //toDeleteIds = searchHandler.getIds();

	        //ImportBooksTask task = new ImportBooksTask();
	        //task.execute(csvData);
	        
	        //prefsEdit = sharedPref.edit();
	        //String s = SimpleDateFormat.getDateTimeInstance().format(new Date());
	        //prefsEdit.putString("last_download_summary", "Most recent: " + s);
	        //prefsEdit.commit();
	    }
	   
	   //LTLoginDownload downloads the library from LibraryThing
	   private class LTLoginDownload extends AsyncTask<Boolean, Integer, String> {
	        String result;        
	        ProgressDialog dialog;

	        
	        @Override
	        protected void onPreExecute() {
	            dialog = new ProgressDialog(InventorySyncScreen.this);
	            dialog.setTitle("Downloading library...");
	            dialog.setMessage("Logging in...");
	            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	            dialog.setCancelable(false);
	            dialog.setCanceledOnTouchOutside(false);
	            dialog.show();
	        }
	        
	        protected void onProgressUpdate(Integer... progUpdate) {
	            if (progUpdate[0] == PROGRESS_LOGGED_IN){  
	               dialog.setMessage("Successfully logged in. Now downloading your library.");
	           } else if (progUpdate[0] == PROGRESS_SUCCESS) {
	               dialog.setMessage("Successfull! Let's import your books.");
	           } else if (progUpdate[0] == PROGRESS_LOGIN_FAIL) {
	               dialog.setMessage("Login failed.");
	               dialog.dismiss();
	               Intent in = new Intent(getApplicationContext(), HomeScreen.class);
	               startActivity(in);
				  	finish();
	           }
	        }
	        
	        protected String doInBackground(Boolean... bools) {
	            HttpClient client = new DefaultHttpClient();
	            HttpPost loginPost = new HttpPost(
	                    "https://www.librarything.com/enter/start");

	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	            nameValuePairs
	            .add(new BasicNameValuePair("formusername", inputUserName.getText().toString()));
	            nameValuePairs.add(new BasicNameValuePair("formpassword",
	            		inputPassword.getText().toString()));
	            nameValuePairs.add(new BasicNameValuePair("index_signin_already",
	                    "Sign in"));
	            try {
	                loginPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
	                        HTTP.UTF_8));
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }

	            HttpResponse loginResponse = null;
	            try {
	                loginResponse = client.execute(loginPost, localContext);
	            } catch (ClientProtocolException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	            
	            String loginResponseBody = "";
	            try {
	                loginResponseBody = EntityUtils.toString(loginResponse.getEntity(), HTTP.UTF_8);
	            } catch (ParseException e1) {
	                // TODO Auto-generated catch block
	                e1.printStackTrace();
	            } catch (IOException e1) {
	                // TODO Auto-generated catch block
	                e1.printStackTrace();
	            }
	            
	            if (!loginResponseBody.contains("Home | LibraryThing")) {
	                this.publishProgress(PROGRESS_LOGIN_FAIL);
	                this.cancel(true);
	                loginPost.abort();
	            } else {
	                this.publishProgress(PROGRESS_LOGGED_IN);
	            }
	            
	            HttpGet downloadRequest = new HttpGet(
	                    "http://www.librarything.com/export-tab");
	            HttpResponse downloadResponse = null;
	            try {
	                downloadResponse = client.execute(downloadRequest,
	                        localContext);
	            } catch (ClientProtocolException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }

	            String downloadResponseBody = "";
	            try {
	                downloadResponseBody = EntityUtils.toString(downloadResponse
	                        .getEntity(), HTTP.UTF_16);
	            } catch (ParseException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }

	            result = downloadResponseBody;
	            
	            this.publishProgress(PROGRESS_SUCCESS);
	            return "";

	        }

	        protected void onPostExecute(String r) {
	            if (result.length() > 0) {
	                importBooksFromDownload(result);
	            } else {
	                //logger.log(TAG + METHOD, "the download was empty");
	            }
	        }

	    }
	   
	   /*public class BookListAdapter extends BaseAdapter {

	        private Context context;
	        LayoutInflater inflater;
	        private ArrayList<ArrayList<String>> columns = new ArrayList<ArrayList<String>>();
	        
	        public BookListAdapter (Context context, String[] columnNames) {
	            String METHOD = ".BookListAdapter constructor()";
	            this.context = context;
	            inflater = LayoutInflater.from(context);
	            //Log.i(TAG + METHOD, "performance_track BookListAdapter_tag1");
	            
	            for (int i = 0; i < columnNames.length; i++) {
	                columns.add(searchHandler.getColumnArray(columnNames[i]));
	            }
	            //Log.i(TAG + METHOD, "performance_track BookListAdapter_tag2");
	        }
	        
	        public int getCount() {
	            return columns.get(0).size();
	        }

	        public Object getItem(int position) {
	            return null;
	        }

	        public long getItemId(int position) {
	            // TODO Auto-generated method stub
	            return position;
	        }

	        public View getView(int position, View convertView, ViewGroup parent) {
	            if (convertView == null) {
	                convertView = inflater.inflate(R.layout.book_list_item, null);
	            }
	            TextView titleView = (TextView) convertView.findViewById(R.id.book_list_item_title);
	            TextView authorView = (TextView) convertView.findViewById(R.id.book_list_item_subtitle);
	            titleView.setText((columns.get(0).get(position)));
	            authorView.setText((columns.get(1).get(position)));
	            return convertView;
	        }
	    }*/

}
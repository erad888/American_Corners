package com.dosamericancorner.options;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.checkout.CheckoutScreen;
import com.dosamericancorner.customlistview.CustomErrorAdapter;
import com.dosamericancorner.customlistview.CustomUserAdapter;
import com.dosamericancorner.reports.CheckoutReportScreen;
import com.dosamericancorner.reports.ReportsByDateScreen;
import com.dosamericancorner.search.*;
import com.dosamericancorner.statistics.StatisticsAdapter;
import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.home.LoginDataBaseAdapter;
import com.dosamericancorner.inventory.*;
import com.dosamericancorner.login.R;
import com.dosamericancorner.membership.*;
import com.dosamericancorner.options.InventoryOptionScreen;

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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingScreen extends Activity {
	EditText inputUserName, inputPassword;
	Button buttonBackup, buttonAdd;
	CustomUserAdapter userAdapter;
	LoginDataBaseAdapter loginDataBaseAdapter;
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
		    loginDataBaseAdapter=new LoginDataBaseAdapter(this);
		    loginDataBaseAdapter=loginDataBaseAdapter.open();
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
	      

		  inputUserName=(EditText)findViewById(R.id.editTextUserNameToLogin);
		  inputPassword=(EditText)findViewById(R.id.editTextPasswordToLogin);
	      
	      buttonBackup = (Button)findViewById(R.id.btnBackup);
	      buttonAdd = (Button)findViewById(R.id.addUserButton);
	      
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
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Manage Members"))
	                        {
	                        	Intent i=new Intent(SettingScreen.this,ManageMemberScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Settings"))
	                        {
	                        	Intent i=new Intent(SettingScreen.this,SettingScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Help"))
	                        {
	                        	Intent i=new Intent(SettingScreen.this,HelpScreen.class);
	        					i.putExtra("username",userName);
	        					startActivity(i);
	        				  	finish();
	                        }
	                        if(menuOptions[position].equals("Sign Off"))
	                        {
	                        	Intent intentHome=new Intent(SettingScreen.this,HomeActivity.class);
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
		  
	      buttonBackup.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
	    		  	Intent i=new Intent(SettingScreen.this,DataBackupScreen.class);
					i.putExtra("username",userName);
					startActivity(i);
				  	finish();
	    	  }
	      });
	      
	      buttonAdd.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		// Add New User
	    		  new LTLoginCheck().execute(null, null, null);
	    	  }
	      });
	      
	      
	      
	      ArrayList<String> usersList = loginDataBaseAdapter.getUsernames();
			// add data in custom adapter
			userAdapter = new CustomUserAdapter(this, R.layout.users_row, usersList);
	        ListView dataList = (ListView) findViewById(R.id.userlist_row);
	        dataList.setAdapter(userAdapter);
	        
	        
	        


	      // Home Button
	      Button bh = (Button) findViewById(R.id.btnHomeBottom);
	      bh.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		/// Create Intent for HomeScreen  and Start The Activity
					Intent i=new Intent(SettingScreen.this,HomeScreen.class);
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
				Intent i=new Intent(SettingScreen.this,SearchScreen.class);
				i.putExtra("username",userName);
				startActivity(i);
			  	finish();
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
				  	finish();
	         } 
	      });
	   }

	   int PROGRESS_LOGGED_IN = 3;
	    int PROGRESS_SUCCESS = 4;
	    int PROGRESS_LOGIN_FAIL = 5;
		
		//LTLogin logs into LibraryThing
		   private class LTLoginCheck extends AsyncTask<Boolean, Integer, String> {
		        String result;        
		        ProgressDialog dialog;

		        
		        @Override
		        protected void onPreExecute() {
		            dialog = new ProgressDialog(SettingScreen.this);
		            dialog.setTitle("Checking User...");
		            dialog.setMessage("Verifying ID and Password");
		            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		            dialog.setCancelable(false);
		            dialog.setCanceledOnTouchOutside(false);
		            dialog.show();
		        }
		        
		        protected void onProgressUpdate(Integer... progUpdate) {
		            if (progUpdate[0] == PROGRESS_LOGGED_IN){  
		               dialog.setMessage("Successfully verified");
		               dialog.dismiss();

	            	   	String userName=inputUserName.getText().toString();
	   	    			String password=inputPassword.getText().toString();
		    			// Save the Data in Database
		    			loginDataBaseAdapter.insertEntry(userName, password);
		    			
                    	Intent i=new Intent(SettingScreen.this,SettingScreen.class);
    					i.putExtra("username",userName);
    					startActivity(i);
    				  	finish();
		               
		           } else if (progUpdate[0] == PROGRESS_LOGIN_FAIL) {
		               dialog.setMessage("Verification failed.");
		        	   dialog.dismiss();
		               Toast.makeText(SettingScreen.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
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
		                loginResponse = client.execute(loginPost);
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
		            return "";

		        }

		    }
	   
	   
}
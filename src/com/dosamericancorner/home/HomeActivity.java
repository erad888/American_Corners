package com.dosamericancorner.home;

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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.dosamericancorner.login.R;
import com.dosamericancorner.options.InventorySyncScreen;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class HomeActivity extends Activity 
{
	Button btnSignIn;
	LoginDataBaseAdapter loginDataBaseAdapter;
	EditText inputUserName, inputPassword;
	//HomeScreen obB=new HomeScreen();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.main);
	     
	     // create a instance of SQLite Database
	     loginDataBaseAdapter=new LoginDataBaseAdapter(this);
	     loginDataBaseAdapter=loginDataBaseAdapter.open();
	     
	     final Dialog dialog = new Dialog(HomeActivity.this);
	     
	  // get the References of views
		    inputUserName=(EditText)findViewById(R.id.editTextUserNameToLogin);
		    inputPassword=(EditText)findViewById(R.id.editTextPasswordToLogin);
		    
		    btnSignIn=(Button)findViewById(R.id.buttonSignIn);
			
	    // Set OnClick Listener on SignIn button 
	    btnSignIn.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		//String userName=inputUserName.getText().toString();
	    		//String password=inputPassword.getText().toString();
	    		
	    		
	    		new LTLogin().execute(null, null, null);
			
	    		
	    		/*
	    		// fetch the Password form database for respective user name
	    		String storedPassword=loginDataBaseAdapter.getSingleEntry(userName);
			
	    		// check if the Stored password matches with  Password entered by user
	    		if(password.equals(storedPassword))
	    		{
	    			// here i call new screen;
	    			Intent i = new Intent(HomeActivity.this, HomeScreen.class);
	    			i.putExtra("username",userName);
	    			startActivity(i);
	    		}
	    		else
	    		{
	    			Toast.makeText(HomeActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
	    		}
	    		*/
			}
		});

	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	    // Close The Database
		loginDataBaseAdapter.close();
	}
	

	
	int PROGRESS_LOGGED_IN = 3;
    int PROGRESS_SUCCESS = 4;
    int PROGRESS_LOGIN_FAIL = 5;
	
	//LTLogin logs into LibraryThing
	   private class LTLogin extends AsyncTask<Boolean, Integer, String> {
	        String result;        
	        ProgressDialog dialog;

	        
	        @Override
	        protected void onPreExecute() {
	            dialog = new ProgressDialog(HomeActivity.this);
	            dialog.setTitle("Logging in...");
	            dialog.setMessage("Verifying ID and Password");
	            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	            dialog.setCancelable(false);
	            dialog.setCanceledOnTouchOutside(false);
	            dialog.show();
	        }
	        
	        protected void onProgressUpdate(Integer... progUpdate) {
	            if (progUpdate[0] == PROGRESS_LOGGED_IN){  
	               dialog.setMessage("Successfully logged in");
	               dialog.dismiss();
	               
	               
	               if(loginDataBaseAdapter.getCount() == 0)
		    		{
	            	   	String userName=inputUserName.getText().toString();
	   	    			String password=inputPassword.getText().toString();
		    			// Save the Data in Database
		    			loginDataBaseAdapter.insertEntry(userName, password);
		    		}
	               
	               Intent in = new Intent(getApplicationContext(), HomeScreen.class);
	               startActivity(in);
	               finish();
	           } else if (progUpdate[0] == PROGRESS_LOGIN_FAIL) {
	               dialog.setMessage("Login failed.");
	        	   dialog.dismiss();
	               Toast.makeText(HomeActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
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

	            System.out.println("loginDataBaseAdapter.getCount(): " + loginDataBaseAdapter.getCount());
	            
	            if (!loginResponseBody.contains("Home | LibraryThing")) {
	            	System.out.println("Login Fail.");
	                this.publishProgress(PROGRESS_LOGIN_FAIL);
	                this.cancel(true);
	                loginPost.abort();
	            } else if(loginDataBaseAdapter.getCount() > 0)
	    		{
	            	System.out.println("Login Success.");
	            	String userName=inputUserName.getText().toString();
   	    			String password=inputPassword.getText().toString();
	            	if(loginDataBaseAdapter.isValid(userName, password) == true)
	            	{
		            	System.out.println("loginDataBaseAdapter.isValid(userName, password) == true");
		                this.publishProgress(PROGRESS_LOGGED_IN);
	            	} else {
		            	System.out.println("loginDataBaseAdapter.isValid(userName, password) == false");
		                this.publishProgress(PROGRESS_LOGIN_FAIL);
	            	}
	    		}
	            else {
	            	System.out.println("Login Success.");
	                this.publishProgress(PROGRESS_LOGGED_IN);
	            }
	            return "";

	        }

	    }
	
}

package com.dosamericancorner.home;

import com.dosamericancorner.login.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class HomeActivity extends Activity 
{
	Button btnSignIn, btnSignUp;
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
		    btnSignUp=(Button)findViewById(R.id.buttonSignUp);
			
	    // Set OnClick Listener on SignIn button 
	    btnSignIn.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		String userName=inputUserName.getText().toString();
	    		String password=inputPassword.getText().toString();
			
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
			}
		});
	    
	 // Set OnClick Listener on SignUp button 
	    btnSignUp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				/// Create Intent for SignUpActivity  and Start The Activity
				Intent intentSignUP=new Intent(getApplicationContext(),SignUPActivity.class);
				startActivity(intentSignUP);
			}
		});

	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	    // Close The Database
		loginDataBaseAdapter.close();
	}
}

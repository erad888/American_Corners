package com.dosamericancorner.membership;

import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.inventory.InventoryAdapter;
import com.dosamericancorner.login.R;
import com.dosamericancorner.options.HelpScreen;
import com.dosamericancorner.options.InventoryOptionScreen;
import com.dosamericancorner.options.SettingScreen;
import com.dosamericancorner.statistics.StatisticsAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MemberAddScreen extends Activity{
	EditText inputFirstName,inputLastName,inputBirthday,inputMemberId,inputEmail,inputNotes;
	Button buttonMemberAdd, buttonBack;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memberadd);
		
		MembershipAdapter=new MembershipAdapter(this);
	    MembershipAdapter=MembershipAdapter.open();
		
		Intent intent = getIntent();
	    final String userName = intent.getExtras().getString("username");
		
	    buttonMemberAdd = (Button)findViewById(R.id.buttonMemberAdd);
	    buttonBack = (Button)findViewById(R.id.buttonBack);
	    
	    inputFirstName = (EditText)findViewById(R.id.inputFirstName);
	    inputLastName = (EditText)findViewById(R.id.inputLastName);
	    inputBirthday = (EditText)findViewById(R.id.inputBirthday);
	    inputMemberId = (EditText)findViewById(R.id.inputMemberId);
	    inputEmail = (EditText)findViewById(R.id.inputEmail);
	    inputNotes = (EditText)findViewById(R.id.inputNotes);
		
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
                        	Intent i=new Intent(MemberAddScreen.this,InventoryOptionScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
                        }
                        if(menuOptions[position].equals("Manage Members"))
                        {
                        	Intent i=new Intent(MemberAddScreen.this,ManageMemberScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
                        }
                        if(menuOptions[position].equals("Settings"))
                        {
                        	Intent i=new Intent(MemberAddScreen.this,SettingScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
                        }
                        if(menuOptions[position].equals("Help"))
                        {
                        	Intent i=new Intent(MemberAddScreen.this,HelpScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
                        }
                        if(menuOptions[position].equals("Sign Off"))
                        {
                        	Intent intentHome=new Intent(MemberAddScreen.this,HomeActivity.class);
        				  	startActivity(intentHome);
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
	    		  String firstName = inputFirstName.getText().toString();
	    		  String lastName = inputLastName.getText().toString();
	    		  String birthday = inputBirthday.getText().toString();
	    		  String memberId = inputMemberId.getText().toString();
	    		  String email = inputEmail.getText().toString();
	    		  String notes = inputNotes.getText().toString();
	    		  
	    		  MembershipAdapter.insertEntry(firstName, lastName, birthday, memberId, email, 0, 0, notes);
	    		  
	    		  Toast.makeText(MemberAddScreen.this, "Member Successfully Added", Toast.LENGTH_LONG).show();
	    		  
					Intent i=new Intent(MemberAddScreen.this,ManageMemberScreen.class);
					i.putExtra("username",userName);
					startActivity(i);
	    	  }
	      });
        
        buttonBack.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
					Intent i=new Intent(MemberAddScreen.this,ManageMemberScreen.class);
					i.putExtra("username",userName);
					startActivity(i);
	    	  }
	      });
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}

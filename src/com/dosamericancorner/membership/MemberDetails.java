package com.dosamericancorner.membership;

import java.util.ArrayList;

import com.dosamericancorner.customlistview.CheckoutItem;
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
import com.dosamericancorner.checkout.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MemberDetails  extends Activity{
	TextView firstName, lastName, memberID, birthday, numCheckouts, karmaPts, email, notes, itemsOnLoan;
	Button buttonEditMember, buttonReturnAll;
	Button titleButton, ISBNButton, checkoutButton, dueDateButton;
	
	ArrayList<CheckoutItem> items = new ArrayList<CheckoutItem>();
	MemberItemAdapter adapter;
	MembershipAdapter MembershipAdapter;
	CheckOutDataBaseAdapter CheckOutDataBaseAdapter;
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
		setContentView(R.layout.member_details);
		
		MembershipAdapter=new MembershipAdapter(this);
	    MembershipAdapter=MembershipAdapter.open();
	    CheckOutDataBaseAdapter=new CheckOutDataBaseAdapter(this);
	    CheckOutDataBaseAdapter=CheckOutDataBaseAdapter.open();
		
		Intent intent = getIntent();
	    final String userName = intent.getExtras().getString("username");
	    final String FirstName = intent.getExtras().getString("FirstName");
	    final String LastName = intent.getExtras().getString("LastName");
	    final String Birthday = intent.getExtras().getString("Birthday");
	    final String MemberID = intent.getExtras().getString("MemberID");
	    final String Email = intent.getExtras().getString("Email");
	    final String Notes = intent.getExtras().getString("Notes");
	    final int NumCheckouts = intent.getExtras().getInt("NumCheckouts");
	    final int KarmaPts = intent.getExtras().getInt("KarmaPts");
		
	    buttonEditMember = (Button)findViewById(R.id.buttonEdit);
	    buttonReturnAll = (Button)findViewById(R.id.buttonReturn);
	    
	    titleButton = (Button)findViewById(R.id.titleButton);
	    ISBNButton = (Button)findViewById(R.id.ISBNButton);
	    checkoutButton = (Button)findViewById(R.id.checkoutButton);
	    dueDateButton = (Button)findViewById(R.id.dueDateButton);
	    
	    firstName = (TextView)findViewById(R.id.FirstName);
	    lastName = (TextView)findViewById(R.id.LastName);
	    birthday = (TextView)findViewById(R.id.Birthday);
	    memberID = (TextView)findViewById(R.id.MemberID);
	    email = (TextView)findViewById(R.id.Email);
	    notes = (TextView)findViewById(R.id.Notes);
	    numCheckouts = (TextView)findViewById(R.id.CheckoutCount);
	    karmaPts = (TextView)findViewById(R.id.KarmaPts);
	    
	    firstName.setText(FirstName);
	    lastName.setText(LastName);
	    birthday.setText("Birthday:  "+Birthday);
	    memberID.setText("Member ID: "+MemberID);
	    email.setText(Email);
	    notes.setText(Notes);
	    numCheckouts.setText("# of Checkouts: "+NumCheckouts);
	    karmaPts.setText("Karma Points: "+KarmaPts);
	    
	    int numEntries = CheckOutDataBaseAdapter.getNumEntriesByMember(MemberID);
	    String[][] entries = new String[numEntries][6];
	    entries = CheckOutDataBaseAdapter.getEntriesByMember(MemberID);
	    for(int i = 0; i < numEntries; i++)
	    	items.add(new CheckoutItem(entries[i][3],entries[i]));
	    
	    adapter = new MemberItemAdapter(this, R.layout.memberitem_row, items);
        ListView dataList = (ListView) findViewById(R.id.memberitem_row);
        dataList.setAdapter(adapter);
        
        // Sorting
        titleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.filter(0);
				adapter.notifyDataSetChanged();
			}
        });
        
        ISBNButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.filter(1);
				adapter.notifyDataSetChanged();
			}
        });
        
        checkoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.filter(2);
				adapter.notifyDataSetChanged();
			}
        });
        
        dueDateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.filter(3);
				adapter.notifyDataSetChanged();
			}
        });
        
        buttonEditMember.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		// Then you start a new Activity via Intent
                  Intent i = new Intent();
                  i.setClass(MemberDetails.this, MemberEdit.class);
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
	    	  }
	      });
      
      buttonReturnAll.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Alert Dialog to confirm Return All Items
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						MemberDetails.this);
		 
					// set title
					alertDialogBuilder.setTitle("Confirm");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage("Return All Items?")
						.setCancelable(false)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, return all current items
								int num = CheckOutDataBaseAdapter.getNumEntriesByMember(MemberID);
								String[][] itemEntries = new String[num][6];
							    itemEntries = CheckOutDataBaseAdapter.getEntriesByMember(MemberID);
								for(int i = 0; i < num; i++)
								{
							    	CheckOutDataBaseAdapter.deleteItem(itemEntries[i][1], itemEntries[i][2], itemEntries[i][3]);
								}
								items = new ArrayList<CheckoutItem>();
								adapter.notifyDataSetChanged();
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
      
      dataList.setOnItemClickListener(new OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position,
                  long id) {
        	  int entryPos = -1;
        	  String[][] memberItems = CheckOutDataBaseAdapter.getEntriesByMember(adapter.getItem(position).getDetails()[3]);
        	  for(int x = 0; x < memberItems.length; x++)
        	  {
        		  if(memberItems[x][3].equals(adapter.getItem(position).getisbn()))
        			  entryPos = x;
        	  }
        	  if(entryPos == -1)
        	  {
        		  
        	  }
        		  
        	  
          	// Then you start a new Activity via Intent
              Intent i = new Intent();
              i.setClass(MemberDetails.this, ItemManagement.class);
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
              i.putExtra("FirstName",FirstName);
              i.putExtra("LastName",LastName);
              i.putExtra("Birthday",Birthday);
              i.putExtra("Email",Email);
              i.putExtra("Notes",Notes);
              i.putExtra("NumCheckouts",NumCheckouts); // type int
              i.putExtra("KarmaPts",KarmaPts); // type int
              startActivity(i);
          }
      });
        
		
        // Menu Spinner
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
                        	Intent i=new Intent(MemberDetails.this,InventoryOptionScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
                        }
                        if(menuOptions[position].equals("Manage Members"))
                        {
                        	Intent i=new Intent(MemberDetails.this,ManageMemberScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
                        }
                        if(menuOptions[position].equals("Settings"))
                        {
                        	Intent i=new Intent(MemberDetails.this,SettingScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
                        }
                        if(menuOptions[position].equals("Help"))
                        {
                        	Intent i=new Intent(MemberDetails.this,HelpScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
                        }
                        if(menuOptions[position].equals("Sign Off"))
                        {
                        	Intent intentHome=new Intent(MemberDetails.this,HomeActivity.class);
        				  	startActivity(intentHome);
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
					Intent intentHome=new Intent(MemberDetails.this,HomeScreen.class);
					intentHome.putExtra("username",userName);
					startActivity(intentHome);
	    		  
	    	  }
	      });
	      
	      // Search Button
	      Button bs = (Button) findViewById(R.id.btnSearchBottom);
	      bs.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		  	Intent i = new Intent(MemberDetails.this, SearchScreen.class);
			        i.putExtra("username",userName);
			        startActivity(i);
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(MemberDetails.this, ReportsByDateScreen.class);
		         i.putExtra("username",userName);
		         i.putExtra("startMonth",0);
		         i.putExtra("startYear", 0);
		         i.putExtra("endMonth", 0);
		         i.putExtra("endYear", 0);
		         startActivity(i);
	         } 
	      });
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MembershipAdapter.close();
		CheckOutDataBaseAdapter.close();
	}

}

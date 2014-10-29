package com.dosamericancorner.membership;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.customlistview.CustomCheckoutAdapter;
import com.dosamericancorner.customlistview.CustomReportAdapter;
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

public class MemberTableScreen extends Activity{

	TextView firstNameButton, lastNameButton, birthdayButton,
	memberNumberButton, emailButton, checkoutCountButton, karmaPtsButton,
	notesButton, memberTableHeader, startDateText;
	EditText searchMember;
	Button searchButton;
	
	StatisticsAdapter StatisticsAdapter;
	InventoryAdapter InventoryAdapter;
	CheckOutDataBaseAdapter CheckOutDataBaseAdapter;
	MembershipAdapter MembershipAdapter;
	
	
	String memberId, reportStartDate;
	ArrayList<MemberItem> memberArray = new ArrayList<MemberItem>();
	MemberTableAdapter adapter;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.membertable);
		
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
 		int startMonth = (defaultStart.get(Calendar.MONTH)+1);
 		int startYear = defaultStart.get(Calendar.YEAR);
	     
 		startDateText = (TextView)findViewById(R.id.startDateText);
	    startDateText.setText(reportStartDate);
	    
	    memberTableHeader = (TextView) findViewById(R.id.membersTableHeader);
	    
	    // search members
	    searchMember = (EditText) findViewById(R.id.inputSearch);
	    searchButton = (Button) findViewById(R.id.buttonSearch);
	    
	    // sort members
	    firstNameButton = (TextView) findViewById(R.id.firstNameButton);
	    lastNameButton = (TextView) findViewById(R.id.lastNameButton);
	    birthdayButton = (TextView) findViewById(R.id.birthdayButton);
	    memberNumberButton = (TextView) findViewById(R.id.memberIdButton);
	    emailButton = (TextView) findViewById(R.id.emailButton);
	    checkoutCountButton = (TextView) findViewById(R.id.checkoutCountButton);
	    karmaPtsButton = (TextView) findViewById(R.id.karmaPtsButton);
	    notesButton = (TextView) findViewById(R.id.notesButton);
	    
	    // add data in membership table adapter
	    
	    final ProgressDialog progress = ProgressDialog.show(MemberTableScreen.this, "Loading Table",
  			  "Please wait a moment.", true);
  	
	    new Thread(new Runnable() {
  		  @Override
  		  public void run()
  		  {
  		    // do the thing that takes a long time
  			// iterate through all members
        	    int numEntries = MembershipAdapter.countMembers();
        	    String[][] records = MembershipAdapter.getAll();
        	    for(int i = 0; i < numEntries; i++)
        	    {
        	    	memberId = records[i][3];
        	    	memberArray.add(new MemberItem(memberId,records[i]));
        	    	System.out.println("memberArray["+i+"] = "+records[i][0]+" "+records[i][1]);
        	    }
        	    String[] blank = new String[8];
        	    for(int j = 0; j < 8; j++)
        	    {
        	    	blank[j] = " ";
        	    }
        	    memberArray.add(new MemberItem(memberId,blank));
        	    String[] addNew = new String[8];
        	    for(int j = 0; j < 8; j = j+2)
        	    {
        	    	addNew[j] = "Add New";
        	    }
        	    for(int j = 1; j < 8; j = j+2)
        	    {
        	    	addNew[j] = "...";
        	    }
        	    memberArray.add(new MemberItem(memberId,addNew));
        	    
        	    adapter = new MemberTableAdapter(MemberTableScreen.this, R.layout.memberlist_row, memberArray);
        	    ListView dataList = (ListView) findViewById(R.id.memberlist_row);
                dataList.setAdapter(adapter);
  			  
  		    runOnUiThread(new Runnable() {
  		      @Override
  		      public void run()
  		      {
  		        progress.dismiss();
  		      }
  		    });
  		  }
  		}).start();
	    
	    //updateTableList();
	    
        
        // Search Members
        searchButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		String searchItem = searchMember.getText().toString();
        		
        		if(searchItem.equals(""))
        		{
        			Toast.makeText(getApplicationContext(), "Field Vaccant.", Toast.LENGTH_LONG).show();
					return;
        		}
        		else
        		{
        			memberTableHeader.setText("Entries matching '"+searchItem+"'");
        			startDateText.setText("");
        			
        			memberArray = new ArrayList<MemberItem>();
        			int numEntries = MembershipAdapter.countMatchingMembers(searchItem);
        		    String[][] records = MembershipAdapter.getAllMatching(searchItem);
        		    for(int i = 0; i < numEntries; i++)
        		    {
        		    	memberId = records[i][3];
        		    	memberArray.add(new MemberItem(memberId,records[i]));
        		    	System.out.println("memberArray["+i+"] = "+records[i][0]+" "+records[i][1]);
        		    }
        	        ListView dataList = (ListView) findViewById(R.id.memberlist_row);
        	        dataList.setAdapter(adapter);
        			
        		}
        	}
        });
        
        
        firstNameButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.filter(0);
				adapter.notifyDataSetChanged();
			}
        });
        
        lastNameButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.filter(1);
				adapter.notifyDataSetChanged();
			}
        });
        
        birthdayButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.filter(2);
				adapter.notifyDataSetChanged();
			}
        });
        
        memberNumberButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.filter(3);
				adapter.notifyDataSetChanged();
			}
        });
        
        emailButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.filter(4);
				adapter.notifyDataSetChanged();
			}
        });
        
        checkoutCountButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.filter(5);
				adapter.notifyDataSetChanged();
			}
        });
        
        karmaPtsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.filter(6);
				adapter.notifyDataSetChanged();
			}
        });
        
        notesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.filter(7);
				adapter.notifyDataSetChanged();
			}
        });
        
       ListView dataList = (ListView) findViewById(R.id.memberlist_row);
     // On Click ========================================================
        dataList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	
            	if((adapter.getItem(position).getDetails()[0]).equals("Add New"))
            	{
            		// Then you start a new Activity via Intent
                    Intent i = new Intent();
                    i.setClass(MemberTableScreen.this, MemberAddScreen.class);
                    i.putExtra("username",userName);
                    startActivity(i);
				  	finish();
            	}
            	else if((adapter.getItem(position).getDetails()[0]).equals(" "))
            	{
            		// Do Nothing. Blank Row.
            		return;
            	}
            	else
            	{
            		// Then you start a new Activity via Intent
                    Intent i = new Intent();
                    i.setClass(MemberTableScreen.this, MemberDetails.class);
                    i.putExtra("username",userName);
	                // 0 = first name
             	    // 1 = last name
             	    // 2 = birthday
             	    // 3 = member Id
             	    // 4 = email
                	// 5 = checkout count
                	// 6 = karma pts
                	// 7 = notes
                    i.putExtra("FirstName",adapter.getItem(position).getDetails()[0]);
                    i.putExtra("LastName",adapter.getItem(position).getDetails()[1]);
                    i.putExtra("Birthday",adapter.getItem(position).getDetails()[2]);
                    i.putExtra("MemberID",adapter.getItem(position).getDetails()[3]);
                    i.putExtra("Email",adapter.getItem(position).getDetails()[4]);
                    i.putExtra("Notes",adapter.getItem(position).getDetails()[7]);
                    i.putExtra("NumCheckouts",Integer.parseInt(adapter.getItem(position).getDetails()[5])); // type int
                    i.putExtra("KarmaPts",Integer.parseInt(adapter.getItem(position).getDetails()[6])); // type int
                    startActivity(i);
				  	finish();
            	}
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
                        	Intent i=new Intent(MemberTableScreen.this,InventoryOptionScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Manage Members"))
                        {
                        	Intent i=new Intent(MemberTableScreen.this,ManageMemberScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Settings"))
                        {
                        	Intent i=new Intent(MemberTableScreen.this,SettingScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Help"))
                        {
                        	Intent i=new Intent(MemberTableScreen.this,HelpScreen.class);
        					i.putExtra("username",userName);
        					startActivity(i);
        				  	finish();
                        }
                        if(menuOptions[position].equals("Sign Off"))
                        {
                        	Intent intentHome=new Intent(MemberTableScreen.this,HomeActivity.class);
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
					Intent intentHome=new Intent(MemberTableScreen.this,HomeScreen.class);
					intentHome.putExtra("username",userName);
					startActivity(intentHome);
				  	finish();
	    		  
	    	  }
	      });
	      
	      // Search Button
	      Button bs = (Button) findViewById(R.id.btnSearchBottom);
	      bs.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		  	Intent i = new Intent(MemberTableScreen.this, SearchScreen.class);
			        i.putExtra("username",userName);
			        startActivity(i);
				  	finish();
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(MemberTableScreen.this, ReportsByDateScreen.class);
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
		// TODO Auto-generated method stub
		super.onDestroy();
		StatisticsAdapter.close();
		CheckOutDataBaseAdapter.close();
		MembershipAdapter.close();
		InventoryAdapter.close();
	}
}

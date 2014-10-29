package com.dosamericancorner.membership;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.dosamericancorner.customlistview.CheckoutItem;
import com.dosamericancorner.home.HomeActivity;
import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.inventory.*;
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
import android.view.LayoutInflater;
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


public class ItemManagement extends Activity {
	TextView checkoutIndividual, memberID, title, author, ISBNText, callNumber, year, checkoutDate, dueDate, defaultLoan, currentLoan;
	Button buttonEdit, buttonReturn, buttonBack;
	
	ArrayList<CheckoutItem> items = new ArrayList<CheckoutItem>();
	MemberItemAdapter adapter;
	InventoryAdapter InventoryAdapter;
	MembershipAdapter MembershipAdapter;
	CheckOutDataBaseAdapter CheckOutDataBaseAdapter;
	StatisticsAdapter StatisticsAdapter;
	String todayDate;
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
		setContentView(R.layout.item_management);
		
		InventoryAdapter=new InventoryAdapter(this);
	    InventoryAdapter=InventoryAdapter.open();
		CheckOutDataBaseAdapter=new CheckOutDataBaseAdapter(this);
		CheckOutDataBaseAdapter=CheckOutDataBaseAdapter.open();
		StatisticsAdapter=new StatisticsAdapter(this);
	    StatisticsAdapter=StatisticsAdapter.open();
		MembershipAdapter=new MembershipAdapter(this);
	    MembershipAdapter=MembershipAdapter.open();
		
		Intent intent = getIntent();
	    final String userName = intent.getExtras().getString("username");
	    final String CheckoutIndividual = intent.getExtras().getString("CheckoutIndividual");
	    final String MemberID = intent.getExtras().getString("MemberID");
	    final String CheckoutDate = intent.getExtras().getString("CheckoutDate");
	    final String DueDate = intent.getExtras().getString("DueDate");
	    final String ISBN = intent.getExtras().getString("ISBN");
	    final String Title = intent.getExtras().getString("Title");
	    final String Author = intent.getExtras().getString("Author");
	    final String FirstName = intent.getExtras().getString("FirstName");
	    final String LastName = intent.getExtras().getString("LastName");
	    final String Birthday = intent.getExtras().getString("Birthday");
	    final String Email = intent.getExtras().getString("Email");
	    final String Notes = intent.getExtras().getString("Notes");
	    final int NumCheckouts = intent.getExtras().getInt("NumCheckouts");
	    final int KarmaPts = intent.getExtras().getInt("KarmaPts");
	    
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
 		todayDate = curYear+"-"+curMonth+"-"+curDay;
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 		Calendar cal1 = new GregorianCalendar();
 	    new GregorianCalendar();
		Calendar cal2 = GregorianCalendar.getInstance();
 	    Date date = new Date();
		try {
			date = sdf.parse(CheckoutDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	    cal1.setTime(date);
 	    // days between checkout date and today
 	    int daysBetweenCheckToday = (int)( (cal2.getTime().getTime() - cal1.getTime().getTime()) / (1000 * 60 * 60 * 24));
 	    
	    checkoutIndividual = (TextView)findViewById(R.id.CheckoutIndividualText);
	    memberID = (TextView)findViewById(R.id.MemberIdText);
	    title = (TextView)findViewById(R.id.titleText);
	    author = (TextView)findViewById(R.id.authorText);
	    ISBNText = (TextView)findViewById(R.id.ISBNText);
	    callNumber = (TextView)findViewById(R.id.callNumberText);
	    year = (TextView)findViewById(R.id.PublishYearText);
	    checkoutDate = (TextView)findViewById(R.id.CheckoutDateText);
	    dueDate = (TextView)findViewById(R.id.DueDateText);
	    defaultLoan = (TextView)findViewById(R.id.DefaultLoanText);
	    currentLoan = (TextView)findViewById(R.id.LoanLengthText);
	    
	    /* 0. ISBN
		 * 1. Title
		 * 2. Author
		 * 3. Publish Date
		 * 4. Call Number
		 * 5. Available Inventory Count
		 * 6. Inventory Count
		 * 7. Default Due Period
		 * 8. Checkout Count
		 * */
	    String[] itemData = com.dosamericancorner.inventory.InventoryAdapter.getInventoryByISBN(ISBN);
	    
	    checkoutIndividual.setText(CheckoutIndividual);
	    memberID.setText(MemberID);
	    title.setText(Title);
	    author.setText(Author);
	    ISBNText.setText(ISBN);
	    System.out.println("itemData[4]: "+itemData[4]);
	    callNumber.setText(itemData[4]);
	    System.out.println("itemData[3]: "+itemData[3]);
	    year.setText(itemData[3]);
	    checkoutDate.setText(CheckoutDate);
	    dueDate.setText(DueDate);
	    System.out.println("itemData[7]: "+itemData[3]);
	    defaultLoan.setText(itemData[7]);
	    currentLoan.setText(((Integer)daysBetweenCheckToday).toString());
	    
	    buttonEdit = (Button)findViewById(R.id.buttonModify);
	    buttonEdit.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		  
	    		// get prompts.xml view
					LayoutInflater li = LayoutInflater.from(getBaseContext());
					View promptsView = li.inflate(R.layout.prompts, null);
	    		  
	    		  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							ItemManagement.this);
	    		  
	    		// set prompts.xml to alertdialog builder
					alertDialogBuilder.setView(promptsView);
					
					final EditText userInput = (EditText) promptsView
							.findViewById(R.id.editTextDialogUserInput);
	    		  
	    		// set dialog message
					alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("OK",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
							// get user input and set it to result
							// update CheckOutDataBaseAdapter entry and edit text
						    String inputText = userInput.getText().toString();
						    CheckOutDataBaseAdapter.updateEntry(userName, CheckoutIndividual, MemberID, ISBN, CheckoutDate, inputText);
							dueDate.setText(userInput.getText());
							dialog.cancel();
						    }
						  })
						.setNegativeButton("Cancel",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						    }
						  });
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
	    	  }
	      });
	    
	    buttonReturn = (Button)findViewById(R.id.buttonReturn);
	    buttonReturn.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		// Alert Dialog to confirm Return All Items
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							ItemManagement.this);
			 
						// set title
						alertDialogBuilder.setTitle("Confirm");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("Return this Item?")
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, return all current items
									
									//define today's date
									Calendar curDate = Calendar.getInstance();
									String curYear = ((Integer)curDate.get(Calendar.YEAR)).toString();
									String curMonth = ((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
									String curDay = ((Integer)curDate.get(Calendar.DAY_OF_MONTH)).toString();
									if(curDate.get(Calendar.MONTH)+1 < 10)
										curMonth = "0"+((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
									if(curDate.get(Calendar.DAY_OF_MONTH) < 10)
										curDay = "0"+((Integer)(curDate.get(Calendar.DAY_OF_MONTH))).toString();
									String currentDate = curYear+"-"+curMonth+"-"+curDay;
									
									int num = CheckOutDataBaseAdapter.getNumEntriesByMember(MemberID);
									String[][] itemEntries = new String[num][6];
								    itemEntries = CheckOutDataBaseAdapter.getEntriesByMember(MemberID);
									for(int i = 0; i < num; i++)
									{
										if(MemberID.equals(itemEntries[i][2]))
										{
											CheckOutDataBaseAdapter.deleteItem(itemEntries[i][1], itemEntries[i][2], itemEntries[i][3]);
											InventoryAdapter.increaseCount(ISBN);
											StatisticsAdapter.increaseCount(ISBN);
											if(itemEntries[i][5].compareTo(currentDate) < 1)
												MembershipAdapter.decreaseKarma(itemEntries[i][2]);
											else
												MembershipAdapter.increaseKarma(itemEntries[i][2]);
										}
									}
									// Then you start a new Activity via Intent
					                  Intent i = new Intent();
					                  i.setClass(ItemManagement.this, MemberDetails.class);
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
			        				  	finish();
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
	    
	    buttonBack = (Button)findViewById(R.id.buttonBack);
	    buttonBack.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		// Then you start a new Activity via Intent
                  Intent i = new Intent();
                  i.setClass(ItemManagement.this, MemberDetails.class);
                  i.putExtra("username",userName);
	            // 0 = first name
           	    // 1 = last name
           	    // 2 = birthday
           	    // 3 = member Id
           	    // 4 = email
              	// 5 = checkout count
              	// 6 = karma pts
              	// 7 = notes
                  i.putExtra("FirstName",FirstName);
                  i.putExtra("LastName",LastName);
                  i.putExtra("Birthday",Birthday);
                  i.putExtra("MemberID",MemberID);
                  i.putExtra("Email",Email);
                  i.putExtra("Notes",Notes);
                  i.putExtra("NumCheckouts",NumCheckouts); // type int
                  i.putExtra("KarmaPts",KarmaPts); // type int
                  startActivity(i);
				  	finish();
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
	                         	Intent i=new Intent(ItemManagement.this,InventoryOptionScreen.class);
	         					i.putExtra("username",userName);
	         					startActivity(i);
	        				  	finish();
	                         }
	                         if(menuOptions[position].equals("Manage Members"))
	                         {
	                         	Intent i=new Intent(ItemManagement.this,ManageMemberScreen.class);
	         					i.putExtra("username",userName);
	         					startActivity(i);
	        				  	finish();
	                         }
	                         if(menuOptions[position].equals("Settings"))
	                         {
	                         	Intent i=new Intent(ItemManagement.this,SettingScreen.class);
	         					i.putExtra("username",userName);
	         					startActivity(i);
	        				  	finish();
	                         }
	                         if(menuOptions[position].equals("Help"))
	                         {
	                         	Intent i=new Intent(ItemManagement.this,HelpScreen.class);
	         					i.putExtra("username",userName);
	         					startActivity(i);
	        				  	finish();
	                         }
	                         if(menuOptions[position].equals("Sign Off"))
	                         {
	                         	Intent intentHome=new Intent(ItemManagement.this,HomeActivity.class);
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
					Intent intentHome=new Intent(ItemManagement.this,HomeScreen.class);
					intentHome.putExtra("username",userName);
					startActivity(intentHome);
				  	finish();
	    		  
	    	  }
	      });
	      
	      // Search Button
	      Button bs = (Button) findViewById(R.id.btnSearchBottom);
	      bs.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		  	Intent i = new Intent(ItemManagement.this, SearchScreen.class);
			        i.putExtra("username",userName);
			        startActivity(i);
				  	finish();
		         } 
	      });
	      
	      // Reports Button
	      Button br = (Button) findViewById(R.id.btnReportsBottom);
	      br.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {
	        	 Intent i = new Intent(ItemManagement.this, ReportsByDateScreen.class);
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
		MembershipAdapter.close();
		CheckOutDataBaseAdapter.close();
		InventoryAdapter.close();
		StatisticsAdapter.close();
	}
	
	
}

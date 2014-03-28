package com.dosamericancorner.membership;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

import com.dosamericancorner.checkout.CheckOutDataBaseAdapter;
import com.dosamericancorner.inventory.InventoryAdapter;
import com.dosamericancorner.login.R;
import com.dosamericancorner.statistics.StatisticsAdapter;

public class MemberTableScreen extends Activity{

	TextView firstNameButton, lastNameButton, birthdayButton,
	memberNumberButton, emailButton, checkoutCountButton, karmaPtsButton,
	notesButton;
	
	StatisticsAdapter StatisticsAdapter;
	InventoryAdapter InventoryAdapter;
	CheckOutDataBaseAdapter CheckOutDataBaseAdapter;
	MembershipAdapter MembershipAdapter;
	
	String memberNumber, reportStartDate;
	ArrayList<MemberItem> memberArray = new ArrayList<MemberItem>();
	MemberTableAdapter adapter;
	MembershipAdapter membershipAdapter;
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
		final int type = intent.getExtras().getInt("type");
		
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
	     
 		TextView startDateText = (TextView)findViewById(R.id.startDateText);
	    startDateText.setText(reportStartDate);
	    
	    
	    
	   
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

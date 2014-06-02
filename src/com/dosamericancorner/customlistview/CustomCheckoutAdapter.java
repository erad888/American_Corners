package com.dosamericancorner.customlistview;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dosamericancorner.login.R;

public class CustomCheckoutAdapter extends ArrayAdapter<CheckoutItem> {
	Context context;
    int layoutResourceId;
    LinearLayout linearMain;
    ArrayList<CheckoutItem> data = new ArrayList<CheckoutItem>();

    public CustomCheckoutAdapter(Context context, int layoutResourceId,
                  ArrayList<CheckoutItem> data) {
           super(context, layoutResourceId, data);
           this.layoutResourceId = layoutResourceId;
           this.context = context;
           this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;

       if (convertView == null) {
              LayoutInflater inflater = ((Activity) context).getLayoutInflater();
              row = inflater.inflate(layoutResourceId, parent, false);
              //Make sure the textview exists in this xml
       } else {
              row = convertView;
       }
       
     //define Report Start date
		Calendar curDate = Calendar.getInstance();
		String curYear = ((Integer)curDate.get(Calendar.YEAR)).toString();
		String curMonth = ((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
		String curDay = ((Integer)curDate.get(Calendar.DAY_OF_MONTH)).toString();
		if(curDate.get(Calendar.MONTH)+1 < 10)
			curMonth = "0"+((Integer)(curDate.get(Calendar.MONTH)+1)).toString();
		if(curDate.get(Calendar.DAY_OF_MONTH) < 10)
			curDay = "0"+((Integer)(curDate.get(Calendar.DAY_OF_MONTH))).toString();
		String currentDate = curYear+"-"+curMonth+"-"+curDay;

       CheckoutItem myItem = data.get(position);
       TextView titleLabel = (TextView) row.findViewById(R.id.titleText);
       titleLabel.setText(myItem.details[0]);
       TextView authorLabel = (TextView) row.findViewById(R.id.authorText);
       authorLabel.setText(myItem.details[1]);
       TextView checkoutIndividual = (TextView) row.findViewById(R.id.checkoutIndividual);
       checkoutIndividual.setText(myItem.details[2]);
       TextView memberID = (TextView) row.findViewById(R.id.memberID);
       memberID.setText(myItem.details[3]);
       TextView checkoutDate = (TextView) row.findViewById(R.id.checkoutDate);
       checkoutDate.setText(myItem.details[4]);
       TextView dueDate = (TextView) row.findViewById(R.id.dueDate);
       dueDate.setText(myItem.details[5]);
       
       if(myItem.details[5].compareTo(currentDate) < 1)
       {
    	   titleLabel.setTextColor(Color.parseColor("#ff2626"));
    	   authorLabel.setTextColor(Color.parseColor("#ff2626"));
    	   checkoutIndividual.setTextColor(Color.parseColor("#ff2626"));
    	   memberID.setTextColor(Color.parseColor("#ff2626"));
    	   checkoutDate.setTextColor(Color.parseColor("#ff2626"));
    	   dueDate.setTextColor(Color.parseColor("#ff2626"));
       }

       return row;
    }
    
    // Adam added filter method
    public void filter(int type) {
    	// 0 = title
    	// 1 = author
    	// 2 = checkout individual
    	// 3 = member Id
    	// 4 = checkout date
    	// 5 = due date
    	for(int i=0; i<data.size(); i++) {
 		   data.get(i).setType(type);
 		   for(int j=i+1; j<data.size(); j++) {
 			   if (data.get(i).compareTo(data.get(j))>0) {
 				   String tempIsbn = data.get(j).getisbn();
 				   String[] tempDetails = data.get(j).getDetails();
 				   data.get(j).setisbn(data.get(i).getisbn());
 				   data.get(j).setDetails(data.get(i).getDetails());
 				   data.get(i).setisbn(tempIsbn);
 				   data.get(i).setDetails(tempDetails);
 			   }
 		   }
 	   }
    }
}

package com.dosamericancorner.customlistview;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dosamericancorner.login.R;


public class CustomReportAdapter extends ArrayAdapter<ReportsItem> {
       Context context;
       int layoutResourceId;
       LinearLayout linearMain;
       ArrayList<ReportsItem> data = new ArrayList<ReportsItem>();

       public CustomReportAdapter(Context context, int layoutResourceId,
                     ArrayList<ReportsItem> data) {
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

          ReportsItem myItem = data.get(position);
          TextView titleLabel = (TextView) row.findViewById(R.id.titleText);
          titleLabel.setText(myItem.details[0]);
          TextView authorLabel = (TextView) row.findViewById(R.id.authorText);
          authorLabel.setText(myItem.details[1]);
          TextView callNumberLabel = (TextView) row.findViewById(R.id.callNumberText);
          callNumberLabel.setText(myItem.details[2]);
          TextView publishYearLabel = (TextView) row.findViewById(R.id.publishYearText);
          publishYearLabel.setText(myItem.details[3]);
          TextView checkoutCountLabel = (TextView) row.findViewById(R.id.checkoutCountText);
          checkoutCountLabel.setText(myItem.details[4]);

          return row;
       }
       
       //ADAM ADDED
       public void filter(int type) {
    	   // 0 = title
    	   // 1 = author
    	   // 2 = call number
    	   // 3 = publish year
    	   // 4 = check out count
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
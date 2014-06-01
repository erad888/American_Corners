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

public class CustomMemberAdapter extends ArrayAdapter<MemberItem> {
	Context context;
    int layoutResourceId;
    LinearLayout linearMain;
    ArrayList<MemberItem> data = new ArrayList<MemberItem>();

    public CustomMemberAdapter(Context context, int layoutResourceId,
                  ArrayList<MemberItem> data) {
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

		// 0 = first name
	    // 1 = last name
	    // 2 = birthday
	    // 3 = member id
	    // 4 = email
		// 5 = checkout count
		// 6 = karma points
/*       MemberItem myItem = data.get(position);
       TextView firstLabel = (TextView) row.findViewById(R.id.firstText);
       firstLabel.setText(myItem.details[0]);
       TextView lastLabel = (TextView) row.findViewById(R.id.lastText);
       lastLabel.setText(myItem.details[1]);
       TextView birthday = (TextView) row.findViewById(R.id.birthday);
       birthday.setText(myItem.details[2]);
       TextView memberID = (TextView) row.findViewById(R.id.memberID);
       memberID.setText(myItem.details[3]);
       TextView email = (TextView) row.findViewById(R.id.email);
       email.setText(myItem.details[4]);
       TextView checkout = (TextView) row.findViewById(R.id.checkout);
       checkout.setText(myItem.details[5]);
       TextView karma = (TextView) row.findViewById(R.id.karma);
       karma.setText(myItem.details[6]);
       
       if(Integer.parseInt(myItem.details[6]) < 0)
       {
    	   firstLabel.setTextColor(Color.parseColor("#ff2626"));
    	   lastLabel.setTextColor(Color.parseColor("#ff2626"));
    	   birthday.setTextColor(Color.parseColor("#ff2626"));
    	   memberID.setTextColor(Color.parseColor("#ff2626"));
    	   email.setTextColor(Color.parseColor("#ff2626"));
    	   checkout.setTextColor(Color.parseColor("#ff2626"));
    	   karma.setTextColor(Color.parseColor("#ff2626"));
       }
*/
       return row;
    }
    
    public void filter(int type) {
    	// 0 = first name
	    // 1 = last name
	    // 2 = birthday
	    // 3 = member id
	    // 4 = email
		// 5 = checkout count
		// 6 = karma points
    	for(int i=0; i<data.size(); i++) {
 		   data.get(i).setType(type);
 		   for(int j=i+1; j<data.size(); j++) {
 			   if (data.get(i).getId().compareTo(data.get(j).getId())>0) {
 				   String tempID = data.get(j).getId();
 				   String[] tempDetails = data.get(j).getDetails();
 				   data.get(j).setId(data.get(i).getId());
 				   data.get(j).setDetails(data.get(i).getDetails());
 				   data.get(i).setId(tempID);
 				   data.get(i).setDetails(tempDetails);
 			   }
 		   }
 	   }
    }
}

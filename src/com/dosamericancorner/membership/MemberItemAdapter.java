package com.dosamericancorner.membership;

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

public class MemberItemAdapter extends ArrayAdapter<MemberLoanItem> {
	Context context;
	int layoutResourceId;
	LinearLayout linearMain;
	ArrayList<MemberLoanItem> data = new ArrayList<MemberLoanItem>();
	
	public MemberItemAdapter(Context context, int layoutResourceId,
	ArrayList<MemberLoanItem> data) {
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
		} else {
			row = convertView;
		}
		
		MemberLoanItem myItem = data.get(position);
		TextView titleLabel = (TextView) row.findViewById(R.id.titleText);
		titleLabel.setText(myItem.details[0]);
		TextView ISBNLabel = (TextView) row.findViewById(R.id.ISBNText);
		ISBNLabel.setText(myItem.details[1]);
		TextView checkoutLabel = (TextView) row.findViewById(R.id.checkoutText);
		checkoutLabel.setText(myItem.details[2]);
		TextView dueDateLabel = (TextView) row.findViewById(R.id.dueDateText);
		dueDateLabel.setText(myItem.details[3]);
		
		return row;
	}
	
	public void filter(int type) {
		// 0 = title
 	    // 1 = ISBN
 	    // 2 = checkout date
 	    // 3 = due date
		
		for(int i=0; i<data.size(); i++) {
 		   data.get(i).setType(type);
 		   for(int j=i+1; j<data.size(); j++) {
 			   if (data.get(i).compareTo(data.get(j))>0) {
 				   String tempISBNNumber = data.get(j).getisbn();
 				   String[] tempDetails = data.get(j).getDetails();
 				   data.get(j).setisbn(data.get(i).getisbn());
 				   data.get(j).setDetails(data.get(i).getDetails());
 				   data.get(i).setisbn(tempISBNNumber);
 				   data.get(i).setDetails(tempDetails);
 			   }
 		   }
 	   }
	}
}

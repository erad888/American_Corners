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

public class MemberTableAdapter extends ArrayAdapter<MemberItem> {
	Context context;
	int layoutResourceId;
	LinearLayout linearMain;
	ArrayList<MemberItem> data = new ArrayList<MemberItem>();
	
	public MemberTableAdapter(Context context, int layoutResourceId,
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
		} else {
			row = convertView;
		}
		
		MemberItem myItem = data.get(position);
		TextView firstNameLabel = (TextView) row.findViewById(R.id.firstNameText);
		firstNameLabel.setText(myItem.details[0]);
		TextView lastNameLabel = (TextView) row.findViewById(R.id.lastNameText);
		lastNameLabel.setText(myItem.details[1]);
		TextView birthdayLabel = (TextView) row.findViewById(R.id.birthdayText);
		birthdayLabel.setText(myItem.details[2]);
		TextView memberIdLabel = (TextView) row.findViewById(R.id.memberIdText);
		memberIdLabel.setText(myItem.details[3]);
		TextView emailLabel = (TextView) row.findViewById(R.id.emailText);
		emailLabel.setText(myItem.details[4]);
		TextView checkoutCountLabel = (TextView) row.findViewById(R.id.checkoutCountText);
		checkoutCountLabel.setText(myItem.details[5]);
		TextView karmaPtsLabel = (TextView) row.findViewById(R.id.karmaPtsText);
		karmaPtsLabel.setText(myItem.details[6]);
		TextView notesLabel = (TextView) row.findViewById(R.id.notesText);
		notesLabel.setText(myItem.details[7]);
		
		return row;
	}
	
	public void filter(int type) {
		// 0 = first name
 	   // 1 = last name
 	   // 2 = birthday
 	   // 3 = member Id
 	   // 4 = email
		// 5 = checkout count
		// 6 = karma pts
		// 7 = notes
		for(int i=0; i<data.size(); i++) {
 		   data.get(i).setType(type);
 		   for(int j=i+1; j<data.size(); j++) {
 			   if (data.get(i).compareTo(data.get(j))>0) {
 				   String tempMemberNumber = data.get(j).getMemberNumber();
 				   String[] tempDetails = data.get(j).getDetails();
 				   data.get(j).setMemberNumber(data.get(i).getMemberNumber());
 				   data.get(j).setDetails(data.get(i).getDetails());
 				   data.get(i).setMemberNumber(tempMemberNumber);
 				   data.get(i).setDetails(tempDetails);
 			   }
 		   }
 	   }
	}
}

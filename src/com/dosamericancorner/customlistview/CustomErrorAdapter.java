package com.dosamericancorner.customlistview;

import com.dosamericancorner.login.R;

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


public class CustomErrorAdapter extends ArrayAdapter<ErrorItem> {
	Context context;
    int layoutResourceId;
    LinearLayout linearMain;
    ArrayList<ErrorItem> data = new ArrayList<ErrorItem>();

    public CustomErrorAdapter(Context context, int layoutResourceId,
                  ArrayList<ErrorItem> data) {
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
       
       ErrorItem myItem = data.get(position);
       TextView titleLabel = (TextView) row.findViewById(R.id.titleText);
       titleLabel.setText(myItem.details[0]);
       TextView authorLabel = (TextView) row.findViewById(R.id.authorText);
       authorLabel.setText(myItem.details[1]);
       TextView dateLabel = (TextView) row.findViewById(R.id.dateText);
       dateLabel.setText(myItem.details[2]);
       TextView isbnLabel = (TextView) row.findViewById(R.id.isbnText);
       isbnLabel.setText(myItem.details[3]);
       TextView copiesLabel = (TextView) row.findViewById(R.id.copiesText);
       copiesLabel.setText(myItem.details[4]);
       TextView tagLabel = (TextView) row.findViewById(R.id.tagText);
       tagLabel.setText(myItem.details[5]);
       
       titleLabel.setTextColor(Color.parseColor("#000000"));
       authorLabel.setTextColor(Color.parseColor("#000000"));
       dateLabel.setTextColor(Color.parseColor("#000000"));
       isbnLabel.setTextColor(Color.parseColor("#000000"));
       copiesLabel.setTextColor(Color.parseColor("#000000"));
       tagLabel.setTextColor(Color.parseColor("#000000"));
       
       if(myItem.details[5].equals("*ERROR*"))
    	   tagLabel.setTextColor(Color.parseColor("#ff2626"));
       if(myItem.details[2].equals("-9999"))
       {
    	   dateLabel.setText("*ERROR*");
    	   dateLabel.setTextColor(Color.parseColor("#ff2626"));
       }
       if(myItem.details[4].equals("-9999"))
       {
    	   copiesLabel.setText("*ERROR*");
    	   copiesLabel.setTextColor(Color.parseColor("#ff2626"));
       }
       if(myItem.details[0].equals("*ERROR*"))
    	   titleLabel.setTextColor(Color.parseColor("#ff2626"));
       if(myItem.details[1].equals("*ERROR*"))
    	   authorLabel.setTextColor(Color.parseColor("#ff2626"));
       if(myItem.details[3].equals("*ERROR*"))
    	   isbnLabel.setTextColor(Color.parseColor("#ff2626"));

       return row;
    }
}

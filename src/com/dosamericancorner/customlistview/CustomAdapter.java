package com.dosamericancorner.customlistview;

import com.dosamericancorner.inventory.*;
import com.dosamericancorner.login.R;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CustomAdapter extends ArrayAdapter<InventoryItem> {
       Context context;
       int layoutResourceId;
       LinearLayout linearMain;
       ArrayList<InventoryItem> data = new ArrayList<InventoryItem>();

       public CustomAdapter(Context context, int layoutResourceId,
                     ArrayList<InventoryItem> data) {
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

          InventoryItem myItem = data.get(position);
          TextView label = (TextView) row.findViewById(R.id.tv);
          label.setText(myItem.details);

          return row;
       }
}
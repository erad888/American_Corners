package com.dosamericancorner.customlistview;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.dosamericancorner.home.HomeScreen;
import com.dosamericancorner.home.LoginDataBaseAdapter;
import com.dosamericancorner.login.R;
import com.dosamericancorner.options.SettingScreen;


public class CustomUserAdapter extends ArrayAdapter<String> {

	LoginDataBaseAdapter loginDataBaseAdapter;
       Context context;
       int layoutResourceId;
       LinearLayout linearMain;
       ArrayList<String> data = new ArrayList<String>();

       public CustomUserAdapter(Context context, int layoutResourceId,
                     ArrayList<String> data) {
              super(context, layoutResourceId, data);
              this.layoutResourceId = layoutResourceId;
              this.context = context;
              this.data = data;

  		    loginDataBaseAdapter=new LoginDataBaseAdapter(context);
  		    loginDataBaseAdapter=loginDataBaseAdapter.open();
       }

       @Override
       public View getView(final int position, View convertView, ViewGroup parent) {

    	   
           View row = null;
           
          if (convertView == null) {
                 LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                 row = inflater.inflate(layoutResourceId, parent, false);
          } else {
                 row = convertView;
          }


          TextView titleLabel = (TextView) row.findViewById(R.id.usernameText);
          titleLabel.setText(data.get(position));
          Button deleteBtn = (Button) row.findViewById(R.id.removeUserButton);
          deleteBtn.setOnClickListener(new View.OnClickListener() {
	    	  public void onClick(View arg0) {
	    		  loginDataBaseAdapter.deleteEntry(data.get(position));
	    	  }
	      });

          return row;
       }
}
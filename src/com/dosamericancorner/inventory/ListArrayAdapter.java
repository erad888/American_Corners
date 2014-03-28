package com.dosamericancorner.inventory;

import com.dosamericancorner.login.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
 
public class ListArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
 
	public ListArrayAdapter(Context context, String[] values) {
		super(context, R.layout.list_entries, values);
		this.context = context;
		this.values = values;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.list_entries, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.textTitle);
		textView.setText(values[position]);
 
		return rowView;
	}
}
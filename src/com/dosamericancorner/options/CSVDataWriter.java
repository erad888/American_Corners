package com.dosamericancorner.options;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.dosamericancorner.customlistview.CheckoutItem;
import com.dosamericancorner.customlistview.ReportsItem;

public class CSVDataWriter {
	private ArrayList<CheckoutItem> checkoutArray;
	private ArrayList<ReportsItem> reportsArray; 
	
	public CSVDataWriter() {}
	
	public void setCheckoutArray(ArrayList<CheckoutItem> checkoutArray) {
		this.checkoutArray = checkoutArray;
	}
	
	public void setReportsArray(ArrayList<ReportsItem> reportsArray) {
		this.reportsArray = reportsArray;
	}
	
	public void generateCheckout(String fileName) {
		try {
			FileWriter writer = new FileWriter(fileName);
			
		       writer.append("Title,Author,Checkout Individual,Member ID,Checkout Date,Due Date\n");
		        for(int i=0; i<checkoutArray.size(); i++) {
		        	writer.append(checkoutArray.get(i).getDetails()[0]+","+
		        			checkoutArray.get(i).getDetails()[1]+","+
		        			checkoutArray.get(i).getDetails()[2]+","+
		        			checkoutArray.get(i).getDetails()[3]+","+
		        			checkoutArray.get(i).getDetails()[4]+","+
		        			checkoutArray.get(i).getDetails()[5]+"\n");
		        }
		        writer.flush();
		        writer.close();

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void generateReport(String fileName) {
		try {
			FileWriter writer = new FileWriter(fileName);
			
		       writer.append("Title,Author,Call Number,Publish Year,Checkout Count\n");
		        for(int i=0; i<reportsArray.size(); i++) {
		        	writer.append(reportsArray.get(i).getDetails()[0]+","+
		        			reportsArray.get(i).getDetails()[1]+","+
		        			reportsArray.get(i).getDetails()[2]+","+
		        			reportsArray.get(i).getDetails()[3]+","+
		        			reportsArray.get(i).getDetails()[4]+"\n");
		        }
		        writer.flush();
		        writer.close();

		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

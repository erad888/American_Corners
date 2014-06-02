package com.dosamericancorner.inventory;

import com.googlecode.jcsv.writer.CSVEntryConverter;

public class InventoryEntryConverter implements CSVEntryConverter<String[]> {
	  @Override
	  public String[] convertEntry(String[] item) {

	    return item;
	  }
	}
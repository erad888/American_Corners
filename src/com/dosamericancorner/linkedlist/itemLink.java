package com.dosamericancorner.linkedlist;

import java.io.Serializable;

public class itemLink implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String title;
    public String author;
    public int date;
    public String isbn;
    public int quantity;
    public String tags;
    public itemLink nextLink;

    //Link constructor
    public itemLink(String title, String author, int date, String isbn, int quantity, String tags) {
	    this.title = title;
	    this.author = author;
	    this.date = date;
	    this.isbn = isbn;
	    this.quantity = quantity;
	    this.tags = tags;
    }
}

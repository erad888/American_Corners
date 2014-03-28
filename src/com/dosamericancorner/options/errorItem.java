package com.dosamericancorner.options;

import java.io.Serializable;

public class errorItem implements Serializable{
	
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
	
	public errorItem(String title, String author, int date, String isbn, int quantity, String tags) {
	    this.title = title;
	    this.author = author;
	    this.date = date;
	    this.isbn = isbn;
	    this.quantity = quantity;
	    this.tags = tags;
    }
	
	public String getTitle(){
		return title;
	}
	
	public String getAuthor(){
		return author;
	}
	
	public int getDate(){
		return date;
	}
	
	public String getISBN(){
		return isbn;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public String getTags(){
		return tags;
	}
	
}

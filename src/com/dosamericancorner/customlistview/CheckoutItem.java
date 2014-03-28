package com.dosamericancorner.customlistview;

// ADAM MADE CHECKOUTITEM IMPLMEENTE COMPARABLE
public class CheckoutItem implements Comparable<CheckoutItem>{

    public String isbn;
    public String[] details;
    
    // ADAM ADDED SORTTYPE
    public int sortType;
   
    public CheckoutItem(String isbn, String[] details) {
           super();
           this.isbn = isbn;
           this.details = details;
    }
    public String getisbn() {
           return isbn;
    }
    public void setisbn(String isbn) {
           this.isbn = isbn;
    }
    public String[] getDetails() {
           return details;
    }
    public void setDetails(String[] details) {
           this.details = details;
    }
    
    // ADAM CREATED TYPE ATTRIBUTES AND METHODS
    public int getType() {
    	return sortType;
    }
    public void setType(int sortType) {
    	this.sortType = sortType;
    }
  
    // ADAM ADDED COMPARETO METHOD
	@Override
	public int compareTo(CheckoutItem c) {
		// 0 = title
 	    // 1 = author
 	    // 2 = checkout individual
 	    // 3 = member Id
 	    // 4 = checkout date
		// 5 = due date
    	int lastCmp;
    	switch(sortType) {
		case 0:
			lastCmp = details[0].compareTo(c.details[0]);
			return(lastCmp);
		case 1:
			lastCmp = details[1].compareTo(c.details[1]);
			return(lastCmp);
  	   	case 2:
  	   		lastCmp = details[2].compareTo(c.details[2]);
  	   		return(lastCmp);
  	   	case 3:
  	   		lastCmp = details[3].compareTo(c.details[3]);
  	   		return(lastCmp);
  	  	case 4:
  	  		lastCmp = details[4].compareTo(c.details[4]);
  	  		return(lastCmp);
  	  	case 5:
  	  		lastCmp = details[5].compareTo(c.details[5]);
  	  		return(lastCmp);
		}
		return 0;
	}

}
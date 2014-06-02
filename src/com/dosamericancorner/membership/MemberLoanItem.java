package com.dosamericancorner.membership;

public class MemberLoanItem implements Comparable<MemberLoanItem>{

    public String isbn;
    public String[] details;
    
    public int sortType;
   
    public MemberLoanItem(String isbn, String[] details) {
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
    
    public int getType() {
    	return sortType;
    }
    public void setType(int sortType) {
    	this.sortType = sortType;
    }
  
	@Override
	public int compareTo(MemberLoanItem c) {
		// 0 = title
 	    // 1 = ISBN
 	    // 2 = checkout date
		// 3 = due date
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
		}
		return 0;
	}

}
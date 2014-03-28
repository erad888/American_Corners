package com.dosamericancorner.customlistview;

// ADAM MADE REPORTSITEM IMPLEMENT COMPARABLE
public class ReportsItem implements Comparable<ReportsItem>{

       public String isbn;
       public String[] details;
       
       // ADAM ADDED SORTTYPE ATTRIBUTE
       public int sortType;
      
       public ReportsItem(String isbn, String[] details) {
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
       
       // ADAM ADDED TYPE ATTRIBUTE FOR REPORTS ITEM
       public int getType() {
    	   	  return sortType;
       }
       
       public void setType(int sortType) {
    	      this.sortType = sortType;
       }
      
       
       // ADDED COMPARETO METHOD THAT TAKES ITEM TO BE COMPARED AND SORTTYPE
       // THAT DETERMINES WHICH DETAIL ELEMENT TO BE COMPARED
       // ADAM ADDED
       @Override
       public int compareTo(ReportsItem r) {
    	// 0 = title
    	    // 1 = author
    	    // 2 = checkout individual
    	    // 3 = publish year
    	    // 4 = check out count
       	int lastCmp;
       	switch(sortType) {
   		case 0:
   			lastCmp = details[0].compareTo(r.details[0]);
   			return(lastCmp);
   		case 1:
   			lastCmp = details[1].compareTo(r.details[1]);
   			return(lastCmp);
     	   	case 2:
     	   		lastCmp = details[2].compareTo(r.details[2]);
     	   		return(lastCmp);
     	   	case 3:
     	   		lastCmp = details[3].compareTo(r.details[3]);
     	   		return(lastCmp);
     	  	case 4:
     	  		lastCmp = details[4].compareTo(r.details[4]);
     	  		return(lastCmp);
   		}
   		return 0;
       }
}

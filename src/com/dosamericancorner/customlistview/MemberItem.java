package com.dosamericancorner.customlistview;

public class MemberItem implements Comparable<MemberItem>{

 public String memberId;
 public String[] details;
 
 public int sortType;

 public MemberItem(String memberId, String[] details) {
        super();
        this.memberId = memberId;
        this.details = details;
 }
 public String getId() {
        return memberId;
 }
 public void setId(String memberId) {
        this.memberId = memberId;
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

	@Override
	public int compareTo(MemberItem c) {
		// 0 = first name
	    // 1 = last name
	    // 2 = birthday
	    // 3 = member id
	    // 4 = email
		// 5 = checkout count
		// 6 = karma points
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
	  	case 6:
	  		lastCmp = details[6].compareTo(c.details[6]);
	  		return(lastCmp);
		}
		return 0;
	}

}
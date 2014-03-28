package com.dosamericancorner.membership;


public class MemberItem implements Comparable<MemberItem>{

	public String memberNumber;
	public String details[];
	
	 // ADAM ADDED SORTTYPE ATTRIBUTE
    public int sortType;
   
    public MemberItem(String memberNumber, String[] details) {
           super();
           this.memberNumber = memberNumber;
           this.details = details;
    }
    public String getMemberNumber() {
           return memberNumber;
    }
    public void setMemberNumber(String memberNumber) {
           this.memberNumber = memberNumber;
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
    public int compareTo(MemberItem m) {
 	// 0 = first name
 	    // 1 = last name
 	    // 2 = birthday
 	    // 3 = member Id
 	    // 4 = email
    	// 5 = checkout count
    	// 6 = karma pts
    	// 7 = notes
    	int lastCmp;
    	switch(sortType) {
		case 0:
			lastCmp = details[0].compareTo(m.details[0]);
			return(lastCmp);
		case 1:
			lastCmp = details[1].compareTo(m.details[1]);
			return(lastCmp);
  	   	case 2:
  	   		lastCmp = details[2].compareTo(m.details[2]);
  	   		return(lastCmp);
  	   	case 3:
  	   		lastCmp = details[3].compareTo(m.details[3]);
  	   		return(lastCmp);
  	  	case 4:
  	  		lastCmp = details[4].compareTo(m.details[4]);
  	  		return(lastCmp);
  	  case 5:
	  		lastCmp = details[5].compareTo(m.details[5]);
	  		return(lastCmp);
  	case 6:
	  		lastCmp = details[6].compareTo(m.details[6]);
	  		return(lastCmp);
  	case 7:
	  		lastCmp = details[7].compareTo(m.details[7]);
	  		return(lastCmp);
		}
		return 0;
    }

}


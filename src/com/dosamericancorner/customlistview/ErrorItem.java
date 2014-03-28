package com.dosamericancorner.customlistview;

public class ErrorItem{

	   public int num;
       public String[] details;
      
       public ErrorItem(int num, String[] details) {
              super();
              this.num = num;
              this.details = details;
       }
       public int getNum() {
    	   return num;
       }
       public void setNum(int num) {
    	   this.num = num;
       }
       public String[] getDetails() {
              return details;
       }
       public void setDetails(String[] details) {
              this.details = details;
       }

}

package com.dosamericancorner.customlistview;

public class InventoryItem {

       public String query;
       public String details;
      
       public InventoryItem(String query, String details) {
              super();
              this.query = query;
              this.details = details;
       }
       public String getQuery() {
              return query;
       }
       public void setQuery(String query) {
              this.query = query;
       }
       public String getDetails() {
              return details;
       }
       public void setDetails(String details) {
              this.details = details;
       }

}

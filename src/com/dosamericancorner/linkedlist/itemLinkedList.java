package com.dosamericancorner.linkedlist;

public class itemLinkedList {
	    private itemLink first;

	    //LinkList constructor
	    public itemLinkedList() {
		    setFirst(null);
	    }

	    //Returns true if list is empty
	    public boolean isEmpty() {
		    return getFirst() == null;
	    }

	    //Inserts a new Link at the first of the list
	    public void insert(String title, String author, int date, String isbn, int quantity, String tags) {
		    itemLink link = new itemLink(title, author, date, isbn, quantity, tags);
		    link.nextLink = getFirst();
		    setFirst(link);
	    }

	    //Deletes the link at the first of the list
	    public itemLink delete() {
		    itemLink temp = getFirst();
		    setFirst(getFirst().nextLink);
		    return temp;
	    }

		public itemLink getFirst() {
			return first;
		}

		public void setFirst(itemLink first) {
			this.first = first;
		}

	}  
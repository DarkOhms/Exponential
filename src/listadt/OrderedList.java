package listadt;
public class OrderedList<T extends Comparable<T>> extends List<T> {

	public void insert(T newEntry){

	        ListItem<T> nextEntry = new ListItem<T>(newEntry);
	        if(head == null) {
	            head = nextEntry;
	            tail = head;
	        }else
	        if(nextEntry.data.compareTo(head.data)<0){
	            nextEntry.next = head;
	            head.prev = nextEntry;
	            head = nextEntry;
	        }else
	        if(nextEntry.data.compareTo(tail.data)>0){
	        	nextEntry.prev = tail; 
	        	tail.next = nextEntry;
	            tail = nextEntry;
	        }else{
	        	ListItem<T> temp = head;
	        	 while (temp.next.data.compareTo(nextEntry.data) < 0)
	               temp = temp.next;
	        	 
	        	 
	        	 nextEntry.next = temp.next;
	        	 temp.next = nextEntry;
	             nextEntry.prev = temp.prev;
	        }
	        	count++;
	    }
}

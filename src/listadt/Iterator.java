package listadt;


public class Iterator<T> {
  
  ListItem<T> currentItem = new ListItem<T>();
  
  Iterator(ListItem<T> head){
	  currentItem = head;
  }
  
  public T nextData(){  
	  
	    ListItem<T> temp = new ListItem<T>();
	    temp = currentItem;
	    currentItem = temp.next;
	    return temp.data;  
  }
  
  public boolean hasNext(){
	 
	  if(currentItem != null)
		  return true;
	  else
		  return false;
  }
}

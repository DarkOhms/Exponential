/**
 * Luke Martin
 * CISP 430
 *
 *  2/27/2016 Start
 *  
 *  3/2/2016
 *  List<T> ----------- 1:m contains --- ListItem<T>
 *  
 */
package listadt;
public class ListItem <T> {
    public T data;


    public ListItem<T> next = null;
    public ListItem<T> prev = null;

    ListItem(){};
    
    ListItem(T newData){
        data = newData;
    }
    ListItem(ListItem<T> newItem){
        data = newItem.data;
        next = newItem.next;
        prev = newItem.prev;
    }
}

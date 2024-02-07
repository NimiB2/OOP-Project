package nimrodBar;

import java.util.Iterator;
import java.util.List;

public class MyGenneric {

	public MyGenneric() {
	
	}
	
	public static <T> boolean add(T item, List<T> itemList) {
        if (item == null) {
            return false;
        }

        if (itemList.contains(item)) {
            return false;
        }

        itemList.add(item);
        return true;
    }
    
    public static <T> boolean delete(int index, List<T> itemList) {
        if (index < 0 || index >= itemList.size()) {
            return false;
        }
        itemList.remove(index);
        return true;
    }	


}

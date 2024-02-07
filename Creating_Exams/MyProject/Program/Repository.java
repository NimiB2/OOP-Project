package nimrodBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Repository {
	private List<DataBase> dbArr = new ArrayList<>();

	public Repository() {
	}

	public boolean addDatabase(DataBase db) {
		return this.dbArr.add(db);
	}

	public int getNumOfSubjects() {
		return this.dbArr.size();
	}

	public List<DataBase> getAllDb() {
		return this.dbArr;
	}

	public DataBase getDbByIndex(int index) {
		return this.dbArr.get(index);
	}

	@SuppressWarnings("unlikely-arg-type")
	public boolean isSubjectExists(String Subject) {
		return this.dbArr.contains(Subject);
	}

	public boolean add(DataBase db) {
		return MyGenneric.add(db, dbArr);        
	}

	public String toString() {
		StringBuffer str = new StringBuffer("All existing databases subject(" + this.dbArr.size() + ") :\n");
		Iterator<DataBase> iterator = this.dbArr.iterator();
		int index=1;
		while(iterator.hasNext()) {
		str.append("Subject #" + (index++) + ": ");
		str.append(iterator.next().getSubject() + "\n");
		}
		return str.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Repository)) {
			return false;
		}
		Repository temp = (Repository) obj;
		return this.dbArr.equals(temp.dbArr);
	}

}

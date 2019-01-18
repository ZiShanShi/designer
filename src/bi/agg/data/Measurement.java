package bi.agg.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Measurement implements Iterable<String> {

	private List<String> fieldList;
	
	public Measurement() {
		fieldList = new ArrayList<String>();
	}
	
	public void add(String field) {
		fieldList.add(field);
	}

	@Override
	public Iterator<String> iterator() {
		return fieldList.iterator();
	}
}

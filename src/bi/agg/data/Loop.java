package bi.agg.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Loop<T> implements Iterable<T> {

	protected List<T> items;
	protected boolean bof;
	protected int pos;
	
	public Loop() {
		items = new ArrayList<T>();
	}
	
	public void first() {
		pos = 0;
		bof = true;
	}
	
	public void open() {
		pos = 0;
		bof = false;
	}
	
	public boolean next() {
		if (items.isEmpty()) {
			return false;
		}
		
		if (bof) {
			bof = false;
			return true;
		}
		
		if (pos < items.size() - 1) {
			pos++;
			return true;
		}
		else {
			pos = 0;
			return false;
		}
	}
	
	public int size() {
		return items.size();
	}
	
	public T getCurrent() {
		if (items.isEmpty()) {
			return null;
		}
		
		return items.get(pos);
	}
	
	public void add(T item) {
		items.add(item);
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}

	public List<T> getItems() {
		return items;
	}
	
	public void addAll(List<T> items) {
		this.items.addAll(items);
	}

	@Override
	public Iterator<T> iterator() {
		return items.iterator();
	}

}

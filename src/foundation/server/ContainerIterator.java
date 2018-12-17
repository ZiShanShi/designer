package foundation.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class ContainerIterator<T> implements Iterator<T> {

	private List<String> names;
	private Container<T> parent;
	private int pos;

	public ContainerIterator(Container<T> parent) {
		this.parent = parent;
		pos = 0;
		
		Set<String> keys = parent.items.keySet();
		names = new ArrayList<String>(keys.size());
		for (String name: keys) {
			names.add(name);
		}
	}
	
	public boolean hasNext() {
		return pos < names.size();
	}

	public T next() {
		if (pos < names.size()) {
			return parent.items.get(names.get(pos++));
		}
		else return null;
	}

	public void remove() {
	}

}

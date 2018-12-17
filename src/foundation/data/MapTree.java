package foundation.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import foundation.server.Container;
import foundation.util.ContentBuilder;
import foundation.util.Util;

public class MapTree<T> implements Iterable<T> {

	protected static Logger logger;
	protected TreeSet<T> itemList;
	protected Map<String, T> itemMap;
	
	static {
		logger = Logger.getLogger(MapTree.class);
	}
	
	public MapTree() {
		itemList = new TreeSet<T>();
		itemMap = new HashMap<String, T>();
	}
	
	public void add(String key, T item) {
		if (key == null) {
			key = "empty";
		}
		key = key.toLowerCase();
		
		if (itemMap.containsKey(key)) {
			logger.error("duplicate " + this.getClass().getName() +  " key: " + key);
			return;
		}
		
		itemMap.put(key, item);
		itemList.add(item);
	}
	
	public T get(String key) {
		if (key == null) {
			key = "empty";
		}
		key = key.toLowerCase();
		
		return itemMap.get(key);
	}
	
	public T remove(String key) {
		if (key == null) {
			return null;
		}
		
		T obj = itemMap.get(key.toLowerCase());
		
		if (obj != null) {
			itemMap.remove(key);
			itemList.remove(obj);
		}
		
		return obj;
	}
	
	public TreeSet<T> getItemList() {
		return itemList;
	}

	public boolean isEmpty() {
		return itemList.isEmpty();
	}
	
	public int size() {
		return itemList.size();
	}
	
	public boolean containsKey(String key) {
		if (key == null) {
			return false;
		}
		
		key = key.toLowerCase();
		return itemMap.containsKey(key);
	}

	@Override
	public Iterator<T> iterator() {
		return itemList.iterator();
	}

	public void clear() {
		itemList.clear();
		itemMap.clear();
	}
	
	public String toString(String separator) {
		ContentBuilder builder = new ContentBuilder(separator);
		
		for (String key: itemMap.keySet()) {
			builder.append(key);
		}
		
		return builder.toString();
	}
	
	public void loadFromString(String content, Container<T> container, String separator) {
		if (Util.isEmptyStr(content)) {
			return;
		}
		
		String[] keyArray = content.trim().replace(";", separator).split(separator);
		
		for (String key: keyArray) {
			T obj = container.get(key);
			
			if (obj == null) {
				continue;
			}
			
			add(key, obj);
		}
	}
}

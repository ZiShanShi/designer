package foundation.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.log4j.Logger;

import foundation.util.Util;


public class Container<T> implements Iterable<T> {

	protected static Logger logger;
	protected String name;
	private List<String> dependentContainer;
	protected Map<String, T> items; 
	protected Map<String, Class<T>> classes;
	protected boolean dirty;
	
	
	static {
		logger = Logger.getLogger("Collector");
	}

	public Container(){
		name = this.getClass().getSimpleName();
		
		dependentContainer = new ArrayList<String>();
		items = new HashMap<String, T>();
		classes = new HashMap<String, Class<T>>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void add(String name, T item) {
		if (name != null) {
			items.put(name.toLowerCase(), item);
		}
	}

	public T get(String name) {
		if (name != null) {
			return items.get(name.toLowerCase());
		}
		else {
			return null;
		}
	}

	public List<T> getItemList(){
		List<T> Result = new LinkedList<T>();
		
		Set<String> keys = items.keySet();
		for (String key : keys){
			Result.add(items.get(key));
		}
		
		return Result;
	}

	public void addItemClass (String name, Class<T> clazz) {
		classes.put(name, clazz);
	}

	public Iterator<T> iterator() {
		return new ContainerIterator<T>(this);
	}

	public void afterLoad() {
		
	}

	public boolean contains(String name) {
		if (name != null) {
			return items.containsKey(name.toLowerCase());
		}
		else {
			return false;
		}
	}
	
	public List<String> getDependentContainer() {
		return dependentContainer;
	}

	public Map<String, T> getItemMap() {
		return items;
	}
	
	public void addDependentContainer(String value) {
		if (!Util.isEmptyStr(value)) {
			dependentContainer.add(value);			
		}
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	public void clear() {
		items.clear();
	}
	
}

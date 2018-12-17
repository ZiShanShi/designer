package foundation.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfigLoaderContainer implements Iterable<IPreloader>{

	private static ConfigLoaderContainer instance;
	private List<IPreloader> itemList;
	
	public synchronized static ConfigLoaderContainer getInstance() {
		if (instance == null) {
			instance = new ConfigLoaderContainer();
		}
		
		return instance;
	}
	
	public ConfigLoaderContainer() {
		itemList = new ArrayList<IPreloader>();
	}

	@Override
	public Iterator<IPreloader> iterator() {
		return itemList.iterator();
	}

	public void add(IPreloader configLoader) {
		itemList.add(configLoader);
	}

}

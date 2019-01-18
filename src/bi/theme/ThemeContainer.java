package bi.theme;

import java.util.HashMap;
import java.util.Map;


public class ThemeContainer {

	private static ThemeContainer instance;
	private Map<String, Theme> namedSQLMap;
	
	private ThemeContainer() {
		namedSQLMap = new HashMap<String, Theme>();
	}
	
	public synchronized static ThemeContainer getInstance() {
		if (instance == null) {
			instance = new ThemeContainer();
		}
		
		return instance;
	}
	
	public Theme get(String name, boolean refresh) throws Exception {
		if (name == null) {
			return null;
		}
		
		if(refresh){
			ThemeLoader loader = new ThemeLoader();
			loader.refresh();
		}
		
		name = name.toLowerCase();
		Theme template = namedSQLMap.get(name);
		return template.newInstance();
	}

	public void append(Theme theme) {
		if (theme == null) {
			return;
		}
		
		String name = theme.getName();
		
		if (name == null) {
			return;
		}
		
		String lower = name.toLowerCase();
		namedSQLMap.put(lower, theme);
	}
	
	public void clear(){
		namedSQLMap.clear();
	}
	
}

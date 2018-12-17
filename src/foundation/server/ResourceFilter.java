package foundation.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResourceFilter {

	private Set<String> resourceSet;
	private List<String> endsWidthList;
	private Set<String> containsSet;
	
	
	public ResourceFilter() {
		resourceSet = new HashSet<String>();
		endsWidthList = new ArrayList<String>();
		containsSet = new HashSet<String>();
	}
	
	public void clear() {
		resourceSet.clear();
		endsWidthList.clear();
		containsSet.clear();
	}
	
	public boolean contains(String path) {
		if (path == null) {
			return false;
		}
		
		//1. check contains
		if (containsSet.contains(path)) {
			return true;
		}
		
		if (!path.startsWith("/")) {
			if (containsSet.contains("/" + path)) {
				return true;
			}
		}
		
		//2. check ends width
		for (String segment: endsWidthList) {
			if (path.endsWith(segment)) {
				return true;
			}
		}
		
		return false;
	}

	public void add(String resource) {
		if (resource == null) {
			return;
		}
		
		if (resourceSet.contains(resource)) {
			return;
		}
		
		resourceSet.add(resource);
		
		if (resource.startsWith("*")) {
			String segment = resource.substring(1);
			endsWidthList.add(segment);
		}
		else {
			containsSet.add(resource);
		}
	}


}

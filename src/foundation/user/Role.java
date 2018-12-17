package foundation.user;

import java.util.HashSet;
import java.util.Set;

import foundation.server.RequestPath;
import foundation.util.Util;


public class Role {

	private String name;
	private Set<String> dataSet;
	private Set<String> includeResources;
	private Set<String> endsWithResource;
	private Set<String> cachedAuthorization;
	
	public Role(String name) {
		this.name = name;
		dataSet = new HashSet<String>();
		includeResources = new HashSet<String>();
		endsWithResource = new HashSet<String>();
		cachedAuthorization = new HashSet<String>();
	}
	
	public boolean containsData(String data) {
		return dataSet.contains(data);
	}

	public boolean containsPath(RequestPath path) {
		String uri = path.getURI();
		
		if (cachedAuthorization.contains(uri)) {
			return true;
		}
		
		for (String include: includeResources) {
			if (uri.contains(include)) {
				return true;
			}
		}
		
		for (String endswidth: cachedAuthorization) {
			if (uri.endsWith(endswidth)) {
				return true;
			}
		}
		
		return false;
	}

	public void addResource(String resource) {
		if (Util.isEmptyStr(resource)) {
			return;
		}
		
		resource = resource.trim().toLowerCase();
		
		if (resource.startsWith("/")) {
			
		}
		
		if (resource.endsWith("/*")) {
			resource = resource.substring(0, resource.length() - "/*".length());
			includeResources.add(resource);
			return;
		}
		
		endsWithResource.add(resource);
	}

	@Override
	public String toString() {
		return "Role [name=" + name + "]";
	}
	
}

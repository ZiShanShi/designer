package foundation.user;

import java.util.HashMap;
import java.util.Map;

import foundation.server.RequestPath;
import foundation.util.Util;

public class UserRightContainer {

	private static UserRightContainer instance;
	private Map<String, Role> roleMap;
	private Map<String, String> dataFilterMap;
	
	public static synchronized UserRightContainer getInstance() {
		if (instance == null) {
			instance = new UserRightContainer();
		}
		
		return instance;
	}
	
	private UserRightContainer() {
		roleMap = new HashMap<String, Role>();
		dataFilterMap = new HashMap<String, String>();
	}
	
	public boolean existsPageRight(String logcode, RequestPath path) {
		Role role = roleMap.get(logcode);
		
		if (role == null) {
			return false;
		}
		
		return role.containsPath(path);
	}
	
	public boolean existsDataRight(String logcode, String data) {
		Role role = roleMap.get(logcode);
		
		if (role == null) {
			return false;
		}
		
		return role.containsData(data);
	}

	public boolean existsCallRight(String logcode, RequestPath path) {
		return true;
	}

	public String getDataFilter(OnlineUser onlineUser, String data) {
		String template = dataFilterMap.get(data);
		
		if (template == null) {
			return null;
		}
		
		return onlineUser.getRowfilter(data, template);
	}
	
	public void addDataRight(String dataName, String where) {
		if (Util.isEmptyStr(dataName) || Util.isEmptyStr(where)) {
			return;
		}
		
		dataName = dataName.toLowerCase();
		
		if (!dataFilterMap.containsKey(dataName)) {
			dataFilterMap.put(dataName, where);
		}
	}

	public void addRoleResource(String roleName, String resource) {
		if (Util.isEmptyStr(roleName)) {
			return;
		}
		
		roleName = roleName.toLowerCase();
		Role role = roleMap.get(roleName);
		
		if (role == null) {
			role = new Role(roleName);
			roleMap.put(roleName, role);
		}
		
		role.addResource(resource);
	}
}

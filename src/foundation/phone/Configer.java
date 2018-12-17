package foundation.phone;

import java.util.HashMap;
import java.util.Map;

public class Configer {
	
	private static String url;
	private static String userName;
	private static String password;
	private static Map<String, Template> templateMap;

	static {
		templateMap = new HashMap<String, Template>();
	}
	
	public static String getURL() {
		return url;
	}

	public static String createContent(String id, String value) {
		if (id == null) {
			return null;
		}
		
		id = id.toLowerCase();
		Template template = templateMap.get(id);
		return template.toContent(value);
	}

	public static String getUserName() {
		return userName;
	}

	public static String getPassword() {
		return password;
	}

	public static void setURL(String value) {
		url = value;
	}

	public static void setUserName(String value) {
		userName = value;
	}

	public static void setPassword(String value) {
		password = value;
	}

	public static void addTemplate(String name, String content) {
		if (name == null) {
			return;
		}
		
		name = name.toLowerCase();
		Template template = new Template(content);
		templateMap.put(name, template);
	}
}

package foundation.config;

import foundation.util.Util;

public class CallableConfig {

	private String path;
	private String className;
	
	public CallableConfig(String path, String className) {
		this.path = path;
		this.className = className;
	}

	public String getPath() {
		return path;
	}

	public String getClassName() {
		return className;
	}

	public boolean invalid() {
		if (Util.isEmptyStr(path)) {
			return true;
		}		
		
		if (Util.isEmptyStr(className)) {
			return true;
		}
		
		return false;
	}
	
}

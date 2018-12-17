package foundation.config;

import foundation.util.Util;

public class VirtualPathConfig {

	private String path;
	private String target;
	private String className;
	
	public VirtualPathConfig(String path, String target, String className) {
		this.path = path;
		this.target = target;
		this.className = className;
	}

	public String getPath() {
		return path;
	}

	public String getTarget() {
		return target;
	}

	public String getClassName() {
		return className;
	}

	public boolean invalid() {
		if (Util.isEmptyStr(path)) {
			return true;
		}

		if (Util.isEmptyStr(target) && Util.isEmptyStr(className)) {
			return true;
		}
		
		return false;
	}
	
}

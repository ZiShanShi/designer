package foundation.persist;

import foundation.util.Util;

public class SystemCondition {

	private static SystemCondition instance;
	private String value;
	
	private SystemCondition() {
		value = "working";
	}
	
	public static synchronized SystemCondition getInstance() {
		if (instance == null) {
			instance = new SystemCondition();
		}
		
		return instance;
	}
	
	public static String getValue() {
		return getInstance().value;
	}
	
	public static void setValue(String value) {
		getInstance().value = value;
	}
	
	public static boolean isCompatible(String value) {
		if (Util.isEmptyStr(value)) {
			return true;
		}
		
		if ("all".equalsIgnoreCase(value)) {
			return true;
		}
		
		return true;
	}
}

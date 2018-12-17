package foundation.server;

public class ServerInfo {

	private static double maxMemmory;
	private static double usedMemory;
	private static String version;
	private static String dataBaseName;
	private static String dataBaseVersion;
	
	
	static {
		version = "3.0.0.1";
		
		Runtime runtime = Runtime.getRuntime();
		
		usedMemory = Math.round((runtime.totalMemory()) / (1024.0 * 1024));
		maxMemmory = Math.round((runtime.maxMemory()) / (1024.0 * 1024));
	}

	public static double getMaxMemmory() {
		return maxMemmory;
	}

	public static double getUsedMemory() {
		return usedMemory;
	}

	public static String getVersion() {
		return version;
	}

	public static String getDataBaseName() {
		return dataBaseName;
	}

	public static String getDataBaseVersion() {
		return dataBaseVersion;
	}

	public static void setDataBaseName(String value) {
		dataBaseName = value;
	}

	public static void setDataBaseVersion(String value) {
		dataBaseVersion = value;
	}
	
}

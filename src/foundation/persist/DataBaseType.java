package foundation.persist;

public enum DataBaseType {

	Oracle, SQLServer, MySQL, Unknown;

	public static DataBaseType valueOfString(String value) {
		if (value == null) {
			return Unknown;
		}
		
		if ("Oracle".equalsIgnoreCase(value)) {
			return Oracle;
		}
		
		if ("SQLServer".equalsIgnoreCase(value)) {
			return SQLServer;
		}
		
		if ("MySQL".equalsIgnoreCase(value)) {
			return MySQL;
		}
		
		return Unknown;
		
	}
	
	
}

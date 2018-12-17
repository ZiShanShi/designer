package foundation.persist.sql;

public enum ReturnType {

	None, EntitySet, Entity, String, Int, BigDecimal, ChangedCount;

	public static ReturnType valueOfString(String value) {
		if (value == null) {
			return None;
		}
		
		value = value.toLowerCase();
		
		if ("entityset".equals(value)) {
			return EntitySet;
		}
		else if ("dataset".equals(value)) {
			return EntitySet;
		}
		else if ("entity".equals(value)) {
			return Entity;
		}
		else if ("string".equals(value)) {
			return String;
		}
		else if ("int".equals(value)) {
			return Int;
		}
		else if ("decimal".equals(value)) {
			return BigDecimal;
		}
		else if ("changecount".equals(value)) {
			return ChangedCount;
		}
		
		return null;
	}
	
}

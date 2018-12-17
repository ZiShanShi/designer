package foundation.persist.sql;

import java.util.HashMap;
import java.util.Map;

import foundation.util.Util;

public class NamedSQLContainer {

	private static NamedSQLContainer instance;
	private Map<String, Map<String, NamedSQL>> contionalSQLMap;
	
	
	private NamedSQLContainer() {
		contionalSQLMap = new HashMap<String, Map<String, NamedSQL>>();
	}
	
	public synchronized static NamedSQLContainer getInstance() {
		if (instance == null) {
			instance = new NamedSQLContainer();
		}
		
		return instance;
	}
	
	public NamedSQL get(String name, String condtion) {
		if (name == null) {
			return null;
		}
		
		if (Util.isEmptyStr(condtion)) {
			condtion = "empty";
		}
		
		Map<String, NamedSQL> sqlmap = contionalSQLMap.get(condtion.toLowerCase());
		if(sqlmap == null){
			return null;
		}
		return sqlmap.get(name.toLowerCase());
	}
	
	public void append(NamedSQL namedSQL, String condition) {
		if (namedSQL == null) {
			return;
		}
		
		//1.
		String name = namedSQL.getName();
		if (name == null) {
			return;
		}
		
		//2.
		if (Util.isEmptyStr(condition)) {
			condition = "empty";
		}
		condition = condition.toLowerCase();
		
		//3.
		Map<String, NamedSQL> sqlmap = contionalSQLMap.get(condition);

		if (sqlmap == null) {
			sqlmap = new HashMap<String, NamedSQL>();
			contionalSQLMap.put(condition, sqlmap);
		}
		
		//4.
		sqlmap.put(name.toLowerCase(), namedSQL);
	}

}

package foundation.persist.loader;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import foundation.config.Configer;
import foundation.persist.sql.ILoadable;


public class PrimaryKeyLoader implements ILoadable {

	private Map<String, String> items;
	private String schema;
	private String keyField;
	private String table;
	
	
	public PrimaryKeyLoader() {
		items = new HashMap<String, String>();
	}
	
	public void load(ResultSet rslt, Object ...arg) throws Exception {
		String table_schem;
		String fieldName;
		
		while (rslt.next()) {
			table_schem = rslt.getString(1);
			fieldName = rslt.getString(3);
			keyField = fieldName;
			
			append(table_schem, fieldName);
		}
	}

	private void append(String tableSchem, String fieldName) {
		items.put(tableSchem.toLowerCase(), fieldName);
	}

	public String getPrimaryKeyField() throws Exception {
		int size = items.size();
		
		if (size == 0) {
			return null;
		}
		
		if (size == 1) {
			return keyField;
		}
		
		Set<String> keys = items.keySet();
		
		if (schema == null) {
			schema = Configer.DataBase_Schema;
		}
		
		for (String key: keys) {
			if (key.equalsIgnoreCase(schema)) {
				keyField = items.get(key);
				return keyField;
			}
		}
		
		throw new Exception("can not get primary key field: more than one schema has the table [" + table + "], you should privade schema");
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

}

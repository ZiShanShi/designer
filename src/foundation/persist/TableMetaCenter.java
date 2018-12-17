package foundation.persist;

import java.util.HashMap;
import java.util.Map;

public class TableMetaCenter {

	private static TableMetaCenter instance;
	private Map<String, TableMeta> metaMap;
	
	
	private TableMetaCenter() {
		metaMap = new HashMap<String, TableMeta>();
	}
	
	public static synchronized TableMetaCenter getInstance() {
		if (instance == null) {
			instance = new TableMetaCenter();
		}
		
		return instance;
	}
	
	public TableMeta get(String tableName) throws Exception {
		return get(tableName, false);
	}

	public TableMeta get(String tableName, boolean refresh) throws Exception {
		if (tableName == null) {
			return null;
		}
		
		tableName = tableName.toLowerCase();
		
		TableMeta meta = metaMap.get(tableName);
		
		if (refresh || meta == null) {
			MetaDataLoader metaDataLoader = new MetaDataLoader(tableName);
			TableMeta newMeta = metaDataLoader.getTableMeta();
			
			newMeta.copyVirtualNamesForm(meta);
			
			metaMap.put(tableName.toLowerCase(), newMeta);
			meta = newMeta;
		}
		
		return meta;
	}

}

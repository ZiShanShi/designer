package foundation.persist;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import foundation.persist.sql.SQLRunner;



public class MetaDataLoader implements IMetaDataLoader {
	
	private String tableName;
	private TableMeta tableMeta;
	private String schema;
	
	
	public MetaDataLoader(String table) {
		tableName = table;
	}

	public String getTableName() {
		if (schema != null) {
			return schema + "." + tableName;
		}
		
		return tableName;
	}

	public void load(ResultSetMetaData metaData) throws Exception {
		ResultMetaFieldReader fieldReader = new ResultMetaFieldReader(metaData);
		tableMeta = new TableMeta(tableName, fieldReader);
	}

	public TableMeta getTableMeta() throws Exception {
		return getCoreTable(null);
	}
	
	public TableMeta getCoreTable(Connection conn) throws Exception {
		if (tableMeta == null) {
			synchronized (this) {
				if (tableMeta == null) {
					SQLRunner.getTableMetaData(conn, this);
				}
			}			
		}

		return tableMeta;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public static int[] getFieldIndex(ResultSetMetaData metaData, String[] fieldnames) throws SQLException {
		ResultMetaFieldReader fieldReader = new ResultMetaFieldReader(metaData);
		TableMeta tableMeta = new TableMeta("anymous", fieldReader);
		
		int[] result = new int[fieldnames.length];
		
		for (int i = 0; i < fieldnames.length; i++) {
			String fieldname = fieldnames[i];
			result[i] = tableMeta.getIndex(fieldname) + 1;
		}
		
		return result;
	}

	public static int getFieldIndex(ResultSetMetaData metaData, String fieldname) throws SQLException {
		ResultMetaFieldReader fieldReader = new ResultMetaFieldReader(metaData);
		TableMeta tableMeta = new TableMeta("anymous", fieldReader);
		
		return tableMeta.getIndex(fieldname) + 1;
	}
	
}

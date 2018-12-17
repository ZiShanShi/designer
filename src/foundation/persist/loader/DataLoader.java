package foundation.persist.loader;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import foundation.persist.ResultMetaFieldReader;
import foundation.persist.TableMeta;
import foundation.persist.sql.ILoadable;


public abstract class DataLoader implements ILoadable {

	protected String tableName;
	protected TableMeta tableMeta;
	
	
	public DataLoader(String tableName) {
		this.tableName = tableName;
	}

	public void load(ResultSet rslt, Object... args) throws Exception {
		ResultSetMetaData metaData = rslt.getMetaData();
		loadTableMeta(metaData);
		
		loadData(rslt);
	}

	private void loadTableMeta(ResultSetMetaData metaData) throws SQLException {
		if (tableMeta == null) {
			ResultMetaFieldReader fieldReader = new ResultMetaFieldReader(metaData);
			tableMeta = new TableMeta(tableName, fieldReader);
		}
	}

	protected abstract void loadData(ResultSet rslt) throws Exception;

	public void setTableMeta(TableMeta tableMeta) {
		this.tableMeta = tableMeta;
	}
}

package foundation.persist;

import java.sql.ResultSetMetaData;

public interface IMetaDataLoader {

	String getTableName();
	
	void load(ResultSetMetaData metaData) throws Exception ;
	
}

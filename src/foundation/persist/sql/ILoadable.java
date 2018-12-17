package foundation.persist.sql;

import java.sql.ResultSet;

public interface ILoadable {

	void load(ResultSet rslt, Object ...args) throws Exception;
	
}

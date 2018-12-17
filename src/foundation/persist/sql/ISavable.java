package foundation.persist.sql;

import java.sql.PreparedStatement;

public interface ISavable {

	void save(PreparedStatement stmt, Object ...agrs) throws Exception;
	
	
}

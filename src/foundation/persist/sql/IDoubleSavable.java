package foundation.persist.sql;

import java.sql.PreparedStatement;


public interface IDoubleSavable {

	void save(PreparedStatement stmt1, PreparedStatement stmt2, Object ...agrs) throws Exception;
	
}

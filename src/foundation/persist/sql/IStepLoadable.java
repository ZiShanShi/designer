package foundation.persist.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IStepLoadable {

	boolean hasNextForLoad();
	
	void load(ResultSet rslt, Object ...args) throws Exception;

	void setLoadParameters(PreparedStatement stmt) throws SQLException;
	
}

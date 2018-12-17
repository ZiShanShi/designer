package foundation.persist.sql;

import java.sql.PreparedStatement;


public interface IStepSavable {

	void save(PreparedStatement stmt, Object ...args) throws Exception;
	
	boolean hasNextForSave();
	
}

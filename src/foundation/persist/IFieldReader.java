package foundation.persist;

import java.sql.SQLException;


public interface IFieldReader {

	boolean next() throws SQLException;

	String getFieldName() throws SQLException;

	int getFieldType() throws SQLException;

	int getFieldLength() throws SQLException;

	int getNullable() throws SQLException;

	void first();

}

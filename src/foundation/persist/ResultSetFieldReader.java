package foundation.persist;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetFieldReader implements IFieldReader {

	private ResultSet resultSet;
	
	
	public ResultSetFieldReader(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public boolean next() throws SQLException {
		return resultSet.next();
	}
	
	public String getFieldName() throws SQLException {
		return resultSet.getString(4);
	}
	
	public int getFieldType() throws SQLException {
		int value = resultSet.getInt(5);
		return value;
	}

	public int getFieldLength() throws SQLException {
		return resultSet.getInt(7);
	}

	public int getNullable() throws SQLException {
		return resultSet.getInt(11);
	}

	@Override
	public void first() {
		try {
			resultSet.first();
		}
		catch (Exception e) {
		}
	}

}

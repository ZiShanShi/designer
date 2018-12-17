package foundation.persist;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultMetaFieldReader implements IFieldReader {
	
	private ResultSetMetaData resultMeta;
	private int pos;
	
	
	public ResultMetaFieldReader(ResultSetMetaData resultMeta) {
		this.resultMeta = resultMeta;
		pos = 0;
	}

	public String getFieldName() throws SQLException {
		return resultMeta.getColumnName(pos);
	}

	public int getFieldType() throws SQLException {
		int value = resultMeta.getColumnType(pos);
		return value;
	}

	public int getFieldLength() throws SQLException {
		return resultMeta.getColumnDisplaySize(pos);
	}

	public int getNullable() throws SQLException {
		return resultMeta.isNullable(pos);
	}

	public boolean next() throws SQLException {
		if (pos < resultMeta.getColumnCount()) {
			pos++;
			return true;
		}

		return false;
	}

	@Override
	public void first() {
		pos = 0;
	}

}

package foundation.dictionary;

import foundation.data.DataType;

public interface IDictionaryDefination {

	String getCode();

	String getFieldName();
	
	String getKeyFieldName();
	
	String getValueFieldName();
	
	String getToFieldName();
	
	DataType getToDataType();
	
	String getTableName();

	String getSelectFieldNames();
	
	String getFilter();

}

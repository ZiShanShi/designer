package foundation.persist;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import foundation.dictionary.Dictionary;
import foundation.dictionary.DictionaryContainer;
import foundation.dictionary.IDictionaryDefination;
import foundation.persist.sql.SQLRunner;
import foundation.util.Util;

public class TableMeta implements Iterable<Field> {

	protected static Logger logger;
	private static TableMetaCenter metaCenter;
	private static DictionaryContainer dictionaryContainer;
	private String name;
	private List<Field> fieldList;
	private String[] names;
	private String[] lowerNames;
	private int[] virtualIndex;
	private String[] virtualNames;
	private String[] virtualLowerNames;
	private Dictionary[] dictionarys;
	private int index_key;
	private Map<String, Integer> nameIndexMap;
	private List<IDictionaryDefination> definationList;
	private List<String> validFields;
	private List<String> validColumns;

	static {
		logger = Logger.getLogger(TableMeta.class);
		dictionaryContainer = DictionaryContainer.getInstance();
		metaCenter = TableMetaCenter.getInstance();
	}

	public TableMeta(String tableName, IFieldReader fieldReader) throws SQLException {
		this.name = tableName;
		nameIndexMap = new HashMap<String, Integer>();
		fieldList = new ArrayList<Field>();
		index_key = -1;

		initFields(fieldReader);
	}

	public static TableMeta getInstance(String tableName) throws Exception {
		return metaCenter.get(tableName);
	}

	public static TableMeta getInstance(String tableName, boolean refresh) throws Exception {
		return metaCenter.get(tableName, refresh);
	}

	private void initFields(IFieldReader fieldReader) throws SQLException {
		String fieldName;
		int columnType;
		int fieldLength;
		int nullable;
		Field field;

		fieldReader.first();

		while (fieldReader.next()) {
			fieldName = fieldReader.getFieldName();
			columnType = fieldReader.getFieldType();
			fieldLength = fieldReader.getFieldLength();
			nullable = fieldReader.getNullable();

			if (fieldName == null) {
				continue;
			}

			field = new Field(fieldName);
			fieldList.add(field);

			field.setType(columnType);
			field.setLength(fieldLength);
			field.setNullable(nullable);
		}

		int fieldCount = fieldList.size();
		names = new String[fieldCount];
		lowerNames = new String[fieldCount];

		String name;
		for (int i = 0; i < fieldCount; i++) {
			field = fieldList.get(i);
			name = field.getName();
			names[i] = name;

			name = name.toLowerCase();
			lowerNames[i] = name;
			nameIndexMap.put(name, i);
		}
	}

	public int getFieldCount() {
		return fieldList.size();
	}

	public Iterator<Field> iterator() {
		return fieldList.iterator();
	}

	public String getName() {
		return name;
	}

	public void setPrimaryKey(String keyField) {
		if (keyField == null) {
			return;
		}

		Integer idx = nameIndexMap.get(keyField.toLowerCase());

		if (idx != null) {
			index_key = idx;
		}
	}

	public List<Field> getFields() {
		return fieldList;
	}

	public Field get(String name) {
		if (name == null)
			return null;

		Integer idx = nameIndexMap.get(name.toLowerCase());

		if (idx == null) {
			return null;
		}

		return fieldList.get(idx);
	}

	public Field get(int idx) {
		return fieldList.get(idx);
	}

	public synchronized String getFiledName_Key() throws Exception {
		if (index_key >= 0) {
			String keyName = SQLRunner.getPrimaryKeyField(null, name);

			if (keyName == null) {
				return null;
			}

			Integer idx = nameIndexMap.get(keyName.toLowerCase());

			if (idx == null) {
				return null;
			}
		}

		if (index_key >= 0) {
			Field field = fieldList.get(index_key);
			return field.getName();
		}

		return "id";
	}

	public int getIndex_Key() {
		return index_key;
	}

	public int getIndex(String fieldName) {
		if (fieldName == null) {
			return -2;
		}
		
		return nameIndexMap.get(fieldName.toLowerCase());
	}

	public String[] getVirtualNames() {
		return virtualNames;
	}

	public String[] getLowerNames() {
		return lowerNames;
	}

	public String[] getVirtualLowerNames() {
		return virtualLowerNames;
	}

	public boolean contains(String name) {
		if (name == null) {
			return false;
		}

		name = name.toLowerCase();
		return nameIndexMap.containsKey(name);
	}

	public void publishDictionaryDefinations(List<IDictionaryDefination> definationList) {
		if (definationList.isEmpty()) {
			return;
		}

		this.definationList = definationList;

		List<IDictionaryDefination> validDefinationList = new ArrayList<IDictionaryDefination>();

		for (IDictionaryDefination defination : definationList) {
			String propertyName = defination.getFieldName();

			if (contains(propertyName)) {
				validDefinationList.add(defination);
			}
		}

		if (validDefinationList.isEmpty()) {
			return;
		}

		int virtualSize = validDefinationList.size();

		virtualNames = new String[virtualSize];
		virtualLowerNames = new String[virtualSize];
		virtualIndex = new int[virtualSize];

		IDictionaryDefination defination;
		String fieldName, toFieldName, code;

		for (int i = 0; i < virtualSize; i++) {
			defination = validDefinationList.get(i);

			code = defination.getCode();
			fieldName = defination.getFieldName();
			toFieldName = defination.getToFieldName();

			virtualNames[i] = toFieldName;
			virtualLowerNames[i] = toFieldName.toLowerCase();
			virtualIndex[i] = nameIndexMap.get(fieldName.toLowerCase());
			dictionarys[i] = dictionaryContainer.get(code);
		}
	}

	public void setObject(Object[] dataArray, String name, Object object) {
		if (name == null) {
			return;
		}

		name = name.toLowerCase();
		Integer idx = nameIndexMap.get(name);

		if (idx == null) {
			return;
		}

		dataArray[idx] = object;
	}

	public void setString(Object[] dataArray, String name, String value) throws ParseException {
		if (name == null) {
			return;
		}

		name = name.toLowerCase();
		Integer idx = nameIndexMap.get(name);

		if (idx == null) {
			return;
		}

		Field field = fieldList.get(idx);
		Object obj = field.stringToObject(value);

		dataArray[idx] = obj;
	}
	

	public String getDateString(Object[] dataArray, String fieldName, String defaultValue) throws SQLException {
		if (fieldName == null) {
			return null;
		}

		fieldName = fieldName.toLowerCase();
		Integer idx = nameIndexMap.get(fieldName);

		if (idx == null) {
			return null;
		}

		Field field = fieldList.get(idx);
		Object obj = dataArray[idx];

		if (obj == null) {
			return defaultValue;
		}

		return field.objectToDateString(obj);
	}


	public String getString(Object[] dataArray, String name, String defaultValue) throws Exception {
		if (name == null) {
			return null;
		}

		name = name.toLowerCase();
		Integer idx = nameIndexMap.get(name);

		if (idx == null) {
			return null;
		}

		Field field = fieldList.get(idx);
		Object obj = dataArray[idx];

		if (obj == null) {
			return defaultValue;
		}

		return field.objectToString(obj);
	}

	public String getString(Object[] dataArray, int idx, String defaultValue) throws Exception {
		Field field = fieldList.get(idx);
		Object obj = dataArray[idx];

		if (obj == null) {
			return defaultValue;
		}

		return field.objectToString(obj);
	}

	public boolean getBoolean(Object[] dataArray, String name) {
		if (name == null) {
			return false;
		}

		name = name.toLowerCase();
		Integer idx = nameIndexMap.get(name);

		if (idx == null) {
			return false;
		}

		Field field = fieldList.get(idx);
		Object obj = dataArray[idx];

		return field.objectToBoolean(obj);
	}

	public Date getDate(Object[] dataArray, String name) throws ParseException {
		if (name == null) {
			return null;
		}

		name = name.toLowerCase();
		Integer idx = nameIndexMap.get(name);

		if (idx == null) {
			return null;
		}

		Field field = fieldList.get(idx);
		Object obj = dataArray[idx];

		return field.objectToDate(obj);
	}

	public Integer getInteger(Object[] dataArray, String name) {
		if (name == null) {
			return null;
		}

		name = name.toLowerCase();
		Integer idx = nameIndexMap.get(name);

		if (idx == null) {
			return null;
		}

		Field field = fieldList.get(idx);
		Object obj = dataArray[idx];

		return field.objectToInteger(obj);
	}
	
	public BigDecimal getBigDecimal(Object[] dataArray, String name) {
		if (name == null) {
			return null;
		}

		name = name.toLowerCase();
		Integer idx = nameIndexMap.get(name);

		if (idx == null) {
			return null;
		}

		Field field = fieldList.get(idx);
		Object obj = dataArray[idx];

		return field.objectToBigDecimal(obj);
	}

	public String getSchemaString(Object[] dataArray, int idx) throws Exception {
		Field field = fieldList.get(idx);
		Object obj = dataArray[idx];

		return field.objectToSchemaString(obj);
	}

	public String getJSONSString(Object[] dataArray, int idx) throws SQLException {
		Field field = fieldList.get(idx);
		Object obj = dataArray[idx];

		return field.objectToJSONSString(obj);
	}

	public String getSqlString(Object value, String name) throws Exception {
		if (name == null) {
			return null;
		}

		name = name.toLowerCase();
		Integer idx = nameIndexMap.get(name);

		if (idx == null) {
			return null;
		}

		Field field = fieldList.get(idx);
		return field.objectToSQLString(value);
	}

	public void setValueToStatement(Object[] dataArray, int dataIdx, PreparedStatement stmt, int paramIdx)
			throws SQLException {
		Field field = fieldList.get(dataIdx);
		Object obj = dataArray[dataIdx];

		field.setValueToStatement(obj, stmt, paramIdx);
	}

	public String getSQLString(Object[] dataArray, int idx) throws Exception {
		Field field = fieldList.get(idx);
		Object obj = dataArray[idx];

		return field.objectToSQLString(obj);
	}



	public String getFieldNames() {
		StringBuffer result = new StringBuffer();

		boolean empty = true;

		for (String name : names) {
			if (!empty) {
				result.append(", ");
			}

			result.append(name);
			empty = false;
		}

		return result.toString();
	}

	public String getDoubleQuotedFieldNames() {
		StringBuffer result = new StringBuffer();

		boolean empty = true;

		for (String name : names) {
			if (!empty) {
				result.append(", ");
			}

			result.append(Util.doubleQuotedStr(name));
			empty = false;
		}

		return result.toString();
	}

	public void copyVirtualNamesForm(TableMeta other) {
		if (other == null) {
			return;
		}

		List<IDictionaryDefination> otherDifinationList = other.definationList;

		if (otherDifinationList == null) {
			return;
		}

		publishDictionaryDefinations(otherDifinationList);

	}

	public List<String> getValidFields() {
		return validFields;
	}

	public List<String> getValidColumns() {
		return validColumns;
	}

	public void loadVaildField(List<String> validColumns, List<String> validFields) {
		this.validColumns = validColumns;
		this.validFields = validFields;
	}

}

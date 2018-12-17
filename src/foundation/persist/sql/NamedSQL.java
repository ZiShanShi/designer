package foundation.persist.sql;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import foundation.data.Entity;
import foundation.persist.Field;
import foundation.persist.SystemCondition;
import foundation.persist.TableMeta;
import foundation.util.ContentBuilder;
import foundation.util.Util;


public class NamedSQL implements Iterable<SQLVariant> {

	public static final String Param_Schema = "schema";
	public static final String Param_TableName = "tablename";
	public static final String Param_FieldNames = "fieldNames";
	public static final String Param_FieldNameValues = "fieldNameValues";
	public static final String Param_Values = "fieldValues";	
	public static final String Param_PlaceHolders = "placeHolders";
	public static final String Param_Filter = "filter";
	public static final String Param_FieldNamePlaceHolders = "fieldNamePlaceHolders";
	public static final String Param_KeyFieldName = "keyFieldName";
	public static final String Param_OrderBy = "orderby";
	
	private static NamedSQLContainer namedSQLContainer;
	
	protected String name;
	protected String sql;
	protected ReturnType returnType;
	protected SQLCreator sqlCreator;
	
	static {
		namedSQLContainer = NamedSQLContainer.getInstance();
	}

	private NamedSQL(String name) {
		this.name = name;
		returnType = ReturnType.None;
	}
	
	public NamedSQL(String name, String sql) throws Exception {
		this.name = name;
		this.sql = sql;
		
		parseSQL(sql);
	}
	
	protected void parseSQL(String sql) throws Exception {
		sqlCreator = new SQLCreator(8);
		SQLParser parser = new SQLParser(sqlCreator);
		parser.parse(sql);		
	}

	public static NamedSQL[] getInstance(String[] names) throws Exception {
		if (names == null) { return new NamedSQL[0]; };
		
		NamedSQL[] result = new NamedSQL[names.length];
		
		for (int i = 0; i < names.length; i++) {
			result[i] = getInstance(names[i]);
		}
		
		return result;
	}
	
	public static NamedSQL getInstance(String name) throws Exception {
		String condtion = SystemCondition.getValue();
		
		NamedSQL result = namedSQLContainer.get(name, condtion);
		
		if (result == null) {
			result = namedSQLContainer.get(name, null);
		}
		
		if (result == null) {
			throw new Exception("can not find named sql: " + name);
		}
		
		result = result.newInstance();
		return result;
	}
	
	public NamedSQL newInstance() throws Exception {
		NamedSQL instance = new NamedSQL(this.name);
		instance.sql = this.sql;
		instance.sqlCreator = this.sqlCreator.newInstance();
		
		return instance;
	}

	public String getSQL() throws Exception {
		String result = sqlCreator.getSql();
		return result;
	}
	
	public String getOriginalSql(){
		return sql;
	}

	public Result exec() throws Exception {
		Result result = SQLRunner.getResult(this);
		return result;
	}
	
	public void setSchema(String schema) {
		setParam(Param_Schema, schema);
	}

	public void setTableName(String tableName) {
		setParam(Param_TableName, tableName);
	}

	public void setFieldNames(String names) {
		setParam(Param_FieldNames, names);
	}
	
	public void setFieldNames(TableMeta tableMeta) {
		StringBuilder result = new StringBuilder();
		boolean empty = true;
		
		for (Field field: tableMeta) {
			if (!empty) {
				result.append(", ");
			}
			
			result.append(field.getName());
			
			empty = false;
		}
		
		setParam(Param_FieldNames, result.toString());
	}
	
	public void setFieldNames(TableMeta tableMeta, Entity entity) {
		StringBuilder result = new StringBuilder();
		boolean empty = true;
		Field field;
		
		for (int i = 0; i < tableMeta.getFieldCount(); i++) {
			if (entity.isEmptyField(i)) {
				continue;
			}
				
			field = tableMeta.get(i);
			
			if (!empty) {
				result.append(", ");
			}

			result.append(field.getName());
			empty = false;
		}
		
		setParam(Param_FieldNames, result.toString());
	}
	
	public void setFieldNames(Collection<Field> fields) {
		ContentBuilder result = new ContentBuilder();
		
		for (Field field: fields) {
			result.append(field.getName(), ", ");
		}
		
		setParam(Param_FieldNames, result.toString());
	}
	
	public void setValues(Entity entity) throws Exception {
		ContentBuilder result = new ContentBuilder();
		int cnt = entity.getFieldCount();
		
		for (int i = 0; i < cnt; i++) {
			if (entity.isEmptyField(i)) {
				continue;
			}
			
			result.append(entity.getSQLString(i), ", ");
		}
		
		setParam(Param_Values, result.toString());
	}
	
	public void setQuotedFieldNames(TableMeta tableMeta) {
		ContentBuilder result = new ContentBuilder();
		
		for (Field field: tableMeta) {
			result.append(Util.doubleQuotedStr(field.getName()), ", ");
		}
		
		setParam(Param_FieldNames, result.toString());
	}

	public void setPlaceHolders(Collection<Field> fields) {
		ContentBuilder result = new ContentBuilder();
		int cnt = fields.size();
		
		for (int i = 0; i < cnt; i++) {
			result.append("?", ", ");
		}
		
		setParam(Param_PlaceHolders, result.toString());
	}
	
	public void setPlaceHolders(String placeHolders) {
		setParam(Param_PlaceHolders, placeHolders);
	}

	public void setFieldNamePlaceHolders(TableMeta tableMeta) {
		ContentBuilder result = new ContentBuilder();
		
		for (Field field: tableMeta) {
			result.append(field.getName() + " = ? ", ", ");
		}
		
		setParam(Param_FieldNamePlaceHolders, result.toString());
	}

	public void setKeyFieldName(TableMeta tableMeta) throws Exception {
		String keyName = tableMeta.getFiledName_Key();
		setParam(Param_KeyFieldName, keyName);		
	}

	public void setKeyFieldName(String fieldName) throws Exception {
		setParam(Param_KeyFieldName, fieldName);			
	}
	
	public void setFilter(String filter) {
		if (Util.isEmptyStr(filter)) {
			filter = "1=1";
		}
		
		setParam(Param_Filter, filter);	
	}

	public void setOrderBy(String orderby) {
		setParam(Param_OrderBy, orderby);	
	}

	public void setFieldNameValues(Entity entity) throws Exception {
		ContentBuilder result = new ContentBuilder();
		
		TableMeta tableMeta = entity.getTableMeta();
		int cnt = tableMeta.getFieldCount();
		
		for (int i = 0; i < cnt; i++) {
			if (entity.isEmptyField(i)) {
				continue;
			}
			
			Field field = tableMeta.get(i);
			result.append(field.getName() + "=" + entity.getSQLString(i), ", ");
		}
		
		setParam(Param_FieldNameValues, result.toString());
	}

	public void setParam(Entity entity) {
		if (entity == null) {
			return;
		}
		
		List<String> matcher = Util.matcher(sql);
		for (String preReplaceField : matcher) {
			SQLVariant sqllVariant = sqlCreator.getVariant(preReplaceField);
			String replacedValue = entity.getString(preReplaceField);
			
			if (sqllVariant != null) {
				sqllVariant.setValue(replacedValue);
			}
		}
		
	}
	public void setParam(String name, String value) {
		if (value == null) {
			return;
		}
		
		SQLVariant sqllVariant = sqlCreator.getVariant(name);
		
		if (sqllVariant != null) {
			sqllVariant.setValue(value);
		}
	}
	
	public void setParam(String name, String value, String defaultValue) {
		if (value == null) {
			value = defaultValue;
		}
		
		SQLVariant sqllVariant = sqlCreator.getVariant(name);
		
		if (sqllVariant != null) {
			sqllVariant.setValue(value);
		}
	}
	
	public void setParam(String name, int value) {
		String stringValue = String.valueOf(value);
		setParam(name, stringValue);
	}
	
	public void setParam(String name, BigDecimal value) {
		String stringValue = value.toString();
		setParam(name, stringValue);		
	}
	
	public void setParam(String name, Date date) {
		String stringValue = Util.toMySQLDateStr(date);
		setParam(name, stringValue);
	}
	
	public void setParam(String name, boolean value) {
		String stringValue = Util.booleanToStr(value);
		setParam(name, stringValue);
	}

	public String getName() {
		return name;
	}

	public ReturnType getReturnType() {
		return returnType;
	}
	

	public void setReturnType(ReturnType returnType) {
		this.returnType = returnType;
	}
	
	public String getSQLString() throws Exception {
		List<SQLVariant> variantList = sqlCreator.getVariantList();
		
		for (SQLVariant variant : variantList) {
			if (variant.isEmpty()) {
				throw new Exception("empty sql param: " + variant.getName());
			}
		}
		
		return sqlCreator.getSql(); 
	}
	
	@Override
	public String toString() {
		return sqlCreator.getSql();
	}

	@Override
	public Iterator<SQLVariant> iterator() {
		return sqlCreator.iterator();
	}

	public Map<String, SQLVariant> getVariantMap() {
		return sqlCreator.getVariantMap();
	}
	
	public void clearVariantValues() {
		for (SQLVariant variant: sqlCreator) {
			variant.clearValue();
		}
	}

	public List<SQLVariant> getVariantList() {
		return sqlCreator.getVariantList();
	}

}

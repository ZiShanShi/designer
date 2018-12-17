package foundation.persist;

import java.sql.Connection;

import foundation.config.Configer;
import foundation.data.Entity;
import foundation.data.EntitySet;
import foundation.data.Page;
import foundation.persist.loader.EntityLoader;
import foundation.persist.loader.EntitySetLoader;
import foundation.persist.loader.ValueLoader;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLRunner;
import foundation.util.Util;

public class DataHandler {

	public static EntitySet getDataSet(String tableName, String filter) throws Exception {
		return getDataSet(tableName, filter, null);
	}
	public static EntitySet getDataSet(String tableName) throws Exception {
		return getDataSet(tableName, null, null);
	}
	
	public static EntitySet getDataSet(String tableName, String filter, String orderby) throws Exception {
		NamedSQL namedSQL = NamedSQL.getInstance("getDataSet");

		namedSQL.setTableName(tableName);
		namedSQL.setFilter(filter);
		namedSQL.setOrderBy(orderby);		
		
		EntitySetLoader loader = new EntitySetLoader(tableName);
		SQLRunner.getData(namedSQL, loader);
		
		return loader.getDataSet();
	}
	
	public static EntitySet getDataSetByPage(TableMeta tableMeta, String fields, String filter, Page page, String orderby) throws Exception {
		NamedSQL namedSQL = NamedSQL.getInstance("getFieldsDataSet");
		String tableName = tableMeta.getName();
		namedSQL.setTableName(tableName);
		
		if (Util.isEmptyStr(fields)) {
			fields = tableMeta.getFieldNames();
		}
		if (Util.isEmptyStr(orderby)) {
			orderby = "id";
		}
		namedSQL.setFilter(filter);
		namedSQL.setFieldNames(fields);
		namedSQL.setOrderBy(orderby);		
		
		String dbtype = Configer.getDataBaseType().toString();
		NamedSQL pageDataSQL = NamedSQL.getInstance("getDataByPage_" + dbtype);
		pageDataSQL.setParam("sql", namedSQL.getSQLString());
		pageDataSQL.setParam("beginNo", page.getBeginRecordNo_1());
		pageDataSQL.setParam("pageSize", page.getPageSize());		
		pageDataSQL.setParam("endno", page.getEndRecordNo());		
		
		EntitySetLoader loader = new EntitySetLoader(tableName);
		SQLRunner.getData(pageDataSQL, loader);
		
		return loader.getDataSet();
	}
	
	private static EntitySet getDataSetByPage(String name, String fields, String filter, Page page, String orderby) throws Exception {
		NamedSQL namedSQL = NamedSQL.getInstance("getFieldsDataSet");
		namedSQL.setTableName(name);
		
		if (Util.isEmptyStr(fields)) {
			fields = " * ";
		}
		if (Util.isEmptyStr(orderby)) {
			orderby = "id";
		}
		namedSQL.setFilter(filter);
		namedSQL.setFieldNames(fields);
		namedSQL.setOrderBy(orderby);		
		
		String dbtype = Configer.getDataBaseType().toString();
		NamedSQL pageDataSQL = NamedSQL.getInstance("getDataByPage_" + dbtype);
		pageDataSQL.setParam("sql", namedSQL.getSQLString());
		pageDataSQL.setParam("beginNo", page.getBeginRecordNo_1());
		pageDataSQL.setParam("pageSize", page.getPageSize());		
		pageDataSQL.setParam("endno", page.getEndRecordNo());		
		
		EntitySetLoader loader = new EntitySetLoader(name);
		SQLRunner.getData(pageDataSQL, loader);
		
		return loader.getDataSet();
	}
	
	public static EntitySet getDataSetByPage(TableMeta tableMeta, String filter, Page page) throws Exception {
		return getDataSetByPage(tableMeta, null, filter, page, null);
	}
	
	public static EntitySet getDataSetByPage(TableMeta tableMeta, String fields, String filter, Page page) throws Exception {
		return getDataSetByPage(tableMeta, fields, filter, page, null);
	}
	
	public static EntitySet getDataSetByPage(String name,String filter, Page page, String orderby) throws Exception {
		return getDataSetByPage(name, null, filter, page, null);
	}

	
	public static Entity getLine(String tableName, String id) throws Exception {
		TableMeta tableMeta = TableMetaCenter.getInstance().get(tableName);
		
		NamedSQL namedSQL = NamedSQL.getInstance("getLineById");
		namedSQL.setTableName(tableName);
		namedSQL.setParam("fieldNameId", tableMeta.getFiledName_Key());
		namedSQL.setParam("id", Util.quotedStr(id));
		
		EntityLoader loader = new EntityLoader(tableName);
		SQLRunner.getData(namedSQL, loader);
		
		return loader.getEntity();
	}
	
	public static void deleteById(String tableName, String id) throws Exception {
		NamedSQL namedSQL = NamedSQL.getInstance("deleteById");
		namedSQL.setTableName(tableName);
		namedSQL.setParam("id", id);
		
		SQLRunner.execSQL(namedSQL);
	}

	public static int getCount(String tableName, String filter) throws Exception {
		NamedSQL namedSQL = NamedSQL.getInstance("getCount");
		namedSQL.setTableName(tableName);
		if (Util.isEmptyStr(filter)) {
			filter = " 1=1 ";
		}
		namedSQL.setFilter(filter);
		
		ValueLoader loader = new ValueLoader();
		SQLRunner.getData(namedSQL, loader);
		
		return loader.getInt();
	}
	
	public static int getCount(String tableName) throws Exception {
		NamedSQL namedSQL = NamedSQL.getInstance("getCount");
		namedSQL.setTableName(tableName);
		namedSQL.setFilter(" 1=1 ");
		
		ValueLoader loader = new ValueLoader();
		SQLRunner.getData(namedSQL, loader);
		
		return loader.getInt();
	}

	public static EntitySet getSetByPage(String tableName, String filter, Page page) throws Exception {
		NamedSQL namedSQL = NamedSQL.getInstance("getSetByPage");
		namedSQL.setTableName(tableName);
		namedSQL.setFilter(filter);
		namedSQL.setParam("beginNo", page.getBeginRecordNo());
		namedSQL.setParam("endNo", page.getEndRecordNo());
		
		EntitySetLoader loader = new EntitySetLoader(tableName);
		SQLRunner.getData(namedSQL, loader);
		
		return loader.getDataSet();
	}
	
	public static EntitySet getSetByPage(TableMeta tableMeta, String filter, Page page) throws Exception {
		String tableName = tableMeta.getName();
		String fieldNames = tableMeta.getDoubleQuotedFieldNames();
		
		NamedSQL namedSQL = NamedSQL.getInstance("getSetByPage");
		namedSQL.setTableName(tableName);
		namedSQL.setFilter(filter);
		namedSQL.setParam("fieldNames", fieldNames);	
		namedSQL.setParam("beginNo", page.getBeginRecordNo());
		namedSQL.setParam("endNo", page.getEndRecordNo());
		
		EntitySetLoader loader = new EntitySetLoader(tableName);
		loader.setTableMeta(tableMeta);
		SQLRunner.getData(namedSQL, loader);
		
		return loader.getDataSet();
	}
	
	public static EntitySet getSetByPage(TableMeta tableMeta, String fields, String filter, Page page) throws Exception {
		String tableName = tableMeta.getName();
		
		NamedSQL namedSQL = NamedSQL.getInstance("getSetByPage");
		namedSQL.setTableName(tableName);
		
		if (Util.isEmptyStr(filter)) {
			filter = " 1=1 ";
		}
		
		namedSQL.setFilter(filter);
		
		if (Util.isEmptyStr(fields)) {
			fields = tableMeta.getFieldNames();
		}
		
		namedSQL.setParam("fieldNames", fields);	
		namedSQL.setParam("beginNo", page.getBeginRecordNo());
		namedSQL.setParam("endNo", page.getEndRecordNo());
		
		EntitySetLoader loader = new EntitySetLoader(tableName);
		loader.setTableMeta(tableMeta);
		SQLRunner.getData(namedSQL, loader);
		
		return loader.getDataSet();
	}

	public static void addLine(Entity entity) throws Exception {
		addLine(null, entity);
	}
	
	public static void addLine(Connection conn, Entity entity) throws Exception {
		TableMeta tableMeta = entity.getTableMeta();
		String tableName = tableMeta.getName();	
		
		NamedSQL namedSQL = NamedSQL.getInstance("insert");
		namedSQL.setTableName(tableName);
		namedSQL.setFieldNames(tableMeta, entity);
		namedSQL.setValues(entity);
		
		SQLRunner.execSQL(conn, namedSQL);
	}

	public static void saveLine(Entity entity) throws Exception {
		TableMeta tableMeta = entity.getTableMeta();
		String tableName = tableMeta.getName();	
		
		NamedSQL namedSQL = NamedSQL.getInstance("getCountOfId");
		namedSQL.setTableName(tableName);
		namedSQL.setParam("id", Util.quotedStr(entity.getString("id")));
		int cnt = SQLRunner.getInteger(namedSQL);
		
		if (cnt == 0) {
			addLine(entity); 
		}
		else {
			updateLine(entity);
		}
	}

	public static void updateLine(Entity entity) throws Exception {
		TableMeta tableMeta = entity.getTableMeta();
		String tableName = tableMeta.getName();	
		
		NamedSQL namedSQL = NamedSQL.getInstance("updateById");
		namedSQL.setTableName(tableName);
		namedSQL.setFieldNameValues(entity);
		namedSQL.setParam("fieldNameId", "id");
		namedSQL.setParam("id", Util.quotedStr(entity.getString("id")));
		
		SQLRunner.execSQL(namedSQL);		
	}
	
	public static void deleteLine(Entity entity) throws Exception {
		TableMeta tableMeta = entity.getTableMeta();
		String tableName = tableMeta.getName();	
		
		NamedSQL namedSQL = NamedSQL.getInstance("deleteById");
		namedSQL.setTableName(tableName);
		namedSQL.setParam("id", entity.getString("id"));
		
		SQLRunner.execSQL(namedSQL);
	}

}

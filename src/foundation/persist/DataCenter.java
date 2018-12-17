package foundation.persist;

import java.math.BigDecimal;

import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLRunner;
import foundation.util.Util;


public class DataCenter {

	public static int Count_BatchLoad = 10000;
	public static int Count_BatchSave = 10000;
	
	public static String SQL_Name_GetDataFilter = "getDataFilter";
	public static String SQL_Name_UploadData = "uploadData";
	public static String SQL_Name_WriteUploadRecord = "writeUploadRecord";
	public static String SQL_Name_WriteUploadSuccess = "writeUploadSuccess";
	public static String SQL_Name_ChangePassword = "changePassword";
	public static String SQL_Name_EmptyTable = "emptyTable";
	public static String SQL_Name_LoadOrgCode = "loadOrgCode";
	public static String SQL_Name_LoadQRCode = "loadQRCode";
	public static String SQL_Name_LoadUPNCode = "loadUPNCode";
	public static String SQL_Name_LoadDocNo = "loadDocNo";
	
	public static String Param_Name_TableNameField = "table_name";
	public static String Param_Name_DataLogTable = "data_log";
	public static String Param_Name_LocalStamp = "localStamp";
	public static String Param_Name_MaxStamp = "maxStamp";
	public static String Param_Name_Schema = "schema";
	public static String Param_Name_TableListId = "tableListId";
	public static String Param_Name_CollectorId = "collectorId";
	public static String Param_Name_OperatorField = "operatorField";
	public static String Param_Name_RowIdField = "rowIdField";
	
	
	private static String SQL_Name_WritePublisherActive = "writePublisherActive";
	private static String SQL_Name_WriteTotalQty = "writeTotalQty";
	
	public static String getDataFilter(String username) throws Exception {
		NamedSQL namedSQL = NamedSQL.getInstance(SQL_Name_GetDataFilter);
		namedSQL.setParam("username", username);
		
		String result = SQLRunner.getString(namedSQL);
		return result;
	}


	public static String writeUploadRecord(String username, String filename, String path) throws Exception {
		String id = Util.newShortGUID();
		
		NamedSQL namedSQL = NamedSQL.getInstance(SQL_Name_WriteUploadRecord);
		namedSQL.setParam("id", id);
		namedSQL.setParam("filename", filename);
		namedSQL.setParam("path", path);		
		namedSQL.setParam("username", username);
		
		SQLRunner.execSQL(namedSQL);
		
		return id;
	}

	public static void writeUploadSuccess(String id, int qty) throws Exception {
		NamedSQL namedSQL = NamedSQL.getInstance(SQL_Name_WriteUploadSuccess);
		namedSQL.setParam("id", id);
		namedSQL.setParam("qty", qty);
		
		SQLRunner.execSQL(namedSQL);
	}

	public static void changePassword(String username, String password) throws Exception {
		NamedSQL namedSQL = NamedSQL.getInstance(SQL_Name_ChangePassword);
		namedSQL.setParam("username", username);
		namedSQL.setParam("password", password);
		
		SQLRunner.execSQL(namedSQL);
	}


	public static void deleteData(String tableName) throws Exception {
		NamedSQL namedSQL = NamedSQL.getInstance(SQL_Name_EmptyTable);
		namedSQL.setTableName(tableName);
		
		SQLRunner.execSQL(namedSQL);
	}

	public static void writePublisherActive(String id, boolean value) throws Exception {
		NamedSQL namedSQL = NamedSQL.getInstance(SQL_Name_WritePublisherActive);
		namedSQL.setParam("id", id);
		namedSQL.setParam("value", Util.booleanToStr(value));
		
		SQLRunner.execSQL(namedSQL);
	}
	
	public static int readTotalQty(String tableName) throws Exception {
		NamedSQL namedSQL = NamedSQL.getInstance(SQL_Name_WriteTotalQty);		
		namedSQL.setTableName(tableName);
		
		Object value = SQLRunner.getObject(namedSQL, 1);
		
		if (value instanceof BigDecimal) {
			BigDecimal result = (BigDecimal) value;
			return result.intValue();
		}
		else if (value instanceof Long) {
			Long result = (Long) value;
			return result.intValue();
		}
		else if (value instanceof Integer) {
			Integer result = (Integer) value;
			return result;
		}		
		
		String str = String.valueOf(value);
		return Integer.valueOf(str);
	}
}

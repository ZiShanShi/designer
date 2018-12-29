package foundation.util;

import foundation.config.Configer;
import foundation.data.DataType;
import foundation.data.Entity;
import foundation.data.EntitySet;
import foundation.persist.DataBaseType;
import foundation.persist.DataHandler;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Util {
	
	public static final String TRUE = "T"; 
	public static final String FALSE = "F"; 
	public static final String String_Return = "\r\n";
	public static final String String_Escape_newSpace = "\t";
	public static final String String_Escape_newLine = "\n";
	public static final String String_Empty = "";
	public static final String Default_Patter = "(?<=@\\{)(.+?)(?=\\})";
	public static final String Integer_Patter = "^-?[1-9]\\d*$";
	public static final String Double_Patter = "^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$";
	public static final String Separator = "-";
	public static final String comma = ",";
	public static final String with = "&";
	public static final String dollar = "$";
	public static final String RMB = "￥";
	public static final String ait = "@";
	public static final String semicolon = ";";
	public static final String Dot = ".";
	public static final String Equal = "=";
	public static final String Percentage = "%";
	public static final String SqlLike = "LIKE";
	public static final String String_Space = " ";
	public static final String windows_slash = "/";
	public static final String java_slash = "\\";

	public static int field;
	private static ArrayList<String> tmpArr = new ArrayList<String>();
	public static String newShortGUID() {
		UUID uuid = UUID.randomUUID();
		String strGUID;
		String shortGUID;

		strGUID = uuid.toString();
		shortGUID = strGUID.substring(0, 8) + strGUID.substring(9, 13) + strGUID.substring(14, 18)
						+ strGUID.substring(19, 23) + strGUID.substring(24, 36);

		return shortGUID;
	}

	public static String filePathJion(String preFilePath, String filePath) {
		if (preFilePath == null || filePath == null) {
			return  null;
		}
		preFilePath = preFilePath.replace(windows_slash,java_slash);
		filePath = filePath.replace(windows_slash,java_slash);
		return  preFilePath + java_slash + filePath;
	}

	public static String stringJoin(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s: strings) {
			stringBuilder.append(s);
		}
		return  stringBuilder.toString();
	}

	public static String quotedStr(String str) {
		if (str != null)
			return "'" + str + "'";
		else
			return "''";
	}
	
	public static String doubleQuotedStr(String str) {
		if (str != null)
			return "\"" + str + "\"";
		else
			return "\"\"";
	}
	
	public static String quotedLikeStr(String str) {
		if (str != null)
			return "'%" + str + "%'";
		else
			return "''";
	}
	public static String quotedLeftLikeStr(String str) {
		if (str != null)
			return "'%" + str + "'";
		else
			return "''";
	}
	public static String quotedRightLikeStr(String str) {
		if (str != null)
			return "'" + str + "'";
		else
			return "''";
	}
	
	public static String quotedEqualStr(String key, String value) {
		if (!Util.isEmptyStr(key) && !Util.isEmptyStr(value))
			return key + String_Space + Equal + String_Space + Util.quotedStr(value);
		else
			return null;
	}
	
	public static String bracketStr(String str) {
		if (str != null)
			return "(" + str + ")";
		else
			return "()";
	}

	public static String getDBUUID() {
		DataBaseType dbType = Configer.getDataBaseType();
		
		if (DataBaseType.Oracle == dbType) {
			return newOracleUUID();
		}
		else if (DataBaseType.SQLServer == dbType) {
			return newSqlServerUUID();
		}
		else if (DataBaseType.MySQL == dbType) {
			return newMySqlUUID();
		}
		else {
			return newShortGUID();
		}
	}
	
	private static String newMySqlUUID() {
		return "replace(uuid(),'-','')";
	}

	private static String newSqlServerUUID() {
		return "replace(NEWID(),'-', '')";
	}

	private static String newOracleUUID() {

		return "lower(sys_guid())";
	}

	public static String newDBDateString() throws Exception {
		Date date = new Date();
		return newDBDateString(date);
	}
	
	public static String newDBDateString(Date date) throws Exception {
		DataBaseType dbType = Configer.getDataBaseType();
		
		if (DataBaseType.Oracle == dbType) {
			return newOracleDateString(date);
		}
		else if (DataBaseType.SQLServer == dbType) {
			return newSqlServerDateString(date);
		}
		else if (DataBaseType.MySQL == dbType) {
			return newMySqlDateString(date);
		}
		else {
			return DataTimeToString(date);
		}
	}
	
	public static String toOracleDataStr(String dataStr) {
		return "to_date('" + dataStr + "','YYYY-MM-DD HH24:MI:SS')";
	}

	public static String toMySQLDateStr(Date value) {
		return DataTimeToString(value, "yyyy-MM-dd HH:mm:ss");
	}

	public static String DataTimeToString(Date value) {
		return DataTimeToString(value, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String DataTimeToString(Date value, String format) {
		if (value == null) {
			return null;
		}
		
		String result = "";
		DateFormat dateFormat = new SimpleDateFormat(format);
		result = dateFormat.format(value);
		
		return result;		
	}
	
	public static String newDateStr(){
		return newDateTimeStr("yyyy-MM-dd");
	}
	
	public static String newDateTimeStr(){
		return newDateTimeStr("yyyy-MM-dd kk:mm:ss");		
	}
	
	public static String newDateTimeStr(String fomater){
		return getDateTimeStr(new Date(), fomater);
	}
	
	public static String getDateTimeStr(Date date, String fomater){
		String result = "";
		DateFormat dateFormat = new SimpleDateFormat(fomater);
		result = dateFormat.format(date);
		
		return result;
	}
	
	public static String booleanToStr(boolean value) {
		if (value)
			return "T";
		else
			return "F";
	}
	
	
	
	public static boolean isEmptyStr(Object str) { 
		boolean result = false;
		
		if ((str == null) || ("".equals(str)) || "null".equals(str))
			result = true;
		
		return result;
	}
	
	public static boolean isNull(Object object) {
		if ((object == null))
			return true;
		if (object instanceof String) {
			return isNull((String)object);
		}
		
		return false;
	}
	public static boolean isNull(String value) {
		if ((value == null))
			return true;

		if ("".equals(value)) {
			return true;
		}

		if (value.length() == 4) {
			value = value.toLowerCase();
			return "null".equals(value);
		}

		return false;
	}
	
	public static String UTF8decode(String str) {
		if (!isUTF8Encoding(str))
			return str;
		byte[] bytes = str.getBytes();
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		Charset csets = Charset.forName("UTF-8");
		CharBuffer c = csets.decode(bb);
		return c.toString();
	}

	private static boolean isUTF8Encoding(String str) {
		byte[] bytes = str.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			int byteLen = Byte.toString(bytes[i]).length();
			if (byteLen == 4)
				return true;
			else
				continue;
		}
		return false;
	}

	public static Boolean stringToBoolean(String value, Boolean defaultValue) {
		if (value != null) {
			value = value.toLowerCase();
			
			if (value.equalsIgnoreCase("t")) {
				return true;
			}
			else if (value.equals("y")) {
				return true;
			}
			else if (value.equals("true")) {
				return true;
			}	
			else if (value.equals("yes")) {
				return true;
			}	
			else {
				return false;			
			}
		}
		else 
			return defaultValue;
	}
	
	public static  Boolean stringToBoolean(String value) {
		return stringToBoolean(value, false);
	}

	public static int stringToInt(String value, int defaultValue) {
		if (value != null) {
			try {
				Double doubleValue = Double.valueOf(value);
				return doubleValue.intValue();
			}
			catch (Exception e) {
				return defaultValue;
			}
		}
		else 
			return defaultValue;
	}
	
	public static double StringToDouble(String dataValue) {
		BigDecimal parseBigDecimal = parseBigDecimal(dataValue);
		return parseBigDecimal.doubleValue();
		
	}
	
	public static float StringToFloat(String dataValue) {
		BigDecimal parseBigDecimal = parseBigDecimal(dataValue);
		return parseBigDecimal.floatValue();
	}
	
	@SuppressWarnings("unchecked")
	public static <T, Y> T StringToOther(String dataValue, Class<? extends  Object> clazz, Class<Y> listClazz) {
		if (clazz.getSimpleName().equalsIgnoreCase("string")) {
			return (T) dataValue;
		}
		else if (clazz.getSimpleName().equalsIgnoreCase("int")) {
			return (T)(Object) stringToInt(dataValue, -1);
		}else if (clazz.getSimpleName().equalsIgnoreCase("integer")) {
			return (T)(Object) stringToInt(dataValue, -1);
		}
		else if (clazz.getSimpleName().equalsIgnoreCase("double")) {
			return (T)(Object) StringToDouble(dataValue);
		}else if (clazz.getSimpleName().equalsIgnoreCase("long")) {
			return (T)(Object) StringtoLong(dataValue);
		}
		else if (clazz.getSimpleName().equalsIgnoreCase("float")) {
			return (T)(Object) StringToFloat(dataValue);
		}
		else if (clazz.getSimpleName().equalsIgnoreCase("date")) {
			try {
				return (T)StringToDate(dataValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		else if (clazz.getSimpleName().equalsIgnoreCase("decimal")) {
			return (T)parseBigDecimal(dataValue);
		}else if (clazz.getSimpleName().equalsIgnoreCase("boolean")) {
			return (T) stringToBoolean(dataValue);
		} else if (clazz.getSimpleName().equalsIgnoreCase("list")) {
            return (T) StringToList(dataValue, listClazz);
        } else {

            return (T) dataValue;
        }
        return  (T) dataValue;
	}

	private static Object StringtoLong(String dataValue) {
		return  new Long(dataValue);
	}

	public static <T> T StringToOther(String dataValue, Class<T> clazz)  {
		return StringToOther(dataValue, clazz, String.class);
	}

	@SuppressWarnings("unchecked")
	public static <T> T StringToOther(String dataValue, DataType type, DataType listType) {
		
		if (type.equals(DataType.String)) {
			return (T) dataValue;
		}
		else if (type.equals(DataType.Integer)) {
			return (T)(Object) stringToInt(dataValue, -1);
		}
		else if (type.equals(DataType.Double)) {
			return (T)(Object) StringToDouble(dataValue);
		}
		else if (type.equals(DataType.Float)) {
			return (T)(Object) StringToFloat(dataValue);
		}
		else if (type.equals(DataType.Date)) {
			try {
				return (T)(Object) StringToDate(dataValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		else if (type.equals(DataType.Decimal)) {
			return (T)(Object) parseBigDecimal(dataValue);
		}
		else if (type.equals(DataType.List)) {
			Class<?> listClaz = listType.getJavaClass();
			return (T) StringToList(dataValue, listClaz);
		}

		return null;
	}
	
	public static <T> T StringToOther(String dataValue, DataType type) throws ParseException {
		return StringToOther(dataValue, type, DataType.String);
	}
	
	public static <T> List<T> StringToList(String dataValue, Class<T> claz) {
		return StringToList(dataValue, semicolon, claz);
	}
	
	public static List<String> StringToList(String dataValue) {
		return StringToList(dataValue, semicolon, String.class);
	}
	
	public static <T> List<T> StringToList(String dataValue, String split, Class<T> claz){
		// 只支持一层list  
		if (Util.isEmptyStr(dataValue)) {
			return new ArrayList<>();
		}
		List<T> resultList = new ArrayList<>();
		String[] splitedData = dataValue.split(split);
		for (String oneValue : splitedData) {
			T stringToOther = StringToOther(oneValue, claz);
			resultList.add(stringToOther);
		}
		return resultList;
	}
	
	public static List<String> StringToList(String dataValue, String split)  {
		return StringToList(dataValue, split, String.class);
	}
	
	public static Date StringToDate(String str) throws ParseException {
		Date result = null;

		String fomater = null;
		
		str = str.replace('T', ' ');
		
		if (str.indexOf("/") == 4) {
			fomater = "yyyy/MM/dd";
		}
		else if (str.indexOf("/") == 2 || str.indexOf("/") == 1) {
			fomater = "MM/dd/yyyy";			
		}
		else if (str.indexOf("-") == 2 || str.indexOf("-") == 1) {
			fomater = "MM-dd-yyyy";		
		}
		else if (str.indexOf("-") == 4 && str.indexOf(":")<0) {
			fomater = "yyyy-MM-dd";				
		}
		else if (str.indexOf("-") == 4  && str.indexOf(":")>0) {
			if(str.split(":").length == 3){
			   fomater = "yyyy-MM-dd HH:mm:ss";
			}else{
			   str = str + ":00";
			   fomater = "yyyy-MM-dd HH:mm:00";
			}
						
		}		
		else if (str.indexOf(".") == 2 || str.indexOf(".") == 1) {
			fomater = "MM.dd.yyyy";	
		}
		else if (str.indexOf(".") == 4) {
			fomater = "yyyy.MM.dd";		
		}
		else if (str.indexOf("-") < 0 && str.indexOf("/") < 0) {
			fomater = "yyyyMMdd";
		}

		DateFormat dateFormat = new SimpleDateFormat(fomater);
		result = dateFormat.parse(str);
		
		return result;
	}

	public static Date doubleToDate(Double value) throws ParseException {
		Date result = null;

		if (value != null) {
			if (value > 195000 && value <= 210001) {
				value = value * 100 + 01;
			}
			
			if (value >= 19500101 && value <= 21000101) {
				String value_Str = String.valueOf(value.intValue());
				result = Util.StringToDate(value_Str);
			}
			else if (value > (1950 - 1900) * 365 && value < (2100 - 1900) * 365) {
				int dateValue = value.intValue();
				double secValue = value - dateValue;
				Date dayDate = intToDate(dateValue);
				long sec = Math.round(secValue * 24 * 3600 * 1000);
				
				result = new Date();
				result.setTime(dayDate.getTime() + sec);
				return result;
			}
		}

		return result;
	}

	public static Date intToDate(int value){
		Calendar result = Calendar.getInstance();
		result.set(Calendar.YEAR, 1900);
		result.set(Calendar.MONTH, 0);
		result.set(Calendar.DAY_OF_MONTH, 1);
		result.set(Calendar.HOUR_OF_DAY, 0);
		result.set(Calendar.MINUTE, 0);
		result.set(Calendar.SECOND, 0);
		
		result.add(Calendar.DATE, value-2);
		return result.getTime();
	}

	public static int getArrayContentSize(Object[] datas) {
		int result = 0;
		
		for (int i = 0; i < datas.length; i++) {
			if (datas[i] != null) {
				result ++;
			}
		}
		
		return result;
	}

	public static String deleteSuffix(String name) {
		String result = null;
		
		if (!isEmptyStr(name)) {
			int pos = name.lastIndexOf(".");
			result = name.substring(0, pos);
		}
		
		return result;
	}

	public static String[] mergeArray(String[] array1, String[] array2) {
		if (array1 == null) {
			return array2;
		}
		
		if (array2 == null) {
			return array2;
		}
	
		List<String> set = new ArrayList<String>(array1.length + array2.length);
		
		for (int i = 0; i < array1.length; i++) {
			set.add(array1[i]);
		}
		
		for (int i = 0; i < array2.length; i++) {
			if (!set.contains(array2[i])) {
				set.add(array2[i]);
			}
		}
		
		String[] result = new String[0];
		return set.toArray(result);
	}
	
	public static String newOracleDateString(Date date) {
		String nowStr = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		nowStr = dateFormat.format(date);

		return "to_date('" + nowStr + "','YYYY-MM-DD HH24:MI:SS')";		
	}
	
	public static String newMySqlDateString(Date date) {
		String nowStr = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		nowStr = dateFormat.format(date);

		return "('" + nowStr + "')";		
	}	
	
	public static String newSqlServerDateString(Date date) {
		String nowStr = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		nowStr = dateFormat.format(date);

		return "('" + nowStr + "')";		
	}

	public static boolean isSameString(String value1, String value2) {
		if (value1 == null) {
			if (value2 == null) {
				return true;				
			}
			else {
				return false;				
			}
		}
		else {
			if (value2 == null) {
				return false;				
			}
			else {
				return value1.equals(value2);
			}
		}
	}
	
	public static boolean isSameStringIgnoreCase(String value1, String value2) {
		if (value1 == null) {
			if (value2 == null) {
				return true;				
			}
			else {
				return false;				
			}
		}
		else {
			if (value2 == null) {
				return false;				
			}
			else {
				return value1.equalsIgnoreCase(value2);
			}
		}
	}

	public static String toLowerCase(String name, String defaultValue) {
		if (name == null) {
			return defaultValue;
		}
		else {
			return name.toLowerCase();
		}
	}
	
	public static String getExceptionStack(Exception e) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(outStream);
		e.printStackTrace(printStream);	
			
		return outStream.toString();
	}
	
	public static Date getSpecialDayOffToday(int dayCount){
		Calendar calendar=Calendar.getInstance();   
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) + dayCount);
		return calendar.getTime();
	}

	public static String getPassWord(int length) {
		  int[] array=new int[length];
		  char[] chars = new char[length];
		  StringBuilder str = new StringBuilder();
		  int temp = 0;
		  for(int i = 0; i < length;  i ++){         
			  while (true) {
				  temp = (int)(Math.random()*1000);
				  if ( temp>=48 && temp <= 57) break;
				  if ( temp>=65 && temp <= 90) break;
				  if ( temp>=97 && temp <= 122) break;
			  }
			  
			  array[i] = temp;
			  chars[i] = (char) array[i];
			  str.append(chars[i]);
		  }
		  
		  return str.toString();   
		 } 
	
	public static void main(String[] args) throws Exception {
		
	}
	public static String setDefaultStringValue(String value) throws Exception {
		if (isEmptyStr(value)) {
			return "";
		}
		return value;
	}
	public static void changeData() throws Exception {
		
		NamedSQL getemployeeidSql = NamedSQL.getInstance("getemployeeid");
		EntitySet employeeSet = SQLRunner.getEntitySet(getemployeeidSql);
		List<String> idList = new ArrayList<String>();
		List<String> parentIdList = new ArrayList<String>();
		//1所有一级岗位
		for (Entity entity : employeeSet) {
			String id = entity.getString("id");
			idList.add(id);
			String parentid = entity.getString("parentid");
			parentIdList.add(parentid);
		}
		
		for (String id : parentIdList) {
			if (idList.contains(id)) {
				idList.remove(id);
			}
		}
		
		//2格式新架构
		for (String id : idList) {
			Entity entity = new Entity("territory");
			entity.set("parentid1", id);
			String childId = id;
			field = 1;
			
			while (field <= entity.getFieldCount()) {
				field++;
				String childid = addparentid(entity, childId, "getparentidfromemployee");
				
				if (childid == null) {
					break;
				}
				childId = childid;
			}
			DataHandler.addLine(entity);
		}
		
	}

	protected static String addparentid(Entity entity, String id, String sqlname) throws Exception {
		String  parents = "parentid";
		if(sqlname.equalsIgnoreCase("getgrandparentidfromemployee")){
			parents = "grandpaid";
		}
		if(Util.isEmptyStr(id)) {
			return null;
		}
		NamedSQL getparentidfromemployee = NamedSQL.getInstance(sqlname);
		getparentidfromemployee.setParam("id", id);
		
		Entity parentidEntity = SQLRunner.getEntity(getparentidfromemployee);
		
		if((parentidEntity == null || Util.isEmptyStr(parentidEntity.getString(parents))) && sqlname.equalsIgnoreCase("getgrandparentidfromemployee")){
			return null;
		}
		
		if ((parentidEntity == null || Util.isEmptyStr(parentidEntity.getString(parents))) && sqlname.equalsIgnoreCase("getparentidfromemployee")) {
			field++;
			
			//String childid = addparentid(entity, id, "getgrandparentidfromemployee");
			//return childid;
	
			
		}
	
		String parentId = parentidEntity.getString(parents);
		entity.set("parentid" + field, parentId);
		return parentId;
	}
	
	
	public static String escapeQuoted(String filter) {
		if (filter == null) {
			return filter;
		}
		
		int length = filter.length();
		
		if (length <= 1) {
			return filter;
		}
		
		char first = filter.charAt(0);
		char last = filter.charAt(length - 1);
		
		if (('\'' == first || '"' == first) && (first == last)) {
			filter = filter.substring(1,length - 1);
			filter = filter.trim();
			return filter;
		}
		
		return filter;
	}

	public static String getFileExt(String filename) {
        int pos = filename.lastIndexOf(".");
        String ext = filename.substring(pos);  		
		return ext;
	}
	
	public static List<String> Regular(String regular, String text) {
		
		return null;
		
	}
	
	public static Boolean isNum(String textStr) {
		boolean isInt = Pattern.compile(Integer_Patter).matcher(textStr).find();
		boolean isDouble = Pattern.compile(Double_Patter).matcher(textStr).find();
		return (isInt || isDouble);
	}
	
	public static List<String> matcher(String testStr) {
//		String test = "@{databaseIp}:{databasePort}{instanceName};database";
//		String initCompile = "(?<=@\\{)(.+?)(?=\\})";
		
		List<String> ls=new ArrayList<String>();
		
		Pattern pattern = Pattern.compile(Default_Patter);
		Matcher matcher = pattern.matcher(testStr);
		
		while(matcher.find()){
			
			ls.add(matcher.group());
			
		}
		return ls;
	}
	
	public static List<String> matcher(String patter, String testStr) {
//		String test = "@{databaseIp}:{databasePort}{instanceName};database";
//		String initCompile = "(?<=@\\{)(.+?)(?=\\})";
		
		List<String> ls=new ArrayList<String>();
		
		Pattern pattern = Pattern.compile(patter);
		Matcher matcher = pattern.matcher(testStr);

		while(matcher.find()){

			ls.add(matcher.group());

		}
		return ls;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })  
    public static ArrayList Dikaerji0(ArrayList al0) {  
  
        ArrayList a0 = (ArrayList) al0.get(0);// l1  
        ArrayList result = new ArrayList();// 组合的结果  
        for (int i = 1; i < al0.size(); i++) {  
            ArrayList a1 = (ArrayList) al0.get(i);  
            ArrayList temp = new ArrayList();  
            // 每次先计算两个集合的笛卡尔积，然后用其结果再与下一个计算  
            for (int j = 0; j < a0.size(); j++) {  
                for (int k = 0; k < a1.size(); k++) {  
                    ArrayList cut = new ArrayList();  
  
                    if (a0.get(j) instanceof ArrayList) {  
                        cut.addAll((ArrayList) a0.get(j));  
                    } else {  
                        cut.add(a0.get(j));  
                    }  
                    if (a1.get(k) instanceof ArrayList) {  
                        cut.addAll((ArrayList) a1.get(k));  
                    } else {  
                        cut.add(a1.get(k));  
                    }  
                    temp.add(cut);  
                }  
            }  
            a0 = temp;  
            if (i == al0.size() - 1) {  
                result = temp;  
            }  
        }  
        return result;  
    } 
	
	public static void combine(int index,int k,String []arr) {
		 if(k == 1){
	            for (int i = index; i < arr.length; i++) {
	                tmpArr.add(arr[i]);
	                System.out.print(tmpArr.toString() + ",");
	                tmpArr.remove((Object)arr[i]);
	            }
	        }else if(k > 1){
	            for (int i = index; i <= arr.length - k; i++) {
	                tmpArr.add(arr[i]); //tmpArr都是临时性存储一下
	                combine(i + 1,k - 1, arr); //索引右移，内部循环，自然排除已经选择的元素
	                tmpArr.remove((Object)arr[i]); //tmpArr因为是临时存储的，上一个组合找出后就该释放空间，存储下一个元素继续拼接组合了
	            }
	        }else{
	            return ;
	        }
    }
	
	public static String List2String(List<? extends Object> list, String linked) {
		if (null == list) {
			return null;
		}
		if (Util.isEmptyStr(linked)) {
			linked = ",";
		}
		ContentBuilder builder = new ContentBuilder(linked);
		builder.append(list.toArray());
		
		return builder.toString();
	}
	
	public static String Join2ReplacedTemplate (String param) {
		if (Util.isEmptyStr(param)) {
			return null;
		}
		return "@{" + param + "}";
	}

	public static BigDecimal parseBigDecimal(Object value) {
		BigDecimal ret = null;
		if (value != null) {
			if (value instanceof BigDecimal) {
				ret = (BigDecimal) value;
			} else if (value instanceof String) {
				ret = new BigDecimal((String) value);
			}else if (value instanceof BigInteger) {
				ret = new BigDecimal((BigInteger) value);
			} else if (value instanceof Number) {
				ret = new BigDecimal(((Number) value).doubleValue());
			} else {
				throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass()+" into a BigDecimal.");
			}
		
		}
		return ret;
	}

	public static Object getDefaultValue(DataType type) {
	    Object object = null;

        if (DataType.String.equals(type)) {
            object = "";
        } else if (DataType.Integer.equals(type)) {
            object = 0;
        }else if (DataType.Double.equals(type)) {
            object = 0D;
        }else if (DataType.Float.equals(type)) {
            object = 0F;
        }else if (DataType.Long.equals(type)) {
            object = 0L;
        }else if (DataType.Boolean.equals(type)) {
            object = false;
        }else if (DataType.ArrayList.equals(type)) {
            object = new ArrayList<>();
        }else if (DataType.LinkedList.equals(type)) {
            object = new LinkedList<>();
        }
        //TODO 未完成
	    return  object;
    }
}

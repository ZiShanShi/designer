package foundation.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;


import foundation.persist.DataBaseType;
import foundation.server.Sysparam;
import foundation.util.Util;

public class Configer {

	private static Map<String, String> params;
	private static List<Sysparam> clientSysparams;
	public static boolean hasPeriod = false;
	public static String DataBase_Schema = null;
	private static String Path_Application;
	private static String Path_WebInfo;
	private static String Path_Config;
	private static String Path_SQL;
	private static DataBaseType dataBaseType;
	private static String baseURI;
	private static String port;
	private static String projectName;

	public static void init(ServletContext servletContext) {
		params = new HashMap<String, String>();
		clientSysparams = new ArrayList<Sysparam>();

		// 应用路径(带盘符的路径)
		Path_Application = servletContext.getRealPath("").replace("\\", "/");

		// webinfo路径
		Path_WebInfo = servletContext.getRealPath("/WEB-INF").replace("\\", "/");

		// webinfo路径下config路径
		Path_Config = Path_WebInfo + "/config/";

		// webinfo路径下sql路径
		Path_SQL = Path_WebInfo + "/sql/";
	}

	public static void afterLoadParams() {
		String typeString = getParam("dataBaseType");
		baseURI = getParam("baseURI");
		port = getParam("port");
		projectName = getParam("projectName");
		
		dataBaseType = DataBaseType.valueOfString(typeString);
	}

	public static void addParam(String name, String value, boolean client) {
		params.put(name.toLowerCase(), value);
		hasPeriod = client;
		
		if (client) {
			clientSysparams.add(new Sysparam(name, value));
		}
	}

	public static String getParam(String name) {
		if (name == null) {
			return null;
		}

		return params.get(name.toLowerCase());
	}

	public static String getPath_WebInfo() {
		return Path_WebInfo;
	}

	public static String getPath_Config() {
		return Path_Config;
	}

	public static String getPath_TimerConfig() {
		return Path_Config + "timer.properties";
	}


	public static String getPath_Application() {
		return Path_Application;
	}

	public static String getPath_LoggerConfig() {
		return Path_Config + "log4j.properties";
	}

	public static String getPath_ActivePeriodConfig() {
		return Path_Config + "activeperiod.properties";
	}

	public static String getPath_SQLConfig() {
		return Path_SQL;
	}

	public static String getPath_SQLDTD() {
		return Path_Config + "sql.dtd";
	}

	public static String getPath_MainConfig() {
		return Path_Config + "config.xml";
	}

	public static String getPath_Datasource() {
		return Path_Config + "datasource.xml";
	}

	public static String getPath_Upload(String username) {
		return Path_Application + "/upload/" + username;
	}

	public static String getPath_Resposity(String username) {
		return Path_Application + "/resposity/" + username;
	}

	public static String getPath_Temp() {
		return Path_Application + "/temp";
	}

	public static String getPath_ExcelTemplate(String dataname, String suffix) {
		return Path_WebInfo + "/template/" + dataname + "." + suffix;
	}

	public static File getFileSavePath(String fileName) {
		
		String path = params.get("filesavepath");
		
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		return new File(file, fileName);
	}
	
	public static boolean hasPeriod() {
		return hasPeriod;
	}

	public static String getPath_WXConfig() {
		return Path_Config + "weixin.properties";
	}

	public static List<Sysparam> getClientSysparams() {
		return clientSysparams;
	}

	// ***************Page******************//
	public static String getPage_TimeOut() {
		String appName = getParam("appName");
		String timeOutPage = getParam("timeOutPage");

		return "/" + appName + "/" + timeOutPage;
	}

	public static DataBaseType getDataBaseType() {
		return dataBaseType;
	}

	public static String getPath_ImageLib(String typeCode) {
		return Path_Application + "/imagelib/" + typeCode + "/" + Util.newDateTimeStr("yyyyMM") + "/";
	}

	public static boolean isMultiplyDatasoure() {
		String lowerCase = "multipleDataSource".toLowerCase();
		String multi = params.get(lowerCase);
		return Util.stringToBoolean(multi);
	}

	public static String getWebserviceURI() {
		String WebserviceURIString = params.get("webservicebaseuri");
		
		return "http://" +baseURI + ":" + port + "/" + projectName + WebserviceURIString;
	}
	
	public static boolean isOpenWebServiceHeader() {
		String doWebServiceHeader = params.get("dowebserviceheader");
		return Util.stringToBoolean(doWebServiceHeader, false);
	}

}

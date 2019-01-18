package foundation.persist;

import com.alibaba.druid.pool.DruidPooledConnection;
import foundation.data.MapList;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class SqlSession {

	private static Logger logger;
	private static MapList<NamedDataSource> dataSourceList;
	private static NamedDataSource defaultDataSource;
	private static NamedDataSource activeDataSource;
	static {
		logger = Logger.getLogger(SqlSession.class);
		dataSourceList = new MapList<NamedDataSource>();
	}

	public static Connection createConnection() {
		try {
			if (activeDataSource != null) {
				return activeDataSource.getConnection();
			}
			
			DruidPooledConnection connection = defaultDataSource.getConnection();
			return connection;
		} 
		catch (SQLException e) {
			logger.error(e);
			return null;
		}
	}
	

	public static void appendDataSource(NamedDataSource dataSource) throws SQLException {
		String name = dataSource.getName();
		
		if (name == null) {
			return;
		}
		
		dataSourceList.add(name, dataSource);
        
        if (defaultDataSource == null) {
        	defaultDataSource = dataSource;
        }
	}

	public static NamedDataSource getDataSource(String code) {
		if (code == null) {
			return null;
		}
		
		return dataSourceList.get(code);
	}

	public static List<NamedDataSource> getDataSourceList() {
		return dataSourceList.getItemList();
	}

	public static void setActiveSource(NamedDataSource dataSource) {
		activeDataSource = dataSource;
	}

	public static NamedDataSource getDefaultDataSource() {
		return defaultDataSource;
	}
}

package foundation.persist;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import foundation.config.ConfigFileLoader;
import foundation.config.Configer;
import foundation.util.DTDEntityResolver;
import foundation.util.Util;

public class DatasuorceLoader extends ConfigFileLoader {

	private static final String Node_Defination = "defination";
	private static final String Node_Defination_DriverName = "driverName";
	private static final String Node_Defination_URL = "url";
	private static final String Node_Defination_UserName = "username";
	private static final String Node_Defination_Password = "password";

	public DatasuorceLoader() {
		path = Configer.getPath_Datasource();
	}

	@Override
	public void load() {
		File file = new File(path);
		loadOneFile(file);
	}

	private void loadOneFile(File file) {
		try {
			logger.debug("load datasource file:" + file);
			InputStream inputStream = new FileInputStream(file);

			try {
				SAXReader reader = new SAXReader();
				reader.setValidation(false);
				
				reader.setEntityResolver(new IgnoreDTDEntityResolver());
				
				Document doc = reader.read(inputStream);
				Element root = doc.getRootElement();

				loadDefinations(root);

			} catch (DocumentException e) {
				e.printStackTrace();
				logger.error("can not load sql file: " + file);
				logger.error(e);
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void loadDefinations(Element root) throws SQLException {
		Iterator<?> iterator = root.elementIterator(Node_Defination);

		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();

			String code = element.attributeValue("code");
			if (Util.isEmptyStr(code)) {
				code = "main";
			}

			boolean active = Util.stringToBoolean(
					element.attributeValue("active"), true);

			if (!active) {
				continue;
			}

			String drivername = element
					.attributeValue(Node_Defination_DriverName);
			String url = element.attributeValue(Node_Defination_URL);
			String username = element.attributeValue(Node_Defination_UserName);
			String password = element.attributeValue(Node_Defination_Password);
			String validationQuery = element.attributeValue("validationQuery");

			NamedDataSource dataSource = new NamedDataSource(code);
			Configer.DataBase_Schema = username.toUpperCase();

			dataSource.setDriverClassName(drivername);
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			dataSource.setValidationQuery(validationQuery);

			dataSource.setInitialSize(1);
			dataSource.setFilters("stat,log4j");
			dataSource.setMaxWait(60000);
			dataSource.setMinIdle(1);
			dataSource.setTimeBetweenEvictionRunsMillis(3000);
			dataSource.setMinEvictableIdleTimeMillis(3000 * 500);
			dataSource.setRemoveAbandonedTimeout(300);
			dataSource.setTestWhileIdle(true);
			dataSource.setTestOnBorrow(false);
			dataSource.setTestOnReturn(false);

			SqlSession.appendDataSource(dataSource);
		}
	}

	@Override
	protected EntityResolver getEntityResolver() throws FileNotFoundException {
		return new DTDEntityResolver(Configer.getPath_Config(),
				"datasource.dtd");
	}

	public class IgnoreDTDEntityResolver implements EntityResolver {

		@Override
		public InputSource resolveEntity(String publicId, String systemId)
				throws SAXException, IOException {
			return new InputSource(new ByteArrayInputStream(
					"<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
		}

	}

}

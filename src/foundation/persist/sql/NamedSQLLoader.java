package foundation.persist.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;

import foundation.config.ConfigFileLoader;
import foundation.config.Configer;
import foundation.util.DTDEntityResolver;
import foundation.util.Util;


public class NamedSQLLoader extends ConfigFileLoader {

	protected String Node_RootElements;
	
	private NamedSQLContainer sqlContainer;
	
	public NamedSQLLoader() throws FileNotFoundException {
		path = Configer.getPath_SQLConfig();
		sqlContainer = NamedSQLContainer.getInstance();
		Node_RootElements = "dataSpace";
	}
	
	@Override
	public void load() {
		File root = new File(path);
		File[] files = root.listFiles();
		
		for (File config: files) {
			if (!config.isFile()) {
				continue;
			}
			
			loadOneFile(config);
		}
	}
	
	public void loadOneFile(File file) {
		try {
			logger.debug("load sql file:" + file);
			InputStream inputStream = new FileInputStream(file);
			
	        try {
	    		SAXReader reader = new SAXReader();
	    		EntityResolver entityResolver = getEntityResolver();
	    		reader.setEntityResolver(entityResolver);
	    			
				Document doc = reader.read(inputStream);
				Element root = doc.getRootElement();
				
				String active = root.attributeValue("active");
				
				if (!Util.isEmptyStr(active)) {
					if (!Util.stringToBoolean(active)) {
						return;
					}
				}
					
				loadRootElements(root);
					
			} catch (DocumentException e) {
				logger.error("can not load sql file: " + file);
				logger.error(e);
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}
		catch (Exception e) {
			logger.error(e);
		}
	}
	
	protected void loadRootElements(Element root) throws Exception {
		Iterator<?> iterator = root.elementIterator(Node_RootElements);
		
		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();	
			loadOneRootElement(element);
		}
	}

	protected void loadOneRootElement(Element element, Object ...args) throws Exception {
		Iterator<?> sqlIterator = element.elementIterator();
		
		while (sqlIterator.hasNext()) {
			Element statementElemnet = (Element) sqlIterator.next();
			loadOneSQLElement(statementElemnet, args);
		}
	}

	protected void loadOneSQLElement(Element element, Object ...args) throws Exception {
		String name = element.attributeValue("name");	
		
		Iterator<?> iterator = element.elementIterator("condition");
		boolean loaded = false;
		
		while (iterator.hasNext()) {
			loaded = true;
			
			Element conditionElemnet = (Element) iterator.next();
			String condition = conditionElemnet.attributeValue("value");
			String sql = conditionElemnet.getTextTrim();
			
			NamedSQL namedSQL = new NamedSQL(name, sql);
			sqlContainer.append(namedSQL, condition);
		}
		
		if (!loaded) {
			String sql = element.getTextTrim();
			NamedSQL namedSQL = new NamedSQL(name, sql);
			sqlContainer.append(namedSQL, null);
		}
	}

	@Override
	protected EntityResolver getEntityResolver() throws FileNotFoundException {
		return new DTDEntityResolver(Configer.getPath_Config(), "sql.dtd");
	}

}

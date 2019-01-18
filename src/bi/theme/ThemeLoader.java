package bi.theme;

import foundation.config.Configer;
import foundation.persist.sql.NamedSQLLoader;
import foundation.util.DTDEntityResolver;
import org.dom4j.Element;
import org.xml.sax.EntityResolver;

import java.io.FileNotFoundException;


public class ThemeLoader extends NamedSQLLoader {

	protected static final String Node_DataSpace = "dataSpace";	
	protected static final String Node_DataSpace_Name = "name";
	protected static final String Node_SQL_Name = "name";
	private ThemeContainer themeContainer;
	
	public ThemeLoader() throws FileNotFoundException {
		path = Configer.getPath_WebInfo() + "/theme";
		themeContainer = ThemeContainer.getInstance();
		Node_RootElements = "dataSpace";
	}
	
	@Override
	protected void loadOneSQLElement(Element element, Object... args) throws Exception {
		String name = element.attributeValue(Node_SQL_Name);	
		String sql = element.getTextTrim();

		Theme theme = new Theme(name, sql);
		themeContainer.append(theme);		
	}
	
	public void refresh(){
		themeContainer.clear();
		load();
	}

	@Override
	protected EntityResolver getEntityResolver() throws FileNotFoundException {
		return new DTDEntityResolver(Configer.getPath_Config(), "aggregate.dtd");
	}

}

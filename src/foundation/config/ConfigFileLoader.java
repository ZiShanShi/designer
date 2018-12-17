package foundation.config;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.xml.sax.EntityResolver;


public abstract class ConfigFileLoader extends Preloader {

	protected String path;
	
	static {
		logger = Logger.getLogger(ConfigFileLoader.class);		
	}
	
	public ConfigFileLoader() {
		
	}

	public abstract void load() throws Exception;
	
	protected abstract EntityResolver getEntityResolver() throws FileNotFoundException;

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return "[" + name + "]: " + path;
	}
	
}

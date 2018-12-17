package foundation.persist.sql;

import org.apache.log4j.Logger;

public abstract class SQLSegment {

	protected static Logger logger;
	
	static {
		logger = Logger.getLogger(SQLSegment.class);
	}
	
	abstract public String getValueString();

	abstract public SQLSegment newInstance();
	
}

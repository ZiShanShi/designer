package foundation.variant;

import org.apache.log4j.Logger;


public abstract class Segment {

	protected static Logger logger;
	
	static {
		logger = Logger.getLogger(Segment.class);
	}
	
	abstract public String getValueString();
	
	abstract public Segment newInstance();
	
}

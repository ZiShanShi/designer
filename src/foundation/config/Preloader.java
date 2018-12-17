package foundation.config;

import org.apache.log4j.Logger;


public abstract class Preloader implements IPreloader {

	protected static Logger logger;
	protected String name;
	protected boolean active;
	
	static {
		logger = Logger.getLogger(Preloader.class);		
	}
	
	public Preloader() {
		
	}

	public abstract void load() throws Exception;
	
	public String getName() {
		return name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}

package foundation.config;

public interface IPreloader {

	void setActive(boolean active);
	
	void setName(String name);
	
	boolean isActive();

	String getName();
	
	void load() throws Exception;
	
}

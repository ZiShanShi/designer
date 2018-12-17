package foundation.server;

public class Sysparam {

	private String name;
	private String value;
	
	public Sysparam(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
	
}

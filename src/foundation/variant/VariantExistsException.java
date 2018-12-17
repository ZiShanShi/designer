package foundation.variant;

public class VariantExistsException extends Exception {

	private static final long serialVersionUID = 6370169285699904127L;
	
	public VariantExistsException(String name) {
		super("variant has exists: " + name);
	}

}

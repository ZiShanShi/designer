package foundation.variant;

public class VariantNotFoundException extends Exception {

	private static final long serialVersionUID = 5270739430116585535L;
	
	public VariantNotFoundException(String name) {
		super("param not found : " + name);
	}
}

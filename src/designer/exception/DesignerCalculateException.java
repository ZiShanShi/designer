package designer.exception;


public class DesignerCalculateException extends DesignerBaseException {

	private static final long serialVersionUID = 1L;
	
	private static String Field_Error_Msg = "element not map real value";
	
	public DesignerCalculateException(String code , String message) {
		super(code, "Calculate error--" + message);
	}
	
	public DesignerCalculateException(String message) {
		super("-1", message);
	}
	

	public DesignerCalculateException(int type) {
		this(null);
		if (type == 1) {
			message = Field_Error_Msg;
		}
	}
	
	public DesignerCalculateException(int type, String msg) {
		this(null);
		if (type == 1) {
			message = Field_Error_Msg + "--tablename :" + msg;
		}
		
	}
	
	
}

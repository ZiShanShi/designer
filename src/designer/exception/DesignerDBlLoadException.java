package designer.exception;


public class DesignerDBlLoadException extends DesignerBaseException {

	private static final long serialVersionUID = 1L;
	
	private static String Entity_Error_Msg = "tablename is empty";
	private static String Field_Error_Msg ="field is null";
	private static String Sql_Error_Msg ="Sql is error";
	
	
	public DesignerDBlLoadException(String code , String message) {
		super(code, "Entity load error--" + message);
	}
	
	public DesignerDBlLoadException(String message) {
		super("-3", message);
	}
	
	public DesignerDBlLoadException(int type) {
		this(null);
		if (type == 1) {
			message = Entity_Error_Msg;
		}
		else if(type == 2){
			message = Field_Error_Msg;
		}
		else if(type == 3){
			message = Sql_Error_Msg;
		}
	}
	
	public DesignerDBlLoadException(int type, String msg) {
		this(null);
		if (type == 1) {
			message = Entity_Error_Msg + "--tablename :" + msg;
		}
		else if(type == 2){
			message = Field_Error_Msg + "--field :" + msg;
		}
		else if(type == 3){
			message = Sql_Error_Msg + "--sql :" + msg;
		}
	}
	
	
}

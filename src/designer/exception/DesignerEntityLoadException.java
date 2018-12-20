package designer.exception;


public class DesignerEntityLoadException extends DesignerBaseException {

	private static final long serialVersionUID = 1L;
	
	private static String Entity_Error_Msg = "entity is null";
	private static String Field_Error_Msg ="field is null";
	
	private static String Reflect_Field_Error_Msg ="reflect field error";
	
	
	public DesignerEntityLoadException(String code , String message) {
		super(code, "Entity load error--" + message);
	}
	
	public DesignerEntityLoadException(String message) {
		super("-2", message);
	}
	

	public DesignerEntityLoadException(int type) {
		this(null);
		if (type == 1) {
			message = Entity_Error_Msg;
		}
		else if(type == 2){
			message = Field_Error_Msg;
		}
		else if(type == 3){
			message = Reflect_Field_Error_Msg;
		}
	}
	
	public DesignerEntityLoadException(int type, String field) {
		this(null);
		if (type == 1) {
			message = Entity_Error_Msg;
		}
		else if(type == 2){
			message = Field_Error_Msg + "--field :" + field;
		}
		else if(type == 3){
			message = Reflect_Field_Error_Msg + "--field :" + field;
		}
	}
	
	
}

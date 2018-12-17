package foundation.callable;

public interface IEnvelop {

	public final static String ResultCode_Success = "success";
	public final static String ResultCode_ErrorCode = "errorcode";
	public final static String ResultCode_ErrorMessage = "errormessage";
	public final static String ResultCode_Message = "message";
	
	public final static String ResultCode_DataLine = "line";
	public final static String ResultCode_DataSet = "Rows";
	public final static String ResultCode_TotalCount = "Total";
	public final static String ResultCode_BarCodeResult = "barcoderesult";	
	public final static String ResultCode_Tree = "tree";	
	public final static String ResultCode_Vtable = "vtable";
	public final static String ResultCode_Value = "value";
	
	public final static String ResultCode_Page = "page";
	public final static String ResultCode_Page_BeginRecordNo = "beginRecordNo";
	public final static String ResultCode_Page_Size = "size";
	public final static String ResultCode_Page_EndRecordNo = "endRecordNo";
	public final static String ResultCode_Page_RecordCount = "recordCount";
	public final static String ResultCode_Page_PageNo = "pageNo";
	public final static String ResultCode_Page_PageCount = "pageCount";	
	
	public final static String ResultCode_MessageList = "messageList";
	public final static String ResultCode_StepMessage = "stepMessage";
	public final static String ResultCode_CurrentPhase = "currentPhase";
	
	public final static String Error_Code_Timeout = "timeout";
	public final static String Error_Code_ServerError = "serverError";
 	public final static String Error_Messgae_Timeout = "session timeout";
 	public final static String Error_Messgae_ServerError = "server error";
}

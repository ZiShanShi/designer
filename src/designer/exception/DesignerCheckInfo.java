package designer.exception;

import foundation.util.Util;

public class DesignerCheckInfo {
	
	private boolean success;
	private String code;
	private String info;
	
	public DesignerCheckInfo() {
	}
	
	public DesignerCheckInfo(boolean success, String code, String info) {
		this.success = success;
		if (success && Util.isEmptyStr(code)) {
			code = "200";
		}
		this.info = info;
	}
	
	public DesignerCheckInfo(boolean success, String info) {
		this.success = success;
		this.info = info;
	}
	
	public void set(boolean success, String info) {
		this.success = success;
		this.info = info;
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}

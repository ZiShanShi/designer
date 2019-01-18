package bi.agg;

public class SQLParameter {

	private String name;
	private String category;
	private String code;
	
	
	public SQLParameter(String segment) {
		int begin = segment.indexOf("(");
		int end = segment.lastIndexOf(")");
		
		if (begin >= 0 && end > begin) {
			name = segment.substring(0, begin);
			
			String param = segment.substring(begin + 1, end);
			int pos = param.indexOf(",");
			
			if (pos > 0) {
				category = param.substring(0, pos);
				code = param.substring(pos + 1);
			}
			else {
				if ("flat".equalsIgnoreCase(param)) {
					category = param;
				}
				else {
					code = param;
				}
			}
		}
		else {
			name = segment;
		}
		
		if (name != null) {
			name = name.trim().toLowerCase();
		}
		if (category != null) {
			category = category.trim();
		}
		if (code != null) {
			code = code.trim();
		}
	}
	
	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public String getCode() {
		return code;
	}

}

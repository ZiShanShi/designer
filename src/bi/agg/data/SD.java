package bi.agg.data;

import bi.agg.SQLParameter;
import foundation.util.ContentBuilder;

public class SD extends DimensionCell {

	public SD() {
		super("sd");
	}
	
	@Override
	public void onGetFields(ContentBuilder builder, SQLParameter parameter) {
		String category = parameter.getCategory();
		String code = parameter.getCode();
		
		if ("flat".equals(category)) {
			if ("group".equals(code)) {
				builder.append("sdid");
			}
			else {
				builder.append("sdid");
				builder.append("'sd'");		
			}
		}
		else {
			builder.append("hierarchyid");
			builder.append("hierarchytype");
		}
	}

	@Override
	public void onGetFilter(ContentBuilder builder, SQLParameter parameter) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onGetMapping(ContentBuilder builder, SQLParameter parameter) {
		if ("3month".equalsIgnoreCase(parameter.getCategory())) {
			String code = parameter.getCode();
			builder.append(code + ".hierarchyid = dest.hierarchyid");
		}
		else {
			builder.append("sour.hierarchyid = dest.hierarchyid");
		}
	}
}

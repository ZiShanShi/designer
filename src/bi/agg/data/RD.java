package bi.agg.data;

import bi.agg.SQLParameter;
import foundation.util.ContentBuilder;

public class RD extends DimensionCell {

	public RD() {
		super("rd");
	}
	
	@Override
	public void onGetFields(ContentBuilder builder, SQLParameter parameter) {
		String category = parameter.getCategory();
		String code = parameter.getCode();
		
		if ("flat".equals(category)) {
			if ("group".equals(code)) {
				builder.append("rdid");
			}
			else {
				builder.append("rdid");
				builder.append("'rd'");	
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

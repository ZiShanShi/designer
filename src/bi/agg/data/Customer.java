package bi.agg.data;

import bi.agg.Defination;
import bi.agg.SQLParameter;
import foundation.util.ContentBuilder;


public class Customer extends Dimension {

	public Customer() {
		super("customer");
	}

	@Override
	public void onGetFields(ContentBuilder builder, SQLParameter parameter) {
		String category = parameter.getCategory();
		
		if ("flat".equalsIgnoreCase(category)) {
			builder.append("customerid");
		}
		else {
			builder.append("customerid");
		}
	}
	
	@Override
	public void onGetFilter(ContentBuilder builder, SQLParameter parameter) {
		
	}

	@Override
	public void onGetMapping(ContentBuilder builder, SQLParameter parameter) {
		if ("3month".equalsIgnoreCase(parameter.getCategory())) {
			String code = parameter.getCode();
			builder.append(code + ".customerid = dest.customerid");
		}
		else {
			builder.append("sour.customerid = dest.customerid");
		}		
	}

	@Override
	public void loadLoopValue(LoopValue loopValue, Defination defination) {
		
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public String getWorkingName() {
		return "customer";
	}
}

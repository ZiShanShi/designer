package bi.agg.data;

import bi.agg.Defination;
import bi.agg.SQLParameter;
import foundation.data.Entity;
import foundation.data.EntitySet;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLRunner;
import foundation.util.ContentBuilder;
import foundation.util.Util;

public class SKU extends DimensionCell {

	public SKU() {
		super("sku");
	}

	@Override
	public void onGetFields(ContentBuilder builder, SQLParameter parameter) {
		String category = parameter.getCategory();
		String code = parameter.getCode();
		
		if ("flat".equals(category)) {
			if ("group".equals(code)) {
				builder.append("productid");
			}
			else {
				builder.append("productid");
				builder.append("'sku'");
			}
		}
		else {
			builder.append("productid");
			builder.append("producttype");			
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
			builder.append(code + ".productid = dest.productid");
		}
		else {
			builder.append("sour.productid = dest.productid");
		}
	}

	@Override
	public void loadLoopValue(LoopValue loopValue, Defination defination) {
		if (defination.getDimensionSize() == 1) {
			ValueItem valueItem = new ValueItem();
			valueItem.add("producttype=" + Util.quotedStr("sku"));
			
			loopValue.add(valueItem);
		}
		else {
			try {
				NamedSQL namedSQL = NamedSQL.getInstance("getProductList");
				EntitySet entitySet = SQLRunner.getEntitySet(namedSQL);
				
				String id;
				for (Entity entity: entitySet) {
					id = entity.getString("id");
					
					ValueItem valueItem = new ValueItem();
					valueItem.add("productid=" + Util.quotedStr(id)); 
					valueItem.add("producttype=" + Util.quotedStr("sku"));
					
					loopValue.add(valueItem);
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}
	
}

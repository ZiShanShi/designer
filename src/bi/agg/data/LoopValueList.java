package bi.agg.data;

import java.util.List;

import bi.agg.Defination;
import bi.agg.SQLParameter;
import foundation.persist.sql.ISQLContext;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLVariant;
import foundation.util.ContentBuilder;
import foundation.util.Util;

public class LoopValueList extends LoopGroup<LoopValue> implements ISQLContext {
 
	private Defination defination;
	
	public LoopValueList(List<Dimension> dimensionList, Defination defination) throws Exception {
		this.defination = defination;
		
		for (Dimension dimension: dimensionList) {
			LoopValue loopValue = new LoopValue(dimension, defination);
			appendLoop(loopValue);
		}
	}

	public void setParametersTo(NamedSQL namedSQL, Object ...args) throws Exception {
		List<SQLVariant> variantList = namedSQL.getVariantList();
		
		for (SQLVariant variant: variantList) {
			if (!variant.isEmpty()) {
				continue;
			}
			
			String name = variant.getName();
			
			SQLParameter parameter = new SQLParameter(name);
			String value = getSqlString(parameter, args);
			
			variant.setValue(value);
		}		
	}

	public String getSqlString(SQLParameter parameter, Object ...args) {
		String name = parameter.getName();
		
		if (name.equals("filter")) {
			String string_1 = getFilterMapping();
			String string_2 = defination.getSqlString(parameter, args);
			
			if (!Util.isEmptyStr(string_1) && !Util.isEmptyStr(string_2)) {
				return string_1 + " and " + string_2;
			}
			else if (!Util.isEmptyStr(string_1)) {
				return string_1;
			}
			else if (!Util.isEmptyStr(string_2)){
				return string_2;
			}
			else {
				return null;
			}
		}
		else {
			return defination.getSqlString(parameter);
		}
	}

	private String getFilterMapping() {
		ContentBuilder builder = new ContentBuilder(" and ");
		
		for (LoopValue loopValue : loopList) {
			loopValue.onGetFilter(builder);
		}
		
		return builder.toString();
	}
	
	public String getWorkingName() {
		return (pos + 1) + "/" + size;
	}

}

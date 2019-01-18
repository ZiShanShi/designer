package bi.agg.data;

import java.util.HashMap;
import java.util.Map;

import bi.agg.Defination;
import bi.agg.SQLParameter;
import foundation.util.ContentBuilder;

public abstract class DimensionCell {

	private String name;
	private Map<String, String> variants;

	public DimensionCell(String name) {
		this.name = name;
		variants = new HashMap<String, String>();
	}
	
	public abstract void onGetFields(ContentBuilder builder, SQLParameter parameter);
	
	public abstract void onGetFilter(ContentBuilder builder, SQLParameter parameter);
	
	public abstract void onGetMapping(ContentBuilder builder, SQLParameter parameter);
	
	public String getName() {
		return name;
	}

	public void loadLoopValue(LoopValue loopValue, Defination defination) {}

	public String getVariant(String name) {
		if (name == null) {
			return null;
		}
		
		name = name.toLowerCase();
		return variants.get(name);
	}
	
	public void addVariant(String name, String value) {
		if (name == null) {
			return;
		}
		
		name = name.toLowerCase();
		variants.put(name, value);
	}

	public String getOnYearPeriod(SQLParameter parameter) {
		return null;
	}

	public String getPeriodFlag() {
		return null;
	}
	
}

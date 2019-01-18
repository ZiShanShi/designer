package bi.agg.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bi.agg.Defination;
import bi.agg.SQLParameter;
import bi.work.ActivePeriod;
import foundation.persist.sql.ISQLString;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLParameterSetter;
import foundation.util.ContentBuilder;

public class Dimension extends Loop<DimensionCell> implements ISQLString {

	private static Map<String, Class<? extends Dimension>> dimensionMap;
	protected String name;

	static {
		dimensionMap = new HashMap<String, Class<? extends Dimension>>();
		dimensionMap.put("hierarchy", Hierarchy.class);
		dimensionMap.put("customer", Customer.class);
		dimensionMap.put("product", Product.class);
		dimensionMap.put("channel", Channel.class);
	}

	public Dimension(String name) {
		this.name = name;
	}

	public static Dimension newInstance(String dimensionCode, String param) throws InstantiationException, IllegalAccessException {
		if (dimensionCode == null) {
			return null;
		}

		dimensionCode = dimensionCode.toLowerCase();
		Class<? extends Dimension> clazz = dimensionMap.get(dimensionCode);

		if (clazz == null) {
			return null;
		}

		Dimension dimension = clazz.newInstance();
		dimension.init(param);

		return dimension;
	}

	public Dimension newInstance() {
		Dimension instance = new Dimension(this.name);

		List<DimensionCell> items = this.getItems();
		instance.addAll(items);

		instance.first();

		return instance;
	}

	protected void init(String param) {
		
	}

	public String getName() {
		return name;
	}

	public void onGetFields(ContentBuilder builder, SQLParameter parameter) {
		DimensionCell cell = getCurrent();
		cell.onGetFields(builder, parameter);
	}

	public void onGetFilter(ContentBuilder builder, SQLParameter parameter) {
		DimensionCell cell = getCurrent();
		cell.onGetFilter(builder, parameter);
	}

	public void onGetMapping(ContentBuilder builder, SQLParameter parameter) {
		DimensionCell cell = getCurrent();
		cell.onGetMapping(builder, parameter);
	}

	public String getWorkingName() {
		DimensionCell cell = getCurrent();
		
		if (cell == null) {
			return name + "[null]";
		}
		
		return cell.getName();
	}

	public void loadLoopValue(LoopValue loopValue, Defination defination) {
		DimensionCell cell = getCurrent();
		cell.loadLoopValue(loopValue, defination);
	}

	public void setParametersTo(NamedSQL namedSQL) throws Exception {
		SQLParameterSetter setter = new SQLParameterSetter(this);
		setter.setTo(namedSQL);
	}

	public String getSqlString(String name) {
		if (name == null) {
			return name;
		}

		SQLParameter parameter = new SQLParameter(name);
		return getSqlString(parameter);
	}

	public String getSqlString(SQLParameter parameter) {
		String name = parameter.getName();

		if ("dimensionField".equalsIgnoreCase(name)) {
			DimensionCell cell = getCurrent();
			return cell.getName();
		}
		else if ("onYearPeriod".equalsIgnoreCase(name)) {
			DimensionCell cell = getCurrent();
			return cell.getOnYearPeriod(parameter);
		}
		else if ("year".equalsIgnoreCase(name)) {
			return String.valueOf(ActivePeriod.getInstance().getYear());
		}
		else if ("periodflag".equals(name)) {
			DimensionCell cell = getCurrent();
			return cell.getPeriodFlag();
		}
		else {
			DimensionCell cell = getCurrent();
			return cell.getVariant(name);
		}
	}

	@Override
	public String toString() {
		DimensionCell cell = getCurrent();
		
		if (cell == null) {
			return null;
		}
		
		return cell.toString();
	}
}

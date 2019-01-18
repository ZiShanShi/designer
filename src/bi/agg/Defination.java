package bi.agg;

import java.util.List;

import bi.agg.data.Dimension;
import bi.agg.data.LoopGroup;
import bi.agg.data.LoopValueList;
import bi.agg.data.Measurement;
import bi.agg.data.Period;
import bi.agg.data.PeriodType;
import bi.agg.ranking.RankingField;
import bi.agg.ranking.RankingFields;
import bi.work.ActivePeriod;
import foundation.data.Entity;
import foundation.persist.sql.ISQLContext;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLParser;
import foundation.persist.sql.SQLVariant;
import foundation.util.ContentBuilder;
import foundation.util.Util;

public class Defination extends LoopGroup<Dimension> implements ISQLContext {

	protected String id;
	protected String name;
	protected String aggtable;
	protected String rankingtable;
	protected String flattable;
	protected String aggObj;
	protected String aggType;
	protected LoopValueList valueList;
	protected Measurement measurement;
	protected RankingFields rankingFields;
	protected String pricelistVersion;
	protected boolean filterByTarget;
	protected boolean active;
	protected PeriodDivision periodDivision;

	
	public Defination() {
		rankingFields = new RankingFields();
	}
	
	public String getWorkingName() {
		ContentBuilder builder = new ContentBuilder();
		
		for (Dimension dimension : loopList) {
			builder.append(dimension.getWorkingName(), ", ");
		}
		
		return builder.toString();
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
		if (parameter == null) {
			return null;
		}
		
		String result = doGetSqlString(parameter, args);
		
		if (result == null) {
			return result;
		}
		
		if (result.indexOf("@") >= 0) {
			String variantName = SQLParser.getVariantName(result);
			
			if (Util.isEmptyStr(variantName)) {
				return result;
			}
			
			parameter = new SQLParameter(variantName);
			String variant = doGetSqlString(parameter, args);
			result = result.replace("@{" + variantName + "}", variant);
		}
		
		return result;
	}
	
	private String doGetSqlString(SQLParameter parameter, Object ...args) {
		PeriodType periodtype = null; Period period = null;
		
		if (args != null && args.length > 0) {
			ParamSetterList setterList = (ParamSetterList) args[0];
			periodtype = setterList.getPeriodType();
			period = setterList.getPeriod();
		}
		
		String name = parameter.getName();
												
		if ("aggtable".equalsIgnoreCase(name)) {
			return aggtable;
		}
		else if ("rankingtable".equalsIgnoreCase(name)) {
			return rankingtable;
		}
		else if ("flattable".equalsIgnoreCase(name)) {
			return flattable;
		}
		else if ("aggtable_month".equalsIgnoreCase(name)) {
			String segment = PeriodDivision.Whole == periodDivision ? "" : "m_";
			String result = aggtable.replace("@{periodflag}", segment);
			return result;
		}		
		else if ("typecode".equals(name)) {
			return aggObj;
		}
		else if ("pricelistVersion".equalsIgnoreCase(name)) {
			return pricelistVersion;
		}
		else if ("year".equalsIgnoreCase(name)) {
			return String.valueOf(ActivePeriod.getInstance().getYear());
		}		
		else if ("month".equals(name)) {
			return String.valueOf(ActivePeriod.getInstance().getMonth());
		}
		else if ("periodflag".equals(name)) {
			if (PeriodDivision.Whole == periodDivision) {
				return "";
			}
			
			return periodtype.getSqlString("periodflag");
		}
		
		ContentBuilder builder = null;
		
		if ("fields".equals(name)) {
			builder = new ContentBuilder(", ");
			
			if (periodtype != null) {
				periodtype.onGetFields(builder, parameter, period);
			}
			
			if (PeriodDivision.Whole == periodDivision) {
				if ("insert".equalsIgnoreCase(parameter.getCode())) {
					builder.append("periodtype");
				}
				else if("group".equalsIgnoreCase(parameter.getCode())) {
				}
				else {
					if (periodtype != null) {
						builder.append(Util.quotedStr(periodtype.getCurrentName()));
					}
					else {
						builder.append("null");
					}
				}
			}				
			
			for (Dimension dimension : loopList) {
				dimension.onGetFields(builder, parameter);
			}
		}
		else if ("values".equals(name) || "measurements".equals(name)) {
			builder = new ContentBuilder(", ");

			periodtype.onGetFields(builder, parameter, period);
			
			for (String value : measurement) {
				onGetValues(builder, value, parameter);
			}
		}		
		else if ("filter".equals(name)) {
			builder = new ContentBuilder(" and ");

			if ("periodtype".equalsIgnoreCase(parameter.getCategory())) {
				if (PeriodDivision.Whole == periodDivision) {
					builder.append("periodtype = '" + parameter.getCode() + "'");
				}
			}
			
			periodtype.onGetFilter(builder, parameter, period);
			
			for (Dimension dimension : loopList) {
				dimension.onGetFilter(builder, parameter);
			}
			
			if (filterByTarget && "flat".equals(parameter.getCategory())) {
				builder.append("istarget='T'");
			}
			
			if (builder.isEmpty()) {
				builder.append("1=1");
			}
		}
		else if ("mapping".equals(name)) {
			builder = new ContentBuilder(" and ");
			
			if (PeriodDivision.Whole == periodDivision) {
				if ("3month".equalsIgnoreCase(parameter.getCategory())) {
					String code = parameter.getCode();
					builder.append(code + ".periodtype = dest.periodtype");
				}
				else {
					builder.append("sour.periodtype = dest.periodtype");
				}
			}
			
			for (Dimension dimension : loopList) {
				dimension.onGetMapping(builder, parameter);
			}
			
			if (builder.isEmpty()) {
				return "1=1";
			}
		}
		
		if (builder == null) {
			return null;
		}
		
		return builder.toString();		
	}

	private void onGetValues(ContentBuilder builder, String value, SQLParameter parameter) {
		String code = parameter.getCode();
		
		if ("sum".equals(code)) {
			builder.append("sum(" + value + ")", ", ");
		}
		else {
			builder.append(value);
		}
	}
	
	public void appendDimension(Dimension dimension) {
		super.appendLoop(dimension);
	}

	public Dimension getDimension(String name) {
		if (name == null) {
			return null;
		}
		
		for (Dimension dimension: loopList) {
			if (name.equalsIgnoreCase(dimension.getName())) {
				return dimension.newInstance();
			}
		}
		
		return null;
	}
	
	public LoopValueList getValueList() throws Exception {
		valueList = new LoopValueList(loopList, this);
		return valueList;
	}
	
	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAggObj() {
		return aggObj;
	}

	@Override
	public String toString() {
		return name + "(" + getWorkingName() + ")";
	}

	public void load(Entity entity) {
		id = entity.getString("id");
		name = entity.getString("name");
		aggtable = entity.getString("aggtable");
		rankingtable = entity.getString("rankingtable");
		flattable = entity.getString("flattable");
		aggObj = entity.getString("aggobj");
		aggType = entity.getString("aggType");
		pricelistVersion = entity.getString("pricelist");
		filterByTarget = entity.getBoolean("filterByTarget");
		periodDivision = PeriodDivision.valueOfString(entity.getString("periodDivision")); 
		active = entity.getBoolean("active");
	}

	public String getAggType() {
		return aggType;
	}

	public void appendRankingField(RankingField rankingField) {
		rankingFields.add(rankingField);
	}
	
	public RankingFields getRankingFields() {
		return rankingFields;
	}

	public PeriodDivision getPeriodDivision() {
		return periodDivision;
	}

	public boolean isActive() {
		return active;
	}

}

package bi.define;


import bi.AggConstant;
import bi.exception.AggCalculateException;
import bi.work.ActivePeriod;
import foundation.persist.sql.SQLContext;
import foundation.util.ContentBuilder;
import foundation.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.*;

public class AggContext extends SQLContext {

	private  static final String initSeparator = "-";
	private static final String initCompile = "(?<=@\\{)(.+?)(?=\\})";
	private static final String leftJoin = "left join ";
	private static final String On = " on ";
	private static final String Dot = ".";
	
	private String targetTable;
	private String targetTableAs;
	
	private String sourceTable; 
	private String sourceTableAs;
	
	private DimensionSpace dimensionSpace;
	private String nameTemplate;
	private List<Measurment> aggList;
	private List<Measurment> mesurmentList;
	
	private AggTheme aggTheme;
	private String themeTableName;
	
	private Map<String, AggTable> tableMap = new HashMap<String, AggTable>();
	private Map<String, String> jointFieldsMap;
	private Logger logger;
	
	private List<String> dimensionAggFieldNameList;
	private Measurment aggedMeasurment;
	private Dimension rankDimension;
	
	
	public AggContext(AggTheme aggTheme, DimensionSpace dimensionSpace) {
		jointFieldsMap = new HashMap<String, String>();
		logger = LoggerFactory.getLogger(this.getClass());
		this.aggTheme = aggTheme;
		this.dimensionSpace = dimensionSpace;
		this.aggList = aggTheme.getAggList();
		this.mesurmentList = aggTheme.getMeasurmentList();
		this.nameTemplate = aggTheme.getNameTemplate();
		
		conventTableName(aggTheme.getTargetTable(), "target");
		conventTableName(aggTheme.getSourceTable(), AggConstant.Agg_Source);
		initfields();
	}
	
	public AggContext(AggTheme aggTheme, List<String> dimensionGroupNameList) {
		jointFieldsMap = new HashMap<String, String>();
		logger = LoggerFactory.getLogger(this.getClass());
		this.aggTheme = aggTheme;
		this.dimensionAggFieldNameList = dimensionGroupNameList;
		this.aggList = aggTheme.getAggList();
		this.mesurmentList = aggTheme.getMeasurmentList();
		this.nameTemplate = aggTheme.getNameTemplate();
		
		conventTableName(aggTheme.getTargetTable(), "target");
		conventTableName(aggTheme.getSourceTable(), AggConstant.Agg_Source);
		initfields();
	}

	public AggContext(AggTheme aggTheme, DimensionSpace dimensionSpace, List<String> dimensionGroupNameList) {
		this(aggTheme, dimensionSpace);
		this.dimensionAggFieldNameList = dimensionGroupNameList;
	}

	private void conventTableName(String tableString, String type) {
		String realTableName = null;
		String realTableAs = null;
		List<String> preChangedFields = Util.matcher(initCompile, tableString);
		for (String fields : preChangedFields) {
			try {
				String sqlString = getSqlString(fields);
				tableString = tableString.replaceAll("@\\{" + fields + "\\}", sqlString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		if(tableString.toLowerCase().indexOf(AggConstant.BI_Default_As) != -1) {
			realTableName = tableString.split(AggConstant.BI_Default_As)[0].trim();
			realTableAs =  tableString.split(AggConstant.BI_Default_As)[1].trim();
		}
		else {
			realTableName = tableString;
			realTableAs = tableString;
		}
		
		if (AggConstant.Agg_Target.equalsIgnoreCase(type)) {
			this.targetTable = tableString;
			this.targetTableAs = realTableAs;
		}
		else if (AggConstant.Agg_Source.equalsIgnoreCase(type)) {
			this.sourceTable = tableString;
			this.sourceTableAs = realTableAs;
		}
		
		AggTable aggTable = AggTableContainer.getInstance().getAggTable(realTableName);
		
		tableMap.put(type, aggTable);
	}

	private void initfields() {
		if (dimensionSpace != null) {
			initDimensionFields();
		}
		
		initMeasurmentFields();
		
		initAggFields();
		
		initThemeName();
	}

	private void initThemeName() {
		ContentBuilder contentBuilder = new ContentBuilder("-");
		
		List<String> matchedList = Util.matcher(initCompile, nameTemplate);
		for (String matcher : matchedList) {
			AggDirection parse = AggDirection.parse(matcher);
			String jointField;
			if (AggDirection.Dimension.equals(parse)) {
				if (dimensionSpace == null) {
					continue;
				}
				for (Dimension dimension: dimensionSpace) {
					contentBuilder.append(dimension.getCode());
				}
			}
			else {
				jointField = getJointField(getAggTable(AggConstant.Agg_Source), parse);
				contentBuilder.append(jointField);
			}
			
		}
		
		this.themeTableName = contentBuilder.toString();
	}

	private void initAggFields() {
		
		for (AggTable oneTable : tableMap.values()) {
			ContentBuilder builder = new ContentBuilder(initSeparator);
			//1.3 agg
			for (Measurment aggMeasurment : aggList) {
				String fieldName = oneTable.getAggFieldName(AggDirection.Agg, aggMeasurment.getCode());
				builder.append(fieldName);
			}
			
			jointFieldsMap.put(oneTable.getName() + "-" + AggDirection.Agg, builder.toString());
		}
	}

	private void initMeasurmentFields() {
		for (AggTable oneTable : tableMap.values()) {
			ContentBuilder builder = new ContentBuilder(initSeparator);
			//1.2 Measurment
			for (Measurment measurment : mesurmentList) {
				String fieldName  = oneTable.getAggFieldName(AggDirection.Measurment, measurment.getCode());
				builder.append(fieldName);
			}
			
			jointFieldsMap.put(oneTable.getName() + "-" + AggDirection.Measurment, builder.toString());
		}
	}

	/**
	 * 
	 */
	private void initDimensionFields() {
		
		for (AggTable oneTable : tableMap.values()) {
			ContentBuilder builder = new ContentBuilder(initSeparator);
			ArrayList<String> fieldList = new ArrayList<>();
			//1.1 dimension
			for (Dimension dimension: dimensionSpace) {
				String fieldName = oneTable.getAggFieldName(AggDirection.Dimension, dimension.getCode());
				fieldList.add(fieldName);
			}

            if (fieldList.size() == 0) {
			    throw new AggCalculateException(MessageFormat.format("{0} 的field还是空", oneTable.getName()));
            }

			Collections.sort(fieldList, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
				    int index = 0;
				    if (Util.isEmptyStr(o1) || Util.isEmptyStr(o2)) {
                        throw new AggCalculateException(MessageFormat.format("{0} 的field还是空", oneTable.getName()));
                    }
                    char[] o1chars = o1.toCharArray();
                    char[] o2chars = o2.toCharArray();
                    int min = Math.min(o1chars.length, o2chars.length);
                    while (index < min) {
                        char o1sub = o1chars[index];
                        char o2sub = o2chars[index];
                        Integer o1ASCII = Integer.valueOf(o1sub);
                        Integer o2ASCII = Integer.valueOf(o2sub);
                        if (o1ASCII.equals(o2ASCII)) {
                            index++;
                            continue;
                        } else if (o1ASCII > o2ASCII) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                    if (o1.length() < o2.length()) {
                        return 1;
                    } else {
                        return -1;
                    }
                    
				}
			});

			for (String oneField : fieldList) {
				builder.append(oneField);
			}
			
			jointFieldsMap.put(oneTable.getName() + "-" + AggDirection.Dimension, builder.toString());
		}
	}
	
	@Override
	public String getSqlString(String name, Object... args) throws Exception {
		if ("targetTable".equalsIgnoreCase(name)) {
			return this.targetTable;
		}
		else if ("sourceTable".equalsIgnoreCase(name)) {
			return this.sourceTable;
		}
		else if ("sourceTableAs".equalsIgnoreCase(name)) {
			return this.sourceTableAs;
		}
		else if ("targetTableAs".equalsIgnoreCase(name)) {
			return this.targetTableAs;
		}
		else if ("year".equalsIgnoreCase(name)) {
			return String.valueOf(ActivePeriod.getInstance().getYear());
		}		
		else if ("month".equals(name)) {
			return String.valueOf(ActivePeriod.getInstance().getMonth());
		}
		else if ("monthno".equalsIgnoreCase(name)) {
			//TODO 更好的写法？
			
			for (Measurment measurment : aggList) {
				String code = measurment.getCode();
				if ("MGrowth".equalsIgnoreCase(code)) {
					return "1";
				}
				else if ("YGrowth".equalsIgnoreCase(code)) {
					return "12";
				}
			}
			
		}
		else if ("rank_insert".equalsIgnoreCase(name)) {
			return "id, aggid, ranktype, rankarea, rank";
		}
		else if ("rank_agged".equalsIgnoreCase(name)) {
			if (aggedMeasurment == null) {
				 return "achieve";
			}
			AggTable soureseAggTable = getAggTable("sourse");
			String code = aggedMeasurment.getCode();
			String aggFieldName = soureseAggTable.getAggFieldName(AggDirection.Agg, code);
			
			return aggFieldName;
		}
		else if ("rank_partition".equalsIgnoreCase(name)) {
			String partition = "";
			String rankCode = rankDimension.getCode();
			boolean isBase = false;
			List<Dimension> dimensionList = dimensionSpace.getDimensionList();
			for (Dimension dimension : dimensionList) {
				String dimensionCode = dimension.getCode();
				if (rankCode.equalsIgnoreCase(dimensionCode)) {
					isBase = true;
				}
			}
			String masterTableName = rankDimension.getFather().getMasterTableName();
			if (!isBase) {
				partition = "partition by " + masterTableName + Dot + rankDimension;
			}
			return partition;
		}
		else if ("rank_type".equalsIgnoreCase(name)) {
			AggTable sourceAggTable = getAggTable(AggConstant.Agg_Source);
			String jointDimensionField = getJointField(sourceAggTable, AggDirection.Dimension);
			return jointDimensionField;
		}
		
		ContentBuilder builder = null;
		
		if ("agg_insert".equalsIgnoreCase(name)) {
			builder = new ContentBuilder(", ");
            addAggInsertExtraField(builder);

			AggDirection[] aggDirections = AggDirection.values();

			for (AggDirection aggDirection : aggDirections) {
				String jointField = getJointField(getAggTable(AggConstant.Agg_Target), aggDirection);
                if (Util.isEmptyStr(jointField)) {
                    continue;
                }
				String fields = jointField.replace(initSeparator, ", ");
				builder.append(fields);
			}
		}
		else if ("agg_select".equalsIgnoreCase(name)) {
			builder = new ContentBuilder(", ");
            addAggInsertExtraFieldValue(builder);

			AggTable sourceAggTable = getAggTable(AggConstant.Agg_Source);
			AggTable targetAggTable = getAggTable(AggConstant.Agg_Target);

            // dimension
            String jointDimensionField = getJointField(sourceAggTable, AggDirection.Dimension);
            String fields = jointDimensionField.replaceAll(initSeparator, ", ");
            builder.append(fields);

			//add Measurment fields
			for (Measurment measurment : mesurmentList) {
				String fieldName = sourceAggTable.getAggFieldName(AggDirection.Measurment, measurment.getCode());
				builder.append(MessageFormat.format(AggConstant.AGG_Sum_Template, fieldName, fieldName));
			}
			
			// add agg fields
			for (Measurment aggMeasurment : aggList) {
				String getFormula = aggMeasurment.getFormula();
				
				String aggName = targetAggTable.getAggFieldName(AggDirection.Agg, aggMeasurment.getCode());
				
				//公式替换
				getFormula = changeFormula(sourceAggTable, getFormula);
				
				builder.append(MessageFormat.format(AggConstant.AGG_As_Template, getFormula, aggName));
			}
			
		}
		else if ("agg_selectin".equalsIgnoreCase(name)) {
			builder = new ContentBuilder(", ");

			AggTable sourceAggTable = getAggTable(AggConstant.Agg_Source);
			
			String jointDimensionField = getJointField(sourceAggTable, AggDirection.Dimension);
			String fields = jointDimensionField.replaceAll(initSeparator, ", ");
			builder.append(fields);
			
			//add Measurment fields
			for (Measurment measurment : mesurmentList) {
				String fieldName = sourceAggTable.getAggFieldName(AggDirection.Measurment, measurment.getCode());
                builder.append(MessageFormat.format(AggConstant.AGG_As_Template, fieldName, fieldName));
			}

		}
		else if ("agg_where".equalsIgnoreCase(name)) {
			builder = new ContentBuilder("and ");
			builder.append("1 = 1");
		}
		else if ("agg_groupby".equalsIgnoreCase(name)) {
			builder = new ContentBuilder(", ");
			
			String dimensionField = getJointField(getAggTable(AggConstant.Agg_Source), AggDirection.Dimension);
			
			String dimensionfields = dimensionField.replaceAll(initSeparator, ", ");
			
			builder.append(dimensionfields);

		}
		else if ("agg_update".equalsIgnoreCase(name)) {
			builder = new ContentBuilder(", ");
			
			AggTable targetAggTable = getAggTable(AggConstant.Agg_Target);
			
			for (Measurment agg : aggList) {
				String fieldName = targetAggTable.getAggFieldName(AggDirection.Agg, agg.getCode());
				
				// 公式替换
				String formula = agg.getFormula();
				
				formula = changeFormula(getAggTable(AggConstant.Agg_Source), formula);
				
				builder.append(fieldName + " = " + formula);
			}
			
		}
		else if ("agg_updatein".equalsIgnoreCase(name)) {
			builder = new ContentBuilder(" , ");
			
			for (String dimension : dimensionAggFieldNameList) {
				if (dimension.equalsIgnoreCase("Period")) {
					continue;
				}
				builder.append(dimension);
			}
			
		}
		else if ("agg_jion".equalsIgnoreCase(name)) {
			builder = new ContentBuilder(" and ");
			
			for (String dimension : dimensionAggFieldNameList) {
				if (dimension.equalsIgnoreCase("Period")) {
					continue;
				}
				builder.append("A." + dimension + "= B." + dimension);
			}
		}
		else if ("rank_select".equalsIgnoreCase(name)) {
			builder = new ContentBuilder(", ");
			String newShortGUID = Util.newShortGUID();
			builder.append(newShortGUID);
			
			builder.append("id");
			
			String masterFieldName = rankDimension.getFieldName();
			builder.append(masterFieldName);
			
			String masterTableName = rankDimension.getFather().getMasterTableName();
			builder.append(masterTableName + Dot + masterFieldName);
			
		}
		else if ("rank_left_join".equalsIgnoreCase(name)) {
			builder = new ContentBuilder(" ");
			AggTable sourceAggTable = getAggTable(AggConstant.Agg_Source);
			List<Dimension> dimensionList = dimensionSpace.getDimensionList();
			
			for (Dimension dimension : dimensionList) {
				//1
				DimensionGroup dimensionGroup = dimension.getFather();
				String masterTableContent = dimensionGroup.getMasterTableContent();
				
				//2 
				String joinPair = assemblyJoinPairs(dimension, sourceAggTable);
				String oneLeftJoin = leftJoin + masterTableContent + On + joinPair;
				
				builder.append(oneLeftJoin);
			}
			
		}
		else if ("rank_where".equalsIgnoreCase(name)) {
			builder = new ContentBuilder(" and ");
			AggTable sourceAggTable = getAggTable(AggConstant.Agg_Source);
			String jointDimensionField = getJointField(sourceAggTable, AggDirection.Dimension);
			builder.append(sourceTable + ".type = " + Util.quotedStr(jointDimensionField));
		}
		
		return builder.toString();				
	}

	private void addAggInsertExtraFieldValue(ContentBuilder builder) {
		builder.append(MessageFormat.format("{0} as id", Util.getDBUUID()));

        builder.append(MessageFormat.format("{0} as {1}", Util.quotedStr(themeTableName), AggConstant.BI_Field_Aggcode));
    }

	private void addAggInsertExtraField(ContentBuilder builder) {
		builder.append(AggConstant.BI_Field_Id);
		builder.append(AggConstant.BI_Field_Aggcode);
	}

	private String assemblyJoinPairs(Dimension dimension, AggTable sourceAggTable) {
		ContentBuilder contentBuilder = new ContentBuilder(" and ");
		StringBuilder pairBuilder = new StringBuilder();
		//1
		pairBuilder.append(dimension.getFather().getMasterTableName() + Dot + dimension.getFieldName());
		pairBuilder.append(" = ");
		
		String code = dimension.getCode();
		String sourceTablename = sourceAggTable.getName();
		String aggFieldName = sourceAggTable.getAggFieldName(AggDirection.Dimension, code);
		pairBuilder.append(sourceTablename + Dot + aggFieldName);
		contentBuilder.append(pairBuilder.toString());
		//2 peroid
		pairBuilder.delete( 0, pairBuilder.length());
		if (!dimension.getCode().equalsIgnoreCase("period")) {
			pairBuilder.append(dimension.getFather().getMasterTableName()+ ".period" + " = " + sourceTablename + ".period");
		}
		contentBuilder.append(pairBuilder.toString());
		return contentBuilder.toString();
	}

	/**
	 * @param sourceAggTable
	 * @param getFormula
	 * @return
	 * @throws Exception 
	 */
	private String changeFormula(AggTable sourceAggTable, String getFormula) throws Exception {
		List<String> preChangedFields = Util.matcher(initCompile, getFormula);
		for (String preChangedField : preChangedFields) {
			String realFieldName = sourceAggTable.getAggFieldName(AggDirection.Measurment, preChangedField);
			
			
			if (Util.isEmptyStr(realFieldName)) {
				realFieldName = sourceAggTable.getAggFieldName(AggDirection.Agg, preChangedField);
				if (Util.isEmptyStr(realFieldName)) {
					realFieldName = getSqlString(preChangedField);
				}
			}
			getFormula = getFormula.replaceAll("@\\{" + preChangedField + "\\}" , realFieldName);
		}
		
		return getFormula;
	}

	public AggTable getAggTable(String tableName) {
		if (tableMap == null) {
			return null;
		}
		return tableMap.get(tableName);
	}
	
	public Map<String, AggTable> getTableMap() {
		return tableMap;
	}
	
	public String getJointField(AggTable table, AggDirection direction) {
		if (jointFieldsMap == null) {
			return null;
		}
		if (table == null) {
			return null;
		}
		String key = table.getName() + "-" + direction.name();
		return jointFieldsMap.get(key);
	}
	
	public Map<String, String> getJointFieldsMap() {
		return jointFieldsMap;
	}

	public String getTargetTable() {
		return targetTable;
	}

	public String getSourceTable() {
		return sourceTable;
	}

	public String getThemeTableName() {
		return themeTableName;
	}

	public String getTargetTableAs() {
		return targetTableAs;
	}

	public String getSourceTableAs() {
		return sourceTableAs;
	}

	public DimensionSpace getDimensionSpace() {
		return dimensionSpace;
	}

	public String getNameTemplate() {
		return nameTemplate;
	}

	public List<Measurment> getAggList() {
		return aggList;
	}

	public List<Measurment> getMesurmentList() {
		return mesurmentList;
	}

	public String getInitSeparator() {
		return initSeparator;
	}

	public String getInitCompile() {
		return initCompile;
	}

	public Logger getLogger() {
		return logger;
	}

	public List<String> getDimensionAggFieldNameList() {
		return dimensionAggFieldNameList;
	}

	public AggTheme getAggTheme() {
		return this.aggTheme;
	}

	public void setRankAgged(Measurment measurment) {
		this.aggedMeasurment = measurment;
	}

	public void setRankWhere(Dimension parentDimension) {
		this.rankDimension = parentDimension;
	}

}

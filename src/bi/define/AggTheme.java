package bi.define;

import bi.AggConstant;
import bi.exception.AggDBlLoadException;
import foundation.data.Entity;
import foundation.data.EntitySet;
import foundation.persist.DataHandler;
import foundation.persist.Field;
import foundation.persist.TableMeta;
import foundation.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AggTheme {

	private Logger logger;
	private String sqlTemplateId;
	private String measurementIds;
	private String dimensionCodes;
	private String nameTemplate;
	private String sqlTemplateName;
	private String sqlTemplate;
	private String targetTable;
	private String sourceTable;
	private AggType aggType;
	
	private List<DimensionSpace> dimensionSpaces;
	private List<Measurment> measurmentList;
	private List<Measurment> aggList;
	
	private List<Entity> periodTypeList;
	
	private List<String> dimensionAggFieldNameList;
	
	public AggTheme(Entity oneTheme) {
		dimensionSpaces = new ArrayList<DimensionSpace>();
		measurmentList = new ArrayList<Measurment>();
		aggList = new ArrayList<Measurment>();
		periodTypeList = new ArrayList<Entity>();
		dimensionAggFieldNameList = new ArrayList<String>();
		
		logger = LoggerFactory.getLogger(this.getClass());
		load(oneTheme);
	}
	
	private void load(Entity oneTheme) {
		try {
			//1
			this.nameTemplate = oneTheme.getString(AggConstant.BI_Field_Name);
			this.sqlTemplateId = oneTheme.getString(AggConstant.BI_Field_Sqltemplateid);
			this.measurementIds = oneTheme.getString(AggConstant.BI_Field_Measurment);
			this.dimensionCodes = oneTheme.getString(AggConstant.BI_Field_Dimension);
			this.targetTable = oneTheme.getString(AggConstant.BI_Field_Targettable);
			this.sourceTable = oneTheme.getString(AggConstant.BI_Field_Sourcetable);
			this.aggType =  AggType.valueOf(oneTheme.getString(AggConstant.BI_Field_GroupId));
			//2.1
			Entity sqlTemplateEntity = DataHandler.getLine(AggConstant.BI_TABLE_SqlTemplate, this.sqlTemplateId);
			this.sqlTemplateName = sqlTemplateEntity.getString(AggConstant.BI_Field_Name);
			this.sqlTemplate = sqlTemplateEntity.getString(AggConstant.BI_Field_Sql);
			
			//2.2
			switch (aggType) {
				case Achieve:
				case Sum:
				case Rank:
					parse2dimensionSpaces(this.dimensionCodes);
			        break;
				case Growth:
					loadDimensionSpace(this.dimensionCodes);
					break;
			    default:
			        break;

			}

			combineMeasurement(measurementIds);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	private void combineMeasurement(String measurementIds) throws Exception {
		String[] measurementIdArray = measurementIds.split(Util.semicolon);
		for (String measurementId : measurementIdArray) {
			Entity entity = DataHandler.getLine(AggConstant.BI_TABLE_Measurment, measurementId);
			String type = entity.getString(AggConstant.BI_Field_Type);
			AggDirection parse = AggDirection.parse(type);
			Measurment measurment = new Measurment(entity);
			if (AggDirection.Agg.equals(parse)) {
				aggList.add(measurment);
			}
			else if (AggDirection.Measurment.equals(parse)) {
				measurmentList.add(measurment);
			}
		}
	}

	private void loadDimensionSpace(String dimensionCodes) {
		String[] dimensionTypes = dimensionCodes.split(Util.semicolon);
		for (String dimensionGroup : dimensionTypes) {
			String dimension = dimensionGroup.split(Util.Spilt_Dot)[0];
			String tableName = sourceTable;
			if(tableName.toLowerCase().indexOf(AggConstant.BI_Default_As) != -1) {
				tableName = tableName.split(AggConstant.BI_Default_As)[0].trim();
			}
			try {
				Entity biTableEntity = DataHandler.getDataSet(AggConstant.BI_TABLE_TABLE, "name = " + Util.quotedStr(tableName)).next();
                if (biTableEntity == null) {
                    // 未配置

                    TableMeta tableMeta = TableMeta.getInstance(tableName);
                    if (tableMeta == null) {
                        throw new AggDBlLoadException(MessageFormat.format("无此表{0}", tableName));
                    }
                    List<Field> fieldList = tableMeta.getFields();
                    fieldList.stream()
                            .filter(field -> !AggConstant.filedUnCatchList.contains(field.getName()))
                            .map(field -> dimensionAggFieldNameList.add(field.getName())).collect(Collectors.toList());
                } else {
                    String biTableId = biTableEntity.getString(AggConstant.BI_Field_Id);

                    Entity biFieldEntity = DataHandler.getDataSet(AggConstant.BI_TABLE_FLEID, "groupcode ="+ Util.quotedStr(dimension) +" and tableid =" + Util.quotedStr(biTableId)).next();
                    if (biFieldEntity != null) {
                        String name = biFieldEntity.getString(AggConstant.BI_Field_Name);
                        dimensionAggFieldNameList.add(name);
                    }
                }

			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void parse2dimensionSpaces(String dimensionCodes) throws Exception {
		String[] dimensionTypes = dimensionCodes.split(";");
		//1 init 二维数组（集合）
		ArrayList<ArrayList<Entity>> init2DimensionalList = init2DimensionalList(dimensionTypes);
		// parse
		parse2DimensionalList(this.dimensionSpaces, init2DimensionalList);
		
		loadDimensionSpace(dimensionCodes);
	}

	@SuppressWarnings("unchecked")
	public void parse2DimensionalList(List<DimensionSpace> dimensionSpaces, ArrayList<ArrayList<Entity>> init2DimensionalList) {
		ArrayList<ArrayList<Entity>> dikaedList;
		if(init2DimensionalList.size() == 1) {
			dikaedList = new ArrayList<>();
			
			ArrayList<Entity> DimensionalList = init2DimensionalList.get(0);
            dikaedList.add(DimensionalList);
		}
		else {
			 dikaedList= Util.Dikaerji0(init2DimensionalList);
		}
		
		for (ArrayList<Entity> dimensionEntityList : dikaedList) {
			DimensionSpace dimensionSpace = new DimensionSpace();
			
			for (Entity entity : dimensionEntityList) {
				Dimension dimension = new Dimension(entity);
				dimensionSpace.addDimension(dimension);
				
			}
			
			dimensionSpaces.add(dimensionSpace);
		}
	}

	/**
	 * @param dimensionTypes
	 * @return ArrayList<ArrayList<String>>
	 * @throws Exception
	 */
	private ArrayList<ArrayList<Entity>> init2DimensionalList(String[] dimensionTypes) throws Exception {
		ArrayList<ArrayList<Entity>> dimensionListLists = new ArrayList<ArrayList<Entity>>();

		for (String dimensionsType : dimensionTypes) {
			ArrayList<Entity> dimensionList = new ArrayList<Entity>();
			String[] dimensions = dimensionsType.split(Util.comma);
			String fitstDimensionType = dimensions[0];
			
			String fitstDimensionGroup = fitstDimensionType.split(Util.Spilt_Dot)[0];
			String fitstDimension = fitstDimensionType.split(Util.Spilt_Dot)[1];
			
			if(Util.Star.equalsIgnoreCase(fitstDimension)) {
				EntitySet dimensionSet = DataHandler.getDataSet(AggConstant.BI_TABLE_Dimension, "groupid = " + Util.quotedStr(fitstDimensionGroup) + "and active = 'T'");
				
				for (Entity dimensionEntity : dimensionSet) {
					dimensionList.add(dimensionEntity);
				}
			}
			else {
				for (String oneDimensionType : dimensions) {
					String[] oneDimensionSplit = oneDimensionType.split(Util.Dot);
					
					String oneDimension = oneDimensionSplit[1];
					Entity dimensionEntity = DataHandler.getDataSet(AggConstant.BI_TABLE_Dimension, "code = " + Util.quotedStr(oneDimension)).next();

					dimensionList.add(dimensionEntity);
				}
			}
			if (dimensionList.size()  != 0) {
				dimensionListLists.add(dimensionList);
			}
			
		}
		return dimensionListLists;
	}

	
	public String getSqlTemplateId() {
		return sqlTemplateId;
	}

	public String getMeasurementIds() {
		return measurementIds;
	}

	public String getDimensionCodes() {
		return dimensionCodes;
	}

	public String getNameTemplate() {
		return nameTemplate;
	}

	public String getSqlTemplateName() {
		return sqlTemplateName;
	}

	public String getSqlTemplate() {
		return sqlTemplate;
	}

	public List<DimensionSpace> getDimensionSpaces() {
		return dimensionSpaces;
	}

	public String getTargetTable() {
		return targetTable;
	}

	public String getSourceTable() {
		return sourceTable;
	}

	public List<Measurment> getMeasurmentList() {
		return measurmentList;
	}

	public List<Measurment> getAggList() {
		return aggList;
	}

	public List<String> getDimensionAggFieldNameList() {
		return dimensionAggFieldNameList;
	}
	
}

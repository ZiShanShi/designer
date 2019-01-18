package bi;

import bi.agg.Operator;
import bi.define.*;
import bi.work.CarrierContainer;
import foundation.data.Entity;
import foundation.data.EntitySet;
import foundation.engine.Command;
import foundation.engine.Engine;
import foundation.engine.State;
import foundation.persist.DataHandler;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLRunner;
import foundation.rule.RuleEngine;
import foundation.rule.RuleList;
import foundation.rule.RuledResult;
import foundation.util.Util;

import java.util.ArrayList;
import java.util.List;

public class WorkEngine extends Engine {

	private static CarrierContainer carrierContainer;
	private static WorkEngine instance;
	
	static {
		carrierContainer = CarrierContainer.getInstance();
	}

	private WorkEngine() throws Exception {
		super();
	}

	@Override
	protected void initCommandMap() throws SecurityException, NoSuchMethodException {
		commandMap.put("createbaseaggregation",	new Command(this, WorkEngine.class.getMethod("createBaseAggregation", Operator.class), true));
	}

	public static synchronized WorkEngine getInstance() {
		if (instance == null) {
			try {
				instance = new WorkEngine();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		return instance;
	}

	public void calculate(Operator operator) {
		progressor.newTask("数据计算");
		try {
			carrierContainer.refresh();

			// 1. 校验
			if (TestOn.Validate) {
				progressor.newPhase("phase_validate", "数据校验");
				int errorCnt = validate();

				if (errorCnt > 0) {
					progressor.terminate("数据校验未通过，请检查");
					return;
				}
				progressor.endPhase();
			}

		}
		catch (Exception e) {
			progressor.terminate("系统错误，运行终止：" + e.getClass() + ":" + e.getMessage());
		}
		finally {
			progressor.endTask();
		}
	}


	private int validate() throws Exception {
		RuleEngine ruleEngine = RuleEngine.getInstance();// 创建引擎

		NamedSQL namedSQL = NamedSQL.getInstance("getCheckList");

		RuleList ruleList = RuleList.newInstance(namedSQL);// 创建校验集
		RuledResult ruledResult = ruleEngine.exec(ruleList);// 执行

		int errorCnt = ruledResult.getErrorCnt();// 执行结果
		return errorCnt;
	}


	private void refreshTerrtory() {
		try {
			NamedSQL truncateInstance = NamedSQL.getInstance("truncateTable");
			truncateInstance.setParam("tableName", "territory");
			SQLRunner.execSQL(truncateInstance);
			
			EntitySet positionSet = DataHandler.getDataSet("t_s_depart", "org_type = '4'");
			for (Entity entity : positionSet) {
				Entity territoryEntity = new Entity("territory");
				int type = 4;
				
				String position = entity.getString("id");
				String parentdepartId = entity.getString("parentdepartid");
				territoryEntity.set("parentid1", position);
				type--;
				addTerritoryField(type, parentdepartId, territoryEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addTerritoryField(int type, String parentdepartId, Entity territoryEntity) throws Exception {
		if (type == 0) {
			EntitySet dataSet = DataHandler.getDataSet("t_s_depart", "org_type = '0'");
			Entity next = dataSet.next();
			String id = next.getString("id");
			territoryEntity.set("parentid5", id);
			
			Entity nowPeroid = DataHandler.getDataSet("peroid", "active = 'T'").next();
			String nowPeroidId = nowPeroid.getString("id");
			territoryEntity.set("period", nowPeroidId);
			
			DataHandler.addLine(territoryEntity);
			return;
		}
		Entity line = DataHandler.getLine("t_s_depart", parentdepartId);
		int orgType = line.getInteger("org_type");
		
		if (orgType > type) {
			String parentdepartid = line.getString("parentdepartid");
			if (Util.isEmptyStr(parentdepartid) || parentdepartid.equalsIgnoreCase(parentdepartId)) {
				logger.error("id = parentid= " + parentdepartId);
				return;
			}
			addTerritoryField(type,parentdepartid, territoryEntity);
		}
		else {
			type = orgType;
			String position = line.getString("id");
			String parentdepartid = line.getString("parentdepartid");
			
			if(Util.isEmptyStr(parentdepartId)) {
				logger.info("parentid is null");
				return;
			}
			
			if (position.equalsIgnoreCase(parentdepartid)) {
				logger.error("id = parentid= " + parentdepartId);
				return;
			}
			
			territoryEntity.set("parentid" + (5 - orgType), position);
			type--;
			if(type >=0) {
				addTerritoryField(type,parentdepartid, territoryEntity);
				
			}else {
				DataHandler.addLine(territoryEntity);
			}
			
		}
	}
	
	public void createBaseAggregation(Operator operator) {
		instance.setState(State.working);
		progressor.newTask("数据计算");
		// 1. 校验
		if (TestOn.Validate) {
			progressor.newPhase("phase_validate", "数据校验");
			int errorCnt;
			try {
				errorCnt = validate();
				if (errorCnt > 0) {
					progressor.terminate("数据校验未通过，请检查");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			progressor.endPhase();
		}

		//2. 数据初始化(清空)
		progressor.newPhase("phase_initdata", "数据初始化");
		EntitySet dataSet;
		try {
			dataSet = DataHandler.getDataSet("bi_table", "clear = 'T'");
			for (Entity entity : dataSet) {
				String tableName = entity.getString("name");
				NamedSQL truncateInstance = NamedSQL.getInstance("deleteByCriteria");
				truncateInstance.setParam("tableName", tableName);
				truncateInstance.setParam("filter", "period = (select id from peroid where active = 'T')");
				int execSQL = SQLRunner.execSQL(truncateInstance);
				
				logger.info("delete--" + tableName + "----count:" +execSQL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//refreshTerrtory();
		
		progressor.endPhase();
		//3. 循环聚合
		AggThemeGroupContainer.refresh();
		List<AggThemeGroup> aggThemeGroupList = AggThemeGroupContainer.getInstance().getAggThemeGroupList();
		
		for (AggThemeGroup aggThemeGroup : aggThemeGroupList) {
			logger.info("aggThemeGroup : " + aggThemeGroup.getName());
			try {
				AggOneTemeGroup(aggThemeGroup);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		instance.setState(State.Idle);
	}

	/**
	 * @param aggThemeGroup
	 * @throws Exception
	 */
	private void AggOneTemeGroup(AggThemeGroup aggThemeGroup) {
		List<AggTheme> aggThemesMapList = aggThemeGroup.getAggThemesMapList();
		logger.info("aggThemes size :" + aggThemesMapList.size());
		AggType aggCode = AggType.valueOf(aggThemeGroup.getCode());
		for (AggTheme aggTheme : aggThemesMapList) {
			AggOneTheme(aggCode, aggTheme);
		}
	}

	/**
	 * @param code
	 * @param aggTheme
	 * @throws Exception
	 */
	private void AggOneTheme(AggType code, AggTheme aggTheme) {
		List<DimensionSpace> dimensionSpaces = aggTheme.getDimensionSpaces();
		List<String> dimensionAggFieldNameList = aggTheme.getDimensionAggFieldNameList();
		//achieve		
		if (Util.isEmptyStr(code)) {
			return;
		}
		switch (code) {
			case Achieve:
			case Sum:
				for (DimensionSpace dimensionSpace : dimensionSpaces) {
					AggContext aggContext = new AggContext(aggTheme, dimensionSpace, dimensionAggFieldNameList);
					execOneTheme(aggContext);
				}
		        break;
			case Rank:
				for (DimensionSpace dimensionSpace : dimensionSpaces) {
					List<Dimension> dimensionList = dimensionSpace.getDimensionList();
					// e.g. c_code-period
					List<Dimension> parentDimensionsLists = getParentDimensionsLists(dimensionList);
					for (Dimension parentDimension : parentDimensionsLists) {
						List<Measurment> aggList = aggTheme.getAggList();
						for (Measurment measurment : aggList) {
							AggContext aggContext = new AggContext(aggTheme, dimensionSpace, dimensionAggFieldNameList);
							aggContext.setRankAgged(measurment);
							aggContext.setRankWhere(parentDimension);
							execOneTheme(aggContext);
						}
					}

				}
				break;
			case Growth:
				AggContext aggContext = new AggContext(aggTheme, dimensionAggFieldNameList);
				execOneTheme(aggContext);
				break;
		    default:
		        break;

		}
	}

	/**
	 * @param dimensionList
	 * @return 
	 */
	private List<Dimension> getParentDimensionsLists(List<Dimension> dimensionList) {
		//c_code
		ArrayList<Dimension> rankAreaList = new ArrayList<Dimension>();
		for (Dimension dimension : dimensionList) {
			//int codeNo = dimension.getCodeNo();
			int codeNo = -1;
			String groupId = dimension.getGroupId();
			try {
				EntitySet dataSet = DataHandler.getDataSet(AggConstant.BI_TABLE_Dimension, "groupid = " + Util.quotedStr(groupId) + " and no >=" + codeNo);
				for (Entity entity : dataSet) {
					Dimension fatherDimension = new Dimension(entity);
					rankAreaList.add(fatherDimension);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rankAreaList;
	}

	/**
	 * @param aggContext
	 * @throws Exception
	 */
	private void execOneTheme(AggContext aggContext) {
		String themeTableName = aggContext.getThemeTableName();
		logger.info("execOneTheme: " + themeTableName);
		AggTheme aggTheme = aggContext.getAggTheme();
		
		try {
			String sqlTemplate = aggTheme.getSqlTemplate();
			String sqlTemplateName = aggTheme.getSqlTemplateName();
			NamedSQL namedSQL = new NamedSQL(sqlTemplateName, sqlTemplate);
			
			aggContext.setParametersTo(namedSQL);
			int execSQL = SQLRunner.execSQL(namedSQL);
			logger.info("exec num: " + execSQL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

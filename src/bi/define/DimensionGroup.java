package bi.define;

import foundation.data.Entity;
import foundation.persist.DataHandler;
import foundation.util.ContentBuilder;
import foundation.util.Util;

public class DimensionGroup {

	private static final String masterTableSplit = ";";
	private static final String masterTableTemplate = "select * , '' as period_lastest from ";
	private static final String masterTableNamesuffix = "master";
	private static final String historyTableTemplate = "select *  from ";

	private String id;
	private String code;
	private String name;
	
	private String masterTableContent;
	private String masterTableName;
	
	public DimensionGroup(Entity entity) {
		load(entity);
	}
	
	public void load(Entity entity) {
		id = entity.getString("id");
		code = entity.getString("code");
		name = entity.getString("name");
		try {
			masterTableName = name + masterTableNamesuffix;
			masterTableContent = assemblyMasterTable(entity.getString("maintableid"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String assemblyMasterTable(String mainTableId) throws Exception {
		if (Util.isEmptyStr(mainTableId)) {
			throw new Exception("DimensionGroup mainTableId is null");
		}
		ContentBuilder builder  = new ContentBuilder(" union ");
		
		Entity line = DataHandler.getLine("bi_maindatatable", mainTableId);
		String masterTableName = line.getString("maindata");
		builder.append(masterTableTemplate + masterTableName);
		
		String historyTableName = line.getString("historymaindata");
		if (!Util.isEmptyStr(historyTableName)) {
			String[] split = historyTableName.split(masterTableSplit);
			for (int i = 0; i < split.length; i++) {
				String tableName = split[i];
				String oneTableString = historyTableTemplate + tableName;
				builder.append(oneTableString);
			}
		}
		
		String master = "(" + builder.toString() + ") as " + masterTableName;
		return master;
	}
	
	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getMasterTableName() {
		return masterTableName;
	}

	public String getMasterTableContent() {
		return masterTableContent;
	}
	
}

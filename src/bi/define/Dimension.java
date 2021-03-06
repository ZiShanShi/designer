package bi.define;

import bi.AggConstant;
import foundation.data.Entity;
import foundation.persist.DataHandler;

public class Dimension {
	
	private String id;
	private String code;
	private String name;
	private String parentCode;
	private String fieldName;
	
	private String groupId;
	private DimensionGroup father;
	
	public Dimension(Entity entity) {
		load(entity);
	}
	
	public void load(Entity entity) {
		try {
			id = entity.getString(AggConstant.BI_Field_Id);
			code = entity.getString(AggConstant.BI_Field_Code);
			name = entity.getString(AggConstant.BI_Field_Name);
			groupId = entity.getString(AggConstant.BI_Field_GroupId);
			fieldName = entity.getString(AggConstant.BI_TABLE_Fieldname);
			
			parentCode = entity.getString(AggConstant.BI_TABLE_Parentcode);
			
			Entity line = DataHandler.getLine(AggConstant.BI_TABLE_TDIMENSIONGROUP, groupId);
			father = new DimensionGroup(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
	
	public String getGroupId() {
		return groupId;
	}

	public String getParentCode() {
		return parentCode;
	}

	public DimensionGroup getFather() {
		return father;
	}

	public String getFieldName() {
		return fieldName;
	}

	@Override
	public int hashCode() {
		return this.getCode().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Dimension) {
			return hashCode() == obj.hashCode();
		}
		return super.equals(obj);
	}
}

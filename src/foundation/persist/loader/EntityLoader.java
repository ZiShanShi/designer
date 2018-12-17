package foundation.persist.loader;

import java.sql.ResultSet;

import foundation.data.Entity;

public class EntityLoader extends DataLoader {

	private Entity entity;
	
	public EntityLoader(String tableName) {
		super(tableName);
	}
	
	@Override
	protected void loadData(ResultSet rslt) throws Exception {
		if (rslt.next()) {
			entity = new Entity(tableMeta);
			int cnt = entity.getFieldCount();
			
			Object obj;
			
			for (int i = 0; i < cnt; i++) {
				obj = rslt.getObject(i + 1);
				entity.set(i, obj);
			}
		}		
	}

	public Entity getEntity() {
		return entity;
	}

}

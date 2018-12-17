package foundation.persist.loader;

import java.sql.ResultSet;

import foundation.data.Entity;
import foundation.data.EntitySet;

public class EntitySetLoader extends DataLoader {

	private EntitySet entitySet;
	
	public EntitySetLoader(String tableName) {
		super(tableName);
	}

	protected void loadData(ResultSet rslt) throws Exception {
		entitySet = new EntitySet(tableMeta);
		int cnt = entitySet.getFieldCount();
		
		Entity entity;
		Object obj;
		
		while (rslt.next()) {
			entity = entitySet.append();
			
			for (int i = 0; i < cnt; i++) {
				obj = rslt.getObject(i + 1);
				entity.set(i, obj);
			}
		}
	}

	public EntitySet getDataSet() {
		return entitySet;
	}

}

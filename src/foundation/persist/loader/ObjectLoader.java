package foundation.persist.loader;

import java.sql.ResultSet;
import java.util.List;

import foundation.data.reader.EntityReaderContainer;
import foundation.data.reader.ObjectReader;
import foundation.persist.Field;

public class ObjectLoader extends DataLoader {

	protected static EntityReaderContainer objectReaderContainer;
	private Class<?> clazz;
	private ObjectReader objectReader;
	private Object object;
	
	static {
		objectReaderContainer = EntityReaderContainer.getInstance();
	}

	public ObjectLoader(String tableName, Class<?> clazz) throws Exception {
		super(tableName);
		
		this.clazz = clazz;
		objectReader = objectReaderContainer.getObjectReader(clazz);
	}
	
	@Override
	protected void loadData(ResultSet rslt) throws Exception {
		if (rslt.next()) {
			if (object == null) {
				object = clazz.newInstance();
			}
			
			List<Field> fields = tableMeta.getFields();
			int size = fields.size(); String name;
			
			for (int i = 0; i < size; i++) {
				Field field = fields.get(i);
				name = field.getName();
				
				if (objectReader.containsProperty(name)) {
					Object value = rslt.getObject(i + 1);
					objectReader.setData(name, value, object);
				}
			}
		}
	}
	
	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
}

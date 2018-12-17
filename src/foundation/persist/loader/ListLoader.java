package foundation.persist.loader;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import foundation.data.reader.EntityReaderContainer;
import foundation.data.reader.ObjectReader;
import foundation.persist.Field;

public class ListLoader<T> extends DataLoader {

	protected static EntityReaderContainer objectReaderContainer;
	private Class<T> clazz;
	private ObjectReader objectReader;
	private List<T> list;
	
	static {
		objectReaderContainer = EntityReaderContainer.getInstance();
	}

	public ListLoader(String tableName, Class<T> clazz) throws Exception {
		super(tableName);
		
		this.clazz = clazz;
		objectReader = objectReaderContainer.getObjectReader(clazz);
	}
	
	@Override 
	protected void loadData(ResultSet rslt) throws Exception {
		list = new ArrayList<T>();
		
		List<Field> fields = tableMeta.getFields();
		T object; int size = fields.size(); String name;
		
		while (rslt.next()) {
			object = clazz.newInstance();
			list.add(object);
			
			for (int i = 1; i <= size; i++) {
				Field field = fields.get(i);
				name = field.getName();
				
				if (objectReader.containsProperty(name)) {
					Object value = rslt.getObject(i);
					objectReader.setData(name, value, object);
				}
			}
		}
	}

	public List<T> getList() {
		return list;
	}

}

package foundation.persist.sql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class NamedSQLSet implements Iterable<NamedSQL> {

	private List<NamedSQL> items;
	
	static {
//		namedSQLContainer = NamedSQLContainer.getInstance();
	}
	
	public NamedSQLSet(List<NamedSQL> list) throws Exception {
		items = new ArrayList<NamedSQL>();
		
		for (NamedSQL namedSQL: list) {
			NamedSQL instance = namedSQL.newInstance();
			items.add(instance);
		}
	}
	
	public static NamedSQLSet getInstance(String name) throws Exception {
		//TODO
		return null;
/*		List<NamedSQL> list = namedSQLContainer.getAll(name);
		NamedSQLSet result = new NamedSQLSet(list);
		
		return result;*/
	}

	public void setParam(String name, String value) {
		for (NamedSQL namedSQL: items) {
			namedSQL.setParam(name, value);
		}
	}

	@Override
	public Iterator<NamedSQL> iterator() {
		return items.iterator();
	}

	public Result exec(String dataSource) throws Exception {
		Result result = SQLRunner.getResult(this);
		return result;
	}

	public void setReturnType(ReturnType returnType) {
		NamedSQL namedSQL = items.get(items.size() - 1);
		namedSQL.setReturnType(returnType);
	}

	public List<NamedSQL> getItems() {
		return items;
	}

}

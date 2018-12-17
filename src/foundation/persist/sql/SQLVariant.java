package foundation.persist.sql;


public class SQLVariant extends SQLSegment {

	private String name;
	private String value;
	
	
	public SQLVariant(String name) {
		this.name = name;
	}
	
	@Override
	public String getValueString() {
		return value;
	}

	@Override
	public SQLSegment newInstance() {
		SQLVariant result = new SQLVariant(name);
		
		if (NamedSQL.Param_Filter.equalsIgnoreCase(name)) {
			result.value = " 1 = 1 ";
		}
		else if (NamedSQL.Param_OrderBy.equalsIgnoreCase(name)) {
			result.value = "";
		}
		
		return result;
	}
	
	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "@{" + name + "}=" + value;
	}

	public void clearValue() {
		value = null;
	}
	
	public boolean isEmpty() {
		return value == null;
	}
	
}

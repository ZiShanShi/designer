package foundation.persist.sql;

import java.util.List;

public class SQLParameterSetter {

	private ISQLString source;
	
	
	public SQLParameterSetter(ISQLString source) {
		this.source = source;
	}
	
	public static void setParametersTo(NamedSQL namedSQL, Object... args) throws Exception {
		for (Object obj: args) {
			if (obj instanceof ISQLContext) {
				ISQLContext context = (ISQLContext)obj;
				context.setParametersTo(namedSQL);
			}
			else if (obj instanceof ISQLString) {
				ISQLString sqlString = (ISQLString)obj;
				SQLParameterSetter setter = new SQLParameterSetter(sqlString);
				setter.setTo(namedSQL);
			}
		}
	}
	

	public void setTo(NamedSQL namedSQL) {
		List<SQLVariant> variantList = namedSQL.getVariantList();

		for (SQLVariant variant : variantList) {
			if (!variant.isEmpty()) {
				continue;
			}
			
			String name = variant.getName();
			String value = source.getSqlString(name);

			if (value != null) {
				variant.setValue(value);
			}
		}		
	}

	
}

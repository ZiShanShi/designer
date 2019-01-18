package bi.agg;

import java.util.List;

import foundation.persist.sql.ISQLContext;
import foundation.persist.sql.ISQLString;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLVariant;

public class SQLParameterSetter {

	
	public static void setParametersTo(NamedSQL namedSQL, Object... args) throws Exception {
		ParamSetterList setterList = new ParamSetterList(args);
		
		for (Object obj: args) {
			if (obj instanceof ISQLContext) {
				ISQLContext context = (ISQLContext)obj;
				context.setParametersTo(namedSQL, setterList);
			}
			else if (obj instanceof ISQLString) {
				ISQLString sqlString = (ISQLString)obj;
				setTo(namedSQL, sqlString);
			}
		}
	}
	
	public static void setTo(NamedSQL namedSQL, ISQLString sqlString) {
		List<SQLVariant> variantList = namedSQL.getVariantList();

		for (SQLVariant variant : variantList) {
			if (!variant.isEmpty()) {
				continue;
			}
			
			String name = variant.getName();
			String value = sqlString.getSqlString(name);

			if (value != null) {
				variant.setValue(value);
			}
		}		
	}
	
}

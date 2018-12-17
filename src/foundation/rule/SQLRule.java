package foundation.rule;

import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLRunner;

public class SQLRule extends Rule {

	public SQLRule(String name, String sqlName) {
		super(name, sqlName);
	}

	@Override
	public void exec(IContext context, RuledResult result) throws Exception {

		NamedSQL namedSQL = NamedSQL.getInstance(key);
		int errorCnt = SQLRunner.getInteger(namedSQL);
		
		result.setErrorCnt(errorCnt);
	}
}

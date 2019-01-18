package bi.theme;

import foundation.persist.sql.NamedSQL;

public class Theme extends NamedSQL {

	public Theme(String name, String sql) throws Exception {
		super(name, sql);
	}

	public Theme newInstance() throws Exception {
		Theme instance = new Theme(this.name, this.sql);
		instance.sqlCreator = this.sqlCreator.newInstance();
		return instance;
	}
}

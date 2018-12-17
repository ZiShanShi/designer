package foundation.persist;

import com.alibaba.druid.pool.DruidDataSource;

public class NamedDataSource extends DruidDataSource {

	private static final long serialVersionUID = 1L;
	private String name;
	
	public NamedDataSource(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}

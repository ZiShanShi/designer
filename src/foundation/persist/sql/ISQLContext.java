package foundation.persist.sql;


public interface ISQLContext {

	void setParametersTo(NamedSQL namedSQL, Object ...args) throws Exception;
	
}

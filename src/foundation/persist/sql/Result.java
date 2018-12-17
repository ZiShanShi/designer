package foundation.persist.sql;

import java.math.BigDecimal;

import foundation.callable.ResultType;
import foundation.data.Entity;
import foundation.data.EntitySet;


public class Result {
	
	private ResultType resultType;
	private BigDecimal bigDecimal;
	private Integer intValue;
	private String stringValue;
	private EntitySet entitySet;
	private Entity entity;
	private Object object;
	
	
	public Result() {
		
	}

	public void setValue(Integer value) {
		intValue = value;
		object = value; 
		resultType = ResultType.Num;  
	}
	
	public void setValue(String value) {
		stringValue = value;
		object = value;
		resultType = ResultType.Str;  
	}

	public void setValue(BigDecimal value) {
		bigDecimal = value;
		object = value;
		resultType = ResultType.Num;		
	}

	public void setValue(Entity value) {
		entity = value;
		object = value;
		resultType = ResultType.Entity;
	}

	public void setValue(EntitySet value) {
		entitySet = value;
		object = value;
		resultType = ResultType.EntitySet;		
	}

	public ResultType getType() {
		return resultType;
	}

	public EntitySet getEntitySet() {
		return entitySet;
	}

	public Entity getEntity() {
		return entity;
	}

	public String getString() {
		return stringValue;
	}
	
	public int getInt() {
		return intValue;
	}
	
	public BigDecimal getBigDecimal() {
		return bigDecimal;
	}
	
	public Object getObject() {
		return object;
	}

}

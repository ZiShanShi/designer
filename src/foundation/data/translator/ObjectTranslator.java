package foundation.data.translator;

import java.math.BigDecimal;
import java.util.Date;

import foundation.data.Variant;

public class ObjectTranslator extends Translator {

	@Override
	public Object loadToObject(String value) throws Exception {
		return value;
	}

	@Override
	public Variant loadToVariant(String value) throws Exception {
		if (value == null) {
			return new Variant();
		}
		
		return new Variant(value);
	}
	
	@Override
	public String toString(Object value) throws Exception {
		if (value == null) {
			return null;
		}
		
		return value.toString();
	}

	@Override
	public String toSqlString(Object value) throws Exception {
		if (value == null) {
			return null;
		}
		
		if (value instanceof String) {
			return "'" + value + "'";
		}
		
		if (value instanceof Date) {
			return "'" + value + "'";
		}		
		
		return value.toString();
	}

	@Override
	public String toSqlString(String prefix, Object value, String suffix) throws Exception {
		if (value == null) {
			return null;
		}
		
		String result = value.toString();
		
		if (prefix != null) {
			result = result + prefix;
		}
		
		if (suffix != null) {
			result = result + suffix;
		}
		
		if (value instanceof String) {
			return "'" + result + "'";
		}
		
		if (value instanceof Date) {
			return "'" + result + "'";
		}	
		
		return result;
	}

	@Override
	public String toJSONString(Object value) throws Exception {
		if (value == null) {
			return "null";
		}
		
		return "\"" + value.toString() + "\"";		
	}

	@Override
	public Double toDouble(Object object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal toBigDecimal(Object object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean toBoolean(Object object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date toDate(Object object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object toSelfType(Object value) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Integer toInteger(Object object) throws Exception {
		if (object == null) {
			return 0;
		}
		
		if (object instanceof Integer) {
			return (Integer)object;
		}
		
		if (object instanceof Double) {
			return ((Double)object).intValue();
		}
		
		if (object instanceof Float) {
			return ((Float)object).intValue();
		}
		
		if (object instanceof Long) {
			return ((Long)object).intValue();
		}
		
		if (object instanceof BigDecimal) {
			return ((BigDecimal)object).intValue();
		}
		
		if (object instanceof String) {
			return Integer.valueOf((String)object);
		}
		
		return Integer.valueOf(String.valueOf(object));
	}
	
	public String getString(Object object) {
		if (object == null) {
			return null;
		}
		
		if (object instanceof String) {
			return (String)object;
		}
		
		return String.valueOf(object);
	}

	public BigDecimal getBigDecimal(Object object) {
		if (object == null) {
			return null;
		}
		
		if (object instanceof BigDecimal) {
			return ((BigDecimal)object);
		}
		
		if (object instanceof Long) {
			BigDecimal value = new BigDecimal((Long)object);
			return value;
		}
		
		if (object instanceof String) {
			BigDecimal value = new BigDecimal((String)object);
			return value;
		}
		
		return (BigDecimal)object;
	}

}

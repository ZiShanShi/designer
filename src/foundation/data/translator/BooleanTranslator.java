package foundation.data.translator;

import java.math.BigDecimal;
import java.util.Date;

import foundation.data.DataType;
import foundation.data.Variant;


public class BooleanTranslator extends Translator {

	
	public Object loadToObject(String value) throws Exception {
		if (value == null) {
			return false;
		}
		
		value = value.toLowerCase();
		
		if ("t".equals(value)) {
			return true;
		}
		
		if ("true".equals(value)) {
			return true;
		}
		
		if ("y".equals(value)) {
			return true;
		}
		
		if ("yes".equals(value)) {
			return true;
		}
		
		return false;
	}
	
	public Variant loadToVariant(String value) throws Exception {
		Object object = loadToObject(value);
		
		Variant variant = new Variant();
		variant.setValue(object, DataType.Boolean);
		
		return variant;
	}
	
	public String toString(Object value) throws Exception {
		if (value != null) {
			Boolean bool = (Boolean) value;
			return String.valueOf(bool);
		}
		
		return null;
	}

	public String toSqlString(Object value) throws Exception {
		if (value != null) {
			Boolean bool = (Boolean) value;
			
			if (bool) {
				return "T";
			}
			else {
				return "F";
			}
		}
		
		return null;
	}
	
	public String toSqlString(String prefix, Object value, String suffix) throws Exception	{
		return toSqlString(value);
	}
	
	public String toJSONString(Object value) throws Exception {
		if (value != null) {
			Boolean bool = (Boolean) value;
			return String.valueOf(bool);
		}
		
		return "null";
	}

	@Override
	public Integer toInteger(Object object) throws Exception {
		throw new Exception("can not translate boolean (" + object + ") to Integer");
	}

	@Override
	public Double toDouble(Object object) throws Exception {
		throw new Exception("can not translate boolean (" + object + ") to Double");
	}

	@Override
	public BigDecimal toBigDecimal(Object object) throws Exception {
		throw new Exception("can not translate boolean (" + object + ") to BigDecimal");
	}
	
	public Boolean toBoolean(Object object) throws Exception {
		return (Boolean)object;
	}

	public Date toDate(Object object) throws Exception {
		throw new Exception("can not translate boolean (" + object + ") to Date");
	}

	public Object toSelfType(Object value) throws Exception {
		return toBoolean(value);
	}

}

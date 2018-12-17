package foundation.data.translator;

import java.math.BigDecimal;
import java.util.Date;

import foundation.data.Variant;
import foundation.util.Util;

public class BigDecimalTranslator extends Translator {

	public Object loadToObject(String value) throws Exception {
		if (Util.isEmptyStr(value)) {
			return null;
		}
		
		return BigDecimal.valueOf(Double.valueOf(value));
	}
	
	public Variant loadToVariant(String value) throws Exception {
		if (value == null) {
			return new Variant();
		}
		
		BigDecimal result = BigDecimal.valueOf(Double.valueOf(value));
		return new Variant(result);
	}

	public String toString(Object value) throws Exception {
		if (value == null) {
			return null;
		}
		
		return value.toString();
	}

	public String toSqlString(Object value) throws Exception {
		return String.valueOf(value);
	}
	
	public String toSqlString(String prefix, Object value, String suffix) throws Exception	{
		return toSqlString(value);
	}

	public String toJSONString(Object value) throws Exception {
		if (value == null) {
			return "null";
		}
		
		return value.toString();
	}

	@Override
	public Integer toInteger(Object object) throws Exception {
		if (object == null) {
			return 0;
		}

		BigDecimal value = (BigDecimal)object;
		return value.intValue();
	}

	@Override
	public Double toDouble(Object object) throws Exception {
		if (object == null) {
			return 0.0;
		}
		
		BigDecimal value = (BigDecimal)object;
		return value.doubleValue();
	}

	@Override
	public BigDecimal toBigDecimal(Object object) throws Exception {
		if (object == null) {
			return BigDecimal.valueOf(0);
		}
		
		return (BigDecimal)object;
	}
	
	@Override
	public Boolean toBoolean(Object object) throws Exception {
		BigDecimal value = toBigDecimal(object);
		return !value.equals(0);
	}

	@Override
	public Date toDate(Object object) throws Exception {
		return null;
	}

	@Override
	public Object toSelfType(Object value) throws Exception {
		return toBigDecimal(value);
	}

}

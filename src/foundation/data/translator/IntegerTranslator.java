package foundation.data.translator;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import foundation.data.Variant;

public class IntegerTranslator extends Translator {

	
	public Object loadToObject(String value) throws Exception {
		return Integer.valueOf(value);
	}

	public Variant loadToVariant(String value) throws Exception {
		if (value == null) {
			return new Variant();
		}
		
		Integer result = Integer.valueOf(value);
		
		return new Variant(result);
	}
	
	public String toString(Object value) throws Exception {
		if (value == null) {
			return null;
		}
		
		return value.toString();
	}
	
	public String toSqlString(Object value) throws Exception {
		if (value == null) {
			return null;
		}
		
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
		
		return (Integer)object;
	}

	@Override
	public Double toDouble(Object object) throws Exception {
		if (object == null) {
			return 0.0;
		}
		
		return Double.valueOf((Integer)object);
	}

	@Override
	public BigDecimal toBigDecimal(Object object) throws Exception {
		if (object == null) {
			return BigDecimal.valueOf(0);
		}
		
		return BigDecimal.valueOf((Integer)object);
	}
	
	@Override
	public Boolean toBoolean(Object object) throws Exception {
		Integer value = toInteger(object);
		return value != 0;
	}

	@Override
	public Date toDate(Object object) throws Exception {
		Integer value = toInteger(object);
		
		if (value >= 19500101 && value <= 21000101) {
			String string = String.valueOf(value);
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			return dateFormat.parse(string);
		}
		else if (value > (1950 - 1900) * 365 && value < (2100 - 1900) * 365) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, 1900);
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.add(Calendar.DATE, value - 2);
			
			return calendar.getTime();
		}
		
		return null;
	}

	@Override
	public Object toSelfType(Object value) throws Exception {
		return toInteger(value);
	}

}

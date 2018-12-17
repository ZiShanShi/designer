package foundation.data.translator;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import foundation.data.Variant;
import foundation.util.Util;

public class DoubleTranslator extends Translator {

	public Object loadToObject(String value) throws Exception {
		if (Util.isEmptyStr(value)) {
			return null;
		}
		
		return Double.valueOf(value);
	}
	
	public Variant loadToVariant(String value) throws Exception {
		if (value == null) {
			return new Variant();
		}
		
		Double result = Double.valueOf(value);
		
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
		
		Double value = (Double)object;
		return value.intValue();
	}

	@Override
	public Double toDouble(Object object) throws Exception {
		if (object == null) {
			return 0.0;
		}
		
		return (Double)object;
	}

	@Override
	public BigDecimal toBigDecimal(Object object) throws Exception {
		if (object == null) {
			return BigDecimal.valueOf(0);
		}
		
		if (object instanceof BigDecimal) {
			return (BigDecimal)object;
		}
		
		return BigDecimal.valueOf((Double)object);
	}
	
	@Override
	public Boolean toBoolean(Object object) throws Exception {
		Double value = toDouble(object);
		return value != 0;
	}

	@Override
	public Date toDate(Object object) throws Exception {
		Integer value = toInteger(object);
		
		if (value >= 19500101 && value <= 21000101) {
			String string = String.valueOf(value.intValue());
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			return dateFormat.parse(string);
		}
		else if (value > (1950 - 1900) * 365 && value < (2100 - 1900) * 365) {
			int dateValue = value.intValue();
			double secValue = value - dateValue;
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, 1900);
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.add(Calendar.DATE, value - 2);
			
			Date dayDate = calendar.getTime();
			long sec = Math.round(secValue * 24 * 3600 * 1000);
			
			Date result = new Date();
			result.setTime(dayDate.getTime() + sec);
			return result;
		}
		
		return null;
	}

	@Override
	public Object toSelfType(Object value) throws Exception {
		return toDouble(value);
	}

}

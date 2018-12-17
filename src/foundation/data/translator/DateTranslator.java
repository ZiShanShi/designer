package foundation.data.translator;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import foundation.data.Variant;
import foundation.util.Util;

public class DateTranslator extends Translator {

	private static String DefaultFormat = "yyyy-MM-dd HH:mm:ss";
			
	
	public Object loadToObject(String value) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(value);
	}
	
	public Variant loadToVariant(String value) throws Exception {
		if (value == null) {
			return new Variant();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = sdf.parse(value);
		
		return new Variant(date);
	}

	public String toString(Object value) throws Exception {
		if (value == null) {
			return null;
		}
		
		if (value instanceof Date) {
			Date date = (Date)value;
			
			return Util.DataTimeToString(date, format);
		}
		
		return value.toString();
	}
	
	public String toSqlString(Object value) throws Exception {
		Date date = new Date();
		return Util.newDBDateString(date);
	}
	
	public String toSqlString(String prefix, Object value, String suffix) throws Exception	{
		return toSqlString(value);
	}

	public String toJSONString(Object value) throws Exception {
		if (value == null) {
			return "null";
		}
		
		if (value instanceof Date) {
			Date date = (Date)value;
			
			return "\"" + Util.DataTimeToString(date, format) + "\"";
		}
		
		return "\"" + value.toString() + "\"";
	}
	
	public void setFormat(String format) {
		if (Util.isEmptyStr(format)) {
			this.format = DefaultFormat;
		}
		else {
			this.format = format;
		}
	}

	@Override
	public Integer toInteger(Object object) throws Exception {
		throw new Exception("can not translate date (" + object + ") to Integer");
	}

	@Override
	public Double toDouble(Object object) throws Exception {
		throw new Exception("can not translate date (" + object + ") to Double");
	}

	@Override
	public BigDecimal toBigDecimal(Object object) throws Exception {
		throw new Exception("can not translate date (" + object + ") to BigDecimal");
	}
	
	@Override
	public Boolean toBoolean(Object object) throws Exception {
		throw new Exception("can not translate date (" + object + ") to BigDecimal");
	}

	@Override
	public Date toDate(Object object) throws Exception {
		return (Date) object;
	}

	@Override
	public Object toSelfType(Object value) throws Exception {
		return toDate(value);
	}

}

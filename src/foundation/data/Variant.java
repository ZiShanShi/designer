package foundation.data;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.apache.log4j.Logger;

import foundation.util.Util;

public class Variant {

	private Object value;
	private DataType type;
	private static Logger logger;
	
	
	static {
		logger = Logger.getLogger(Variant.class);
	}
	
	public Variant() {
		value = null;
	}
	public Variant(Object object) {
		value = object;
		type = DataType.String;
	}
	
	public Variant(Boolean value) {
		setValue(value);
	}
	
	public Variant(Integer value) {
		setValue(value);
	}
	
	public Variant(Double value) {
		setValue(value);
	}
	
	public Variant(BigDecimal value) {
		setValue(value);
	}
	
	public Variant(Date value) {
		setValue(value);
	}
	
	public Variant(String value) {
		setValue(value);
	}

	public void setValue(Boolean value) {
		this.value = value;
		type = DataType.Integer;		
	}
	
	public void setValue(Integer value) {
		this.value = value;
		type = DataType.Integer;
	}

	public void setValue(Double value) {
		this.value = value;
		type = DataType.Double;
	}
	
	public void setValue(BigDecimal value) {
		this.value = value;
		type = DataType.Decimal;
	}	

	public void setValue(Date value) {
		this.value = value;
		type = DataType.Date;
	}

	public void setValue(String value) {
		if ("null".equalsIgnoreCase(value)) {
			this.value = null;
		}
		else {
			this.value = value;				
		}
	
		type = DataType.String;		
	}

	public void setValue(Object value, DataType type) {
		this.value = value;
		this.type = type;		
	}
	
	public DataType getType() {
		return type;
	}

	public Integer getIntValue() {
		Integer result = null;
		
		if (value != null) {
			Class<?> clazz = value.getClass();
			
			if (clazz == Integer.class) {
				result = (Integer)value;
			}
			else if (clazz == BigDecimal.class) {
				result = ((BigDecimal)value).intValue();				
			}
			else if (clazz == String.class) {
				result = Integer.parseInt((String)value);					
			}
			else {
				String value_str = String.valueOf(value);
				result = Integer.parseInt(value_str);
			}
		}
		else {
			result = 0;
		}
		
		return result;
	}
	
	public int getIntValue(int defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		
		Integer result = getIntValue();
		
		if (result == null) {
			return defaultValue;
		}
		
		return result;
	}	
	
	public Double getDoubleValue() {
		if (type == DataType.Integer) {
			return Double.valueOf((Integer)value);
		}
		else {
			try {
				return (Double)value;				
			}
			catch(Exception e) {
				return Double.valueOf(String.valueOf(value));				
			}
		}
	}
	
	public BigDecimal getBigDecimalValue() {
		if (type == DataType.Integer) {
			return BigDecimal.valueOf((Integer)value);
		}
		else {
			try {
				return BigDecimal.valueOf((Double)value);				
			}
			catch(Exception e) {
				return BigDecimal.valueOf(Double.valueOf(String.valueOf(value)));			
			}
		}
	}
	
	public Long getLongValue() {
		return (Long) value;
	}

	public Date getDateValue() throws ParseException {
		Date result = null;
		
		if (DataType.Date == type) {
			result = (Date)value;
		}
		else if (DataType.String == type) {
			result = Util.StringToDate(value.toString());
		}
		else if (DataType.Integer == type) {
			logger.error("can not parse int to date");
		}
		else if (DataType.Double == type) {
			logger.error("can not parse double to date");			
		}
		
		return result;
	}
	
	public java.sql.Timestamp getSqlDateValue() {
		return new java.sql.Timestamp(((Date)value).getTime());
	}	

	public String getStringValue() {
		String result = null;
		
		if (value != null) {
			if (type == DataType.Date) {
				result = Util.DataTimeToString((Date)value); 
			}
			else {
				result = value.toString();
				if (result.length() == 1 && result.equalsIgnoreCase("T")) {
					result = String.valueOf(Util.stringToBoolean(result));
				}				
			}
		}
		
		return result;
	}
	
	public String getStringValue(String defaultValue) {
		if (value != null) {
			if (DataType.Date == type) {
				String result = value.toString();
				if (result.length() > 15) {
					return result.substring(0, 16);
				}
			}

			return value.toString();
		}
		else {
			return defaultValue;
		}
	}
	
	public boolean getBooleanValue() {
		String str = getStringValue();
		
		boolean result = "T".equalsIgnoreCase(str);
		result = result || "True".equalsIgnoreCase(str);
		result = result || "Y".equalsIgnoreCase(str);
		result = result || "Yes".equalsIgnoreCase(str);		
		
		return result;
	}
	
	public boolean getBooleanValue(boolean defaultValue) {
		String str = getStringValue();
		
		if (Util.isEmptyStr(str)) {
			return defaultValue;
		}
		
		boolean result = "T".equalsIgnoreCase(str);
		result = result || "True".equalsIgnoreCase(str);
		result = result || "Y".equalsIgnoreCase(str);
		result = result || "Yes".equalsIgnoreCase(str);		
		
		return result;
	}
	
	public Object getValue() {
		return value;
	}

	public boolean isNull() {
		return value == null;
	}
	
	public boolean isEmpty() {
		String str = getStringValue();
		return Util.isEmptyStr(str);
	}
	
	public void setNull() {
		value = null;
	}	
	
	public void clear() {
		value = null;
	}

	public String toString() {
		return getStringValue();
	}
	
}

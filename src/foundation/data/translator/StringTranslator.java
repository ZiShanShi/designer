package foundation.data.translator;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import foundation.data.Variant;

public class StringTranslator extends Translator {

	
	public Object loadToObject(String value) throws Exception {
		return value;
	}

	public Variant loadToVariant(String value) throws Exception {
		if (value == null) {
			return new Variant();
		}
		
		return new Variant(value);
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
		
		return "'" + value + "'";
	}

	public String toSqlString(String prefix, Object value, String suffix) throws Exception	{
		if (value == null) {
			return null;
		}
		
		String result = "";
		
		if (prefix != null) {
			result = result + prefix;
		}
		
		result = result + value;
		
		if (suffix != null) {
			result = result + suffix;
		}
		
		return "'" + result + "'";
	}
	
	public String toJSONString(Object value) throws Exception {
		if (value == null) {
			return "null";
		}
		
		return "\"" + value.toString() + "\"";
	}

	@Override
	public Integer toInteger(Object object) throws Exception {
		if (object == null) {
			return 0;
		}
		
		return Integer.valueOf(String.valueOf(object));
	}

	@Override
	public Double toDouble(Object object) throws Exception {
		if (object == null) {
			return 0.0;
		}
		
		return Double.valueOf(String.valueOf(object));
	}

	@Override
	public BigDecimal toBigDecimal(Object object) throws Exception {
		if (object == null) {
			return BigDecimal.valueOf(0);
		}
		
		return BigDecimal.valueOf(Double.valueOf(String.valueOf(object)));
	}
	
	@Override
	public Boolean toBoolean(Object object) throws Exception {
		if (object == null) {
			return false;
		}
		
		String value = String.valueOf(object).toLowerCase();
		
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

	@Override
	public Date toDate(Object object) throws Exception {
		if (object == null) {
			return null;
		}
		
		String fomater = null;
		String string = String.valueOf(object).replace('T', ' ');
		
		if (string.indexOf("/") == 4) {
			fomater = "yyyy/MM/dd";
		}
		else if (string.indexOf("/") == 2 || string.indexOf("/") == 1) {
			fomater = "MM/dd/yyyy";			
		}
		else if (string.indexOf("-") == 2 || string.indexOf("-") == 1) {
			fomater = "MM-dd-yyyy";		
		}
		else if (string.indexOf("-") == 4 && string.indexOf(":")<0) {
			fomater = "yyyy-MM-dd";				
		}
		else if (string.indexOf("-") == 4  && string.indexOf(":")>0) {
			if(string.split(":").length == 3){
			   fomater = "yyyy-MM-dd HH:mm:ss";
			}
			else{
				string = string + ":00";
				fomater = "yyyy-MM-dd HH:mm:00";
			}
						
		}		
		else if (string.indexOf(".") == 2 || string.indexOf(".") == 1) {
			fomater = "MM.dd.yyyy";	
		}
		else if (string.indexOf(".") == 4) {
			fomater = "yyyy.MM.dd";		
		}
		else if (string.indexOf("-") < 0 && string.indexOf("/") < 0) {
			fomater = "yyyyMMdd";
		}

		DateFormat dateFormat = new SimpleDateFormat(fomater);
		return dateFormat.parse(string);
	}

	@Override
	public Object toSelfType(Object value) throws Exception {
		return toString(value);
	}

}

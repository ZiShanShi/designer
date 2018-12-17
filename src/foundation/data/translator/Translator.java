package foundation.data.translator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import foundation.data.DataType;
import foundation.data.Variant;

public abstract class Translator {
	
	protected String format;
	private static Map<Class<?>, Class<? extends Translator>> classTranslatorMap;
	private static Map<DataType, Class<? extends Translator>> dataTypeTranslatorMap;
	
	
	static {
		initClassTranslatorMap();
		initDataTypeTranslatorMap();
	}
	
	public static Translator getInstance(Class<?> type) throws Exception {
		return getInstance(type, null);
	}
	
	public static Translator getInstance(Class<?> type, String format) throws Exception {
		Translator result = null;
		
		Class<? extends Translator> translatorClass = classTranslatorMap.get(type);
		
		if (translatorClass == null) {
			throw new Exception("can not create translator for: " + type);
		}

		result = translatorClass.newInstance();
		result.setFormat(format);
		
		return result;
	}
	
	public static Translator getInstance(DataType type, String format) throws Exception {
		Translator result = null;
		
		Class<? extends Translator> translatorClass = dataTypeTranslatorMap.get(type);
		
		if (translatorClass == null) {
			throw new Exception("can not create translator for: " + type);
		}

		result = translatorClass.newInstance();
		result.setFormat(format);
		
		return result;
	}
	
	public static Translator getInstance(DataType type) throws Exception {
		return getInstance(type, null);
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public String getFormat() {
		return format;
	}

	public abstract Object loadToObject(String value) throws Exception;
	
	public abstract Variant loadToVariant(String value) throws Exception;
	
	public abstract String toString(Object value) throws Exception;
	
	public abstract String toSqlString(Object value) throws Exception;
	
	public abstract String toSqlString(String prefix, Object value, String suffix) throws Exception;	

	public abstract String toJSONString(Object value) throws Exception;
	
	public abstract Integer toInteger(Object object) throws Exception;

	public abstract Double toDouble(Object object) throws Exception;

	public abstract BigDecimal toBigDecimal(Object object) throws Exception;
	
	public abstract Boolean toBoolean(Object object) throws Exception;

	public abstract Date toDate(Object object) throws Exception;

	public abstract Object toSelfType(Object value) throws Exception;
	
	
	private static void initClassTranslatorMap() {
		classTranslatorMap = new HashMap<Class<?>, Class<? extends Translator>>();
		
		classTranslatorMap.put(String.class, StringTranslator.class);
		classTranslatorMap.put(Integer.class, IntegerTranslator.class);
		classTranslatorMap.put(int.class, IntegerTranslator.class);
		classTranslatorMap.put(BigInteger.class, IntegerTranslator.class);
		classTranslatorMap.put(Double.class, DoubleTranslator.class);
		classTranslatorMap.put(double.class, DoubleTranslator.class);
		classTranslatorMap.put(Float.class, DoubleTranslator.class);
		classTranslatorMap.put(float.class, DoubleTranslator.class);
		classTranslatorMap.put(BigDecimal.class, DoubleTranslator.class);
		classTranslatorMap.put(Date.class, DateTranslator.class);
		classTranslatorMap.put(Boolean.class, BooleanTranslator.class);
		classTranslatorMap.put(boolean.class, BooleanTranslator.class);
		classTranslatorMap.put(Object.class, ObjectTranslator.class);
		classTranslatorMap.put(Long.class, LongTranslator.class);
	}

	private static void initDataTypeTranslatorMap() {
		dataTypeTranslatorMap = new HashMap<DataType, Class<? extends Translator>>();
		
		dataTypeTranslatorMap.put(DataType.String, StringTranslator.class);
		dataTypeTranslatorMap.put(DataType.Integer, IntegerTranslator.class);
		dataTypeTranslatorMap.put(DataType.Double, DoubleTranslator.class);
		dataTypeTranslatorMap.put(DataType.Date, DateTranslator.class);
		dataTypeTranslatorMap.put(DataType.Boolean, BooleanTranslator.class);
		
		
	}

	public static boolean containsType(Class<?> type) {
		return classTranslatorMap.containsKey(type);
	}

}

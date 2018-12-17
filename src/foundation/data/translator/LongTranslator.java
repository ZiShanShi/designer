package foundation.data.translator;

import java.math.BigDecimal;
import java.util.Date;

import foundation.data.Variant;
import foundation.util.Util;

public class LongTranslator extends Translator {

	@Override
	public Object loadToObject(String value) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Variant loadToVariant(String value) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString(Object value) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toSqlString(Object value) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toSqlString(String prefix, Object value, String suffix) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toJSONString(Object value) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer toInteger(Object object) throws Exception {
		Number _object = (Number)object;
		return _object.intValue();
	}

	@Override
	public Double toDouble(Object object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal toBigDecimal(Object object) throws Exception {
		return Util.parseBigDecimal(object);
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

}

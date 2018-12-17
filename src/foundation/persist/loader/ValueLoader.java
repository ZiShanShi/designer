package foundation.persist.loader;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Date;

import foundation.data.translator.Translator;
import foundation.persist.sql.ILoadable;


public class ValueLoader implements ILoadable {

	private Object object;
	
	public ValueLoader() {
		
	}

	@Override
	public void load(ResultSet rslt, Object... args) throws Exception {
		if (rslt.next()) {
			object = rslt.getObject(1);
		}		
	}
	
	public int getInt() throws Exception {
		Translator translator = Translator.getInstance(object.getClass());
		return translator.toInteger(object);
	}
	
	public String getString() throws Exception {
		Translator translator = Translator.getInstance(object.getClass());
		return translator.toString(object);		
	}

	public BigDecimal getBigDecimal() throws Exception {
		Translator translator = Translator.getInstance(object.getClass());
		return translator.toBigDecimal(object);			
	}
	
	public Date getDate() throws Exception {
		Translator translator = Translator.getInstance(object.getClass());
		return translator.toDate(object);			
	}

	public Object getObject() {
		return object;
	}

}

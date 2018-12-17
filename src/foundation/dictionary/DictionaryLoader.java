package foundation.dictionary;

import java.sql.ResultSet;

import foundation.persist.sql.ILoadable;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLRunner;



public class DictionaryLoader implements ILoadable {

	public static String SQLName_LoadOneDictionary = "loadOneDictionary"; 
	
	private static DictionaryLoader instance;
	private static DictionaryContainer container;
	
	private DictionaryLoader() {
		container = DictionaryContainer.getInstance();
	}
	
	public static synchronized DictionaryLoader getInstance() {
		if (instance == null) {
			instance = new DictionaryLoader();
		}
		
		return instance;
	}
	
	public Dictionary load(IDictionaryDefination defination) throws Exception {
		String code = defination.getCode();
		Dictionary dictionary = new Dictionary(code);
		loadOneDictionary(dictionary, defination);
		
		return dictionary;
	}
	
	public void refresh(IDictionaryDefination defination) throws Exception {
		String code = defination.getCode();
		Dictionary dictionary =  container.get(code);
		
		if (dictionary == null) {
			dictionary = container.append(code);
			loadOneDictionary(dictionary, defination);
			return;
		}
		
		if (dictionary.isDirty()) {
			loadOneDictionary(dictionary, defination);
			return;
		}
	}

	private void loadOneDictionary(Dictionary dictionary, IDictionaryDefination defination) throws Exception {
		String tableName = defination.getTableName();
		
		if (!SQLRunner.isTableExists(tableName)) {
			return;
		}
		
		String selectFieldNames = defination.getSelectFieldNames();
		String filter = defination.getFilter();
		
		
		NamedSQL namedSQL = NamedSQL.getInstance(SQLName_LoadOneDictionary);
		namedSQL.setTableName(tableName);
		namedSQL.setFieldNames(selectFieldNames);
		namedSQL.setFilter(filter);
		
		SQLRunner.getData(namedSQL, this, dictionary);
	}

	public void load(ResultSet rslt, Object ...args) throws Exception {
		Dictionary dictionary = (Dictionary)args[0];
		dictionary.clear();
		
		String code;
		String value;
		
		while (rslt.next()) {
			code = rslt.getString(1);
			value = rslt.getString(2);
			
			dictionary.append(code, value);
		}
	}

}

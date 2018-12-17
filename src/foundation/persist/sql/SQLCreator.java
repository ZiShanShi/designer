package foundation.persist.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import foundation.util.Util;

public class SQLCreator implements Iterable<SQLVariant> {
	
	protected static Logger logger;
	private List<SQLSegment> segments;
	private Map<String, SQLVariant> variantMap;
	private List<SQLVariant> variantList;

	
	static {
		logger = Logger.getLogger(SQLCreator.class);
	}
	
	public SQLCreator(int size) {
		if (size <= 0) {
			size = 4;
		}
		
		segments = new ArrayList<SQLSegment>(size);
		variantMap = new HashMap<String, SQLVariant>();
		variantList = new ArrayList<SQLVariant>();
	}

	public SQLCreator newInstance() {
		int size = segments.size();
		SQLCreator instance = new SQLCreator(size);
		
		SQLSegment segment;
		SQLVariant sqlVariant;
		
		for (int i = 0; i < size; i++) {
			segment = segments.get(i);
			
			if (segment instanceof SQLVariant) {
				sqlVariant = (SQLVariant) segment;
				String lower = sqlVariant.getName().toLowerCase();
				
				if (instance.variantMap.containsKey(lower)) {
					instance.segments.add(instance.variantMap.get(lower));
				}
				else {
					segment = segment.newInstance();
					instance.segments.add(segment);
					instance.variantMap.put(lower, (SQLVariant) segment);
					instance.variantList.add((SQLVariant) segment);
				}
			}
			else {
				segment = segment.newInstance();
				instance.segments.add(segment);
			}
		}
		
		return instance;
	}
	
	public String getSql() {
		StringBuilder result = new StringBuilder();
		
		int n = segments.size();
		SQLSegment segment;
		String value;
		
		for (int i = 0; i < n; i++) {
			segment = segments.get(i);
			value = segment.getValueString();
			
			result.append(value);
		}
		
		return result.toString();
	}
	
	public void addSQLString(String value) {
		if (Util.isEmptyStr(value)) {
			return;
		}
		
		SQLSegment segment = new SQLString(value);
		segments.add(segment);
	}

	public void addSqlVariant(String name) throws Exception {
		if (Util.isEmptyStr(name)) {
			return;
		}
		
		String lower = name.toLowerCase();
		
		if (variantMap.containsKey(lower)) {
			SQLVariant segment = variantMap.get(lower);
			segments.add(segment);
		}
		else {
			SQLVariant segment = new SQLVariant(name);
			segments.add(segment);
			variantMap.put(lower, segment);
			variantList.add(segment);			
		}
	}
	
	public SQLVariant getVariant(String name) {
		if (Util.isEmptyStr(name)) {
			return null;
		}
		
		return variantMap.get(name.toLowerCase());
	}

	public Iterator<SQLVariant> iterator() {
		return variantList.iterator();
	}

	public Map<String, SQLVariant> getVariantMap() {
		return variantMap;
	}

	public List<SQLVariant> getVariantList() {
		return variantList;
	}

}

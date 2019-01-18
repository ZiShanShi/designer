package bi.agg.ranking;

import java.util.ArrayList;
import java.util.List;

import foundation.persist.sql.ISQLString;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLParameterSetter;
import foundation.util.ContentBuilder;

public class RankingFields implements ISQLString {

	private List<RankingField> items;
	
	public RankingFields() {
		items = new ArrayList<RankingField>();
	}

	public void setParametersTo(NamedSQL namedSQL) {
		SQLParameterSetter setter = new SQLParameterSetter(this);
		setter.setTo(namedSQL);
	}
	
	@Override
	public String getSqlString(String name) {
		if ("destfields".equalsIgnoreCase(name)) {
			return getDestFields();
		}
		
		return null;
	}
	
	private String getDestFields() {
		ContentBuilder result = new ContentBuilder(", ");
		
		for (RankingField rankingField: items) {
			result.append(rankingField.getDestField());
		}
		
		return result.toString();
	}

	public void add(RankingField rankingField) {
		items.add(rankingField);
	}

	public int size() {
		return items.size();
	}

	public RankingField get(int idx) {
		return items.get(idx);
	}


	
}

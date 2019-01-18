package bi.agg.ranking;

import java.math.BigDecimal;

public class DataItem {

	private String id;
	private SortItem[] sortItems;
	
	public DataItem(String id, int size) {
		this.sortItems = new SortItem[size];
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public SortItem appendValue(int i, BigDecimal value) {
		SortItem result = new SortItem(this, value);
		sortItems[i] = result;
		
		return result;
	}

	public SortItem getSortItem(int idx) {
		return sortItems[idx];
	}

}

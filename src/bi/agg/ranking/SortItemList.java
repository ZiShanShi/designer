package bi.agg.ranking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortItemList {

	private List<SortItem> items;
	private SortItem[] itemAray; 
	private boolean valueEmpty;
	
	
	public SortItemList() {
		items = new ArrayList<SortItem>();
		valueEmpty = true;
	}
	
	public SortItemList(int size) {
		items = new ArrayList<SortItem>(size);
		valueEmpty = true;
	}
	
	public void add(SortItem item) {
		items.add(item);
		
		if (valueEmpty) {
			valueEmpty = item.isValueEmpty();
		}
	}
	
	public void sort() {
		int cnt = items.size();
		itemAray = new SortItem[items.size()];
		
		for (int i = 0; i < cnt; i++) {
			itemAray[i] = items.get(i);
		}
		
		Arrays.sort(itemAray);
	}

	public void ranking() {
		long level = 0, current, ranking = 1, i = 0;

		for (SortItem item: itemAray) {
			i++;
			current = item.getValue();
			
			if (current < level) {
				ranking =  i;
			}
			
			item.setRanking((int)ranking);
			level = current;
		}		
	}

	public boolean isValueEmpty() {
		return valueEmpty;
	}
}

package bi.agg.data;

import java.util.ArrayList;
import java.util.List;

import foundation.util.ContentBuilder;


public class ValueItem {

	private List<String> valueList;
	
	public ValueItem() {
		valueList = new ArrayList<String>();
	}
	
	public int size() {
		return 0;
	}

	public void add(String value) {
		valueList.add(value);
	}

	public void onGetFilter(ContentBuilder builder) {
		for (String value: valueList) {
			builder.append(value);
		}
	}

}

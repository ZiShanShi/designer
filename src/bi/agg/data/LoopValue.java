package bi.agg.data;

import bi.agg.Defination;
import foundation.util.ContentBuilder;

public class LoopValue extends Loop<ValueItem> {

	private Dimension dimension;
	
	public LoopValue(Dimension dimension, Defination defination) {
		this.dimension = dimension;
		dimension.loadLoopValue(this, defination);
	}
	
	public String getName() {
		return dimension.getName();
	}

	public void onGetFilter(ContentBuilder builder) {
		ValueItem valueItem = getCurrent();
		
		if (valueItem != null) {
			valueItem.onGetFilter(builder);
		}
	}

}

package foundation.variant;

import foundation.data.MapList;

public class VariantList extends MapList<VariantSegment>{
	
	public void clearValues() {
		for (VariantSegment param: itemList) {
			param.clearValue();
		}
	}
}

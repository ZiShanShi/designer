package bi.agg.data;

import java.util.ArrayList;
import java.util.List;

import bi.agg.Defination;

import foundation.util.Util;

public class Hierarchy extends Dimension {

	private static List<Class<? extends DimensionCell>> classList;
	
	static {
		classList = new ArrayList<Class<? extends DimensionCell>>();
		classList.add(MR.class);
		classList.add(AM.class);
		classList.add(RD.class);
		classList.add(SD.class);
	}
	
	public Hierarchy() {
		this("sd", "mr");
	}
	
	public Hierarchy(String from, String to) {
		super("hierarchy");
		createCells(from, to);
	}

	@Override
	public void loadLoopValue(LoopValue loopValue, Defination defination) {
		DimensionCell cell = getCurrent();
		
		ValueItem item = new ValueItem();
		item.add("hierarchytype=" + Util.quotedStr(cell.getName()));
		loopValue.add(item);
	}
	
	private int getIndex(String name) {
		int idx = 0;
		
		for (Class<? extends DimensionCell> clazz: classList) {
			String current = clazz.getSimpleName();
			
			if (current.equalsIgnoreCase(name)) {
				return idx;
			}
			
			idx = idx + 1;
		}
		
		return -1;
	}
	
	@Override
	protected void init(String param) {
		super.init(param);
		
		if (Util.isEmptyStr(param)) {
			return;
		}
		
		items.clear();
		
		param = param.replace(" ", "").replace(",", ";").replace("，", ";").replace("；", ";");
		String[] paramArray = param.split(";");
		
		if (paramArray.length == 2) {
			createCells(paramArray[0], paramArray[1]);
		}
	}
	
	private void createCells(String from, String to) {
		try {
			int idx_from = getIndex(from);
			int idx_to = getIndex(to);
			
			if (idx_from >= idx_to) {
				int temp = idx_from;
				idx_from = idx_to;
				idx_to = temp;
			}
			
			for (int i = idx_from; i <= idx_to; i++) {
				Class<? extends DimensionCell> clazz = classList.get(i);
				DimensionCell cell;
				cell = clazz.newInstance();
				items.add(cell);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

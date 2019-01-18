package bi.agg;

import java.util.ArrayList;
import java.util.List;

import bi.agg.data.Period;
import bi.agg.data.PeriodType;

public class ParamSetterList {

	private List<Object> items;
	private Period period;
	private PeriodType periodType;
	
	
	public ParamSetterList(Object[] args) {
		items = new ArrayList<Object>();
		
		for (Object obj: args) {
			items.add(obj);
			
			if (obj instanceof Period) {
				period = (Period) obj;
			}
			else if (obj instanceof PeriodType) {
				periodType = (PeriodType) obj;
			}
		}
	}
	
	public Period getPeriod() {
		return period;
	}
	
	public PeriodType getPeriodType() {
		return periodType;
	}
}

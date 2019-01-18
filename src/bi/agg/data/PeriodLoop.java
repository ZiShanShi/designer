package bi.agg.data;

import bi.work.ActivePeriod;
import bi.work.WorkingCalendar;

public class PeriodLoop extends Loop<Period> {

	private PeriodLoop(boolean current, boolean prior) {
		ActivePeriod activePeriod = ActivePeriod.getInstance();
		int year = activePeriod.getYear();
		int month = activePeriod.getMonth();
				
		if (!WorkingCalendar.isSystemInit()) {
			appendLoop(year, month);
		}
		else {
			//1. append current year
			if (current) {
				for (int i = 1; i <= month; i++) {
					appendLoop(year, i);
				}				
			}

			//2. append previous year
			if (prior) {
				for (int i = 1; i <= 12; i++) {
					appendLoop(year - 1, i);
				}
			}
		}
	}
	
	public static PeriodLoop getInstance_Current_Prior_Year() {
		PeriodLoop result = new PeriodLoop(true, true);
		result.first();
		
		return result;
	}
	
	public static PeriodLoop getInstance_Current_Year() {
		PeriodLoop result = new PeriodLoop(true, false);
		result.first();
		
		return result;
	}
	
	public static PeriodLoop getInstance_Prior_Year() {
		PeriodLoop result = new PeriodLoop(false, true);
		result.first();
		
		return result;
	}
	

	public void appendLoop(int year, int month) {
		Period period = new Period(year, month);
		add(period);
	}
	
}

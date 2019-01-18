package bi.work;

import bi.agg.data.DatePoint;

public class WorkingCalendar {

	private static WorkingCalendar instance;
	private DatePoint begin;
	private DatePoint end;
	
	private WorkingCalendar() {
		ActivePeriod activePeriod = ActivePeriod.getInstance();
		begin = new DatePoint(activePeriod.getCalculateYear(), activePeriod.getCalculateMonth());
		end = new DatePoint(activePeriod.getYear(), activePeriod.getMonth());
	}
	
	public static synchronized WorkingCalendar getInstance() {
		if (instance == null) {
			instance = new WorkingCalendar();
		}
		
		return instance;
	}
	
	public static void setBegin(int year, int month) {
		getInstance().begin = new DatePoint(year, month);
	}
	
	public static void setEnd(int year, int month) {
		getInstance().end = new DatePoint(year, month);
	}

	public static DatePoint getBegin() {
		return getInstance().begin;
	}

	public static DatePoint getEnd() {
		return getInstance().end;
	}
	
	public static boolean isSystemInit() {
		return getInstance().begin.isEmpty();
	}

	public static String getSQLString() {
		DatePoint point = getEnd();
		
		if (isSystemInit()) {
			return " 1=1 ";
		}
		else {
			return " calendarTable.year = " + point.getYear() + " and calendarTable.month = " + point.getMonth() + " ";
		}
	}
	
}

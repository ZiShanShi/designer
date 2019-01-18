package bi.agg.data;

public class DatePoint {

	private int year;
	private int dmonth;
	private int month;
	private boolean empty;
	
	public DatePoint(Integer year, Integer month) {
		empty = true;
		if (year == null || month == null) {
			return;
		}
		
		this.year = year;
		this.dmonth = Double.valueOf(Math.ceil((month / 2.0))).intValue();
		this.month = month;
		empty = false;
	}

	public int getYear() {
		return year;
	}

	public int getDmonth() {
		return dmonth;
	}

	public int getMonth() {
		return month;
	}

	public boolean isEmpty() {
		return empty;
	}
}

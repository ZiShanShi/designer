package bi.agg.data;

import foundation.persist.sql.ISQLString;

public class Period implements ISQLString {

	private int year;
	private int month;
	private int dmonth;
	
	public Period(int year, int month) {
		this.year = year;
		this.month = month;
		this.dmonth = Double.valueOf(Math.ceil(month / 2.0)).intValue();
	}

	public String getWorkingName() {
		return "(" + year + ", " + month + ")";
	}

	@Override
	public String getSqlString(String name) {
		if ("year".equalsIgnoreCase(name) || "loopyear".equalsIgnoreCase(name)) {
			return String.valueOf(year);
		}
		else if ("month".equalsIgnoreCase(name) || "loopmonth".equalsIgnoreCase(name)) {
			return String.valueOf(month);
		}
		else if ("dmonth".equalsIgnoreCase(name) || "loopdmonth".equalsIgnoreCase(name)) {
			return String.valueOf(dmonth);
		}
		else if ("monthno".equalsIgnoreCase(name)) {
			int monthno = (year - 2015) + month;
			return String.valueOf(monthno);
		}		
		else if ("month_offset".equalsIgnoreCase(name)) {
			if (month <= 2) {
				return "0";
			}
			else {
				int value = (month % 2) == 0 ? 2 : 1;
				return String.valueOf(value);	
			}
		}
		
		return null;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDMonth() {
		return dmonth;
	}

	@Override
	public String toString() {
		return "(" + year + ", " + month + ")";
	}
	
}

package bi.agg.data;

import bi.agg.SQLParameter;
import foundation.persist.sql.ISQLString;
import foundation.util.ContentBuilder;

public class PeriodType extends Loop<PeriodTypeCell> implements ISQLString {

	private PeriodType() {
		
	}

	@Override
	public String getSqlString(String name) {
		if ("periodflag".equalsIgnoreCase(name)) {
			PeriodTypeCell cell = getCurrent();
			return cell.getShortName();
		}
		
		return null;
	}
	
	public void onGetFields(ContentBuilder builder, SQLParameter parameter, Period period) {
		String code = parameter.getCode();
		
		//1. year
		if (currentIs("Year")) {
			if ("group".equalsIgnoreCase(code)) {
				builder.append("year");
			}
			else if ("insert".equalsIgnoreCase(code)) {
				builder.append("year");
				builder.append("month");
			}
			else if ("select".equalsIgnoreCase(code)) {
				builder.append(period.getYear());
				builder.append(period.getMonth());
			}
			return;
		}
		
		//2. month
		if (currentIs("Month")) {
			if ("group".equalsIgnoreCase(code)) {
				builder.append("year");
				builder.append("month");
			}
			else if ("insert".equalsIgnoreCase(code)) {
				builder.append("year");
				builder.append("month");
			}
			else if ("select".equalsIgnoreCase(code)) {
				builder.append(period.getYear());
				builder.append(period.getMonth());
			}
			return;
		}

		//3. dmonth
		if (currentIs("DMonth")) {
			if ("group".equalsIgnoreCase(code)) {
				builder.append("year");
				builder.append("dmonth");
			}
			else if ("insert".equalsIgnoreCase(code)) {
				builder.append("year");
				builder.append("month");
			}
			else if ("select".equalsIgnoreCase(code)) {
				builder.append(period.getYear());
				builder.append(period.getMonth());
			}
			return;
		}
		
		//4. null
//		builder.append(period.getYear());
//		builder.append(period.getMonth());
	}
	
	public void onGetFilter(ContentBuilder builder, SQLParameter parameter, Period period) {
		String category = parameter.getCategory();
		
		//1. year
		if (currentIs("Year")) {
			if ("flat".equals(category)) {
				builder.append("year = " + period.getYear()).append("month <= " + period.getMonth());
			}
			else if ("agg".equals(category)) {
				builder.append("year = " + period.getYear());
			}
			else if ("ranking".equals(category)) {
				builder.append("year = " + period.getYear()).append("month = " + period.getMonth());
			}
			return;
		}
		
		//2. month
		if (currentIs("Month")) {
			if ("flat".equals(category)) {
				builder.append("year = " + period.getYear() + " and month = " + period.getMonth());
			}
			else if ("agg".equals(category)) {
				builder.append("year = " + period.getYear() + " and month = " + period.getMonth());
			}
			else if ("ranking".equals(category)) {
				builder.append("year = " + period.getYear()).append("month = " + period.getMonth());
			}
			return;
		}
		
		//3. dmonth
		if (currentIs("DMonth")) {
			if ("flat".equals(category)) {
				builder.append("year = " + period.getYear() + " and dmonth = " + period.getDMonth() + " and month <= " + period.getMonth());
			}
			else if ("agg".equals(category)) {
				builder.append("year = " + period.getYear() + " and dmonth = " + period.getDMonth());
			}
			else if ("ranking".equals(category)) {
				builder.append("year = " + period.getYear()).append("month = " + period.getMonth());
			}
			return;
		}
		
		//4. null
//		builder.append("year = " + period.getYear()).append("month = " + period.getMonth());
	}
	
	public boolean currentIs(String name) {
		if (isEmpty()) {
			return false;
		}
		
		PeriodTypeCell cell = getCurrent();
		return cell.getName().equalsIgnoreCase(name);
	}

	public static PeriodType getInstance_all() {
		PeriodType periodtype = new PeriodType();
		periodtype.add(new PeriodTypeCell("Year", "y_"));
		periodtype.add(new PeriodTypeCell("DMonth", "s_"));
		periodtype.add(new PeriodTypeCell("Month", "m_"));
		
		periodtype.first();
		
		return periodtype;
	}
	
	public static PeriodType getInstance_null() {
		PeriodType periodtype = new PeriodType();
		return periodtype;
	}
	

	public String getCurrentName() {
		if (isEmpty()) {
			return null;
		}
		
		PeriodTypeCell cell = getCurrent();
		return cell.getName();
	}

	@Override
	public String toString() {
		if (items.isEmpty()) {
			return "null";
		}
		
		PeriodTypeCell cell = getCurrent();
		return cell.getName();
	}


	
	
}

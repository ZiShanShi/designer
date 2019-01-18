package bi.work;

import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLRunner;

public class WorkManager {

	
	public static void nextPeriod() {

	}

	public static void setCalculatePeriod() throws Exception {
		int year = ActivePeriod.getInstance().getYear();
		int month = ActivePeriod.getInstance().getMonth();
		NamedSQL namedSQL = new NamedSQL("", "update workperiod set calyear=" + year + ", calmonth=" + month);
		namedSQL.exec();
	}

	public static void clearWorkStatus() {

	}

	public static void setWrokStatus(String dataname) throws Exception {
		setWrokStatus(dataname, null);
	}

	public static void setWrokStatus(String dataname, DataStatus status) throws Exception {
		if (status == null) {
			status = DataStatus.Changed;
		}

		NamedSQL namedSQL = NamedSQL.getInstance("setWorkstatus");
		namedSQL.setParam("statuscode", status.toString());
		namedSQL.setParam("dataname", dataname);
		
		SQLRunner.execSQL(namedSQL);
	}
}

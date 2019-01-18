package bi.agg;

public enum PeriodDivision {

	Whole, Part;
	
	
	public static PeriodDivision valueOfString(String value) {
		if ("Whole".equalsIgnoreCase(value)) {
			return Whole;
		}
		else if ("Part".equalsIgnoreCase(value)) {
			return Part;
		}
		
		return Part;
	}
}

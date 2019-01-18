package bi.agg.ranking;

import java.math.BigDecimal;

public class SortItem implements Comparable<SortItem> {

	private static BigDecimal unit;
	protected DataItem dataItem;
	private BigDecimal originalValue;
	private long value;
	private int ranking;

	static {
		unit = BigDecimal.valueOf(1000);
	}
	
	public SortItem(DataItem dataItem, BigDecimal value) {
		this.dataItem = dataItem;
		this.originalValue = value;
		this.value = 0;
		
		if (value != null) {
			this.value = value.multiply(unit).longValue();
		}
	}

	public long getValue() {
		return value;
	}

	public BigDecimal getOriginalValue() {
		return originalValue;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	@Override
	public int compareTo(SortItem other) {
		long result = other.value - this.value;
		
		if (result == 0) {
			return 0;
		}
		else if (result > 0) {
			return 1;
		}
		else {
			return -1;
		}
		
		//long thisVal = this.value;
		//long otherVal = other.value;
		//return (thisVal>otherVal ? -1 : (thisVal==otherVal ? 0 : 1));//逆序
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public boolean isValueEmpty() {
		return value == 0;
	}
	
}

package bi.agg.ranking;

import java.util.Comparator;

public class SortItemComparator implements Comparator<SortItem> {

	public SortItemComparator() {

	}

	@Override
	public int compare(SortItem one, SortItem another) {
		long result = one.getValue() - another.getValue();

		if (result == 0) {
			return 0;
		} else if (result > 0) {
			return 1;
		} else {
			return -1;
		}
	}

}

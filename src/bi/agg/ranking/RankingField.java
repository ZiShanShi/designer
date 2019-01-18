package bi.agg.ranking;

import bi.agg.Defination;

public class RankingField {

	private Defination defination;
	private String sourField;
	private String destField;
	
	public RankingField(Defination defination, String sour, String dest) {
		this.defination = defination;
		sourField = sour;
		destField = dest;
	}

	public String getSourField() {
		return sourField;
	}

	public String getDestField() {
		return destField;
	}

	public Defination getDefination() {
		return defination;
	}
	
}

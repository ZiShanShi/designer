package bi.agg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefinationGroup {

	private String aggobject;  // A || B
	private Map<String, Defination> rankingMap;   // key:  1
	private List<Defination> rankingList;
	private Map<String, Defination> aggregationMap; // key: 2
	private List<Defination> aggregationList;
	
	public DefinationGroup(String aggobject) {
		this.aggobject = aggobject;
		
		rankingMap = new HashMap<String, Defination>();
		rankingList = new ArrayList<Defination>();
		
		aggregationMap = new HashMap<String, Defination>();
		aggregationList = new ArrayList<Defination>();
	}

	public Defination getAggDefination(String name) {
		if (name == null) {
			return null;
		}
		
		name = name.toLowerCase();
		return aggregationMap.get(name);
	}

	public String getAggobject() {
		return aggobject;
	}

	public List<Defination> getAggregationList() {
		return aggregationList;
	}

	public List<Defination> getRankingList() {
		return rankingList;
	}

	public void appendRankingDefination(Defination defination) {
		String id = defination.getId();
		
		if (id == null) {
			return;
		}
		
		String lower = id.toLowerCase();
		rankingMap.put(lower, defination);
		rankingList.add(defination);		
	}

	public void appendAggDefination(Defination defination) {
		String id = defination.getId();
		
		if (id == null) {
			return;
		}
		
		String lower = id.toLowerCase();
		aggregationMap.put(lower, defination);
		aggregationList.add(defination);
	}
	
}

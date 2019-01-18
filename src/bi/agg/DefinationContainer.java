package bi.agg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DefinationContainer {

	private static DefinationContainer instance;
	private Map<String, DefinationGroup> groupMap;  //key:  A && B
	
	
	private DefinationContainer() {
		groupMap = new HashMap<String, DefinationGroup>();
	}
	
	public synchronized static DefinationContainer getInstance() {
		if (instance == null) {
			instance = new DefinationContainer();
		}
		
		return instance;
	}
	
	public Defination getAggDefination(String targetcode, String name) {
		DefinationGroup group = groupMap.get(targetcode);
		
		if (group == null) {
			return null;
		}
		
		return group.getAggDefination(name);
	}
	
	public void appendDefination(Defination defination) {
		String aggType = defination.getAggType();
		String aggObj = defination.getAggObj(); 
		
		if ("agg".equalsIgnoreCase(aggType)) {
			appendAggDefination(aggObj, defination);
		}
		else if ("ranking".equalsIgnoreCase(aggType)) {
			appendRankingDefination(aggObj, defination);
		}
	}

	public void appendAggDefination(String aggObj, Defination defination) {
		if (defination == null) {
			return;
		}
		
		DefinationGroup group = groupMap.get(aggObj);
		
		if (group == null) {
			group = new DefinationGroup(aggObj);
			groupMap.put(aggObj, group);
		}
		
		group.appendAggDefination(defination);
	}
	
	public void appendRankingDefination(String aggObj, Defination defination) {
		if (defination == null) {
			return;
		}
		
		DefinationGroup group = groupMap.get(aggObj);
		
		if (group == null) {
			group = new DefinationGroup(aggObj);
			groupMap.put(aggObj, group);
		}
		
		group.appendRankingDefination(defination);
	}

	public List<Defination> getAggregationList(String targetcode) {
		DefinationGroup group = groupMap.get(targetcode);
		
		if (group == null) {
			return null;
		}
		
		return group.getAggregationList();
	}

	public List<Defination> getRankingList(String targetcode) {
		DefinationGroup group = groupMap.get(targetcode);
		
		if (group == null) {
			return new ArrayList<Defination>();
		}
		
		return group.getRankingList();
	}
	
}

package bi.agg.ranking;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import foundation.persist.sql.ILoadable;
import foundation.persist.sql.ISavable;

public class RankingSorter implements ISavable, ILoadable {

	private List<DataItem> dataList;
	private RankingFields rankingFields; 
	private SortItemList[] sortItemLists;
	private int[] sourIndex;
	
	
	public RankingSorter(RankingFields rankingFields) {
		this.rankingFields = rankingFields;
		this.dataList = new ArrayList<DataItem>(1000);
		this.sortItemLists = new SortItemList[rankingFields.size()]; 
	}
	
	@Override
	public void load(ResultSet rslt, Object... args) throws Exception {
		String id; BigDecimal value; SortItem sortItem;
		
		init(rslt.getMetaData());
		int fieldCount = rankingFields.size();
		
		while (rslt.next()) {
			id = rslt.getString(1);
			
			DataItem dataItem = new DataItem(id, fieldCount);
			dataList.add(dataItem);
			
			for (int i = 0; i < sourIndex.length; i++) {
				value = rslt.getBigDecimal(sourIndex[i]);
				sortItem = dataItem.appendValue(i, value);
				sortItemLists[i].add(sortItem);
			}
		}
	}
	
	@Override
	public void save(PreparedStatement stmt, Object... agrs) throws Exception {
		SortItem sortItem;
		
		//1.
		int fieldCount = rankingFields.size();
		boolean[] empty = new boolean[fieldCount]; 
		
		for (int i = 0; i < fieldCount; i++) {
			empty[i] = sortItemLists[i].isValueEmpty();
		}
		
		//2.
		for (DataItem dataItem: dataList) {
			
			//2.1
			for (int i = 0; i < fieldCount; i++) {
				if (empty[i]) {
					stmt.setNull(i + 1, Types.INTEGER);
				}
				else {
					sortItem = dataItem.getSortItem(i);
					stmt.setInt(i + 1, sortItem.getRanking());
				}
			}
			
			//2.2
			stmt.setString(fieldCount + 1, dataItem.getId());

			//2.3
			stmt.addBatch();
		}
		
		//3.
		stmt.executeBatch();
	}

	public int size() {
		return dataList.size();
	}

	public void sort() {
		for (SortItemList itemList: sortItemLists) {
			itemList.sort();
			itemList.ranking();
		}
	}

	private void init(ResultSetMetaData metaData) throws SQLException {
		int fieldSize = rankingFields.size();
		
		sortItemLists = new SortItemList[fieldSize];
		for (int i = 0 ; i < fieldSize; i++) {
			sortItemLists[i] = new SortItemList(1000);
		}
		
		sourIndex = new int[fieldSize];
		
		int size = metaData.getColumnCount();
		
		for (int m = 0; m < fieldSize; m++) {
			RankingField rankingField = rankingFields.get(m);
			String sourField = rankingField.getSourField();
			
			for (int i = 1; i <= size; i++) {
				String fieldname = metaData.getColumnName(i);
				
				if (sourField.equalsIgnoreCase(fieldname)) {
					sourIndex[m] = i;
				}
			}
		}
	}

}

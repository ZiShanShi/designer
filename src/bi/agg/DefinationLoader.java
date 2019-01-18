package bi.agg;

import bi.agg.data.Dimension;
import bi.agg.data.Measurement;
import bi.agg.ranking.RankingField;
import foundation.config.ConfigLoader;
import foundation.data.Entity;
import foundation.data.EntitySet;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLRunner;

import java.io.FileNotFoundException;

public class DefinationLoader extends ConfigLoader {

	private DefinationContainer definationContainer;
	private Measurement measurement;

	public DefinationLoader() throws FileNotFoundException {
		definationContainer = DefinationContainer.getInstance();

		measurement = new Measurement();
		measurement.add("qty_target");
		measurement.add("qty_actual");
		measurement.add("amt_target");
		measurement.add("amt_actual");
	}

	@Override
	public void setActive(boolean active) {

	}

	@Override
	public void setName(String name) {

	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public String getName() {
		return "DefinationLoader";
	}

	@Override
	public void load() throws Exception {
		// 1.
		NamedSQL namedSQL = NamedSQL.getInstance("loadAggDefination");
		EntitySet entitys = SQLRunner.getEntitySet(namedSQL);
		
		for (Entity entity : entitys) {
			Defination defination = new Defination();
			defination.load(entity);
			loadDimensions(defination);
			loadRankingFields(defination);
			defination.setMeasurement(measurement);
			
			definationContainer.appendDefination(defination);
		}
	}

	private void loadDimensions(Defination defination) throws Exception {
		String id = defination.getId();
		
		NamedSQL namedSQL = NamedSQL.getInstance("loadAggDimension");
		namedSQL.setParam("parentid", id);
		EntitySet entitys = SQLRunner.getEntitySet(namedSQL);
		
		for (Entity detail : entitys) {
			String dimensionCode = detail.getString("dimensionCode");
			String param = detail.getString("param");
			
			Dimension dimension = Dimension.newInstance(dimensionCode, param);
			defination.appendDimension(dimension);
		}
	}
	
	private void loadRankingFields(Defination defination) throws Exception {
		String id = defination.getId();
		
		NamedSQL namedSQL = NamedSQL.getInstance("loadAggRanking");
		namedSQL.setParam("parentid", id);
		EntitySet entitys = SQLRunner.getEntitySet(namedSQL);
		
		for (Entity detail : entitys) {
			String sourField = detail.getString("sourField");
			String destField = detail.getString("destField");
			
			RankingField rankingField = new RankingField(defination, sourField, destField);
			defination.appendRankingField(rankingField);
		}
	}

}

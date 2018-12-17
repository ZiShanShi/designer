package foundation.rule;

import foundation.data.Entity;
import foundation.data.EntitySet;

import java.util.Collection;


public class RuleEngine implements IContext {

	private static RuleEngine instance;
	private EntitySet entitySet;
	private Entity entity;
	private Collection<String> collection;


	public synchronized static RuleEngine getInstance() {
		if (instance == null) {
			instance = new RuleEngine();
		}
		return instance;
	}

	public RuledResult exec(RuleList rulelist) throws Exception {
		// 校验的结果信息
		RuledResult result = new RuledResult();

		for (Rule rule : rulelist) {
			execOneRule(rule, result);
		}

		return result;
	}

	private void execOneRule(Rule rule, RuledResult result) throws Exception {
		rule.exec(this, result);
	}

	@Override
	public EntitySet getEntitySet() {
		return entitySet;
	}

	@Override
	public Entity getEntity() {
		return entity;
	}

	@Override
	public Collection<String> getCollection() {
		return collection;
	}



	public void setEntitySet(EntitySet entitySet) {
		this.entitySet = entitySet;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public void setCollection(Collection<String> collection) {
		this.collection = collection;
	}


}

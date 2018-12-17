package foundation.rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import foundation.data.Entity;
import foundation.data.EntitySet;
import foundation.persist.SystemCondition;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLRunner;

public class RuleList implements Iterable<Rule> {

	private List<Rule> items;

	public RuleList() {
		items = new ArrayList<Rule>();
	}

	@SuppressWarnings("unchecked")
	public static RuleList newInstance(Object object) throws Exception {
		if (object instanceof Collection) {

			// 校验字段情况
			Collection<IRuledValue> collection = (Collection<IRuledValue>) object;
			return createInstanceForCollection(collection);

		} else if (object instanceof NamedSQL) {

			// 校验总体情况
			NamedSQL namedSQL = (NamedSQL) object;
			return createInstanceForDB(namedSQL);
		}

		return null;
	}

	private static RuleList createInstanceForCollection(Collection<IRuledValue> collection) {
		RuleList ruleList = new RuleList();

		for (IRuledValue ruledValue : collection) {

			// 一个字段生成一个规则 (一个字段可能有多个规则，即生成Rule[] ====TODO==== )
			Rule rule = Rule.newInstance(ruledValue);
			if (rule != null) {
				ruleList.add(rule);
			}
		}
		return ruleList;
	}

	private void add(Rule rule) {
		items.add(rule);
	}

	private static RuleList createInstanceForDB(NamedSQL namedSQL) throws Exception {
		RuleList ruleList = new RuleList();
		
		EntitySet entitySet = SQLRunner.getEntitySet(namedSQL);
		for (Entity entity : entitySet) {
			String condition = entity.getString("condition");
			
			if (!SystemCondition.isCompatible(condition)) {
				continue;
			}
			
			Rule rule = Rule.newInstance(entity);
			if (rule != null) {
				ruleList.add(rule);
			}
		}

		return ruleList;
	}

	@Override
	public Iterator<Rule> iterator() {
		return items.iterator();
	}
}

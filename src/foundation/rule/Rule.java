package foundation.rule;

import foundation.util.Util;

public abstract class Rule {

	protected String name; // 规则名字
	protected String key; // 要校验的值

	public Rule(String name, String key) {
		this.name = name;
		this.key = key;
	}

	public abstract void exec(IContext context, RuledResult result) throws Exception;

	public static Rule newInstance(IRuledValue ruledValue) {
		
		if(Util.isEmptyStr(ruledValue.getRuledValue())){
			return null;
		}
		
		RuleType type = ruledValue.getRuledType();

		if (RuleType.exists == type) {
			return new ExistsRule("[ 存在校验 ]", ruledValue.getRuledValue());

		} else if (RuleType.notEmpty == type) {
			//new NotEmpty("[ 非空校验 ]", ruledValue.getRuledValue());
			return null;

		} else if (RuleType.sql == type) {
			return new SQLRule("[ sql校验 ]", ruledValue.getRuledValue());
		}
		
		return null;
	}
}

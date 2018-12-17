package foundation.rule;

import java.util.Collection;

public class ExistsRule extends Rule {

	public ExistsRule(String name, String fieldName) {
		super(name, fieldName);
	}

	@Override
	public void exec(IContext context, RuledResult result) throws Exception {

		// 运行时字段集合
		Collection<String> collection = context.getCollection();

		/*
		 * if (collection.isEmpty()) { result.setErrorCnt(1); result.add(name +
		 * ":校验失败！  字段:[" + key + "]不存在"); }
		 */


		boolean exists = collection.contains(key);
		String msg = name + "---->校验失败，字段[ " + key + " ]不存在";

		if (!exists && !result.contains(msg)) {
			result.setErrorCnt(1);
			result.add(msg);
		}
	}
}

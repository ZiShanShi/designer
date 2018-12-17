package foundation.variant;

import java.util.ArrayList;
import java.util.List;

public abstract class VariantContext implements IVariantRequestListener {

	private List<VariantContext> linkedContextList;
	
	public VariantContext() {
		linkedContextList = new ArrayList<VariantContext>();
	}
	
	public void setParametersTo(IExpression expression) throws Exception {
		doSetParametersTo(expression, true);
	}
	
	protected void doSetParametersTo(IExpression expression, boolean queryGlabal) throws Exception {
		VariantList paramList = expression.getVariantList();
		
		//1. on local（自己设置--主设置器）
		for (VariantSegment variant: paramList) {
			if (!variant.isEmpty()) {
				continue;
			}
			
			String name = variant.getName();
			String value = getStringValue(name, null);

			if (value != null) {
				variant.setValue(value);
			}
		}
		
		//2. on local（别人设置--链条上的其它设置器，主设置器调用了linkContext()方法）
		for (VariantContext linked: linkedContextList) {
			linked.doSetParametersTo(expression, false);
		}
		
		//3.
		if (queryGlabal) {
			for (VariantSegment variant: paramList) {
				if (!variant.isEmpty()) {
					continue;
				}
				
				String name = variant.getName();
				String value = GlobalVariant.getStringValue(name, null);

				if (value != null) {
					variant.setValue(value);
				}
			}
		}
	}

	@Override
	public List<String> getVariantNames() {
		return null;
	}
	
	public void linkContext(VariantContext context) {
		linkedContextList.add(context);
	}
}

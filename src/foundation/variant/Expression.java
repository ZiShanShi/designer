package foundation.variant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import foundation.util.Util;

public class Expression implements Iterable<VariantSegment>, IVariantParseListener, IExpression {
	
	protected static Logger logger;
	protected List<Segment> segments;
	protected VariantList variantList;
	
	static {
		logger = Logger.getLogger(Expression.class);
	}
	
	public Expression(String string) throws Exception {
		this(string, 0);
	}
	
	public Expression(String string, int size) throws Exception {
		this(size);
		
		VariantParser parser = new VariantParser(this);
		parser.parse(string);
	}

	private Expression(int size) throws Exception {
		if (size <= 0) {
			size = 4;
		}
		
		segments = new ArrayList<Segment>(size);
		variantList = new VariantList();
	}
	
	public Expression newInstance() throws Exception {
		int size = segments.size();
		Expression instance = new Expression(size);
		
		Segment segment;
		VariantSegment param;
		
		for (int i = 0; i < size; i++) {
			segment = segments.get(i);
			
			if (segment instanceof VariantSegment) {
				param = (VariantSegment) segment;
				String name = param.getName();
				
				if (instance.variantList.contains(param.getName())) {
					instance.segments.add(instance.variantList.get(name));
				}
				else {
					segment = segment.newInstance();
					instance.segments.add(segment);
					instance.variantList.add(name, (VariantSegment) segment);
				}
			}
			else {
				segment = segment.newInstance();
				instance.segments.add(segment);
			}
		}
		
		return instance;
	}
	
	public String tryGetString() throws Exception {
		for (VariantSegment variant : variantList) {
			if (variant.isEmpty()) {
				throw new Exception("empty sql param: " + variant.getName());
			}
		}
		
		return getString();
	}
	
	public String getString() {
		StringBuilder result = new StringBuilder();
		
		int n = segments.size();
		Segment segment;
		String value;
		
		for (int i = 0; i < n; i++) {
			segment = segments.get(i);
			value = segment.getValueString();
			
			result.append(value);
		}
		
		return result.toString();
	}
	
	@Override
	public void onSegment(String value) {
		if (Util.isEmptyStr(value)) {
			return;
		}
		
		Segment segment = new StringSegment(value);
		segments.add(segment);
	}

	@Override
	public void addVariant(String name) throws Exception {
		if (Util.isEmptyStr(name)) {
			return;
		}
		
		if (variantList.contains(name)) {
			VariantSegment segment = variantList.get(name);
			segments.add(segment);
		}
		else {
			VariantSegment segment = new VariantSegment(name);
			segments.add(segment);
			variantList.add(name, segment);
		}	
	}
	
	public VariantSegment getVariant(String name) {
		if (Util.isEmptyStr(name)) {
			return null;
		}
		
		return variantList.get(name);
	}

	public Iterator<VariantSegment> iterator() {
		return variantList.getItemList().iterator();
	}

	@Override
	public VariantList getVariantList() {
		return variantList;
	}

	public boolean isVariantEmpty() {
		return variantList.isEmpty();
	}
	
	public void clearVariantValues() {
		for (VariantSegment variant: variantList) {
			variant.clearValue();
		}		
	}

}

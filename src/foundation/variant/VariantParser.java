package foundation.variant;

public class VariantParser {

	private char Flag_Variant = '@';
	private char Flag_LeftQuote = '{';	
	private char Flag_RightQuote = '}';	
	private IVariantParseListener listener;

	public VariantParser(IVariantParseListener listener) {
		this.listener = listener;
	}
	
	public void parse(String expression) throws Exception {
		if (expression == null) {
			return;
		}
		
		int length = expression.length();
		
		String segmentString;
		int variantLength = 0;		
		int begin = 0;
		char curr;
		
		for (int i = 0; i < length; i++) {
			curr = expression.charAt(i);
			
			if (isKey(curr)) {
				if (Flag_LeftQuote != expression.charAt(Math.min(i + 1, length - 1))) {
					continue;
				}
				
				segmentString = expression.substring(begin, i);
				onFindSegment(segmentString);
				
				variantLength = getVariantLength(expression, i + 1, length);
				segmentString = expression.substring(i + 2, i + 2 + variantLength);
				
				onFindVariant(curr, segmentString);
				
				i = i + 2 + variantLength + 1;
				begin = i;
			}
		}
		
		if (begin <= length - 1) {
			segmentString = expression.substring(begin, length);
			onFindSegment(segmentString);
		}
	}
	
	public static String getVariantName(String value) {
		if (value == null) {
			return null;
		}
		
		int begin = value.indexOf("@{") + 2;
		int end = value.indexOf("}", begin);
		
		if (end <= begin) {
			return null; 
		}
		
		return value.substring(begin, end);
	}
	
	protected boolean isKey(char value) {
		return Flag_Variant == value;
	}

	protected void onFindSegment(String segment) throws Exception {
		if (listener != null) {
			listener.onSegment(segment);
		}
	}	
	
	protected void onFindVariant(char key, String variant) throws Exception {
		if (listener != null) {
			listener.addVariant(variant);
		}		
	}

	private int getVariantLength(String expression, int pos, int max) throws Exception {
		if (Flag_LeftQuote == expression.charAt(pos)) {
			for (int i = pos + 1; i < max; i++) {
				if (Flag_RightQuote == expression.charAt(i)) {
					return i - pos - 1;
				}
			}
		}
		else {
			throw new Exception("error parse param expression (" + pos + "):" + expression);
		}
		
		throw new Exception("error parse param expression (" + pos + "):" + expression);
	}
	
	public static void main(String[] args) {
		System.out.println(getVariantName("123@{456}789"));
	}

}

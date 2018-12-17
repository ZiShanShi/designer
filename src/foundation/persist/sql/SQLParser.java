package foundation.persist.sql;


public class SQLParser {

	public static final char Flag_SQLVariant = '@';
	public static final char Flag_LeftQuote = '{';	
	public static final char Flag_RightQuote = '}';	
	
	protected SQLCreator sqlCreator;
	
	public SQLParser(SQLCreator sqlCreator) {
		this.sqlCreator = sqlCreator;
	}
	
	public void parse(String sql) throws Exception {
		int length = sql.length();
		
		String segmentString;
		int variantLength = 0;		
		int begin = 0;
		char curr;
		
		for (int i = 0; i < length; i++) {
			curr = sql.charAt(i);
			
			if (isKey(curr)) {
				if (Flag_LeftQuote != sql.charAt(Math.min(i + 1, length - 1))) {
					continue;
				}
				
				segmentString = sql.substring(begin, i);
				sqlCreator.addSQLString(segmentString);
				
				variantLength = getVariantLength(sql, i + 1, length);
				segmentString = sql.substring(i + 2, i + 2 + variantLength);
				
				onFindVariant(curr, segmentString);

				
				i = i + 2 + variantLength + 1;
				begin = i;
			}
		}
		
		if (begin <= length - 1) {
			segmentString = sql.substring(begin, length);
			sqlCreator.addSQLString(segmentString);			
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
		return Flag_SQLVariant == value;
	}

	protected void onFindVariant(char key, String variant) throws Exception {
		sqlCreator.addSqlVariant(variant);		
	}

	private int getVariantLength(String sql, int pos, int max) throws Exception {
		if (Flag_LeftQuote == sql.charAt(pos)) {
			for (int i = pos + 1; i < max; i++) {
				if (Flag_RightQuote == sql.charAt(i)) {
					return i - pos - 1;
				}
			}
		}
		else {
			throw new Exception("error parse sql (" + pos + "):" + sql);
		}
		
		throw new Exception("error parse sql (" + pos + "):" + sql);
	}
	
	public static void main(String[] args) {
		System.out.println(getVariantName("123@{456}789"));
	}

}

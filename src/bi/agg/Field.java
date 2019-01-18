package bi.agg;

import java.util.HashMap;
import java.util.Map;

public class Field {

	private String name;
	private String typecodeField;
	private String typecode;
	private String flatfield;
	private String title;
	private String[] isotopes;
	private Map<String, String> variants;
	
	public Field(String name, String title) {
		this.name = name;
		this.title = title;
		variants = new HashMap<String, String>();
	}
	
	public Field(String name, String title, String ... isotopes) {
		this(name, title);
		this.isotopes = isotopes;
	}
	
	public void setTypeCode(String fieldName, String typecode) {
		this.typecodeField = fieldName;
		this.typecode = typecode;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public String[] getIsotopes() {
		return isotopes;
	}
	
	public void appendVariant(String name, String value) {
		if (name == null) {
			return;
		}
		
		name = name.toLowerCase();
		variants.put(name, value);
	}
	
	public String getVariant(String name) {
		return variants.get(name);
	}

	public String getTypeCode() {
		return typecode;
	}

	public String getTypecodeField() {
		return typecodeField;
	}

	public String getFlatfield() {
		return flatfield;
	}

	public void setFlatfield(String flatfield) {
		this.flatfield = flatfield;
	}
	
}

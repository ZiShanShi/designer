package foundation.util;

public class ContentBuilder {

	protected StringBuilder content;
	protected boolean empty;
	protected String division;
	
	
	public ContentBuilder() {
		empty = true;
		content = new StringBuilder();
	}
	
	public ContentBuilder(String division) {
		this.empty = true;
		this.division = division;
		this.content = new StringBuilder();
	}
	
	public ContentBuilder append(Object data) {
		if (data == null) {
			return this;
		}
		
		return append(data.toString());
	}
	
	public ContentBuilder append(String data) {
		if (division != null) {
			if (empty) {
				empty = false;
			}
			else {
				content.append(division);
			}			
		}
		
		content.append(data);
		return this;
	}
	
	public ContentBuilder append(Object data, String division) {
		if (data == null) {
			return this;
		}
		
		return append(data.toString(), division);
	}
	
	public ContentBuilder append(String data, String division) {
		if (empty) {
			empty = false;
		}
		else {
			content.append(division);
		}
		
		content.append(data);
		return this;
	}
	
	public ContentBuilder append(char[] data) {
		return append(data, 0, data.length);
	}
	
	public ContentBuilder append(char[] data, int begin, int length) {
		content.append(data, begin, length);
		return this;
	}
	
	public String toString() {
		return content.toString();
	}
	
	public boolean isEmpty() {
		return empty;
	}
	
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
}

package foundation.variant;


public class StringSegment extends Segment {

	private String value;
	
	public StringSegment(String value) {
		this.value = value;
	}
	
	@Override
	public String getValueString() {
		return value;
	}

	public String toString() {
		return value;
	}

	@Override
	public Segment newInstance() {
		return new StringSegment(value);
	}

}

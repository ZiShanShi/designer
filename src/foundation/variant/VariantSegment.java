package foundation.variant;


public class VariantSegment extends Segment {

	protected String name;
	protected String value;
	
	
	public VariantSegment(String name) {
		this.name = name;
	}
	
	@Override
	public Segment newInstance() {
		return new VariantSegment(name);
	}
	
	@Override
	public String getValueString() {
		return value;
	}

	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "@{" + name + "}=" + value;
	}

	public void clearValue() {
		value = null;
	}
	
	public boolean isEmpty() {
		return value == null;
	}
	
}

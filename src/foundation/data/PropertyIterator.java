package foundation.data;

import java.util.Iterator;

import foundation.data.reader.ObjectReader;
import foundation.data.reader.PropertyReader;

public class PropertyIterator implements Iterator<PropertyReader> {

	private ObjectReader objectReader;
	private int size;
	private int pos;
	
	public PropertyIterator(ObjectReader objectReader) {
		this.objectReader = objectReader;
		this.size = objectReader.size();
		this.pos = 0;
	}
	
	@Override
	public boolean hasNext() {
		return pos < size;
	}

	@Override
	public PropertyReader next() {
		return objectReader.getPropertyReader(pos++);
	}

	@Override
	public void remove() {
		
	}

}

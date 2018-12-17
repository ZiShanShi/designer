package foundation.dictionary;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {
	private String name;
	private boolean dirty;
	private List<DictionaryLine> dataList;

	public Dictionary(String name) {
		this.dataList = new ArrayList<DictionaryLine>();
		this.name = name;
	}

	public void append(String key, String value) {
		if (key == null) {
			return;
		}

		DictionaryLine line = new DictionaryLine(key, value);
		this.dataList.add(line);
	}

	public boolean isDirty() {
		return this.dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public void clear() {
		this.dataList.clear();
	}

	public String getName() {
		return this.name;
	}

	public Object getDataList() {
		return this.dataList;
	}
}
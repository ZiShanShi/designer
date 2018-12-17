package foundation.rule;

import java.util.ArrayList;
import java.util.List;

public class RuledResult {// 校验结果

	private int errorCnt;
	private List<String> messages;

	public RuledResult() {
		errorCnt = 0;
		messages = new ArrayList<String>();
	}

	public int getErrorCnt() {
		return errorCnt;
	}

	public void setErrorCnt(int errorCnt) {
		this.errorCnt += errorCnt;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void add(String msg) {
		messages.add(msg);
	}

	public void clear() {
		this.errorCnt = 0;
		this.messages.clear();
	}

	public boolean contains(String msg) {
		return messages.contains(msg);
	}
}

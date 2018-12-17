package foundation.callable;

import javax.servlet.http.HttpServletRequest;

import foundation.data.Page;
import foundation.util.Util;

public class PageParameter {

	private String pageNo;
	private String pageSize;
	private String beginRecord;
	private boolean empty;
	
	
	public PageParameter(HttpServletRequest request) {
		pageNo = request.getParameter("pageno");
		pageSize = request.getParameter("pagesize");
		beginRecord = request.getParameter("beginrecord");
		
		empty = Util.isEmptyStr(pageNo) && Util.isEmptyStr(pageSize) && Util.isEmptyStr(beginRecord); 
	}
	
	public boolean isEmpty() {
		return empty;
	}
	
	public Page toPage(int recordCount) {
		Page page = new Page(recordCount);
		
		if (!Util.isEmptyStr(pageSize)) {
			page.setPageSize(Integer.valueOf(pageSize));
		}
		
		if (!Util.isEmptyStr(pageNo)) {
			page.setPageNo(Integer.valueOf(pageNo));
		}
		
		if (!Util.isEmptyStr(beginRecord)) {
			page.setBeginRecord(Integer.valueOf(beginRecord));
		}

		return page;
	}

}

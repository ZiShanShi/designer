package foundation.data;

public class Page {  
	
    private int pageSize;  
    private int recordCount;  
    private int pageNo;
  
    
    public Page() { //默认一页20条，第一页
    }  
    
    public Page(int recordCount) { //默认一页20条，第一页
    	this();
        this.recordCount = recordCount;
    }  
    
    public int getBeginRecordNo() { 
    	int recordNo = pageSize * (pageNo - 1) + 1; 
        return recordNo;  
    }  
    
    public int getBeginRecordNo_1() { 
    	int recordNo = getBeginRecordNo();
        return recordNo - 1;  
    } 
    
    public int getEndRecordNo() {  
    	int recordNo = pageSize * pageNo;
        return Math.min(recordNo, recordCount);  
    }  
    
    public int getPageSize() {  
        return pageSize;  
    }  
  
    public int getPageNo() {  
        return pageNo;
    }  
  
    public int getRecordCount() {  
        return recordCount;  
    }  
  
    public int getPageCount() {  
        return (int)Math.ceil(recordCount * 1.0d / pageSize);
    }  
  
    public void setRecordCount(int count) {
    	this.recordCount = count;
    }
  
    public void setPageSize(int value) { 
    	if (value <= 0) {
    		return;
    	}
    	
       	pageSize = value;  
    }  
    
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setBeginRecord(int beginRecord) {
		this.pageNo = (beginRecord / pageSize) + 1;
	} 

	public void set(String name, String value) {
		name = name.toLowerCase();
		
		if ("pageno".equals(name)) {
			setPageNo(Integer.parseInt(value));
		}
		else if ("pagesize".equals(name)) {
			setPageSize(Integer.parseInt(value));			
		}
	}

 	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append("size=").append(pageSize).append(",");
		result.append("recordCount=").append(recordCount).append(",");
		result.append("pageNo=").append(pageNo);
		
		return result.toString();
	}
	
}  
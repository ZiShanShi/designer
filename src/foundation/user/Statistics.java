package foundation.user;

public class Statistics implements Comparable<Statistics> {
	private String ip;
	private Integer cnt;
	private String date;

	public Statistics(String ip, Integer cnt, String date) {
		this.ip = ip;
		this.cnt = cnt;
		this.date = date;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getCnt() {
		return cnt;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public int compareTo(Statistics o) {// 逆序
		if (this.cnt > o.getCnt())
			return -1;

		if (this.cnt == o.getCnt())
			return 0;

		if (this.cnt < o.getCnt())
			return 1;

		return 0;
	}
}

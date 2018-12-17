package foundation.user;

public class OnlineAnymous extends OnlineUser {

	private static OnlineAnymous instance;
	
	static {
		instance = new OnlineAnymous();
	}
	
	public static OnlineAnymous getInstance() {
		return instance;
	}
	
	public OnlineAnymous() {
		super();
		
		id = "anymous";
		name = "anymous";
		orgid = "anymous";
		orgCode = "anymous";
		orgName = "anymous";
	}

}

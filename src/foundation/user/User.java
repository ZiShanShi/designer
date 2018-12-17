package foundation.user;

import java.util.Random;

import javax.servlet.http.HttpSession;

import foundation.callable.Callable;
import foundation.config.Configer;
import foundation.data.Entity;
import foundation.persist.DataCenter;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLRunner;
import foundation.phone.SendResult;
import foundation.phone.Sendsms;
import foundation.util.Util;


public class User extends Callable {

	public static String Error_NotExist = "USER_NotExist";
	public static String Error_EmptyOrgCode = "USER_EmptyOrgCode";
	public static String Error_InvalidUser = "USER_InvalidUser";
	public static String Error_InvalidVCode = "USER_InvalidVCode";
	public static String Error_EmptyVCodeOrPassword = "USER_EmptyVCodeOrPass";
	public static String Error_MultiUser = "USER_MultiUser";
	
	public static boolean isTest = false;
	public static final String Code_UserName = "username";
	public static String Code_User_Dealer = "user_dealer";
	public static String Code_User_Manufacturer = "user_manufacturer";
	public static String SuperVCode;
	private static Random random;
	
	static {
		random = new Random();
		SuperVCode = Configer.getParam("SuperVCode");
		isTest = !Util.stringToBoolean(Configer.getParam("SendSMS"));
	}

	@Override
	protected void doReceive(String[] paths) throws Exception {
		if (paths.length == 2) {
			String operator = paths[1];
			
			if ("login".equalsIgnoreCase(operator)) {
				login();
			}
			else if ("logout".equalsIgnoreCase(operator)) {
				logout();
			}
			else if ("getvcade".equalsIgnoreCase(operator)) {
				getVcode();
			}			
			else if ("changePassword".equalsIgnoreCase(operator)) {
				changePassword();
			}
			else if ("getinfo".equalsIgnoreCase(operator)) {
				getInfo();
			}
		}
		else {
			writer.ReplyError("bad data message path:" + fullPath);
		}
	}
	
	private void getVcode() throws Exception {
		String phone = request.getParameter("phone");
		
		if (Util.isEmptyStr(phone)) {
			resultPool.error("电话号码为空");
			return;
		}
		
		String vcode = "";
		
		for (int i = 0; i < 6; i++) {
			int value = random.nextInt(9);
			
			if (value <= 0) {
				value = 1;
			}
			
			vcode = vcode + value;
		}
		
		SendResult sendResult = Sendsms.sendVCode(phone, vcode);
		
		if (sendResult.isSuccess()) {
			HttpSession session = request.getSession(true);
			session.setAttribute("vcode", vcode);
			resultPool.success();			
		}
		else {
			resultPool.error(sendResult.getMsg());
		}
	}

	private void login() throws Exception {
		HttpSession session = request.getSession(true);
		
		String orgCode = request.getParameter("org");
		String phone = request.getParameter("phone");
		String username = request.getParameter("username");
		String vcode = request.getParameter("vcode");
		String password = request.getParameter("password");		
		
		//1. 检查用户是否存在
		NamedSQL namedSQL = NamedSQL.getInstance("getClientUserByPhoneOrName");
		namedSQL.setParam("phone", phone, "empty_phone");
		namedSQL.setParam("username", username, "empty_username");
		Entity entity = SQLRunner.getEntity(namedSQL);
		
		if (entity == null) {
			resultPool.error(Error_NotExist, "user not exists");
			return;
		}
		
		//2.检查是否需要公司码 
		boolean orgcheck = entity.getBoolean("orgcheck");
		if (orgcheck && Util.isEmptyStr(orgCode)) {
			resultPool.error(Error_EmptyOrgCode, "empty org code");
			return;			
		}
		
		//3.验证码或密码是否正确
		if (!Util.isEmptyStr(vcode)) {
			if (!vcode.equals(SuperVCode)) {
				String sourceVcode = (String)session.getAttribute("vcode");
				
				if (!vcode.equals(sourceVcode)) {
					resultPool.error(Error_InvalidVCode, "invalid vcode");
					return;
				}
			}
		}
		else if (!Util.isEmptyStr(password)) {
			if (!password.equals(entity.getString("password"))) {
				resultPool.error(Error_InvalidUser, "invalid username or password");
			}
		}
		else {
			resultPool.error(Error_EmptyVCodeOrPassword, "empty vcode or password");
			return;
		}
		
		//4、检查电话号码、公司码是否正确, 获取用户和公司信息
		String orgFilter = Util.isEmptyStr(orgCode) ? "" : " and org.code = '" + orgCode + "'" ;
		String userFilter = Util.isEmptyStr(phone) ? "usr.name = '" + username + "' and usr.password = '" + password + "'" : "usr.phone = '" + phone + "'";
		
		OnlineUser onlineUser = new OnlineUser();
		
		namedSQL = NamedSQL.getInstance("getUser");
		namedSQL.setParam("userfilter", userFilter);
		namedSQL.setParam("orgfilter", orgFilter);
		SQLRunner.getData(namedSQL, onlineUser);

		if (onlineUser.isEmpty()) {
			resultPool.error(Error_InvalidUser, "invalid username or password");
			return;
		}
		
		if (!onlineUser.isOnlyOne()) {
			resultPool.error(Error_MultiUser, "multi user, need orgCode");
			return;
		}
			
		session.setAttribute(OnlineUser.class.getSimpleName(), onlineUser);
		resultPool.success();		
	}

	private void logout() throws Exception {
		HttpSession session = request.getSession(true);
		session.invalidate();
			
		resultPool.success();
	}

	private void changePassword() throws Exception {
		String username = onlineUser.getName();
		String password = request.getParameter("pass");
		
		if (Util.isEmptyStr(username)) {
			resultPool.error("用户名丢失");
		}
		else if (Util.isEmptyStr(password)) {
			resultPool.error("密码丢失");
		}
		else {
			DataCenter.changePassword(username, password);
			resultPool.success();
		}
	}
	
	private void getInfo() throws Exception {
		resultPool.addValue("user", onlineUser);
	}
	
}

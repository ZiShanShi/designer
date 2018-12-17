package foundation.callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import foundation.user.OnlineUser;


public interface ICallable {

	void receive(String resource);

	void setResponse(HttpServletResponse response);

	void setRequest(HttpServletRequest request);

	void setOnlineUser(OnlineUser instance);

}

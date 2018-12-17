package foundation.callable;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import foundation.data.Entity;
import foundation.data.Page;
import foundation.persist.TableMeta;
import foundation.user.OnlineUser;
import foundation.user.UserRightContainer;
import foundation.util.Util;

public abstract class Callable implements ICallable, IParameterProvider {

	protected static Logger logger;
	private static UserRightContainer userRightContainer;
	protected HttpServletResponse response;
	protected HttpServletRequest request;
	protected OnlineUser onlineUser;
	protected EnvelopWriter writer;
	protected ResultPool resultPool;
	private DataPool dataPool;
	protected String fullPath;
	protected String[] paths;
	protected Map<String, Method> methodMap;
	protected Map<String, Method> allMethodMap;

	static {
		logger = Logger.getLogger(Callable.class);
		userRightContainer = UserRightContainer.getInstance();
	}

	public Callable() {
		methodMap = new HashMap<String, Method>();
		allMethodMap = new HashMap<String, Method>();

		collectMethod();
		publishMethod();
	}

	public void receive(String path) {
		fullPath = path;
		resultPool = new ResultPool();
		try {
			writer = new EnvelopWriter(request, response);
			try {
				if (onlineUser != null) {
					paths = path.split("/");
					try {
						doReceive(paths);
					}
					catch (Exception e) {
						onError(e);
					}
				}
				else {
					writer.replayTimeout();
				}
				
				if(writer.isDefaultJson()) {
					writer.replay(resultPool);
				}
				
				writer.flush();
			}
			catch (Exception e) {
				String error = e.getClass().getName() + ": " + e.getMessage();
				writer.ReplyError(error);
				logger.error("ajax error: " + e);
			}
		}
		catch (Exception e) {
		}
	}



	protected void doReceive(String[] paths) throws Exception {
		if (paths.length >= 2) {
			String operator = paths[1];
			operator = operator.toLowerCase();

			Method method = methodMap.get(operator);

			if (method != null) {
				try {
					beforeExecute();
					method.invoke(this);
				}
				catch (InvocationTargetException e) {
					Throwable throwable = e.getTargetException();

					if (throwable == null) {
						throw e;
					}
					else {
						throw (Exception) throwable;
					}
				}
			}
			else {
				resultPool.error("method not exists: " + operator);
			}
		}
		else {
			writer.ReplyError("bad bidding console message path:" + fullPath);
		}
	}

	protected void beforeExecute() throws Exception {

	}

	protected void onError(Exception e) throws Exception {
		logger.error(e);
		e.printStackTrace();
	}

	protected DataPool getDataPool() throws Exception {
		if (dataPool == null) {
			synchronized (this) {
				if (dataPool == null) {
					dataPool = new DataPool(request);
				}
			}
		}

		return dataPool;
	}

	protected void collectMethod() {
		Method[] methods = this.getClass().getDeclaredMethods();

		for (Method one : methods) {
			allMethodMap.put(one.getName().toLowerCase(), one);
		}
	}

	protected void publishMethod() {

	}

	protected void addMethod(String name) {
		if (name == null) {
			return;
		}

		name = name.toLowerCase();
		Method method = allMethodMap.get(name);

		if (method != null) {
			method.setAccessible(true);
			methodMap.put(name, method);
		}
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setOnlineUser(OnlineUser onlineUser) {
		this.onlineUser = onlineUser;
	}

	protected void getEntity(Entity entity) throws UnsupportedEncodingException {
		TableMeta tableMeta = entity.getTableMeta();
		String[] names = tableMeta.getLowerNames();
		int i = 0;

		for (String name : names) {
			String value = request.getParameter(name);

			if (value != null && value.startsWith("@{")) {
				name = value.substring(2, value.length() - 1);
				value = getDefaultParameter(name);
			}

			if ("id".equals(name) && Util.isEmptyStr(value)) {
				entity.set(i++, Util.newShortGUID());
				continue;
			}

			entity.set(i++, value);
		}
	}

	protected String getOrderBy() {
		String orderBy = request.getParameter("orderby");

		if (Util.isEmptyStr(orderBy)) {
			return null;
		}

		orderBy = " order by " + orderBy;

		return orderBy;
	}

	protected String getId() {
		String id = request.getParameter("id");

		if (id != null && id.startsWith("@{")) {
			id = id.substring(2, id.length() - 1);
			id = getDefaultParameter(id);
		}

		return id;
	}

	protected String getFilter(String data) throws Exception {
		// 1. user filter set in config file
		String userFilter = userRightContainer.getDataFilter(onlineUser, data);
		// 2. request filter set in page
		String requestFilter = request.getParameter("filter");

		boolean empty_user = Util.isEmptyStr(userFilter);
		boolean empty_request = Util.isEmptyStr(requestFilter);

		if (empty_user && empty_request) {
			return " 1=1 ";
		}

		if (empty_user) {
			return requestFilter;
		}

		if (empty_request) {
			return userFilter;
		}

		String result = userFilter + " and " + requestFilter;
		result = result.replace("@{userid}", onlineUser.getId());
		return result;
	}

	protected Page getPage(int recordCount) {
		String pageNo = request.getParameter("pageno");
		String pageSize = request.getParameter("pagesize");

		Page page = new Page(recordCount);
		
		if (!Util.isEmptyStr(pageNo)) {
			page.setPageNo(Integer.valueOf(pageNo));
		}
		
		if (!Util.isEmptyStr(pageSize)) {
			page.setPageSize(Integer.valueOf(pageSize));
		}

		return page;
	}

	public String getStringParam(String name) throws Exception {
		String value = request.getParameter(name);

		if (value != null && value.startsWith("@{")) {
			name = value.substring(2, value.length() - 1);
			value = getDefaultParameter(name);
			return value;
		}
		else if (value != null) {
			return value;
		}

		value = getDefaultParameter(name);

		return value;
	}

	protected String getDefaultParameter(String name) {
		if ("userid".equalsIgnoreCase(name)) {
			return onlineUser.getId();
		}
		else if ("rolecode".equalsIgnoreCase(name)) {
			return onlineUser.getRoleCode();
		}
		else if ("username".equalsIgnoreCase(name)) {
			return onlineUser.getName();
		}
		else if ("orgid".equalsIgnoreCase(name)) {
			return onlineUser.getOrgId();
		}
		else if ("newid".equalsIgnoreCase(name)) {
			return Util.newShortGUID();
		}
		else if ("newDate".equalsIgnoreCase(name)) {
			return Util.DataTimeToString(new Date());
		}
		else if ("filter".equalsIgnoreCase(name)) {
			return "1 = 1";
		}

		return null;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public OnlineUser getOnlineUser() {
		return onlineUser;
	}

	public ResultPool getResultPool() {
		return resultPool;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String[] getPaths() {
		return paths;
	}

}

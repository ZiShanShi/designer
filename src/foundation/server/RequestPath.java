package foundation.server;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

public class RequestPath {

	private static Set<String> pageTypes;
	public static int contextLength;
	private HttpServletRequest request;
	private String uri;
	private String target;  //去掉项目名，去掉root标签，并以/开头
	private String shortTarget;  //去掉项目名，去掉root标签，不以/开头
	private String suffix;  //请求后缀，没用则为VirtualPath路径，有则为Resource路径
	private String leaf;  ////target的最后一截
	private String parent;  //target的第一截
	private RequestType type;  //VirtualPath、Resource

	static {
		pageTypes = new HashSet<String>();
		pageTypes.add(".html");
		pageTypes.add(".htm");
		pageTypes.add(".jsp");
	}

	public RequestPath(HttpServletRequest request) {
		this.request = request;

		// /sfez/xxx/xxx/xx.html,uri是不带请求参数的，即不会带？xxx=xxx
		uri = request.getRequestURI();
		
		try {
			uri = URLDecoder.decode(uri, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 1. delete parameters(没用)
		int last = uri.length();
		int pos_param = uri.indexOf("?");
		if (pos_param > 0) {
			last = pos_param;
		}

		
		target = uri.substring(contextLength, last);

		// 2. root
		if ("/".equals(target)) {
			type = RequestType.VirtualPath;
			return;
		}

		// 3. find root
		int pos = target.lastIndexOf("/root/");
		if (pos >= 0) {
			target = target.substring(pos + "/root/".length());
		}

		if (target.charAt(0) != '/') {
			target = "/" + target;
		}

		// 4. resource
		int pos_dot = target.lastIndexOf(".");

		if (pos_dot > 0) {
			suffix = target.substring(pos_dot + 1);
			CommonRequestSourceType parse = CommonRequestSourceType.parse(suffix);
			if (parse.equals(CommonRequestSourceType.unknow)) {
				type = RequestType.VirtualPath;
			}
			else {
				type = RequestType.Resource;
			}
		} else {
			type = RequestType.VirtualPath;
		}

		int pos_begin = target.indexOf("/", 1);
		if (pos_begin > 0) {
			parent = target.substring(0, pos_begin);
		}

		int pos_end = target.lastIndexOf("/");
		if (pos_end >= pos_begin) {
			leaf = target.substring(pos_end);
		}

		shortTarget = target.substring(1);
	}

	public String getSuffix() {
		return suffix;
	}

	public String getTarget() {
		return target;
	}

	public String getShortTarget() {
		return shortTarget;
	}

	public String getLeaf() {
		return leaf;
	}

	public String getParent() {
		return parent;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getURI() {
		return uri;
	}

	public RequestType getType() {
		return type;
	}

	@Override
	public String toString() {
		return uri;
	}

}

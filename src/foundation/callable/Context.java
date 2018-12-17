package foundation.callable;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import foundation.data.Variant;

public class Context {

	private static Map<String, Variant> params;

	public Context(HttpServletRequest request) throws Exception {
		params = new HashMap<String, Variant>();
		load(request);
	}

	private void load(HttpServletRequest request) throws Exception {
		Enumeration<String> paramNames = request.getParameterNames();
		
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String value = request.getParameter(paramName);
			paramName = paramName.toLowerCase();
			
			Variant variant = new Variant(value);
			params.put(paramName, variant);
		}
	}

	public Variant getParameter(String name) {
		if (name == null) {
			return new Variant();
		}
		
		name = name.toLowerCase();
		Variant variant = params.get(name);
		
		if (variant == null) {
			variant = new Variant();
		}
		
		return variant;
	}

}

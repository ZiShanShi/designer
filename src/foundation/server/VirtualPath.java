package foundation.server;

import foundation.callable.ICallable;
import foundation.util.Util;

public class VirtualPath {

	private VirtualPathType type;
	private String target;
	private Class<? extends ICallable> callableClass;
	
	
	public VirtualPath(String target, String className) throws Exception {
		if (!Util.isEmptyStr(target)) {
			setTarget(target);
		}
		else if (!Util.isEmptyStr(className)) {
			setCallableClassName(className);
		}
		else {
			throw new Exception("invalid virtual path");
		}
	}
	
	public void setTarget(String value) {
		target = value;
		type = VirtualPathType.Resource;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setCallableClassName(String classname) throws ClassNotFoundException {
		Class clazz = Class.forName(classname);
		callableClass = (Class<? extends ICallable>) clazz;
		
		type = VirtualPathType.Callable;
	}
	
	public VirtualPathType getType() {
		return type;
	}

	public String getTarget() {
		return target;
	}

	public Class<? extends ICallable> getCallableClass() {
		return callableClass;
	}

}

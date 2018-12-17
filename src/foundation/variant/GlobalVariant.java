package foundation.variant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalVariant {

	private static Object lock = new Object();
	private static GlobalVariant instance;
	private Map<String, IVariantRequestListener> listenerMap;
	
	private GlobalVariant() {
		listenerMap = new HashMap<String, IVariantRequestListener>();
	}
	
	public static GlobalVariant getInstance() {
		if (instance == null) {
			synchronized(lock) {
				if (instance == null) {
					instance = new GlobalVariant();
				}
			}
		}
		
		return instance;
	}
	
	public static void regist(IVariantRequestListener listener) throws VariantExistsException {
		GlobalVariant service = getInstance();
		
		List<String> variantNames = listener.getVariantNames();
		
		if (variantNames == null) {
			return;
		}
		
		for (String name: variantNames) {
			if (name == null) {
				continue;
			}
			
			String lower = name.toLowerCase();
			
			if (service.listenerMap.containsKey(lower)) {
				throw new VariantExistsException(name);
			}
			
			service.listenerMap.put(lower, listener);
		}
	}
	
	public static String getStringValue(String name, VariantRequestParams paramVariantRequestParams) throws Exception {
		GlobalVariant service = getInstance();
		
		if (name == null) {
			return null;
		}
		
		String lower = name.toLowerCase();
		
		IVariantRequestListener listener = service.listenerMap.get(lower);
		
		if (listener == null) {
			new VariantNotFoundException(name);
		}
		
		return listener.getStringValue(lower, paramVariantRequestParams);
	}
}

package foundation.dictionary;

import foundation.server.Container;

public class DictionaryContainer extends Container<Dictionary> {

	private static DictionaryContainer instance;
	private static Object lock = new Object();
	
	private DictionaryContainer() {
		
	}
	
	public static DictionaryContainer getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new DictionaryContainer();
				}
			}
		}
		
		return instance;
	}

	public Dictionary append(String code) {
		Dictionary dictionary = new Dictionary(code);
		add(code, dictionary);
		
		return dictionary;
	}
	
}

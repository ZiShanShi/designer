package foundation.server;

import foundation.config.IPreloader;

public abstract class PreloadContainer<T> extends Container<T> implements IPreloader {

	private boolean active;
	
	public PreloadContainer() {
		active = true;
	}
	
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	abstract public void load() throws Exception;
	
	public void checkUpdated() throws Exception {
		if (dirty) {
			synchronized (this) {
				if (dirty) {
					load();
					dirty = false;
				}
			}
		}
	}

}

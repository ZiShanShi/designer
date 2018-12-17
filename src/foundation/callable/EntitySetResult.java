package foundation.callable;

import foundation.data.EntitySet;
import foundation.data.Page;

public class EntitySetResult {

	private EntitySet entitySet;
	private Page page;
	
	public EntitySetResult(EntitySet entitySet, Page page) {
		this.entitySet = entitySet;
		this.page = page;
	}

	public EntitySet getEntitySet() {
		return entitySet;
	}

	public Page getPage() {
		return page;
	}
	
	
}

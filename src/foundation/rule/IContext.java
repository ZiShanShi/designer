package foundation.rule;

import foundation.data.Entity;
import foundation.data.EntitySet;

import java.util.Collection;

public interface IContext {

	EntitySet getEntitySet();
	
	Entity getEntity();
	
	Collection<String> getCollection();
	
}

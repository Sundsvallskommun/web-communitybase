package se.dosf.communitybase.events;

import java.io.Serializable;

import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;

public abstract class CBBaseSearchableEvent implements Serializable{

	private static final long serialVersionUID = 8595555844256611806L;
	
	protected final ForegroundModuleDescriptor moduleDescriptor;

	public CBBaseSearchableEvent(ForegroundModuleDescriptor moduleDescriptor) {

		super();
		this.moduleDescriptor = moduleDescriptor;
	}

	public ForegroundModuleDescriptor getModuleDescriptor() {

		return moduleDescriptor;
	}

}

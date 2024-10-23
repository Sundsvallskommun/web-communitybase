package se.dosf.communitybase.events;

import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;


public class CBSearchableItemClearEvent extends CBBaseSearchableEvent {

	private static final long serialVersionUID = -1873804935445156592L;

	public CBSearchableItemClearEvent(ForegroundModuleDescriptor moduleDescriptor) {

		super(moduleDescriptor);
	}

}

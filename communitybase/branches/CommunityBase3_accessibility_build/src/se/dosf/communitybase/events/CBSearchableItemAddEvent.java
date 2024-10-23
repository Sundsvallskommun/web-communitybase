package se.dosf.communitybase.events;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import se.dosf.communitybase.interfaces.CBSearchableItem;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;

/**
 * This event is used to signal search modules regarding new and/or updated items.
 *
 * @author Unlogic
 *
 */
public class CBSearchableItemAddEvent extends CBBaseSearchableEvent implements Serializable {

	private static final long serialVersionUID = 7886742947562452835L;

	private final List<CBSearchableItem> items;

	public CBSearchableItemAddEvent(List<CBSearchableItem> items, ForegroundModuleDescriptor moduleDescriptor) {

		super(moduleDescriptor);
		this.items = items;
	}

	public CBSearchableItemAddEvent(CBSearchableItem item, ForegroundModuleDescriptor moduleDescriptor) {

		super(moduleDescriptor);
		this.items = Collections.singletonList(item);
	}

	@Override
	public ForegroundModuleDescriptor getModuleDescriptor() {

		return moduleDescriptor;
	}

	public List<CBSearchableItem> getItems() {

		return items;
	}

}

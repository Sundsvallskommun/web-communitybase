package se.dosf.communitybase.events;

import java.util.List;

import se.dosf.communitybase.interfaces.CBSearchableItem;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;

/**
 * This event is used to signal search modules regarding new and/or updated items.
 *
 * @author Unlogic
 *
 */
public class CBSearchableItemUpdateEvent extends CBSearchableItemAddEvent {

	private static final long serialVersionUID = 6412380388031754882L;

	public CBSearchableItemUpdateEvent(CBSearchableItem item, ForegroundModuleDescriptor moduleDescriptor) {

		super(item, moduleDescriptor);
	}

	public CBSearchableItemUpdateEvent(List<CBSearchableItem> items, ForegroundModuleDescriptor moduleDescriptor) {

		super(items, moduleDescriptor);
	}

}

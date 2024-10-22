package se.dosf.communitybase.events;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.dosf.communitybase.interfaces.CBSearchableItem;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;

/**
 * This event is used to signal search modules regarding deleted items.
 *
 * @author Unlogic
 *
 */
public class CBSearchableItemDeleteEvent extends CBBaseSearchableEvent implements Serializable{

	private static final long serialVersionUID = 2371864432121147623L;

	private final List<String> itemIDs;

	public CBSearchableItemDeleteEvent(String itemID, ForegroundModuleDescriptor moduleDescriptor) {

		super(moduleDescriptor);
		this.itemIDs = Collections.singletonList(itemID);
	}

	public CBSearchableItemDeleteEvent(List<String> itemIDs, ForegroundModuleDescriptor moduleDescriptor) {

		super(moduleDescriptor);
		this.itemIDs = itemIDs;
	}

	public CBSearchableItemDeleteEvent(ForegroundModuleDescriptor moduleDescriptor, List<CBSearchableItem> searchableItems) {

		super(moduleDescriptor);

		itemIDs = new ArrayList<String>(searchableItems.size());

		for(CBSearchableItem item : searchableItems){

			itemIDs.add(item.getID());
		}
	}

	@Override
	public ForegroundModuleDescriptor getModuleDescriptor() {

		return moduleDescriptor;
	}

	public List<String> getItemIDs() {

		return itemIDs;
	}

}

package se.dosf.communitybase.modules.search.events;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import se.dosf.communitybase.modules.search.SearchModule;
import se.dosf.communitybase.modules.search.tasks.DeleteTask;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;


public class DeleteEvent extends QueuedSearchEvent {

	private final List<String> itemIDs;

	public DeleteEvent(ForegroundModuleDescriptor moduleDescriptor, List<String> itemIDs) {

		super(moduleDescriptor);
		this.itemIDs = itemIDs;
	}

	@Override
	public void queueTasks(ThreadPoolExecutor executor, SearchModule searchModule) {

		for(String itemID : itemIDs){

			executor.execute(new DeleteTask(moduleDescriptor, itemID, searchModule));
		}
	}

	@Override
	public int getTaskCount() {

		return itemIDs.size();
	}

	@Override
	public String toString() {

		return "delete event with " + itemIDs.size() + " item ID's from module " + moduleDescriptor;
	}
}

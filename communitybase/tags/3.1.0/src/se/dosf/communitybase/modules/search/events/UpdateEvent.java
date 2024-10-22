package se.dosf.communitybase.modules.search.events;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import se.dosf.communitybase.interfaces.CBSearchableItem;
import se.dosf.communitybase.modules.search.SearchModule;
import se.dosf.communitybase.modules.search.tasks.UpdateTask;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;

public class UpdateEvent extends AddEvent {

	public UpdateEvent(ForegroundModuleDescriptor moduleDescriptor, List<CBSearchableItem> items) {

		super(moduleDescriptor, items);
	}

	@Override
	public void queueTasks(ThreadPoolExecutor executor, SearchModule searchModule) {

		for(CBSearchableItem item : items){

			executor.execute(new UpdateTask(moduleDescriptor, item, searchModule));
		}
	}

	@Override
	public String toString() {

		return "update event with " + items.size() + " items from module " + moduleDescriptor;
	}
}

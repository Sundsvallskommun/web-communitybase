package se.dosf.communitybase.modules.search.events;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import se.dosf.communitybase.interfaces.CBSearchableItem;
import se.dosf.communitybase.modules.search.SearchModule;
import se.dosf.communitybase.modules.search.tasks.AddTask;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;


public class AddEvent extends QueuedSearchEvent {

	protected final List<? extends CBSearchableItem> items;

	public AddEvent(ForegroundModuleDescriptor moduleDescriptor, List<? extends CBSearchableItem> items) {

		super(moduleDescriptor);
		this.items = items;
	}

	@Override
	public void queueTasks(ThreadPoolExecutor executor, SearchModule searchModule) {

		for(CBSearchableItem item : items){

			try {
				executor.execute(new AddTask(moduleDescriptor, item, searchModule));
			} catch (RejectedExecutionException e) {}
		}
	}

	@Override
	public int getTaskCount() {

		return items.size();
	}

	@Override
	public String toString() {

		return "add event with " + items.size() + " items from module " + moduleDescriptor;
	}
}

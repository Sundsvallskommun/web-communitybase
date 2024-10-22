package se.dosf.communitybase.modules.search.events;

import java.util.concurrent.ThreadPoolExecutor;

import se.dosf.communitybase.modules.search.SearchModule;
import se.dosf.communitybase.modules.search.tasks.ClearModuleTask;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;


public class ClearModuleEvent extends QueuedSearchEvent {

	public ClearModuleEvent(ForegroundModuleDescriptor moduleDescriptor) {

		super(moduleDescriptor);
	}

	@Override
	public void queueTasks(ThreadPoolExecutor executor, SearchModule searchModule) {

		executor.execute(new ClearModuleTask(moduleDescriptor, searchModule));
	}

	@Override
	public int getTaskCount() {

		return 1;
	}

	@Override
	public String toString() {

		return "clear event from module " + moduleDescriptor;
	}
}

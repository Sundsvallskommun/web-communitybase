package se.dosf.communitybase.modules.search.events;

import java.util.concurrent.ThreadPoolExecutor;

import se.dosf.communitybase.modules.search.SearchModule;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;


public abstract class QueuedSearchEvent {

	protected final ForegroundModuleDescriptor moduleDescriptor;

	public QueuedSearchEvent(ForegroundModuleDescriptor moduleDescriptor) {

		super();
		this.moduleDescriptor = moduleDescriptor;
	}

	public abstract void queueTasks(ThreadPoolExecutor executor, SearchModule searchModule);

	public abstract int getTaskCount();

	public ForegroundModuleDescriptor getModuleDescriptor() {

		return moduleDescriptor;
	}
}

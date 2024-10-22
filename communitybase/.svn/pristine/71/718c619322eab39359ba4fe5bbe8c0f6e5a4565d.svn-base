package se.dosf.communitybase.modules.search.events;

import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import se.dosf.communitybase.modules.search.SearchModule;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;


public abstract class QueuedSearchEvent {
	
	protected Logger log = Logger.getLogger(this.getClass());

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

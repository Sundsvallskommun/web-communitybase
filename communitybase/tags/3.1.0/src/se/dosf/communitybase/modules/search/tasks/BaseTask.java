package se.dosf.communitybase.modules.search.tasks;

import org.apache.log4j.Logger;

import se.dosf.communitybase.modules.search.SearchModule;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;


public abstract class BaseTask implements Runnable {

	protected final Logger log = Logger.getLogger(this.getClass());

	protected final ForegroundModuleDescriptor moduleDescriptor;
	protected final SearchModule searchModule;

	public BaseTask(ForegroundModuleDescriptor moduleDescriptor, SearchModule searchModule) {

		super();
		this.moduleDescriptor = moduleDescriptor;
		this.searchModule = searchModule;
	}
}

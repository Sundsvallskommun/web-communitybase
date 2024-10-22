package se.dosf.communitybase.modules.search.tasks;

import java.io.IOException;

import org.apache.lucene.index.Term;

import se.dosf.communitybase.modules.search.SearchConstants;
import se.dosf.communitybase.modules.search.SearchModule;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;


public class ClearModuleTask extends BaseTask {

	public ClearModuleTask(ForegroundModuleDescriptor moduleDescriptor, SearchModule searchModule) {

		super(moduleDescriptor, searchModule);
	}

	@Override
	public void run() {

		try{
			searchModule.deleteDocuments(new Term(SearchConstants.MODULE_ID_FIELD, moduleDescriptor.getModuleID().toString()));

		}catch(IOException e){

			log.error("Error deleteing documents module ID " + moduleDescriptor.getModuleID() + " from index", e);

			return;
		}
	}

}

package se.dosf.communitybase.modules.search.tasks;

import java.io.IOException;

import org.apache.lucene.index.Term;

import se.dosf.communitybase.interfaces.CBSearchableItem;
import se.dosf.communitybase.modules.search.SearchConstants;
import se.dosf.communitybase.modules.search.SearchModule;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;

public class UpdateTask extends AddTask implements Runnable {

	public UpdateTask(ForegroundModuleDescriptor moduleDescriptor, CBSearchableItem item, SearchModule searchModule) {

		super(moduleDescriptor, item, searchModule);
	}

	@Override
	public void run() {

		try{
			searchModule.deleteDocuments(new Term(SearchConstants.MODULE_ID_FIELD, moduleDescriptor.getModuleID().toString()), new Term(SearchConstants.ITEM_ID_FIELD, item.getID()));
		}catch(IOException e){

			log.error("Error deleteing document with item ID " + item.getID() + " from module " + moduleDescriptor + " from index", e);

			return;
		}

		super.run();
	}

}

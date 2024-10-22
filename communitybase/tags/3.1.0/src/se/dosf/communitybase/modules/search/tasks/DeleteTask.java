package se.dosf.communitybase.modules.search.tasks;

import java.io.IOException;

import org.apache.lucene.index.Term;

import se.dosf.communitybase.modules.search.SearchConstants;
import se.dosf.communitybase.modules.search.SearchModule;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;

public class DeleteTask extends BaseTask implements Runnable {

	private final String itemID;

	public DeleteTask(ForegroundModuleDescriptor moduleDescriptor, String itemID, SearchModule searchModule) {

		super(moduleDescriptor, searchModule);

		this.itemID = itemID;
	}

	@Override
	public void run() {

		try{
			searchModule.deleteDocuments(new Term(SearchConstants.MODULE_ID_FIELD, moduleDescriptor.getModuleID().toString()), new Term(SearchConstants.ITEM_ID_FIELD, itemID));
		}catch(IOException e){

			log.error("Error deleteing document with item ID " + itemID + " from module " + moduleDescriptor + " from index", e);

			return;
		}
	}
}

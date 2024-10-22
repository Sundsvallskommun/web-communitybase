package se.dosf.communitybase.modules.search.tasks;

import org.apache.lucene.document.Document;

import se.dosf.communitybase.interfaces.CBSearchableItem;
import se.dosf.communitybase.modules.search.SearchModule;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;

public class AddTask extends BaseTask implements Runnable {

	protected final CBSearchableItem item;

	public AddTask(ForegroundModuleDescriptor moduleDescriptor, CBSearchableItem item, SearchModule searchModule) {

		super(moduleDescriptor, searchModule);
		this.item = item;
	}

	@Override
	public void run() {

		if(!searchModule.isValidSystemState()){

			return;
		}

		Document doc;

		try{
			doc = searchModule.parseItem(moduleDescriptor, item);

		} catch(Throwable e){

			log.error("Error parsing item " + item + " from module " + moduleDescriptor,e);
			return;
		}

		if(doc == null){

			//Item no longer available skip it
			log.info("Item " + item + " from module " + moduleDescriptor + " no longer available, skipping.");
			return;
		}

		try{
			searchModule.addDocument(doc);
			
		}catch(Exception e){

			log.warn("Error adding document to index. Dcoument generated from item " + item + " from module " + moduleDescriptor, e);
		}
	}

}

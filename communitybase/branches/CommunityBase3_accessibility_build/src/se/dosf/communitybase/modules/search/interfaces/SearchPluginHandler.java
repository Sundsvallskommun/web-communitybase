package se.dosf.communitybase.modules.search.interfaces;


public interface SearchPluginHandler {

	boolean addSearchPlugin(SearchPlugin searchPlugin);
	
	boolean removeSearchPlugin(SearchPlugin searchPlugin);
}
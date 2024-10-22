package se.dosf.communitybase.interfaces;

import java.util.Collection;


public interface TagProvider {

	public Collection<String> getTags() throws Exception;
	
	public String getTagProviderAlias();
}

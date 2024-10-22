package se.dosf.communitybase.modules.search.interfaces;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import se.dosf.communitybase.modules.search.ContentHit;
import se.dosf.communitybase.modules.search.SearchAccessMode;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;

public interface SearchPlugin {

	String getAlias();
	
	String getName();
	
	ViewFragment getFragment(String queryString, SearchAccessMode accessMode, List<ContentHit> hits, int maxHitCount, User user, HttpServletRequest req) throws Exception;
	
	ViewFragment getJsonFragment(String queryString, SearchAccessMode accessMode, List<ContentHit> hits, int maxHitCount, User user, HttpServletRequest req) throws Exception;

	int getPriority();
	
	boolean hasOwnIndex();
	
	boolean supportsTags();
}
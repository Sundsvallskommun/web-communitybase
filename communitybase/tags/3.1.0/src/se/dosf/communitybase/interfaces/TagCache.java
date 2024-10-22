package se.dosf.communitybase.interfaces;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface TagCache {

	public List<String> getMatchingTags(String searchString, int maxHits);
	
	public void addTags(Collection<String> tags);

	public void sendMatchingTagsAsJSON(HttpServletRequest req, HttpServletResponse res, int maxHits) throws IOException;
	
}

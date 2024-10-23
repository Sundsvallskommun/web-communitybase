package se.dosf.communitybase.interfaces;

import java.io.InputStream;
import java.util.List;



public interface CBSearchableItem {

	public String getID();

	/**
	 * @return the full alias of this item
	 */
	public String getAlias();

	public String getTitle();

	public String getInfoLine();

	public String getContentType();

	public InputStream getData() throws Exception;

	public String getType();

	/**
	 * @return Null or a list of tags
	 */
	public List<String> getTags();
}

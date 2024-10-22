package se.dosf.communitybase.modules.search.searchitems;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.modules.search.ContentHit;
import se.dosf.communitybase.modules.search.SearchAccessMode;
import se.dosf.communitybase.modules.search.SearchModule;
import se.dosf.communitybase.modules.search.UserHit;
import se.dosf.communitybase.modules.search.interfaces.SearchPlugin;
import se.unlogic.hierarchy.core.beans.SimpleViewFragment;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.xml.XMLTransformer;
import se.unlogic.standardutils.xml.XMLUtils;

public class UsersPlugin implements SearchPlugin {
	
	private int priority;
	
	private String pluginName;
	
	private SearchModule searchModule;

	public UsersPlugin(String pluginName, int priority, SearchModule searchModule) {
		
		this.pluginName = pluginName;
		this.priority = priority;
		this.searchModule = searchModule;
	}

	@Override
	public ViewFragment getFragment(String queryString, SearchAccessMode accessMode, List<ContentHit> contentHits, int maxHitCount, User user, HttpServletRequest req) throws Exception {

		if (accessMode == SearchAccessMode.RESTRICTED) {
			return null;
		}
		
		return appendSearchHits(queryString, "PluginSearch", maxHitCount, user, req);
	}

	@Override
	public ViewFragment getJsonFragment(String queryString, SearchAccessMode accessMode, List<ContentHit> contentHits, int maxHitCount, User user, HttpServletRequest req) throws Exception {

		if (accessMode == SearchAccessMode.RESTRICTED) {
			return null;
		}
		
		return appendSearchHits(queryString, "PluginJson", maxHitCount, user, req);
	}

	public ViewFragment appendSearchHits(String queryString, String elementName, int maxHitCount, User user, HttpServletRequest req) throws IOException, TransformerException, TransformerConfigurationException {

		if (searchModule.getUserIndexer() == null) {
			return null;
		}
		
		List<UserHit> hits = searchModule.getUserIndexer().search(queryString, user, maxHitCount);

		if (CollectionUtils.isEmpty(hits)) {
			return null;
		}

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		
		Element element = doc.createElement(elementName);
		
		XMLUtils.appendNewElement(doc, element, "ContextPath", req.getContextPath());
		XMLUtils.appendNewElement(doc, element, "PluginName", pluginName);
		XMLUtils.append(doc, element, "UserHits", hits);
		
		if (searchModule.getUserProfileProvider() != null) {

			XMLUtils.appendNewElement(doc, element, "ProfileImageAlias", searchModule.getUserProfileProvider().getProfileImageAlias());
			XMLUtils.appendNewElement(doc, element, "ShowProfileAlias", searchModule.getUserProfileProvider().getShowProfileAlias());
		}

		documentElement.appendChild(element);
		
		doc.appendChild(documentElement);
		
		StringWriter stringWriter = new StringWriter();

		XMLTransformer.transformToWriter(searchModule.getTransformer(), doc, stringWriter, searchModule.getEncoding());

		return new SimpleViewFragment(stringWriter.toString(), null, null);
	}
	
	@Override
	public String getAlias() {

		return "users";
	}

	@Override
	public String getName() {

		return pluginName;
	}
	
	public void setPluginName(String pluginName) {

		this.pluginName = pluginName;
	}

	@Override
	public int getPriority() {

		return priority;
	}
	
	public void setPriority(int priority) {

		this.priority = priority;
	}

	@Override
	public boolean hasOwnIndex() {

		return true;
	}

	@Override
	public boolean supportsTags() {

		return false;
	}
	
	@Override
	public String toString() {
		
		return pluginName;
	}
}
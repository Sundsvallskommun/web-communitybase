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
import se.dosf.communitybase.modules.search.interfaces.SearchPlugin;
import se.unlogic.hierarchy.core.beans.SimpleViewFragment;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.xml.XMLTransformer;
import se.unlogic.standardutils.xml.XMLUtils;

public class CBContentPlugin implements SearchPlugin {
	
	private int priority;
	
	private String alias;
	
	private String pluginName;
	
	private SearchModule searchModule;

	public CBContentPlugin(String pluginName, String alias, int priority, SearchModule searchModule) {
		
		this.pluginName = pluginName;
		this.alias = alias;
		this.priority = priority;
		this.searchModule = searchModule;
	}

	@Override
	public ViewFragment getFragment(String queryString, SearchAccessMode accessMode, List<ContentHit> contentHits, int maxHitCount, User user, HttpServletRequest req) throws Exception {

		return appendSearchHits(contentHits, "PluginSearch", maxHitCount, user, req);
	}

	@Override
	public ViewFragment getJsonFragment(String queryString, SearchAccessMode accessMode, List<ContentHit> contentHits, int maxHitCount, User user, HttpServletRequest req) throws Exception {

		return appendSearchHits(contentHits, "PluginJson", maxHitCount, user, req);
	}

	public ViewFragment appendSearchHits(List<ContentHit> contentHits, String elementName, int maxHitCount, User user, HttpServletRequest req) throws IOException, TransformerException, TransformerConfigurationException {

		if (CollectionUtils.isEmpty(contentHits)) {
			return null;
		}
		
		if (contentHits.size() > maxHitCount) {
			contentHits = contentHits.subList(0, maxHitCount);
		}

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		
		Element searchElement = doc.createElement(elementName);
		
		XMLUtils.appendNewElement(doc, searchElement, "ContextPath", req.getContextPath());
		XMLUtils.appendNewElement(doc, searchElement, "PluginName", pluginName);
		XMLUtils.appendNewElement(doc, searchElement, "Alias", alias);
		
		XMLUtils.append(doc, searchElement, "ContentHits", contentHits);
		
		documentElement.appendChild(searchElement);
		
		doc.appendChild(documentElement);
		
		StringWriter stringWriter = new StringWriter();

		XMLTransformer.transformToWriter(searchModule.getTransformer(), doc, stringWriter, searchModule.getEncoding());

		return new SimpleViewFragment(stringWriter.toString(), null, null);
	}

	@Override
	public String getAlias() {

		return alias;
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

		return false;
	}

	@Override
	public boolean supportsTags() {

		return true;
	}
	
	@Override
	public String toString() {
		
		return pluginName;
	}
}
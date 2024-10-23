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
import se.dosf.communitybase.modules.search.SectionHit;
import se.dosf.communitybase.modules.search.interfaces.SearchPlugin;
import se.unlogic.hierarchy.core.beans.SimpleViewFragment;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.xml.XMLTransformer;
import se.unlogic.standardutils.xml.XMLUtils;

public class SectionsPlugin implements SearchPlugin {

	private int priority;

	private String pluginName;

	private SearchModule searchModule;

	public SectionsPlugin(String pluginName, int priority, SearchModule searchModule) {

		this.pluginName = pluginName;
		this.priority = priority;
		this.searchModule = searchModule;
	}

	@Override
	public ViewFragment getFragment(String queryString, SearchAccessMode accessMode, List<ContentHit> contentHits, int maxHitCount, User user, HttpServletRequest req) throws Exception {

		return appendSearchHits(queryString, accessMode, "PluginSearch", maxHitCount, user, req);
	}

	@Override
	public ViewFragment getJsonFragment(String queryString, SearchAccessMode accessMode, List<ContentHit> contentHits, int maxHitCount, User user, HttpServletRequest req) throws Exception {

		return appendSearchHits(queryString, accessMode, "PluginJson", maxHitCount, user, req);
	}

	public ViewFragment appendSearchHits(String queryString, SearchAccessMode accessMode, String elementName, int maxHitCount, User user, HttpServletRequest req) throws IOException, TransformerException, TransformerConfigurationException {

		List<SectionHit> hits = searchModule.getSectionIndexer().search(queryString, user, accessMode, maxHitCount, true, searchModule.getCBInterface());

		if (CollectionUtils.isEmpty(hits)) {
			return null;
		}

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");

		Element searchElement = doc.createElement(elementName);

		XMLUtils.appendNewElement(doc, searchElement, "ContextPath", req.getContextPath());
		XMLUtils.appendNewElement(doc, searchElement, "PluginName", pluginName);

		if (searchModule.getCBUtilityModule() != null) {
			XMLUtils.appendNewElement(doc, searchElement, "SectionLogoURI", req.getContextPath() + searchModule.getCBUtilityModule().getFullAlias());
		}

		XMLUtils.append(doc, searchElement, "SectionHits", hits);

		documentElement.appendChild(searchElement);

		doc.appendChild(documentElement);

		StringWriter stringWriter = new StringWriter();

		XMLTransformer.transformToWriter(searchModule.getTransformer(), doc, stringWriter, searchModule.getEncoding());

		return new SimpleViewFragment(stringWriter.toString(), null, null);
	}

	@Override
	public String getAlias() {

		return "sections";
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
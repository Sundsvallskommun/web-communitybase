package se.dosf.communitybase.modules.search;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.modules.userprofile.UserProfileProvider;
import se.dosf.communitybase.modules.util.CBUtilityModule;
import se.unlogic.hierarchy.backgroundmodules.AnnotatedBackgroundModule;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleResponse;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

public class SearchBackgroundModule extends AnnotatedBackgroundModule {

	@InstanceManagerDependency(required = true)
	private SearchModule searchModule;

	@InstanceManagerDependency(required = false)
	private CBUtilityModule cbUtilityModule;

	@InstanceManagerDependency(required = false)
	private UserProfileProvider userProfileProvider;

	@Override
	protected BackgroundModuleResponse processBackgroundRequest(HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		doc.appendChild(documentElement);

		XMLUtils.appendNewElement(doc, documentElement, "ContextPath", req.getContextPath());
		XMLUtils.appendNewElement(doc, documentElement, "SearchModuleURI", req.getContextPath() + searchModule.getFullAlias());

		XMLUtils.append(doc, documentElement, "SearchTypes", searchModule.getSearchTypes());

		if (userProfileProvider != null) {
			XMLUtils.appendNewElement(doc, documentElement, "ProfileImageURI", req.getContextPath() + userProfileProvider.getProfileImageAlias());
		}

		if (cbUtilityModule != null) {
			XMLUtils.appendNewElement(doc, documentElement, "SectionLogoURI", req.getContextPath() + cbUtilityModule.getFullAlias() + "/sectionlogo");
			XMLUtils.appendNewElement(doc, documentElement, "SectionLogoURI", req.getContextPath() + cbUtilityModule.getFullAlias());
		}

		return new SimpleBackgroundModuleResponse(doc);
	}
}
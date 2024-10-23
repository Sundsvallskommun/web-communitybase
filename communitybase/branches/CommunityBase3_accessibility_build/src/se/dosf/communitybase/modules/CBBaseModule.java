package se.dosf.communitybase.modules;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.interfaces.CBInterface;
import se.dosf.communitybase.modules.sectionevents.CBSectionEventHandler;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class CBBaseModule extends AnnotatedForegroundModule implements CRUDCallback<User> {

	@InstanceManagerDependency(required = true)
	protected CBInterface cbInterface;

	@InstanceManagerDependency
	protected CBSectionEventHandler sectionEventHandler;

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();

		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));

		if(user != null) {
			document.appendChild(user.toXML(doc));
		}

		doc.appendChild(document);

		return doc;
	}

	@Override
	public String getTitlePrefix() {

		return moduleDescriptor.getName();
	}

	public CBInterface getCBInterface() {

		return cbInterface;
	}

	public Integer getSectionID() {

		return moduleDescriptor.getSectionID();
	}

	public SectionDescriptor getSectionDescriptor() {

		return sectionInterface.getSectionDescriptor();
	}

}

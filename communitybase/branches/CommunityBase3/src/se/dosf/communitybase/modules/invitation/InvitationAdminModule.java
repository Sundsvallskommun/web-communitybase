package se.dosf.communitybase.modules.invitation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class InvitationAdminModule extends AnnotatedForegroundModule {

	@InstanceManagerDependency(required = true)
	private InvitationModule invitationModule;

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Document doc = createDocument(req, uriParser, user);
		Element listInvitationsElement = doc.createElement("ListInvitations");
		doc.getFirstChild().appendChild(listInvitationsElement);
		
		XMLUtils.append(doc, listInvitationsElement, "Invitations", invitationModule.getAllInvitations());
		XMLUtils.appendNewElement(doc, listInvitationsElement, "RegistrationBaseURL", invitationModule.getRegistrationBaseURL());
		
		return new SimpleForegroundModuleResponse(doc);
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();

		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));

		if (user != null) {
			document.appendChild(user.toXML(doc));
		}

		doc.appendChild(document);

		return doc;
	}
}
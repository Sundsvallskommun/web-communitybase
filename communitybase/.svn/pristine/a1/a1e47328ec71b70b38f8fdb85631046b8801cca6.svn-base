package se.dosf.communitybase.modules.notifications;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.interfaces.NotificationHandler;
import se.unlogic.hierarchy.backgroundmodules.AnnotatedBackgroundModule;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleResponse;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;


public class NotificationBackgroundModule extends AnnotatedBackgroundModule {

	@InstanceManagerDependency(required = true)
	protected NotificationHandler notificationHandler;
	
	@Override
	protected BackgroundModuleResponse processBackgroundRequest(HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		if(user == null){
			
			return null;
		}
		
		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		doc.appendChild(documentElement);
		
		Integer unreadCount = notificationHandler.getUnreadCount(user.getUserID());
		
		if(unreadCount > 0){
			
			XMLUtils.appendNewElement(doc, documentElement, "UnreadCount", unreadCount);
		}
		
		XMLUtils.appendNewElement(doc, documentElement, "NotificationHandlerURL", notificationHandler.getUrl(req));
		
		return new SimpleBackgroundModuleResponse(doc);
	}
}

package se.dosf.communitybase.modules.logout;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.foregroundmodules.SimpleForegroundModule;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

public class LogoutModule extends SimpleForegroundModule {

	public ForegroundModuleResponse processRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws IOException {

		HttpSession session = req.getSession();

		if(session != null){
			try{
				//This step is done in order to able to separate manual logins from sessions timeouts
				session.removeAttribute("user");
				
				session.invalidate();

				log.info("User " + user + " logged out!");
			}catch(IllegalStateException e){
				log.info("Unable to logout user " + user + " session already invalidated");
			}
		}

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		doc.appendChild(document);
		document.appendChild(doc.createElement("LoggedOut"));

		return new SimpleForegroundModuleResponse(doc,true);
	}
}

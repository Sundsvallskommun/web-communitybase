package se.dosf.communitybase.modules.logout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.ModuleSetting;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

public class LogoutModule extends AnnotatedForegroundModule {

	@ModuleSetting(allowsNull=true)
	@TextFieldSettingDescriptor(name="Redirect URL",description="If this field is set then users will be redirected on logout. If the url does not begin with http:// or https:// then the contextpath will be appended to the beginning of the given url.")
	protected String redirectURL;
	
	protected boolean relativeRedirectURL;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		
		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		this.checkSettings();
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);
	
		this.checkSettings();
	}
	
	protected void checkSettings() {

		String redirectURL = this.redirectURL;
		
		if(redirectURL != null){
			
			redirectURL = redirectURL.toLowerCase();
			
			if(redirectURL.startsWith("http://") || redirectURL.startsWith("https://")){
				
				relativeRedirectURL = false;
				
			}else{
				
				relativeRedirectURL = true;
			}
		}
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		HttpSession session = req.getSession();

		if(session != null){
			try{
				
				session.removeAttribute("user");
				
				session.invalidate();

				log.info("User " + user + " logged out!");
				
			} catch(IllegalStateException e){
				log.info("Unable to logout user " + user + " session already invalidated");
			}
		}
		
		if(redirectURL != null){
			
			if(relativeRedirectURL){
				
				res.sendRedirect(req.getContextPath() + redirectURL);
				
			}else{
				
				res.sendRedirect(redirectURL);
			}
		}

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		doc.appendChild(document);
		document.appendChild(doc.createElement("LoggedOut"));

		return new SimpleForegroundModuleResponse(doc,true);
	
	}

}

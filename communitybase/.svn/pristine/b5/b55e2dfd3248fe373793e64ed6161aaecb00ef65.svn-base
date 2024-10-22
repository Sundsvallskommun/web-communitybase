package se.dosf.communitybase.modules.mysettings;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.enums.ModuleType;
import se.dosf.communitybase.modules.AnnotatedGlobalModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.populators.CommunityUserPopulator;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class MySettingsModule extends AnnotatedGlobalModule implements CommunityModule {

	private static final CommunityUserPopulator USER_POPULATOR = new CommunityUserPopulator();
	private CommunityUserDAO userDAO;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
	
		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		this.userDAO = new CommunityUserDAO(dataSource);
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {
		
		super.update(moduleDescriptor, dataSource);
		
		this.userDAO = new CommunityUserDAO(dataSource);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {
		
		ValidationException validationException = null;
		
		if(req.getMethod().equalsIgnoreCase("POST")) {
			
			try {
				String password = user.getPassword();
				
				String email = req.getParameter("email");
				
				// check if user changed email address and if so, check if email already exist.
				if(email != null && !email.equals(user.getEmail())){
					
					//TODO also check for existing email address in invitations
					CommunityUser userExist = this.userDAO.getUser(email, false, false);
					
					if(userExist != null){
						throw new ValidationException(new ValidationError("UserAlreadyExist"));
					}
					
				}
				
				boolean updatePassword = req.getParameter("changepassword") != null;
				
				if(!updatePassword){
					
					user.setPassword(password);
				}
				
				user = USER_POPULATOR.populate(user, req);
				
				this.userDAO.update(user, updatePassword, false, false);
				
				log.info("User " + user + " updated user settings");
				
				res.sendRedirect(uriParser.getCurrentURI(true) + this.sectionInterface.getSectionDescriptor().getUserDefaultURI());
				
				return null;
				
			}catch (ValidationException e) {
				
				validationException = e;
				
			}
			
		}
		
		log.info("User " + user + " requesting mysettings");
		
		Document doc = this.createDocument(req, uriParser, user);
		
		Element mySettingsElement = doc.createElement("MySettingsModule");
		
		doc.getFirstChild().appendChild(mySettingsElement);
		
		mySettingsElement.appendChild(user.toXML(doc));
		
		if(validationException != null){
			mySettingsElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			mySettingsElement.appendChild(validationException.toXML(doc));
		}
		
		return new SimpleForegroundModuleResponse(doc, this.getModuleBreadcrumb());
		
	}
	

	@Override
	public ModuleType getModuleType() {
		return ModuleType.Administration;
	}

	@Override
	public List<? extends Event> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {
		// No resume in MysettingsModule
		return null;
	}
	
	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityUser user){

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		doc.appendChild(document);

		return doc;
		
	}
	
}

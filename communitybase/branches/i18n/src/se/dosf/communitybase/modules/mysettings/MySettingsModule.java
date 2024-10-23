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
import se.dosf.communitybase.modules.AnotatedGlobalModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.populators.CommunityUserPopulator;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.modules.ModuleSetting;
import se.unlogic.standardutils.i18n.Language;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class MySettingsModule extends AnotatedGlobalModule implements CommunityModule {

	private static final CommunityUserPopulator USER_POPULATOR = new CommunityUserPopulator();
	private CommunityUserDAO userDAO;

	@ModuleSetting(allowsNull=false)
	@CheckboxSettingDescriptor(name="i18n support",description="Internationalization support, i.e. multiple languages")
	private boolean i18n = false;

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

				String email = req.getParameter("email");

				// check if user changed email address and if so, check if email already exist.
				if(email != null && !email.equals(user.getEmail())){

					CommunityUser userExist = this.userDAO.getUser(email, false, false);

					if(userExist != null){
						throw new ValidationException(new ValidationError("UserAlreadyExist"));
					}

				}

				user = USER_POPULATOR.populate(user, req);
				
				this.userDAO.update(user, (req.getParameter("changepassword") != null), false, false);
				
				// If internationalization is supported and user has set a language
				if(this.i18n && user.getLanguage() != null) {
					req.getSession().removeAttribute("language");
				}

				log.info("User " + user + " updated user settings");

				//res.sendRedirect(uriParser.getCurrentURI(true) + this.sectionInterface.getSectionDescriptor().getUserDefaultURI());
				
				Language language;
				SectionDescriptor sectionDescriptor;
				String uri;
				
				if (!user.isAdmin()) {
					
					if((language = user.getLanguage()) != null) {
						sectionDescriptor = this.sectionInterface.getSystemInterface().getRootSection().getSectionCache().getEntry(language.getLanguageCode()).getKey();						
						uri = req.getContextPath() + "/" + sectionDescriptor.getAlias();
					} else {
						sectionDescriptor = this.sectionInterface.getSystemInterface().getRootSection().getSectionDescriptor();
						uri = req.getContextPath();
					}

					res.sendRedirect(uri + sectionDescriptor.getUserDefaultURI());

				} else {

					if((language = user.getLanguage()) != null) {
						sectionDescriptor = this.sectionInterface.getSystemInterface().getRootSection().getSectionCache().getEntry(language.getLanguageCode()).getKey();
						uri = req.getContextPath() + "/" + sectionDescriptor.getAlias();
					} else {
						sectionDescriptor = this.sectionInterface.getSystemInterface().getRootSection().getSectionDescriptor();
						uri = req.getContextPath();
					}
					
					res.sendRedirect(uri + sectionDescriptor.getAdminDefaultURI());
					
				}

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

		// Append supported languages
		if(this.i18n && !this.systemInterface.getLanguageXSLs().isEmpty()) {
			Element languagesElement = doc.createElement("languages");
			for(Language language : systemInterface.getLanguageXSLs().keySet()) {
				languagesElement.appendChild(language.toXML(doc));

			}
			mySettingsElement.appendChild(languagesElement);
		}

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

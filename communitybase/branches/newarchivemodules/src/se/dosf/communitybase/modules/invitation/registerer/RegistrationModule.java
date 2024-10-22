package se.dosf.communitybase.modules.invitation.registerer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.GroupRelation;
import se.dosf.communitybase.beans.Invitation;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.daos.InvitationDAO;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.administration.global.GlobalAdminModule;
import se.dosf.communitybase.modules.firstpage.FirstpageModule;
import se.dosf.communitybase.utils.ModuleUtils;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class RegistrationModule extends AnnotatedForegroundModule {

	private static final AnnotatedRequestPopulator<CommunityUser> POPULATOR = new AnnotatedRequestPopulator<CommunityUser>(CommunityUser.class);

	private static final ArrayList<SettingDescriptor> SETTINGDESCRIPTORS = new ArrayList<SettingDescriptor>();
	
	static {
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("defaultResume", "Default resume", "Controls what hour the resume is set on new users", true, "16", new StringIntegerValidator(0, 23)));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextAreaSetting("registerRules", "Register rules", "Registration rules used in registration form", true, "", null));
	}
	
	@ModuleSetting
	protected int defaultResume = 16;
	
	@ModuleSetting
	protected String registerRules = "";

	private InvitationDAO invitationDAO;

	private CommunityUserDAO userDAO;

	@Override
	protected void createDAOs(DataSource dataSource) {

		CommunityUserDAO userDAO = new CommunityUserDAO(dataSource);
		userDAO.setGroupDao(new CommunityGroupDAO(dataSource));
		userDAO.setSchoolDAO(new CommunitySchoolDAO(dataSource));

		this.userDAO = userDAO;

		this.invitationDAO = new InvitationDAO(dataSource);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (uriParser.size() != 3) {
			throw new URINotFoundException(uriParser);
		}

		Integer invitationID = NumberUtils.toInt(uriParser.get(1));

		String linkID = uriParser.get(2);

		if (invitationID == null || linkID == null) {
			throw new URINotFoundException(uriParser);
		}

		Invitation invitation = invitationDAO.getInvitation(invitationID, linkID, true, true);

		if (invitation == null) {
			throw new URINotFoundException(uriParser);
		}

		ValidationException validationException = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {
			try {
				CommunityUser invitedUser = POPULATOR.populate(req);

				if(StringUtils.isEmpty(invitedUser.getPassword())){
					throw new ValidationException(new ValidationError("password", ValidationErrorType.RequiredField));
				}else if(!invitedUser.getPassword().equals(req.getParameter("passwordReference"))){
					throw new ValidationException(new ValidationError("passwordsDontMatch"));
				}

				if(req.getParameter("license") == null){
					throw new ValidationException(new ValidationError("license"));
				}

				invitedUser.setAdded(new Timestamp(System.currentTimeMillis()));
				invitedUser.setAdmin(invitation.isAdmin());
				invitedUser.setEnabled(true);
				invitedUser.setGroups(invitation.getGroups());
				invitedUser.setSchools(invitation.getSchools());
				invitedUser.setEmail(invitation.getEmail());
				invitedUser.setResume(defaultResume);

				log.info("User " + invitedUser + " registered using invitation " + invitation);

				this.userDAO.add(invitedUser);
				this.invitationDAO.delete(invitation);

				FirstpageModule firstpageModule = this.getFirstPageModule();
				
				if (firstpageModule != null) {
					
					if(invitation.getGroups() != null){
						
						for(Entry<CommunityGroup, GroupRelation> groupRelation : invitation.getGroups().entrySet()){

							if(groupRelation.getValue().getAccessLevel().equals(GroupAccessLevel.ADMIN)){
								firstpageModule.groupAdminAdded(groupRelation.getKey(), invitedUser);
							}
							
						}
						
					}
					
				}
				
				GlobalAdminModule globalAdminModule = this.getGlobalAdminModule();
				
				if(globalAdminModule != null){
					
					if((invitation.getSchools() != null && !invitation.getSchools().isEmpty()) || invitation.isAdmin()){
						
						globalAdminModule.regenerateMenu();
						
					}
					
				}
				
				Document doc = this.createDocument(req, uriParser);
				Element registerElement = doc.createElement("registered");
				doc.getFirstChild().appendChild(registerElement);

				registerElement.appendChild(invitedUser.toXML(doc));

				return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), this.getDefaultBreadcrumb());

			} catch (ValidationException e) {
				validationException = e;
			}
		}

		log.info("User " + user + " accessing invitation " + invitation + " from " + req.getRemoteAddr());

		Document doc = this.createDocument(req, uriParser);
		Element registerElement = doc.createElement("register");
		doc.getFirstChild().appendChild(registerElement);

		registerElement.appendChild(invitation.toXML(doc));
		registerElement.appendChild(invitation.toXML(doc));
		XMLUtils.appendNewCDATAElement(doc, registerElement, "registerRules", this.registerRules);

		if (validationException != null) {
			registerElement.appendChild(validationException.toXML(doc));
			registerElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), this.getDefaultBreadcrumb());
	}

	@Override
	public ForegroundModuleResponse methodNotFound(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		return defaultMethod(req, res, user, uriParser);
	}

	public FirstpageModule getFirstPageModule() {

		FirstpageModule firstpageModule = ModuleUtils.getCachedModule(sectionInterface, FirstpageModule.class);

		if (firstpageModule == null) {

			log.warn("Module " + moduleDescriptor + " unable to find FirstpageModule!");
		}

		return firstpageModule;
	}
	
	public GlobalAdminModule getGlobalAdminModule() {

		GlobalAdminModule globalAdminModule = ModuleUtils.getCachedModule(sectionInterface, GlobalAdminModule.class);

		if (globalAdminModule == null) {

			log.warn("Module " + moduleDescriptor + " unable to find GlobalAdminModule!");
		}

		return globalAdminModule;
	}
	
	public Document createDocument(HttpServletRequest req, URIParser uriParser) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		doc.appendChild(document);
		return doc;
	}

	@Override
	public List<SettingDescriptor> getSettings() {

		List<? extends SettingDescriptor> superSettings = super.getSettings();

		if (superSettings != null) {
			ArrayList<SettingDescriptor> combinedSettings = new ArrayList<SettingDescriptor>();

			combinedSettings.addAll(superSettings);
			combinedSettings.addAll(SETTINGDESCRIPTORS);

			return combinedSettings;
		} else {
			return SETTINGDESCRIPTORS;
		}
	}
}

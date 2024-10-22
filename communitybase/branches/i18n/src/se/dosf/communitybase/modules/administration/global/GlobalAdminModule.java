package se.dosf.communitybase.modules.administration.global;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.annotations.GenericAdminMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Invitation;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.daos.InvitationDAO;
import se.dosf.communitybase.modules.AnnotatedAdminModule;
import se.dosf.communitybase.modules.firstpage.FirstpageModule;
import se.dosf.communitybase.populators.EmailPopulator;
import se.dosf.communitybase.utils.AccessUtils;
import se.unlogic.emailutils.framework.EmailUtils;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleMenuItemDescriptor;
import se.unlogic.hierarchy.core.enums.URLType;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.MenuItemDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.modules.ModuleSetting;
import se.unlogic.standardutils.i18n.Language;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.MillisecondTimeUnits;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.LanguagePopulator;

public class GlobalAdminModule extends AnnotatedAdminModule<School> {

	private static final SettingDescriptor INVITATION_TIMEOUT_SETTING_DESCRIPTOR = SettingDescriptor.createTextFieldSetting("invitationTimeout", "Invitation timeout", "The time before invitations added using this module will be deleted specified in days", true, "30", new StringIntegerValidator(1,null));
	private static final SettingDescriptor GROUP_ADMIN_MODULE_ALIAS_SETTING_DESCRIPTOR = SettingDescriptor.createTextFieldSetting("groupAdminModuleAlias", "Group admin module alias", "The full alias to the group admin module with leading and trailing slashes", true, "not set", null);
	private static final SettingDescriptor USE_EMAIL_SETTING_DESCRIPTOR = SettingDescriptor.createCheckboxSetting("useGroupEmail", "Use group emailaddress", "Controls whether groupemail should be used or not", false);
	
	private static final EmailPopulator EMAIL_POPULATOR = new EmailPopulator();
	private static final LanguagePopulator LANGUAGE_POPULATOR = new LanguagePopulator();
	

	private CommunitySchoolDAO schoolDAO;
	private CommunityGroupDAO groupDAO;
	private CommunityUserDAO userDAO;
	private InvitationDAO invitationDAO;

	@XSLVariable
	protected String renameScoolBreadCrumbTest = "Rename school: ";

	@XSLVariable
	protected String updateGroupBreadCrumbTest = "Rename group: ";

	@ModuleSetting
	protected String groupAdminModuleAlias = "not set";

	@ModuleSetting
	protected Integer invitationTimeout = 30;
	
	@ModuleSetting
	protected Boolean useGroupEmail = false;
	
	@ModuleSetting(allowsNull=false)
	private Boolean i18n = false;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorschool, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptorschool, sectionInterface, dataSource);

		this.createDAOs(dataSource);
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		if(this.dataSource != dataSource){
			this.createDAOs(dataSource);
		}

		super.update(moduleDescriptor, dataSource);
	}


	private void createDAOs(DataSource dataSource) {
		this.schoolDAO = new CommunitySchoolDAO(dataSource);
		this.groupDAO = new CommunityGroupDAO(dataSource);
		this.userDAO = new CommunityUserDAO(dataSource);
		this.invitationDAO = new InvitationDAO(dataSource);

		this.schoolDAO.setGroupDAO(groupDAO);
		this.schoolDAO.setUserDAO(userDAO);

		this.groupDAO.setUserDao(userDAO);

		this.userDAO.setGroupDao(groupDAO);
		this.userDAO.setSchoolDAO(schoolDAO);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		return this.globalDefaultMethod(req, res, user, uriParser, null);
	}

	private ForegroundModuleResponse globalDefaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, ValidationException validationException) throws AccessDeniedException, IOException, SQLException {

		List<School> schools;

		if(user.isAdmin()){

			//Show full tree
			log.info("User " + user + " listing system details");

			schools = this.schoolDAO.getSchools(false,true);

		}else if(user.getSchools() == null || user.getSchools().isEmpty()){
			throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor, "User does not have access to any schools");
		}else{

			if(user.getSchools().size() == 1){
				//User only has access to one school redirect user to school
				res.sendRedirect(this.getURI(req) + "/" + user.getSchools().get(0).getSchoolID());
			}

			//Show partial tree
			log.info("User " + user + " listing schools");
			schools = schoolDAO.getSchools(user,false,true);
		}

		Document doc = this.createDocument(req, uriParser, null, user);

		Element systemInfoElement = doc.createElement("systemInfoElement");
		doc.getDocumentElement().appendChild(systemInfoElement);

		XMLUtils.append(doc, systemInfoElement, "schools", schools);
		
		// Append supported languages
		if(this.i18n && !this.systemInterface.getLanguageXSLs().isEmpty()) {
			Element languagesElement = doc.createElement("languages");
			for(Language language : systemInterface.getLanguageXSLs().keySet()) {
				languagesElement.appendChild(language.toXML(doc));
			}
			systemInfoElement.appendChild(languagesElement);
		}

		if(user.isAdmin()){
			XMLUtils.append(doc, systemInfoElement, "admins", userDAO.getAdminUsers(false,false));
			XMLUtils.append(doc, systemInfoElement, "invitations", this.invitationDAO.getAdminInvitations(false, false));
		}

		if (validationException != null) {
			systemInfoElement.appendChild(validationException.toXML(doc));
			systemInfoElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(),getDefaultBreadcrumb());
	}

	//Show school
	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, School school) throws Exception {

		return schoolDefaultMethod(req, res, user, uriParser, school, null);
	}

	private ForegroundModuleResponse schoolDefaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, School school, ValidationException validationException) throws SQLException {

		log.info("User " + user + " listing details for school " + school);

		Document doc = this.createDocument(req, uriParser, school, user);

		Element schoolInfoElement = doc.createElement("schoolInfoElement");
		doc.getDocumentElement().appendChild(schoolInfoElement);

		//Append school, groups, admins
		schoolInfoElement.appendChild(school.toXML(doc));

		//Append invitations
		XMLUtils.append(doc, schoolInfoElement,"invitations", this.invitationDAO.getInvitations(school, false, false));
		
		XMLUtils.appendNewElement(doc, schoolInfoElement, "useGroupEmail", this.useGroupEmail);
		
		// Append supported languages
		if(this.i18n && !this.systemInterface.getLanguageXSLs().isEmpty()) {
			Element languagesElement = doc.createElement("languages");
			for(Language language : systemInterface.getLanguageXSLs().keySet()) {
				languagesElement.appendChild(language.toXML(doc));
			}
			schoolInfoElement.appendChild(languagesElement);
		}

		if (validationException != null) {
			schoolInfoElement.appendChild(validationException.toXML(doc));
			schoolInfoElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc,school.getName(),getDefaultBreadcrumb(),new Breadcrumb(school.getName(), school.getName(), getFullAlias() + "/" + school.getSchoolID()));
	}

	@WebPublic
	public ForegroundModuleResponse addAdmin(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		if (uriParser.size() != 2) {
			throw new URINotFoundException(sectionInterface, moduleDescriptor, uriParser);
		}

		if (req.getMethod().equalsIgnoreCase("POST")) {

			try {
				String email = EMAIL_POPULATOR.populate(req);
				Language language = null;
				if(this.i18n) {
					language = LANGUAGE_POPULATOR.populate(req);
				}
				
				// Check if there exists a user with this email address
				CommunityUser selectedUser = userDAO.getUser(email, false, false);

				if (selectedUser != null) {

					// User already exists

					if(selectedUser.isAdmin()){

						//User is already admin redirect to default method
						res.sendRedirect(this.getURI(req));
						return null;
					}

					log.info("User " + user + " making user " + selectedUser + " global admin");
					selectedUser.setAdmin(true);
					this.userDAO.update(selectedUser, false,false, false);

					this.regenerateMenu();

				} else {

					// New user, add invitation
					log.info("User " + user + " inviting " + email + " as global admin");

					this.invitationDAO.addAdminInvitation(email, language, new Timestamp(System.currentTimeMillis() + ((long) this.invitationTimeout * MillisecondTimeUnits.DAY)));
				}

				res.sendRedirect(this.getURI(req));
				return null;

			} catch (ValidationException e) {

				return globalDefaultMethod(req, res, user, uriParser, e);
			}
		} else {

			return this.redirectToDefaultMethod(req, res);
		}
	}

	@WebPublic
	public ForegroundModuleResponse removeAdmin(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		if (uriParser.size() != 3 || !NumberUtils.isInt(uriParser.get(2))) {
			throw new URINotFoundException(sectionInterface, moduleDescriptor, uriParser);
		}

		CommunityUser selectedUser = this.getRequestedUser(uriParser.get(2));

		if (selectedUser != null) {

			log.info("User " + user + " removing global admin access from user " + selectedUser);

			selectedUser.setAdmin(false);

			this.userDAO.update(selectedUser, false, false, false);

			this.regenerateMenu();

			res.sendRedirect(this.getURI(req));
			return null;

		} else {
			return this.globalDefaultMethod(req, res, user, uriParser, new ValidationException(new ValidationError("DeleteFailedUserNotFound")));
		}
	}

	@WebPublic
	public ForegroundModuleResponse addSchool(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		if (req.getMethod().equalsIgnoreCase("POST")) {

			try {
				String schoolName = req.getParameter("schoolName");

				if(StringUtils.isEmpty(schoolName)){
					throw new ValidationException(new ValidationError("schoolName", ValidationErrorType.RequiredField));
				}

				School school = new School(null, schoolName);

				log.info("User " + user + " adding school " + school);

				this.schoolDAO.add(school);

				res.sendRedirect(this.getURI(req));
				return null;

			} catch (ValidationException e) {

				return globalDefaultMethod(req, res, user, uriParser, e);
			}
		} else {

			return this.redirectToDefaultMethod(req, res);
		}
	}

	@GenericAdminMethod
	public ForegroundModuleResponse renameSchool(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, School school) throws Exception {

		ValidationException validationException = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {
			try {

				String schoolName = req.getParameter("schoolName");

				if (StringUtils.isEmpty(schoolName)) {
					throw new ValidationException(new ValidationError("schoolName", ValidationErrorType.RequiredField));
				}

				log.info("User " + user + " renaming school " + school + " to " + schoolName);

				school.setName(schoolName);

				this.schoolDAO.update(school);

				List<FirstpageModule> firstpageModules = this.getFirstPageModules();

				if (firstpageModules != null) {
					for (FirstpageModule firstpageModule : firstpageModules) {
						firstpageModule.schoolUpdated(school);
					}
				}

				return this.redirectToDefaultMethod(req, res);

			} catch (ValidationException e) {
				validationException = e;
			}
		}

		Document doc = this.createDocument(req, uriParser, school, user);
		Element renameSchoolElement = doc.createElement("renameSchool");
		doc.getFirstChild().appendChild(renameSchoolElement);

		if (validationException != null) {
			renameSchoolElement.appendChild(validationException.toXML(doc));
			renameSchoolElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc, this.renameScoolBreadCrumbTest + school.getName(), this.getDefaultBreadcrumb(),new Breadcrumb(renameScoolBreadCrumbTest + school.getName(), renameScoolBreadCrumbTest + school.getName(), this.getFullAlias() + "/" + school.getSchoolID() + "/renameSchool"));
	}

	@GenericAdminMethod
	public ForegroundModuleResponse deleteSchool(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, School school) throws Exception {

		log.info("User " + user + " deleting school " + school);

		this.schoolDAO.delete(school);

		List<FirstpageModule> firstpageModules = this.getFirstPageModules();

		if (firstpageModules != null) {
			for (FirstpageModule firstpageModule : firstpageModules) {
				firstpageModule.schoolRemoved(school);
			}
		}

		return this.redirectToDefaultMethod(req, res);
	}

	@GenericAdminMethod
	public ForegroundModuleResponse addSchoolAdmin(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, School school) throws Exception {

		if (req.getMethod().equalsIgnoreCase("POST")) {

			try {
				String email = EMAIL_POPULATOR.populate(req);
				Language language = null;
				if(this.i18n) {
					language = LANGUAGE_POPULATOR.populate(req);
				}
				
				// Check if there exists a user with this email address
				CommunityUser selectedUser = userDAO.getUser(email, true, false);

				if (selectedUser != null) {

					// User already exists

					if(selectedUser.getSchools() != null && selectedUser.getSchools().contains(school)){

						//User is already admin redirect to default method
						res.sendRedirect(this.getURI(req));
						return null;
					}else if(selectedUser.getSchools() == null){
						selectedUser.setSchools(new ArrayList<School>());
					}

					log.info("User " + user + " making user " + selectedUser + " admin of school " + school);
					selectedUser.getSchools().add(school);
					this.userDAO.update(selectedUser, false, false, true);

					this.regenerateMenu();

				} else {

					// New user, add invitation
					log.info("User " + user + " inviting " + email + " as global admin");

					this.invitationDAO.addSchoolInvitation(email, language, school, new Timestamp(System.currentTimeMillis() + ((long) this.invitationTimeout * MillisecondTimeUnits.DAY)));
				}

				return this.redirectToDefaultMethod(req, res, school);

			} catch (ValidationException e) {

				return schoolDefaultMethod(req, res, user, uriParser, school,e);
			}
		} else {

			return this.redirectToDefaultMethod(req, res, school);
		}
	}

	@GenericAdminMethod
	public ForegroundModuleResponse removeSchoolAdmin(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, School school) throws Exception {

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3))) {
			throw new URINotFoundException(sectionInterface, moduleDescriptor, uriParser);
		}

		CommunityUser selectedUser = this.getRequestedUser(uriParser.get(3), school);

		if (selectedUser != null) {

			log.info("User " + user + " removing school admin access for school " + school + " from user " + selectedUser);

			selectedUser.getSchools().remove(school);

			this.userDAO.update(selectedUser, false, false, true);

			this.regenerateMenu();

			return this.redirectToDefaultMethod(req, res, school);

		} else {
			return this.schoolDefaultMethod(req, res, user, uriParser, school, new ValidationException(new ValidationError("DeleteFailedUserNotFound")));
		}
	}

	@GenericAdminMethod
	public ForegroundModuleResponse addGroup(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, School school) throws Exception {

		if (req.getMethod().equalsIgnoreCase("POST")) {

			List<ValidationError> validationErrors = new ArrayList<ValidationError>();	
			
			try {
				String groupName = req.getParameter("groupName");

				if(StringUtils.isEmpty(groupName)){
					validationErrors.add(new ValidationError("groupName", ValidationErrorType.RequiredField));
				}

				String email = null;
				
				if(this.useGroupEmail) {
					email = req.getParameter("email");
					
					if(email != null) {
						if(!email.equals("") && !EmailUtils.isValidEmailAddress(email)){
							validationErrors.add(new ValidationError("email", ValidationErrorType.InvalidFormat));
						} else if(email.equals("")) {
							email = null;
						}
					}
				}
				
				if(!validationErrors.isEmpty()) {
					throw new ValidationException(validationErrors);
				}
				
				CommunityGroup group = new CommunityGroup();

				group.setName(groupName);
				group.setEmail(email);
				group.setSchool(school);

				log.info("User " + user + " adding group " + group + " to school " + school);

				this.groupDAO.add(group);

				FirstpageModule firstpageModule = this.getFirstPageModule();

				if(firstpageModule != null){
					firstpageModule.groupAdded(group);
				}

				return this.redirectToDefaultMethod(req, res, school);

			} catch (ValidationException e) {

				return schoolDefaultMethod(req, res, user, uriParser, school, e);
			}
		} else {

			return this.redirectToDefaultMethod(req, res, school);
		}
	}

	@GenericAdminMethod
	public ForegroundModuleResponse updateGroup(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, School school) throws Exception {

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3))) {
			throw new URINotFoundException(sectionInterface, moduleDescriptor, uriParser);
		}

		CommunityGroup group = this.getRequestedGroup(uriParser.get(3), school);

		if(group != null){
			
			ValidationException validationException = null;

			List<ValidationError> validationErrors = new ArrayList<ValidationError>();
			
			if (req.getMethod().equalsIgnoreCase("POST")) {
				try {

					String groupName = req.getParameter("groupName");

					if(StringUtils.isEmpty(groupName)){
						validationErrors.add(new ValidationError("groupName", ValidationErrorType.RequiredField));
					}
					
					String email = req.getParameter("email");
						
					if(email != null) {
						if(!email.equals("") && !EmailUtils.isValidEmailAddress(email)){
							validationErrors.add(new ValidationError("email", ValidationErrorType.InvalidFormat));
						} else if(email.equals("")) {
							email = null;
						}
					}

					if(!validationErrors.isEmpty()) {
						throw new ValidationException(validationErrors);
					}
					
					log.info("User " + user + " updating group " + group + " to " + groupName);

					group.setName(groupName);
					
					group.setEmail(email);

					this.groupDAO.update(group);

					FirstpageModule firstpageModule = this.getFirstPageModule();

					if(firstpageModule != null){
						firstpageModule.groupUpdated(group);
					}

					return this.redirectToDefaultMethod(req, res, school);

				} catch (ValidationException e) {
					validationException = e;
				}
			}

			Document doc = this.createDocument(req, uriParser, school, user);
			Element updateGroupElement = doc.createElement("updateGroup");
			updateGroupElement.appendChild(group.toXML(doc));
			XMLUtils.appendNewElement(doc, updateGroupElement, "useGroupEmail", this.useGroupEmail);
			doc.getFirstChild().appendChild(updateGroupElement);

			if (validationException != null) {
				updateGroupElement.appendChild(validationException.toXML(doc));
				updateGroupElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			return new SimpleForegroundModuleResponse(doc, this.updateGroupBreadCrumbTest + group.getName(), this.getDefaultBreadcrumb(),new Breadcrumb(school.getName(), school.getName(), this.getFullAlias() + "/" + school.getSchoolID()),new Breadcrumb(this.updateGroupBreadCrumbTest + group.getName(), this.updateGroupBreadCrumbTest + group.getName(), this.getFullAlias() + "/" + school.getSchoolID() + "/updateGroup/" + group.getGroupID()));
		}else{
			return this.schoolDefaultMethod(req, res, user, uriParser, school, new ValidationException(new ValidationError("RenameFailedGroupNotFound")));
		}
	}

	@GenericAdminMethod
	public ForegroundModuleResponse deleteGroup(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, School school) throws Exception {

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3))) {
			throw new URINotFoundException(sectionInterface, moduleDescriptor, uriParser);
		}

		CommunityGroup group = this.getRequestedGroup(uriParser.get(3), school);

		if(group != null){

			log.info("User " + user + " deleting group " + group);

			this.groupDAO.delete(group);

			List<FirstpageModule> firstpageModules = this.getFirstPageModules();

			if (firstpageModules != null) {
				for (FirstpageModule firstpageModule : firstpageModules) {
					firstpageModule.groupRemoved(group);
				}
			}

			return this.redirectToDefaultMethod(req, res, school);

		}else{
			return this.schoolDefaultMethod(req, res, user, uriParser, school, new ValidationException(new ValidationError("DeleteFailedGroupNotFound")));
		}
	}

	@GenericAdminMethod
	public ForegroundModuleResponse removeInvitation(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, School school) throws Exception {

		Invitation invitation = this.getInvitation(req, res, user, uriParser, school);

		if (invitation != null) {

			log.info("User " + user + " removing school " + school + " from invitation " + invitation);

			invitation.getSchools().remove(school);
			invitation.setUpdated(new Timestamp(System.currentTimeMillis()));

			this.invitationDAO.update(invitation,false,true);
		}

		return this.redirectToDefaultMethod(req, res, school);
	}

	@WebPublic
	public ForegroundModuleResponse removeInvitation(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		Invitation invitation = this.getInvitation(req, res, user, uriParser);

		if (invitation != null) {

			log.info("User " + user + " removing global admin access from invitation " + invitation);

			invitation.setAdmin(false);
			invitation.setUpdated(new Timestamp(System.currentTimeMillis()));

			this.invitationDAO.update(invitation,false,false);
		}

		return this.redirectToDefaultMethod(req, res);
	}

	@GenericAdminMethod
	public ForegroundModuleResponse resendInvitation(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, School school) throws Exception {

		Invitation invitation = this.getInvitation(req, res, user, uriParser, school);

		if (invitation != null) {

			log.info("User " + user + " requesting resending of invitation " + invitation);

			invitation.setResend(true);

			this.invitationDAO.update(invitation, false, false);
		}

		return this.redirectToDefaultMethod(req, res, school);
	}

	@WebPublic
	public ForegroundModuleResponse resendInvitation(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		Invitation invitation = this.getInvitation(req, res, user, uriParser);

		if (invitation != null) {

			log.info("User " + user + " requesting resending of invitation " + invitation);

			invitation.setResend(true);

			this.invitationDAO.update(invitation, false, false);
		}

		return this.redirectToDefaultMethod(req, res);
	}

	private Invitation getInvitation(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, School school) throws URINotFoundException, SQLException {

		Integer invitationID;

		if (uriParser.size() != 4 || (invitationID = NumberUtils.toInt(uriParser.get(3))) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor, uriParser);
		}

		Invitation invitation = this.invitationDAO.getInvitation(invitationID, false, true);

		if (invitation == null || invitation.getSchools() == null || !invitation.getSchools().contains(school)) {

			return null;
		}

		return invitation;
	}

	private Invitation getInvitation(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws SQLException, URINotFoundException {

		Integer invitationID;

		if (uriParser.size() != 3 || (invitationID = NumberUtils.toInt(uriParser.get(2))) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor, uriParser);
		}

		Invitation invitation = this.invitationDAO.getInvitation(invitationID, false, false);

		if (invitation == null || !invitation.isAdmin()) {

			return null;
		}

		return invitation;
	}

	@Override
	protected Class<? extends School> getGenericClass() {

		return School.class;
	}

	@Override
	protected School getRequestedBean(Integer schoolID) throws SQLException {

		return this.schoolDAO.getSchool(schoolID, true, true);
	}

	@Override
	protected boolean checkAccess(School school, CommunityUser user) throws AccessDeniedException {

		return AccessUtils.checkAccess(user, school);
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, School school, CommunityUser user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(user.toXML(doc));

		XMLUtils.appendNewCDATAElement(doc, document, "groupAdminModuleAlias", groupAdminModuleAlias);

		if(school != null){
			document.appendChild(school.toXML(doc));
		}

		doc.appendChild(document);

		return doc;
	}

	private CommunityUser getRequestedUser(String userIDString) throws SQLException {

		Integer userID = NumberUtils.toInt(userIDString);

		if (userID != null) {

			CommunityUser user = this.userDAO.findByUserID(userID, false, false);

			if (user != null && user.isAdmin()) {
				return user;
			}
		}

		return null;
	}

	private CommunityUser getRequestedUser(String userIDString, School school) throws SQLException {

		Integer userID = NumberUtils.toInt(userIDString);

		if (userID != null) {

			CommunityUser user = this.userDAO.findByUserID(userID, true, false);

			if (user != null && user.getSchools() != null && user.getSchools().contains(school)) {
				return user;
			}
		}

		return null;
	}

	private CommunityGroup getRequestedGroup(String groupIDString, School school) throws SQLException {

		Integer groupID = NumberUtils.toInt(groupIDString);

		if (groupID != null) {

			CommunityGroup group = this.groupDAO.getGroup(groupID, false);

			if (group != null && group.getSchool().equals(school)) {
				return group;
			}
		}

		return null;
	}
	
	@Override
	public List<? extends SettingDescriptor> getSettings() {

		ArrayList<SettingDescriptor> settings = new ArrayList<SettingDescriptor>();

		settings.add(INVITATION_TIMEOUT_SETTING_DESCRIPTOR);
		settings.add(GROUP_ADMIN_MODULE_ALIAS_SETTING_DESCRIPTOR);
		settings.add(USE_EMAIL_SETTING_DESCRIPTOR);
		settings.add(SettingDescriptor.createCheckboxSetting("i18n", "i18n support", "Internationalization support, i.e. multiple languages", false));

		List<? extends SettingDescriptor> superSettings = super.getSettings();

		if (superSettings != null) {
			settings.addAll(superSettings);
		}

		return settings;
	}

	private ForegroundModuleResponse redirectToDefaultMethod(HttpServletRequest req, HttpServletResponse res, School school) throws IOException {
		res.sendRedirect(this.getURI(req) + "/" + school.getSchoolID());
		return null;
	}

	@Override
	public List<? extends MenuItemDescriptor> getVisibleMenuItems() {

		try {
			SimpleMenuItemDescriptor menuItemDescriptor = new SimpleMenuItemDescriptor();

			menuItemDescriptor.setName(this.moduleDescriptor.getName());
			menuItemDescriptor.setUrl(this.getFullAlias());
			menuItemDescriptor.setUrlType(URLType.RELATIVE_FROM_CONTEXTPATH);
			menuItemDescriptor.setUniqueID(this.moduleDescriptor.getModuleID().toString());
			menuItemDescriptor.setDescription(this.moduleDescriptor.getDescription());
			menuItemDescriptor.setItemType(menuItemType);

			menuItemDescriptor.setAllowedUserIDs(this.userDAO.getAdminAndSchoolAdminIDs());

			return Collections.singletonList((MenuItemDescriptor) menuItemDescriptor);
		} catch (SQLException e) {
			log.error("Unable to get user ID's for admins and school admins", e);
			return null;
		}
	}

	public void regenerateMenu(){
		
		log.debug("Regenerating menu");
		
		this.sectionInterface.getMenuCache().moduleUpdated(moduleDescriptor, this);
	
	}
}

package se.dosf.communitybase.modules.administration.group;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.annotations.GenericAdminMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.beans.GroupRelation;
import se.dosf.communitybase.beans.Invitation;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunityModuleDAO;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.daos.InvitationDAO;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.AnnotatedAdminModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.firstpage.FirstpageModule;
import se.dosf.communitybase.populators.EmailPopulator;
import se.dosf.communitybase.utils.AccessUtils;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.enums.URLType;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.foregroundmodules.ModuleSetting;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.MillisecondTimeUnits;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.EnumRequestPopulator;

public class GroupAdminModule extends AnnotatedAdminModule<CommunityGroup> implements CommunityModule {

	private static final SettingDescriptor INVITATION_TIMEOUT_SETTING_DESCRIPTOR = SettingDescriptor.createTextFieldSetting("invitationTimeout", "Invitation timeout", "The time before invitations added using this module will be deleted specified in days", true, "30", new StringIntegerValidator(1,null));
	private static final SettingDescriptor USE_EMAIL_SETTING_DESCRIPTOR = SettingDescriptor.createCheckboxSetting("useGroupEmail", "Use group emailaddress", "Controls whether groupemail should be used or not", false);
	
	private static final EmailPopulator EMAIL_POPULATOR = new EmailPopulator();
	private static final EnumRequestPopulator<GroupAccessLevel> GROUP_ACCESS_LEVEL_POPULATOR = new EnumRequestPopulator<GroupAccessLevel>(GroupAccessLevel.class, "GroupAccessLevel");

	private CommunityUserDAO userDAO;
	private CommunitySchoolDAO schoolDAO;
	private CommunityGroupDAO groupDAO;
	private InvitationDAO invitationDAO;
	private CommunityModuleDAO moduleDAO;

	@ModuleSetting
	protected Integer invitationTimeout = 30;

	@ModuleSetting
	protected Boolean useGroupEmail = false;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name="Place the invitation area at the top", description="Controls whether to place the invitation area at the top or not")
	protected boolean invitationAreaOnTop = false;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		this.createDAOs(dataSource);
	}

	@Override
	protected void createDAOs(DataSource dataSource) {
		userDAO = new CommunityUserDAO(dataSource);
		groupDAO = new CommunityGroupDAO(dataSource);
		schoolDAO = new CommunitySchoolDAO(dataSource);
		invitationDAO = new InvitationDAO(dataSource);
		moduleDAO = new CommunityModuleDAO(dataSource);

		groupDAO.setUserDao(userDAO);
		userDAO.setGroupDao(groupDAO);
		schoolDAO.setGroupDAO(groupDAO);
		schoolDAO.setUserDAO(userDAO);
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		if (dataSource != this.dataSource) {
			this.createDAOs(dataSource);
		}

		super.update(moduleDescriptor, dataSource);
	}

	@Override
	public List<? extends SettingDescriptor> getSettings() {

		ArrayList<SettingDescriptor> settings = new ArrayList<SettingDescriptor>();

		settings.add(INVITATION_TIMEOUT_SETTING_DESCRIPTOR);
		settings.add(USE_EMAIL_SETTING_DESCRIPTOR);

		List<? extends SettingDescriptor> superSettings = super.getSettings();

		if (superSettings != null) {
			settings.addAll(superSettings);
		}

		return settings;
	}

	@Override
	protected boolean checkAccess(CommunityGroup group, CommunityUser user) {

		return AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN);
	}

	@Override
	protected Class<? extends CommunityGroup> getGenericClass() {

		return CommunityGroup.class;
	}

	@Override
	protected CommunityGroup getRequestedBean(Integer groupID) throws SQLException {

		return this.groupDAO.getGroup(groupID, true);
	}

	public Integer getModuleID() {

		return this.moduleDescriptor.getModuleID();
	}

	public String getModuleName() {
		return this.moduleDescriptor.getName();
	}

	public List<Event> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {
		return null;
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		// This method is not supported by this module

		throw new URINotFoundException(sectionInterface, moduleDescriptor, uriParser);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return defaultMethod(req, res, user, uriParser, group, null);
	}

	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, ValidationException validationException) throws SQLException {

		log.info("User " + user + " listing group details for group " + group);

		Document doc = this.createDocument(req, uriParser, group, user);

		Element groupInfoElement = doc.createElement("GroupInfoElement");
		doc.getDocumentElement().appendChild(groupInfoElement);

		XMLUtils.appendNewElement(doc, groupInfoElement, "invitationAreaOnTop", invitationAreaOnTop);
		
		// Append users
		XMLUtils.append(doc, groupInfoElement, "Users", group.getUsers());

		// Append invitations
		XMLUtils.append(doc, groupInfoElement, "Invitations", this.invitationDAO.getInvitations(group, true, false));

		// Append started CommunityBase modules with with ModuleType Public
		FirstpageModule firstpageModule = this.getFirstPageModule();

		if (firstpageModule != null) {

			Map<ForegroundModuleDescriptor, CommunityModule> moduleMap = firstpageModule.getPublicModuleMap();

			if (!moduleMap.isEmpty()) {

				Element availableModulesElement = doc.createElement("AvailableModules");
				groupInfoElement.appendChild(availableModulesElement);

				for (Entry<ForegroundModuleDescriptor, CommunityModule> moduleEntry : firstpageModule.getPublicModuleMap().entrySet()) {

					// Link!?
					
					CommunityModule module = moduleEntry.getValue();
					
					boolean moduleEnabled = moduleDAO.isModuleEnabled(moduleEntry.getKey().getModuleID(), group);
					
					if(moduleEnabled || AccessUtils.checkAccess(module.getModuleDescriptor(), group)) {
						
						Element moduleElement = moduleEntry.getKey().toXML(doc);
						XMLUtils.appendNewElement(doc, moduleElement, "link", module.getFullAlias(group));
						availableModulesElement.appendChild(moduleElement);
						
					}
				}

				// Append the moduleID of all CommunityBase modules that have been activated in this group
				List<Integer> moduleIDs = this.moduleDAO.getEnabledModules(group);

				if (moduleIDs != null) {

					Element enabledModulesElement = doc.createElement("EnabledModules");
					groupInfoElement.appendChild(enabledModulesElement);

					for (Integer moduleID : moduleIDs) {
						enabledModulesElement.appendChild(XMLUtils.createElement("moduleID", moduleID.toString(), doc));
					}
				}
			}
		}

		if (validationException != null) {
			groupInfoElement.appendChild(validationException.toXML(doc));
			groupInfoElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		// TODO Språkstöd via XSLVariableReader
		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), getModuleBreadcrumb(group));
	}

	@GenericAdminMethod
	public ForegroundModuleResponse addUser(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, AccessDeniedException, IOException, URINotFoundException {

		if (uriParser.size() != 3) {
			throw new URINotFoundException(sectionInterface, moduleDescriptor, uriParser);
		}

		if (req.getMethod().equalsIgnoreCase("POST")) {

			try {
				String email = EMAIL_POPULATOR.populate(req);
				GroupAccessLevel groupAccessLevel = GROUP_ACCESS_LEVEL_POPULATOR.populate(req);

				// TODO validate length
				String comment = req.getParameter("comment");

				GroupRelation groupRelation = new GroupRelation(groupAccessLevel, comment);

				if (groupAccessLevel == GroupAccessLevel.ADMIN && !AccessUtils.checkAccess(user, group.getSchool())) {
					throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Group administrators are not allowed to add new administrators!");
				}

				// Check if there exists a user with this email address
				CommunityUser selectedUser = userDAO.getUser(email, false, true);

				if (selectedUser != null) {

					// User already exists

					GroupAccessLevel currentAccessLevel = selectedUser.getAccessLevel(group);

					if (currentAccessLevel == null) {

						// Add user to this group
						selectedUser.addGroup(group, groupRelation);
						userDAO.update(selectedUser, false ,true, false);

						log.info("User " + user + " added user " + selectedUser + " to group " + group + " with access level " + groupAccessLevel);

					} else if (currentAccessLevel != groupAccessLevel) {

						// Change users access level in this group
						selectedUser.addGroup(group, groupRelation);
						userDAO.update(selectedUser, false ,true, false);

						log.info("User " + user + " changed accesslevel for user " + selectedUser + " in group " + group + " to " + groupAccessLevel);
					} else {

						// No change
						this.redirectToDefaultMethod(req, res, group);
						return null;
					}

					if (currentAccessLevel == null || currentAccessLevel != groupAccessLevel) {

						// TODO send email?

						if (groupAccessLevel == GroupAccessLevel.ADMIN) {

							FirstpageModule firstpageModule = this.getFirstPageModule();

							if (firstpageModule != null) {
								firstpageModule.groupAdminAdded(group, selectedUser);
							}

						} else if (currentAccessLevel == GroupAccessLevel.ADMIN) {

							FirstpageModule firstpageModule = this.getFirstPageModule();

							if (firstpageModule != null) {
								firstpageModule.groupAdminRemoved(group, selectedUser);
							}
						}
					}
				} else {

					// New user, add invitation
					log.info("User " + user + " inviting " + email + " to group " + group);

					this.invitationDAO.addGroupInvitation(email, group, groupRelation, new Timestamp(System.currentTimeMillis() + ((long) this.invitationTimeout * MillisecondTimeUnits.DAY)), useGroupEmail);
				}

				this.redirectToDefaultMethod(req, res, group);
				return null;

			} catch (ValidationException e) {

				return defaultMethod(req, res, user, uriParser, group, e);
			}
		} else {

			this.redirectToDefaultMethod(req, res, group);
			return null;
		}
	}

	@GenericAdminMethod
	public ForegroundModuleResponse updateUser(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, AccessDeniedException, IOException, URINotFoundException {

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3))) {
			throw new URINotFoundException(sectionInterface, moduleDescriptor, uriParser);
		}

		CommunityUser selectedUser = this.getRequestedUser(uriParser.get(3), group);

		if (selectedUser != null) {

			GroupAccessLevel currentAccessLevel = selectedUser.getAccessLevel(group);

			if (!AccessUtils.checkAccess(user, group.getSchool())) {

				// Check so that a group admin isn't demoting himself
				if (user.equals(selectedUser)) {
					redirectToDefaultMethod(req, res, group);
					return null;

				} else if (currentAccessLevel == GroupAccessLevel.ADMIN) {
					throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Group administrators are not allowed to edit other group administrators!");
				}
			}

			ValidationException validationException = null;

			if (req.getMethod().equalsIgnoreCase("POST")) {
				try {

					GroupAccessLevel groupAccessLevel = GROUP_ACCESS_LEVEL_POPULATOR.populate(req);

					// Group admins cant grant other users group admin access without having higher access
					if (groupAccessLevel == GroupAccessLevel.ADMIN && !AccessUtils.checkAccess(user, group.getSchool())) {
						throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Group administrators are not allowed to add new group administrators!");
					}

					selectedUser.getGroupRelation(group).setAccessLevel(groupAccessLevel);

					if (!StringUtils.isEmpty(req.getParameter("comment"))) {
						selectedUser.getGroupRelation(group).setComment(req.getParameter("comment"));
					} else {
						selectedUser.getGroupRelation(group).setComment(null);
					}

					userDAO.update(selectedUser, false ,true, false);

					log.info("User " + user + " updated user " + selectedUser + " in group " + group);

					if (currentAccessLevel != groupAccessLevel) {

						if (groupAccessLevel == GroupAccessLevel.ADMIN) {

							FirstpageModule firstpageModule = this.getFirstPageModule();

							if (firstpageModule != null) {
								firstpageModule.groupAdminAdded(group, selectedUser);
							}

						} else if (currentAccessLevel == GroupAccessLevel.ADMIN) {

							FirstpageModule firstpageModule = this.getFirstPageModule();

							if (firstpageModule != null) {
								firstpageModule.groupAdminRemoved(group, selectedUser);
							}
						}
					}

					redirectToDefaultMethod(req, res, group);
					return null;

				} catch (ValidationException e) {
					validationException = e;
				}
			}

			Document doc = this.createDocument(req, uriParser, group, user);
			Element updateTypeElement = doc.createElement("UpdateUser");
			doc.getFirstChild().appendChild(updateTypeElement);

			updateTypeElement.appendChild(selectedUser.toXML(doc));

			if (validationException != null) {
				updateTypeElement.appendChild(validationException.toXML(doc));
			}

			// TODO Språkstöd via XSLVariableReader
			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), this.getBreadcrumb("Administrera " + group.getName(), "", "", group), this.getBreadcrumb("Uppdatera " + selectedUser.getFirstname() + " " + selectedUser.getLastname(), "", "updateUser/" + selectedUser.getUserID(), group));

		} else {
			return this.defaultMethod(req, res, user, uriParser, group, new ValidationException(new ValidationError("UpdateFailedUserNotFound")));
		}

	}

	@GenericAdminMethod
	public ForegroundModuleResponse removeUser(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, AccessDeniedException, IOException, URINotFoundException {

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3))) {
			throw new URINotFoundException(sectionInterface, moduleDescriptor, uriParser);
		}

		CommunityUser selectedUser = this.getRequestedUser(uriParser.get(3), group);

		if (selectedUser != null) {

			if (!AccessUtils.checkAccess(user, group.getSchool())) {

				// Check that a group admin isnt deleting himself
				if (user.equals(selectedUser)) {
					redirectToDefaultMethod(req, res, group);
					return null;

					// Check that a group admin isnt deleting another group admin
				} else if (selectedUser.getAccessLevel(group) == GroupAccessLevel.ADMIN) {
					throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Group administrators are not allowed to delete other group administrators!");
				}
			}

			log.info("User " + user + " removing " + selectedUser + " from group " + group);

			selectedUser.removeGroup(group);

			this.userDAO.update(selectedUser, false ,true, false);

			if (selectedUser.getAccessLevel(group) == GroupAccessLevel.ADMIN) {
				FirstpageModule firstpageModule = this.getFirstPageModule();

				if (firstpageModule != null) {
					firstpageModule.groupAdminRemoved(group, selectedUser);
				}
			}

			redirectToDefaultMethod(req, res, group);
			return null;

		} else {
			return this.defaultMethod(req, res, user, uriParser, group, new ValidationException(new ValidationError("DeleteFailedUserNotFound")));
		}
	}

	@GenericAdminMethod
	public ForegroundModuleResponse moveUsers(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, AccessDeniedException, IOException, URINotFoundException {

		if(req.getMethod().equalsIgnoreCase("POST")) {
		
			if(uriParser.size() >= 3) {
			
				List<CommunityUser> users = new ArrayList<CommunityUser>();
				
				String[] userIDs = req.getParameterValues("move.userID");
	
				if(userIDs != null) {
					
					for(String userID : userIDs) {
					
						CommunityUser selectedUser = this.userDAO.getUser(Integer.valueOf(userID), false, true);
					
						if(selectedUser != null ) {
							
							users.add(selectedUser);
							
						}
						
					}
					
				}else {
					return this.defaultMethod(req, res, user, uriParser, group, new ValidationException(new ValidationError("NoUserChoosen")));		
				}
				
				if(uriParser.size() == 3) {
				
					Document doc = this.createDocument(req, uriParser, group, user);
					
					Element moveUsers = doc.createElement("MoveUsers");
					
					doc.getFirstChild().appendChild(moveUsers);
					
					XMLUtils.append(doc, moveUsers, users);
					
					moveUsers.appendChild(this.getUserAccessXML(user, doc));
					
					return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), this.getBreadcrumb("Administrera " + group.getName(), "", "", group), this.getBreadcrumb("Flytta användare", "", "moveUsers", group));
					
				}
				
				if(uriParser.size() == 4) {
					
					CommunityGroup selectedGroup = null;
					
					// move users
					if(uriParser.get(3) != null && NumberUtils.isInt(uriParser.get(3)) && (selectedGroup = this.groupDAO.getGroup(Integer.valueOf(uriParser.get(3)), true)) != null) {
						
						boolean adminMoved = false;
						
						for(CommunityUser selectedUser : users) {

							if(selectedGroup.getUsers() == null || (!selectedGroup.getUsers().contains(selectedUser))) {
								
								log.info("User " + user  + " moving user " + selectedUser + " from group " + group + " to group " + selectedGroup);
								
								GroupRelation relation = selectedUser.getGroupRelation(group);
								
								if(!adminMoved && relation != null && relation.getAccessLevel() == GroupAccessLevel.ADMIN) {
									adminMoved = true;
								}
								
								selectedUser.removeGroup(group);
								
								selectedUser.addGroup(selectedGroup, relation);
								
								userDAO.update(selectedUser, false, true, false);
								
							}else {
								log.info("User " + selectedUser + " is already member of group " + selectedGroup + ", skipping");
							}

						}
						
						if(adminMoved) {
						
							FirstpageModule firstpageModule = this.getFirstPageModule();
	
							if (firstpageModule != null) {
															
								firstpageModule.groupAdminRemoved(group, user);
								firstpageModule.groupAdminAdded(selectedGroup, user);
							}
						
						}
						
						this.redirectToDefaultMethod(req, res, group);
						
						return null;
					}
				
				}
			
			}
			
		}

		throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor, uriParser);
	}
	
	@GenericAdminMethod
	public ForegroundModuleResponse enableModule(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, AccessDeniedException, IOException, URINotFoundException {

		return updateModule(req, res, user, uriParser, group, true);
	}

	@GenericAdminMethod
	public ForegroundModuleResponse disableModule(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, AccessDeniedException, IOException, URINotFoundException {

		return updateModule(req, res, user, uriParser, group, false);
	}

	private ForegroundModuleResponse updateModule(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, boolean enable) throws URINotFoundException, IOException, SQLException {

		Integer moduleID;

		if (uriParser.size() != 4 || (moduleID = NumberUtils.toInt(uriParser.get(3))) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor, uriParser);
		}

		FirstpageModule firstpageModule = this.getFirstPageModule();

		if (firstpageModule == null) {

			this.redirectToDefaultMethod(req, res, group);
			return null;
		}

		for (ForegroundModuleDescriptor moduleDescriptor : firstpageModule.getPublicModuleMap().keySet()) {

			boolean moduleEnabled = this.moduleDAO.isModuleEnabled(moduleID, group);
			
			if (moduleDescriptor.getModuleID() == moduleID && (moduleEnabled || AccessUtils.checkAccess(moduleDescriptor, group))) {

				if (enable && !moduleEnabled) {

					log.info("User " + user + " enabling module " + moduleDescriptor + " in group " + group);

					this.moduleDAO.enableModule(moduleDescriptor.getModuleID(), group);

					firstpageModule.groupModuleEnabled(group, moduleDescriptor);

				} else if (!enable && moduleEnabled) {

					log.info("User " + user + " disabling module " + moduleDescriptor + " in group " + group);

					this.moduleDAO.disableModule(moduleDescriptor, group);

					firstpageModule.groupModuleDisabled(group, moduleDescriptor);
				}

				break;
			}
		}

		this.redirectToDefaultMethod(req, res, group);
		return null;
	}

	@GenericAdminMethod
	public ForegroundModuleResponse resendInvitation(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, AccessDeniedException, IOException, URINotFoundException {

		Invitation invitation = this.getInvitation(req, res, user, uriParser, group);

		if (invitation != null) {

			log.info("User " + user + " requesting resending of invitation " + invitation);

			invitation.setResend(true);

			this.invitationDAO.update(invitation, false, false);
		}

		this.redirectToDefaultMethod(req, res, group);
		return null;
	}

	@GenericAdminMethod
	public ForegroundModuleResponse removeInvitation(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, AccessDeniedException, IOException, URINotFoundException {

		Invitation invitation = this.getInvitation(req, res, user, uriParser, group);

		if (invitation != null) {

			log.info("User " + user + " removing group " + group + " from invitation " + invitation);

			invitation.getGroups().remove(group);
			invitation.setUpdated(new Timestamp(System.currentTimeMillis()));

			this.invitationDAO.update(invitation,true,false);
		}

		this.redirectToDefaultMethod(req, res, group);
		return null;
	}

	private Invitation getInvitation(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws URINotFoundException, IOException, SQLException {

		Integer invitationID;

		if (uriParser.size() != 4 || (invitationID = NumberUtils.toInt(uriParser.get(3))) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor, uriParser);
		}

		Invitation invitation = this.invitationDAO.getInvitation(invitationID, true, false);

		if (invitation == null || invitation.getGroups() == null || invitation.getGroups().get(group) == null) {

			return null;
		}

		return invitation;
	}

	private CommunityUser getRequestedUser(String userIDString, CommunityGroup group) throws SQLException {

		Integer userID = NumberUtils.toInt(userIDString);

		if (userID != null) {

			CommunityUser user = this.userDAO.findByUserID(userID, false, true);

			if (user != null && user.getAccessLevel(group) != null) {
				return user;
			}
		}

		return null;
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityGroup group, CommunityUser user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(group.toXML(doc));

		if (AccessUtils.checkAccess(user, group.getSchool())) {
			document.appendChild(doc.createElement("isSchoolAdmin"));
		}

		doc.appendChild(document);

		return doc;
	}

	public Breadcrumb getBreadcrumb(String name, String description, String method, CommunityGroup group) {

		return new Breadcrumb(name, description, this.getFullAlias() + "/" + group.getGroupID() + "/" + method, URLType.RELATIVE_FROM_CONTEXTPATH);
	}
	
	private Element getUserAccessXML(CommunityUser user, Document doc) throws SQLException, AccessDeniedException {
		
		Element schoolsElement = doc.createElement("schools");
		
		if(!user.isAdmin()){
			
			Map<CommunityGroup, GroupRelation> groups = user.getGroupMap();
			
			if (groups != null && !groups.isEmpty()) {
				
				HashMap<School, Element> addedSchools = new HashMap<School, Element>();
				
				List<School> schoolAdmins = user.getSchools() != null ? user.getSchools() : new ArrayList<School>();
				
				for (Entry<CommunityGroup, GroupRelation> entry : groups.entrySet()) {

					School school = entry.getKey().getSchool();

					if(!schoolAdmins.contains(school) && entry.getValue().getAccessLevel() != GroupAccessLevel.MEMBER) {
					
						if(addedSchools.containsKey(school)){
							
							addedSchools.get(school).appendChild(entry.getKey().toXML(doc));
							
						}else {
							
							Element groupsElement = doc.createElement("groups");
							
							groupsElement.appendChild(entry.getKey().toXML(doc));
							
							addedSchools.put(school, groupsElement);
							
						}
					
					}
					
				}
				
				for(School schoolAdmin : schoolAdmins) {
					
					if(!addedSchools.containsKey(schoolAdmin)) {
						
						Element groupsElement = doc.createElement("groups");
						
						XMLUtils.append(doc, groupsElement, this.groupDAO.getGroups(schoolAdmin));
						
						addedSchools.put(schoolAdmin, groupsElement);
					}
					
				}
				
				if(!addedSchools.isEmpty()){
				
					for(School school : addedSchools.keySet()){
						
						Element schoolElement = school.toXML(doc);
						
						schoolElement.appendChild(addedSchools.get(school));
						
						schoolsElement.appendChild(schoolElement);
						
					}
				
				}else{
					throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Permission denied for user " + user);	
				}
				
			}
			
		}else{
			
			List<School> schools = this.schoolDAO.getSchools(false, true);
			
			if(schools != null){
				XMLUtils.append(doc, schoolsElement, schools);
			}
			
		}
	
		return schoolsElement;
		
	}
}

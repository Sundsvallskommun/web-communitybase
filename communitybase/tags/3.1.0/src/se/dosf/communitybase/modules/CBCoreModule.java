package se.dosf.communitybase.modules;

import java.awt.image.BufferedImage;
import java.io.File;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import se.dosf.communitybase.CBConstants;
import se.dosf.communitybase.beans.NewSectionValues;
import se.dosf.communitybase.beans.SimpleForegroundModuleConfiguration;
import se.dosf.communitybase.beans.SimpleRole;
import se.dosf.communitybase.beans.SimpleSectionType;
import se.dosf.communitybase.dao.CBDAOFactory;
import se.dosf.communitybase.enums.ModuleAccessMode;
import se.dosf.communitybase.enums.SectionAccessMode;
import se.dosf.communitybase.events.CBMemberRemovedEvent;
import se.dosf.communitybase.interfaces.CBInterface;
import se.dosf.communitybase.interfaces.ForegroundModuleConfiguration;
import se.dosf.communitybase.interfaces.ModuleConfiguration;
import se.dosf.communitybase.interfaces.NotificationHandler;
import se.dosf.communitybase.interfaces.Role;
import se.dosf.communitybase.interfaces.SectionType;
import se.dosf.communitybase.utils.CBSectionAttributeHelper;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.GroupMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.BaseVisibleModuleDescriptor;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.MutableUser;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleSectionDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.UnableToAddGroupException;
import se.unlogic.hierarchy.core.exceptions.UnableToDeleteGroupException;
import se.unlogic.hierarchy.core.exceptions.UnableToUpdateGroupException;
import se.unlogic.hierarchy.core.exceptions.UnableToUpdateUserException;
import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionCacheListener;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.SystemStartupListener;
import se.unlogic.hierarchy.core.sections.Section;
import se.unlogic.hierarchy.core.utils.UserUtils;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.groupproviders.SimpleGroup;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.collections.KeyAlreadyCachedException;
import se.unlogic.standardutils.collections.KeyNotCachedException;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.datatypes.SimpleEntry;
import se.unlogic.standardutils.image.ImageUtils;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.WritableDirectoryStringValidator;

public class CBCoreModule extends AnnotatedForegroundModule implements CBInterface, SystemStartupListener, SectionCacheListener {

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "File store", description = "The filestore for section logotypes etc.", formatValidator = WritableDirectoryStringValidator.class)
	protected String fileStore;

	@ModuleSetting(allowsNull = true)
	@GroupMultiListSettingDescriptor(name = "Default groups for open section instances", description = "Groups to set on section instances with open section access mode", required = false)
	protected List<Integer> openAccessModeDefaultGroups;

	@ModuleSetting
	@CheckboxSettingDescriptor(name="Check section groups at startup", description="Controls if this module should check the section groups for all sections during startup")
	protected boolean checkGroupsDuringStartup;
	
	protected CBDAOFactory daoFactory;

	protected ConcurrentHashMap<Integer, SimpleSectionType> sectionTypeMap;
	protected ConcurrentHashMap<Integer, SimpleRole> roleMap;

	protected ConcurrentHashMap<Integer, Set<Integer>> sectionMembersMap;

	@InstanceManagerDependency(required = false)
	protected NotificationHandler notificationHandler;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		cacheSectionTypes();
		cacheRoles();

		if (sectionInterface.getSystemInterface().getSystemStatus() == SystemStatus.STARTING) {

			sectionInterface.getSystemInterface().addStartupListener(this);

		} else {

			systemStarted();
		}

		cacheRoles();

		if (!systemInterface.getInstanceHandler().addInstance(CBInterface.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + CBInterface.class.getName());
		}
	}

	@Override
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {

		List<Integer> oldDefaultGroups = null;

		if (openAccessModeDefaultGroups != null) {

			oldDefaultGroups = new ArrayList<Integer>(openAccessModeDefaultGroups);

		}

		super.update(descriptor, dataSource);

		checkOpenAccessModeDefaultGroups(oldDefaultGroups);

	}

	@Override
	protected void moduleConfigured() throws Exception {

		if (fileStore == null) {

			log.warn("No file store configured for module " + moduleDescriptor);
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(CBInterface.class, this);

		super.unload();
	}

	@Override
	public void systemStarted() {

		try {
			if(checkGroupsDuringStartup){
				checkSectionGroups();
			}

		} catch (SQLException e) {

			log.error("Error checking section groups", e);
		}
		
		cacheMembers();
		systemInterface.addSectionCacheListener(this);
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		daoFactory = new CBDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());
	}

	private void cacheSectionTypes() throws SQLException {

		HighLevelQuery<SimpleSectionType> query = new HighLevelQuery<SimpleSectionType>(SimpleSectionType.ADD_ACCESS_GROUPS_RELATION, SimpleSectionType.ALLOWED_ROLES_RELATION, SimpleSectionType.SUPPORTED_BACKGROUND_MODULES_RELATION, SimpleSectionType.SUPPORTED_FOREGROUND_MODULES_RELATION);

		List<SimpleSectionType> sectionTypes = daoFactory.getSectionTypeDAO().getAll(query);

		if (sectionTypes == null) {

			this.sectionTypeMap = new ConcurrentHashMap<Integer, SimpleSectionType>(0);

		} else {

			ConcurrentHashMap<Integer, SimpleSectionType> tempSectionTypeMap = new ConcurrentHashMap<Integer, SimpleSectionType>(sectionTypes.size());

			for (SimpleSectionType sectionType : sectionTypes) {

				tempSectionTypeMap.put(sectionType.getSectionTypeID(), sectionType);
			}

			this.sectionTypeMap = tempSectionTypeMap;
		}
	}

	protected void cacheRoles() throws SQLException {

		List<SimpleRole> roles = daoFactory.getRoleDAO().getAll();

		if (roles == null) {

			this.roleMap = new ConcurrentHashMap<Integer, SimpleRole>(0);

		} else {

			ConcurrentHashMap<Integer, SimpleRole> roleTempMap = new ConcurrentHashMap<Integer, SimpleRole>();

			for (SimpleRole role : roles) {

				roleTempMap.put(role.getRoleID(), role);
			}

			this.roleMap = roleTempMap;
		}

	}

	private void cacheMembers() {

		sectionMembersMap = new ConcurrentHashMap<Integer, Set<Integer>>();

		cacheSectionMembers(systemInterface.getRootSection(), true);

	}

	private void cacheSectionMembers(SectionInterface section, boolean recursive) {

		Integer sectionID = section.getSectionDescriptor().getSectionID();

		List<Group> groups = getRoleGroups(sectionID, true);

		if (groups != null) {

			Set<Integer> sectionMembers = new HashSet<Integer>();

			for (Group group : groups) {

				List<User> users = systemInterface.getUserHandler().getUsersByGroup(group.getGroupID(), false, false);

				if (users != null) {

					for (User user : users) {

						sectionMembers.add(user.getUserID());

					}

				}

			}

			sectionMembersMap.put(sectionID, sectionMembers);

		}

		if (recursive) {

			for (Section subSection : section.getSectionCache().getSections()) {

				cacheSectionMembers(subSection, true);
			}
		}

	}

	private void cacheSectionMember(Integer sectionID, Integer userID) {

		Set<Integer> sectionMembers = sectionMembersMap.get(sectionID);

		if (sectionMembers == null) {

			sectionMembers = new HashSet<Integer>(1);
		}

		sectionMembers.add(userID);

		sectionMembersMap.put(sectionID, sectionMembers);

	}

	private void unCacheSectionMember(Integer sectionID, Integer userID) {

		Set<Integer> sectionMembers = sectionMembersMap.get(sectionID);

		if (sectionMembers != null) {

			sectionMembers.remove(userID);

			sectionMembersMap.put(sectionID, sectionMembers);
		}

	}

	private void checkOpenAccessModeDefaultGroups(List<Integer> oldDefaultGroups) throws SQLException {

		List<SimpleSectionDescriptor> sectionDescriptors = getSections(SectionAccessMode.OPEN);

		if (sectionDescriptors != null) {

			for (SimpleSectionDescriptor sectionDescriptor : sectionDescriptors) {

				List<Integer> allowedGroupIDs = sectionDescriptor.getAllowedGroupIDs();

				if (allowedGroupIDs != null && oldDefaultGroups != null) {

					allowedGroupIDs.removeAll(oldDefaultGroups);
				}

				if (openAccessModeDefaultGroups != null) {

					if (allowedGroupIDs == null) {
						allowedGroupIDs = new ArrayList<Integer>(openAccessModeDefaultGroups.size());
					}

					allowedGroupIDs.addAll(openAccessModeDefaultGroups);

					CollectionUtils.removeDuplicates(allowedGroupIDs);

				} else {

					sectionDescriptor.setUserAccess(true);
				}

				sectionDescriptor.setAllowedGroupIDs(allowedGroupIDs);

				systemInterface.getCoreDaoFactory().getSectionDAO().update(sectionDescriptor);

				// check if we need to update section cache
				SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionDescriptor.getSectionID());

				if (sectionInterface != null) {

					sectionInterface.getParentSectionInterface().getSectionCache().update(sectionDescriptor);
				}
			}
		}
	}

	private void checkSectionGroups() throws SQLException {

		//Check that there is a group for each allowed role in each section based on the section type configuration
		for (SimpleSectionType sectionType : sectionTypeMap.values()) {

			List<SimpleSectionDescriptor> sectionDescriptors = getSections(sectionType);

			if (sectionDescriptors != null) {

				//Iterate over all section instances
				for (SimpleSectionDescriptor sectionDescriptor : sectionDescriptors) {

					//Get all groups for this section
					List<Group> sectionGroups = systemInterface.getGroupHandler().getGroupsByAttribute(CBConstants.GROUP_SECTION_ID_ATTRIBUTE, sectionDescriptor.getSectionID().toString(), true);

					if (sectionType.getAllowedRoles() != null) {

						//Iterate over all roles
						roleLoop: for (SimpleRole role : sectionType.getAllowedRoles()) {

							if (sectionGroups != null) {

								//Find a group matching the role being iterated over
								for (Group group : sectionGroups) {

									Integer roleID = group.getAttributeHandler().getInt(CBConstants.GROUP_ROLE_ID_ATTRIBUTE);

									if (roleID != null && role.getRoleID().equals(roleID)) {

										//Matching group found, removing group from list
										sectionGroups.remove(group);

										continue roleLoop;
									}
								}
							}

							log.warn("No group found for role " + role + " in sectionID " + sectionDescriptor.getSectionID());

							//TODO add group
						}
					}

					//Check if there are any leftover groups
					if (!CollectionUtils.isEmpty(sectionGroups)) {

						//Check if the leftover groups are role groups or just section related in some other way
						for (Group group : sectionGroups) {

							Integer roleID = group.getAttributeHandler().getInt(CBConstants.GROUP_ROLE_ID_ATTRIBUTE);

							if (roleID != null) {

								log.warn("Leftover group for roleID " + roleID + " found in sectionID " + sectionDescriptor.getSectionID());

								//TODO remove group
							}
						}
					}
				}
			}
		}
	}

	private List<SimpleSectionDescriptor> getSections(SimpleSectionType sectionType) throws SQLException {

		return this.systemInterface.getCoreDaoFactory().getSectionDAO().getSectionsByAttribute(CBConstants.SECTION_ATTRIBUTE_SECTION_TYPE_ID, sectionType.getSectionTypeID().toString(), true);
	}

	private List<SimpleSectionDescriptor> getSections(SectionAccessMode accessMode) throws SQLException {

		return this.systemInterface.getCoreDaoFactory().getSectionDAO().getSectionsByAttribute(CBConstants.SECTION_ATTRIBUTE_ACCESS_MODE, accessMode.name(), true);
	}

	@Override
	public List<Integer> getSectionMembers(Integer sectionID) {

		Set<Integer> sectionMembers = sectionMembersMap.get(sectionID);

		if (sectionMembers == null) {

			return new ArrayList<Integer>(1);
		}

		return new ArrayList<Integer>(sectionMembers);
	}

	@Override
	public Role getRole(Integer sectionID, User user) {

		Group group = UserUtils.getGroupByAttribute(user, CBConstants.GROUP_SECTION_ID_ATTRIBUTE, sectionID);

		if (group != null) {

			AttributeHandler attributeHandler = group.getAttributeHandler();

			if (attributeHandler != null) {

				Integer roleID = attributeHandler.getInt(CBConstants.GROUP_ROLE_ID_ATTRIBUTE);

				if (roleID != null) {

					return roleMap.get(roleID);
				}
			}
		}

		return null;
	}

	@Override
	public boolean setUserRole(User user, Integer sectionID, Integer roleID) {

		Group newGroup = getGroup(roleID, sectionID, true);

		if (newGroup != null) {

			Group currentGroup = null;

			synchronized (user) {

				boolean hasChanges = false;

				if (user.getGroups() != null) {

					currentGroup = UserUtils.getGroupByAttribute(user, CBConstants.GROUP_SECTION_ID_ATTRIBUTE, sectionID);

					if (currentGroup != null) {

						user.getGroups().remove(currentGroup);

						hasChanges = true;
					}

					if(!user.getGroups().contains(newGroup)) {

						user.getGroups().add(newGroup);

						hasChanges = true;
					}

				} else {

					((MutableUser) user).setGroups(CollectionUtils.getList(newGroup));

					hasChanges = true;
				}

				if(!hasChanges) {

					return true;
				}
			}

			try {

				systemInterface.getUserHandler().updateUser(user, false, true, false);

				cacheSectionMember(sectionID, user.getUserID());

				List<User> users = User.getLoggedInUsers();

				for (User loggedInUser : users) {

					if (loggedInUser.equals(user) && user instanceof MutableUser) {

						synchronized (loggedInUser) {

							if (loggedInUser.getGroups() != null) {

								if (currentGroup != null) {

									loggedInUser.getGroups().remove(currentGroup);
								}

								if(!loggedInUser.getGroups().contains(newGroup)) {
									loggedInUser.getGroups().add(newGroup);
								}

							} else {

								((MutableUser) loggedInUser).setGroups(CollectionUtils.getList(newGroup));
							}

						}

					}
				}

				return true;

			} catch (UnableToUpdateUserException e) {

				log.error("Unable to update user " + user, e);
			}

		}

		return false;
	}

	@Override
	public boolean setUserRole(Integer userID, Integer sectionID, Integer roleID) {

		User user = systemInterface.getUserHandler().getUser(userID, true, true);

		if (user != null) {

			return setUserRole(user, sectionID, roleID);

		}

		return false;
	}

	@Override
	public boolean removeUser(User user, Integer sectionID) {

		try {

			Group group = UserUtils.getGroupByAttribute(user, CBConstants.GROUP_SECTION_ID_ATTRIBUTE, sectionID);

			synchronized (user) {

				if (group != null && user.getGroups() != null) {

					user.getGroups().remove(group);

				}

				systemInterface.getUserHandler().updateUser(user, false, true, false);

			}

			List<User> users = User.getLoggedInUsers();

			for (User loggedInUser : users) {


				if (loggedInUser.equals(user) && user instanceof MutableUser) {

					synchronized (loggedInUser) {

						if (user.getGroups() != null) {

							loggedInUser.getGroups().remove(group);
						}
					}

				}
			}

			unCacheSectionMember(sectionID, user.getUserID());
			
			
			SectionAccessMode sectionAccessMode = null;

			SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionID);

			if (sectionInterface != null) {

				sectionAccessMode = CBSectionAttributeHelper.getAccessMode(sectionInterface.getSectionDescriptor());
			}

			this.systemInterface.getEventHandler().sendEvent(SimpleSectionDescriptor.class, new CBMemberRemovedEvent(user.getUserID(), sectionID, sectionAccessMode), EventTarget.ALL);

			return true;

		} catch (UnableToUpdateUserException e) {

			log.error("Unable to update user " + user, e);
		}

		return false;
	}

	@Override
	public boolean removeUser(Integer userID, Integer sectionID) {

		User user = systemInterface.getUserHandler().getUser(userID, true, true);

		if (user != null) {

			return removeUser(user, sectionID);

		}

		return false;

	}

	@Override
	public List<Role> getRoles(Integer sectionID) {

		// TODO Only return allowed roles for given sectionID

		return new ArrayList<Role>(roleMap.values());
	}

	@Override
	public Role getSectionRole(Integer sectionID, Integer roleID) {

		// TODO filter by sectionID

		return roleMap.get(roleID);
	}

	@Override
	public Group getGroup(Integer roleID, Integer sectionID, boolean attributes) {

		List<Entry<String, String>> attributeEntries = new ArrayList<Entry<String, String>>(2);
		attributeEntries.add(new SimpleEntry<String, String>(CBConstants.GROUP_SECTION_ID_ATTRIBUTE, sectionID.toString()));
		attributeEntries.add(new SimpleEntry<String, String>(CBConstants.GROUP_ROLE_ID_ATTRIBUTE, roleID.toString()));

		return systemInterface.getGroupHandler().getGroupByAttributes(attributeEntries, attributes);

	}

	public List<Group> getRoleGroups(Integer sectionID, boolean attributes) {

		return systemInterface.getGroupHandler().getGroupsByAttribute(CBConstants.GROUP_SECTION_ID_ATTRIBUTE, sectionID.toString(), attributes);
	}

	@Override
	public List<SectionType> getAvailableSectionTypes(User user) {

		if (user == null || user.getGroups() == null) {

			return null;
		}

		List<SectionType> sectionTypes = new ArrayList<SectionType>(sectionTypeMap.size());

		outer: for (SectionType sectionType : sectionTypeMap.values()) {

			if (sectionType.getAddAccessGroupIDs() != null) {

				for (Group group : user.getGroups()) {

					if (sectionType.getAddAccessGroupIDs().contains(group.getGroupID())) {

						sectionTypes.add(sectionType);

						continue outer;
					}
				}
				
			} else {
				
				sectionTypes.add(sectionType);
			}
		}

		if (sectionTypes.isEmpty()) {

			return null;
		}

		return sectionTypes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SectionInterface addSection(NewSectionValues newSectionValues, User creator) throws SQLException, UnableToAddGroupException, UnableToUpdateUserException {

		//TODO this method need better error handling and transaction support where it is possible/available

		SimpleSectionType sectionType = sectionTypeMap.get(newSectionValues.getSectionTypeID());

		if (sectionType == null) {

			throw new RuntimeException("SectionType " + newSectionValues.getSectionTypeID() + " not found");
		}

		SimpleSectionDescriptor sectionDescriptor = new SimpleSectionDescriptor();

		sectionDescriptor.setName(newSectionValues.getName());
		sectionDescriptor.setDescription(newSectionValues.getName());
		sectionDescriptor.setEnabled(false);
		sectionDescriptor.setParentSectionID(systemInterface.getRootSection().getSectionDescriptor().getSectionID());
		sectionDescriptor.setBreadCrumb(true);
		sectionDescriptor.setVisibleInMenu(true);

		//TODO these values should not be hardcoded
		sectionDescriptor.setAnonymousDefaultURI("/overview");
		sectionDescriptor.setUserDefaultURI("/overview");

		//Set temporary alias
		sectionDescriptor.setAlias(newSectionValues.hashCode() + "");

		// Set section access
		setSectionAccess(sectionDescriptor, newSectionValues.getAccessMode());

		//Add the section descriptor so that it gets a proper ID
		systemInterface.getCoreDaoFactory().getSectionDAO().add(sectionDescriptor);

		//Set the alias based on sectionID
		sectionDescriptor.setAlias(sectionDescriptor.getSectionID().toString());
		sectionDescriptor.setFullAlias("/" + sectionDescriptor.getAlias());

		ArrayList<Integer> moduleManagerGroupIDs = null;

		//Generate necessary groups
		if (sectionType.getAllowedRoles() != null) {

			moduleManagerGroupIDs = new ArrayList<Integer>(sectionType.getAllowedRoles().size());

			ArrayList<Integer> groupIDs = new ArrayList<Integer>(sectionType.getAllowedRoles().size());

			for (Role role : sectionType.getAllowedRoles()) {

				SimpleGroup group = new SimpleGroup();

				group.setName(StringUtils.substring(role.getName() + " - " + sectionDescriptor.getName(), 255, "..."));
				group.setEnabled(true);
				group.setDescription(group.getName());
				group.getAttributeHandler().setAttribute(CBConstants.GROUP_ROLE_ID_ATTRIBUTE, role.getRoleID());
				group.getAttributeHandler().setAttribute(CBConstants.GROUP_SECTION_ID_ATTRIBUTE, sectionDescriptor.getSectionID());

				systemInterface.getGroupHandler().addGroup(group);
				groupIDs.add(group.getGroupID());

				//Check if this is the default role for creators
				if (creator != null && sectionType.getCreatorRoleID().equals(role.getRoleID())) {

					//Set the role of the creator
					if (creator instanceof MutableUser) {

						creator.getGroups().add(group);

						systemInterface.getUserHandler().updateUser(creator, false, true, false);
					}
				}

				//Check if the roles has module admin access
				if (role.hasManageModulesAccess()) {

					moduleManagerGroupIDs.add(group.getGroupID());
				}
			}

			//Set groupID's on section descriptor
			sectionDescriptor.setAllowedGroupIDs(CollectionUtils.removeDuplicates(CollectionUtils.combine(groupIDs, sectionDescriptor.getAllowedGroupIDs())));
		}

		if (sectionType.getSupportedForegroundModules() != null) {

			for (ModuleConfiguration moduleConfiguration : sectionType.getSupportedForegroundModules()) {

				SimpleForegroundModuleDescriptor moduleDescriptor = systemInterface.getCoreDaoFactory().getForegroundModuleDAO().getModule(moduleConfiguration.getModuleID());

				if (moduleDescriptor == null) {

					log.warn("Unable to find module descriptor for foreground module with ID " + moduleConfiguration.getModuleID());

					continue;
				}

				configureModuleDescriptor(moduleDescriptor, moduleConfiguration, sectionDescriptor.getSectionID(), moduleManagerGroupIDs);

				systemInterface.getCoreDaoFactory().getForegroundModuleDAO().add(moduleDescriptor);
			}
		}

		if (sectionType.getSupportedBackgroundModules() != null) {

			for (ModuleConfiguration moduleConfiguration : sectionType.getSupportedBackgroundModules()) {

				SimpleBackgroundModuleDescriptor moduleDescriptor = systemInterface.getCoreDaoFactory().getBackgroundModuleDAO().getModule(moduleConfiguration.getModuleID());

				if (moduleDescriptor == null) {

					log.warn("Unable to find module descriptor for background module with ID " + moduleConfiguration.getModuleID());

					continue;
				}

				configureModuleDescriptor(moduleDescriptor, moduleConfiguration, sectionDescriptor.getSectionID(), moduleManagerGroupIDs);

				systemInterface.getCoreDaoFactory().getBackgroundModuleDAO().add(moduleDescriptor);
			}
		}

		//Create section instance bean
		CBSectionAttributeHelper.setSectionType(sectionDescriptor, sectionType);
		CBSectionAttributeHelper.setAccessMode(sectionDescriptor, newSectionValues.getAccessMode());
		CBSectionAttributeHelper.setDescription(sectionDescriptor, newSectionValues.getDescription());

		//Save logotype
		if (newSectionValues.getLogo() != null) {

			setSectionLogo(sectionDescriptor.getSectionID(), newSectionValues.getLogo());
		}

		//Enable the section
		sectionDescriptor.setEnabled(true);

		//Update the section descriptor
		systemInterface.getCoreDaoFactory().getSectionDAO().update(sectionDescriptor);

		SectionInterface sectionInterface = systemInterface.getRootSection().getSectionCache().cache(sectionDescriptor);

		sortMenu(sectionInterface);

		cacheSectionMembers(sectionInterface, true);

		return sectionInterface;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setSectionAccess(SimpleSectionDescriptor sectionDescriptor, SectionAccessMode accessMode) {

		CBSectionAttributeHelper.setAccessMode(sectionDescriptor, accessMode);

		if (accessMode == SectionAccessMode.OPEN) {

			if (openAccessModeDefaultGroups != null) {

				sectionDescriptor.setAllowedGroupIDs(CollectionUtils.removeDuplicates(CollectionUtils.combine(openAccessModeDefaultGroups, sectionDescriptor.getAllowedGroupIDs())));

			} else {

				sectionDescriptor.setUserAccess(true);
			}

		} else if (accessMode == SectionAccessMode.CLOSED || accessMode == SectionAccessMode.HIDDEN) {

			sectionDescriptor.setUserAccess(false);

			if(sectionDescriptor.getAllowedGroupIDs() != null && openAccessModeDefaultGroups != null){

				sectionDescriptor.getAllowedGroupIDs().removeAll(openAccessModeDefaultGroups);
			}

		} else {

			throw new RuntimeException("Unsupported section access mode " + accessMode);
		}

	}

	private String getSectionLogoFilePath(int sectionID) {

		return this.fileStore + File.separator + "sectionlogos" + File.separator + sectionID + ".png";
	}

	private void configureModuleDescriptor(BaseVisibleModuleDescriptor moduleDescriptor, ModuleConfiguration moduleConfiguration, Integer sectionID, ArrayList<Integer> moduleManagerGroupIDs) {

		moduleDescriptor.getAttributeHandler().setAttribute(CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE, moduleDescriptor.getModuleID());

		moduleDescriptor.setModuleID(null);
		moduleDescriptor.setSectionID(sectionID);
		moduleDescriptor.setEnabled(true);

		moduleDescriptor.setAnonymousAccess(false);
		moduleDescriptor.setAdminAccess(false);
		moduleDescriptor.setAllowedGroupIDs(null);
		moduleDescriptor.setAllowedUserIDs(null);

		if (moduleConfiguration.getAccessMode() == ModuleAccessMode.NONE) {

			moduleDescriptor.setUserAccess(false);

		} else if (moduleConfiguration.getAccessMode() == ModuleAccessMode.ALL) {

			moduleDescriptor.setUserAccess(true);

		} else if (moduleConfiguration.getAccessMode() == ModuleAccessMode.ADMINS) {

			moduleDescriptor.setUserAccess(false);
			moduleDescriptor.setAllowedGroupIDs(moduleManagerGroupIDs);

		} else {

			throw new RuntimeException("Unsupported module access mode " + moduleConfiguration.getAccessMode());
		}
	}

	@Override
	public SimpleForegroundModuleDescriptor addForegroundModule(int sectionID, int sectionTypeID, ForegroundModuleConfiguration moduleConfiguration) throws SQLException {

		SimpleForegroundModuleDescriptor moduleDescriptor = systemInterface.getCoreDaoFactory().getForegroundModuleDAO().getModule(moduleConfiguration.getModuleID());

		if (moduleDescriptor == null) {

			return null;
		}

		SectionType sectionType = sectionTypeMap.get(sectionTypeID);

		if (sectionType == null) {

			return null;
		}

		ArrayList<Integer> moduleManagerGroupIDs = getSectionModuleManagersGroupID(sectionID, sectionType);

		configureModuleDescriptor(moduleDescriptor, moduleConfiguration, sectionID, moduleManagerGroupIDs);

		systemInterface.getCoreDaoFactory().getForegroundModuleDAO().add(moduleDescriptor);

		if (notificationHandler != null) {

			try {
				notificationHandler.enableNotifications(sectionID, moduleConfiguration.getModuleID());

			} catch (Exception e) {

				log.error("Error enabling previous notifications for module " + moduleDescriptor + " in section ID " + sectionID, e);
			}
		}

		return moduleDescriptor;
	}

	private ArrayList<Integer> getSectionModuleManagersGroupID(int sectionID, SectionType sectionType) {

		if (CollectionUtils.isEmpty(sectionType.getAllowedRoles())) {

			ArrayList<Integer> moduleManagerGroupIDs = new ArrayList<Integer>(sectionType.getAllowedRoles().size());

			for (Role role : sectionType.getAllowedRoles()) {

				if (role.hasManageModulesAccess()) {

					Group group = getGroup(role.getRoleID(), sectionID, false);

					if (group != null) {

						moduleManagerGroupIDs.add(group.getGroupID());
					}
				}
			}

			return moduleManagerGroupIDs;
		}

		return null;
	}

	//Listen for when section are delete and remove their groups
	@EventListener(channel = SimpleSectionDescriptor.class)
	public void processEvent(CRUDEvent<SimpleSectionDescriptor> event, EventSource eventSource) {

		log.info("Event detected " + event);

		if (event.getAction() == CRUDAction.DELETE) {

			for (SimpleSectionDescriptor sectionDescriptor : event.getBeans()) {

				//Get all role groups for this section
				List<Group> groups = systemInterface.getGroupHandler().getGroupsByAttribute(CBConstants.GROUP_SECTION_ID_ATTRIBUTE, sectionDescriptor.getSectionID().toString(), false);

				if (groups != null) {

					for (Group group : groups) {

						log.info("Deleting role group " + group);

						try {
							systemInterface.getGroupHandler().deleteGroup(group);

						} catch (UnableToDeleteGroupException e) {

							log.error("Unable to delete role group " + group, e);
						}
					}
				}
			}
		}
	}

	@Override
	public File getSectionLogo(int sectionID) {

		File file = new File(getSectionLogoFilePath(sectionID));

		if (file.exists()) {

			return file;
		}

		return null;
	}

	@Override
	public boolean sortMenu(SectionInterface sectionInterface) throws SQLException {

		//Get section type ID
		Integer sectionTypeID = getSectionTypeID(sectionInterface.getSectionDescriptor());

		if (sectionTypeID != null) {

			SectionType sectionType = sectionTypeMap.get(sectionTypeID);

			if (sectionType != null && sectionType.getSupportedForegroundModules() != null) {

				sectionInterface.getMenuCache().sortMenu(new CBMenuSorter(sectionInterface, sectionType));

				return true;
			}
		}

		return false;
	}

	private Integer getSectionTypeID(SectionDescriptor descriptor) throws SQLException {

		return CBSectionAttributeHelper.getSectionTypeID(descriptor);
	}

	@Override
	public void deleteSectionLogo(Integer sectionID) {

		FileUtils.deleteFile(getSectionLogoFilePath(sectionID));
	}

	@Override
	public void setSectionLogo(Integer sectionID, BufferedImage image) {

		File file = new File(getSectionLogoFilePath(sectionID));

		file.mkdirs();

		try {
			ImageUtils.writeImage(image, file, ImageUtils.PNG);

		} catch (Exception e) {

			log.error("Error saving image for sectionID " + sectionID, e);
		}
	}

	@Override
	public List<SimpleForegroundModuleConfiguration> getSupportedForegroundModules(Integer sectionTypeID) {

		SimpleSectionType sectionType = sectionTypeMap.get(sectionTypeID);

		if (sectionType != null) {

			return sectionType.getSupportedForegroundModules();
		}

		return null;
	}

	@Override
	public boolean isGlobalAdmin(User user) {

		return false;
	}

	@Override
	public SimpleSectionType getSectionType(Integer sectionTypeID) throws SQLException {

		return sectionTypeMap.get(sectionTypeID);
	}

	@Override
	public void setSectionName(SimpleSectionDescriptor sectionDescriptor, String name) throws SQLException, UnableToUpdateGroupException {

		if (StringUtils.isEmpty(name)) {
			throw new InvalidParameterException();
		}

		sectionDescriptor.setName(name);

		SimpleSectionType sectionType = CBSectionAttributeHelper.getSectionType(sectionDescriptor, this);

		if (sectionType == null) {

			throw new RuntimeException("SectionType " + CBSectionAttributeHelper.getSectionTypeID(sectionDescriptor) + " not found");
		}

		//Update group names
		if (sectionType.getAllowedRoles() != null) {

			List<Group> groups = getRoleGroups(sectionDescriptor.getSectionID(), true);

			if (!CollectionUtils.isEmpty(groups)) {

				for (Group group : groups) {

					Role groupRole = null;
					Integer roleID = group.getAttributeHandler().getInt(CBConstants.GROUP_ROLE_ID_ATTRIBUTE);

					for (Role role : sectionType.getAllowedRoles()) {
						if (role.getRoleID().equals(roleID)) {
							groupRole = role;
							break;
						}
					}

					if (groupRole == null) {

						log.warn("Unable to find matching role for group \"" + group.toString() + "\" in section \"" + sectionDescriptor.toString() + "\" during section rename");
						continue;
					}

					if (group instanceof SimpleGroup) {

						SimpleGroup group2 = (SimpleGroup) group;

						group2.setName(StringUtils.substring(groupRole.getName() + " - " + sectionDescriptor.getName(), 255, "..."));
						group2.setDescription(group.getName());

						systemInterface.getGroupHandler().updateGroup(group, false);
					}
				}
			}
		}
	}

	@Override
	public void sectionCached(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyAlreadyCachedException {

		cacheSectionMembers(sectionInstance, false);
	}

	@Override
	public void sectionUpdated(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyNotCachedException {

	}

	@Override
	public void sectionUnloaded(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyNotCachedException {

	}

}

package se.dosf.communitybase.modules;

import java.awt.image.BufferedImage;
import java.io.File;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import se.dosf.communitybase.CBConstants;
import se.dosf.communitybase.beans.AddSectionType;
import se.dosf.communitybase.beans.NewSectionValues;
import se.dosf.communitybase.beans.SectionAccessRequest;
import se.dosf.communitybase.beans.SectionGroupRole;
import se.dosf.communitybase.beans.SimpleForegroundModuleConfiguration;
import se.dosf.communitybase.beans.SimpleRole;
import se.dosf.communitybase.beans.SimpleSectionType;
import se.dosf.communitybase.dao.CBDAOFactory;
import se.dosf.communitybase.enums.ModuleAccessMode;
import se.dosf.communitybase.enums.SectionAccessMode;
import se.dosf.communitybase.events.CBMemberRemovedEvent;
import se.dosf.communitybase.events.CBSectionAccessRequestEvent;
import se.dosf.communitybase.events.CBSectionActivityEvent;
import se.dosf.communitybase.events.CBSectionPreDeleteEvent;
import se.dosf.communitybase.interfaces.CBInterface;
import se.dosf.communitybase.interfaces.ForegroundModuleConfiguration;
import se.dosf.communitybase.interfaces.ModuleConfiguration;
import se.dosf.communitybase.interfaces.NotificationHandler;
import se.dosf.communitybase.interfaces.Role;
import se.dosf.communitybase.interfaces.SectionType;
import se.dosf.communitybase.modules.securesection.SecureSectionFilterModule;
import se.dosf.communitybase.utils.CBSectionAttributeHelper;
import se.unlogic.cron4jutils.CronStringValidator;
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
import se.unlogic.hierarchy.core.comparators.PriorityComparator;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.UnableToAddGroupException;
import se.unlogic.hierarchy.core.exceptions.UnableToDeleteGroupException;
import se.unlogic.hierarchy.core.exceptions.UnableToUpdateGroupException;
import se.unlogic.hierarchy.core.exceptions.UnableToUpdateUserException;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.listeners.SectionCacheListener;
import se.unlogic.hierarchy.core.interfaces.listeners.SystemStartupListener;
import se.unlogic.hierarchy.core.interfaces.modules.BackgroundModule;
import se.unlogic.hierarchy.core.interfaces.modules.ForegroundModule;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.BackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.sections.Section;
import se.unlogic.hierarchy.core.utils.UserUtils;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.groupproviders.SimpleGroup;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.collections.KeyAlreadyCachedException;
import se.unlogic.standardutils.collections.KeyNotCachedException;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.datatypes.SimpleEntry;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.image.ImageUtils;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.populators.NonNegativeStringIntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.WritableDirectoryStringValidator;

import it.sauronsoftware.cron4j.Scheduler;

public class CBCoreModule extends AnnotatedForegroundModule implements CBInterface, SystemStartupListener, SectionCacheListener, Runnable {

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "File store", description = "The filestore for section logotypes etc.", formatValidator = WritableDirectoryStringValidator.class)
	protected String fileStore;

	@ModuleSetting(allowsNull = true)
	@GroupMultiListSettingDescriptor(name = "Default groups for open section instances", description = "Groups to set on section instances with open section access mode", required = false)
	protected List<Integer> openAccessModeDefaultGroups;

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Section type admin roleID", description = "Role ID user for users who are admins of section types", formatValidator = NonNegativeStringIntegerPopulator.class)
	protected Integer sectionTypeAdminRoleID = 1;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Check section groups at startup", description = "Controls if this module should check the section groups for all sections during startup")
	protected boolean checkGroupsDuringStartup;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Section group members interval", description = "How often section group members should be cached (specified in crontab format)", required = true, formatValidator = CronStringValidator.class)
	private String sectionGroupMembersInterval = "0 4 * * *";

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable secure sections", description = "Whether or not secure sections should be allowed, adds foreground and filter modules")
	protected boolean enableSecureSections;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Secure sections warning", description = "Warning regarding usage of secure sections")
	protected String secureSectionsWarning;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable section statistics", description = "Whether or not to keep stats for different section modules")
	protected boolean enableSectionStatistics;

	private Scheduler scheduler;

	protected CBDAOFactory daoFactory;

	protected ConcurrentHashMap<Integer, SimpleSectionType> sectionTypeMap;
	protected ConcurrentHashMap<Integer, SimpleRole> roleMap;

	protected ConcurrentHashMap<Integer, Set<Integer>> sectionMembersMap;
	protected ConcurrentHashMap<Integer, Set<Integer>> sectionGroupMembersMap;
	protected ConcurrentHashMap<Integer, Set<SectionGroupRole>> sectionGroupsMap;
	protected ConcurrentHashMap<Integer, Set<Integer>> groupSectionsMap;

	protected QueryParameterFactory<SectionGroupRole, Integer> sectionGroupRoleSectionIDParamFactory;
	protected QueryParameterFactory<SectionGroupRole, Integer> sectionGroupRoleGroupIDParamFactory;
	
	protected QueryParameterFactory<SectionAccessRequest, User> accessRequestUserParamFactory;
	protected QueryParameterFactory<SectionAccessRequest, Integer> accessRequestSectionIDParamFactory;

	@InstanceManagerDependency(required = false)
	protected NotificationHandler notificationHandler;

	private final PriorityComparator PRIORITY_COMPARATOR = new PriorityComparator(Order.ASC);

	private SecureSectionFilterModule secureSectionFilterModule;
	
	private Date today;
	private Set<Integer> activeSections;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		cacheSectionTypes();

		sectionInterface.getSystemInterface().addSystemStartupListener(this);

		cacheRoles();

		if (!systemInterface.getInstanceHandler().addInstance(CBInterface.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + CBInterface.class.getName());
		}

		initScheduler();
	}

	@Override
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {

		List<Integer> oldDefaultGroups = null;

		if (openAccessModeDefaultGroups != null) {

			oldDefaultGroups = new ArrayList<Integer>(openAccessModeDefaultGroups);

		}

		super.update(descriptor, dataSource);

		checkOpenAccessModeDefaultGroups(oldDefaultGroups);

		stopScheduler();
		initScheduler();
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

		stopScheduler();

		super.unload();
	}

	@Override
	public void systemStarted() {

		try {
			if (checkGroupsDuringStartup) {
				checkSectionGroups();
			}

			checkSectionAccess();

		} catch (SQLException e) {

			log.error("Error checking section groups", e);
		}

		cacheMembers();
		cacheGroups();
		cacheSectionGroupMembers();
		systemInterface.addSectionCacheListener(this);
		
		today = DateUtils.getCurrentSQLDate(false);
		activeSections = ConcurrentHashMap.newKeySet();
		
		if (enableSecureSections && secureSectionFilterModule == null) {
			systemInterface.getInstanceHandler().removeInstance(CBInterface.class, this);
			
			log.error("Unloading CBCore from instance handler due to non existing secure section filter");
		}
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		daoFactory = new CBDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		sectionGroupRoleSectionIDParamFactory = daoFactory.getSectionGroupRoleDAO().getParamFactory("sectionID", Integer.class);
		sectionGroupRoleGroupIDParamFactory = daoFactory.getSectionGroupRoleDAO().getParamFactory("groupID", Integer.class);
		
		accessRequestUserParamFactory = daoFactory.getAccessRequestDAO().getParamFactory("user", User.class);
		accessRequestSectionIDParamFactory = daoFactory.getAccessRequestDAO().getParamFactory("sectionID", Integer.class);
	}

	private void cacheSectionTypes() throws SQLException {

		HighLevelQuery<SimpleSectionType> query = new HighLevelQuery<SimpleSectionType>(SimpleSectionType.ADD_SECTION_TYPE_RELATION, SimpleSectionType.ADMIN_GROUPS_RELATION, SimpleSectionType.ALLOWED_ROLES_RELATION, SimpleSectionType.SUPPORTED_BACKGROUND_MODULES_RELATION, SimpleSectionType.SUPPORTED_FOREGROUND_MODULES_RELATION);

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

	private void cacheGroups() {

		sectionGroupsMap = new ConcurrentHashMap<Integer, Set<SectionGroupRole>>();
		groupSectionsMap = new ConcurrentHashMap<Integer, Set<Integer>>();

		try {
			List<SectionGroupRole> sectionGroupRoles = daoFactory.getSectionGroupRoleDAO().getAll();

			if (!CollectionUtils.isEmpty(sectionGroupRoles)) {
				for (SectionGroupRole sectionGroupRole : sectionGroupRoles) {
					Set<SectionGroupRole> set = sectionGroupsMap.get(sectionGroupRole.getSectionID());
					Set<Integer> groupSectionSet = groupSectionsMap.get(sectionGroupRole.getGroupID());

					if (set == null) {
						set = new HashSet<SectionGroupRole>();
						sectionGroupsMap.put(sectionGroupRole.getSectionID(), set);
					}

					if (groupSectionSet == null) {
						groupSectionSet = new HashSet<Integer>();
						groupSectionsMap.put(sectionGroupRole.getGroupID(), groupSectionSet);
					}

					set.add(sectionGroupRole);
					groupSectionSet.add(sectionGroupRole.getSectionID());
				}
			}
		} catch (SQLException e) {
			log.error("Unable to cache section group roles", e);
		}
	}

	private void cacheSectionGroupRoles(Section sectionInstance) {

		List<SectionGroupRole> sectionGroupRoles = null;

		try {
			HighLevelQuery<SectionGroupRole> query = new HighLevelQuery<SectionGroupRole>();
			query.addParameter(sectionGroupRoleSectionIDParamFactory.getParameter(sectionInstance.getSectionDescriptor().getSectionID()));

			sectionGroupRoles = daoFactory.getSectionGroupRoleDAO().getAll(query);
		} catch (SQLException e) {
			log.error("Unable to get section group roles for section " + sectionInstance.getSectionDescriptor(), e);
		}

		if (CollectionUtils.isEmpty(sectionGroupRoles)) {
			return;
		}

		Set<SectionGroupRole> set = sectionGroupsMap.get(sectionInstance.getSectionDescriptor().getSectionID());

		if (set == null) {
			set = new HashSet<SectionGroupRole>();
			sectionGroupsMap.put(sectionInstance.getSectionDescriptor().getSectionID(), set);
		}

		for (SectionGroupRole sectionGroupRole : sectionGroupRoles) {
			set.remove(sectionGroupRole);
			set.add(sectionGroupRole);

			Set<Integer> groupSectionSet = groupSectionsMap.get(sectionGroupRole.getGroupID());

			if (groupSectionSet == null) {
				groupSectionSet = new HashSet<Integer>();
				groupSectionsMap.put(sectionGroupRole.getGroupID(), groupSectionSet);
			}

			groupSectionSet.add(sectionGroupRole.getSectionID());
		}
	}

	private void cacheSectionMembers(SectionInterface section, boolean recursive) {

		Integer sectionID = section.getSectionDescriptor().getSectionID();

		List<Group> groups = getRoleGroups(sectionID, true);

		if (groups != null) {

			List<Integer> groupIDs = new ArrayList<Integer>(groups.size());

			for (Group group : groups) {
				groupIDs.add(group.getGroupID());
			}

			List<User> users = systemInterface.getUserHandler().getUsersByGroups(groupIDs, false);

			Set<Integer> sectionMembers = new HashSet<Integer>();

			if (users != null) {

				for (User user : users) {

					sectionMembers.add(user.getUserID());

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

	private void cacheSectionGroup(Integer sectionID, SectionGroupRole sectionGroupRole) {

		Set<SectionGroupRole> sectionGroups = sectionGroupsMap.get(sectionID);

		if (sectionGroups == null) {

			sectionGroups = new HashSet<SectionGroupRole>(1);
		}

		sectionGroups.remove(sectionGroupRole);
		sectionGroups.add(sectionGroupRole);

		sectionGroupsMap.put(sectionID, sectionGroups);

		Set<Integer> groupSectionSet = groupSectionsMap.get(sectionGroupRole.getGroupID());

		if (groupSectionSet == null) {
			groupSectionSet = new HashSet<Integer>();
			groupSectionsMap.put(sectionGroupRole.getGroupID(), groupSectionSet);
		}

		groupSectionSet.add(sectionGroupRole.getSectionID());

		recacheSectionGroupMembers(sectionID);
	}

	private void unCacheSectionGroup(Integer sectionID, SectionGroupRole sectionGroupRole) {

		Set<SectionGroupRole> sectionGroups = sectionGroupsMap.get(sectionID);

		if (sectionGroups != null) {

			sectionGroups.remove(sectionGroupRole);

			sectionGroupsMap.put(sectionID, sectionGroups);
		}

		Set<Integer> groupSectionSet = groupSectionsMap.get(sectionGroupRole.getGroupID());

		if (groupSectionSet != null) {
			groupSectionSet.remove(sectionGroupRole.getSectionID());
		}

		recacheSectionGroupMembers(sectionID);
	}

	private void cacheSectionGroupMembers() {

		if (!CollectionUtils.isEmpty(sectionGroupsMap.values())) {
			ConcurrentHashMap<Integer, Set<Integer>> sectionMembers = new ConcurrentHashMap<Integer, Set<Integer>>(sectionGroupsMap.size());

			for (Entry<Integer, Set<SectionGroupRole>> sectionGroupEntries : sectionGroupsMap.entrySet()) {
				Set<SectionGroupRole> sectionGroupRoles = sectionGroupEntries.getValue();

				if (!CollectionUtils.isEmpty(sectionGroupRoles)) {
					List<Integer> groupIDs = new ArrayList<Integer>(sectionGroupRoles.size());

					for (SectionGroupRole sectionGroupRole : sectionGroupRoles) {
						groupIDs.add(sectionGroupRole.getGroupID());
					}

					List<User> users = systemInterface.getUserHandler().getUsersByGroups(groupIDs, false);

					if (!CollectionUtils.isEmpty(users)) {
						sectionMembers.put(sectionGroupEntries.getKey(), new HashSet<Integer>(UserUtils.getUserIDs(users)));
					}
				}
			}

			sectionGroupMembersMap = sectionMembers;
		} else {
			sectionGroupMembersMap = new ConcurrentHashMap<Integer, Set<Integer>>(1);
		}
	}

	private void recacheSectionGroupMembers(Integer sectionID) {

		Set<SectionGroupRole> sectionGroupRoles = sectionGroupsMap.get(sectionID);

		if (!CollectionUtils.isEmpty(sectionGroupRoles)) {
			List<Integer> groupIDs = new ArrayList<Integer>(sectionGroupRoles.size());

			for (SectionGroupRole sectionGroupRole : sectionGroupRoles) {
				groupIDs.add(sectionGroupRole.getGroupID());
			}

			List<User> users = systemInterface.getUserHandler().getUsersByGroups(groupIDs, false);

			if (!CollectionUtils.isEmpty(users)) {
				sectionGroupMembersMap.put(sectionID, new HashSet<Integer>(UserUtils.getUserIDs(users)));
			}
		} else {
			sectionGroupMembersMap.remove(sectionID);
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

	private void checkSectionAccess() throws SQLException {

		//TODO Update section access groups to include section type admin groups 
	}

	private List<SimpleSectionDescriptor> getSections(SimpleSectionType sectionType) throws SQLException {

		return this.systemInterface.getCoreDaoFactory().getSectionDAO().getSectionsByAttribute(CBConstants.SECTION_ATTRIBUTE_SECTION_TYPE_ID, sectionType.getSectionTypeID().toString(), true);
	}

	private List<SimpleSectionDescriptor> getSections(SectionAccessMode accessMode) throws SQLException {

		return this.systemInterface.getCoreDaoFactory().getSectionDAO().getSectionsByAttribute(CBConstants.SECTION_ATTRIBUTE_ACCESS_MODE, accessMode.name(), true);
	}

	@Override
	public List<Integer> getSectionMembers(Integer sectionID, boolean includeGroupMembers) {

		Set<Integer> sectionMembers = sectionMembersMap.get(sectionID);
		Set<Integer> sectionGroupMembers = null;

		if (includeGroupMembers) {
			sectionGroupMembers = sectionGroupMembersMap.get(sectionID);
		}

		if ((sectionMembers == null && !includeGroupMembers) || (sectionMembers == null && includeGroupMembers && sectionGroupMembers == null)) {
			return new ArrayList<Integer>(1);
		}

		if (sectionGroupMembers != null) {
			if (sectionMembers == null) {
				sectionMembers = sectionGroupMembers;
			} else {
				sectionMembers = new HashSet<Integer>(sectionMembers);
				sectionMembers.addAll(sectionGroupMembers);
			}
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

		Set<SectionGroupRole> sectionGroupRoles = sectionGroupsMap.get(sectionID);

		if (!CollectionUtils.isEmpty(sectionGroupRoles)) {
			ArrayList<SimpleRole> allowedRoles = new ArrayList<SimpleRole>();

			Iterator<SectionGroupRole> iterator = sectionGroupRoles.iterator();

			while (iterator.hasNext()) {
				SectionGroupRole sectionGroupRole = iterator.next();

				SimpleGroup simpleGroup = new SimpleGroup();
				simpleGroup.setGroupID(sectionGroupRole.getGroupID());

				if (user.getGroups().contains(simpleGroup)) {
					allowedRoles.add(roleMap.get(sectionGroupRole.getRoleID()));
				}
			}

			if (!CollectionUtils.isEmpty(allowedRoles)) {
				if (allowedRoles.size() > 1) {
					Collections.sort(allowedRoles, PRIORITY_COMPARATOR);
				}

				return allowedRoles.get(0);
			}
		}

		if (sectionTypeAdminRoleID != null && !CollectionUtils.isEmpty(user.getGroups())) {

			SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionID);

			if (sectionInterface != null) {

				try {
					SectionType sectionType = CBSectionAttributeHelper.getSectionType(sectionInterface.getSectionDescriptor(), this);

					if (sectionType != null && !CollectionUtils.isEmpty(sectionType.getAdminGroupIDs())) {

						for (Group groupID : user.getGroups()) {

							if (sectionType.getAdminGroupIDs().contains(groupID.getGroupID())) {

								return roleMap.get(sectionTypeAdminRoleID);
							}
						}
					}

				} catch (SQLException e) {
					log.error("Error getting section type for " + sectionInterface.getSectionDescriptor(), e);
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

					if (!user.getGroups().contains(newGroup)) {

						user.getGroups().add(newGroup);

						hasChanges = true;
					}

				} else {

					((MutableUser) user).setGroups(CollectionUtils.getList(newGroup));

					hasChanges = true;
				}

				if (!hasChanges) {

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

								if (!loggedInUser.getGroups().contains(newGroup)) {
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
	public boolean setGroupRole(Integer groupID, Integer sectionID, Integer roleID) {

		SectionGroupRole sectionGroupRole = new SectionGroupRole(sectionID, groupID, roleID);

		try {
			SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionID);

			if (sectionInterface != null) {
				if (!sectionInterface.getSectionDescriptor().getAllowedGroupIDs().contains(groupID)) {
					sectionInterface.getSectionDescriptor().getAllowedGroupIDs().add(groupID);
					systemInterface.getCoreDaoFactory().getSectionDAO().update((SimpleSectionDescriptor) sectionInterface.getSectionDescriptor());
				}
			} else {
				return false;
			}

			HighLevelQuery<SectionGroupRole> query = new HighLevelQuery<SectionGroupRole>();
			query.addParameter(sectionGroupRoleGroupIDParamFactory.getParameter(groupID));
			query.addParameter(sectionGroupRoleSectionIDParamFactory.getParameter(sectionID));

			SectionGroupRole existingSGR = daoFactory.getSectionGroupRoleDAO().get(query);

			Role newRole = roleMap.get(roleID);

			SectionType sectionType = sectionTypeMap.get(sectionInterface.getSectionDescriptor().getAttributeHandler().getInt(CBConstants.SECTION_ATTRIBUTE_SECTION_TYPE_ID));

			boolean alreadyManager = false;

			if (existingSGR != null) {
				Role existingRole = roleMap.get(existingSGR.getRoleID());

				if (existingRole.hasManageModulesAccess() && !newRole.hasManageModulesAccess()) {
					removeGroupAdminFromModules(groupID, sectionInterface, sectionType);
				} else if (existingRole.hasManageModulesAccess() && newRole.hasManageModulesAccess()) {
					alreadyManager = true;
				}

				daoFactory.getSectionGroupRoleDAO().delete(sectionGroupRole);
			}

			daoFactory.getSectionGroupRoleDAO().add(sectionGroupRole);

			if (newRole.hasManageModulesAccess() && !alreadyManager) {
				addGroupAdminToModules(groupID, sectionInterface, sectionType);
			}

			cacheSectionGroup(sectionID, sectionGroupRole);

			return true;
		} catch (SQLException e) {

			log.error("Unable to set section group role " + sectionGroupRole, e);
		}

		return false;
	}

	public void addGroupAdminToModules(Integer groupID, SectionInterface sectionInterface, SectionType sectionType) throws SQLException {

		if (!CollectionUtils.isEmpty(sectionType.getSupportedForegroundModules())) {
			for (ForegroundModuleConfiguration moduleConfiguration : sectionType.getSupportedForegroundModules()) {
				if (moduleConfiguration.getAccessMode() == ModuleAccessMode.ADMINS) {
					List<SimpleForegroundModuleDescriptor> foregroundModuleDescriptors = systemInterface.getCoreDaoFactory().getForegroundModuleDAO().getModulesByAttribute(CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE, moduleConfiguration.getModuleID() + "");

					if (!CollectionUtils.isEmpty(foregroundModuleDescriptors)) {
						for (SimpleForegroundModuleDescriptor foregroundModuleDescriptor : foregroundModuleDescriptors) {
							if (!foregroundModuleDescriptor.getSectionID().equals(sectionInterface.getSectionDescriptor().getSectionID())) {
								continue;
							}

							Entry<ForegroundModuleDescriptor, ForegroundModule> entry = sectionInterface.getForegroundModuleCache().getEntry(foregroundModuleDescriptor.getModuleID());

							if (entry != null) {
								ForegroundModuleDescriptor currentDescriptor = entry.getKey();

								currentDescriptor.getAllowedGroupIDs().add(groupID);
							}

							foregroundModuleDescriptor.getAllowedGroupIDs().add(groupID);
							systemInterface.getCoreDaoFactory().getForegroundModuleDAO().update(foregroundModuleDescriptor);
						}
					}
				}
			}
		}

		if (!CollectionUtils.isEmpty(sectionType.getSupportedBackgroundModules())) {
			for (ModuleConfiguration moduleConfiguration : sectionType.getSupportedBackgroundModules()) {
				if (moduleConfiguration.getAccessMode() == ModuleAccessMode.ADMINS) {
					List<SimpleBackgroundModuleDescriptor> backgroundModuleDescriptors = systemInterface.getCoreDaoFactory().getBackgroundModuleDAO().getModulesByAttribute(CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE, moduleConfiguration.getModuleID() + "");

					if (!CollectionUtils.isEmpty(backgroundModuleDescriptors)) {
						for (SimpleBackgroundModuleDescriptor backgroundModuleDescriptor : backgroundModuleDescriptors) {
							if (!backgroundModuleDescriptor.getSectionID().equals(sectionInterface.getSectionDescriptor().getSectionID())) {
								continue;
							}

							Entry<BackgroundModuleDescriptor, BackgroundModule> entry = sectionInterface.getBackgroundModuleCache().getEntry(backgroundModuleDescriptor.getModuleID());

							if (entry != null) {
								BackgroundModuleDescriptor currentDescriptor = entry.getKey();

								currentDescriptor.getAllowedGroupIDs().add(groupID);
							}

							backgroundModuleDescriptor.getAllowedGroupIDs().add(groupID);
							systemInterface.getCoreDaoFactory().getBackgroundModuleDAO().update(backgroundModuleDescriptor);
						}
					}
				}
			}
		}
	}

	public void removeGroupAdminFromModules(Integer groupID, SectionInterface sectionInterface, SectionType sectionType) throws SQLException {

		if (!CollectionUtils.isEmpty(sectionType.getSupportedForegroundModules())) {
			for (ForegroundModuleConfiguration moduleConfiguration : sectionType.getSupportedForegroundModules()) {
				if (moduleConfiguration.getAccessMode() == ModuleAccessMode.ADMINS) {
					List<SimpleForegroundModuleDescriptor> foregroundModuleDescriptors = systemInterface.getCoreDaoFactory().getForegroundModuleDAO().getModulesByAttribute(CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE, moduleConfiguration.getModuleID() + "");

					if (!CollectionUtils.isEmpty(foregroundModuleDescriptors)) {
						for (SimpleForegroundModuleDescriptor foregroundModuleDescriptor : foregroundModuleDescriptors) {
							if (!foregroundModuleDescriptor.getSectionID().equals(sectionInterface.getSectionDescriptor().getSectionID())) {
								continue;
							}

							Entry<ForegroundModuleDescriptor, ForegroundModule> entry = sectionInterface.getForegroundModuleCache().getEntry(foregroundModuleDescriptor.getModuleID());

							if (entry != null) {
								ForegroundModuleDescriptor currentDescriptor = entry.getKey();

								currentDescriptor.getAllowedGroupIDs().remove(groupID);
							}

							foregroundModuleDescriptor.getAllowedGroupIDs().remove(groupID);
							systemInterface.getCoreDaoFactory().getForegroundModuleDAO().update(foregroundModuleDescriptor);
						}
					}
				}
			}
		}

		if (!CollectionUtils.isEmpty(sectionType.getSupportedBackgroundModules())) {
			for (ModuleConfiguration moduleConfiguration : sectionType.getSupportedBackgroundModules()) {
				if (moduleConfiguration.getAccessMode() == ModuleAccessMode.ADMINS) {
					List<SimpleBackgroundModuleDescriptor> backgroundModuleDescriptors = systemInterface.getCoreDaoFactory().getBackgroundModuleDAO().getModulesByAttribute(CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE, moduleConfiguration.getModuleID() + "");

					if (!CollectionUtils.isEmpty(backgroundModuleDescriptors)) {
						for (SimpleBackgroundModuleDescriptor backgroundModuleDescriptor : backgroundModuleDescriptors) {
							if (!backgroundModuleDescriptor.getSectionID().equals(sectionInterface.getSectionDescriptor().getSectionID())) {
								continue;
							}

							Entry<BackgroundModuleDescriptor, BackgroundModule> entry = sectionInterface.getBackgroundModuleCache().getEntry(backgroundModuleDescriptor.getModuleID());

							if (entry != null) {
								BackgroundModuleDescriptor currentDescriptor = entry.getKey();

								currentDescriptor.getAllowedGroupIDs().remove(groupID);
							}

							backgroundModuleDescriptor.getAllowedGroupIDs().remove(groupID);
							systemInterface.getCoreDaoFactory().getBackgroundModuleDAO().update(backgroundModuleDescriptor);
						}
					}
				}
			}
		}
	}

	@Override
	public boolean removeGroup(Integer groupID, Integer sectionID) {

		SectionGroupRole sectionGroupRole = new SectionGroupRole(sectionID, groupID, null);

		try {
			SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionID);

			if (sectionInterface != null) {
				sectionInterface.getSectionDescriptor().getAllowedGroupIDs().remove(groupID);

				systemInterface.getCoreDaoFactory().getSectionDAO().update((SimpleSectionDescriptor) sectionInterface.getSectionDescriptor());
			} else {
				return false;
			}

			HighLevelQuery<SectionGroupRole> query = new HighLevelQuery<SectionGroupRole>();
			query.addParameter(sectionGroupRoleGroupIDParamFactory.getParameter(groupID));
			query.addParameter(sectionGroupRoleSectionIDParamFactory.getParameter(sectionID));

			SectionGroupRole existingSGR = daoFactory.getSectionGroupRoleDAO().get(query);

			if (existingSGR != null) {
				Role role = roleMap.get(existingSGR.getRoleID());

				if (role.hasManageModulesAccess()) {
					SectionType sectionType = sectionTypeMap.get(sectionInterface.getSectionDescriptor().getAttributeHandler().getInt(CBConstants.SECTION_ATTRIBUTE_SECTION_TYPE_ID));

					removeGroupAdminFromModules(groupID, sectionInterface, sectionType);
				}
			}

			daoFactory.getSectionGroupRoleDAO().delete(sectionGroupRole);

			unCacheSectionGroup(sectionID, sectionGroupRole);

			return true;
		} catch (SQLException e) {

			log.error("Unable to set section group role " + sectionGroupRole, e);
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

	@Override
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

			if (sectionType.getAddSectionTypes() != null) {

				for (AddSectionType addSectionType : sectionType.getAddSectionTypes()) {

					for (Group group : user.getGroups()) {

						if (addSectionType.getGroupID().equals(group.getGroupID())) {

							sectionTypes.add(sectionType);

							continue outer;
						}
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
		setSectionAccess(sectionDescriptor, newSectionValues.getAccessMode(), sectionType);

		//Add the section descriptor so that it gets a proper ID
		systemInterface.getCoreDaoFactory().getSectionDAO().add(sectionDescriptor);
		systemInterface.getEventHandler().sendEvent(SectionDescriptor.class, new CRUDEvent<SectionDescriptor>(CRUDAction.ADD, sectionDescriptor), EventTarget.ALL);

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

		if (!CollectionUtils.isEmpty(sectionType.getAdminGroupIDs())) {

			if (moduleManagerGroupIDs == null) {
				moduleManagerGroupIDs = new ArrayList<Integer>(sectionType.getAdminGroupIDs().size());
			}

			moduleManagerGroupIDs.addAll(sectionType.getAdminGroupIDs());
			sectionDescriptor.setAllowedGroupIDs(CollectionUtils.removeDuplicates(CollectionUtils.combine(sectionType.getAdminGroupIDs(), sectionDescriptor.getAllowedGroupIDs())));
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
				systemInterface.getEventHandler().sendEvent(ForegroundModuleDescriptor.class, new CRUDEvent<ForegroundModuleDescriptor>(CRUDAction.ADD, moduleDescriptor), EventTarget.ALL);
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
				systemInterface.getEventHandler().sendEvent(BackgroundModuleDescriptor.class, new CRUDEvent<BackgroundModuleDescriptor>(CRUDAction.ADD, moduleDescriptor), EventTarget.ALL);
			}
		}

		//Create section instance bean
		CBSectionAttributeHelper.setSectionType(sectionDescriptor, sectionType);
		CBSectionAttributeHelper.setAccessMode(sectionDescriptor, newSectionValues.getAccessMode());
		CBSectionAttributeHelper.setDescription(sectionDescriptor, newSectionValues.getDescription());
		CBSectionAttributeHelper.setCreated(sectionDescriptor, TimeUtils.getCurrentTimestamp());
		CBSectionAttributeHelper.setLastActivityDate(sectionDescriptor, today);

		if (newSectionValues.getSectionDeleteDate() != null) {
			CBSectionAttributeHelper.setDeleteOnDate(sectionDescriptor, newSectionValues.getSectionDeleteDate());
		}

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

	@Override
	public void setSectionAccess(SimpleSectionDescriptor sectionDescriptor, SectionAccessMode accessMode, SectionType sectionType) {

		CBSectionAttributeHelper.setAccessMode(sectionDescriptor, accessMode);

		if (accessMode == SectionAccessMode.OPEN) {

			if (openAccessModeDefaultGroups != null) {

				sectionDescriptor.setAllowedGroupIDs(CollectionUtils.removeDuplicates(CollectionUtils.combine(openAccessModeDefaultGroups, sectionDescriptor.getAllowedGroupIDs())));

			} else {

				sectionDescriptor.setUserAccess(true);
			}

		} else if (accessMode == SectionAccessMode.CLOSED || accessMode == SectionAccessMode.HIDDEN) {

			sectionDescriptor.setUserAccess(false);

			if (sectionDescriptor.getAllowedGroupIDs() != null && openAccessModeDefaultGroups != null) {

				sectionDescriptor.getAllowedGroupIDs().removeAll(openAccessModeDefaultGroups);
			}

		} else {

			throw new RuntimeException("Unsupported section access mode " + accessMode);
		}

		if (!CollectionUtils.isEmpty(sectionType.getAdminGroupIDs())) {

			sectionDescriptor.setAllowedGroupIDs(CollectionUtils.removeDuplicates(CollectionUtils.combine(sectionType.getAdminGroupIDs(), sectionDescriptor.getAllowedGroupIDs())));
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
		systemInterface.getEventHandler().sendEvent(ForegroundModuleDescriptor.class, new CRUDEvent<ForegroundModuleDescriptor>(CRUDAction.ADD, moduleDescriptor), EventTarget.ALL);

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

		ArrayList<Integer> moduleManagerGroupIDs = new ArrayList<Integer>();

		if (!CollectionUtils.isEmpty(sectionType.getAdminGroupIDs())) {

			moduleManagerGroupIDs.addAll(sectionType.getAdminGroupIDs());
		}

		if (CollectionUtils.isEmpty(sectionType.getAllowedRoles())) {

			for (Role role : sectionType.getAllowedRoles()) {

				if (role.hasManageModulesAccess()) {

					Group group = getGroup(role.getRoleID(), sectionID, false);

					if (group != null) {

						moduleManagerGroupIDs.add(group.getGroupID());
					}
				}
			}
		}

		return moduleManagerGroupIDs;
	}

	//Listen for when section are delete and remove their groups
	@EventListener(channel = SectionDescriptor.class)
	public void processEvent(CRUDEvent<SectionDescriptor> event, EventSource eventSource) {

		log.info("Event detected " + event);

		if (event.getAction() == CRUDAction.DELETE) {

			for (SectionDescriptor sectionDescriptor : event.getBeans()) {

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
	
	@EventListener(channel = CBSectionActivityEvent.class)
	public void processActivityEvent(CBSectionActivityEvent event, EventSource eventSource) {
		
		if (activeSections.contains(event.getSectionID())) {
			return;
		}
		
		activeSections.add(event.getSectionID());
		
		SectionInterface sectionInterface = systemInterface.getSectionInterface(event.getSectionID());
		
		if (sectionInterface != null) {
			SimpleSectionDescriptor descriptor = (SimpleSectionDescriptor) sectionInterface.getSectionDescriptor();
			
			log.info("Setting today as last active date for section " + descriptor);
			
			CBSectionAttributeHelper.setLastActivityDate(descriptor, today);
			
			try {
				systemInterface.getCoreDaoFactory().getSectionDAO().update(descriptor);
			}
			catch (SQLException ex) {
				log.error("Unable to set last active date for section " + sectionInterface.getSectionDescriptor(), ex);
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
		cacheSectionGroupRoles(sectionInstance);
	}

	@Override
	public void sectionUpdated(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyNotCachedException {

	}

	@Override
	public void sectionUnloaded(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyNotCachedException {

	}

	@Override
	public List<SectionGroupRole> getSectionGroups(Integer sectionID) {

		Set<SectionGroupRole> sectionGroupRoles = sectionGroupsMap.get(sectionID);

		if (sectionGroupRoles != null) {
			return new ArrayList<SectionGroupRole>(sectionGroupRoles);
		}

		return null;
	}

	@Override
	public List<Integer> getGroupSections(Integer groupID) {

		Set<Integer> groupSections = groupSectionsMap.get(groupID);

		if (groupSections != null) {
			return new ArrayList<Integer>(groupSections);
		}

		return null;
	}

	protected synchronized void initScheduler() {

		scheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());
		
		scheduler.setDaemon(true);

		scheduler.schedule(this.sectionGroupMembersInterval, this);
		
		scheduler.schedule("0 0 * * *", new Runnable() {
			@Override
			public void run() {
				today = DateUtils.getCurrentSQLDate(false);
				activeSections = ConcurrentHashMap.newKeySet();
			}
		});
		
		scheduler.start();
	}

	protected synchronized void stopScheduler() {

		try {

			if (scheduler != null) {

				scheduler.stop();
				scheduler = null;
			}

		} catch (IllegalStateException e) {
			log.error("Error stopping scheduler", e);
		}
	}

	@Override
	public void run() {

		try {
			cacheSectionGroupMembers();

			triggerDeleteSectionOnDate();

		} catch (Exception e) {

			log.error("Error running synchronization of users ", e);
		}
	}

	private void triggerDeleteSectionOnDate() {

		try {
			List<SimpleSectionDescriptor> sections = systemInterface.getCoreDaoFactory().getSectionDAO().getSectionsByAttribute(CBConstants.SECTION_ATTRIBUTE_DELETEONDATE, false);

			if (!CollectionUtils.isEmpty(sections)) {
				for (SimpleSectionDescriptor section : sections) {
					Date date = DateUtils.getDate(DateUtils.DATE_FORMATTER, section.getAttributeHandler().getString(CBConstants.SECTION_ATTRIBUTE_DELETEONDATE));

					if (date.before(today)) {
						log.info("Automatically marking section " + section + " for deletion due to delete date");

						SectionInterface sectionInterface = systemInterface.getSectionInterface(section.getSectionID());

						//Stop the section if it is started
						if (sectionInterface != null) {
							try {
								sectionInterface.getParentSectionInterface().getSectionCache().unload(section);
							}

							catch (KeyNotCachedException e) {}
						}

						section.setEnabled(false);

						CBSectionAttributeHelper.setDeleteOnDate(section, null);
						CBSectionAttributeHelper.setDeleted(section, TimeUtils.getCurrentTimestamp());

						systemInterface.getCoreDaoFactory().getSectionDAO().update(section);

						this.systemInterface.getEventHandler().sendEvent(SimpleSectionDescriptor.class, new CBSectionPreDeleteEvent(section.getSectionID()), EventTarget.ALL);
					}
				}
			}
		} catch (SQLException ex) {
			log.error("Could not get sections by delete date attribute", ex);
		}
	}

	@Override
	public boolean isEnableSecureSections() {

		return enableSecureSections;
	}

	@Override
	public String getSecureSectionsWarning() {

		return secureSectionsWarning;
	}
	
	@InstanceManagerDependency
	public void setSecureSectionForegroundModule(SecureSectionFilterModule filterModule) {

		if (enableSecureSections && filterModule == null) {
			systemInterface.getInstanceHandler().removeInstance(CBInterface.class, this);
		}
		
		this.secureSectionFilterModule = filterModule;
	}

	@Override
	public SectionAccessRequest getAccessRequest(User user, Integer sectionID) throws SQLException {

		HighLevelQuery<SectionAccessRequest> query = new HighLevelQuery<SectionAccessRequest>();
		query.addParameter(accessRequestUserParamFactory.getParameter(user));
		query.addParameter(accessRequestSectionIDParamFactory.getParameter(sectionID));
		
		return daoFactory.getAccessRequestDAO().get(query);
	}
	
	@Override
	public List<SectionAccessRequest> getAccessRequests(Integer sectionID) throws SQLException {

		HighLevelQuery<SectionAccessRequest> query = new HighLevelQuery<SectionAccessRequest>();
		query.addParameter(accessRequestSectionIDParamFactory.getParameter(sectionID));
		
		return daoFactory.getAccessRequestDAO().getAll(query);
	}

	@Override
	public void addAccessRequest(User user, Integer sectionID, String comment) throws SQLException {

		SectionAccessRequest accessRequest = new SectionAccessRequest();
		accessRequest.setSectionID(sectionID);
		accessRequest.setUser(user);
		accessRequest.setComment(comment);
		
		daoFactory.getAccessRequestDAO().add(accessRequest);
		
		systemInterface.getEventHandler().sendEvent(SimpleSectionDescriptor.class, new CBSectionAccessRequestEvent(sectionID, user), EventTarget.ALL);
	}
	
	@Override
	public void deleteAccessRequest(User user, Integer sectionID) throws SQLException {
		
		SectionAccessRequest accessRequest = new SectionAccessRequest();
		accessRequest.setSectionID(sectionID);
		accessRequest.setUser(user);
		
		daoFactory.getAccessRequestDAO().delete(accessRequest);
	}

	@Override
	public boolean isEnableSectionStatistics() {

		return enableSectionStatistics;
	}
}
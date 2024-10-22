package se.dosf.communitybase.modules.firstpage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunityModuleDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.enums.ModuleType;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.calendar.beans.CalendarResume;
import se.dosf.communitybase.modules.calendar.daos.CalendarModuleDAO;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleBundleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleMenuItemDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.MenuItemType;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.enums.URLType;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.interfaces.BundleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModule;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleCacheListener;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.MenuItemDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.SystemStartupListener;
import se.unlogic.hierarchy.core.servlets.CoreServlet;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.ModuleSetting;
import se.unlogic.standardutils.collections.KeyAlreadyCachedException;
import se.unlogic.standardutils.collections.KeyNotCachedException;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class FirstpageModule extends AnnotatedForegroundModule implements ForegroundModuleCacheListener, SystemStartupListener {

	private static final ModuleDescriptorComparator MODULE_DESCRIPTOR_COMPARATOR = new ModuleDescriptorComparator();
	private static final MenuItemDescriptorComparator MENU_ITEM_DESCRIPTOR_COMPARATOR = new MenuItemDescriptorComparator();

	private static final ArrayList<SettingDescriptor> SETTINGDESCRIPTORS = new ArrayList<SettingDescriptor>(); 
	
	public static final String VERSION_PREFIX = "CommunityBase 2.0.6";

	public static final String VERSION;

	static {
		
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("calendarItemLimit", "Calendar item limit", "The maximum number of calendar items to be showed on the firstpage", true, "5", new StringIntegerValidator(1,null)));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("combinedCalendarAlias", "Alias of CombinedCalendarModule", "The alias of CombinedCalendarModule", true, "combinedcalendar", null));
		
		String tempVersion;

		try {
			tempVersion = VERSION_PREFIX + " (rev. " + StringUtils.readStreamAsString(CoreServlet.class.getResourceAsStream("/META-INF/communitybase_svnrevision.txt")) + ")";

		} catch (Exception e) {

			tempVersion = VERSION_PREFIX + " (rev. unknown)";
		}

		VERSION = tempVersion;
	}

	protected Logger log = Logger.getLogger(this.getClass());
	private CommunityModuleDAO communityModuleDAO;
	private CommunityGroupDAO communityGroupDAO;
	private CommunityUserDAO communityUserDAO;
	private CalendarModuleDAO calendarDAO;

	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final ReadLock readLock = readWriteLock.readLock();
	private final WriteLock writeLock = readWriteLock.writeLock();

	private final TreeMap<ForegroundModuleDescriptor, CommunityModule> publicModuleMap = new TreeMap<ForegroundModuleDescriptor, CommunityModule>(MODULE_DESCRIPTOR_COMPARATOR);
	private final TreeMap<ForegroundModuleDescriptor, CommunityModule> adminModuleMap = new TreeMap<ForegroundModuleDescriptor, CommunityModule>(MODULE_DESCRIPTOR_COMPARATOR);

	private final ConcurrentHashMap<CommunityGroup, BundleDescriptor> menuMap = new ConcurrentHashMap<CommunityGroup, BundleDescriptor>();

	@ModuleSetting
	protected Integer calendarItemLimit = 5;
	
	@ModuleSetting
	protected String combinedCalendarAlias = "combinedcalendar";

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		this.createDAOs();

		this.scanModules(sectionInterface);

		if(systemInterface.getSystemStatus() == SystemStatus.Started){
			
			this.generateMenu();
			
		}else{
			
			this.systemInterface.addStartupListener(this);
		}
		
		sectionInterface.getForegroundModuleCache().addCacheListener(this);
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		if (dataSource != this.dataSource) {
			this.createDAOs();
		}

		super.update(moduleDescriptor, dataSource);
	}

	@Override
	public List<? extends SettingDescriptor> getSettings() {
		ArrayList<SettingDescriptor> settings = new ArrayList<SettingDescriptor>();

		settings.addAll(SETTINGDESCRIPTORS);

		List<? extends SettingDescriptor> superSettings = super.getSettings();

		if (superSettings != null) {
			settings.addAll(superSettings);
		}

		return settings;
	}

	private void createDAOs() {
		this.communityModuleDAO = new CommunityModuleDAO(dataSource);
		this.communityGroupDAO = new CommunityGroupDAO(dataSource);
		this.communityUserDAO = new CommunityUserDAO(dataSource);
		this.communityGroupDAO.setUserDao(this.communityUserDAO);
		this.communityUserDAO.setGroupDao(this.communityGroupDAO);
		this.calendarDAO = new CalendarModuleDAO(dataSource, this.sectionInterface.getSystemInterface().getUserHandler());
	}

	private void scanModules(SectionInterface sectionInterface) throws Exception {

		for (Entry<ForegroundModuleDescriptor, ForegroundModule> entry : this.sectionInterface.getForegroundModuleCache().getCachedModuleSet()) {

			if (entry.getValue() instanceof CommunityModule) {

				log.info("Detected community module " + entry.getKey());

				try {
					writeLock.lock();
					
					ModuleType moduleType = ((CommunityModule) entry.getValue()).getModuleType();
					
					if (moduleType != ModuleType.Administration && moduleType != ModuleType.Hidden) {

						this.publicModuleMap.put(entry.getKey(), (CommunityModule) entry.getValue());

					} else {

						this.adminModuleMap.put(entry.getKey(), (CommunityModule) entry.getValue());
					}

				} finally {
					writeLock.unlock();
				}
			}
		}
	}

	private void generateMenu() {

		try {
			ArrayList<CommunityGroup> groups = communityGroupDAO.getGroups(false,false,false);

			if (groups != null) {

				long startTime = System.currentTimeMillis();

				log.info("Generating menus for all (" + groups.size() + ") groups ...");

				for (CommunityGroup group : groups) {

					this.generateGroupMenu(group);
				}

				log.info("Generated menus for all (" + groups.size() + ") groups in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime) + " ms");
			}

		} catch (SQLException e) {

			log.error("Unable to generate menu!", e);
		}
	}

	private void generateGroupMenu(CommunityGroup group) {

		ArrayList<MenuItemDescriptor> menuItemDescriptors = new ArrayList<MenuItemDescriptor>();

		try {
			readLock.lock();

			getModuleMenuItems(group, this.publicModuleMap, menuItemDescriptors, ModuleType.Public);
			getModuleMenuItems(group, this.adminModuleMap, menuItemDescriptors, ModuleType.Administration);

		} finally {
			readLock.unlock();
		}

		if (menuItemDescriptors.isEmpty()) {

			this.menuMap.remove(group);

		} else {

			Collections.sort(menuItemDescriptors, MENU_ITEM_DESCRIPTOR_COMPARATOR);

			SimpleBundleDescriptor bundle = new SimpleBundleDescriptor();
			bundle.setName(group.getName());
			bundle.setUniqueID("communitybasebundle" + group.getGroupID());

			bundle.setDescription(group.getSchool().getName());
			bundle.setItemType(MenuItemType.TITLE);
			bundle.setAdminAccess(false);
			bundle.setAllowedGroupIDs(Arrays.asList(group.getGroupID()));
			bundle.setUserAccess(false);
			bundle.setMenuItemDescriptors(menuItemDescriptors);

			this.menuMap.put(group, bundle);
		}
	}

	private void getModuleMenuItems(CommunityGroup group, Map<ForegroundModuleDescriptor, CommunityModule> moduleMap, ArrayList<MenuItemDescriptor> menuItemDescriptors, ModuleType moduleType) {

		for (Entry<ForegroundModuleDescriptor, CommunityModule> moduleEntry : moduleMap.entrySet()) {

			try {

				if(moduleEntry.getValue().getModuleType() != ModuleType.Hidden){
				
					String fullGroupAlias = moduleEntry.getValue().getFullAlias(group);
					
					if (fullGroupAlias != null) {
	
						SimpleMenuItemDescriptor menuItemDescriptor = new SimpleMenuItemDescriptor();
	
						menuItemDescriptor.setAdminAccess(false);
						menuItemDescriptor.setUserAccess(false);
						menuItemDescriptor.setAnonymousAccess(false);
	
						menuItemDescriptor.setDescription(moduleEntry.getKey().getDescription());
						menuItemDescriptor.setItemType(MenuItemType.MENUITEM);
						menuItemDescriptor.setName(moduleEntry.getKey().getName());
	
						menuItemDescriptor.setUrl(fullGroupAlias);
						menuItemDescriptor.setUrlType(URLType.RELATIVE_FROM_CONTEXTPATH);
	
						menuItemDescriptor.setUniqueID("communitybasebundle" + group.getGroupID());
	
						// check if this module is enabled for the given group
						if (moduleType == ModuleType.Public && communityModuleDAO.isModuleEnabled(moduleEntry.getKey().getModuleID(), group)) {
	
							// if module is enabled in the given group allow the given group to access the menuitem
							menuItemDescriptor.setAllowedGroupIDs(Arrays.asList(group.getGroupID()));
	
						} else {
	
							menuItemDescriptor.setAllowedUserIDs(this.communityUserDAO.getUserIdsByAccessLevel(group, GroupAccessLevel.ADMIN));
							menuItemDescriptor.setItemType(MenuItemType.TITLE);
						}
	
						menuItemDescriptors.add(menuItemDescriptor);
					}
				
				}

			} catch (Throwable e) {
				log.error("Unable to get menuitem from community module " + moduleEntry.getKey(),e);
			}
		}

	}

	@Override
	public List<BundleDescriptor> getVisibleBundles() {
		return new ArrayList<BundleDescriptor>(this.menuMap.values());
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (user == null) {
			throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "User needs to be logged in!");
		}

		Document doc = this.createDocument(req, uriParser);

		if (!(user instanceof CommunityUser)) {

			log.warn("The user " + user + " is of the wrong type");

			doc.getDocumentElement().appendChild(doc.createElement("userWrongType"));

			return new SimpleForegroundModuleResponse(doc);
		}

		CommunityUser communityUser = (CommunityUser) user;

		if ((!communityUser.isAdmin() && communityUser.getSchools() == null) && (communityUser.getGroups() == null || communityUser.getGroups().isEmpty())) {

			log.warn("The user " + user + " has no groups and no admin access");

			doc.getDocumentElement().appendChild(doc.createElement("emptyUserGroup"));

			return new SimpleForegroundModuleResponse(doc);
		}

		log.info("User " + user + " requested firstpage");

		Element groupListElement = doc.createElement("groups");
		doc.getFirstChild().appendChild(groupListElement);

		if (user.getGroups() != null && user.getLastLogin() != null) {

			for (CommunityGroup group : communityUser.getCommunityGroups()) {

				Element groupElement = group.toXML(doc);
				groupListElement.appendChild(groupElement);

				// Check which community modules are active in this group
				ArrayList<Integer> groupModuleIDs = this.communityModuleDAO.getEnabledModules(group);

				Element groupModuleElement = doc.createElement("groupModules");
				groupElement.appendChild(groupModuleElement);

				if (groupModuleIDs != null) {

					for (Integer moduleID : groupModuleIDs) {

						CommunityModule moduleInstance = null;

						try {
							readLock.lock();

							for (Entry<ForegroundModuleDescriptor, CommunityModule> moduleEntry : this.publicModuleMap.entrySet()) {

								if (moduleEntry.getKey().getModuleID().equals(moduleID)) {

									moduleInstance = moduleEntry.getValue();
									break;
								}
							}

						} finally {
							readLock.unlock();
						}

						if(moduleInstance != null){
							try {
								List<? extends Event> groupEvents = moduleInstance.getGroupResume(group, (CommunityUser) user, user.getLastLogin());

								if (groupEvents != null && !groupEvents.isEmpty()) {

									Element moduleElement = moduleInstance.getModuleDescriptor().toXML(doc);
									groupModuleElement.appendChild(moduleElement);

									Element eventElement = doc.createElement("events");
									moduleElement.appendChild(eventElement);

									for (Event event : groupEvents) {
										eventElement.appendChild(event.toXML(doc));
									}
								}

							} catch (Throwable e) {
								log.error("Error getting group resume from module " + moduleInstance.getModuleDescriptor() + " for group " + group + " and user " + communityUser,e);
							}
						}
					}
				}
			}
		}

		CalendarResume calendarResume = this.calendarDAO.getUserCalendarResume((CommunityUser) user, this.calendarItemLimit);
		
		if (calendarResume != null) {
			
			calendarResume.setCalendarAlias(this.combinedCalendarAlias);
			
			groupListElement.appendChild(calendarResume.toXML(doc));
		
		}

		return new SimpleForegroundModuleResponse(doc);
	}

	@Override
	public void unload() {
		this.sectionInterface.getForegroundModuleCache().removeCacheListener(this);
	}

	public void moduleCached(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule moduleInstance) throws KeyAlreadyCachedException {

		if (moduleInstance instanceof CommunityModule) {

			log.info("Detected caching of community module " + moduleDescriptor);

			try {
				writeLock.lock();

				if (((CommunityModule) moduleInstance).getModuleType() == ModuleType.Public) {

					this.publicModuleMap.put(moduleDescriptor, (CommunityModule) moduleInstance);

				} else {

					this.adminModuleMap.put(moduleDescriptor, (CommunityModule) moduleInstance);
				}
			} finally {
				writeLock.unlock();
			}

			if(systemInterface.getSystemStatus() == SystemStatus.Started){
			
				this.generateMenu();
				this.updateSectionMenuCache();				
			}
		}
	}

	public void moduleUpdated(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule moduleInstance) throws KeyNotCachedException {

		if (moduleInstance instanceof CommunityModule) {

			log.info("Detected update of community module " + moduleDescriptor);

			try {
				writeLock.lock();

				if (((CommunityModule) moduleInstance).getModuleType() == ModuleType.Public) {

					this.publicModuleMap.remove(moduleDescriptor);
					this.publicModuleMap.put(moduleDescriptor, (CommunityModule) moduleInstance);

				} else {

					this.adminModuleMap.remove(moduleDescriptor);
					this.adminModuleMap.put(moduleDescriptor, (CommunityModule) moduleInstance);
				}
			} finally {
				writeLock.unlock();
			}

			this.generateMenu();
			this.updateSectionMenuCache();
		}
	}

	public void moduleUnloaded(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule moduleInstance) throws KeyNotCachedException {

		if (this.sectionInterface.getSystemInterface().getSystemStatus() != SystemStatus.Stopping && moduleInstance instanceof CommunityModule) {

			log.info("Detected unloading of community module " + moduleDescriptor);

			try {
				writeLock.lock();

				if (((CommunityModule) moduleInstance).getModuleType() == ModuleType.Public) {

					this.publicModuleMap.remove(moduleDescriptor);

				} else {

					this.adminModuleMap.remove(moduleDescriptor);
				}
			} finally {
				writeLock.unlock();
			}

			this.generateMenu();
			this.updateSectionMenuCache();
		}
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		doc.appendChild(document);
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));

		return doc;
	}

	/**
	 * @return the moduleMap
	 */
	public Map<ForegroundModuleDescriptor, CommunityModule> getPublicModuleMap() {
		return Collections.unmodifiableMap(publicModuleMap);
	}

	public void groupAdminAdded(CommunityGroup group, CommunityUser user) {

		this.generateGroupMenu(group);
		this.updateSectionMenuCache();
	}

	public void groupAdminRemoved(CommunityGroup group, CommunityUser user) {

		this.generateGroupMenu(group);
		this.updateSectionMenuCache();
	}

	public void groupModuleEnabled(CommunityGroup group, ForegroundModuleDescriptor moduleDescriptor) {

		this.generateGroupMenu(group);
		this.updateSectionMenuCache();
	}

	public void groupModuleDisabled(CommunityGroup group, ForegroundModuleDescriptor moduleDescriptor) {

		this.generateGroupMenu(group);
		this.updateSectionMenuCache();
	}

	public void schoolUpdated(School school) {

		if (school.getGroups() != null) {

			for (CommunityGroup group : school.getGroups()) {

				group.setSchool(school);

				this.generateGroupMenu(group);
			}

			this.updateSectionMenuCache();
		}
	}

	public void schoolRemoved(School school) {

		if (school.getGroups() != null) {

			for (CommunityGroup group : school.getGroups()) {

				this.menuMap.remove(group);
			}

			this.updateSectionMenuCache();
		}
	}

	public void groupAdded(CommunityGroup group) {

		this.generateGroupMenu(group);
		this.updateSectionMenuCache();
	}

	public void groupUpdated(CommunityGroup group) {

		this.generateGroupMenu(group);
		this.updateSectionMenuCache();
	}

	public void groupRemoved(CommunityGroup group) {

		this.menuMap.remove(group);
		this.updateSectionMenuCache();
	}

	private void updateSectionMenuCache() {
		this.sectionInterface.getMenuCache().moduleUpdated(moduleDescriptor, this);
	}

	public void systemStarted() {
		
		this.generateMenu();
		this.updateSectionMenuCache();	
		
		log.info(VERSION + " started");
	}
}

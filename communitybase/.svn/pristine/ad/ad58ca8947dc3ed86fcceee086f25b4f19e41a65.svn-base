package se.dosf.communitybase.modules.dbuserprovider;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.interfaces.BundleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.MenuItemDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.UserProvider;
import se.unlogic.hierarchy.modules.SimpleForegroundModule;
import se.unlogic.webutils.http.URIParser;

public class DBUserProviderModule extends SimpleForegroundModule implements UserProvider {

	protected Logger log = Logger.getLogger(this.getClass());
	private CommunityUserDAO userDAO;
	private CommunityGroupDAO groupDAO;
	private CommunitySchoolDAO schoolDAO;
	private boolean userProviderAdded = false;
	private ForegroundModuleDescriptor moduleDescriptor;
	private UserHandler userHandler;
	private DataSource dataSource;
	private SectionInterface sectionInterface;

	public ForegroundModuleResponse processRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException {
		log.warn("processRequest invoked on DBUserProviderModule (" + this.moduleDescriptor + "), access to this module should be restricted so no users can access it!");
		throw new URINotFoundException(sectionInterface.getSectionDescriptor(), moduleDescriptor, uriParser);
	}

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		this.sectionInterface = sectionInterface;
		this.userHandler = sectionInterface.getSystemInterface().getUserHandler();
		this.moduleDescriptor = moduleDescriptorBean;
		this.dataSource = dataSource;
		this.addUserProvider();
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		// Check if the this user provider was previously added to the user handler and if the datasource has changed, in that case remove user provider
		if (this.userProviderAdded && !this.dataSource.equals(dataSource)) {

			this.removeUserProvider();

			this.dataSource = dataSource;

			this.addUserProvider();

		} else if (!this.userProviderAdded) {

			this.dataSource = dataSource;

			this.addUserProvider();
		}
	}

	@Override
	public void unload() {

		if (this.userProviderAdded) {
			this.removeUserProvider();
		}
	}

	private void addUserProvider() throws InstantiationException, IllegalAccessException {

		// Create userdao
		this.userDAO = new CommunityUserDAO(dataSource);
		this.groupDAO = new CommunityGroupDAO(dataSource);
		this.schoolDAO = new CommunitySchoolDAO(dataSource);

		this.userDAO.setGroupDao(groupDAO);
		this.groupDAO.setUserDao(userDAO);
		this.userDAO.setSchoolDAO(schoolDAO);

		// Add this module as a user provider
		this.userProviderAdded = userHandler.addProvider(this);

		// Check if a provider for this datasource already had been added, in that case remove dao
		if (!this.userProviderAdded) {
			log.warn("User provider for datasource " + dataSource + " already added in user handler by different module than " + this.moduleDescriptor);
			this.userDAO = null;
		} else {
			log.info("User provider for datasource " + this.dataSource + " added by module " + this.moduleDescriptor);
		}
	}

	private void removeUserProvider() {
		if (this.userHandler.removeProvider(this)) {
			log.info("User provider for datasource " + this.dataSource + " removed by module " + this.moduleDescriptor);
		} else {
			log.warn("User provider for datasource " + this.dataSource + " already removed by diffrent module than " + this.moduleDescriptor);
		}

		this.userProviderAdded = false;
	}

	public byte getPriority() {
		return 0;
	}

	public CommunityUser getUser(Integer userID, boolean groups) {
		try {
			return this.userDAO.findByUserID(userID, true, groups);
		} catch (SQLException e) {
			log.error("Error getting user with ID " + userID, e);
			return null;
		}
	}

	public CommunityUser getUser(String username, String password, boolean groups) {
		try {
			return this.userDAO.findByEmailPassword(username, password, true ,groups);
		} catch (SQLException e) {
			log.error("Error getting user with username " + username, e);
			return null;
		}
	}

	public ArrayList<CommunityUser> getUsers(boolean groups) {
		try {
			return this.userDAO.getUsers(true,groups);
		} catch (SQLException e) {
			log.error("Error getting users", e);
			return null;
		}
	}

	public CommunityGroup getGroup(Integer groupID, boolean users) {
		try {
			return this.groupDAO.getGroup(groupID, users);
		} catch (SQLException e) {
			log.error("Error getting group with groupID " + groupID + " (get users: " + users + ")", e);
			return null;
		}
	}

	public ArrayList<CommunityGroup> getGroups(boolean users) {
		try {
			return this.groupDAO.getGroups(users,true,true);
		} catch (SQLException e) {
			log.error("Error getting groups (get users: " + users + ")", e);
			return null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataSource == null) ? 0 : dataSource.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DBUserProviderModule other = (DBUserProviderModule) obj;
		if (dataSource == null) {
			if (other.dataSource != null) {
				return false;
			}
		} else if (!dataSource.equals(other.dataSource)) {
			return false;
		}
		return true;
	}

	@Override
	public List<? extends SettingDescriptor> getSettings() {
		return null;
	}

	@Override
	public List<BundleDescriptor> getVisibleBundles() {
		return null;
	}

	@Override
	public List<MenuItemDescriptor> getAllMenuItems() {
		return null;
	}

	@Override
	public List<MenuItemDescriptor> getVisibleMenuItems() {
		return null;
	}

	@Override
	public List<BundleDescriptor> getAllBundles() {
		return null;
	}
}

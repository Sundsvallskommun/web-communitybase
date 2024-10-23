package se.dosf.communitybase.modules.dbuserprovider;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.UserField;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.UserProvider;
import se.unlogic.hierarchy.foregroundmodules.SimpleForegroundModule;
import se.unlogic.standardutils.dao.enums.Order;
import se.unlogic.standardutils.string.StringUtils;
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

	public int getPriority() {
		return 0;
	}

	public List<CommunityUser> getUsers(boolean groups) throws SQLException {

		return this.userDAO.getUsers(true,groups);
	}

	public CommunityUser getUser(Integer userID, boolean groups) throws SQLException {

		return this.userDAO.findByUserID(userID, true, groups);
	}

	public List<CommunityUser> getUsers(List<Integer> userIDs, boolean groups) throws SQLException {

		return this.userDAO.getUsers(userIDs, groups);
	}

	public List<? extends User> getUsersByGroup(Integer groupID) throws SQLException {

		return this.userDAO.getUsers(groupID);
	}

	public User getUserByUsername(String username, boolean groups) throws SQLException {

		return this.userDAO.getUser(username, true, groups);
	}

	public User getUserByUsernamePassword(String username, String password, boolean groups) throws SQLException {

		return this.userDAO.findByEmailPassword(username, password, true, groups);
	}

	public User getUserByEmail(String email, boolean groups) throws SQLException {

		return this.getUserByUsername(email, true);
	}

	public User getUserByEmailPassword(String email, String password, boolean groups) throws SQLException {

		return this.getUserByUsernamePassword(email, password, groups);
	}

	public int getUserCount() throws SQLException {

		return this.userDAO.getUserCount();
	}

	public int getDisabledUserCount() throws SQLException {

		return 0;
	}

	public List<Character> getUserFirstLetterIndex(UserField filteringField) throws SQLException {

		return this.userDAO.getUserFirstLetterIndex(filteringField);
	}

	public List<? extends User> getUsers(UserField filteringField, char startsWith, Order order, boolean groups) throws SQLException {

		return this.userDAO.getUsers(filteringField, order, startsWith, groups);
	}

	public Group getGroup(Integer groupID) throws SQLException {

		return this.groupDAO.getGroup(groupID, false);
	}

	public List<? extends Group> getGroups() throws SQLException {

		return this.groupDAO.getGroups(false, false, false);
	}

	public List<? extends Group> getGroups(List<Integer> groupIDs) throws SQLException {

		return this.groupDAO.getGroups(groupIDs, false);
	}

	public int getGroupCount() throws SQLException {

		return this.groupDAO.getGroupCount();
	}

	public int getDisabledGroupCount() throws SQLException {

		return 0;
	}

	public List<? extends Group> getGroups(Order order, char startsWith) throws SQLException {

		return this.groupDAO.getGroups(order, startsWith);
	}

	public List<Character> getGroupFirstLetterIndex() throws SQLException {

		return this.groupDAO.getGroupFirstLetterIndex();
	}

	public boolean isProviderFor(User user) {

		return false;
	}

	public boolean isProviderFor(Group group) {

		return false;
	}

	public DataSource getDataSource() {

		return this.dataSource;
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
	public String toString() {

		return StringUtils.substring(this.moduleDescriptor.getName(), 30, "...") + " (ID: " + this.moduleDescriptor.getModuleID() + ")";
	}

	public List<? extends User> getUsersByGroups(List<Integer> groupIDs) throws SQLException {

		// TODO Auto-generated method stub
		return null;
	}

}

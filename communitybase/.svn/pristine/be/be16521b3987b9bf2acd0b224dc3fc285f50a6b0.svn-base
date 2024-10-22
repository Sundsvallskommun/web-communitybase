package se.dosf.communitybase.modules.dbuserprovider;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.RelationGroups;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.beans.UserSearchQuery;
import se.unlogic.hierarchy.core.enums.UserField;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.handlers.GroupHandler;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.providers.GroupProvider;
import se.unlogic.hierarchy.core.interfaces.providers.UserProvider;
import se.unlogic.hierarchy.foregroundmodules.SimpleForegroundModule;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.webutils.http.URIParser;

public class DBUserProviderModule extends SimpleForegroundModule implements UserProvider, GroupProvider{

	protected Logger log = Logger.getLogger(this.getClass());

	private CommunityUserDAO userDAO;
	private CommunityGroupDAO groupDAO;
	private CommunitySchoolDAO schoolDAO;
	private boolean providerAdded = false;
	private UserHandler userHandler;
	private GroupHandler groupHandler;

	@Override
	public ForegroundModuleResponse processRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException {
		log.warn("processRequest invoked on DBUserProviderModule (" + this.moduleDescriptor + "), access to this module should be restricted so no users can access it!");
		throw new URINotFoundException(uriParser);
	}

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		this.userHandler = sectionInterface.getSystemInterface().getUserHandler();
		this.groupHandler = sectionInterface.getSystemInterface().getGroupHandler();
		this.addProviders();
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		// Check if the this user provider was previously added to the user handler and if the datasource has changed, in that case remove user provider
		if (this.providerAdded && !this.dataSource.equals(dataSource)) {

			this.removeProviders();

			this.addProviders();

		} else if (!this.providerAdded) {

			this.addProviders();
		}

		super.update(moduleDescriptor, dataSource);
	}

	@Override
	public void unload() {

		if (this.providerAdded) {
			this.removeProviders();
		}
	}

	private void addProviders() throws InstantiationException, IllegalAccessException {

		// Create userdao
		this.userDAO = new CommunityUserDAO(dataSource);
		this.groupDAO = new CommunityGroupDAO(dataSource);
		this.schoolDAO = new CommunitySchoolDAO(dataSource);

		this.userDAO.setGroupDao(groupDAO);
		this.groupDAO.setUserDao(userDAO);
		this.userDAO.setSchoolDAO(schoolDAO);

		providerAdded = true;

		// Add this module as a user provider
		userHandler.addProvider(this);
		groupHandler.addProvider(this);

		log.info("User and group providers for datasource " + this.dataSource + " added by module " + this.moduleDescriptor);
	}

	private void removeProviders() {

		this.userHandler.removeProvider(this);
		this.groupHandler.removeProvider(this);

		log.info("User provider for datasource " + this.dataSource + " removed by module " + this.moduleDescriptor);

		this.providerAdded = false;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public List<CommunityUser> getUsers(boolean groups, boolean attributes) throws SQLException {

		return this.userDAO.getUsers(true,groups);
	}

	@Override
	public CommunityUser getUser(Integer userID, boolean groups, boolean attributes) throws SQLException {

		return this.userDAO.findByUserID(userID, true, groups);
	}

	@Override
	public List<CommunityUser> getUsers(Collection<Integer> userIDs, boolean groups, boolean attributes) throws SQLException {

		return this.userDAO.getUsers(userIDs, groups);
	}

	@Override
	public List<? extends User> getUsersByGroup(Integer groupID, boolean groups, boolean attributes) throws SQLException {

		return this.userDAO.getUsers(groupID);
	}

	@Override
	public User getUserByUsername(String username, boolean groups, boolean attributes) throws SQLException {

		return this.userDAO.getUser(username, true, groups);
	}

	@Override
	public User getUserByUsernamePassword(String username, String password, boolean groups, boolean attributes) throws SQLException {

		return this.userDAO.findByEmailPassword(username, password, true, groups);
	}

	@Override
	public User getUserByEmail(String email, boolean groups, boolean attributes) throws SQLException {

		return this.getUserByUsername(email, true, attributes);
	}

	@Override
	public User getUserByEmailPassword(String email, String password, boolean groups, boolean attributes) throws SQLException {

		return this.getUserByUsernamePassword(email, password, groups, attributes);
	}

	@Override
	public int getUserCount() throws SQLException {

		return this.userDAO.getUserCount();
	}

	@Override
	public int getDisabledUserCount() throws SQLException {

		return 0;
	}

	@Override
	public List<Character> getUserFirstLetterIndex(UserField filteringField) throws SQLException {

		return this.userDAO.getUserFirstLetterIndex(filteringField);
	}

	@Override
	public List<? extends User> getUsers(UserField filteringField, char startsWith, Order order, boolean groups, boolean attributes) throws SQLException {

		return this.userDAO.getUsers(filteringField, order, startsWith, groups);
	}

	@Override
	public Group getGroup(Integer groupID, boolean attributes) throws SQLException {

		return this.groupDAO.getGroup(groupID, false);
	}

	@Override
	public List<? extends Group> getGroups(boolean attributes) throws SQLException {

		return RelationGroups.appendRelationGroups(this.groupDAO.getGroups(false, false, false));
	}

	@Override
	public List<? extends Group> getGroups(Collection<Integer> groupIDs, boolean attributes) throws SQLException {

		List<CommunityGroup> groups = this.groupDAO.getGroups(groupIDs, false);

		for(CommunityGroup group : RelationGroups.getRelationGroups()){
			if(groupIDs.contains(group.getGroupID())) {
				if(groups == null){
					groups = new ArrayList<CommunityGroup>();
				}
				groups.add(group);
			}
		}

		return groups;
	}

	@Override
	public int getGroupCount() throws SQLException {

		return this.groupDAO.getGroupCount();
	}

	@Override
	public int getDisabledGroupCount() throws SQLException {

		return 0;
	}

	@Override
	public List<? extends Group> getGroups(Order order, char startsWith, boolean attributes) throws SQLException {

		return RelationGroups.appendRelationGroups(this.groupDAO.getGroups(order, startsWith));
	}

	@Override
	public List<Character> getGroupFirstLetterIndex() throws SQLException {

		return this.groupDAO.getGroupFirstLetterIndex();
	}

	@Override
	public boolean isProviderFor(User user) {

		return false;
	}

	@Override
	public boolean isProviderFor(Group group) {

		return false;
	}

	@Override
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

	@Override
	public List<? extends User> getUsersByGroups(Collection<Integer> groupIDs, boolean attributes) throws SQLException {

		return null;
	}

	@Override
	public List<? extends User> getUsers(UserField sortingField, Order order, boolean groups, boolean attributes) throws SQLException {

		return null;
	}

	@Override
	public List<? extends User> getUsersByAttribute(String attributeName, String attributeValue, boolean groups, boolean attributes) throws SQLException {

		return null;
	}

	@Override
	public User getUserByAttribute(String attributeName, String attributeValue, boolean groups, boolean attributes) throws SQLException {

		return null;
	}

	@Override
	public List<? extends Group> searchGroups(String query, boolean attributes, Integer maxHits) throws SQLException {

		List<CommunityGroup> groups = groupDAO.searchGroups(query, attributes);

		for(CommunityGroup group : RelationGroups.getRelationGroups()){
			if(group.getName().toLowerCase().contains(query.toLowerCase())) {
				if(groups == null){
					groups = new ArrayList<CommunityGroup>();
				}
				groups.add(group);
			}
		}

		return groups;
	}

	@Override
	public List<? extends User> searchUsers(String query, boolean groups, boolean attributes, Integer maxHits, List<Integer> groupIDs, boolean onlyEnabledUsers, boolean requireUsername) throws SQLException {
		return userDAO.searchUsers(query, groups, attributes, maxHits, groupIDs, onlyEnabledUsers);
	}

	@Override
	public List<? extends Group> getGroupsByAttribute(String attributeName, String attributeValue, boolean attributes) throws SQLException {

		return null;
	}

	@Override
	public Group getGroupByAttribute(String attributeName, String attributeValue, boolean attributes) throws SQLException {

		return null;
	}

	@Override
	public Group getGroupByAttributes(List<Entry<String, String>> attributeEntries, boolean attributes) throws SQLException {

		return null;
	}

	@Override
	public List<? extends User> getUsersByAttribute(String attributeName, boolean groups, boolean attributes) {

		return null;
	}

	@Override
	public List<? extends User> getUsersWithoutAttribute(String attributeName, boolean groups, boolean attributes) throws SQLException {

		return null;
	}

	@Override
	public List<? extends Group> getGroupsByAttribute(String attributeName, boolean attributes) {

		return null;
	}

	@Override
	public List<? extends Group> searchGroupsWithAttribute(String query, boolean attributes, String attributeName, Integer maxHits) throws SQLException {

		return null;
	}

	@Override
	public int getUserCountByGroup(Integer groupID) throws SQLException {

		return 0;
	}

	@Override
	public List<? extends User> getUsersByAttributes(String attributeName, Collection<String> attributeValues, boolean groups, boolean attributes) throws SQLException {

		return null;
	}

	@Override
	public List<? extends User> getUsersByUsername(Collection<String> usernames, boolean groups, boolean attributes) throws SQLException {

		return null;
	}

	@Override
	public List<? extends User> getUsersByEmail(Collection<String> emails, boolean groups, boolean attributes) throws SQLException {

		return null;
	}

	@Override
	public List<? extends User> getUsersByAttributeWildcard(String attributeName, String attributeValue, boolean groups, boolean attributes) throws SQLException {

		return null;
	}

	@Override
	public List<? extends User> searchUsers(UserSearchQuery searchQuery) throws SQLException {

		return null;
	}

	@Override
	public List<? extends Group> getGroupsByName(Collection<String> groupNames, boolean attributes) throws SQLException {

		return null;
	}

}

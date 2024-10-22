package se.dosf.communitybase.daos;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.GroupRelation;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.populators.CommunityUserPopulator;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.IntegerKeyCollector;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;
import se.unlogic.standardutils.db.DBUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;

public class CommunityUserDAO extends BaseDAO {

	private static CommunityUserPopulator POPULATOR = new CommunityUserPopulator();
	private CommunityGroupDAO groupDAO;
	private CommunitySchoolDAO schoolDAO;

	public CommunityUserDAO(DataSource ds) {
		super(ds);
	}

	public void setGroupDao(CommunityGroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	public void setSchoolDAO(CommunitySchoolDAO schoolDAO) {
		this.schoolDAO = schoolDAO;
	}

	//TODO share connection?
	public ArrayList<CommunityUser> getAll(boolean getSchools, boolean getGroups) throws SQLException {

		ArrayListQuery<CommunityUser> query = new ArrayListQuery<CommunityUser>(this.dataSource, true, "SELECT * FROM users", POPULATOR);

		ArrayList<CommunityUser> users = query.executeQuery();

		if (users != null && (getSchools || getGroups)) {

			for(CommunityUser user : users){

				if(getGroups){
					user.setGroups(this.groupDAO.getGroupRelations(user));
				}

				if(getSchools){
					user.setSchools(this.schoolDAO.getSchools(user,false,false));
				}
			}
		}

		return users;
	}

	public void delete(CommunityUser user) throws SQLException {

		UpdateQuery query = new UpdateQuery(this.dataSource, true, "DELETE FROM users WHERE userID = ?");

		query.setInt(1, user.getUserID());

		query.executeUpdate();
	}

	public void update(CommunityUser user, boolean encryptPassword, boolean updateGroups, boolean updateSchools) throws SQLException {

		TransactionHandler transactionHandler = null;

		try {
			transactionHandler = new TransactionHandler(dataSource);

			UpdateQuery query = transactionHandler.getUpdateQuery("UPDATE users SET email = ?, firstName = ?, lastName = ?, phoneHome = ?, phoneMobile = ?, phoneWork = ?, lastLogin = ?, password = " + (encryptPassword ? "PASSWORD(?)" : "?") + ", resume = ?, lastResume = ?,  admin = ?,  languageCode = ?, enabled = ?, added = ? WHERE userID = ?");

			query.setString(1, user.getEmail());
			query.setString(2, user.getFirstname());
			query.setString(3, user.getLastname());
			query.setString(4, user.getPhoneHome());
			query.setString(5, user.getPhoneMobile());
			query.setString(6, user.getPhoneWork());
			query.setTimestamp(7, user.getLastLogin());
			query.setString(8, user.getPassword());
			
			if(user.getResume() != null){
				query.setInt(9, user.getResume());
			}else{
				query.setNull(9, 0);
			}
			
			query.setTimestamp(10, user.getLastResume());
			query.setBoolean(11, user.isAdmin());
			query.setString(12, user.getLanguage() == null ? null : user.getLanguage().getLanguageCode());
			query.setBoolean(13, user.isEnabled());
			query.setTimestamp(14, user.getAdded());
			query.setInt(15, user.getUserID());

			query.executeUpdate();

			if(updateGroups){
				clearGroups(user,transactionHandler);
				addGroups(user,transactionHandler);
			}

			if(updateSchools){
				clearSchools(user,transactionHandler);
				addSchools(user,transactionHandler);
			}

			transactionHandler.commit();

		} finally {
			TransactionHandler.autoClose(transactionHandler);
		}

	}

	private void addSchools(CommunityUser user,
			TransactionHandler transactionHandler) throws SQLException {

		if(user.getSchools() != null){
			for(School school : user.getSchools()){
				addSchool(user,school,transactionHandler);
			}
		}
	}

	private void addSchool(CommunityUser user, School school, TransactionHandler transactionHandler) throws SQLException {

		UpdateQuery query = transactionHandler.getUpdateQuery("INSERT INTO schooladmins VALUES (?,?)");

		query.setInt(1,school.getSchoolID());
		query.setInt(2,user.getUserID());

		query.executeUpdate();
	}

	private void clearSchools(CommunityUser user, TransactionHandler transactionHandler) throws SQLException {

		UpdateQuery query = transactionHandler.getUpdateQuery("DELETE FROM schooladmins WHERE userID = ?");

		query.setInt(1, user.getUserID());

		query.executeUpdate();
	}

	private void addGroups(CommunityUser user, TransactionHandler transactionHandler) throws SQLException {

		if(user.getGroupMap() != null){
			for(Entry<CommunityGroup, GroupRelation> groupEntry : user.getGroupMap().entrySet()){
				addGroup(user,groupEntry, transactionHandler);
			}
		}
	}

	private void addGroup(CommunityUser user, Entry<CommunityGroup, GroupRelation> groupEntry, TransactionHandler transactionHandler) throws SQLException {

		UpdateQuery query = transactionHandler.getUpdateQuery("INSERT INTO groupusers VALUES (?,?,?,?)");

		query.setInt(1,groupEntry.getKey().getGroupID());
		query.setInt(2,user.getUserID());
		query.setString(3, groupEntry.getValue().getAccessLevel().toString());
		query.setString(4, groupEntry.getValue().getComment());

		query.executeUpdate();
	}

	private void clearGroups(CommunityUser user, TransactionHandler transactionHandler) throws SQLException {

		UpdateQuery query = transactionHandler.getUpdateQuery("DELETE FROM groupusers WHERE userID = ?");

		query.setInt(1, user.getUserID());

		query.executeUpdate();
	}

	public ArrayList<CommunityUser> getUsers(CommunityGroup group, boolean getSchools, boolean getGroups) throws SQLException {

		ArrayListQuery<CommunityUser> query = new ArrayListQuery<CommunityUser>(this.dataSource, true, "SELECT users.* FROM groupusers INNER JOIN users ON (groupusers.userID = users.userID) " + "WHERE (groupusers.groupID = ?) ORDER BY users.firstname, users.lastname;", POPULATOR);

		query.setInt(1, group.getGroupID());

		ArrayList<CommunityUser> users = query.executeQuery();

		if (users != null && (getSchools || getGroups)) {

			for(CommunityUser user : users){

				if(getGroups){
					user.setGroups(this.groupDAO.getGroupRelations(user));
				}

				if(getSchools){
					user.setSchools(this.schoolDAO.getSchools(user,false,false));
				}
			}
		}

		return users;

	}

	public ArrayList<CommunityUser> getUsers(boolean getSchools, boolean getGroups) throws SQLException {

		Connection connection = null;
		try {
			connection = this.dataSource.getConnection();

			ArrayList<CommunityUser> users = new ArrayListQuery<CommunityUser>(connection, true, "SELECT * FROM users ORDER BY firstname, lastname", POPULATOR).executeQuery();

			if (users != null && (getSchools || getGroups)) {

				for(CommunityUser user : users){

					if(getGroups){
						user.setGroups(this.groupDAO.getGroupRelations(user));
					}

					if(getSchools){
						user.setSchools(this.schoolDAO.getSchools(user,false,false));
					}
				}
			}

			return users;
		} finally {
			DBUtils.closeConnection(connection);
		}
	}

	public void getUsers(CommunityGroup group, Connection connection) throws SQLException {

		ArrayListQuery<CommunityUser> query = new ArrayListQuery<CommunityUser>(connection, true, "SELECT users.userID, users.`password`, users.firstname, users.lastname, users.email, users.admin, users.languageCode, users.enabled, users.added, users.lastlogin, users.phoneHome, users.phoneWork, " + "users.resume, users.lastresume, users.phoneMobile " + "FROM groupusers INNER JOIN users ON (groupusers.userID=users.userID) " + "WHERE (groupusers.groupID = ?) ORDER BY users.firstname, users.lastname;", POPULATOR);

		query.setInt(1, group.getGroupID());

		group.setUsers(query.executeQuery());

	}

	public CommunityUser findByUserID(Integer userID, boolean getSchools, boolean getGroups) throws SQLException {

		String sql = "SELECT * FROM users WHERE users.userID = ?;";

		ObjectQuery<CommunityUser> query = new ObjectQuery<CommunityUser>(this.dataSource, true, sql, POPULATOR);

		query.setInt(1, userID);

		CommunityUser user = query.executeQuery();

		if (user != null) {

			if(getGroups){
				user.setGroups(this.groupDAO.getGroupRelations(user));
			}

			if(getSchools){
				user.setSchools(this.schoolDAO.getSchools(user,false,false));
			}
		}

		return user;

	}

	public CommunityUser findByEmailPassword(String email, String password, boolean getSchools, boolean getGroups) throws SQLException {

		ObjectQuery<CommunityUser> query = new ObjectQuery<CommunityUser>(this.dataSource, true, "SELECT * FROM users WHERE email = ? and password = PASSWORD(?)", POPULATOR);

		query.setString(1, email);
		query.setString(2, password);

		CommunityUser user = query.executeQuery();

		if (user != null) {

			if(getGroups){
				user.setGroups(this.groupDAO.getGroupRelations(user));
			}

			if(getSchools){
				user.setSchools(this.schoolDAO.getSchools(user,false,false));
			}
		}

		return user;
	}

	public ArrayList<Integer> getUserIdsByAccessLevel(CommunityGroup group, GroupAccessLevel... accessLevel) throws SQLException {

		String queryString = "SELECT userID FROM groupusers WHERE groupID = ? AND accessLevel IN(" + StringUtils.toQuotedCommaSeparatedString(accessLevel) + ")";

		ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(this.dataSource, true, queryString, IntegerPopulator.getPopulator());

		query.setInt(1, group.getGroupID());

		return query.executeQuery();
	}

	public CommunityUser getUser(String email, boolean getSchools, boolean getGroups) throws SQLException {

		ObjectQuery<CommunityUser> query = new ObjectQuery<CommunityUser>(this.dataSource, true, "SELECT * FROM users WHERE email = ?", POPULATOR);

		query.setString(1, email);

		CommunityUser user = query.executeQuery();

		if (user != null) {

			if(getGroups){
				user.setGroups(this.groupDAO.getGroupRelations(user));
			}

			if(getSchools){
				user.setSchools(this.schoolDAO.getSchools(user,false,false));
			}
		}

		return user;
	}

	public CommunityUser getUser(Integer userID, boolean getSchools, boolean getGroups) throws SQLException {
		String sql = "SELECT * FROM users WHERE userID = ?;";

		ObjectQuery<CommunityUser> query = new ObjectQuery<CommunityUser>(this.dataSource.getConnection(), true, sql, POPULATOR);

		query.setInt(1, userID);

		CommunityUser user = query.executeQuery();

		if (user != null) {

			if(getGroups){
				user.setGroups(this.groupDAO.getGroupRelations(user));
			}

			if(getSchools){
				user.setSchools(this.schoolDAO.getSchools(user,false,false));
			}
		}

		return user;
	}

	public List<CommunityUser> getAdminUsers(boolean getSchools, boolean getGroups) throws SQLException {

		ArrayListQuery<CommunityUser> query = new ArrayListQuery<CommunityUser>(this.dataSource, true, "SELECT * FROM users WHERE admin = true", POPULATOR);

		ArrayList<CommunityUser> users = query.executeQuery();

		if (users != null && (getSchools || getGroups)) {

			for(CommunityUser user : users){

				if(getGroups){
					user.setGroups(this.groupDAO.getGroupRelations(user));
				}

				if(getSchools){
					user.setSchools(this.schoolDAO.getSchools(user,false,false));
				}
			}
		}

		return users;
	}

	public ArrayList<CommunityUser> getUsers(School school) throws SQLException {

		ArrayListQuery<CommunityUser> query = new ArrayListQuery<CommunityUser>(this.dataSource, true, "SELECT users.* FROM schooladmins INNER JOIN users ON (schooladmins.userID=users.userID) WHERE schooladmins.schoolID = ?", POPULATOR);

		query.setInt(1, school.getSchoolID());

		return query.executeQuery();
	}

	public Collection<Integer> getAdminAndSchoolAdminIDs() throws SQLException {

		return new ArrayListQuery<Integer>(dataSource, true, "SELECT users.userID FROM users LEFT OUTER JOIN schooladmins ON (users.userID=schooladmins.userID) WHERE (users.admin = true) OR (schooladmins.schoolID IS NOT NULL)", IntegerPopulator.getPopulator()).executeQuery();
	}

	public ArrayList<CommunityUser> getUsers(int resumeHour) throws SQLException {

		ArrayListQuery<CommunityUser> query = new ArrayListQuery<CommunityUser>(this.dataSource, true, "SELECT * FROM users WHERE resume = ?", POPULATOR);

		query.setInt(1, resumeHour);

		ArrayList<CommunityUser> users = query.executeQuery();

		if (users != null) {

			for(CommunityUser user : users){

				user.setGroups(this.groupDAO.getGroupRelations(user));
			}
		}

		return users;
	}

	public void setLastResume(Integer userID, Timestamp lastResume) throws SQLException {

		UpdateQuery query = new UpdateQuery(dataSource, true, "UPDATE users SET lastResume = ? WHERE userID = ?");

		query.setTimestamp(1, lastResume);
		query.setInt(2, userID);

		query.executeUpdate();
	}

	public void add(CommunityUser user) throws SQLException {

		TransactionHandler transactionHandler = null;

		try {
			transactionHandler = new TransactionHandler(dataSource);

			//UpdateQuery query = transactionHandler.getUpdateQuery("INSERT INTO users VALUES (email = ?, firstName = ?, lastName = ?, phoneHome = ?, phoneMobile = ?, phoneWork = ?, lastLogin = ?, password = ?, lastLogin = ?, resume = ?, lastResume = ?,  admin = ?, enabled = ?, added = ?)");
			UpdateQuery query = transactionHandler.getUpdateQuery("INSERT INTO users VALUES (null,?,?,?,?,?,?,?,PASSWORD(?),?,?,?,?,?,?)");

			query.setString(1, user.getEmail());
			query.setString(2, user.getFirstname());
			query.setString(3, user.getLastname());
			query.setString(4, user.getPhoneHome());
			query.setString(5, user.getPhoneMobile());
			query.setString(6, user.getPhoneWork());
			query.setTimestamp(7, user.getLastLogin());
			query.setString(8, user.getPassword());
			query.setInt(9, user.getResume());
			query.setTimestamp(10, user.getLastResume());
			query.setBoolean(11, user.isEnabled());
			query.setTimestamp(12, user.getAdded());
			query.setBoolean(13, user.isAdmin());
			query.setString(14, user.getLanguage() == null ? null : user.getLanguage().getLanguageCode());

	        IntegerKeyCollector keyCollector = new IntegerKeyCollector();
	        
	        query.executeUpdate(keyCollector);
			
			user.setUserID(keyCollector.getKeyValue());

			addGroups(user,transactionHandler);

			addSchools(user,transactionHandler);

			transactionHandler.commit();

		} finally {
			TransactionHandler.autoClose(transactionHandler);
		}
	}
}

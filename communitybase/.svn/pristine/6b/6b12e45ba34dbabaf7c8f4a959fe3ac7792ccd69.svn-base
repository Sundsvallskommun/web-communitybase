package se.dosf.communitybase.daos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.GroupRelation;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.populators.CommunityGroupPopulator;
import se.dosf.communitybase.populators.GroupRelationPopulator;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.HashMapQuery;
import se.unlogic.standardutils.dao.querys.IntegerKeyCollector;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.populators.CharacterPopulator;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;

public class CommunityGroupDAO extends BaseDAO {

	private static final CommunityGroupPopulator GROUP_POPULATOR = new CommunityGroupPopulator();
	private static final GroupRelationPopulator GROUP_ACCESS_ENTRY_POPULATOR = new GroupRelationPopulator();
	private CommunityUserDAO userDAO;

	public CommunityGroupDAO(DataSource ds) {
		super(ds);
	}

	public void setUserDao(CommunityUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public ArrayList<CommunityGroup> getGroups(boolean getUsers, boolean getUserSchools, boolean getUserGroups) throws SQLException {

		ArrayListQuery<CommunityGroup> query = new ArrayListQuery<CommunityGroup>(this.dataSource.getConnection(), true, "SELECT groups.*, schools.schoolID, schools.name as schoolname FROM groups INNER JOIN schools on (schools.schoolID=groups.schoolID)", GROUP_POPULATOR);

		ArrayList<CommunityGroup> groups = query.executeQuery();

		if (groups != null && getUsers) {

			for (CommunityGroup group : groups) {

				group.setUsers(this.userDAO.getUsers(group, getUserSchools, getUserGroups));
			}
		}

		return groups;

	}

	public CommunityGroup getGroup(int groupID, boolean users) throws SQLException {

		ObjectQuery<CommunityGroup> query = new ObjectQuery<CommunityGroup>(this.dataSource.getConnection(), true, "SELECT groups.*, schools.schoolID, schools.name as schoolname FROM groups INNER JOIN schools on (schools.schoolID = groups.schoolID) WHERE groupID = ?", GROUP_POPULATOR);

		query.setInt(1, groupID);

		CommunityGroup group = query.executeQuery();

		if (group != null && users) {
			group.setUsers(this.userDAO.getUsers(group,false, true));
		}

		return group;

	}

	public Map<CommunityGroup, GroupRelation> getGroupRelations(CommunityUser user) throws SQLException {

		String sql = "SELECT groups.*, groupusers.accessLevel, groupusers.comment, schools.schoolID, schools.name as schoolname FROM groupusers INNER JOIN groups ON (groupusers.groupID=groups.groupID) INNER JOIN schools on (schools.schoolID=groups.schoolID) WHERE (groupusers.userID = ?) ORDER BY groups.name";

		HashMapQuery<CommunityGroup, GroupRelation> query = new HashMapQuery<CommunityGroup, GroupRelation>(this.dataSource.getConnection(), true, sql, GROUP_ACCESS_ENTRY_POPULATOR);

		query.setInt(1, user.getUserID());

		return query.executeQuery();

	}

	public Map<CommunityGroup, GroupRelation> getGroupRelations(CommunityUser user, Connection connection) throws SQLException {

		String sql = "SELECT groups.*, groupusers.accessLevel, groupusers.comment, schools.schoolID, schools.name as schoolname FROM groupusers INNER JOIN groups ON (groupusers.groupID=groups.groupID) INNER JOIN schools on (schools.schoolID=groups.schoolID) WHERE (groupusers.userID = ?) ORDER BY groups.name";

		HashMapQuery<CommunityGroup, GroupRelation> query = new HashMapQuery<CommunityGroup, GroupRelation>(connection, false, sql, GROUP_ACCESS_ENTRY_POPULATOR);

		query.setInt(1, user.getUserID());

		return query.executeQuery();

	}

	public void update(CommunityGroup cGroup) throws SQLException {

		TransactionHandler transactionHandler = null;

		try {

			transactionHandler = new TransactionHandler(dataSource);

			UpdateQuery query;

			query = transactionHandler.getUpdateQuery("UPDATE groups SET name = ?, schoolID = ?, email = ? WHERE groupID = ?");

			query.setString(1, cGroup.getName());
			query.setInt(2, cGroup.getSchool().getSchoolID());
			if(cGroup.getEmail() != null){
				query.setString(3, cGroup.getEmail());
			}else{
				query.setNull(3, 0);
			}
			query.setInt(4, cGroup.getGroupID());

			query.executeUpdate();
			transactionHandler.commit();

		} finally {

			if (transactionHandler != null && !transactionHandler.isClosed()) {
				transactionHandler.abort();
			}
		}
	}

	public void delete(CommunityGroup group) throws SQLException {

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, "DELETE FROM groups WHERE groupID = ?");

		query.setInt(1, group.getGroupID());

		query.executeUpdate();
	}

	public ArrayList<CommunityGroup> getGroups(Collection<Integer> groupIDList, boolean getSchools) throws SQLException {
		
		String sql;

		if (getSchools) {
			sql = "SELECT groups.*, schools.name as schoolname FROM groups INNER JOIN schools on (schools.schoolID=groups.schoolID) WHERE groupID IN (" + StringUtils.toCommaSeparatedString(groupIDList) + ") ORDER BY groups.name";
		} else {
			sql = "SELECT * FROM groups WHERE groupID IN (" + StringUtils.toCommaSeparatedString(groupIDList) + ") ORDER BY name";
		}

		ArrayListQuery<CommunityGroup> query = new ArrayListQuery<CommunityGroup>(this.dataSource.getConnection(), true, sql, GROUP_POPULATOR);
		
		return query.executeQuery();
	}

	public ArrayList<CommunityGroup> getActivatedModules() throws SQLException {
		Connection connection = null;
		try {
			connection = this.dataSource.getConnection();

			ArrayListQuery<CommunityGroup> query = new ArrayListQuery<CommunityGroup>(connection, false, "SELECT * FROM modules", GROUP_POPULATOR);

			ArrayList<CommunityGroup> module = query.executeQuery();

			/*
			if(module != null && users.isEnabled()){
				for(CommunityGroup modules : module){
					this.userDAO.getUsers(group, connection);
				}
			}
			 */
			return module;
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException ec) {
			}
		}
	}

	public ArrayList<CommunityGroup> getGroups(School school) throws SQLException {

		ArrayListQuery<CommunityGroup> query = new ArrayListQuery<CommunityGroup>(this.dataSource.getConnection(), true, "SELECT groupID, name, email FROM groups WHERE schoolID = ? ORDER BY name", GROUP_POPULATOR);

		query.setInt(1, school.getSchoolID());

		return query.executeQuery();
	}

	public ArrayList<CommunityGroup> getGroups(School school, Connection connection) throws SQLException {

		ArrayListQuery<CommunityGroup> query = new ArrayListQuery<CommunityGroup>(connection, false, "SELECT groupID, name, email FROM groups WHERE schoolID = ? ORDER BY name", GROUP_POPULATOR);

		query.setInt(1, school.getSchoolID());

		return query.executeQuery();
	}

	public void add(CommunityGroup group) throws SQLException {

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, "INSERT INTO groups VALUES (null,?,?,?)");

		query.setInt(1, group.getSchool().getSchoolID());
		query.setString(2, group.getName());
		if(group.getEmail() != null){
			query.setString(3, group.getEmail());
		}else{
			query.setNull(3, 0);
		}

        IntegerKeyCollector keyCollector = new IntegerKeyCollector();
        
        query.executeUpdate(keyCollector);
		
		group.setGroupID(keyCollector.getKeyValue());
	}
	
	public int getGroupCount() throws SQLException {

		ObjectQuery<Integer> query = new ObjectQuery<Integer>(this.dataSource, "SELECT COUNT(groupID) FROM groups", IntegerPopulator.getPopulator());
		
		return query.executeQuery();
	}
	
	public List<CommunityGroup> getGroups(Order order, char startsWith) throws SQLException {

		String sql = "SELECT * FROM groups WHERE name LIKE '" + startsWith + "%' ORDER BY name " + order; 
		
		ArrayListQuery<CommunityGroup> query = new ArrayListQuery<CommunityGroup>(this.dataSource.getConnection(), true, sql, GROUP_POPULATOR);
			
		return query.executeQuery();
	}
	
	public List<Character> getGroupFirstLetterIndex() throws SQLException {

		return new ArrayListQuery<Character>(dataSource, "SELECT DISTINCT UPPER(LEFT(name, 1)) as letter FROM groups ORDER BY letter ", CharacterPopulator.getPopulator()).executeQuery();
	}

	public List<CommunityGroup> searchGroups(String search, boolean attributes) throws SQLException {

		String sql = "SELECT * FROM groups WHERE name LIKE ? ORDER BY name";
		
		ArrayListQuery<CommunityGroup> query = new ArrayListQuery<CommunityGroup>(this.dataSource, sql, GROUP_POPULATOR);
		
		query.setString(1, "%" + search + "%");

		return query.executeQuery();
	}
}

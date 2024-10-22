package se.dosf.communitybase.modules.members.daos;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.BooleanQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;
import se.unlogic.standardutils.populators.IntegerPopulator;

public class MembersDAO extends BaseDAO {

	private static final String HIDDEN_USERS_TABLE = "hiddengroupusers";
	
	public MembersDAO(DataSource dataSource) {
		super(dataSource);
		
	}

	public boolean userIsHidden(CommunityGroup group, CommunityUser user) throws SQLException {
		
		BooleanQuery query = new BooleanQuery(this.dataSource, "SELECT userID FROM " + HIDDEN_USERS_TABLE + " WHERE groupID = ? AND userID = ?");
		
		query.setInt(1, group.getGroupID());
		query.setInt(2, user.getUserID());
		
		return query.executeQuery();
	}

	public List<Integer> getHiddenUserIDs(CommunityGroup group) throws SQLException {
		
		ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(this.dataSource, "SELECT userID FROM " + HIDDEN_USERS_TABLE + " WHERE groupID = ?", IntegerPopulator.getPopulator());
		
		query.setInt(1, group.getGroupID());
		
		return query.executeQuery();
	}

	public void setVisible(CommunityGroup group, CommunityUser user) throws SQLException {
		
		UpdateQuery query = new UpdateQuery(this.dataSource, "DELETE FROM " + HIDDEN_USERS_TABLE + " WHERE userID = ? AND groupID = ?");
		
		query.setInt(1, user.getUserID());
		query.setInt(2, group.getGroupID());
		query.executeUpdate();
	}
	
	public void setInVisible(CommunityGroup group, CommunityUser user) throws SQLException {

		UpdateQuery query = new UpdateQuery(this.dataSource, "INSERT INTO " + HIDDEN_USERS_TABLE + " VALUES(?,?)");
		
		query.setInt(1, user.getUserID());
		query.setInt(2, group.getGroupID());
		query.executeUpdate();
	}
	
}

package se.dosf.communitybase.modules.absence.daos;

import java.sql.SQLException;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityUser;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.standardutils.dao.querys.BooleanQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;

public class AbsenceNotificationDAO extends BaseDAO {

	private static final String TABLE_NAME = "absence_disablednotifications";
	
	public AbsenceNotificationDAO(DataSource dataSource) {
		
		super(dataSource);
		
	}

	public boolean hasDisabledNotification(CommunityUser user) throws SQLException {
		
		BooleanQuery query = new BooleanQuery(dataSource, "SELECT userID FROM " + TABLE_NAME + " WHERE userID = ?");
		
		query.setInt(1, user.getUserID());
		
		return query.executeQuery();
		
	}
	
	public void disableNotification(CommunityUser user) throws SQLException {
		
		UpdateQuery query = new UpdateQuery(dataSource, "INSERT INTO " + TABLE_NAME + " VALUES(?)");
		
		query.setInt(1, user.getUserID());
		
		query.executeUpdate();
		
	}
	
	public void enableNotification(CommunityUser user) throws SQLException {
		
		UpdateQuery query = new UpdateQuery(dataSource, "DELETE FROM " + TABLE_NAME + " WHERE userID = ?");
		
		query.setInt(1, user.getUserID());
		
		query.executeUpdate();
		
	}
	
}

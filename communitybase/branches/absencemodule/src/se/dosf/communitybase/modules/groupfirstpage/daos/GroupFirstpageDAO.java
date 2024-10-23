package se.dosf.communitybase.modules.groupfirstpage.daos;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.modules.groupfirstpage.beans.GroupFirstpage;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;
import se.unlogic.standardutils.populators.annotated.AnnotatedResultSetPopulator;

public class GroupFirstpageDAO extends BaseDAO {

	private static final AnnotatedResultSetPopulator<GroupFirstpage> POPULATOR = new AnnotatedResultSetPopulator<GroupFirstpage>(GroupFirstpage.class);
	private static final AnnotatedResultSetPopulator<IdentifiedEvent> EVENT_POPULATOR = new AnnotatedResultSetPopulator<IdentifiedEvent>(IdentifiedEvent.class);
	
	public GroupFirstpageDAO(DataSource dataSource) {
		
		super(dataSource);
		
	}
	
	public void update(GroupFirstpage firstpage) throws SQLException{
		
		UpdateQuery query = new UpdateQuery(this.dataSource, true, "INSERT INTO groupfirstpage VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE title = ?, content = ?, changed = ?, image = ?, imagelocation = ?");
		
		query.setInt(1, firstpage.getGroupID());
		query.setString(2, firstpage.getTitle());
		query.setString(3, firstpage.getContent());
		query.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
		
		if(firstpage.getImage() != null){

			query.setBlob(5, firstpage.getImage());
			query.setString(6, firstpage.getImageLocation());
			query.setBlob(10, firstpage.getImage());
			query.setString(11, firstpage.getImageLocation());
			
		}else{

			query.setNull(5, 0);
			query.setNull(6, 0);
			query.setNull(10, 0);
			query.setNull(11, 0);

		}
		
		query.setString(7, firstpage.getTitle());
		query.setString(8, firstpage.getContent());
		query.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
		
		query.executeUpdate();
		
	}
	
	public void delete(GroupFirstpage firstpage) throws SQLException{
		
		UpdateQuery query = new UpdateQuery(this.dataSource, true, "DELETE FROM groupfirstpage WHERE groupID = ?");
		
		query.setInt(1, firstpage.getGroupID());
		
		query.executeUpdate();
		
	}
	
	public GroupFirstpage get(CommunityGroup group) throws SQLException{
		
		ObjectQuery<GroupFirstpage> query = new ObjectQuery<GroupFirstpage>(this.dataSource, true, "SELECT * FROM groupfirstpage WHERE groupID = ?", POPULATOR);
		
		query.setInt(1, group.getGroupID());
		
		return query.executeQuery();
		
	}
	
	public List<IdentifiedEvent> getEvents(CommunityGroup group, Timestamp lastLogin) throws SQLException {

		ArrayListQuery<IdentifiedEvent> query = new ArrayListQuery<IdentifiedEvent>(dataSource, true, "SELECT groupID as id, title as description, title, changed as added FROM groupfirstpage WHERE groupID = ? AND changed > ? ORDER by changed", EVENT_POPULATOR);

		query.setInt(1, group.getGroupID());
		query.setTimestamp(2, lastLogin);

		return query.executeQuery();
	}
	                                        
}

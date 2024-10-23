package se.dosf.communitybase.modules.links;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.SimpleEvent;
import se.dosf.communitybase.enums.RelationType;
import se.dosf.communitybase.modules.links.populators.LinkPopulator;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.IntegerKeyCollector;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;
import se.unlogic.standardutils.populators.annotated.AnnotatedResultSetPopulator;

public class LinkDAO extends BaseDAO {

	private static final AnnotatedResultSetPopulator<SimpleEvent> EVENT_POPULATOR = new AnnotatedResultSetPopulator<SimpleEvent>(SimpleEvent.class);

	private LinkPopulator POPULATOR = null;

	public LinkDAO(DataSource ds, UserHandler userHandler) {
		super(ds);
		this.POPULATOR = new LinkPopulator(userHandler);
	}

	public void addGroupLink(CommunityGroup group, CommunityUser user, Link link) throws SQLException{

		TransactionHandler transactionHandler = null;

		try{
			transactionHandler = new TransactionHandler(dataSource);

			String sql = "INSERT INTO links VALUES (null,?,?,?,?)";

			UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

			query.setString(1, link.getDescription());
			query.setString(2, link.getUrl());
			query.setInt(3, user.getUserID());
			query.setTimestamp(4, link.getDate());

	        IntegerKeyCollector keyCollector = new IntegerKeyCollector();
	        
	        query.executeUpdate(keyCollector);
			
			Integer linkID = keyCollector.getKeyValue();

			String sql2 = "INSERT INTO linkgroups VALUES(null, ?, ?)";

			UpdateQuery query2 = new UpdateQuery(this.dataSource.getConnection(), true, sql2);

			query2.setInt(1, group.getGroupID());
			query2.setInt(2, linkID);

			query2.executeUpdate();

			transactionHandler.commit();

		}finally{
			TransactionHandler.autoClose(transactionHandler);
		}


	}

	public void updateLink(Link link) throws SQLException{

		String sql = "UPDATE links SET description = ?, url = ?  WHERE  linkID = ? ";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setString(1, link.getDescription());
		query.setString(2, link.getUrl());
		query.setInt(3, link.getLinkID());

		query.executeUpdate();
	}

	public void deleteLink(Integer linkID) throws SQLException{

		String sql = "DELETE FROM links WHERE linkID = ?";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(),true, sql);

		query.setInt(1, linkID);

		query.executeUpdate();
	}

	public ArrayList<Link> getAllGroupLinks(Integer groupID) throws SQLException{

		String sql = "SELECT links.* " +
		"FROM links, linkgroups, users " +
		"WHERE linkgroups.groupID = ? AND linkgroups.linkID = links.linkID and links.posterID = users.userID;";

		ArrayListQuery<Link> query = new ArrayListQuery<Link>(this.dataSource.getConnection(), true, sql, POPULATOR);

		query.setInt(1, groupID);

		ArrayList<Link> bean = 	query.executeQuery();

		return bean;
	}
	
	public void addSchoolLink(CommunityGroup group, CommunityUser user, Link link) throws SQLException{

		TransactionHandler transactionHandler = null;

		try{
			transactionHandler = new TransactionHandler(dataSource);

			String sql = "INSERT INTO links VALUES (null,?,?,?,?)";

			UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

			query.setString(1, link.getDescription());
			query.setString(2, link.getUrl());
			query.setInt(3, user.getUserID());
			query.setTimestamp(4, link.getDate());

	        IntegerKeyCollector keyCollector = new IntegerKeyCollector();
	        
	        query.executeUpdate(keyCollector);
			
			Integer linkID = keyCollector.getKeyValue();

			String sql2 = "INSERT INTO schoollinks VALUES(null, ?, ?)";

			UpdateQuery query2 = new UpdateQuery(this.dataSource.getConnection(), true, sql2);

			query2.setInt(1, group.getSchool().getSchoolID());
			query2.setInt(2, linkID);

			query2.executeUpdate();

			transactionHandler.commit();

		}finally{
			if(transactionHandler != null && !transactionHandler.isClosed()){
				transactionHandler.abort();
			}
		}
	}

	public ArrayList<Link> getAllSchoolLinks(Integer schoolID) throws SQLException{

		ArrayListQuery<Link> query = new ArrayListQuery<Link>(this.dataSource.getConnection(), true, "SELECT links.* FROM links, schoollinks where schoollinks.schoolID = ? AND schoollinks.linkID = links.linkID", POPULATOR);

		query.setInt(1, schoolID);

		ArrayList<Link> bean = query.executeQuery();


		return bean;
	}

	public void addGlobalLink(CommunityGroup group, CommunityUser user, Link link) throws SQLException{

		TransactionHandler transactionHandler = null;

		try{
			transactionHandler = new TransactionHandler(dataSource);

			String sql = "INSERT INTO links VALUES (null,?,?,?,?)";

			UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

			query.setString(1, link.getDescription());
			query.setString(2, link.getUrl());
			query.setInt(3, user.getUserID());
			query.setTimestamp(4, link.getDate());

	        IntegerKeyCollector keyCollector = new IntegerKeyCollector();
	        
	        query.executeUpdate(keyCollector);
			
			Integer linkID = keyCollector.getKeyValue();

			String sql2 = "INSERT INTO globallinks VALUES(null, ?)";

			UpdateQuery query2 = new UpdateQuery(this.dataSource.getConnection(), true, sql2);

			query2.setInt(1, linkID);

			query2.executeUpdate();

			transactionHandler.commit();

		}finally{
			TransactionHandler.autoClose(transactionHandler);
		}


	}

	public ArrayList<Link> getAllGlobalLinks() throws SQLException{

		String sql = "SELECT links.* FROM links, globallinks WHERE globallinks.linkID = links.linkID";

		ArrayListQuery<Link> query = new ArrayListQuery<Link>(this.dataSource.getConnection(), true, sql , POPULATOR);

		ArrayList<Link> bean = query.executeQuery();

		return bean;
	}

	public Link getLink(Integer linkID, RelationType type) throws SQLException {

		String join = null;
		
		if(type.equals(RelationType.GLOBAL)) {
			join = "INNER JOIN globallinks ON (globallinks.linkID = links.linkID)";
		} else if(type.equals(RelationType.SCHOOL)) {
			join = "INNER JOIN schoollinks ON (schoollinks.linkID = links.linkID)";
		} else {
			join = "INNER JOIN linkgroups ON (linkgroups.linkID = links.linkID)";
		}
		
		String sql = "SELECT links.* FROM links " + join + " WHERE links.linkID = ?";

		ObjectQuery<Link> query = new ObjectQuery<Link>(this.dataSource.getConnection(), true, sql , POPULATOR);

		query.setInt(1, linkID);

		Link link = query.executeQuery();

		return link;
	}
	
	public List<SimpleEvent> getEvents(CommunityGroup group, Timestamp lastLogin) throws SQLException {

		String sql ="SELECT DISTINCT\r\n" +
		"  links.description as title,description,\r\n" +
		"  links.posted as added\r\n" +
		"FROM\r\n" +
		"  links\r\n" +
		"  LEFT OUTER JOIN globallinks ON (globallinks.linkID=links.linkID)\r\n" +
		"  LEFT OUTER JOIN linkgroups ON (linkgroups.linkID=links.linkID)\r\n" +
		"  LEFT OUTER JOIN schoollinks ON (schoollinks.linkID=links.linkID)\r\n" +
		"WHERE\r\n" +
		"  posted > ? AND\r\n" +
		"  ((links.linkID = globallinks.linkID) OR\r\n" +
		"  (linkgroups.groupID = ?) OR\r\n" +
		"  (schoollinks.schoolID = ?))\r\n" +
		"ORDER BY\r\n" +
		"  links.posted DESC";

		ArrayListQuery<SimpleEvent> query = new ArrayListQuery<SimpleEvent>(dataSource, true, sql, EVENT_POPULATOR);

		query.setTimestamp(1, lastLogin);
		query.setInt(2, group.getGroupID());
		query.setInt(3, group.getSchool().getSchoolID());

		return query.executeQuery();
	}

}

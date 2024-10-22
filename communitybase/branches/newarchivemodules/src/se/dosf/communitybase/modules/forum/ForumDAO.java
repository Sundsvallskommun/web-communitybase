package se.dosf.communitybase.modules.forum;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.modules.forum.beans.Forum;
import se.dosf.communitybase.modules.forum.beans.Post;
import se.dosf.communitybase.modules.forum.beans.Thread;
import se.dosf.communitybase.modules.forum.populators.ForumPopulator;
import se.dosf.communitybase.modules.forum.populators.ForumPostPopulator;
import se.dosf.communitybase.modules.forum.populators.ForumThreadPopulator;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.IntegerKeyCollector;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.populators.annotated.AnnotatedResultSetPopulator;

public class ForumDAO extends BaseDAO {

	private static final AnnotatedResultSetPopulator<IdentifiedEvent> EVENT_POPULATOR = new AnnotatedResultSetPopulator<IdentifiedEvent>(IdentifiedEvent.class);
	private final ForumPopulator POPULATOR = new ForumPopulator();
	private ForumThreadPopulator THREADPOPULATOR  = null;
	private ForumPostPopulator POSTPOPULATOR = null;

	
	public ForumDAO(DataSource ds, UserHandler userHandler) {
		super(ds);
		this.THREADPOPULATOR = new ForumThreadPopulator(userHandler);
		this.POSTPOPULATOR = new ForumPostPopulator(userHandler);
	}

	public ArrayList<Forum> showCommunity(CommunityGroup group, boolean thread) throws SQLException{

		String sql = "SELECT * FROM forums WHERE groupID = ? ORDER BY name";

		ArrayListQuery<Forum> query = new ArrayListQuery<Forum>(this.dataSource.getConnection(), true, sql, POPULATOR);

		query.setInt(1, group.getGroupID());

		ArrayList<Forum> communities = query.executeQuery();

		/*
		if(thread && communities != null){
			for(CommunityBean community: communities){
				if(community.getForumID() != null){
					community.setThreads(getCommunityThreads(community.getForumID(), true));
				}
			}
		}*/

		return communities;
	}

	public void addCommunity(CommunityGroup group, Forum community) throws SQLException {

		String sql = "INSERT INTO forums VALUES (null,?,?,?)";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setString(1, community.getName());
		query.setInt(2, group.getGroupID());
		query.setString(3, community.getDescription());

		query.executeUpdate();

	}

	public void updateCommunity(Forum community) throws SQLException{

		String sql = "UPDATE forums SET name = ?, groupID = ?, description = ? WHERE forums.forumID = ?";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setString(1, community.getName());
		query.setInt(2, community.getGroupID());
		query.setString(3, community.getDescription());
		query.setInt(4, community.getForumID());

		query.executeUpdate();
	}


	public void deleteCommunity(Integer communityID) throws SQLException {
		String sql = "DELETE FROM forums where forumID = ? ";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setInt(1, communityID);

		query.executeUpdate();
	}

	public ArrayList<Thread> getCommunityThreads(Integer forumID, boolean posts) throws SQLException{

		String sql = "SELECT * FROM " +
		"(SELECT *, (SELECT MAX(posted) " +
		"FROM forumposts WHERE forumposts.forumThreadID=forumthreads.forumThreadID) " +
		"Test FROM forumthreads) T1 " +
		"WHERE T1.forumID = ? ORDER BY COALESCE(Test, posted) DESC;";

		ArrayListQuery<Thread> query = new ArrayListQuery<Thread>(this.dataSource.getConnection(), true, sql, THREADPOPULATOR);

		query.setInt(1, forumID);

		ArrayList<Thread> threads = query.executeQuery();


		if(posts && threads != null){
			for(Thread thread: threads){
				if(thread.getThreadID() != null){
					thread.setPosts(getCommunityPosts(thread.getThreadID()));
				}
			}
		}

		return threads;
	}

	public Integer countCommunityPosts(Integer threadID) throws SQLException{

		String sql = "SELECT Count(ForumPostID) as count FROM forumposts WHERE ForumThreadID = ?";

		ObjectQuery<Integer> query = new ObjectQuery<Integer>(this.dataSource.getConnection(), true, sql, IntegerPopulator.getPopulator());
		query.setInt(1, threadID);

		return query.executeQuery();
	}

	public Integer getCommunityID(Integer threadID) throws SQLException{

		String sql = "SELECT forumID FROM forumthreads WHERE forumThreadID = ? ";

		ObjectQuery<Integer> query = new ObjectQuery<Integer>(this.dataSource.getConnection(), true, sql , IntegerPopulator.getPopulator());

		query.setInt(1, threadID);

		return query.executeQuery();
	}

	public ArrayList<Post> getCommunityPosts(Integer threadID) throws SQLException{

		String sql = "SELECT * FROM forumposts WHERE forumThreadID = ?";

		ArrayListQuery<Post> query = new ArrayListQuery<Post>(this.dataSource.getConnection(), true, sql, POSTPOPULATOR);

		query.setInt(1, threadID);

		ArrayList<Post> posts = query.executeQuery();

		return posts;
	}

	public Forum getCommunity(Integer communityID) throws SQLException {

		String sql = "SELECT * FROM forums WHERE forumID = ?";

		ObjectQuery<Forum> query = new ObjectQuery<Forum>(this.dataSource.getConnection(), true, sql, POPULATOR);

		query.setInt(1, communityID);

		Forum community = query.executeQuery();

		return community;
	}

	public Thread getThread(Integer threadID) throws SQLException {

		String sql = "SELECT * FROM forumthreads WHERE forumThreadID = ?";

		ObjectQuery<Thread> query = new ObjectQuery<Thread>(this.dataSource.getConnection(), true, sql, THREADPOPULATOR);

		query.setInt(1, threadID);

		Thread thread = query.executeQuery();

		return thread;
	}


	public void updateCommunityThread(Thread thread, Integer userID) throws SQLException {

		String sql = "UPDATE forumthreads SET subject = ?, message = ?, changerID = ?, changed = ? WHERE forumThreadID = ?";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setString(1, thread.getSubject());
		query.setString(2, thread.getMessage());
		query.setInt(3, userID);
		query.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
		query.setInt(5, thread.getThreadID());

		query.executeUpdate();
	}


	public void deleteCommunityThread(Integer threadID) throws SQLException {
		String sql = "DELETE FROM forumthreads WHERE forumThreadID = ?";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setInt(1, threadID);

		query.executeUpdate();
	}

	public void addCommunityThread(Thread thread, CommunityGroup group,Integer communityID, Integer userID) throws SQLException {

		String sql = "INSERT INTO forumthreads VALUES (null, ?, ?, ?, ?, null, ? ,null);";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setInt(1, communityID);
		query.setString(2, thread.getSubject());
		query.setString(3, thread.getMessage());
		query.setInt(4, userID);
		query.setTimestamp(5, thread.getPosted());

        IntegerKeyCollector keyCollector = new IntegerKeyCollector();
        
        query.executeUpdate(keyCollector);
		
		thread.setThreadID(keyCollector.getKeyValue());

	}



	public void addCommunityPost(Post post, Integer threadID, Integer userID) throws SQLException{
		String sql = "INSERT INTO forumposts VALUES (null, ?, ?, ?, null, ?, null);";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setInt(1, threadID);
		query.setString(2, post.getMessage());
		query.setInt(3, userID);
		query.setTimestamp(4, post.getPosted());

		query.executeUpdate();
	}


	public Post getPost(Integer postID) throws SQLException {
		String sql = "SELECT * FROM forumposts WHERE forumPostID = ?";

		ObjectQuery<Post> query = new ObjectQuery<Post>(this.dataSource.getConnection(), true, sql, POSTPOPULATOR);

		query.setInt(1, postID);

		Post post = query.executeQuery();

		return post;
	}

	public void updateCommunityPost(Post post, Integer userID) throws SQLException {

		String sql = "UPDATE forumposts SET message = ?, changerID = ?, changed = ? WHERE forumPostID = ?";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setString(1, post.getMessage());
		query.setInt(2, userID);
		query.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
		query.setInt(4, post.getPostID());

		query.executeUpdate();

	}

	public void deleteCommunityPost(Integer postID) throws SQLException {
		String sql = "DELETE FROM forumposts WHERE forumPostID = ?";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setInt(1, postID);

		query.executeUpdate();
	}

	public List<IdentifiedEvent> getEvents(CommunityGroup group, Timestamp lastLogin) throws SQLException {

		ArrayListQuery<IdentifiedEvent> query = new ArrayListQuery<IdentifiedEvent>(dataSource, "SELECT forumThreadID as id, subject as title, subject as description, GREATEST(COALESCE(changed,posted),COALESCE(post,1)) as added FROM (SELECT *, (SELECT MAX(posted) FROM forumposts WHERE forumposts.forumThreadID=forumthreads.forumThreadID) post FROM forumthreads) T1  WHERE T1.forumID IN (SELECT forumID FROM forums WHERE groupID = ?) AND GREATEST(COALESCE(changed,posted),COALESCE(post,1)) > ? ORDER BY added DESC", EVENT_POPULATOR);

		query.setInt(1, group.getGroupID());
		query.setTimestamp(2, lastLogin);

		return query.executeQuery();
	}

}

package se.dosf.communitybase.modules.forum.populators;

import java.sql.ResultSet;
import java.sql.SQLException;

import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.modules.forum.beans.Post;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;

public class ForumPostPopulator implements BeanResultSetPopulator<Post> {
	private UserHandler userHandler;
	
	public ForumPostPopulator() {
		
	}
	
	public ForumPostPopulator(UserHandler userHandler) {
		this.userHandler = userHandler;
	}
	
	public Post populate(ResultSet rs) throws SQLException {

		Post post = new Post();
		Integer postID = null;

		post.setPoster((CommunityUser)userHandler.getUser(rs.getInt("posterID"), false));
		
		post.setPostID(rs.getInt("forumPostID"));
		post.setThreadID(rs.getInt("forumThreadID"));
		post.setMessage(rs.getString("message"));
		post.setPosterID(rs.getInt("posterID"));
		String id = rs.getString("changerID");
		if(id != null){
			postID = Integer.parseInt(id.toString());
			post.setChangerID(postID);
			post.setChanger((CommunityUser)userHandler.getUser(postID, false));
		}
		post.setPosted(rs.getTimestamp("posted"));
		post.setChanged(rs.getTimestamp("changed"));

		return post;
	}
}

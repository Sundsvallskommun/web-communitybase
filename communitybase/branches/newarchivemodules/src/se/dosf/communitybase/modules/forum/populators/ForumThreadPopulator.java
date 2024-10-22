package se.dosf.communitybase.modules.forum.populators;

import java.sql.ResultSet;
import java.sql.SQLException;

import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.modules.forum.beans.Thread;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;

public class ForumThreadPopulator implements BeanResultSetPopulator<Thread>{

	private UserHandler userHandler;
	
	public ForumThreadPopulator() {
		
	}
	
	public ForumThreadPopulator(UserHandler userHandler) {
		this.userHandler = userHandler;
	}

	@Override
	public Thread populate(ResultSet rs) throws SQLException {

		Thread thread = new Thread();
		Integer changerID = null;

		thread.setPoster((CommunityUser)userHandler.getUser(rs.getInt("posterID"), false, false));
		thread.setPosterID(rs.getInt("posterID"));
		thread.setPosted(rs.getTimestamp("posted"));
		thread.setMessage(rs.getString("message"));
		thread.setThreadID(rs.getInt("forumThreadID"));
		String id = rs.getString("changerID");
		if(id != null){
			changerID = Integer.parseInt(id.toString());
			thread.setChangerID(changerID);
			thread.setChanger((CommunityUser)userHandler.getUser(rs.getInt("changerID"), false, false));
		}
		thread.setChanged(rs.getTimestamp("changed"));
		thread.setSubject(rs.getString("subject"));
		thread.setForumID(rs.getInt("forumID"));

		return thread;
	}
}

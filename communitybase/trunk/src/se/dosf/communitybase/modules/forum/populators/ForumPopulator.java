package se.dosf.communitybase.modules.forum.populators;

import java.sql.ResultSet;
import java.sql.SQLException;

import se.dosf.communitybase.modules.forum.beans.Forum;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;

public class ForumPopulator implements BeanResultSetPopulator<Forum> {

	public Forum populate(ResultSet rs) throws SQLException {

		Forum communityBean = new Forum();

		communityBean.setForumID(rs.getInt("forumID"));
		communityBean.setName(rs.getString("name"));
		communityBean.setGroupID(rs.getInt("groupID"));
		communityBean.setDescription(rs.getString("description"));

		return communityBean;
	}
}

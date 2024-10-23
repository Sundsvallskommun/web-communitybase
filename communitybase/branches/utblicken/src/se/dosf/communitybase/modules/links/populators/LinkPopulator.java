package se.dosf.communitybase.modules.links.populators;

import java.sql.ResultSet;
import java.sql.SQLException;

import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.modules.links.Link;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;

public class LinkPopulator implements BeanResultSetPopulator<Link> {

	private UserHandler userHandler;

	public LinkPopulator(){

	}
	public LinkPopulator (UserHandler userHandler){
		this.userHandler = userHandler;
	}
	public Link populate(ResultSet rs) throws SQLException {

		Link linkBean = new Link();

		linkBean.setDate(rs.getTimestamp("posted"));
		linkBean.setDescription(rs.getString("description"));
		linkBean.setUrl(rs.getString("url"));
		linkBean.setLinkID(rs.getInt("linkID"));
		linkBean.setPostedBy((CommunityUser)userHandler.getUser(rs.getInt("posterID"), false));

		return linkBean;
	}
}

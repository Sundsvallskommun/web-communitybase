package se.dosf.communitybase.modules.linkarchive.daos;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.modules.linkarchive.beans.Link;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOFactory;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.populators.BeanStringPopulator;
import se.unlogic.standardutils.populators.QueryParameterPopulator;


public class LinkDAO extends AnnotatedDAO<Link> {

	private final String getGroupLinksSQL;

	public LinkDAO(DataSource dataSource, AnnotatedDAOFactory daoFactory, List<? extends QueryParameterPopulator<?>> queryParameterPopulators, List<? extends BeanStringPopulator<?>> typePopulators) {

		super(dataSource, Link.class, daoFactory, queryParameterPopulators, typePopulators);

		getGroupLinksSQL = "SELECT DISTINCT n.* FROM " + this.getTableName() + " as n " +
				"LEFT OUTER JOIN linkarchive_linkgroups as sg ON n.linkID = sg.linkID " +
				"LEFT OUTER JOIN linkarchive_linkschools as ss ON n.linkID = ss.linkID " +
				"WHERE sg.groupID = ? OR ss.schoolID = ? OR n.global = true";
	}


	public List<Link> getLinks(CommunityGroup group, String orderingField, Order order) throws SQLException{

		LowLevelQuery<Link> query = new LowLevelQuery<Link>();

		String sql = getGroupLinksSQL;

		if(orderingField != null){
			sql += " ORDER BY " + orderingField;
		}

		if(order != null){
			sql += " " + order;
		}

		query.setSql(sql);

		query.addParameters(group.getGroupID(), group.getSchool().getSchoolID());

		return getAll(query);
	}
}

package se.dosf.communitybase.modules.filearchive.daos;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.modules.filearchive.beans.Section;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOFactory;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.populators.BeanStringPopulator;
import se.unlogic.standardutils.populators.QueryParameterPopulator;


public class SectionDAO extends AnnotatedDAO<Section> {

	private final String getGroupSectionsSQL;

	public SectionDAO(DataSource dataSource, AnnotatedDAOFactory daoFactory, List<? extends QueryParameterPopulator<?>> queryParameterPopulators, List<? extends BeanStringPopulator<?>> typePopulators) {

		super(dataSource, Section.class, daoFactory, queryParameterPopulators, typePopulators);

		getGroupSectionsSQL = "SELECT DISTINCT n.* FROM " + getTableName() + " as n " + "LEFT OUTER JOIN filearchive_sectiongroups as sg ON n.sectionID = sg.sectionID " + "LEFT OUTER JOIN filearchive_sectionschools as ss ON n.sectionID = ss.sectionID " + "WHERE sg.groupID = ? OR ss.schoolID = ? OR n.global = true";
	}


	public List<Section> getSections(CommunityGroup group, String orderingField, Order order) throws SQLException{

		LowLevelQuery<Section> query = new LowLevelQuery<Section>();

		String sql = getGroupSectionsSQL;

		if(orderingField != null){
			sql += " ORDER BY " + orderingField;
		}

		if(order != null){
			sql += " " + order;
		}

		query.setSql(sql);

		query.addParameters(group.getGroupID(), group.getSchool().getSchoolID());
		query.disableAutoRelations(true);
		query.addRelations(Section.FILE_RELATION, Section.GROUP_RELATION, Section.SCHOOL_RELATION);

		return getAll(query);
	}
}

package se.dosf.communitybase.modules.filearchive.daos;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.modules.filearchive.beans.File;
import se.dosf.communitybase.modules.filearchive.beans.Section;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOFactory;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.populators.BeanStringPopulator;
import se.unlogic.standardutils.populators.QueryParameterPopulator;


public class FileDAO extends AnnotatedDAO<File> {

	private final String getGroupFilesSQL;

	public FileDAO(DataSource dataSource, AnnotatedDAOFactory daoFactory, List<? extends QueryParameterPopulator<?>> queryParameterPopulators, List<? extends BeanStringPopulator<?>> typePopulators) {

		super(dataSource, File.class, daoFactory, queryParameterPopulators, typePopulators);

		getGroupFilesSQL = "SELECT fileID, description, posted, poster, sectionID, filename, size, null as file FROM " + this.getTableName() + " WHERE sectionID IN (SELECT DISTINCT n.sectionID FROM filearchive_sections as n " +
				"LEFT OUTER JOIN filearchive_sectiongroups as sg ON n.sectionID = sg.sectionID " +
				"LEFT OUTER JOIN filearchive_sectionschools as ss ON n.sectionID = ss.sectionID " +
				"WHERE sg.groupID = ? OR ss.schoolID = ? OR n.global = true )";
	}


	public List<File> getFiles(CommunityGroup group, String orderingField, Order order) throws SQLException{

		LowLevelQuery<File> query = new LowLevelQuery<File>();

		String sql = getGroupFilesSQL;

		if(orderingField != null){
			sql += " ORDER BY " + orderingField;
		}

		if(order != null){
			sql += " " + order;
		}

		query.setSql(sql);
		query.addParameters(group.getGroupID(), group.getSchool().getSchoolID());
		query.disableAutoRelations(true);
		query.addRelations(File.SECTION_RELATION, Section.GROUP_RELATION, Section.SCHOOL_RELATION);

		return getAll(query);
	}
	
}

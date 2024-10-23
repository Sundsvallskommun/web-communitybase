package se.dosf.communitybase.daos;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.populators.annotated.AnnotatedResultSetPopulator;

public class EventDAO extends BaseDAO {

	private static final AnnotatedResultSetPopulator<IdentifiedEvent> EVENT_POPULATOR = new AnnotatedResultSetPopulator<IdentifiedEvent>(IdentifiedEvent.class);

	private final String tableName;
	private final String idColumn;
	private final String descriptionColumn;
	private final String dateColumn;
	
	private final String groupRelationTableName;
	private final String schoolRelationTableName;
	
	private String additionalWhereStatement = "";

	public EventDAO(DataSource dataSource, String tableName, String idColumn, String descriptionColumn, String dateColumn) {
		this(dataSource, tableName, "group" + tableName, "school" + tableName, idColumn, descriptionColumn, dateColumn); // Backward compatibility with newslettermodule 
	}
	
	public EventDAO(DataSource dataSource, String tableName, String groupRelationTableName, String schoolRelationTableName, String idColumn, String descriptionColumn, String dateColumn) {
		super(dataSource);

		this.tableName = tableName;
		this.idColumn = idColumn;
		this.descriptionColumn = descriptionColumn;
		this.dateColumn = dateColumn;
		
		this.groupRelationTableName = groupRelationTableName;
		this.schoolRelationTableName = schoolRelationTableName;

	}

	public List<IdentifiedEvent> getEvents(CommunityGroup group, Timestamp lastLogin) throws SQLException {

		String sql = "SELECT DISTINCT o." + idColumn + " as id, o." + descriptionColumn + " as description, o." + descriptionColumn + " as title, o." + dateColumn + " as added FROM " + tableName + " as o " +
				"LEFT OUTER JOIN " + groupRelationTableName + " as go ON o." + idColumn + " = go." + idColumn + " " +
				"LEFT OUTER JOIN " + schoolRelationTableName + " as so ON o." + idColumn + " = so." + idColumn + " " +
				" WHERE " + additionalWhereStatement + " (go.groupID = ? OR so.schoolID = ? OR o.global = ?) AND o." + dateColumn + " > ? ORDER BY " + dateColumn;

		ArrayListQuery<IdentifiedEvent> query = new ArrayListQuery<IdentifiedEvent>(this.dataSource, sql, EVENT_POPULATOR);

		query.setInt(1, group.getGroupID());
		query.setInt(2, group.getSchool().getSchoolID());
		query.setBoolean(3, true);
		query.setTimestamp(4, lastLogin);

		return query.executeQuery();

	}
	
	public String getAdditionalWhereStatement() {
	
		return additionalWhereStatement;
	}

	
	public void setAdditionalWhereStatement(String additionalWhereStatement) {
	
		this.additionalWhereStatement = additionalWhereStatement;
	}
}
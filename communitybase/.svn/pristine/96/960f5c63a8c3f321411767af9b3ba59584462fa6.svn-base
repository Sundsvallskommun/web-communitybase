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

	public EventDAO(DataSource dataSource, String tableName, String idColumn, String descriptionColumn, String dateColumn) {
		super(dataSource);

		this.tableName = tableName;
		this.idColumn = idColumn;
		this.descriptionColumn = descriptionColumn;
		this.dateColumn = dateColumn;

	}

	public List<IdentifiedEvent> getEvents(CommunityGroup group, Timestamp lastLogin) throws SQLException {

		String sql = "SELECT DISTINCT o." + idColumn + " as id, o." + descriptionColumn + " as description, o." + descriptionColumn + ", o." + dateColumn + " as added FROM " + tableName + " as o " +
				"LEFT OUTER JOIN group" + tableName + " as go ON o." + idColumn + " = go." + idColumn + " " +
				"LEFT OUTER JOIN school" + tableName + " as so ON o." + idColumn + " = so." + idColumn + " " +
				" WHERE (go.groupID = ? OR so.schoolID = ? OR o.global = ?) AND o." + dateColumn + " > ? ORDER BY " + dateColumn;

		ArrayListQuery<IdentifiedEvent> query = new ArrayListQuery<IdentifiedEvent>(this.dataSource, true, sql, EVENT_POPULATOR);

		query.setInt(1, group.getGroupID());
		query.setInt(2, group.getSchool().getSchoolID());
		query.setBoolean(3, true);
		query.setTimestamp(4, lastLogin);

		return query.executeQuery();

	}

}

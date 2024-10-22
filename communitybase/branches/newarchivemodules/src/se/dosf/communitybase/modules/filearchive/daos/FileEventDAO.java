package se.dosf.communitybase.modules.filearchive.daos;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.interfaces.GroupAliasProvider;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.populators.annotated.AnnotatedResultSetPopulator;
import se.unlogic.standardutils.string.StringUtils;


public class FileEventDAO extends BaseDAO {

	private static final AnnotatedResultSetPopulator<IdentifiedEvent> EVENT_POPULATOR = new AnnotatedResultSetPopulator<IdentifiedEvent>(IdentifiedEvent.class);
	private static final String SQL = "SELECT DISTINCT o.fileID as id, o.description as description, o.filename as title, o.posted as added " + "FROM filearchive_files as o " + "JOIN filearchive_sections as b ON o.sectionID = b.sectionID " + "LEFT OUTER JOIN filearchive_sectiongroups as go ON o.sectionID = go.sectionID " + "LEFT OUTER JOIN filearchive_sectionschools as so ON o.sectionID = so.sectionID " + " WHERE (go.groupID = ? OR so.schoolID = ? OR b.global = ?) AND o.posted > ? ORDER BY o.posted";

	private GroupAliasProvider groupAliasProvider;

	public FileEventDAO(DataSource dataSource, GroupAliasProvider groupAliasProvider) {

		super(dataSource);

		this.groupAliasProvider = groupAliasProvider;
	}

	public List<IdentifiedEvent> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp, String newFileText) throws Exception {

		ArrayListQuery<IdentifiedEvent> query = new ArrayListQuery<IdentifiedEvent>(this.dataSource, SQL, EVENT_POPULATOR);

		query.setInt(1, group.getGroupID());
		query.setInt(2, group.getSchool().getSchoolID());
		query.setBoolean(3, true);
		query.setTimestamp(4, startStamp);

		ArrayList<IdentifiedEvent> fileEvents = query.executeQuery();

		if(fileEvents != null){
			for(IdentifiedEvent event : fileEvents){
				event.setTitle(newFileText + StringUtils.substring(event.getTitle(), 50, "..."));
				event.setDescription(event.getTitle());
				event.setFullAlias(groupAliasProvider.getFullAlias(group));
			}
		}

		return fileEvents;
	}
}

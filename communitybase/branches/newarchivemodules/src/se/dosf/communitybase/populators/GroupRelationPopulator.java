package se.dosf.communitybase.populators;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map.Entry;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.GroupRelation;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;
import se.unlogic.standardutils.datatypes.SimpleEntry;

public class GroupRelationPopulator implements BeanResultSetPopulator<Entry<CommunityGroup, GroupRelation>> {

	private static final CommunityGroupPopulator COMMUNITY_GROUP_POPULATOR = new CommunityGroupPopulator();

	@Override
	public Entry<CommunityGroup, GroupRelation> populate(ResultSet rs) throws SQLException {

		CommunityGroup group = COMMUNITY_GROUP_POPULATOR.populate(rs);

		GroupRelation groupRelation = new GroupRelation();

		groupRelation.setAccessLevel(GroupAccessLevel.valueOf(rs.getString("accessLevel")));
		groupRelation.setComment(rs.getString("comment"));

		return new SimpleEntry<CommunityGroup, GroupRelation>(group, groupRelation);
	}
}

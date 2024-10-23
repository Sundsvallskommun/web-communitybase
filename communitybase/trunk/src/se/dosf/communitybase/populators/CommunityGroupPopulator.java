package se.dosf.communitybase.populators;

import java.sql.ResultSet;
import java.sql.SQLException;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.School;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;

public class CommunityGroupPopulator implements BeanResultSetPopulator<CommunityGroup> {

	public CommunityGroup populate(ResultSet rs) throws SQLException {

		CommunityGroup group = new CommunityGroup();

		group.setGroupID(rs.getInt("groupID"));
		group.setName(rs.getString("name"));
		group.setEmail(rs.getString("email"));
		
		if(rs.getMetaData().getColumnCount() >= 5){
			group.setSchool(new School(rs.getInt("schoolID"), rs.getString("schoolname")));
		}

		return group;
	}

}

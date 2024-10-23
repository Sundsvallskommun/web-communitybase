package se.dosf.communitybase.populators;

import java.sql.ResultSet;
import java.sql.SQLException;

import se.dosf.communitybase.beans.School;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;

public class CommunitySchoolPopulator implements BeanResultSetPopulator<School> {

	@Override
	public School populate(ResultSet rs) throws SQLException {

		return new School(rs.getInt("schoolID"), rs.getString("name"));
	}
}

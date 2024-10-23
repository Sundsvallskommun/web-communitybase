package se.dosf.communitybase.populators;

import java.sql.SQLException;

import se.dosf.communitybase.beans.School;
import se.unlogic.standardutils.dao.querys.PreparedStatementQuery;
import se.unlogic.standardutils.populators.QueryParameterPopulator;

public class SchoolQueryPopulator implements QueryParameterPopulator<School> {

	public static final SchoolQueryPopulator POPULATOR = new SchoolQueryPopulator();

	public Class<School> getType() {

		return School.class;
	}

	public void populate(PreparedStatementQuery query, int paramIndex, Object bean) throws SQLException {

		if(bean == null){

			query.setObject(paramIndex, null);

		}else{

			School school = (School) bean;

			query.setInt(paramIndex, school.getSchoolID());
		}
	}
	
}

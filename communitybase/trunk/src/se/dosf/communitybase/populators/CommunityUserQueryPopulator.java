package se.dosf.communitybase.populators;

import java.sql.SQLException;

import se.dosf.communitybase.beans.CommunityUser;
import se.unlogic.standardutils.dao.querys.PreparedStatementQuery;
import se.unlogic.standardutils.populators.QueryParameterPopulator;

public class CommunityUserQueryPopulator implements QueryParameterPopulator<CommunityUser> {

	public static final CommunityUserQueryPopulator POPULATOR = new CommunityUserQueryPopulator();

	public Class<? extends CommunityUser> getType() {

		return CommunityUser.class;
	}

	public void populate(PreparedStatementQuery query, int paramIndex, Object bean) throws SQLException {

		if(bean == null){

			query.setObject(paramIndex, null);

		}else{

			CommunityUser user = (CommunityUser)bean;

			query.setInt(paramIndex, user.getUserID());
		}
	}
	
}

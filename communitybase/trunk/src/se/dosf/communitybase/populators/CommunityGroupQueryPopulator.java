package se.dosf.communitybase.populators;

import java.sql.SQLException;

import se.dosf.communitybase.beans.CommunityGroup;
import se.unlogic.standardutils.dao.querys.PreparedStatementQuery;
import se.unlogic.standardutils.populators.QueryParameterPopulator;

public class CommunityGroupQueryPopulator implements QueryParameterPopulator<CommunityGroup> {

	public static final CommunityGroupQueryPopulator POPULATOR = new CommunityGroupQueryPopulator();

	public Class<CommunityGroup> getType() {

		return CommunityGroup.class;
	}

	public void populate(PreparedStatementQuery query, int paramIndex, Object bean) throws SQLException {

		if(bean == null){

			query.setObject(paramIndex, null);

		}else{

			CommunityGroup group = (CommunityGroup)bean;

			query.setInt(paramIndex, group.getGroupID());
		}
	}
	
}

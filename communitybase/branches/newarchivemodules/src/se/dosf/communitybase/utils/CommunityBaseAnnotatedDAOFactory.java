package se.dosf.communitybase.utils;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.populators.CommunityGroupQueryPopulator;
import se.dosf.communitybase.populators.CommunityGroupTypePopulator;
import se.dosf.communitybase.populators.CommunityUserQueryPopulator;
import se.dosf.communitybase.populators.CommunityUserTypePopulator;
import se.dosf.communitybase.populators.SchoolQueryPopulator;
import se.dosf.communitybase.populators.SchoolTypePopulator;
import se.unlogic.hierarchy.core.handlers.GroupHandler;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.populators.BeanStringPopulator;
import se.unlogic.standardutils.populators.QueryParameterPopulator;

public class CommunityBaseAnnotatedDAOFactory extends SimpleAnnotatedDAOFactory {

	public CommunityBaseAnnotatedDAOFactory(DataSource dataSource, UserHandler userHandler, GroupHandler groupHandler) {
		
		this(dataSource, userHandler, groupHandler, null);
		
	}
	
	public CommunityBaseAnnotatedDAOFactory(DataSource dataSource, UserHandler userHandler, GroupHandler groupHandler, CommunitySchoolDAO schoolDAO){

		super(dataSource);

		List<QueryParameterPopulator<?>> queryParameterPopulators = new ArrayList<QueryParameterPopulator<?>>();

		List<BeanStringPopulator<?>> typePopulators = new ArrayList<BeanStringPopulator<?>>();
		
		queryParameterPopulators.add(CommunityUserQueryPopulator.POPULATOR);
		queryParameterPopulators.add(CommunityGroupQueryPopulator.POPULATOR);

		typePopulators.add((new CommunityUserTypePopulator(userHandler, false)));
		typePopulators.add(new CommunityGroupTypePopulator(groupHandler));
		
		
		if(schoolDAO != null) {
			queryParameterPopulators.add(SchoolQueryPopulator.POPULATOR);
			typePopulators.add(new SchoolTypePopulator(schoolDAO, false, false));
		}
		
		this.queryParameterPopulators = queryParameterPopulators;
		this.beanStringPopulators = typePopulators;
		
	}
	
}

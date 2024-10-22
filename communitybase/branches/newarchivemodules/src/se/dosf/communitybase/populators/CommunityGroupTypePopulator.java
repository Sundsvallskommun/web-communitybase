package se.dosf.communitybase.populators;

import se.dosf.communitybase.beans.CommunityGroup;
import se.unlogic.hierarchy.core.handlers.GroupHandler;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.BeanStringPopulator;

public class CommunityGroupTypePopulator implements BeanStringPopulator<CommunityGroup> {

	private final GroupHandler groupHandler;
	
	public CommunityGroupTypePopulator(GroupHandler groupHandler) {
		this.groupHandler = groupHandler;
	}
	
	@Override
	public String getPopulatorID() {

		return null;
	}

	@Override
	public Class<? extends CommunityGroup> getType() {
		return CommunityGroup.class;
	}

	@Override
	public CommunityGroup getValue(String value) {
		return (CommunityGroup) this.groupHandler.getGroup(Integer.parseInt(value), false);
	}

	@Override
	public boolean validateFormat(String value) {
		return NumberUtils.isInt(value);
	}

}

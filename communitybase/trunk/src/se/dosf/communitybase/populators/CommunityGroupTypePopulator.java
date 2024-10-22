package se.dosf.communitybase.populators;

import se.dosf.communitybase.beans.CommunityGroup;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.BeanStringPopulator;

public class CommunityGroupTypePopulator implements BeanStringPopulator<CommunityGroup> {

	private final UserHandler userHandler;
	
	public CommunityGroupTypePopulator(UserHandler userHandler) {
		this.userHandler = userHandler;
	}
	
	public String getPopulatorID() {

		return null;
	}

	public Class<? extends CommunityGroup> getType() {
		return CommunityGroup.class;
	}

	public CommunityGroup getValue(String value) {
		return (CommunityGroup) this.userHandler.getGroup(Integer.parseInt(value));
	}

	public boolean validateFormat(String value) {
		return NumberUtils.isInt(value);
	}

}

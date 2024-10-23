package se.dosf.communitybase.populators;

import se.dosf.communitybase.beans.CommunityUser;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.BeanStringPopulator;

public class CommunityUserTypePopulator implements BeanStringPopulator<CommunityUser> {

	private final UserHandler userHandler;
	private boolean getGroups;
		
	public CommunityUserTypePopulator(UserHandler userHandler, boolean getGroups) {
		this.userHandler = userHandler;
		this.getGroups = getGroups;
	}
	
	public boolean isGetGroups() {
		return getGroups;
	}

	public void setGetGroups(boolean getGroups) {
		this.getGroups = getGroups;
	}

	@Override
	public String getPopulatorID() {

		return null;
	}

	@Override
	public Class<? extends CommunityUser> getType() {
		return CommunityUser.class;
	}

	@Override
	public CommunityUser getValue(String value) {
		return (CommunityUser) this.userHandler.getUser(Integer.parseInt(value), this.getGroups, false);
	}

	@Override
	public boolean validateFormat(String value) {
		return NumberUtils.isInt(value);
	}

}

package se.dosf.communitybase.interfaces;

import se.dosf.communitybase.beans.CommunityGroup;


public interface GroupAliasProvider {

	String getFullAlias(CommunityGroup group);

}

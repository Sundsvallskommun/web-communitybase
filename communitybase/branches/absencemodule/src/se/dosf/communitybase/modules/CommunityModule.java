package se.dosf.communitybase.modules;

import java.sql.Timestamp;
import java.util.List;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.enums.ModuleType;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;

public interface CommunityModule {

	// TODO get module information etc..
	List<? extends Event> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception;

	String getFullAlias(CommunityGroup group);

	ForegroundModuleDescriptor getModuleDescriptor();

	ModuleType getModuleType();
}

package se.dosf.communitybase.modules;

import java.sql.Timestamp;
import java.util.List;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.enums.ModuleType;
import se.dosf.communitybase.interfaces.GroupAliasProvider;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;

public interface CommunityModule extends GroupAliasProvider {

	List<? extends Event> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception;

	@Override
	String getFullAlias(CommunityGroup group);

	ForegroundModuleDescriptor getModuleDescriptor();

	ModuleType getModuleType();
}

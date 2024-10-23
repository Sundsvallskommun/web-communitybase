package se.dosf.communitybase.modules.absence.cruds;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.enums.ModuleType;
import se.dosf.communitybase.modules.absence.beans.Absence;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.utils.CRUDCallback;

public interface AbsenceCRUDCallback extends CRUDCallback<CommunityUser> {

	public Integer getLastReportHour();
	
	public Breadcrumb getModuleBreadcrumb(CommunityGroup group);

	public Breadcrumb getGroupBreadcrumb(CommunityGroup group);
	
	public Breadcrumb getShowBreadCrumb(CommunityGroup group, Absence absence);
	
	public Breadcrumb getAddBreadCrumb(CommunityGroup group);

	public Breadcrumb getUpdateBreadCrumb(CommunityGroup group, Absence absence);

	public ForegroundModuleDescriptor getModuleDescriptor();
	
	public SectionDescriptor getSectionDescriptor();
	
	public boolean hasAccess(Absence absence, CommunityUser user);

	public ModuleType getModuleType();
	
}

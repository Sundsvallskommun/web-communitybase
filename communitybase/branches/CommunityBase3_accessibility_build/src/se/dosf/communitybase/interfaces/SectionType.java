package se.dosf.communitybase.interfaces;

import java.util.List;

import se.dosf.communitybase.beans.AddSectionType;
import se.dosf.communitybase.enums.DeleteDateMode;
import se.unlogic.standardutils.xml.Elementable;


public interface SectionType extends Elementable{

	public Integer getSectionTypeID();

	public String getName();
	
	public DeleteDateMode getDeleteDateMode();

	public List<? extends Role> getAllowedRoles();

	public Integer getCreatorRoleID();
	
	public Integer getDefaultRoleID();

	public List<? extends ForegroundModuleConfiguration> getSupportedForegroundModules();

	public List<? extends ModuleConfiguration> getSupportedBackgroundModules();

	public List<AddSectionType> getAddSectionTypes();
	
	public List<Integer> getAdminGroupIDs();
}
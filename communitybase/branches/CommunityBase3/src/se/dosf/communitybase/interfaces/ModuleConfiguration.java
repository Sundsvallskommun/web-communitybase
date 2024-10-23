package se.dosf.communitybase.interfaces;

import se.dosf.communitybase.beans.SimpleSectionType;
import se.dosf.communitybase.enums.ModuleAccessMode;
import se.dosf.communitybase.enums.ModuleManagementMode;
import se.unlogic.standardutils.xml.Elementable;

public interface ModuleConfiguration extends Elementable{

	public SimpleSectionType getSectionType();

	public Integer getModuleID();

	public boolean isAutoEnable();

	public ModuleManagementMode getManagementMode();

	public ModuleAccessMode getAccessMode();

}
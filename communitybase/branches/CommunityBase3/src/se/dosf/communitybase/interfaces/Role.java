package se.dosf.communitybase.interfaces;

import java.util.List;

import se.unlogic.standardutils.xml.Elementable;


public interface Role extends Elementable {

	public boolean hasManageSectionAccessModeAccess();

	public boolean hasManageModulesAccess();

	public boolean hasManageMembersAccess();

	public boolean hasDeleteOtherContentAccess();

	public boolean hasUpdateOtherContentAccess();

	public boolean hasDeleteOwnContentAccess();

	public boolean hasUpdateOwnContentAccess();

	public boolean hasAddContentAccess();
	
	public boolean hasManageArchivedAccess();
	
	public boolean hasDeleteRoomAccess();

	public String getName();

	public Integer getRoleID();

	public List<? extends SectionType> getSectionTypes();
}

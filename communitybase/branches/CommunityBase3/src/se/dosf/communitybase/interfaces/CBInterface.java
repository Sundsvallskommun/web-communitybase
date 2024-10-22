package se.dosf.communitybase.interfaces;

import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

import se.dosf.communitybase.beans.NewSectionValues;
import se.dosf.communitybase.beans.SectionAccessRequest;
import se.dosf.communitybase.beans.SectionGroupRole;
import se.dosf.communitybase.beans.SimpleSectionType;
import se.dosf.communitybase.enums.SectionAccessMode;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleSectionDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.UnableToUpdateGroupException;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;


public interface CBInterface {

	public Role getRole(Integer sectionID, User user);

	public boolean setUserRole(Integer userID, Integer sectionID, Integer roleID);

	public boolean setUserRole(User user, Integer sectionID, Integer roleID);

	public boolean removeUser(User user, Integer sectionID);

	public boolean removeUser(Integer userID, Integer sectionID);
	
	public boolean setGroupRole(Integer groupID, Integer sectionID, Integer roleID);
	
	public boolean removeGroup(Integer groupID, Integer sectionID);

	public Role getSectionRole(Integer sectionID, Integer roleID);

	public Group getGroup(Integer roleID, Integer sectionID, boolean attributes);

	public List<Role> getRoles(Integer sectionID);

	public List<SectionType> getAvailableSectionTypes(User user);

	public List<Integer> getSectionMembers(Integer sectionID, boolean includeGroupMembers);
	
	public List<SectionGroupRole> getSectionGroups(Integer sectionID);
	
	public List<Integer> getGroupSections(Integer groupID);

	public SectionInterface addSection(NewSectionValues descriptor, User creator) throws Exception;

	public File getSectionLogo(int sectionID);

	public void deleteSectionLogo(Integer sectionID);

	public void setSectionLogo(Integer sectionID, BufferedImage profileImage);

	public List<? extends ForegroundModuleConfiguration> getSupportedForegroundModules(Integer sectionID);

	public SimpleForegroundModuleDescriptor addForegroundModule(int sectionID, int sectionTypeID, ForegroundModuleConfiguration moduleConfiguration) throws SQLException;

	public boolean sortMenu(SectionInterface sectionInterface) throws SQLException;

	public void setSectionAccess(SimpleSectionDescriptor sectionDescriptor, SectionAccessMode accessMode, SectionType sectionType);

	public boolean isGlobalAdmin(User user);

	public SimpleSectionType getSectionType(Integer sectionTypeID) throws SQLException;

	public void setSectionName(SimpleSectionDescriptor sectionDescriptor, String name) throws SQLException, UnableToUpdateGroupException;

	public List<Group> getRoleGroups(Integer sectionID, boolean attributes);
	
	public boolean isEnableSecureSections();
	
	public String getSecureSectionsWarning();

	public SectionAccessRequest getAccessRequest(User user, Integer sectionID) throws SQLException;

	public void addAccessRequest(User user, Integer sectionID, String comment) throws SQLException;

	public void deleteAccessRequest(User user, Integer sectionID) throws SQLException;

	public List<SectionAccessRequest> getAccessRequests(Integer sectionID) throws SQLException;
	
	public boolean isEnableSectionStatistics();
}
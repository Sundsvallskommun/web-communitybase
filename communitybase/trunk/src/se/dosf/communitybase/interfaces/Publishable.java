package se.dosf.communitybase.interfaces;

import java.util.List;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.School;


public interface Publishable {

	public void setGroups(List<CommunityGroup> groups);
	
	public List<CommunityGroup> getGroups();
	
	public void setSchools(List<School> schools);
	
	public List<School> getSchools();
	
	public void setGlobal(boolean global);
	
	public boolean isGlobal();
	
}

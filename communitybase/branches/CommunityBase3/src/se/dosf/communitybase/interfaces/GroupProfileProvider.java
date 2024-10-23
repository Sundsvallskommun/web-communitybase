package se.dosf.communitybase.interfaces;

import se.unlogic.hierarchy.core.beans.User;


public interface GroupProfileProvider {
	
	public String getFullAlias();
	
	public boolean checkAccess(User user);
}
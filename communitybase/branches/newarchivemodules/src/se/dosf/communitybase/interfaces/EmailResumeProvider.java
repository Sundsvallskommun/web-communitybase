package se.dosf.communitybase.interfaces;

import se.dosf.communitybase.beans.CommunityUser;

public interface EmailResumeProvider {

	public String getGlobalEmailResumeHTML(CommunityUser user) throws Exception;
	
}

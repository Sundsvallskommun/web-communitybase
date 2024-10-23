package se.dosf.communitybase.modules.oldcontentremover.interfaces;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.modules.oldcontentremover.beans.OldContent;


public interface OldContentRemover {

	public int getOldContentCount(CommunityUser user, CommunityGroup group, Date endDate) throws SQLException;

	public Collection<OldContent> getOldContent(CommunityUser user, CommunityGroup group, Date endDate) throws SQLException;

	public int deleteOldContent(Collection<OldContent> oldContents, CommunityUser user, CommunityGroup group) throws SQLException;

}

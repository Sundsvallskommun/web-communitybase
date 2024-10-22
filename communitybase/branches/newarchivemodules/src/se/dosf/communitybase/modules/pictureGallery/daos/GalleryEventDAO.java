package se.dosf.communitybase.modules.pictureGallery.daos;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.daos.EventDAO;
import se.dosf.communitybase.interfaces.GroupAliasProvider;
import se.dosf.communitybase.utils.EventComparator;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.db.DBUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.populators.TimeStampPopulator;
import se.unlogic.standardutils.string.StringUtils;

public class GalleryEventDAO extends BaseDAO {

	private static final String GROUP_GALLERIES_SQL = "SELECT DISTINCT o.galleryID as id FROM picturegallery_galleries as o LEFT OUTER JOIN picturegallery_gallerygroups as go ON o.galleryID = go.galleryID LEFT OUTER JOIN picturegallery_galleryschools as so ON o.galleryID = so.galleryID WHERE (go.groupID = ? OR so.schoolID = ? OR o.global = true) ORDER BY o.name;";
	private static final String UPDATED_GALLERIES = "SELECT DISTINCT galleryID FROM picturegallery_pictures WHERE galleryID IN (replace) AND posted > ?";

	private GroupAliasProvider groupAliasProvider;
	private EventDAO eventDAO;

	public GalleryEventDAO(DataSource dataSource, GroupAliasProvider groupAliasProvider) {

		super(dataSource);

		this.groupAliasProvider = groupAliasProvider;

		this.eventDAO = new EventDAO(dataSource, "picturegallery_galleries", "picturegallery_gallerygroups", "picturegallery_galleryschools", "galleryID", "name", "posted");
	}

	public List<IdentifiedEvent> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp, String newGalleryText, String newPicturesInGalleryText) throws SQLException  {

		// Get new albums
		List<IdentifiedEvent> events = this.eventDAO.getEvents(group, startStamp);

		if(events != null){
			for(IdentifiedEvent event : events){
				event.setTitle(newGalleryText + StringUtils.substring(event.getTitle(), 50, "..."));
				event.setDescription(event.getTitle());
				event.setFullAlias(groupAliasProvider.getFullAlias(group) + "/showGallery/" + event.getId());
			}
		}

		//Rewritten SQL and query structure since the old one could take up to 10 seconds to execute on large database and included dupes

		Connection connection = null;

		try{
			connection = dataSource.getConnection();

			//Get the ID's of all galleries that this group has access to
			ArrayListQuery<Integer> galleryIDsQuery = new ArrayListQuery<Integer>(connection, false, GROUP_GALLERIES_SQL, IntegerPopulator.getPopulator());

			galleryIDsQuery.setInt(1, group.getGroupID());
			galleryIDsQuery.setInt(2, group.getSchool().getSchoolID());

			List<Integer> groupGalleryIDs = galleryIDsQuery.executeQuery();

			if(groupGalleryIDs == null){

				return events;
			}

			//Check for new pictures in the galleries
			ArrayListQuery<Integer> updatedGalleryIDsQuery = new ArrayListQuery<Integer>(connection, false, UPDATED_GALLERIES.replace("replace", StringUtils.toCommaSeparatedString(groupGalleryIDs)), IntegerPopulator.getPopulator());

			updatedGalleryIDsQuery.setTimestamp(1, startStamp);

			List<Integer> updatedGroupGalleryIDs = updatedGalleryIDsQuery.executeQuery();

			if(updatedGroupGalleryIDs == null){

				return events;
			}

			//Remove dupes in the form of galleries which are both new and have new content
			if(events != null){

				Iterator<IdentifiedEvent> iterator = events.iterator();

				while(iterator.hasNext()){

					IdentifiedEvent event = iterator.next();

					updatedGroupGalleryIDs.remove(event.getId());
				}
			}

			boolean needsSorting = true;

			if(updatedGroupGalleryIDs.isEmpty()){

				return events;

			}else if(events == null){

				events = new ArrayList<IdentifiedEvent>(updatedGroupGalleryIDs.size());
				needsSorting = false;
			}

			for(Integer galleryID : updatedGroupGalleryIDs){

				String galleryName = getGalleryName(connection, galleryID);

				//Check if the gallery has been deleted since this chain of queries was started
				if(galleryName == null){
					continue;
				}

				Timestamp latestPicture = getLatestPictureTimestamp(connection, galleryID);

				//Check if there still are any pictures in the album
				if(latestPicture == null){

					continue;
				}

				IdentifiedEvent event = new IdentifiedEvent();

				event.setId(galleryID);
				event.setTitle(newPicturesInGalleryText +  StringUtils.substring(galleryName, 50, "..."));
				event.setDescription(event.getTitle());
				event.setFullAlias(groupAliasProvider.getFullAlias(group) + "/showGallery/" + event.getId());
				event.setAdded(latestPicture);
				events.add(event);
			}

			if(events.isEmpty()){

				return null;

			}else if(needsSorting){

				Collections.sort(events, EventComparator.getComparator());
			}

			return events;

		}finally{
			DBUtils.closeConnection(connection);
		}

	}

	private Timestamp getLatestPictureTimestamp(Connection connection, Integer galleryID) throws SQLException {

		return new ObjectQuery<Timestamp>(connection, false, "SELECT max(posted) FROM picturegallery_pictures WHERE galleryID = " + galleryID, TimeStampPopulator.getPopulator()).executeQuery();
	}

	private String getGalleryName(Connection connection, Integer galleryID) throws SQLException {

		return new ObjectQuery<String>(connection, false, "SELECT name FROM picturegallery_galleries WHERE galleryID = " + galleryID, StringPopulator.getPopulator()).executeQuery();
	}
}

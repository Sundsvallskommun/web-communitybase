package se.dosf.communitybase.modules.pictureGallery.daos;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.enums.RelationType;
import se.dosf.communitybase.modules.pictureGallery.beans.Gallery;
import se.dosf.communitybase.modules.pictureGallery.populators.PictureGalleryPopulator;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.IntegerKeyCollector;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;
import se.unlogic.standardutils.populators.annotated.AnnotatedResultSetPopulator;

public class PictureGalleryModuleDAO extends BaseDAO{

	private static final AnnotatedResultSetPopulator<IdentifiedEvent> EVENT_POPULATOR = new AnnotatedResultSetPopulator<IdentifiedEvent>(IdentifiedEvent.class);
	private final PictureGalleryPopulator GROUPGALLERYPOPULATOR = new PictureGalleryPopulator(RelationType.GROUP);
	private final PictureGalleryPopulator SCHOOLGALLERYPOPULATOR = new PictureGalleryPopulator(RelationType.SCHOOL);

	private final PictureDao pictureDao;

	public PictureGalleryModuleDAO(DataSource ds) {
		super(ds);

		this.pictureDao = new PictureDao(ds);
	}

	public ArrayList<Event> getPictureGalleryResume(CommunityGroup group, CommunityUser user) throws SQLException {

		ArrayList<Event> galleryResume = new ArrayList<Event>();

		String queryString = "SELECT DISTINCT (picturegallerygroupsections.sectionID), " +
				"picturegallerygroupsections.name, " +
				"picturegallerygrouppictures.posted " +
				"FROM picturegallerygrouppictures, picturegallerygroupsections " +
				"WHERE picturegallerygroupsections.groupID = ? " +
				"AND picturegallerygrouppictures.posted > ? " +
				"AND picturegallerygrouppictures.sectionID = picturegallerygroupsections.sectionID " +
				"ORDER BY picturegallerygroupsections.name;";

		ArrayListQuery<Event> query = new ArrayListQuery<Event>(this.dataSource.getConnection(), true, queryString, null);

		query.setInt(1, group.getGroupID());
		query.setTimestamp(2, user.getLastLogin());

		ArrayList<Event> groupGalleryResume = query.executeQuery();

		if(groupGalleryResume != null){
			galleryResume.addAll(groupGalleryResume);
		}

		String queryString2 = "SELECT DISTINCT(picturegalleryschoolsections.sectionID), " +
				"picturegalleryschoolsections.name, " +
				"picturegalleryschoolpictures.posted " +
				"FROM picturegalleryschoolpictures, picturegalleryschoolsections " +
				"WHERE picturegalleryschoolsections.schoolID = ? " +
				"AND picturegalleryschoolpictures.posted > ? " +
				"AND picturegalleryschoolpictures.sectionID = picturegalleryschoolsections.sectionID " +
				"ORDER BY picturegalleryschoolsections.name;";

		query = new ArrayListQuery<Event>(this.dataSource.getConnection(), true, queryString2, null);

		query.setInt(1, group.getSchool().getSchoolID());
		query.setTimestamp(2, user.getLastLogin());

		ArrayList<Event> schoolGalleryResume = query.executeQuery();

		if(schoolGalleryResume != null){
			galleryResume.addAll(schoolGalleryResume);
		}

		return galleryResume;

	}

	public Gallery getGallery(Integer sectionID, boolean pictures, RelationType type) throws SQLException{

		ObjectQuery<Gallery> query = new ObjectQuery<Gallery>(this.dataSource.getConnection(), true, "SELECT * FROM " + this.getRelationTableName(type) + " WHERE sectionID = ?", this.getPopulator(type));

		query.setInt(1, sectionID);

		Gallery gallery = query.executeQuery();

		if(gallery != null && pictures){
			gallery.setPictures(this.pictureDao.findByGalleryID(gallery.getSectionID(), false, false, false, type));
		}

		return gallery;
	}

	public ArrayList<Gallery> getAllGalleries(CommunityGroup group, boolean pictures) throws SQLException{

		ArrayList<Gallery> galleries = new ArrayList<Gallery>();

		ArrayList<Gallery> groupGalleries = this.getGalleries(group, pictures, RelationType.GROUP);

		if(groupGalleries != null){
			galleries.addAll(groupGalleries);
		}

		ArrayList<Gallery> schoolGalleries = this.getGalleries(group, pictures, RelationType.SCHOOL);

		if(schoolGalleries != null){
			galleries.addAll(schoolGalleries);
		}

		return galleries;
	}

	public ArrayList<Gallery> getGalleries(CommunityGroup group, boolean pictures, RelationType type) throws SQLException{

		ArrayListQuery<Gallery> query = new ArrayListQuery<Gallery>(this.dataSource.getConnection(), true, "SELECT * FROM " + this.getRelationTableName(type) + " WHERE " + this.getRelationColumnName(type) + " = ? ORDER BY name", this.getPopulator(type));

		if(type.equals(RelationType.GROUP)){
			query.setInt(1, group.getGroupID());
		}else{
			query.setInt(1, group.getSchool().getSchoolID());
		}

		ArrayList<Gallery> galleries = query.executeQuery();

		if(pictures && galleries != null && !galleries.isEmpty()){
			for(Gallery gallery : galleries){
				gallery.setPictures(this.pictureDao.findByGalleryID(gallery.getSectionID(), false, false, false, type));
			}
		}

		return galleries;

	}


	public Integer add(Gallery gallery) throws SQLException {

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(),true, "INSERT INTO " + this.getRelationTableName(gallery.getRelationType()) + " VALUES (null,?,?,?,?)");

		query.setInt(1, gallery.getRelationID());
		query.setString(2, gallery.getSectionName());
		query.setString(3, gallery.getDescription());
		query.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

		IntegerKeyCollector keyCollector = new IntegerKeyCollector();

		query.executeUpdate(keyCollector);

		return keyCollector.getKeyValue();

	}

	public void update(Gallery gallery) throws SQLException {

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, "UPDATE " + this.getRelationTableName(gallery.getRelationType()) + " SET name = ?, description = ? WHERE sectionID = ?");

		query.setString(1, gallery.getSectionName());
		query.setString(2, gallery.getDescription());
		query.setInt(3, gallery.getSectionID());

		query.executeUpdate();

	}

	public void delete(Gallery gallery) throws SQLException{

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, "DELETE FROM " + this.getRelationTableName(gallery.getRelationType()) + " WHERE sectionID = ?");

		query.setInt(1, gallery.getSectionID());

		query.executeUpdate();

	}

	private String getRelationTableName(RelationType type){

		return type.equals(RelationType.GROUP) ? "picturegallerygroupsections" : "picturegalleryschoolsections";

	}

	private String getRelationColumnName(RelationType type){

		return type.equals(RelationType.GROUP) ? "groupID" : "schoolID";

	}

	private PictureGalleryPopulator getPopulator(RelationType type){
		return type.equals(RelationType.GROUP) ? this.GROUPGALLERYPOPULATOR : this.SCHOOLGALLERYPOPULATOR;
	}

	public List<IdentifiedEvent> getEvents(Timestamp lastLogin, String tableName, String idName, int id) throws SQLException {

		ArrayListQuery<IdentifiedEvent> query = new ArrayListQuery<IdentifiedEvent>(dataSource, true, "SELECT sectionID as id, name as title, description, posted as added FROM " + tableName + " WHERE " + idName + " = ? AND posted > ? ORDER by posted", EVENT_POPULATOR);

		query.setInt(1, id);
		query.setTimestamp(2, lastLogin);

		return query.executeQuery();
	}

}

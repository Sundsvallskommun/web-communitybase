package se.dosf.communitybase.modules.pictureGallery.daos;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.sql.DataSource;

import se.dosf.communitybase.enums.RelationType;
import se.dosf.communitybase.modules.pictureGallery.beans.Gallery;
import se.dosf.communitybase.modules.pictureGallery.beans.Picture;
import se.dosf.communitybase.modules.pictureGallery.populators.PicturePopulator;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.IntegerKeyCollector;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;
import se.unlogic.standardutils.populators.IntegerPopulator;

public class PictureDao extends BaseDAO {

	private static PicturePopulator Populator = new PicturePopulator();

	public PictureDao(DataSource ds) {
		super(ds);
	}

	public ArrayList<Picture> findByGalleryID(Integer galleryID, boolean smallThumbs, boolean mediumThumbs, boolean largeThumbs, RelationType type) throws SQLException {

		StringBuilder queryString = new StringBuilder("SELECT fileID, sectionID, description, filename, contentTypeSmall, postedBy, contentTypeMedium, contentTypeLarge, posted");

		if(smallThumbs){
			queryString.append(", picSmall");
		}
		if(mediumThumbs){
			queryString.append(", picMedium");
		}
		if(largeThumbs){
			queryString.append(", picLarge");
		}

		queryString.append(" FROM " + this.getRelationTableName(type) + " WHERE sectionID = ?");

		ArrayListQuery<Picture> query = new ArrayListQuery<Picture>(this.dataSource.getConnection(), true, queryString.toString(), Populator);

		query.setInt(1, galleryID);

		return query.executeQuery();
	}

	public Picture get(Integer fileID, boolean smallThumb, boolean mediumThumb, boolean largeThumb, RelationType type) throws SQLException {

		StringBuilder queryString = new StringBuilder("SELECT fileID, sectionID, description, filename, contentTypeSmall, postedBy, contentTypeMedium, contentTypeLarge, posted");

		if(smallThumb){
			queryString.append(", picSmall");
		}
		if(mediumThumb){
			queryString.append(", picMedium");
		}
		if(largeThumb){
			queryString.append(", picLarge");
		}

		queryString.append(" FROM " + this.getRelationTableName(type) + " WHERE fileID = ?");

		ObjectQuery<Picture> query = new ObjectQuery<Picture>(this.dataSource.getConnection(), true, queryString.toString(), Populator);

		query.setInt(1, fileID);

		return query.executeQuery();
	}

	public Integer getFileIDByFilenameAndGallery(String filename, Gallery gallery, RelationType type) throws SQLException {

		String queryString = "SELECT fileID FROM " + this.getRelationTableName(type) + " WHERE filename = ? AND galleryID = ?";

		ObjectQuery<Integer> query = new ObjectQuery<Integer>(this.dataSource.getConnection(), true, queryString, IntegerPopulator.getPopulator());

		query.setString(1, filename);
		query.setInt(2, gallery.getSectionID());

		return query.executeQuery();
	}

	public Picture getByFilename(String filename, Integer galleryID, boolean smallThumb, boolean mediumThumb, boolean largeThumb, RelationType type) throws SQLException {

		StringBuilder queryString = new StringBuilder("SELECT fileID, sectionID, description, filename, contentTypeSmall, postedBy, contentTypeMedium, contentTypeLarge, posted");

		if(smallThumb){
			queryString.append(", picSmall");
		}
		if(mediumThumb){
			queryString.append(", picMedium");
		}
		if(largeThumb){
			queryString.append(", picLarge");
		}

		queryString.append(" FROM " + this.getRelationTableName(type) + " WHERE filename = ? AND galleryID = ?");

		ObjectQuery<Picture> query = new ObjectQuery<Picture>(this.dataSource.getConnection(), true, queryString.toString(), Populator);

		query.setString(1, filename);
		query.setInt(2, galleryID);

		return query.executeQuery();
	}

	public Integer add(Picture picture, RelationType type) throws SQLException{

		// Insert
		UpdateQuery insertQuery = new UpdateQuery(this.dataSource.getConnection(), true, "INSERT INTO " + this.getRelationTableName(type) + " VALUES (null,?,?,?,?,?,?,?,?,?,?,?)");

		insertQuery.setInt(1, picture.getSectionID());
		insertQuery.setObject(2, picture.getDescription());
		insertQuery.setBlob(3, picture.getSmallPic());
		insertQuery.setBlob(4, picture.getMediumPic());
		insertQuery.setBlob(5, picture.getLargePic());
		insertQuery.setString(6, picture.getFilename());
		insertQuery.setString(7, picture.getContentTypeSmall());
		insertQuery.setInt(8, picture.getPostedBy());
		insertQuery.setString(9, picture.getContentTypeMedium());
		insertQuery.setString(10, picture.getContentTypeLarge());
		insertQuery.setTimestamp(11, new Timestamp(System.currentTimeMillis()));

		IntegerKeyCollector keyCollector = new IntegerKeyCollector();
		
		insertQuery.executeUpdate(keyCollector);
		
		return keyCollector.getKeyValue();

	}

	public void addAll(ArrayList<Picture> pictures, RelationType type) throws SQLException{


		TransactionHandler transactionHandler = new TransactionHandler(this.dataSource);

		try {

			for(Picture picture : pictures){

				// Insert
				UpdateQuery insertQuery = transactionHandler.getUpdateQuery("INSERT INTO " + this.getRelationTableName(type) + " VALUES (null,?,?,?,?,?,?,?,?,?,?,?)");

				insertQuery.setInt(1, picture.getSectionID());
				insertQuery.setObject(2, picture.getDescription());
				insertQuery.setBlob(3, picture.getSmallPic());
				insertQuery.setBlob(4, picture.getMediumPic());
				insertQuery.setBlob(5, picture.getLargePic());
				insertQuery.setString(6, picture.getFilename());
				insertQuery.setString(7, picture.getContentTypeSmall());
				insertQuery.setInt(8, picture.getPostedBy());
				insertQuery.setString(9, picture.getContentTypeMedium());
				insertQuery.setString(10, picture.getContentTypeLarge());
				insertQuery.setTimestamp(11, new Timestamp(System.currentTimeMillis()));

				insertQuery.executeUpdate();

			}

			transactionHandler.commit();

		} finally {
			if (transactionHandler != null && !transactionHandler.isClosed()) {
				transactionHandler.abort();
			}
		}

	}

	public void delete(Picture picture, RelationType type) throws SQLException {

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, "DELETE FROM " + this.getRelationTableName(type) + " WHERE fileID = ?");

		query.setInt(1, picture.getFileID());

		query.executeUpdate();
	}


	private String getRelationTableName(RelationType type){

		return type.equals(RelationType.GROUP) ? "picturegallerygrouppictures" : "picturegalleryschoolpictures";

	}
}

package se.dosf.communitybase.modules.pictureGallery.daos;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.enums.RelationType;
import se.dosf.communitybase.modules.pictureGallery.beans.PictureComment;
import se.dosf.communitybase.modules.pictureGallery.populators.PictureCommentPopulator;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.IntegerKeyCollector;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;

public class PictureCommentDao extends BaseDAO {

	private static PictureCommentPopulator Populator = new PictureCommentPopulator(null);

	public PictureCommentDao(DataSource ds, CommunityUserDAO userDao) {
		super(ds);

		Populator = new PictureCommentPopulator(userDao);
	}

	public ArrayList<PictureComment> findByFileID(Integer fileID, RelationType type) throws SQLException {

		ArrayListQuery<PictureComment> query = new ArrayListQuery<PictureComment>(this.dataSource.getConnection(), true, "SELECT * FROM " + this.getRelationTableName(type) + " WHERE fileID = ? ORDER BY date ASC, commentID ASC", Populator);

		query.setInt(1, fileID);

		return query.executeQuery();
	}

	public PictureComment get(Integer commentID, RelationType type) throws SQLException {

		String queryString = "SELECT * FROM " + this.getRelationTableName(type) +" WHERE commentID = ?";

		ObjectQuery<PictureComment> query = new ObjectQuery<PictureComment>(this.dataSource.getConnection(), true, queryString, Populator);

		query.setInt(1, commentID);

		PictureComment comment = query.executeQuery();

		return comment;
	}

	public ArrayList<PictureComment> getByFileID(Integer fileID, RelationType type) throws SQLException {

		//Integer fileID = pictureDao.getFileIDByFilenameAndGallery(fileID, gallery, type);
		if (fileID != null) {

			ArrayListQuery<PictureComment> query = new ArrayListQuery<PictureComment>(this.dataSource.getConnection(), true, "SELECT * FROM " + this.getRelationTableName(type) +" WHERE fileID = ? ORDER BY date ASC, commentID ASC", Populator);

			query.setInt(1, fileID);

			return query.executeQuery();
		}

		return null;
	}

	public Integer add(PictureComment comment, RelationType type) throws SQLException {

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, "INSERT INTO " + this.getRelationTableName(type) + " VALUES (null,?,?,?,?)");

		query.setInt(1, comment.getFileID());
		query.setString(2, comment.getComment());
		query.setTimestamp(3, comment.getDate());
		if (comment.getUser() != null) {
			query.setInt(4, comment.getUser().getUserID());
		} else {
			query.setObject(4, null);
		}

		IntegerKeyCollector keyCollector = new IntegerKeyCollector();
		
		query.executeUpdate(keyCollector);
		
		return keyCollector.getKeyValue();
	}

	public void delete(PictureComment comment, RelationType type) throws SQLException {

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, "DELETE FROM " + this.getRelationTableName(type) +" WHERE commentID = ?");

		query.setInt(1, comment.getCommentID());

		query.executeUpdate();
	}

	public void update(PictureComment comment, RelationType type) throws SQLException {
		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, "UPDATE " + this.getRelationTableName(type) + " SET comment = ? WHERE commentID = ?");

		query.setString(1, comment.getComment());
		query.setInt(2, comment.getCommentID());

		query.executeUpdate();
	}
	
	private String getRelationTableName(RelationType type){
		
		return type.equals(RelationType.GROUP) ? "grouppicturecomments" : "schoolpicturecomments";
		
	}
}

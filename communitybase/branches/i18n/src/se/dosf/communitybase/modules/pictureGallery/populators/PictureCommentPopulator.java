package se.dosf.communitybase.modules.pictureGallery.populators;

import java.sql.ResultSet;
import java.sql.SQLException;

import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.modules.pictureGallery.beans.PictureComment;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;

public class PictureCommentPopulator implements BeanResultSetPopulator<PictureComment> {

	private final CommunityUserDAO userDao;

	public PictureCommentPopulator(CommunityUserDAO userDao) {
		this.userDao = userDao;
	}

	public PictureComment populate(ResultSet rs) throws SQLException {

		PictureComment comment = new PictureComment();

		comment.setCommentID(rs.getInt("commentID"));
		comment.setFileID(rs.getInt("fileID"));
		comment.setComment(rs.getString("comment"));
		comment.setDate(rs.getTimestamp("date"));

		Integer userID = rs.getInt("userID");

		if (userID != null) {
			comment.setUser(userDao.findByUserID(userID, false,false));
		}

		return comment;
	}
}

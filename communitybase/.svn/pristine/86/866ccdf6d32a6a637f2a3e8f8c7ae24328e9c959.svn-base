package se.dosf.communitybase.modules.pictureGallery.daos;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.modules.pictureGallery.beans.Gallery;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOFactory;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.db.DBUtils;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.populators.BeanStringPopulator;
import se.unlogic.standardutils.populators.QueryParameterPopulator;
import se.unlogic.standardutils.populators.TimeStampPopulator;


public class GalleryDAO extends AnnotatedDAO<Gallery> {

	private final String getGroupGalleriesSQL;

	public GalleryDAO(DataSource dataSource, AnnotatedDAOFactory daoFactory, List<? extends QueryParameterPopulator<?>> queryParameterPopulators, List<? extends BeanStringPopulator<?>> typePopulators) {

		super(dataSource, Gallery.class, daoFactory, queryParameterPopulators, typePopulators);

		getGroupGalleriesSQL = "SELECT DISTINCT n.* " +
				"FROM " + getTableName() + " as n " +
				"LEFT OUTER JOIN picturegallery_gallerygroups as gg ON n.galleryID = gg.galleryID " +
				"LEFT OUTER JOIN picturegallery_galleryschools as gs ON n.galleryID = gs.galleryID " +
				"WHERE gg.groupID = ? OR gs.schoolID = ? OR n.global = true";
	}

	public List<Gallery> getGalleries(CommunityGroup group, String orderingField, Order order, boolean getLastPicture) throws SQLException {

		Connection connection = null;

		try{
			connection = dataSource.getConnection();

			LowLevelQuery<Gallery> query = new LowLevelQuery<Gallery>();

			String sql = getGroupGalleriesSQL;

			if(orderingField != null){
				sql += " ORDER BY " + orderingField;
			}

			if(order != null){
				sql += " " + order;
			}

			query.setSql(sql);

			query.addParameters(group.getGroupID(), group.getSchool().getSchoolID());
			query.disableAutoRelations(true);
			query.addRelations(Gallery.PICTURE_RELATION,Gallery.GROUP_RELATION,Gallery.SCHOOL_RELATION);

			List<Gallery> galleries = getAll(query, connection);

			if(getLastPicture && galleries != null){

				for(Gallery gallery : galleries){

					gallery.setLastPicture(getLastPictureTimestamp(gallery.getGalleryID(), connection));
				}
			}

			return galleries;

		}finally{

			DBUtils.closeConnection(connection);
		}
	}

	public Timestamp getLastPictureTimestamp(Integer galleryID, Connection connection) throws SQLException {

		ObjectQuery<Timestamp> query = new ObjectQuery<Timestamp>(connection, false, "SELECT MAX(posted) FROM picturegallery_pictures WHERE galleryID = ?", TimeStampPopulator.getPopulator());

		query.setInt(1, galleryID);

		return query.executeQuery();
	}
}

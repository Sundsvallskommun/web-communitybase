package se.dosf.communitybase.modules.pictureGallery.populators;

import java.sql.ResultSet;
import java.sql.SQLException;

import se.dosf.communitybase.modules.pictureGallery.beans.Picture;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;

public class PicturePopulator implements BeanResultSetPopulator<Picture> {
		
	public Picture populate(ResultSet rs) throws SQLException {
	
		Picture picture = new Picture();	
		
		picture.setFileID(rs.getInt("fileID"));
		picture.setFilename(rs.getString("filename"));
		picture.setSectionID(rs.getInt("sectionID"));
		picture.setDescription(rs.getString("description"));
		picture.setContentTypeSmall(rs.getString("contentTypeSmall"));
		picture.setContentTypeMedium(rs.getString("contentTypeMedium"));
		picture.setContentTypeLarge(rs.getString("contentTypeLarge"));
		picture.setPostedBy(rs.getInt("postedBy"));
		picture.setPosted(rs.getTimestamp("posted"));
		
		int columnCount = rs.getMetaData().getColumnCount();

		if(columnCount > 9){

			for(int columnIndex = 10; columnIndex < columnCount+1; columnIndex++){

				if(rs.getMetaData().getColumnName(columnIndex).equals("picSmall")){
					picture.setSmallPic(rs.getBlob(columnIndex));
				}else if(rs.getMetaData().getColumnName(columnIndex).equals("picMedium")){
					picture.setMediumPic(rs.getBlob(columnIndex));
				}else if(rs.getMetaData().getColumnName(columnIndex).equals("picLarge")){
					picture.setLargePic(rs.getBlob(columnIndex));
				}
				
			}
		}

		return picture ;
	}
}

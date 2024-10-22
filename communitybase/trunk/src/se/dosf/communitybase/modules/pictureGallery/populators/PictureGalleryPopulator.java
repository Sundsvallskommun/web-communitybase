package se.dosf.communitybase.modules.pictureGallery.populators;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import se.dosf.communitybase.enums.RelationType;
import se.dosf.communitybase.modules.pictureGallery.beans.Gallery;
import se.unlogic.fileuploadutils.BeanMultipartRequestPopulator;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.BeanRequestPopulator;

public class PictureGalleryPopulator implements BeanResultSetPopulator<Gallery>, BeanRequestPopulator<Gallery>, BeanMultipartRequestPopulator<Gallery> {

	private RelationType type;
	
	public PictureGalleryPopulator(RelationType type){
		this.type = type;
	}
	
	public Gallery populate(ResultSet rs) throws SQLException {
		
		Gallery gallery = new Gallery();
		
		gallery.setSectionID(rs.getInt(1));
		gallery.setRelationID(rs.getInt(2));
		gallery.setSectionName(rs.getString(3));
		gallery.setDescription(rs.getString(4));
		gallery.setRelationType(this.type);
		gallery.setPosted(rs.getTimestamp(5));
		
		return gallery;
		
	}
	
	public Gallery populate(MultipartRequest req, Integer relationID) throws ValidationException {
		
		Gallery gallery = new Gallery();
		gallery.setRelationID(relationID);
		
		return this.populate(gallery, req);
		
	}
	
	public Gallery populate(Gallery gallery, MultipartRequest req) throws ValidationException {
		
		return this.populate(gallery, req.getParameter("name"), req.getParameter("description"));
	
	}
	

	public Gallery populate(Gallery gallery, HttpServletRequest req) throws ValidationException {

		return this.populate(gallery, req.getParameter("name"), req.getParameter("description"));
		
	}
	
	private Gallery populate(Gallery gallery, String name, String description) throws ValidationException{
	
		ArrayList<ValidationError> validationErrors = new ArrayList<ValidationError>();

		if (StringUtils.isEmpty(name)) {
			validationErrors.add(new ValidationError("name", ValidationErrorType.RequiredField));
		}else if(name.length() > 65){
			validationErrors.add(new ValidationError("name", ValidationErrorType.TooLong));
		}
		
		
		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		} else {
			
			gallery.setSectionName(name);
			gallery.setDescription(description);
			
			return gallery;
		}
	
	}
	
	public Gallery populate(HttpServletRequest req, Integer relationID) throws ValidationException {
		
		Gallery gallery = new Gallery();
		gallery.setRelationID(relationID);
		
		return this.populate(gallery, req);
	}
	
	public Gallery populate(HttpServletRequest req) throws ValidationException {
		throw new UnsupportedOperationException();
	}

	public Gallery populate(MultipartRequest req){
		throw new UnsupportedOperationException();
	}
	

}

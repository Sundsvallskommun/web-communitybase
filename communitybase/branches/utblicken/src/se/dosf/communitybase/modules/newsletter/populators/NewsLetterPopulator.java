package se.dosf.communitybase.modules.newsletter.populators;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.fileupload.FileItem;

import se.dosf.communitybase.modules.newsletter.beans.NewsLetter;
import se.dosf.communitybase.utils.ScriptFilter;
import se.unlogic.fileuploadutils.BeanMultipartRequestPopulator;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.foregroundmodules.imagegallery.SimpleFileFilter;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;
import se.unlogic.standardutils.image.ImageUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;

public class NewsLetterPopulator implements BeanResultSetPopulator<NewsLetter>, BeanMultipartRequestPopulator<NewsLetter> {

	public NewsLetter populate(ResultSet rs) throws SQLException {

		NewsLetter newsletter = new NewsLetter();

		newsletter.setNewsletterID(rs.getInt("weekLetterID"));
		newsletter.setDate(rs.getTimestamp("date"));
		newsletter.setDescription(rs.getString("title"));
		newsletter.setURL(rs.getString("groupID"));
		newsletter.setGroupID(rs.getInt("groupID"));
		newsletter.setMessage(rs.getString("message"));
		newsletter.setMimetype(rs.getString("mimetype"));
		newsletter.setUserID(rs.getInt("posterID"));
		newsletter.setImage(rs.getBlob("image"));
		newsletter.setImageLocation(rs.getString("imagelocation"));

		return newsletter;
	}


	public NewsLetter populate(MultipartRequest req) throws ValidationException {
		throw new UnsupportedOperationException();
	}

	public NewsLetter populate(NewsLetter newsletter, MultipartRequest req) throws ValidationException {

		ArrayList<ValidationError> validationErrors = new ArrayList<ValidationError>();

		String title = req.getParameter("title");
		String text = req.getParameter("text");

		if(StringUtils.isEmpty(title)){
			validationErrors.add(new ValidationError("title", ValidationErrorType.RequiredField));
		} else if(title.length() > 255){
			validationErrors.add(new ValidationError("title", ValidationErrorType.TooLong));
		}

		if(StringUtils.isEmpty(text)){
			validationErrors.add(new ValidationError("text", ValidationErrorType.RequiredField));
		} else if(text.length() > 65535){
			validationErrors.add(new ValidationError("text", ValidationErrorType.TooLong));
		} else if(!ScriptFilter.validateNoScriptField(text)){
			validationErrors.add(new ValidationError("InvalidContent"));
		}

		Boolean pictureupload = req.getParameter("uploadCheck") != null;

		FileItem fileItem = null;

		if(pictureupload && req.getFileCount() > 0){

			fileItem = req.getFile(0);

			if(StringUtils.isEmpty(fileItem.getName())){

				validationErrors.add(new ValidationError("NoFile"));

			}else{

				if(!SimpleFileFilter.isValidFilename(fileItem.getName())){
					validationErrors.add(new ValidationError("BadFileFormat"));
				}

			}

		}

		if(!validationErrors.isEmpty()){
			throw new ValidationException(validationErrors);
		}else{

			newsletter.setTitle(title);
			newsletter.setMessage(text);
			newsletter.setDate(new Timestamp(System.currentTimeMillis()));

			if (pictureupload) {

				try {

					BufferedImage image = ImageUtils.getImage(fileItem.getInputStream());

					BufferedImage smallImage = ImageUtils.scaleImage(image, 400, 500, Image.SCALE_SMOOTH,BufferedImage.TYPE_INT_RGB);

					ByteArrayOutputStream smallImageStream = new ByteArrayOutputStream();

					ImageIO.write(smallImage, "jpg", smallImageStream);

					byte[] smallImageByteArray = smallImageStream.toByteArray();

					newsletter.setImage(new SerialBlob(smallImageByteArray));

					newsletter.setImageLocation(req.getParameter("imagelocation"));

				} catch (Exception e) {
					throw new ValidationException(new ValidationError("UnableToParseRequest"));
				}

			}

			// check for edit newsletter
			String imageLocation = req.getParameter("imagelocation.old");

			if(imageLocation != null){
				newsletter.setImageLocation(imageLocation);
			}

			boolean deleteImage = req.getParameter("deleteImage") != null;

			if(deleteImage){
				newsletter.setImage(null);
				newsletter.setImageLocation(null);
			}

			return newsletter;

		}

	}






}

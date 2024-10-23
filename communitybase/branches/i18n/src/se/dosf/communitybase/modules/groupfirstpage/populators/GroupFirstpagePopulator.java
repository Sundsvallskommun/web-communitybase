package se.dosf.communitybase.modules.groupfirstpage.populators;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.fileupload.FileItem;

import se.dosf.communitybase.modules.groupfirstpage.beans.GroupFirstpage;
import se.dosf.communitybase.utils.ScriptFilter;
import se.unlogic.fileuploadutils.BeanMultipartRequestPopulator;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.modules.imagegallery.SimpleFileFilter;
import se.unlogic.standardutils.image.ImageUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;


public class GroupFirstpagePopulator implements BeanMultipartRequestPopulator<GroupFirstpage> {

	public GroupFirstpage populate(MultipartRequest req) throws ValidationException {
		return this.populate(new GroupFirstpage(), req);
	}

	public GroupFirstpage populate(GroupFirstpage firstpage, MultipartRequest req) throws ValidationException {

		ArrayList<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		String title = req.getParameter("title");
		String text = req.getParameter("text");
		
		if(StringUtils.isEmpty(title)){
			validationErrors.add(new ValidationError("title", ValidationErrorType.RequiredField));
		}else if(title.length() > 255){
			validationErrors.add(new ValidationError("title", ValidationErrorType.TooLong));
		}
		
		if(StringUtils.isEmpty(text)){
			validationErrors.add(new ValidationError("text", ValidationErrorType.RequiredField));
		}else if(!ScriptFilter.validateNoScriptField(text)){
			validationErrors.add(new ValidationError("InvalidContent"));
		}
		
		Boolean pictureupload = req.getParameter("uploadCheck") != null;
		
		FileItem fileItem = null;
		
		if(pictureupload){
			
			if(req.getFileCount() > 0){
			
				fileItem = req.getFile(0);
				
				if(StringUtils.isEmpty(fileItem.getName())){
					validationErrors.add(new ValidationError("NoFile"));
				}else{
				
					if(!SimpleFileFilter.isValidFilename(fileItem.getName())){
						validationErrors.add(new ValidationError("BadFileFormat"));
					}
					
				}
			
			}else{
				validationErrors.add(new ValidationError("NoFile"));
			}
			
		}
		
		if(!validationErrors.isEmpty()){
			throw new ValidationException(validationErrors);
		}else{
			
			firstpage.setTitle(title);
			firstpage.setContent(text);

			if (pictureupload) {

				try {
					
					BufferedImage image = ImageUtils.getImage(fileItem.getInputStream());
					
					BufferedImage smallImage = ImageUtils.scaleImage(image, 400, 500, Image.SCALE_SMOOTH);
					
					ByteArrayOutputStream smallImageStream = new ByteArrayOutputStream();

					ImageIO.write(smallImage, "jpg", smallImageStream);

					byte[] smallImageByteArray = smallImageStream.toByteArray();

					firstpage.setImage(new SerialBlob(smallImageByteArray));
					
					firstpage.setImageLocation(req.getParameter("imagelocation"));
				
				} catch (Exception e) {
					throw new ValidationException(new ValidationError("UnableToParseRequest"));
				}

			}
			
			// check for edit newsletter
			String imageLocation = req.getParameter("imagelocation.old");
			
			if(imageLocation != null){
				firstpage.setImageLocation(imageLocation);
			}
			
			boolean deleteImage = req.getParameter("deleteImage") != null;
			
			if(deleteImage){
				firstpage.setImage(null);
				firstpage.setImageLocation(null);
			}
			
			return firstpage;

		}
		
	}
	
	
}

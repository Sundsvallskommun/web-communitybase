package se.dosf.communitybase.modules.filearchive.populators;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;

import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.modules.filearchive.beans.File;
import se.unlogic.fileuploadutils.BeanMultipartRequestPopulator;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;
import se.unlogic.standardutils.mime.MimeUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;

public class FilePopulator implements BeanResultSetPopulator<File>, BeanMultipartRequestPopulator<File> {

	private UserHandler userHandler = null;

	public FilePopulator(){

	}

	public FilePopulator(UserHandler userHandler){
		this.userHandler = userHandler;
	}

	public File populate(ResultSet rs) throws SQLException {
		File filearchive = new File();

		filearchive.setFileID(rs.getInt("fileID"));
		filearchive.setDate(rs.getTimestamp("posted"));
		filearchive.setDescription(rs.getString("description"));
		filearchive.setFileName(rs.getString("fileName"));
		filearchive.setSectionID(rs.getInt("sectionID"));
		filearchive.setContentType(rs.getString("contentType"));
		filearchive.setPoster((CommunityUser)userHandler.getUser(rs.getInt("postedBy"), false));
		filearchive.setSectionName(rs.getString("name"));
		filearchive.setFileSize(rs.getLong("fileSize"));

		if(rs.getMetaData().getColumnCount() > 9){
			filearchive.setBlob(rs.getBlob("file"));
		}

		return filearchive;
	}


	public File populate(HttpServletRequest req) throws ValidationException{
		return this.populate(new File(), req);
	}

	public File populate(File file, HttpServletRequest req) throws ValidationException {

		String id = req.getParameter("sectionID");
		String description = req.getParameter("description");

		file.setDescription(description);
		file.setSectionID(Integer.parseInt(id));

		return file;
	}

	public File populate(MultipartRequest req)throws ValidationException {
		// TODO Auto-generated method stub
		return null;
	}


	public File populate(File file, MultipartRequest req)throws ValidationException {
		if(req.getFileCount() >= 1){
			FileItem fileItem = req.getFile(0);

			if(StringUtils.isEmpty(fileItem.getName())){
				throw new ValidationException(new ValidationError("NoFileAttached"));
			}else{

				String description = req.getParameter("description");
				
				file.setFileName(StringUtils.toValidHttpFilename(FilenameUtils.getName(fileItem.getName())));
				file.setFileSize(fileItem.getSize());
				file.setContentType(MimeUtils.getMimeType(file.getFileName()));
				
				try {
					SerialBlob blob = new SerialBlob(fileItem.get());
					
					if (blob.length() - 1 < 0) {
			            throw new ValidationException(new ValidationError("FileTooSmall"));
			        }
					
					file.setBlob(blob);
					
				} catch (SerialException e1) {
					throw new ValidationException(new ValidationError("UnableToParseRequest"));
				} catch (SQLException e1) {
					throw new ValidationException(new ValidationError("UnableToParseRequest"));
				}
				
				file.setDescription(description);

				return file;
			}

		}else{
			throw new ValidationException(new ValidationError("NoFileAttached"));
		}
	}

}

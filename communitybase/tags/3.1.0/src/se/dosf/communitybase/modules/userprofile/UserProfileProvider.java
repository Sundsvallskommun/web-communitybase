package se.dosf.communitybase.modules.userprofile;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.MutableUser;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.UnableToAddUserException;
import se.unlogic.hierarchy.core.exceptions.UnableToUpdateUserException;
import se.unlogic.standardutils.validation.ValidationException;


public interface UserProfileProvider {

	public User addExternalUser(HttpServletRequest req, String email) throws ValidationException, UnableToAddUserException, IOException, UnableToUpdateUserException, ModuleConfigurationException, Exception;

	public User updateExternalUser(HttpServletRequest req, MutableUser user) throws ValidationException, UnableToAddUserException, IOException, UnableToUpdateUserException;

	public void setUserProfileImage(User user, BufferedImage image) throws Exception;

	public String getProfileImageAlias();

	public String getShowProfileAlias();

	public int getMaximumFileSize();

	public Long getUserProfileImageLastModified(User user);

}

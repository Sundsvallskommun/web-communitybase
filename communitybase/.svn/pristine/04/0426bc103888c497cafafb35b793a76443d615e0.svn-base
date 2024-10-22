package se.dosf.communitybase.modules.userprofile;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.CBConstants;
import se.dosf.communitybase.utils.CBAccessUtils;
import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.MutableUser;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.exceptions.UnableToAddUserException;
import se.unlogic.hierarchy.core.exceptions.UnableToUpdateUserException;
import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.utils.UserUtils;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.bool.BooleanUtils;
import se.unlogic.standardutils.image.ImageUtils;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.http.enums.ContentDisposition;
import se.unlogic.webutils.validation.ValidationUtils;

public class UserProfileModule extends AnnotatedForegroundModule implements UserProfileProvider {

	private static final Pattern PASSWORD_STRENGTH_PATTERN = Pattern.compile("^(?=.*\\p{N})(?=.*\\p{Ll})(?=.*\\p{Lu}).{8,}$");

	private static final EmailPopulator EMAIL_POPULATOR = new EmailPopulator();

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Base file store path (profile images)", description = "Path to the directory to be used as base filestore for profile images", required = true)
	private String baseFilestore;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max upload size", description = "Maxmium upload size in megabytes allowed in a single post request", required = true, formatValidator = PositiveStringIntegerValidator.class)
	private Integer diskThreshold = 100;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "RAM threshold", description = "Maximum size of files in KB to be buffered in RAM during file uploads. Files exceeding the threshold are written to disk instead.", required = true, formatValidator = PositiveStringIntegerValidator.class)
	private Integer ramThreshold = 500;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Profile image max size (in px)", description = "The max size of the profile image (default is 270)", required = true, formatValidator = StringIntegerValidator.class)
	protected Integer profileImageSize = 270;

	@ModuleSetting(allowsNull=true)
	@TextFieldSettingDescriptor(name="Resumé settings alias", description="The full alias e-mail resumé settings alias")
	private String resumeSettingsAlias;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Set external user attribute", description = "Controls whether or not external user attribute should be set when adding external users")
	protected boolean setExternalUserAttribute = true;
	
	@ModuleSetting(allowsNull = true)
	@HTMLEditorSettingDescriptor(name = "Internal user info text", description = "The information text displayed for internal users regarding their ability to update their profile.")
	private String internalUserInfoText;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name="External user class", description="The user class to be used for new external users", required=true)
	private String userClass;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(UserProfileProvider.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + UserProfileProvider.class.getName());
		}
	}

	@Override
	protected void moduleConfigured() throws Exception {

		if(userClass == null){
			
			log.error("No external user class set, check settings.");
		}
	}
	
	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		redirectToMethod(req, res, "/show/" + user.getUserID());

		return null;
	}

	@WebPublic
	public ForegroundModuleResponse show(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Integer userID = uriParser.getInt(2);

		User requestedUser = null;

		if (userID != null && (requestedUser = systemInterface.getUserHandler().getUser(userID, false, true)) != null && requestedUser instanceof MutableUser) {

			log.info("User " + user + " showing user profile for user " + requestedUser);

			Document doc = this.createDocument(req, uriParser, user);

			Element userProfileElement = doc.createElement("ShowUserProfile");
			doc.getFirstChild().appendChild(userProfileElement);

			if(resumeSettingsAlias != null && user.equals(requestedUser)){

				XMLUtils.appendNewElement(doc, userProfileElement, "ResumeSettingsURI", req.getContextPath() + resumeSettingsAlias);
			}

			XMLUtils.appendNewElement(doc, userProfileElement, "InternalUserInfoText", internalUserInfoText);

			UserUtils.appendUsers(doc, userProfileElement, Arrays.asList(requestedUser), true);

			return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), this.getDefaultBreadcrumb());

		}

		throw new URINotFoundException(uriParser);

	}

	@WebPublic
	public ForegroundModuleResponse update(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Integer userID = uriParser.getInt(2);

		User requestedUser = null;

		if (userID != null && (requestedUser = systemInterface.getUserHandler().getUser(userID, false, true)) != null && requestedUser instanceof MutableUser) {

			if (!requestedUser.getUserID().equals(user.getUserID()) || !CBAccessUtils.isExternalUser(requestedUser)) {

				throw new AccessDeniedException("Update profile access denied");
			}

			List<ValidationError> errors = new ArrayList<ValidationError>();

			if (req.getMethod().equalsIgnoreCase("POST")) {

				try {

					requestedUser = this.updateExternalUser(req, (MutableUser) user);

					log.info("User " + user + " updated user " + requestedUser);

					redirectToMethod(req, res, "/show/" + requestedUser.getUserID());

					return null;

				} catch (ValidationException validationException) {

					if (req.getAttribute("multipartRequest") != null) {

						req = (MultipartRequest) req.getAttribute("multipartRequest");
					}

					errors.addAll(validationException.getErrors());

				}

			}

			log.info("User " + user + " requested user profile update form for user " + requestedUser);

			Document doc = this.createDocument(req, uriParser, user);

			Element userProfileElement = doc.createElement("UpdateUserProfile");
			doc.getFirstChild().appendChild(userProfileElement);

			XMLUtils.appendNewElement(doc, userProfileElement, "MaxAllowedFileSize", BinarySizeFormater.getFormatedSize(getMaximumFileSize() * BinarySizes.MegaByte));
			if (hasProfileImage(requestedUser)) {
				XMLUtils.appendNewElement(doc, userProfileElement, "HasProfileImage", true);
			}

			UserUtils.appendUsers(doc, userProfileElement, Arrays.asList(requestedUser), true);

			if (!errors.isEmpty()) {

				userProfileElement.appendChild(new ValidationException(errors).toXML(doc));
				userProfileElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), this.getDefaultBreadcrumb());

		}

		throw new URINotFoundException(uriParser);

	}

	@Override
	public User addExternalUser(HttpServletRequest req, String email) throws Exception {

		if(userClass == null){
			
			throw new ModuleConfigurationException("No external user class set");
		}
		
		MutableUser mutableUser = (MutableUser) ReflectionUtils.getInstance(userClass);
		mutableUser.setEmail(email);

		return addOrUpdateExternalUser(req, mutableUser, false, true);

	}

	@Override
	public User updateExternalUser(HttpServletRequest req, MutableUser user) throws ValidationException, UnableToAddUserException, IOException, UnableToUpdateUserException {

		return addOrUpdateExternalUser(req, user, true, false);
	}

	@Override
	public String getProfileImageAlias() {

		return getFullAlias() + "/profileimage";
	}

	@Override
	public String getShowProfileAlias() {

		return getFullAlias() + "/show";
	}

	@Override
	public int getMaximumFileSize() {

		return diskThreshold;
	}

	private User addOrUpdateExternalUser(HttpServletRequest req, MutableUser user, boolean populateEmail, boolean forcePopulatePassword) throws ValidationException, UnableToAddUserException, IOException, UnableToUpdateUserException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		req = parseRequest(req);

		String firstname = ValidationUtils.validateParameter("firstname", req, true, 0, 255, errors);
		String lastname = ValidationUtils.validateParameter("lastname", req, true, 0, 255, errors);
		String phone = ValidationUtils.validateParameter("phone", req, true, 0, 255, errors);
		String organization = ValidationUtils.validateParameter("organization", req, true, 0, 255, errors);
		String description = ValidationUtils.validateParameter("description", req, false, 0, 4096, errors);

		boolean passwordChanged = false;
		String password = null;

		if (forcePopulatePassword || req.getParameter("password") != null) {

			password = ValidationUtils.validateParameter("password", req, forcePopulatePassword, 0, 255, errors);

			if (password != null) {

				String passwordMatch = ValidationUtils.validateParameter("passwordMatch", req, true, 0, 255, errors);

				if (!password.equals(passwordMatch)) {

					errors.add(new ValidationError("PasswordDontMatch"));
				}

				if (!PASSWORD_STRENGTH_PATTERN.matcher(password).matches()) {

					errors.add(new ValidationError("InvalidPasswordStrength"));
				}
				
				passwordChanged = true;
			}

		}

		String email = null;

		if (populateEmail) {

			email = ValidationUtils.validateParameter("email", req, true, 0, 255, EMAIL_POPULATOR, errors);

		}

		BufferedImage profileImage = this.populateProfileImage(req);

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}

		user.setEnabled(true);
		user.setFirstname(firstname);
		user.setLastname(lastname);

		if (passwordChanged) {
			user.setPassword(password);
		}

		if (populateEmail) {
			user.setEmail(email);
			user.setUsername(email);
		}

		AttributeHandler attributeHandler = user.getAttributeHandler();

		if (attributeHandler != null && attributeHandler instanceof MutableAttributeHandler) {

			MutableAttributeHandler mutableAttributeHandler = (MutableAttributeHandler) attributeHandler;

			mutableAttributeHandler.setAttribute(CBConstants.USER_PHONE_ATTRIBUTE, phone);
			mutableAttributeHandler.setAttribute(CBConstants.USER_ORGANIZATION_NAME_ATTRIBUTE, organization);

			if (!StringUtils.isEmpty(description)) {
				mutableAttributeHandler.setAttribute("description", description);
			}

			if(setExternalUserAttribute) {
			
				mutableAttributeHandler.setAttribute(CBConstants.EXTERNAL_USER_ATTRIBUTE, true);
			}

		}

		if (user.getUserID() == null) {

			systemInterface.getUserHandler().addUser(user);

		} else {

			systemInterface.getUserHandler().updateUser(user, passwordChanged, false, true);

		}

		if (profileImage != null) {

			addProfileImage(profileImage, user, true);

		} else if (BooleanUtils.toBoolean(req.getParameter("deleteProfileImage"))) {

			deleteProfileImage(req, user);
		}

		return user;
	}

	private HttpServletRequest parseRequest(HttpServletRequest req) throws ValidationException {

		if (MultipartRequest.isMultipartRequest(req)) {

			try {

				MultipartRequest multipartRequest = new MultipartRequest(ramThreshold * BinarySizes.MegaByte, diskThreshold * BinarySizes.MegaByte, req);

				req.setAttribute("multipartRequest", multipartRequest);

				return multipartRequest;

			} catch (SizeLimitExceededException e) {

				throw new ValidationException(new ValidationError("FileSizeLimitExceeded"));

			} catch (FileSizeLimitExceededException e) {

				throw new ValidationException(new ValidationError("FileSizeLimitExceeded"));

			} catch (FileUploadException e) {

				throw new ValidationException(new ValidationError("UnableToParseRequest"));
			}
		}

		return req;

	}

	private BufferedImage populateProfileImage(HttpServletRequest req) throws ValidationException {

		if (!(req instanceof MultipartRequest)) {

			return null;
		}

		MultipartRequest multipartRequest = (MultipartRequest) req;

		try {

			if (multipartRequest.getFileCount() > 0 && !StringUtils.isEmpty(multipartRequest.getFile(0).getName())) {

				FileItem file = multipartRequest.getFile(0);

				String lowerCasefileName = file.getName().toLowerCase();

				if (!(lowerCasefileName.endsWith(".png") || lowerCasefileName.endsWith(".jpg") || lowerCasefileName.endsWith(".gif") || lowerCasefileName.endsWith(".bmp"))) {

					throw new ValidationException(new ValidationError("InvalidProfileImageFileFormat"));

				} else {

					try {

						BufferedImage image = ImageUtils.getImage(file.get());

						image = ImageUtils.cropAsSquare(image);

						if (image.getWidth() > profileImageSize || image.getHeight() > profileImageSize) {

							image = ImageUtils.scale(image, profileImageSize, profileImageSize, Image.SCALE_SMOOTH, BufferedImage.TYPE_INT_ARGB);

						}

						return image;

					} catch (Exception e) {

						throw new ValidationException(new ValidationError("UnableToParseProfileImage"));
					}

				}

			}

		} finally {

			multipartRequest.deleteFiles();
		}

		return null;
	}

	private void addProfileImage(BufferedImage image, User user, boolean logResults) throws IOException {

		File imageFile = new File(baseFilestore + "/" + user.getUserID() + ".png");

		boolean exists = imageFile.exists();

		ImageIO.write(image, "png", imageFile);

		if(logResults){

			if (exists) {
				log.info("Updated profile image for user " + user);
			} else {
				log.info("Added profile image for user " + user);
			}
		}
	}

	private void deleteProfileImage(HttpServletRequest req, User user) throws ValidationException {

		checkConfiguration();

		if (!deleteProfileImage(user.getUserID())) {

			throw new ValidationException(new ValidationError("UnableToDeleteProfileImage"));
		}

	}

	private boolean deleteProfileImage(int userID){

		File profileImage = new File(baseFilestore + File.separator + userID + ".png");

		if (profileImage.exists()) {

			return profileImage.delete();
		}

		return false;
	}

	private boolean hasProfileImage(User user) {

		checkConfiguration();

		File profileImage = new File(baseFilestore + "/" + user.getUserID() + ".png");

		return profileImage.exists();
	}

	@WebPublic(alias = "profileimage")
	public ForegroundModuleResponse getProfileImage(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		checkConfiguration();

		Integer userID = null;

		if (uriParser.size() == 3 && (userID = NumberUtils.toInt(uriParser.get(2))) != null) {

			String fileName = userID + ".png";

			File profileImage = new File(baseFilestore + "/" + fileName);

			if (profileImage.exists()) {

				HTTPUtils.sendFile(profileImage, req, res, ContentDisposition.INLINE);

			} else {

				URL url = UserProfileModule.class.getResource("staticcontent/pics/empty-profileimage.png");

				HTTPUtils.sendFile(url, url.openStream(), "empty-profileimage.png", req, res, ContentDisposition.INLINE);

			}

			return null;

		}

		throw new URINotFoundException(uriParser);
	}

	private void checkConfiguration() {

		if (baseFilestore == null) {

			throw new RuntimeException("Module is not properly configured, please check modulesettings");
		}

	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(UserProfileProvider.class, this);

		super.unload();
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();

		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));

		if (user != null) {
			document.appendChild(user.toXML(doc));
		}

		XMLUtils.appendNewElement(doc, document, "ProfileImageAlias", getProfileImageAlias());

		doc.appendChild(document);

		return doc;
	}

	@Override
	public void setUserProfileImage(User user, BufferedImage image) throws Exception {

		checkConfiguration();

		if(image == null){

			deleteProfileImage(user.getUserID());

		}else{

			image = ImageUtils.cropAsSquare(image);

			if (image.getWidth() > profileImageSize || image.getHeight() > profileImageSize) {

				image = ImageUtils.scale(image, profileImageSize, profileImageSize, Image.SCALE_SMOOTH, BufferedImage.TYPE_INT_ARGB);
			}

			addProfileImage(image, user, false);
		}
	}

	@Override
	public Long getUserProfileImageLastModified(User user) {

		File imageFile = new File(baseFilestore + "/" + user.getUserID() + ".png");

		if(imageFile.exists()){

			return imageFile.lastModified();
		}

		return null;
	}
}

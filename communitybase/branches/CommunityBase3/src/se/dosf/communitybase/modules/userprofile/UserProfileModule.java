package se.dosf.communitybase.modules.userprofile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import se.dosf.communitybase.interfaces.GroupProfileProvider;
import se.dosf.communitybase.modules.mobilephonevalidation.MobilePhoneValidationModule;
import se.dosf.communitybase.modules.userprofile.beans.CropCoordinates;
import se.dosf.communitybase.modules.userprofile.events.UserUpdatedEvent;
import se.dosf.communitybase.utils.CBAccessUtils;
import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.MutableUser;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.exceptions.UnableToAddUserException;
import se.unlogic.hierarchy.core.exceptions.UnableToUpdateUserException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.UserUtils;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.bool.BooleanUtils;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.image.ImageUtils;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.http.enums.ContentDisposition;
import se.unlogic.webutils.validation.ValidationUtils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;

public class UserProfileModule extends AnnotatedForegroundModule implements UserProfileProvider {

	protected static final Pattern PASSWORD_STRENGTH_PATTERN = Pattern.compile("^(?=.*\\p{N})(?=.*\\p{Ll})(?=.*\\p{Lu}).{8,}$");

	protected static final EmailPopulator EMAIL_POPULATOR = new EmailPopulator();

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Base file store path (profile images)", description = "Path to the directory to be used as base filestore for profile images", required = true)
	private String baseFilestore;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max file size", description = "Maxmium file size in megabytes allowed", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer maxFileSize = 15;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max upload size", description = "Maxmium upload size in megabytes allowed in a single post request", required = true, formatValidator = PositiveStringIntegerValidator.class)
	private Integer diskThreshold = 100;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "RAM threshold", description = "Maximum size of files in KB to be buffered in RAM during file uploads. Files exceeding the threshold are written to disk instead.", required = true, formatValidator = PositiveStringIntegerValidator.class)
	private Integer ramThreshold = 500;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Profile image max size (in px)", description = "The max size of the profile image (default is 270)", required = true, formatValidator = StringIntegerValidator.class)
	protected Integer profileImageSize = 270;

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Resumé settings alias", description = "The full alias e-mail resumé settings alias")
	private String resumeSettingsAlias;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Set external user attribute", description = "Controls whether or not external user attribute should be set when adding external users")
	protected boolean setExternalUserAttribute = true;

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "External user group", description = "Group to add external users to")
	protected Integer externalUserGroup;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Lock updating to external users", description = "Controls whether or not only external users can use profile update")
	protected boolean lockUpdateToExternalUsers = true;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Allow changing person data", description = "Allow changing person data for internal users")
	protected boolean allowChangingPersonDataInternal = false;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Allow changing person company", description = "Allow changing person companty for internal users")
	protected boolean allowChangingPersonCompanyInternal = false;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Allow changing of description", description = "Allow changing of description for internal users")
	protected boolean allowChangingDescription = true;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Allow changing of profile image", description = "Allow changing of profile image for internal users")
	protected boolean allowChangingProfileImage = true;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Force external update form", description = "Force all users to use external update form")
	protected boolean forceExternalUpdateForm = false;

	@ModuleSetting(allowsNull = true)
	@HTMLEditorSettingDescriptor(name = "Internal user info text", description = "The information text displayed for internal users regarding their ability to update their profile.")
	private String internalUserInfoText;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "External user class", description = "The user class to be used for new external users", required = true)
	private String userClass;
	
	// TODO differentiate between internal and external users?
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Require phone number", description = "Require phone number in registration")
	private boolean requirePhone = true;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Hide default description", description = "Hide default user description")
	private boolean hideDefaultDescription = false;

	@InstanceManagerDependency(required = false)
	private GroupProfileProvider groupProfileProvider;

	@InstanceManagerDependency
	private MobilePhoneValidationModule phoneValidationModule;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(UserProfileProvider.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + UserProfileProvider.class.getName());
		}
	}

	@Override
	protected void moduleConfigured() throws Exception {

		if (userClass == null) {

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

			if (groupProfileProvider != null && groupProfileProvider.checkAccess(user)) {
				XMLUtils.appendNewElement(doc, userProfileElement, "UserGroupProviderAlias", groupProfileProvider.getFullAlias());
			}

			if (resumeSettingsAlias != null && user.equals(requestedUser)) {

				XMLUtils.appendNewElement(doc, userProfileElement, "ResumeSettingsURI", req.getContextPath() + resumeSettingsAlias);
			}

			XMLUtils.appendNewElement(doc, userProfileElement, "InternalUserInfoText", internalUserInfoText);

			UserUtils.appendUsers(doc, userProfileElement, Arrays.asList(requestedUser), true);

			if (lockUpdateToExternalUsers) {
				XMLUtils.appendNewElement(doc, userProfileElement, "LockUpdateToExternalUsers");
			}
			
			if (hideDefaultDescription) {
				XMLUtils.appendNewElement(doc, userProfileElement, "HideDefaultDescription");
			}

			XMLUtils.appendNewElement(doc, userProfileElement, "Timestamp", TimeUtils.getCurrentTimestamp().getTime());

			return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), this.getDefaultBreadcrumb());

		}

		throw new URINotFoundException(uriParser);

	}

	protected User getUser(Integer userID) {

		return systemInterface.getUserHandler().getUser(userID, false, true);
	}

	@WebPublic
	public ForegroundModuleResponse update(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Integer userID = uriParser.getInt(2);

		User requestedUser = null;

		if (userID != null && (requestedUser = getUser(userID)) != null && requestedUser instanceof MutableUser) {

			if (!requestedUser.getUserID().equals(user.getUserID()) || (lockUpdateToExternalUsers && !CBAccessUtils.isExternalUser(requestedUser))) {

				throw new AccessDeniedException("Update profile access denied");
			}

			List<ValidationError> errors = new ArrayList<ValidationError>();

			if (req.getMethod().equalsIgnoreCase("POST")) {

				try {

					if (forceExternalUpdateForm || CBAccessUtils.isExternalUser(requestedUser)) {
						requestedUser = this.updateExternalUser(req, (MutableUser) user);
					} else {
						requestedUser = this.updateInternalUser(req, (MutableUser) user);
					}

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

			if (!CBAccessUtils.isExternalUser(requestedUser) && !allowChangingPersonCompanyInternal && !forceExternalUpdateForm) {
				XMLUtils.appendNewElement(doc, userProfileElement, "DisablePersonCompany");
			}
			if (!CBAccessUtils.isExternalUser(requestedUser) && !allowChangingPersonDataInternal && !forceExternalUpdateForm) {
				XMLUtils.appendNewElement(doc, userProfileElement, "DisablePersonData");
			}
			
			if (!CBAccessUtils.isExternalUser(requestedUser) && allowChangingDescription) {
				XMLUtils.appendNewElement(doc, userProfileElement, "AllowChangingDescription");
			}
			
			if (!CBAccessUtils.isExternalUser(requestedUser) && allowChangingProfileImage) {
				XMLUtils.appendNewElement(doc, userProfileElement, "AllowChangingProfileImage");
			}

			if (CBAccessUtils.isExternalUser(requestedUser) && phoneValidationModule != null) {
				XMLUtils.appendNewElement(doc, userProfileElement, "PhoneValidationSupported");
			}

			if (forceExternalUpdateForm) {
				XMLUtils.appendNewElement(doc, userProfileElement, "ForceExternalUpdateForm");
				
			}
			
			XMLUtils.appendNewElement(doc, userProfileElement, "PhoneRequired", isRequirePhone());

			XMLUtils.appendNewElement(doc, userProfileElement, "MaxAllowedFileSize", BinarySizeFormater.getFormatedSize(getMaximumFileSize() * BinarySizes.MegaByte));
			if (hasProfileImage(requestedUser)) {
				XMLUtils.appendNewElement(doc, userProfileElement, "HasProfileImage", true);
			}

			UserUtils.appendUsers(doc, userProfileElement, Arrays.asList(requestedUser), true);

			updateElementHook(doc, userProfileElement, user, requestedUser);

			if (!errors.isEmpty()) {

				userProfileElement.appendChild(new ValidationException(errors).toXML(doc));
				userProfileElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			XMLUtils.appendNewElement(doc, userProfileElement, "ImageSize", profileImageSize);

			XMLUtils.appendNewElement(doc, userProfileElement, "Timestamp", TimeUtils.getCurrentTimestamp().getTime());

			return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), this.getDefaultBreadcrumb());

		}

		throw new URINotFoundException(uriParser);

	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse sendVerification(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		String phoneNumber = null;

		if (phoneValidationModule != null && (phoneNumber = req.getParameter("mobilePhone")) != null) {
			JsonObject jsonObject = new JsonObject(1);

			if (phoneValidationModule.validateNumber(phoneNumber)) {
				if (phoneValidationModule.sendSMSCode(phoneNumber, user, req)) {
					jsonObject.putField("Success", "true");
				} else {
					jsonObject.putField("UnknownError", "true");
				}
			} else {
				jsonObject.putField("InvalidPhoneNumber", "true");
			}

			HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);

			return null;
		}

		throw new URINotFoundException(uriParser);
	}

	private User updateInternalUser(HttpServletRequest req, MutableUser user) throws Exception {

		List<ValidationError> errors = new ArrayList<>();

		req = parseRequest(req);

		String firstname = null;
		String lastname = null;
		String phone = null;
		String phone2 = null;
		String email = null;
		String organization = null;
		String title = null;

		if (allowChangingPersonDataInternal) {
			firstname = ValidationUtils.validateParameter("firstname", req, true, 0, 255, errors);
			lastname = ValidationUtils.validateParameter("lastname", req, true, 0, 255, errors);
			phone = ValidationUtils.validateParameter("phone", req, requirePhone, 0, 255, errors);
			phone2 = ValidationUtils.validateParameter("phone2", req, false, 0, 255, errors);
			email = ValidationUtils.validateParameter("email", req, true, 0, 255, EMAIL_POPULATOR, errors);
		}

		if (allowChangingPersonCompanyInternal) {
			organization = ValidationUtils.validateParameter("organization", req, true, 0, 255, errors);
			title = ValidationUtils.validateParameter("title", req, false, 0, 255, errors);
		}

		String description = null;
		BufferedImage populatedImage = null;
		
		if (allowChangingPersonDataInternal || allowChangingDescription) {
			description = ValidationUtils.validateParameter("description", req, false, 0, 4096, errors);
			
			if (description == null) {
				description = "";
			}
		}
		
		if (allowChangingPersonDataInternal || allowChangingProfileImage) {
			populatedImage = populateProfileImage(req);
		}

		if (email != null) {

			User emailMatch = systemInterface.getUserHandler().getUserByEmail(email, false, false);

			if (emailMatch != null && !emailMatch.equals(user)) {
				errors.add(new ValidationError("EmailAlreadyTaken"));
			}
		}

		if (!errors.isEmpty()) {
			throw new ValidationException(errors);
		}

		if (allowChangingPersonDataInternal) {
			user.setFirstname(firstname);
			user.setLastname(lastname);
			user.setEmail(email);
		}

		AttributeHandler attributeHandler = user.getAttributeHandler();

		if (attributeHandler instanceof MutableAttributeHandler) {

			MutableAttributeHandler mutableAttributeHandler = (MutableAttributeHandler) attributeHandler;

			if (allowChangingPersonDataInternal) {
				mutableAttributeHandler.setAttribute(CBConstants.USER_PHONE_ATTRIBUTE, phone);
				mutableAttributeHandler.setAttribute(CBConstants.USER_PHONE2_ATTRIBUTE, phone2);
			}

			if (allowChangingPersonCompanyInternal) {
				mutableAttributeHandler.setAttribute(CBConstants.USER_ORGANIZATION_NAME_ATTRIBUTE, organization);
				mutableAttributeHandler.setAttribute(CBConstants.USER_TITLE_ATTRIBUTE, title);
			}

			if (description != null) {
				mutableAttributeHandler.setAttribute("description", description);
			}
		}

		systemInterface.getUserHandler().updateUser(user, false, false, true);

		if (populatedImage != null) {

			populatedImage = addProfileImage(populatedImage, getCropCoordinates(req), user, true);

		} else if (BooleanUtils.toBoolean(req.getParameter("deleteProfileImage"))) {

			deleteProfileImage(req, user);
		}

		UserUpdatedEvent userUpdatedEvent = new UserUpdatedEvent();
		userUpdatedEvent.setUser(user);
		userUpdatedEvent.setImageDeleted(BooleanUtils.toBoolean(req.getParameter("deleteProfileImage")));

		if (populatedImage != null) {
			userUpdatedEvent.setImage(populatedImage);
		}

		systemInterface.getEventHandler().sendEvent(User.class, userUpdatedEvent, EventTarget.ALL);

		return user;
	}

	private BufferedImage checkAndFixRotation(BufferedImage image, InputStream inputStream) {

		int orientation = 1;

		try {
			Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

			Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);

			orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);

			return transformImage(image, getExifTransformation(orientation, jpegDirectory.getImageWidth(), jpegDirectory.getImageHeight()));
		} catch (Exception me) {
			log.warn("Could not get orientation for image");
		}

		return image;
	}

	// Look at http://chunter.tistory.com/143 for information
	public AffineTransform getExifTransformation(int orientation, int width, int height) {

		AffineTransform t = new AffineTransform();

		switch (orientation) {
			case 1:
				break;
			case 2: // Flip X
				t.scale(-1.0, 1.0);
				t.translate(-width, 0);
				break;
			case 3: // PI rotation 
				t.translate(width, height);
				t.rotate(Math.PI);
				break;
			case 4: // Flip Y
				t.scale(1.0, -1.0);
				t.translate(0, -height);
				break;
			case 5: // - PI/2 and Flip X
				t.rotate(-Math.PI / 2);
				t.scale(-1.0, 1.0);
				break;
			case 6: // -PI/2 and -width
				t.translate(height, 0);
				t.rotate(Math.PI / 2);
				break;
			case 7: // PI/2 and Flip
				t.scale(-1.0, 1.0);
				t.translate(-height, 0);
				t.translate(0, width);
				t.rotate(3 * Math.PI / 2);
				break;
			case 8: // PI / 2
				t.translate(0, width);
				t.rotate(3 * Math.PI / 2);
				break;
		}

		return t;
	}

	public static BufferedImage transformImage(BufferedImage image, AffineTransform transform) throws Exception {

		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);

		BufferedImage destinationImage = op.createCompatibleDestImage(image, (image.getType() == BufferedImage.TYPE_BYTE_GRAY) ? image.getColorModel() : null);
		Graphics2D g = destinationImage.createGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, destinationImage.getWidth(), destinationImage.getHeight());
		destinationImage = op.filter(image, destinationImage);

		return destinationImage;
	}

	private CropCoordinates getCropCoordinates(HttpServletRequest req) {

		String coordParam = req.getParameter("crop-coordinates");

		if (!StringUtils.isEmpty(coordParam)) {
			ArrayList<Integer> coords = NumberUtils.toInt(coordParam.split(","));

			if (coords != null && coords.size() == 4 && coords.get(1) < coords.get(3) && coords.get(0) < coords.get(2)) {
				return new CropCoordinates(coords);
			}
		}

		return null;
	}

	protected void updateElementHook(Document doc, Element userProfileElement, User user, User requestedUser) {

	}

	@Override
	public User addExternalUser(HttpServletRequest req, String email) throws Exception {

		if (userClass == null) {

			throw new ModuleConfigurationException("No external user class set");
		}

		MutableUser mutableUser = (MutableUser) ReflectionUtils.getInstance(userClass);
		mutableUser.setEmail(email);

		return addOrUpdateExternalUser(req, mutableUser, false, true, true);

	}

	@Override
	public User updateExternalUser(HttpServletRequest req, MutableUser user) throws ValidationException, UnableToAddUserException, IOException, UnableToUpdateUserException {

		return addOrUpdateExternalUser(req, user, true, false, false);
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

		return maxFileSize;
	}

	protected User addOrUpdateExternalUser(HttpServletRequest req, MutableUser user, boolean populateEmail, boolean forcePopulatePassword, boolean isAdd) throws ValidationException, UnableToAddUserException, IOException, UnableToUpdateUserException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		req = parseRequest(req);

		String firstname = ValidationUtils.validateParameter("firstname", req, true, 0, 30, errors);
		String lastname = ValidationUtils.validateParameter("lastname", req, true, 0, 50, errors);
		String phone = ValidationUtils.validateParameter("phone", req, requirePhone, 0, 255, errors);
		String phone2 = ValidationUtils.validateParameter("phone2", req, false, 0, 255, errors);
		String organization = ValidationUtils.validateParameter("organization", req, true, 0, 255, errors);
		String title = ValidationUtils.validateParameter("title", req, false, 0, 255, errors);
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

			if (email != null) {

				User emailMatch = systemInterface.getUserHandler().getUserByEmail(email, false, false);

				if (emailMatch != null && !emailMatch.equals(user)) {
					errors.add(new ValidationError("EmailAlreadyTaken"));
				}
			}

		}

		String mobilePhone = null;

		if (phoneValidationModule != null && (isAdd || req.getParameter("changeMobilePhone") != null)) {
			mobilePhone = ValidationUtils.validateParameter("mobilePhone", req, true, 0, 255, errors);
			String verificationCode = ValidationUtils.validateParameter("verificationCode", req, true, 0, 255, errors);

			if (!StringUtils.isEmpty(mobilePhone) && !StringUtils.isEmpty(verificationCode)) {
				if (!phoneValidationModule.validateNumber(mobilePhone)) {
					errors.add(new ValidationError("mobilePhone", ValidationErrorType.InvalidFormat));
				} else if (!phoneValidationModule.validateCode(mobilePhone, verificationCode, user, req)) {
					errors.add(new ValidationError("WrongVerificationCode"));
				}
			}
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
			mutableAttributeHandler.setAttribute(CBConstants.USER_PHONE2_ATTRIBUTE, phone2);
			mutableAttributeHandler.setAttribute(CBConstants.USER_ORGANIZATION_NAME_ATTRIBUTE, organization);
			mutableAttributeHandler.setAttribute(CBConstants.USER_TITLE_ATTRIBUTE, title);

			if (phoneValidationModule != null && (isAdd || req.getParameter("changeMobilePhone") != null)) {
				mutableAttributeHandler.setAttribute(CBConstants.USER_MOBILE_PHONE_ATTRIBUTE, mobilePhone);
				mutableAttributeHandler.setAttribute(CBConstants.USER_MOBILE_PHONE_VALIDATED_ATTRIBUTE, DateUtils.DATE_TIME_FORMATTER.format(new Date()));
			}

			if (!StringUtils.isEmpty(description)) {
				mutableAttributeHandler.setAttribute("description", description);
			}

			if (setExternalUserAttribute) {
				mutableAttributeHandler.setAttribute(CBConstants.EXTERNAL_USER_ATTRIBUTE, true);
			}
		}

		if (externalUserGroup != null) {
			Group externalGroup = systemInterface.getGroupHandler().getGroup(externalUserGroup, false);

			if (user.getGroups() == null) {
				user.setGroups(new ArrayList<Group>());
			}

			if (!user.getGroups().contains(externalGroup)) {
				user.getGroups().add(externalGroup);
			}
		}

		if (user.getUserID() == null) {
			systemInterface.getUserHandler().addUser(user);
		} else {
			systemInterface.getUserHandler().updateUser(user, passwordChanged, false, true);
		}

		if (profileImage != null) {
			addProfileImage(profileImage, getCropCoordinates(req), user, true);
		} else if (BooleanUtils.toBoolean(req.getParameter("deleteProfileImage"))) {
			deleteProfileImage(req, user);
		}

		return user;
	}

	@Override
	public HttpServletRequest parseRequest(HttpServletRequest req) throws ValidationException {

		if (MultipartRequest.isMultipartRequest(req) && req.getAttribute("multipartRequest") == null) {

			try {

				MultipartRequest multipartRequest = new MultipartRequest(ramThreshold * BinarySizes.MegaByte, diskThreshold * BinarySizes.MegaByte, diskThreshold * BinarySizes.MegaByte, req);

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

	@Override
	public BufferedImage populateProfileImage(HttpServletRequest req) throws ValidationException {

		if (!(req instanceof MultipartRequest)) {

			return null;
		}

		MultipartRequest multipartRequest = (MultipartRequest) req;

		try {

			if (multipartRequest.getFileCount() > 0 && !StringUtils.isEmpty(multipartRequest.getFile(0).getName())) {

				FileItem file = multipartRequest.getFile(0);

				if (file.getSize() > maxFileSize * BinarySizes.MegaByte) {
					throw new ValidationException(new ValidationError("FileSizeLimitExceeded"));
				}

				try {
					BufferedImage bufferedImage = ImageUtils.getImage(file.get());

					if (bufferedImage == null) {
						throw new Exception();
					}

					bufferedImage = checkAndFixRotation(bufferedImage, file.getInputStream());

					return bufferedImage;

				} catch (Exception e) {

					throw new ValidationException(new ValidationError("UnableToParseProfileImage"));
				}

			}

		} finally {

			multipartRequest.deleteFiles();
		}

		return null;
	}

	protected BufferedImage addProfileImage(BufferedImage image, CropCoordinates cropCoords, User user, boolean logResults) throws IOException {

		if (cropCoords != null && cropCoords.getX2() <= image.getWidth() && cropCoords.getY2() <= image.getHeight()) {
			image = ImageUtils.crop(image, cropCoords.getX1(), cropCoords.getY1(), cropCoords.getX2(), cropCoords.getY2());
		} else {
			image = ImageUtils.cropAsSquare(image);
		}

		if (image.getWidth() > profileImageSize || image.getHeight() > profileImageSize) {

			image = ImageUtils.scale(image, profileImageSize, profileImageSize, Image.SCALE_SMOOTH, BufferedImage.TYPE_INT_ARGB);

		}

		File imageFile = new File(baseFilestore + "/" + user.getUserID() + ".png");

		boolean exists = imageFile.exists();

		if (isImageTransparent(image)) {
			image = createProfileImageWithBackground(image);
		}

		ImageIO.write(image, "png", imageFile);

		if (logResults) {

			if (exists) {
				log.info("Updated profile image for user " + user);
			} else {
				log.info("Added profile image for user " + user);
			}
		}

		return image;
	}

	private BufferedImage createProfileImageWithBackground(BufferedImage image) {

		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = newImage.createGraphics();

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());

		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		return newImage;
	}

	private boolean isImageTransparent(BufferedImage image) {

		return image.getColorModel().hasAlpha();
	}

	protected void deleteProfileImage(HttpServletRequest req, User user) throws ValidationException {

		checkConfiguration();

		if (!deleteProfileImage(user.getUserID())) {

			throw new ValidationException(new ValidationError("UnableToDeleteProfileImage"));
		}

	}

	@Override
	public boolean deleteProfileImage(int userID) {

		File profileImage = new File(baseFilestore + File.separator + userID + ".png");

		if (profileImage.exists()) {

			return profileImage.delete();
		}

		return false;
	}

	@Override
	public boolean hasProfileImage(User user) {

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

			try {
				if (profileImage.exists()) {

					HTTPUtils.sendFile(profileImage, req, res, ContentDisposition.INLINE);

				} else {

					URL url = UserProfileModule.class.getResource("staticcontent/pics/empty-profileimage.png");

					HTTPUtils.sendFile(url, "empty-profileimage.png", req, res, ContentDisposition.INLINE);

				}

			} catch (IOException e) {

				log.info("Unable to send profile image for userID " + userID + " to user " + user + ", " + e);
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
	public BufferedImage setUserProfileImage(User user, CropCoordinates cropCoordinates, BufferedImage image) throws Exception {

		checkConfiguration();

		if (image == null) {

			deleteProfileImage(user.getUserID());

			return null;
		}

		return addProfileImage(image, cropCoordinates, user, false);
	}

	@Override
	public Long getUserProfileImageLastModified(User user) {

		File imageFile = new File(baseFilestore + "/" + user.getUserID() + ".png");

		if (imageFile.exists()) {

			return imageFile.lastModified();
		}

		return null;
	}

	@Override
	public boolean isRequirePhone() {

		return requirePhone;
	}
}

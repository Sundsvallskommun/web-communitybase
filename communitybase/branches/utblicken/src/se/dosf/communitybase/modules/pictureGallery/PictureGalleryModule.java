package se.dosf.communitybase.modules.pictureGallery;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import se.dosf.communitybase.annotations.GroupMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.enums.RelationType;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.pictureGallery.beans.Gallery;
import se.dosf.communitybase.modules.pictureGallery.beans.Picture;
import se.dosf.communitybase.modules.pictureGallery.beans.PictureComment;
import se.dosf.communitybase.modules.pictureGallery.daos.PictureCommentDao;
import se.dosf.communitybase.modules.pictureGallery.daos.PictureDao;
import se.dosf.communitybase.modules.pictureGallery.daos.PictureGalleryModuleDAO;
import se.dosf.communitybase.modules.pictureGallery.populators.PictureGalleryPopulator;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.URLType;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.foregroundmodules.ModuleSetting;
import se.unlogic.hierarchy.foregroundmodules.imagegallery.SimpleFileFilter;
import se.unlogic.standardutils.image.ImageUtils;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class PictureGalleryModule extends AnnotatedCommunityModule implements CommunityModule {

	private static final ArrayList<SettingDescriptor> SETTINGDESCRIPTORS = new ArrayList<SettingDescriptor>();

	static {
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("numOfThumbsPerPage", "Thumbnails per page", "The number of thumbnails per page (default is 15)", false, "15", new StringIntegerValidator(1,null)));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("smallImageMaxHeight", "Small image max height ", "The max height of the small thumbnails (default is 93)", false, "93", new StringIntegerValidator(1,null)));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("smallImageMaxWidth", "Small image max width ", "The max width of the small thumbnails (default is 125)", false, "125", new StringIntegerValidator(1,null)));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("mediumImageMaxHeight", "Medium image max height ", "The max height of the medium thumbnails (default is 500)", false, "500", new StringIntegerValidator(1,null)));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("mediumImageMaxWidth", "Medium image max width ", "The max width of the medium thumbnails (default is 500)", false, "500", new StringIntegerValidator(1,null)));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createCheckboxSetting("allowsComments", "Allow comments", "Control wheter or not comments is used", false));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("diskThreshold", "Max upload size", "Maxmium upload size in megabytes allowed in a single post request", false, "100", new StringIntegerValidator(1,null)));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("ramThreshold", "RAM threshold", "How many megabytes of RAM to use as buffer during file uploads. If the threshold is exceeded the files are written to disk instead.", false, "20", new StringIntegerValidator(1,null)));
	}

	private PictureGalleryModuleDAO galleryDAO;
	private PictureDao pictureDao;
	private PictureCommentDao commentDao;
	private static PictureGalleryPopulator GalleryPopulator = new PictureGalleryPopulator(null);

	protected static final SimpleFileFilter fileFilter = new SimpleFileFilter();
	protected UserHandler userHandler;

	@ModuleSetting
	protected Integer numOfThumbsPerPage = 15;

	@ModuleSetting
	protected Integer smallImageMaxHeight = 93;

	@ModuleSetting
	protected Integer smallImageMaxWidth = 125;

	@ModuleSetting
	protected Integer mediumImageMaxHeight = 500;

	@ModuleSetting
	protected Integer mediumImageMaxWidth = 500;

	@ModuleSetting
	protected Boolean allowsComments = false;

	@ModuleSetting
	protected Integer diskThreshold = 100;

	@ModuleSetting
	protected Integer ramThreshold = 20;

	@ModuleSetting
	protected Integer thumbQuality = Image.SCALE_SMOOTH;

	@XSLVariable
	protected String addGalleryBreadCrumb = "Add gallery";

	@XSLVariable
	protected String addImagesBreadCrumb = "Add pictures";

	@XSLVariable
	protected String editGalleryBreadCrumb = "Update gallery";

	@XSLVariable
	protected String newGalleryText = "New gallery: ";

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		this.userHandler = sectionInterface.getSystemInterface().getUserHandler();

		this.galleryDAO = new PictureGalleryModuleDAO(dataSource);
		this.pictureDao = new PictureDao(dataSource);
		this.commentDao = new PictureCommentDao(dataSource, new CommunityUserDAO(dataSource));
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);
		this.galleryDAO = new PictureGalleryModuleDAO(dataSource);
		this.pictureDao = new PictureDao(dataSource);
		this.commentDao = new PictureCommentDao(dataSource, new CommunityUserDAO(dataSource));
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		log.info("User " + user + " listing galleries in group " + group);

		Document doc = this.createDocument(req, uriParser, group, user);
		Element picturegallery = doc.createElement("pictureGallery");
		doc.getFirstChild().appendChild(picturegallery);

		if(user.getLastLogin() != null){
			picturegallery.appendChild(XMLUtils.createElement("userLastLoginInMillis", String.valueOf(user.getLastLogin().getTime()), doc));
		}

		// get all galleries in this group
		Element galleriesElement = doc.createElement("galleries");
		picturegallery.appendChild(galleriesElement);

		ArrayList<Gallery> galleries = this.galleryDAO.getAllGalleries(group, true);

		if (galleries != null) {
			for (Gallery gallery : galleries) {
				Element galleryElement = gallery.toXML(doc);
				galleriesElement.appendChild(galleryElement);

				ArrayList<Picture> galleryPictures = gallery.getPictures();
				// get random image from gallery.
				if (galleryPictures != null && !galleryPictures.isEmpty()) {
					Random rand = new Random();
					int idx = rand.nextInt(galleryPictures.size());
					galleryElement.appendChild(XMLUtils.createElement("randomFile", galleryPictures.get(idx).getFileID().toString(), doc));
					galleryElement.appendChild(XMLUtils.createElement("numPics", String.valueOf(galleryPictures.size()), doc));
				} else {
					galleryElement.appendChild(XMLUtils.createElement("numPics", "0", doc));
				}
			}
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), getModuleBreadcrumb(group));
	}

	@GroupMethod
	public ForegroundModuleResponse addGroupGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.addGallery(req, res, user, uriParser, group, RelationType.GROUP);

	}

	@GroupMethod
	public ForegroundModuleResponse addSchoolGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.addGallery(req, res, user, uriParser, group, RelationType.SCHOOL);

	}

	private ForegroundModuleResponse addGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, RelationType type) throws Exception {

		this.checkAdminAccess(user, group);

		ValidationException validationException = null;

		MultipartRequest requestWrapper = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {
			try {

				requestWrapper = new MultipartRequest(this.ramThreshold * BinarySizes.MegaByte, this.diskThreshold * BinarySizes.MegaByte, req);

				Integer relationID = type.equals(RelationType.GROUP) ? group.getGroupID() : group.getSchool().getSchoolID();

				Gallery gallery = GalleryPopulator.populate(requestWrapper, relationID);

				gallery.setRelationType(type);

				log.info("User " + user + " adding gallery " + gallery);

				Integer sectionID = this.galleryDAO.add(gallery);
				gallery.setSectionID(sectionID);

				Boolean zipUpload = requestWrapper.getParameter("uploadCheck") != null;

				if (zipUpload) {

					try {

						FileItem fileItem = requestWrapper.getFile(0);

						ArrayList<Picture> pictures = this.uploadGalleryZip(fileItem, gallery, user);

						this.pictureDao.addAll(pictures, type);

						log.info("User " + user + " added " + pictures.size() + " images to gallery " + gallery);

					} catch (FileUploadException e) {

						throw new ValidationException(new ValidationError("UnableToParseRequest"));

					}catch(ZipException e){

						throw new ValidationException(new ValidationError("UnableToParseRequest"));

					}finally {
						if (requestWrapper != null) {
							requestWrapper.deleteFiles();
						}
					}

				}

				res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID() + "/show" + type + "Gallery/" + sectionID);
				return null;

			} catch (ValidationException e) {
				validationException = e;
			}

		}

		Document doc = this.createDocument(req, uriParser, group, user);
		Element addGalleryElement = doc.createElement("addGallery");
		doc.getFirstChild().appendChild(addGalleryElement);
		addGalleryElement.appendChild(XMLUtils.createElement("relationType", type.name(), doc));

		if (validationException != null) {
			addGalleryElement.appendChild(validationException.toXML(doc));
			addGalleryElement.appendChild(RequestUtils.getRequestParameters(requestWrapper, doc));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), getModuleBreadcrumb(group), new Breadcrumb(addGalleryBreadCrumb, addGalleryBreadCrumb, "#"));

	}

	@GroupMethod
	public ForegroundModuleResponse updateGroupGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws NumberFormatException, URINotFoundException, SQLException, IOException, AccessDeniedException, DOMException {

		return this.updateGallery(req, res, user, uriParser, group, RelationType.GROUP);

	}

	@GroupMethod
	public ForegroundModuleResponse updateSchoolGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws NumberFormatException, URINotFoundException, SQLException, IOException, AccessDeniedException {

		return this.updateGallery(req, res, user, uriParser, group, RelationType.SCHOOL);

	}

	private ForegroundModuleResponse updateGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, RelationType type) throws AccessDeniedException, NumberFormatException, URINotFoundException, DOMException, SQLException, IOException {

		this.checkAdminAccess(user, group);

		Gallery gallery = null;

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3)) || (gallery = this.galleryDAO.getGallery(Integer.valueOf(uriParser.get(3)), false, type)) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			ValidationException validationException = null;

			if (req.getMethod().equalsIgnoreCase("POST")) {
				try {
					gallery = GalleryPopulator.populate(gallery, req);

					gallery.setRelationType(type);

					log.info("User " + user + " updating gallery " + gallery);

					this.galleryDAO.update(gallery);

					res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID());
					return null;

				} catch (ValidationException e) {
					validationException = e;
				}
			}

			Document doc = this.createDocument(req, uriParser, group, user);

			Element updateGalleryElement = doc.createElement("updateGallery");
			doc.getFirstChild().appendChild(updateGalleryElement);

			updateGalleryElement.appendChild(gallery.toXML(doc));

			if (validationException != null) {
				updateGalleryElement.appendChild(validationException.toXML(doc));
				updateGalleryElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getGroupBreadcrumb(group), getModuleBreadcrumb(group), new Breadcrumb(editGalleryBreadCrumb, editGalleryBreadCrumb, "#"));

		}

	}

	@GroupMethod
	public ForegroundModuleResponse deleteGroupGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws NumberFormatException, URINotFoundException, AccessDeniedException, SQLException, IOException {

		return this.deleteGallery(req, res, user, uriParser, group, RelationType.GROUP);

	}

	@GroupMethod
	public ForegroundModuleResponse deleteSchoolGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws NumberFormatException, URINotFoundException, AccessDeniedException, SQLException, IOException {

		return this.deleteGallery(req, res, user, uriParser, group, RelationType.SCHOOL);

	}

	private ForegroundModuleResponse deleteGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, RelationType type) throws NumberFormatException, URINotFoundException, SQLException, IOException, AccessDeniedException {

		this.checkAdminAccess(user, group);

		Gallery gallery = null;

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3)) || (gallery = this.galleryDAO.getGallery(Integer.valueOf(uriParser.get(3)), false, type)) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			log.info("User " + user + " deleting gallery " + gallery);

			this.galleryDAO.delete(gallery);

			this.redirectToDefaultMethod(req, res, group);

			return null;
		}

	}

	@GroupMethod
	public ForegroundModuleResponse showGroupGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		return this.showGallery(req, res, user, uriParser, group, RelationType.GROUP);

	}

	@GroupMethod
	public ForegroundModuleResponse showSchoolGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		return this.showGallery(req, res, user, uriParser, group, RelationType.SCHOOL);

	}

	private ForegroundModuleResponse showGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, RelationType type) throws URINotFoundException, NumberFormatException, SQLException {

		Gallery gallery = null;

		if (uriParser.size() < 4 || uriParser.size() > 5 || !NumberUtils.isInt(uriParser.get(3)) || (gallery = this.galleryDAO.getGallery(Integer.valueOf(uriParser.get(3)), true, type)) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			Document doc = this.createDocument(req, uriParser, group, user);

			Element showGallery = doc.createElement("showGallery");
			doc.getFirstChild().appendChild(showGallery);

			Element galleryElement = gallery.toXML(doc);
			showGallery.appendChild(galleryElement);

			Integer page = 1;

			if (uriParser.size() == 5) {
				page = NumberUtils.toInt(uriParser.get(4));

				if (page == null || page <= 0) {
					throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
				}
			}

			int num = gallery.getPictures() != null ? gallery.getPictures().size() : 0;
			int pagesInGallery = num % numOfThumbsPerPage == 0 ? num / numOfThumbsPerPage : (num / numOfThumbsPerPage) + 1;

			ArrayList<Picture> pictures = gallery.getPictures();

			if (num == 0) {

				galleryElement.appendChild(XMLUtils.createElement("pages", "1", doc));
				galleryElement.appendChild(XMLUtils.createElement("currentPage", "1", doc));
				galleryElement.appendChild(XMLUtils.createElement("numPics", "0", doc));

				return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getGroupBreadcrumb(group), getModuleBreadcrumb(group), new Breadcrumb(gallery.getSectionName(), gallery.getDescription(), "#"));

			} else if (page <= pagesInGallery) {

				// find next and previous page
				Integer nextPage = page == pagesInGallery ? null : page + 1;
				Integer prevPage = page == 1 ? null : page - 1;

				// calculate start- and endindex
				int startIndex = (numOfThumbsPerPage * page) - numOfThumbsPerPage;
				int endIndex = startIndex + numOfThumbsPerPage;
				if (page == pagesInGallery) {
					endIndex = pictures.size();
				}

				Element filesElement = doc.createElement("files");

				galleryElement.appendChild(XMLUtils.createElement("pages", String.valueOf(pagesInGallery), doc));
				galleryElement.appendChild(XMLUtils.createElement("currentPage", String.valueOf(page), doc));
				galleryElement.appendChild(XMLUtils.createElement("numPics", String.valueOf(num), doc));

				if (nextPage != null) {
					galleryElement.appendChild(XMLUtils.createElement("nextPage", nextPage.toString(), doc));
				}
				if (prevPage != null) {
					galleryElement.appendChild(XMLUtils.createElement("prevPage", prevPage.toString(), doc));
				}

				// find images for requested page
				for (int i = startIndex; i < endIndex; i++) {

					Picture picture = pictures.get(i);

					Element fileElement = doc.createElement("file");
					Integer fileID = picture.getFileID();
					String filename = picture.getFilename();

					fileElement.appendChild(XMLUtils.createElement("fileID", fileID.toString(), doc));
					fileElement.appendChild(XMLUtils.createElement("filename", filename, doc));

					filesElement.appendChild(fileElement);

				}

				galleryElement.appendChild(filesElement);

				return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getGroupBreadcrumb(group), getModuleBreadcrumb(group), new Breadcrumb(gallery.getSectionName(), gallery.getDescription(), "#"));

			} else {
				throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
			}
		}

	}

	@GroupMethod
	public ForegroundModuleResponse multiGroupUploader(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {
		return this.multiUploader(req, res, user, uriParser, group, RelationType.GROUP);
	}

	@GroupMethod
	public ForegroundModuleResponse multiSchoolUploader(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {
		return this.multiUploader(req, res, user, uriParser, group, RelationType.SCHOOL);
	}

	private ForegroundModuleResponse multiUploader(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, RelationType type) throws Exception {

		Gallery gallery = null;

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3)) || (gallery = this.galleryDAO.getGallery(Integer.valueOf(uriParser.get(3)), false, type)) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			Document doc = this.createDocument(req, uriParser, group, user);
			Element uploadElement = doc.createElement("multiUploader");
			doc.getFirstChild().appendChild(uploadElement);

			uploadElement.appendChild(gallery.toXML(doc));

			uploadElement.appendChild(XMLUtils.createElement("sessionID", req.getSession().getId(), doc));

			return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getGroupBreadcrumb(group), getModuleBreadcrumb(group), getMethodBreadcrumb(gallery.getSectionName(), gallery.getDescription(), "show" + type + "Gallery/" + gallery.getSectionID(), group), new Breadcrumb(addImagesBreadCrumb, addImagesBreadCrumb, "#"));

		}

	}

	@GroupMethod
	public ForegroundModuleResponse uploadGroup(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {
		return this.upload(req, res, user, uriParser, group, RelationType.GROUP);
	}

	@GroupMethod
	public ForegroundModuleResponse uploadSchool(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {
		return this.upload(req, res, user, uriParser, group, RelationType.SCHOOL);
	}

	private ForegroundModuleResponse upload(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, RelationType type) throws Exception {

		Gallery gallery = null;

		if (uriParser.size() < 4 || !NumberUtils.isInt(uriParser.get(3)) || (gallery = this.galleryDAO.getGallery(Integer.valueOf(uriParser.get(3)), false, type)) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			try {

				MultipartRequest requestWrapper = new MultipartRequest(this.ramThreshold * BinarySizes.MegaByte, this.diskThreshold * BinarySizes.MegaByte, req);

				FileItem fileItem = requestWrapper.getFile(0);

				if (fileItem != null) {

					Picture picture = this.createPicture(fileItem.getInputStream(), FilenameUtils.getName(fileItem.getName()), gallery, user);

					this.pictureDao.add(picture, type);

					log.info("User " + user + " added 1 image to gallery " + gallery);

				}

			}catch (FileUploadException e) {

				log.info("Error uploading image in gallery " + gallery + " caused by read time out, uploaded by user " + user + " using multiuploader");

			}catch (ValidationException e) {

				log.info("Error uploading image in gallery " + gallery + " caused by parsing problem, uploaded by user " + user + " using multiuploader");

			}

			return null;

		}

	}

	@GroupMethod
	public ForegroundModuleResponse simpleGroupUploader(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {
		return this.simpleUploader(req, res, user, uriParser, group, RelationType.GROUP);
	}

	@GroupMethod
	public ForegroundModuleResponse simpleSchoolUploader(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {
		return this.simpleUploader(req, res, user, uriParser, group, RelationType.SCHOOL);
	}

	private ForegroundModuleResponse simpleUploader(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, RelationType type) throws Exception {

		this.checkAdminAccess(user, group);

		Gallery gallery = null;

		if (uriParser.size() < 4 || !NumberUtils.isInt(uriParser.get(3)) || (gallery = this.galleryDAO.getGallery(Integer.valueOf(uriParser.get(3)), false, type)) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			ValidationException validationException = null;

			if (req.getMethod().equalsIgnoreCase("POST")) {

				MultipartRequest requestWrapper = null;

				try {
					requestWrapper = new MultipartRequest(this.ramThreshold * BinarySizes.MegaByte, this.diskThreshold * BinarySizes.MegaByte, req);

					FileItem fileItem = requestWrapper.getFile(0);

					if (fileItem.getName() != null && fileItem.getName().toLowerCase().endsWith(".zip")) {

						try {

							log.info("User " + user + " adding images from zip file to gallery " + gallery);

							ArrayList<Picture> pictures = this.uploadGalleryZip(fileItem, gallery, user);

							this.pictureDao.addAll(pictures, type);

							log.info("User " + user + " added " + pictures.size() + " images to gallery " + gallery);

						} catch (FileUploadException e) {
							throw new ValidationException(new ValidationError("UnableToParseRequest"));
						} catch (ZipException e) {
							throw new ValidationException(new ValidationError("UnableToParseRequest"));
						}

					} else if (fileFilter.accept(fileItem)) {

						log.info("User " + user + " adding image to gallery " + gallery);

						Picture picture = this.createPicture(fileItem.getInputStream(), FilenameUtils.getName(fileItem.getName()), gallery, user);

						this.pictureDao.add(picture, type);

						log.info("User " + user + " added 1 image to gallery " + gallery);

					} else if(StringUtils.isEmpty(fileItem.getName())){
						throw new ValidationException(new ValidationError("NoImage"));
					} else {
						throw new ValidationException(new ValidationError("UnableToParseRequest"));
					}

					res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID() + "/show" + type + "Gallery/" + gallery.getSectionID());
					return null;

				} catch (ValidationException e) {
					validationException = e;
				} catch (FileUploadException e) {

					log.info("Error uploading image in gallery " + gallery + " caused by read time out, uploaded by user " + user + " using simpleuploader");

				}finally {
					if (requestWrapper != null) {
						requestWrapper.deleteFiles();
					}
				}
			}

			Document doc = this.createDocument(req, uriParser, group, user);

			Element addImageElement = doc.createElement("addImages");
			doc.getFirstChild().appendChild(addImageElement);
			addImageElement.appendChild(gallery.toXML(doc));

			if (uriParser.get(4) != null && uriParser.get(4).equals("noflash")) {
				addImageElement.appendChild(XMLUtils.createElement("noflash", "true", doc));
			}

			if (validationException != null) {
				addImageElement.appendChild(validationException.toXML(doc));
				addImageElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getGroupBreadcrumb(group), getModuleBreadcrumb(group), getMethodBreadcrumb(gallery.getSectionName(), gallery.getDescription(), "show" + type + "Gallery/" + gallery.getSectionID(), group), new Breadcrumb(addImagesBreadCrumb, addImagesBreadCrumb, "#"));
		}

	}

	@GroupMethod
	public ForegroundModuleResponse showGroupImage(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		return this.showImage(req, res, user, uriParser, group, RelationType.GROUP);

	}

	@GroupMethod
	public ForegroundModuleResponse showSchoolImage(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		return this.showImage(req, res, user, uriParser, group, RelationType.SCHOOL);

	}

	private ForegroundModuleResponse showImage(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, RelationType type) throws URINotFoundException, NumberFormatException, DOMException, SQLException, IOException {

		Gallery gallery = null;

		if (uriParser.size() != 5 || !NumberUtils.isInt(uriParser.get(3)) || !NumberUtils.isInt(uriParser.get(4)) || (gallery = this.galleryDAO.getGallery(Integer.valueOf(uriParser.get(3)), true, type)) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			Integer fileID = Integer.valueOf(uriParser.get(4));
			Picture picture = null;

			boolean found = false;
			int currentIdx = -1;

			ArrayList<Picture> pictures = gallery.getPictures();

			for (int i = 0; i < pictures.size(); i++) {

				picture = pictures.get(i);

				if (picture.getFileID().equals(fileID)) {
					found = true;
					currentIdx = i;
					break;
				}

			}
			int picsInGallery = pictures.size();

			if (found) {

				log.info("User " + user + " requested image " + picture + " in gallery " + gallery);

				// find next and previous picture
				Integer nextPicture = currentIdx < picsInGallery - 1 ? pictures.get(currentIdx + 1).getFileID() : null;
				Integer prevPicture = currentIdx > 0 ? pictures.get(currentIdx - 1).getFileID() : null;

				// create XML-document containing information about the requested gallery and images
				Document doc = this.createDocument(req, uriParser, group, user);

				Element pictureElement = doc.createElement("showImage");
				doc.getFirstChild().appendChild(pictureElement);

				pictureElement.appendChild(XMLUtils.createElement("allowComments", this.allowsComments == true ? "true" : "false", doc));

				Node gNode = gallery.toXML(doc);
				gNode.appendChild(XMLUtils.createElement("numPics", String.valueOf(picsInGallery), doc));
				gNode.appendChild(XMLUtils.createElement("currentPic", String.valueOf(currentIdx + 1), doc));

				if (nextPicture != null) {
					gNode.appendChild(XMLUtils.createElement("nextImage", nextPicture.toString(), doc));
				}
				if (prevPicture != null) {
					gNode.appendChild(XMLUtils.createElement("prevImage", prevPicture.toString(), doc));
				}

				Element fileElement = doc.createElement("file");
				fileElement.appendChild(XMLUtils.createElement("fileID", picture.getFileID().toString(), doc));
				fileElement.appendChild(XMLUtils.createCDATAElement("filename", picture.getFilename(), doc));

				if (this.allowsComments) {
					this.useComments(req, res, user, uriParser, group, doc, fileElement, gallery, fileID, type);
				}

				gNode.appendChild(fileElement);
				pictureElement.appendChild(gNode);

				return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getGroupBreadcrumb(group), getModuleBreadcrumb(group), getMethodBreadcrumb(gallery.getSectionName(), gallery.getDescription(), "show" + type + "Gallery/" + gallery.getSectionID(), group), new Breadcrumb(picture.getFilename(), picture.getDescription(), "#"));

			} else {
				throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
			}
		}

	}

	private void useComments(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, Document doc, Element fileElement, Gallery gallery, Integer fileID, RelationType type) throws IOException, SQLException {

		Element commentsElement = doc.createElement("comments");
		ArrayList<PictureComment> comments = this.commentDao.getByFileID(fileID, type);

		HttpSession session = req.getSession(true);

		if (req.getMethod().equalsIgnoreCase("POST")) {

			String commentStatus = req.getParameter("viewComments");

			if (commentStatus != null) {
				if (commentStatus.equals("true")) {
					session.setAttribute(this.moduleDescriptor.getModuleID() + ".showAll", true);
				} else {
					session.setAttribute(this.moduleDescriptor.getModuleID() + ".showAll", false);
				}
			}

			String commentText = req.getParameter("commentText");
			if (commentText != null) {
				try {
					if (StringUtils.isEmpty(commentText)) {
						throw new ValidationException(new ValidationError("commentText", ValidationErrorType.RequiredField));
					}

					PictureComment comment = new PictureComment();
					comment.setComment(commentText);
					comment.setDate(new Timestamp(System.currentTimeMillis()));
					comment.setFileID(fileID);

					if (user != null) {
						comment.setUser(user);
					}

					commentDao.add(comment, type);

					res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID() + "/show" + this.getRelationType(type) + "Image/" + gallery.getSectionID() + "/" + fileID);
				} catch (ValidationException e) {
					fileElement.appendChild(e.toXML(doc));
				}
			}
		}

		// check if user want´s to show all comments or not
		Boolean showAll = (Boolean) session.getAttribute(this.moduleDescriptor.getModuleID() + ".showAll");
		if (showAll == null || showAll) {
			commentsElement.appendChild(XMLUtils.createElement("showAll", "true", doc));

			if (comments != null) {

				for (PictureComment comment : comments) {
					commentsElement.appendChild(comment.toXML(doc));
				}
			}
		}

		if (comments != null) {
			commentsElement.appendChild(XMLUtils.createElement("commentsNum", String.valueOf(comments.size()), doc));
		}

		fileElement.appendChild(commentsElement);

		if (se.dosf.communitybase.utils.AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN)) {
			fileElement.appendChild(doc.createElement("commentsAllowed"));
		}

	}

	@GroupMethod
	public ForegroundModuleResponse updateGroupComment(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		return this.updateComment(req, res, user, uriParser, group, RelationType.GROUP);

	}

	@GroupMethod
	public ForegroundModuleResponse updateSchoolComment(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		return this.updateComment(req, res, user, uriParser, group, RelationType.SCHOOL);

	}

	private ForegroundModuleResponse updateComment(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, RelationType type) throws IOException, SQLException, URINotFoundException, AccessDeniedException {

		this.checkAdminAccess(user, group);

		PictureComment comment = null;

		if (uriParser.size() < 4 || !NumberUtils.isInt(uriParser.get(3)) || (comment = this.commentDao.get(Integer.parseInt((uriParser.get(3))), type)) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			String commentText = req.getParameter("comment");

			if (!StringUtils.isEmpty(commentText)) {

				log.info("User " + user + " updating comment " + comment + " new text " + commentText);

				comment.setComment(commentText);

				this.commentDao.update(comment, type);
			}

			Picture picture = this.pictureDao.get(comment.getFileID(), false, false, false, type);

			Gallery gallery = this.galleryDAO.getGallery(picture.getSectionID(), false, type);

			res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID() + "/show" + this.getRelationType(type) + "Image/" + gallery.getSectionID() + "/" + picture.getFileID());

			return null;
		}
	}

	@GroupMethod
	public ForegroundModuleResponse deleteGroupComment(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		return this.deleteComment(req, res, user, uriParser, group, RelationType.GROUP);

	}

	@GroupMethod
	public ForegroundModuleResponse deleteSchoolComment(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		return this.deleteComment(req, res, user, uriParser, group, RelationType.SCHOOL);

	}

	private ForegroundModuleResponse deleteComment(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, RelationType type) throws IOException, SQLException, URINotFoundException, AccessDeniedException {

		this.checkAdminAccess(user, group);

		PictureComment comment = null;

		if (uriParser.size() < 4 || !NumberUtils.isInt(uriParser.get(3)) || (comment = this.commentDao.get(Integer.parseInt((uriParser.get(3))), type)) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			log.info("User " + user + " deleting comment " + comment);

			Picture picture = this.pictureDao.get(comment.getFileID(), false, false, false, type);

			Gallery gallery = this.galleryDAO.getGallery(picture.getSectionID(), false, type);

			this.commentDao.delete(comment, type);

			res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID() + "/show" + this.getRelationType(type) + "Image/" + gallery.getSectionID() + "/" + picture.getFileID());

			return null;
		}
	}

	@GroupMethod
	public ForegroundModuleResponse deleteGroupImage(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException, ValidationException {

		return this.deleteImage(req, res, user, uriParser, group, RelationType.GROUP);

	}

	@GroupMethod
	public ForegroundModuleResponse deleteSchoolImage(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException, ValidationException {

		return this.deleteImage(req, res, user, uriParser, group, RelationType.SCHOOL);

	}

	private ForegroundModuleResponse deleteImage(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, RelationType type) throws IOException, SQLException, URINotFoundException, ValidationException, AccessDeniedException {

		this.checkAdminAccess(user, group);

		Gallery gallery = null;

		if (uriParser.size() != 5 || !NumberUtils.isInt(uriParser.get(3)) || !NumberUtils.isInt(uriParser.get(4)) || (gallery = this.galleryDAO.getGallery(Integer.valueOf(uriParser.get(3)), false, type)) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			Integer fileID = Integer.valueOf(uriParser.get(4));

			Picture picture = pictureDao.get(fileID, false, false, false, type);

			if (picture != null) {

				log.info("User " + user + " deleting image " + picture);

				pictureDao.delete(picture, type);

				res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID() + "/show" + this.getRelationType(type) + "Gallery/" + gallery.getSectionID());

			} else {
				throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
			}

		}

		return null;
	}

	protected ArrayList<Picture> uploadGalleryZip(FileItem fileItem, Gallery gallery, CommunityUser user) throws Exception {

		File file = null;

		ArrayList<Picture> pictures = new ArrayList<Picture>();

		try {

			file = File.createTempFile("galleryupload-" + System.currentTimeMillis(), ".zip");
			fileItem.write(file);
			ZipFile zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> e = zipFile.entries();

			while (e.hasMoreElements()) {
				ZipEntry ze = e.nextElement();

				if (!ze.isDirectory() && SimpleFileFilter.isValidFilename(ze.getName())) {

					String filename = FilenameUtils.getName(ze.getName());

					log.info("Adding file " + filename + " to gallery " + gallery);

					InputStream zipEntryInputStream = zipFile.getInputStream(ze);

					pictures.add(this.createPicture(zipEntryInputStream, filename, gallery, user));

					if (zipEntryInputStream != null) {
						zipEntryInputStream.close();
					}
				}
			}

		} finally {
			if (file != null) {
				file.delete();
			}
		}

		return pictures;

	}

	@GroupMethod
	public ForegroundModuleResponse smallGroupThumb(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		return this.smallThumb(req, res, user, uriParser, group, RelationType.GROUP);

	}

	@GroupMethod
	public ForegroundModuleResponse smallSchoolThumb(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		return this.smallThumb(req, res, user, uriParser, group, RelationType.SCHOOL);

	}

	@WebPublic
	public ForegroundModuleResponse smallThumb(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, CommunityGroup group, RelationType type) throws IOException, SQLException, URINotFoundException, AccessDeniedException {

		Gallery gallery = null;

		// check that the requested gallery exist
		if (uriParser.size() != 5 || !NumberUtils.isInt(uriParser.get(3)) || !NumberUtils.isInt(uriParser.get(4)) || (gallery = this.galleryDAO.getGallery(Integer.valueOf(uriParser.get(3)), false, type)) == null) {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
		} else {

			Integer pictureID = Integer.valueOf(uriParser.get(4));

			log.debug("User " + user + " requesting small thumb of image " + pictureID + " in gallery " + gallery);

			Picture picture = pictureDao.get(pictureID, true, false, false, type);

			if (picture != null) {

				try {
					writePicture(picture.getSmallPic(), picture.getFilename(), res);
				} catch (IOException e) {
					log.info("Caught exception " + e + " while sending picture " + picture + " in gallery " + gallery + " to " + user);
				}

			} else {
				throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
			}

		}

		return null;
	}

	@GroupMethod
	public ForegroundModuleResponse mediumGroupThumb(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		return this.mediumThumb(req, res, user, uriParser, group, RelationType.GROUP);

	}

	@GroupMethod
	public ForegroundModuleResponse mediumSchoolThumb(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		return this.mediumThumb(req, res, user, uriParser, group, RelationType.SCHOOL);

	}

	private ForegroundModuleResponse mediumThumb(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, RelationType type) throws IOException, SQLException, URINotFoundException, AccessDeniedException {

		Gallery gallery = null;

		// check that the requested gallery exist
		if (uriParser.size() != 5 || !NumberUtils.isInt(uriParser.get(3)) || !NumberUtils.isInt(uriParser.get(4)) || (gallery = this.galleryDAO.getGallery(Integer.valueOf(uriParser.get(3)), false, type)) == null) {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
		} else {

			Integer fileID = Integer.valueOf(uriParser.get(4));

			Picture picture = pictureDao.get(fileID, false, true, false, type);

			if (picture != null) {

				log.debug("User " + user + " requesting medium thumb of image " + picture + " in gallery " + gallery);

				try {
					writePicture(picture.getMediumPic(), picture.getFilename(), res);
				} catch (IOException e) {
					log.info("Caught exception " + e + " while sending picture " + picture + " in gallery " + gallery + " to " + user);
				}

			} else {
				log.info("The picture with fileID " + fileID + " does not exist in gallery " + gallery);
				throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
			}
		}

		return null;
	}

	@GroupMethod
	public ForegroundModuleResponse getGroupImage(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		return this.getImage(req, res, user, uriParser, group, RelationType.GROUP);

	}

	@GroupMethod
	public ForegroundModuleResponse getSchoolImage(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		return this.getImage(req, res, user, uriParser, group, RelationType.SCHOOL);

	}

	private ForegroundModuleResponse getImage(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, RelationType type) throws IOException, SQLException, URINotFoundException, AccessDeniedException {

		Gallery gallery = null;

		// check that the requested gallery exist
		if (uriParser.size() != 5 || !NumberUtils.isInt(uriParser.get(3)) || !NumberUtils.isInt(uriParser.get(4)) || (gallery = this.galleryDAO.getGallery(Integer.valueOf(uriParser.get(3)), false, type)) == null) {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
		} else {

			Integer fileID = Integer.valueOf(uriParser.get(4));

			Picture picture = this.pictureDao.get(fileID, false, false, true, type);

			if (picture != null) {

				try {

					writePicture(picture.getLargePic(), picture.getFilename(), res);

				} catch (IOException e) {
					log.info("Caught exception " + e + " while sending picture " + picture + " in gallery " + gallery + " to " + user);
				}

			} else {
				log.info("The picture with fileID " + fileID + " in gallery " + gallery + " requested by user " + user + " does not exist");
				throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
			}
		}

		return null;

	}

	protected Picture createPicture(InputStream inputStream, String filename, Gallery gallery, CommunityUser user) throws SQLException, IOException, ValidationException {

		log.info("Creating thumbs for picture " + filename + " in gallery " + gallery);

		Picture picture = new Picture();

		BufferedImage image = ImageUtils.getImage(inputStream);

		if(image != null){

			BufferedImage smallImage = ImageUtils.scaleImage(image, this.smallImageMaxHeight, this.smallImageMaxWidth, thumbQuality,BufferedImage.TYPE_INT_RGB);
			BufferedImage mediumImage = ImageUtils.scaleImage(image, this.mediumImageMaxHeight, this.mediumImageMaxWidth, thumbQuality,BufferedImage.TYPE_INT_RGB);

			ByteArrayOutputStream smallThumbStream = new ByteArrayOutputStream();

			ImageIO.write(smallImage, "jpg", smallThumbStream);

			byte[] smallThumbByteArray = smallThumbStream.toByteArray();

			ByteArrayOutputStream mediumThumbStream = new ByteArrayOutputStream();

			ImageIO.write(mediumImage, "jpg", mediumThumbStream);

			byte[] mediumThumbByteArray = mediumThumbStream.toByteArray();

			ByteArrayOutputStream largeThumbStream = new ByteArrayOutputStream();

			ImageIO.write(image, "jpg", largeThumbStream);

			byte[] largeThumbByteArray = largeThumbStream.toByteArray();

			picture.setSmallPic(new SerialBlob(smallThumbByteArray));
			picture.setMediumPic(new SerialBlob(mediumThumbByteArray));
			picture.setLargePic(new SerialBlob(largeThumbByteArray));

			picture.setFilename(filename);
			picture.setSectionID(gallery.getSectionID());
			picture.setPostedBy(user.getUserID());

			picture.setContentTypeSmall("image/jpeg");
			picture.setContentTypeMedium("image/jpeg");
			picture.setContentTypeLarge("image/jpeg");

			return picture;

		}else{
			throw new ValidationException(new ValidationError("UnableToParseRequest"));
		}

	}

	public static void writePicture(Blob blob, String filename, HttpServletResponse res) throws SQLException, IOException {

		// send thumb to user
		HTTPUtils.setContentLength(blob.length(), res);

		res.setContentType("image/jpeg");
		res.setHeader("Content-Disposition", "inline; filename=" + filename);

		InputStream in = blob.getBinaryStream();
		OutputStream out = res.getOutputStream();

		byte[] buf = new byte[1024];
		int count = 0;

		while ((count = in.read(buf)) >= 0) {
			out.write(buf, 0, count);
		}
		in.close();
		out.close();

	}

	private Breadcrumb getMethodBreadcrumb(String name, String description, String method, CommunityGroup group) {
		return new Breadcrumb(name, description, this.getFullAlias() + "/" + group.getGroupID() + "/" + method, URLType.RELATIVE_FROM_CONTEXTPATH);
	}

	public List<IdentifiedEvent> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws SQLException {

		List<IdentifiedEvent> groupEvents = this.galleryDAO.getEvents(startStamp,"picturegallerygroupsections","groupID",group.getGroupID());

		if(groupEvents != null){

			for(IdentifiedEvent event : groupEvents){
				event.setTitle(this.newGalleryText + StringUtils.substring(event.getTitle(), 50, "..."));
				event.setFullAlias(this.getFullAlias(group) + "/showGroupGallery/" + event.getId());
			}
		}

		List<IdentifiedEvent> schoolEvents = this.galleryDAO.getEvents(startStamp,"picturegalleryschoolsections","schoolID",group.getSchool().getSchoolID());

		if(schoolEvents != null){

			for(IdentifiedEvent event : schoolEvents){
				event.setTitle(this.newGalleryText + StringUtils.substring(event.getTitle(), 50, "..."));
				event.setFullAlias(this.getFullAlias(group) + "/showSchoolGallery/" + event.getId());
			}
		}

		if(groupEvents == null && schoolEvents == null){
			return null;
		}

		ArrayList<IdentifiedEvent> events = new ArrayList<IdentifiedEvent>();

		if(groupEvents != null){
			events.addAll(groupEvents);
		}

		if(schoolEvents != null){
			events.addAll(schoolEvents);
		}

		return events;
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityGroup group, CommunityUser user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(group.toXML(doc));
		if (se.dosf.communitybase.utils.AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)) {
			document.appendChild(XMLUtils.createElement("isAdmin", "true", doc));
		}
		doc.appendChild(document);

		return doc;
	}

	private String getRelationType(RelationType type) {
		return type.equals(RelationType.GROUP) ? "Group" : "School";
	}

	@Override
	public List<? extends SettingDescriptor> getSettings() {

		ArrayList<SettingDescriptor> settingsList = new ArrayList<SettingDescriptor>();

		List<? extends SettingDescriptor> superSettings = super.getSettings();

		if (superSettings != null) {

			settingsList.addAll(superSettings);
		}

		settingsList.addAll(SETTINGDESCRIPTORS);

		return settingsList;
	}

}

package se.dosf.communitybase.modules.pictureGallery;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.annotations.GroupMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.dbcleanup.DBCleaner;
import se.dosf.communitybase.modules.oldcontentremover.beans.OldContent;
import se.dosf.communitybase.modules.oldcontentremover.beans.OldContentComparator;
import se.dosf.communitybase.modules.oldcontentremover.interfaces.OldContentRemover;
import se.dosf.communitybase.modules.pictureGallery.beans.Comment;
import se.dosf.communitybase.modules.pictureGallery.beans.Gallery;
import se.dosf.communitybase.modules.pictureGallery.beans.Picture;
import se.dosf.communitybase.modules.pictureGallery.cruds.CommentCRUD;
import se.dosf.communitybase.modules.pictureGallery.cruds.GalleryCRUD;
import se.dosf.communitybase.modules.pictureGallery.cruds.PictureCRUD;
import se.dosf.communitybase.modules.pictureGallery.daos.GalleryDAO;
import se.dosf.communitybase.modules.pictureGallery.daos.GalleryEventDAO;
import se.dosf.communitybase.modules.pictureGallery.enums.ImageSize;
import se.dosf.communitybase.modules.pictureGallery.migration.PictureGalleryDataMigrator;
import se.dosf.communitybase.utils.AccessUtils;
import se.dosf.communitybase.utils.CommunityBaseAnnotatedDAOFactory;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.URLType;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.ModuleNotCachedException;
import se.unlogic.hierarchy.core.exceptions.ModuleUnloadException;
import se.unlogic.hierarchy.core.exceptions.ModuleUpdatedInProgressException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.handlers.SimpleSettingHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.foregroundmodules.imagegallery.SimpleFileFilter;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.db.DBUtils;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.image.ImageUtils;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.http.enums.ContentDisposition;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.url.JSessionIDRemover;

public class PictureGalleryModule extends AnnotatedCommunityModule implements CommunityModule, CRUDCallback<CommunityUser>, DBCleaner, OldContentRemover {

	public static final Map<String, Order> GALLERY_SORT_COLUMNS = new HashMap<String, Order>();
	public static final Map<String, Order> PICTURE_SORT_COLUMNS = new HashMap<String, Order>();
	public static final List<String> SORT_COLUMNS = new ArrayList<String>();

	static{
		GALLERY_SORT_COLUMNS.put("name", Order.ASC);
		PICTURE_SORT_COLUMNS.put("posted", Order.DESC);
		PICTURE_SORT_COLUMNS.put("filename", Order.ASC);
		SORT_COLUMNS.addAll(GALLERY_SORT_COLUMNS.keySet());
		SORT_COLUMNS.addAll(PICTURE_SORT_COLUMNS.keySet());
	}

	public static boolean isValidGallerySortColumn(String sortBy) {

		return GALLERY_SORT_COLUMNS.containsKey(sortBy);
	}

	public static boolean isValidPictureSortColumn(String sortBy) {

		return PICTURE_SORT_COLUMNS.containsKey(sortBy);
	}

	private static enum Route {
		LIST_GALLERIES, LIST_PICTURES;
	}

	/* Breadcrumbs */
	@XSLVariable
	protected String newGalleryText = "New gallery: ";

	@XSLVariable
	protected String addGalleryBreadCrumb = "Add gallery";

	@XSLVariable
	protected String updateGalleryBreadCrumb = "Update gallery";

	@XSLVariable
	protected String addPictureBreadCrumb = "Add picture";

	@XSLVariable
	protected String newPicturesInGalleryText = "New pictures in gallery: ";

	@XSLVariable
	protected String othersCategory = "Others";

	@XSLVariable(name = "i18n.NewCategory")
	protected String newCategory = "New category";

	@XSLVariable(name = "i18n.EmptyCategory")
	protected String emptyCategory = "No category";

	/* Module Settings */
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Pictures (thumbnails) per album page", description = "The number of pictures (thumbnails) per album page (default is 15)", required = false, formatValidator = StringIntegerValidator.class)
	protected Integer numOfThumbsPerAlbumPage = 15;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Pictures (thumbnails) per file list page", description = "The number of pictures (thumbnails) per file list page (default is 10)", required = false, formatValidator = StringIntegerValidator.class)
	protected Integer numOfThumbsPerFilePage = 10;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Small image max height", description = "The max height of the small thumbnails (default is 93)", required = false, formatValidator = StringIntegerValidator.class)
	protected Integer smallImageMaxHeight = 93;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Small image max width", description = "The max width of the small thumbnails (default is 125)", required = false, formatValidator = StringIntegerValidator.class)
	protected Integer smallImageMaxWidth = 125;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Medium image max height", description = "The max height of the medium thumbnails (default is 500)", required = false, formatValidator = StringIntegerValidator.class)
	protected Integer mediumImageMaxHeight = 500;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Medium image max width", description = "The max width of the medium thumbnails (default is 500)", required = false, formatValidator = StringIntegerValidator.class)
	protected Integer mediumImageMaxWidth = 500;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Allow comments", description = "Control whether or not comments is used")
	protected boolean allowsComments = false;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Allow album download", description = "Control wheter or not album download is allowed")
	protected boolean allowsGalleryDownload = false;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max upload size", description = "Maxmium upload size in megabytes allowed in a single post request (default is 100)", required = false, formatValidator = StringIntegerValidator.class)
	protected Integer diskThreshold = 100;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "RAM threshold", description = "Maximum size of files in KB to be buffered in RAM during file uploads. Files exceeding the threshold are written to disk instead.", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer ramThreshold = 500;

	@ModuleSetting
	protected Integer thumbQuality = Image.SCALE_SMOOTH;

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Image redistribution disclaimer", description = "Disclaimer displayed for users when watching medium sized thumbs")
	protected String imageRedistributionDisclaimer = null;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Base file store", description = "Directory where gallery directory is created", required = true)
	protected String filestore;

	protected static final SimpleFileFilter FILE_FILTER = new SimpleFileFilter();

	private GalleryDAO galleryDAO;
	private AnnotatedDAOWrapper<Picture, Integer> pictureCRUDDAO;

	private QueryParameterFactory<Picture, Gallery> pictureGalleryParamFactory;
	private QueryParameterFactory<Gallery, Integer> galleryIDParamFactory;

	private GalleryCRUD galleryCRUD;
	private PictureCRUD pictureCRUD;
	private CommentCRUD commentCRUD;

	private GalleryEventDAO galleryEventDAO;

	private boolean migrating;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(PictureGalleryModule.class, this)) {

			log.warn("Another instance has already been registered in instance handler for class " + this.getClass().getName());
		}
	}

	@Override
	public void unload() throws Exception {

		super.unload();

		systemInterface.getInstanceHandler().removeInstance(PictureGalleryModule.class, this);
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		String tableGroupName = this.getClass().getPackage().getName();
		XMLDBScriptProvider scriptProvider = new XMLDBScriptProvider(this.getClass().getResourceAsStream("MySQL DB Script.xml"));
		Integer currentVersion = TableVersionHandler.getTableGroupVersion(dataSource, tableGroupName);

		if(currentVersion == null){

			// Create new tables (notice the max version argument at the end)
			UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, tableGroupName, scriptProvider, null, 1);

			if(upgradeResult.isUpgrade()){
				log.info(upgradeResult.toString());
			}
		}

		TransactionHandler transactionHandler = null;

		try{
			if(currentVersion == null || currentVersion == 1){

				// Migrate data
				transactionHandler = new TransactionHandler(dataSource);

				PictureGalleryDataMigrator migrator = new PictureGalleryDataMigrator(dataSource, this.log);
				migrator.run(true);

				transactionHandler.commit();
			}

		}finally{

			TransactionHandler.autoClose(transactionHandler);
		}

		// Run normal upgrade to upgrade to version 3
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, tableGroupName, scriptProvider, null, 3);

		if (upgradeResult.isUpgrade()) {
			log.info(upgradeResult.toString());
		}

		currentVersion = TableVersionHandler.getTableGroupVersion(dataSource, tableGroupName);

		if (currentVersion == 3) { //Special fix for db script previously being in global CommunityBase script

			List<String> columns = DBUtils.getTableColumns(dataSource.getConnection(), "picturegallery_galleries");

			if (columns.contains("category")) {

				TableVersionHandler.setTableGroupVersion(dataSource, tableGroupName, 4);
				log.info("Skipped db upgrade from version 3 to 4");

			} else {

				upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, tableGroupName, scriptProvider, 3, 4);

				if (upgradeResult.isUpgrade()) {
					log.info(upgradeResult.toString());
				}
			}

			currentVersion = 4;
		}

		if (currentVersion == 4) { //Special fix for db script previously being in global CommunityBase script

			String dataSourceName;
			Connection connection = null;

			try{
				connection = dataSource.getConnection();

				dataSourceName = connection.getCatalog();

			}finally{

				DBUtils.closeConnection(connection);
			}

			ObjectQuery<Integer> query = new ObjectQuery<Integer>(dataSource, "SELECT COUNT(*) FROM information_schema.REFERENTIAL_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = '" + dataSourceName + "' AND CONSTRAINT_NAME = 'FK_picturegallery_gallerygroups_2'", new IntegerPopulator());

			int count = query.executeQuery();

			if (count == 1) {

				TableVersionHandler.setTableGroupVersion(dataSource, tableGroupName, 5);
				log.info("Skipped db upgrade from version 4 to 5");

			} else {

				upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, tableGroupName, scriptProvider, 4, 5);

				if (upgradeResult.isUpgrade()) {
					log.info(upgradeResult.toString());
				}
			}

			currentVersion = 5;
		}

		if (currentVersion == 5) {

			migrating = true;

			String filestore = moduleDescriptor.getMutableSettingHandler().getString("filestore");

			if (StringUtils.isEmpty(filestore)) {
				log.error("Unable to migrate data to disk, filestore not set");
				return;
			}

			SimpleSettingHandler settingHandler = new SimpleSettingHandler();
			settingHandler.setSetting("moduleID", moduleDescriptor.getModuleID());
			settingHandler.setSetting("sectionID", sectionInterface.getSectionDescriptor().getSectionID());
			settingHandler.setSetting("filestore", filestore);

			SimpleForegroundModuleDescriptor descriptor = new SimpleForegroundModuleDescriptor();
			descriptor.setName(moduleDescriptor.getName() + " Migrator");
			descriptor.setAlias(moduleDescriptor.getAlias() + "Migrator");
			descriptor.setDescription("Migrates picture data from DB to disk");
			descriptor.setClassname(se.dosf.communitybase.modules.pictureGallery.migration.PictureGalleryDataMigrator2.class.getName());
			descriptor.setMutableSettingHandler(settingHandler);
			descriptor.setAdminAccess(true);
			descriptor.setDataSourceID(moduleDescriptor.getDataSourceID());
			descriptor.setSectionID(systemInterface.getRootSection().getSectionDescriptor().getSectionID());

			systemInterface.getRootSection().getForegroundModuleCache().cache(descriptor);

			new Thread(new Runnable() {

				@Override
				public void run() {

					while (sectionInterface.getForegroundModuleCache().isCached(moduleDescriptor)) {

						try {
							sectionInterface.getForegroundModuleCache().unload(moduleDescriptor);
						} catch (ModuleNotCachedException e) {
							e.printStackTrace();
						} catch (ModuleUnloadException e) {
							e.printStackTrace();
						} catch (ModuleUpdatedInProgressException e) {
							e.printStackTrace();
						}

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();

			throw new RuntimeException("Module start aborted for data migration running in separate module " + descriptor);
		}

		if (currentVersion >= 6) {

			// Run normal upgrade to upgrade to latest version
			upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, tableGroupName, scriptProvider);

			if (upgradeResult.isUpgrade()) {
				log.info(upgradeResult.toString());
			}
		}

		CommunityBaseAnnotatedDAOFactory daoFactory = new CommunityBaseAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), getSchoolDAO());

		AnnotatedDAO<Gallery> galleryDAO = daoFactory.getDAO(Gallery.class);
		AnnotatedDAO<Picture> pictureDAO = daoFactory.getDAO(Picture.class);
		AnnotatedDAO<Comment> commentDAO = daoFactory.getDAO(Comment.class);

		galleryIDParamFactory = galleryDAO.getParamFactory("galleryID", Integer.class);
		pictureGalleryParamFactory = pictureDAO.getParamFactory("gallery", Gallery.class);

		this.galleryDAO = new GalleryDAO(dataSource, daoFactory, daoFactory.getQueryParameterPopulators(), daoFactory.getBeanStringPopulators());
		daoFactory.addDAO(Gallery.class, galleryDAO);

		this.pictureCRUDDAO = new AnnotatedDAOWrapper<Picture, Integer>(pictureDAO, "pictureID", Integer.class);

		AnnotatedDAOWrapper<Gallery, Integer> galleryCRUDDAO = new AnnotatedDAOWrapper<Gallery, Integer>(galleryDAO, "galleryID", Integer.class);
		AnnotatedDAOWrapper<Comment, Integer> commentCRUDDAO = new AnnotatedDAOWrapper<Comment, Integer>(commentDAO, "commentID", Integer.class);

		this.galleryCRUD = new GalleryCRUD(galleryCRUDDAO, new AnnotatedRequestPopulator<Gallery>(Gallery.class), "Gallery", "gallery", "", this);
		this.pictureCRUD = new PictureCRUD(pictureCRUDDAO, "Picture", "picture", "", this);
		this.commentCRUD = new CommentCRUD(commentCRUDDAO, new AnnotatedRequestPopulator<Comment>(Comment.class), "Comment", "comment", "", this, pictureCRUDDAO);

		this.galleryEventDAO = new GalleryEventDAO(dataSource, this);
	}

	public Integer getNumOfThumbsPerAlbumPage() {

		return numOfThumbsPerAlbumPage;
	}

	public Integer getNumOfThumbsPerFilePage() {

		return numOfThumbsPerFilePage;
	}

	public Boolean getAllowsGalleryDownload() {

		return allowsGalleryDownload;
	}

	public Boolean getAllowsComments() {

		return allowsComments;
	}

	private void setStickyPreferences(HttpServletRequest req, String criteria, boolean reverse) {

		req.getSession().setAttribute("pictureGallery.sortingPreferences.criteria", criteria);
		req.getSession().setAttribute("pictureGallery.sortingPreferences.reverse", Boolean.toString(reverse));
	}

	private void propagatePreferences(HttpServletRequest req, String criteria, boolean reverse, Order order) {

		req.setAttribute("pictureGallery.sortingPreferences.criteria", criteria);
		req.setAttribute("pictureGallery.sortingPreferences.reverse", reverse);
		req.setAttribute("pictureGallery.sortingPreferences.order", order);
	}

	private Route setSortingPreferences(HttpServletRequest req) throws URINotFoundException {

		return this.setSortingPreferences(req, true, true);
	}

	/**
	 * Determines and sets user sorting preferences for listing albums or pictures. Preferences from request take precedence over "sticky" preferences from
	 * session Preferences are set to session and propagated with request to subsequent handlers
	 *
	 * @param req
	 * @param getStickyPreferences
	 * @param setStickyPreferences
	 * @return
	 * @throws URINotFoundException
	 */
	private Route setSortingPreferences(HttpServletRequest req, boolean getStickyPreferences, boolean setStickyPreferences) throws URINotFoundException {

		HttpSession session;
		String sortingCriteria = null;
		boolean reverse = false;
		Order sortOrder = null;

		// Get preferences from query string
		if(req.getQueryString() != null){
			sortingCriteria = req.getParameter("orderby");
			reverse = Boolean.parseBoolean(req.getParameter("reverse"));
		}

		// Get "sticky" preferences from session
		else if(getStickyPreferences && (session = req.getSession(false)) != null){
			sortingCriteria = (String) session.getAttribute("pictureGallery.sortingPreferences.criteria");
			reverse = Boolean.parseBoolean((String) session.getAttribute("pictureGallery.sortingPreferences.reverse"));
		}

		// Get default preferences
		if(sortingCriteria == null){
			sortingCriteria = "name";
		}

		// Set "sticky" preferences to session
		if(setStickyPreferences){
			this.setStickyPreferences(req, sortingCriteria, reverse);
		}

		if(isValidGallerySortColumn(sortingCriteria)){
			if(reverse){
				sortOrder = PictureGalleryModule.GALLERY_SORT_COLUMNS.get(sortingCriteria).equals(Order.ASC) ? Order.DESC : Order.ASC;
			}else{
				sortOrder = PictureGalleryModule.GALLERY_SORT_COLUMNS.get(sortingCriteria);
			}
			this.propagatePreferences(req, sortingCriteria, reverse, sortOrder);
			return Route.LIST_GALLERIES;
		}

		if(isValidPictureSortColumn(sortingCriteria)){
			if(reverse){
				sortOrder = PictureGalleryModule.PICTURE_SORT_COLUMNS.get(sortingCriteria).equals(Order.ASC) ? Order.DESC : Order.ASC;
			}else{
				sortOrder = PictureGalleryModule.PICTURE_SORT_COLUMNS.get(sortingCriteria);
			}
			this.propagatePreferences(req, sortingCriteria, reverse, sortOrder);
			return Route.LIST_PICTURES;
		}

		throw new URINotFoundException(req.getRequestURI());
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		if(this.setSortingPreferences(req).equals(Route.LIST_GALLERIES)){
			return this.galleryCRUD.list(req, res, user, uriParser, null);
		}
		return this.pictureCRUD.list(req, res, user, uriParser, null);
	}

	@GroupMethod
	public ForegroundModuleResponse galleries(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.setSortingPreferences(req, false, true);
		return this.galleryCRUD.list(req, res, user, uriParser, null);
	}

	@GroupMethod
	public ForegroundModuleResponse pictures(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.setSortingPreferences(req);

		//Temporary fix until the sorting logic is re-written from scratch
		if(!isValidPictureSortColumn(req.getAttribute("pictureGallery.sortingPreferences.criteria").toString())){

			req.setAttribute("pictureGallery.sortingPreferences.criteria", "filename");
		}

		return this.pictureCRUD.list(req, res, user, uriParser, null);
	}

	@GroupMethod
	public ForegroundModuleResponse addGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.galleryCRUD.add(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse updateGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.galleryCRUD.update(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse deleteGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.galleryCRUD.delete(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse showGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.galleryCRUD.show(req, res, user, uriParser);
	}

	@GroupMethod(alias = "addcomment")
	public ForegroundModuleResponse addComment(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.commentCRUD.add(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse multiUploader(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.checkAdminAccess(user, group);

		Gallery gallery = null;

		if((gallery = this.galleryCRUD.getRequestedBean(req, res, user, uriParser, null)) == null){

			throw new URINotFoundException(uriParser);

		}else if(!AccessUtils.checkAdminAccess(gallery, user, GroupAccessLevel.PUBLISHER)){

			throw new AccessDeniedException("Picture upload access in gallery " + gallery + " denied!");

		}else{

			Document doc = this.createDocument(req, uriParser, user);

			if(group != null){
				doc.getFirstChild().appendChild(group.toXML(doc));
			}

			Element uploadElement = doc.createElement("multiUploader");

			doc.getFirstChild().appendChild(uploadElement);

			uploadElement.appendChild(gallery.toXML(doc));

			XMLUtils.appendNewElement(doc, uploadElement, "diskThreshold", this.diskThreshold * BinarySizes.MegaByte);

			uploadElement.appendChild(XMLUtils.createElement("sessionID", req.getSession().getId(), doc));

			return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getGroupBreadcrumb(group), getModuleBreadcrumb(group), getMethodBreadcrumb(gallery.getName(), gallery.getDescription(), "showGallery/" + gallery.getGalleryID(), group), new Breadcrumb(this.addPictureBreadCrumb, addPictureBreadCrumb, "#"));

		}
	}

	@GroupMethod
	public ForegroundModuleResponse upload(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		//Fix for jsessionid not being removed from URL in tomcat 6.0.33 and later versions
		Integer galleryID = NumberUtils.toInt(JSessionIDRemover.remove(uriParser, 3));

		Gallery gallery = null;

		if(galleryID == null || (gallery = this.galleryCRUD.getBean(galleryID)) == null){

			throw new URINotFoundException(uriParser);

		}else if(!AccessUtils.checkAdminAccess(gallery, user, GroupAccessLevel.PUBLISHER)){

			throw new AccessDeniedException("Picture upload access in gallery " + gallery + " denied!");

		}else{

			MultipartRequest requestWrapper = null;

			InputStream is = null;

			try{

				requestWrapper = new MultipartRequest(this.ramThreshold * BinarySizes.KiloByte, this.diskThreshold * BinarySizes.MegaByte, req);

				FileItem fileItem = requestWrapper.getFile(0);

				if(fileItem != null){

					is = fileItem.getInputStream();

					TransactionHandler transactionHandler = new TransactionHandler(dataSource);

					Picture picture = this.createPicture(is, FilenameUtils.getName(fileItem.getName()), gallery, user, transactionHandler);

					transactionHandler.commit();

					log.info("User " + user + " added picture " + picture + " to gallery " + gallery);

				}

			}catch(FileUploadException e){

				log.info("Error processing image upload by user " + user + " in gallery " + gallery + ", cause: " + e);

			}catch(ValidationException e){

				log.debug("Error processing image upload by user " + user + " in gallery " + gallery + ", cause: " + e);

			}catch(IOException e){

				log.info("Error processing image upload by user " + user + " in gallery " + gallery + ", cause: " + e);

			}finally{

				if(requestWrapper != null){
					requestWrapper.deleteFiles();
				}

				CloseUtils.close(is);

				try{

					res.getWriter().print("Ok");
					res.flushBuffer();

				}catch(IOException e){}

			}

			return null;
		}
	}

	@GroupMethod
	public ForegroundModuleResponse simpleUploader(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		Integer galleryID = NumberUtils.toInt(uriParser.get(3));

		Gallery gallery = null;

		if(galleryID == null || (gallery = this.galleryCRUD.getBean(galleryID)) == null){

			throw new URINotFoundException(uriParser);

		}else if(!AccessUtils.checkAdminAccess(gallery, user, GroupAccessLevel.PUBLISHER)){

			throw new AccessDeniedException("Picture upload access in gallery " + gallery + " denied!");

		}else{

			ValidationException validationException = null;

			if(req.getMethod().equalsIgnoreCase("POST")){

				MultipartRequest requestWrapper = null;

				InputStream is = null;

				try{
					requestWrapper = new MultipartRequest(this.ramThreshold * BinarySizes.KiloByte, this.diskThreshold * BinarySizes.MegaByte, req);

					if(requestWrapper.getFileCount() == 0){
						throw new ValidationException(new ValidationError("NoImage"));
					}

					FileItem fileItem = requestWrapper.getFile(0);

					if(fileItem.getName() != null && fileItem.getName().toLowerCase().endsWith(".zip")){

						try{

							log.info("User " + user + " adding pictures from zip to gallery " + gallery);

							TransactionHandler transactionHandler = new TransactionHandler(dataSource);

							ArrayList<Picture> pictures = this.uploadGalleryZip(fileItem, gallery, user, transactionHandler);

							transactionHandler.commit();

							log.info("User " + user + " added " + pictures.size() + " zipped images to gallery " + gallery);

						}catch(FileUploadException e){
							throw new ValidationException(new ValidationError("UnableToParseRequest"));
						}catch(ZipException e){
							throw new ValidationException(new ValidationError("UnableToParseRequest"));
						}

					}else if(FILE_FILTER.accept(fileItem)){

						is = fileItem.getInputStream();

						log.info("User " + user + " adding picture to gallery " + gallery);

						TransactionHandler transactionHandler = new TransactionHandler(dataSource);

						Picture picture = this.createPicture(is, FilenameUtils.getName(fileItem.getName()), gallery, user, transactionHandler);

						transactionHandler.commit();

						log.info("User " + user + " added picture " + picture + " to gallery " + gallery);

					}else if(StringUtils.isEmpty(fileItem.getName())){
						throw new ValidationException(new ValidationError("NoImage"));
					}else{
						throw new ValidationException(new ValidationError("UnableToParseRequest"));
					}

					res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID() + "/showGallery/" + gallery.getGalleryID());
					return null;

				}catch(ValidationException e){

					validationException = e;

				}catch(FileUploadException e){

					log.info("Error processing image upload by user " + user + " in gallery " + gallery + ", cause: " + e);

				}catch(IOException e){

					log.info("Error processing image upload by user " + user + " in gallery " + gallery + ", cause: " + e);

				}finally{
					if(requestWrapper != null){
						requestWrapper.deleteFiles();
					}

					CloseUtils.close(is);
				}
			}

			Document doc = this.createDocument(req, uriParser, user);

			if(group != null){
				doc.getFirstChild().appendChild(group.toXML(doc));
			}

			Element simpleUploaderElement = doc.createElement("simpleUploader");
			doc.getFirstChild().appendChild(simpleUploaderElement);
			simpleUploaderElement.appendChild(gallery.toXML(doc));

			if(uriParser.get(4) != null && uriParser.get(4).equals("noflash")){
				simpleUploaderElement.appendChild(XMLUtils.createElement("noflash", "true", doc));
			}

			if(validationException != null){
				simpleUploaderElement.appendChild(validationException.toXML(doc));
				simpleUploaderElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getGroupBreadcrumb(group), getModuleBreadcrumb(group), getMethodBreadcrumb(gallery.getName(), gallery.getDescription(), "showGallery/" + gallery.getGalleryID(), group), new Breadcrumb(this.addPictureBreadCrumb, this.addPictureBreadCrumb, "#"));
		}
	}

	@GroupMethod
	public ForegroundModuleResponse showPicture(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.pictureCRUD.show(req, res, user, uriParser);
	}

	@GroupMethod(alias = "viewcomments")
	public ForegroundModuleResponse viewComments(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		if(uriParser.size() < 4 || !NumberUtils.isInt(uriParser.get(3))){
			throw new URINotFoundException(uriParser);
		}

		if(req.getMethod().equalsIgnoreCase("POST")){
			if(Boolean.parseBoolean(req.getParameter("viewComments"))){
				req.getSession().setAttribute(this.moduleDescriptor.getModuleID() + ".showAll", true);
			}else{
				req.getSession().setAttribute(this.moduleDescriptor.getModuleID() + ".showAll", false);
			}
		}

		res.sendRedirect(req.getContextPath() + this.getFullAlias((CommunityGroup) req.getAttribute("group")) + "/showPicture/" + uriParser.get(3));
		return null;
	}

	@GroupMethod
	public ForegroundModuleResponse updateComment(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.commentCRUD.update(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse deleteComment(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.commentCRUD.delete(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse deletePicture(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.pictureCRUD.delete(req, res, user, uriParser);
	}

	protected ArrayList<Picture> uploadGalleryZip(FileItem fileItem, Gallery gallery, CommunityUser user, TransactionHandler transactionHandler) throws Exception {

		java.io.File file = null;
		ArrayList<Picture> pictures = new ArrayList<Picture>();

		InputStream zipEntryInputStream = null;

		try{

			file = java.io.File.createTempFile("galleryupload-" + System.currentTimeMillis(), ".zip");
			fileItem.write(file);
			ZipFile zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> e = zipFile.entries();

			while(e.hasMoreElements()){
				ZipEntry ze = e.nextElement();

				if(!ze.isDirectory() && SimpleFileFilter.isValidFilename(ze.getName())){

					String filename = FilenameUtils.getName(ze.getName());
					log.info("Adding file " + filename + " to gallery " + gallery);
					zipEntryInputStream = zipFile.getInputStream(ze);
					pictures.add(this.createPicture(zipEntryInputStream, filename, gallery, user, transactionHandler));
				}
			}

		}finally{
			if(file != null){
				file.delete();
			}

			CloseUtils.close(zipEntryInputStream);

		}
		return pictures;
	}

	@GroupMethod
	public ForegroundModuleResponse smallThumb(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws IOException, SQLException, URINotFoundException, AccessDeniedException {

		this.getImage(ImageSize.SMALL, req, res, user, uriParser, group);
		return null;
	}

	@GroupMethod
	public ForegroundModuleResponse mediumThumb(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		this.getImage(ImageSize.MEDIUM, req, res, user, uriParser, group);
		return null;
	}

	@GroupMethod
	public ForegroundModuleResponse getImage(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		this.getImage(ImageSize.ORIGINAL, req, res, user, uriParser, group);
		return null;
	}

	private void getImage(ImageSize size, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws AccessDeniedException, SQLException, IOException, NumberFormatException, URINotFoundException {

		Picture picture = null;

		// Validate existence
		if((picture = this.pictureCRUD.getRequestedBean(req, res, user, uriParser, size.toString())) == null){
			throw new URINotFoundException(uriParser);
		}

		log.debug("User " + user + " requesting " + size.toString().toLowerCase() + " size of picture " + picture + " in gallery " + picture.getGallery());

		try{

			writePictureToWeb(picture, size, req, res, user);

		}catch(IOException e){
			log.info("Caught exception " + e + " while sending picture " + picture + " in gallery " + picture.getGallery() + " to " + user);
		}
	}

	public FileInputStream getPictureStream(Picture picture, ImageSize size) {

		return getPictureStream(getPictureFile(picture, size));
	}

	private FileInputStream getPictureStream(File file) {

		if (file != null) {

			try {
				return new FileInputStream(file);

			} catch (Exception e) {

				log.warn("Error reading picture from disk at " + file.getAbsolutePath(), e);
			}
		}

		return null;
	}

	public File getPictureFile(Picture picture, ImageSize size) {

		File file = null;

		if (ImageSize.ORIGINAL.equals(size)) {

			file = getPictureFile(getPictureFullFilePath(picture));

			if (file == null) {
				file = getPictureFile(getPictureMediumFilePath(picture));
			}

		} else if (ImageSize.MEDIUM.equals(size)) {

			file = getPictureFile(getPictureMediumFilePath(picture));

			if (file == null) {
				file = getPictureFile(getPictureSmallFilePath(picture));
			}

		} else if (ImageSize.SMALL.equals(size)) {

			file = getPictureFile(getPictureSmallFilePath(picture));
		}

		return file;
	}

	private File getPictureFile(String path) {

		if (StringUtils.isEmpty(filestore)) {
			return null;
		}

		File file = new File(path);

		if (file.exists()) {

			return file;

		} else {

			log.warn("Missing picture on disk at " + path);
		}

		return null;
	}

	@GroupMethod
	public ForegroundModuleResponse downloadPictures(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		//TODO If we run out of memory while downloading downloading from group with access to many pictures then we will have to rewrite this method so that it gets one image at time from the database

		if(this.allowsGalleryDownload){

			log.info("User " + user + " downloading pictures...");

			// Get pictures accessible from group or school
			LowLevelQuery<Picture> query = new LowLevelQuery<Picture>();

			String sql = "SELECT * FROM " + this.pictureCRUDDAO.getAnnotatedDAO().getTableName() + " WHERE galleryID IN " + "(SELECT DISTINCT n.galleryID FROM picturegallery_galleries as n " + "LEFT OUTER JOIN picturegallery_gallerygroups as gg ON n.galleryID = gg.galleryID " + "LEFT OUTER JOIN picturegallery_galleryschools as gs ON n.galleryID = gs.galleryID " + "WHERE gg.groupID = ? OR gs.schoolID = ? OR n.global = ? )";

			query.setSql(sql);

			query.addParameters(group.getGroupID(), group.getSchool().getSchoolID(), true);
			query.disableAutoRelations(true);

			List<Picture> pictures = this.pictureCRUDDAO.getAnnotatedDAO().getAll(query);

			if(!CollectionUtils.isEmpty(pictures)){

				long startTime = System.currentTimeMillis();
				ZipOutputStream zipOutputStream = null;

				try{
					res.setContentType("application/zip");
					res.setHeader("Content-Disposition", "inline; filename=\"pictures.zip\"");
					res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, proxy-revalidate");

					zipOutputStream = new ZipOutputStream(res.getOutputStream());

					for(Picture picture : pictures){

						ZipEntry zipEntry = new ZipEntry(picture.getPictureID() + "_" + picture.getFilename());
						InputStream inputStream = null;

						try{

							inputStream = getPictureStream(picture, ImageSize.ORIGINAL);

							if (inputStream != null) {
								zipOutputStream.putNextEntry(zipEntry);
								StreamUtils.transfer(inputStream, zipOutputStream);
								zipOutputStream.closeEntry();
							}

						}finally{
							CloseUtils.close(inputStream);
						}
					}
					zipOutputStream.flush();

					log.info("Sent archive containing " + pictures.size() + " files to user " + user + " in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime));

				}catch(IOException e){
					log.info("Error sending picture archive to user " + user);
				}finally{
					CloseUtils.close(zipOutputStream);
				}
			}
			return null;
		}
		throw new URINotFoundException(uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse downloadGallery(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		//TODO If we run out of memory while downloading large galleries then we will have to rewrite this method so that it gets one image at time from the database

		if(this.allowsGalleryDownload){

			Gallery gallery = galleryCRUD.getRequestedBean(req, res, user, uriParser, null);

			log.info("User " + user + " downloading gallery " + gallery + "...");

			HighLevelQuery<Picture> query = new HighLevelQuery<Picture>();

			query.disableAutoRelations(true);
			query.addParameter(pictureGalleryParamFactory.getParameter(gallery));

			List<Picture> pictures = this.pictureCRUDDAO.getAnnotatedDAO().getAll(query);

			if(!CollectionUtils.isEmpty(pictures)){

				long startTime = System.currentTimeMillis();
				ZipOutputStream zipOutputStream = null;

				try{
					res.setContentType("application/zip");
					res.setHeader("Content-Disposition", "inline; filename=\"" + FileUtils.toValidHttpFilename(gallery.getName()) + ".zip\"");
					res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, proxy-revalidate");

					zipOutputStream = new ZipOutputStream(res.getOutputStream());

					for(Picture picture : pictures){

						ZipEntry zipEntry = new ZipEntry(picture.getPictureID() + "_" + picture.getFilename());
						InputStream inputStream = null;

						try{

							inputStream = getPictureStream(picture, ImageSize.ORIGINAL);

							if (inputStream != null) {
								zipOutputStream.putNextEntry(zipEntry);
								StreamUtils.transfer(inputStream, zipOutputStream);
								zipOutputStream.closeEntry();
							}

						}finally{
							CloseUtils.close(inputStream);
						}
					}

					zipOutputStream.flush();

					log.info("Sent gallery " + gallery + " containing " + pictures.size() + " files to user " + user + " in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime));

				}catch(IOException e){
					log.info("Error sending gallery " + gallery + " to user " + user);
				}finally{
					CloseUtils.close(zipOutputStream);
				}
			}
			return null;
		}
		throw new URINotFoundException(uriParser);
	}

	protected Picture createPicture(InputStream inputStream, String filename, Gallery gallery, CommunityUser user, TransactionHandler transactionHandler) throws SQLException, ValidationException, IOException {

		log.info("Creating thumbs and storing picture " + filename + " in gallery " + gallery);

		byte[] rawFile = StreamUtils.toByteArray(inputStream);

		BufferedImage bufferedImage = ImageUtils.getImage(rawFile);

		if(bufferedImage != null){

			Picture picture = new Picture();

			picture.setFilename(filename);
			picture.setGallery(gallery);
			picture.setPoster(user);

			pictureCRUDDAO.add(picture, transactionHandler);

			try {

				File file = new File(getPictureFullFilePath(picture));
				FileUtils.createMissingDirectories(file);
				FileUtils.writeFile(file, new ByteArrayInputStream(rawFile), true);

				bufferedImage = ImageUtils.changeImageType(bufferedImage, BufferedImage.TYPE_INT_RGB, Color.WHITE);

				//Only resize pictures if necessary and using the medium thumb when generating the small thumb to speed up the conversion process

				if (bufferedImage.getWidth() > mediumImageMaxWidth || bufferedImage.getHeight() > mediumImageMaxHeight) {

					bufferedImage = ImageUtils.scaleImage(bufferedImage, mediumImageMaxHeight, mediumImageMaxWidth, java.awt.Image.SCALE_SMOOTH, BufferedImage.TYPE_INT_RGB);
				}

				file = new File(getPictureMediumFilePath(picture));
				FileUtils.createMissingDirectories(file);
				FileUtils.writeFile(file, new ByteArrayInputStream(ImageUtils.convertImage(bufferedImage, ImageUtils.JPEG)), true);

				if (bufferedImage.getWidth() > smallImageMaxWidth || bufferedImage.getHeight() > smallImageMaxHeight) {

					bufferedImage = ImageUtils.scaleImage(bufferedImage, smallImageMaxHeight, smallImageMaxWidth, java.awt.Image.SCALE_SMOOTH, BufferedImage.TYPE_INT_RGB);
				}

				file = new File(getPictureSmallFilePath(picture));
				FileUtils.createMissingDirectories(file);
				FileUtils.writeFile(file, new ByteArrayInputStream(ImageUtils.convertImage(bufferedImage, ImageUtils.JPEG)), true);

			} catch (IOException e) {

				removePictureFromDisk(picture);

				throw e;
			}

			return picture;

		}else{

			throw new ValidationException(new ValidationError("UnableToParseRequest"));
		}

	}

	public void removePictureFromDisk(Picture picture){

		File file = new File(getPictureFullFilePath(picture));
		deletePictureFile(picture, file);

		file = new File(getPictureMediumFilePath(picture));
		deletePictureFile(picture, file);

		file = new File(getPictureSmallFilePath(picture));
		deletePictureFile(picture, file);

		log.info("Removed picture " + picture + " from disk belonging to gallery " + picture.getGallery());
	}

	private void deletePictureFile(Picture picture, File file) {

		if (file.exists()) {

			if (!FileUtils.deleteFile(file)) {

				log.warn("Unable to delete file " + file + " belonging to picture " + picture + " of gallery " + picture.getGallery());
			}

		} else {

			log.warn("Attempted to delete missing file " + file + " belonging to picture " + picture + " of gallery " + picture.getGallery());
		}
	}

	protected String getPictureFullFilePath(Picture picture) {

		return getPictureFullFilePath(filestore, picture.getPictureID());
	}

	protected String getPictureMediumFilePath(Picture picture) {

		return getPictureMediumFilePath(filestore, picture.getPictureID());
	}

	protected String getPictureSmallFilePath(Picture picture) {

		return getPictureSmallFilePath(filestore, picture.getPictureID());
	}

	public static String getPictureFullFilePath(String filestore, Integer pictureID) {

		return filestore + File.separator + "gallery" + File.separator + "full" + File.separator + pictureID;
	}

	public static String getPictureMediumFilePath(String filestore, Integer pictureID) {

		return filestore + File.separator + "gallery" + File.separator + "medium" + File.separator + pictureID;
	}

	public static String getPictureSmallFilePath(String filestore, Integer pictureID) {

		return filestore + File.separator + "gallery" + File.separator + "small" + File.separator + pictureID;
	}

	public void writePictureToWeb(Picture picture, ImageSize size, HttpServletRequest req, HttpServletResponse res, User user) throws SQLException, IOException {

		String filename = picture.getFilename();

		String contentType = null;

		if (size != ImageSize.ORIGINAL) {

			contentType = "image/jpeg";
			filename = filename.replace("\\..{1-3}$", ".jpg");
		}

		try {

			HTTPUtils.sendFile(getPictureFile(picture, size), filename, contentType, req, res, ContentDisposition.INLINE);

		} catch (IOException e) {

			this.log.info("Error sending attachment " + picture + " to user " + user);
		}
	}

	private Breadcrumb getMethodBreadcrumb(String name, String description, String method, CommunityGroup group) {

		return new Breadcrumb(name, description, this.getFullAlias() + "/" + group.getGroupID() + "/" + method, URLType.RELATIVE_FROM_CONTEXTPATH);
	}

	@Override
	public List<IdentifiedEvent> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws SQLException {

		if (StringUtils.isEmpty(filestore) || migrating) {
			return null;
		}

		return galleryEventDAO.getGroupResume(group, user, startStamp, newGalleryText, newPicturesInGalleryText);
	}

	public Breadcrumb getShowPictureBreadcrumb(CommunityGroup group, Picture picture) {

		return this.getMethodBreadcrumb(picture.getFilename(), "showPicture/" + picture.getPictureID(), group);
	}

	public Breadcrumb getShowGalleryBreadcrumb(CommunityGroup group, Gallery gallery) {

		return this.getMethodBreadcrumb(gallery.getName(), gallery.getDescription(), "showGallery/" + gallery.getGalleryID(), group);
	}

	public Breadcrumb getAddBreadCrumb(CommunityGroup group) {

		return this.getMethodBreadcrumb(this.addGalleryBreadCrumb, "addGallery", group);
	}

	public Breadcrumb getUpdateBreadCrumb(CommunityGroup group, Gallery gallery) {

		return this.getMethodBreadcrumb(this.updateGalleryBreadCrumb, "updateGallery" + "/" + gallery.getGalleryID(), group);
	}

	private Breadcrumb getMethodBreadcrumb(String name, String methodUri, CommunityGroup group) {

		return new Breadcrumb(name, this.getFullAlias() + "/" + group.getGroupID() + "/" + methodUri);
	}

	/* Read Access */
	public boolean checkAccess(Gallery gallery, CommunityGroup group) {

		if(gallery != null && ((gallery.getGroups() != null && gallery.getGroups().contains(group)) || (gallery.getSchools() != null && gallery.getSchools().contains(group.getSchool())) || gallery.isGlobal())){
			return true;
		}
		return false;
	}

	/* Read Access */
	public boolean checkAccess(Picture picture, CommunityGroup group) {

		return this.checkAccess(picture.getGallery(), group);
	}

	/* Read Access */
	public boolean checkAccess(Comment comment, CommunityGroup group) {

		if(comment.getPicture() == null){
			return false;
		}
		return this.checkAccess(comment.getPicture().getGallery(), group);
	}

	/* Write Access */
	public boolean hasUpdateAccess(Gallery gallery, CommunityUser user) {

		return AccessUtils.checkAdminAccess(gallery, user, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER);
	}

	/* Write Access */
	public boolean hasUpdateAccess(Picture picture, CommunityUser user) {

		return AccessUtils.checkAdminAccess(picture.getGallery(), user, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER);
	}

	/* Write Access */
	public boolean hasUpdateAccess(Comment comment, CommunityUser user) {

		if(comment.getPicture() == null){
			return false;
		}
		return AccessUtils.checkAdminAccess(comment.getPicture().getGallery(), user, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER);
	}

	public void appendGroupAndAccess(Document doc, CommunityGroup group, CommunityUser user) {

		Element document = doc.getDocumentElement();
		if(group != null){
			document.appendChild(group.toXML(doc));
		}
		if(AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)){
			document.appendChild(XMLUtils.createElement("isAdmin", "true", doc));
		}

		if(user.isAdmin()){
			document.appendChild(XMLUtils.createElement("isSysAdmin", "true", doc));
		}

	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityUser user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));

		doc.appendChild(document);

		return doc;
	}

	@Override
	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	@Override
	public String getFullAlias(CommunityGroup group) {

		return super.getFullAlias(group);
	}

	public GalleryDAO getGalleryDAO() {

		return galleryDAO;
	}

	public String getImageRedistributionDisclaimer() {

		return imageRedistributionDisclaimer;
	}

	private List<Integer> getOrphanedGalleryIDs() throws SQLException {

		String sql = "SELECT DISTINCT ga.galleryID FROM picturegallery_galleries ga LEFT OUTER JOIN picturegallery_gallerygroups g ON ga.galleryID = g.galleryID LEFT OUTER JOIN picturegallery_galleryschools s ON ga.galleryID = s.galleryID WHERE groupID IS null AND schoolID is null AND global = 0";

		ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(this.dataSource, sql, new IntegerPopulator());

		return query.executeQuery();
	}

	public Gallery getGalleryByID(Integer galleryID) throws SQLException {

		HighLevelQuery<Gallery> query = new HighLevelQuery<Gallery>();
		query.addParameter(galleryIDParamFactory.getParameter(galleryID));

		return galleryDAO.get(query);
	}

	@Override
	public void cleanDB() throws SQLException {

		if (StringUtils.isEmpty(filestore) || migrating) {
			return;
		}

		List<Integer> orphans = getOrphanedGalleryIDs();

		if(orphans != null && orphans.size() > 0){
			log.warn("There are " + orphans.size() + " orphaned galleries.");

			Connection connection = null;

			try {
				connection = dataSource.getConnection();

				for (Integer id : orphans) {
					HighLevelQuery<Gallery> query = new HighLevelQuery<Gallery>();
					query.addParameter(galleryIDParamFactory.getParameter(id));

					log.info("Deleting gallery with id " + id);
					galleryDAO.delete(query, connection);
				}

			} finally {

				DBUtils.closeConnection(connection);
			}
		}
	}

	public String getOthersCategoryName() {

		return othersCategory;
	}

	public String getEmptyCategoryName() {

		return emptyCategory;
	}

	public String getNewCategoryName() {

		return newCategory;
	}

	@Override
	public int getOldContentCount(CommunityUser user, CommunityGroup group, Date endDate) throws SQLException {

		if (StringUtils.isEmpty(filestore) || migrating) {
			return 0;
		}

		Connection connection = null;

		try {
			connection = dataSource.getConnection();

			ArrayListQuery<Gallery> query = new ArrayListQuery<Gallery>(connection, false,
					"SELECT DISTINCT n.galleryID, n.posted FROM " + galleryDAO.getTableName() + " AS n " +
							"LEFT OUTER JOIN picturegallery_gallerygroups AS g ON n.galleryID = g.galleryID " +
							"WHERE g.groupID = ? AND n.global = 0 AND n.posted <= ?",
							new BeanResultSetPopulator<Gallery>() {
				@Override
				public Gallery populate(ResultSet rs) throws SQLException {

					Gallery gallery = new Gallery();
					gallery.setGalleryID(rs.getInt(1));
					gallery.setPosted(rs.getTimestamp(2));
					return gallery;
				}
			});

			query.setInt(1, group.getGroupID());
			query.setObject(2, new Timestamp(endDate.getTime()));

			List<Gallery> galleries = query.executeQuery();

			int pictureCount = 0;

			if (galleries != null) {

				for (Gallery gallery : galleries) {

					Timestamp latestPictureTimestamp = galleryDAO.getLastPictureTimestamp(gallery.getGalleryID(), connection);

					if (latestPictureTimestamp != null && gallery.getPosted().compareTo(latestPictureTimestamp) < 0) {
						gallery.setPosted(latestPictureTimestamp);
					}

					if (gallery.getPosted().before(endDate)) {

						Integer amount = getPictureCount(gallery, connection);

						if (amount != null) {
							pictureCount += amount;
						}
					}
				}

			}

			return pictureCount;
		} finally {

			DBUtils.closeConnection(connection);
		}
	}

	@Override
	public Collection<OldContent> getOldContent(CommunityUser user, CommunityGroup group, Date endDate) throws SQLException {

		if (StringUtils.isEmpty(filestore) || migrating) {
			return null;
		}

		Connection connection = null;

		try {
			connection = dataSource.getConnection();

			LowLevelQuery<Gallery> query = new LowLevelQuery<Gallery>("SELECT DISTINCT n.galleryID, n.name, n.description, n.category, n.global, n.posted " +
					"FROM " + galleryDAO.getTableName() + " AS n " +
					"LEFT OUTER JOIN picturegallery_gallerygroups AS g ON n.galleryID = g.galleryID " +
					"WHERE g.groupID = ? AND n.global = 0 AND n.posted <= ?");

			query.addParameters(group.getGroupID(), new Timestamp(endDate.getTime()));

			List<Gallery> galleries = galleryDAO.getAll(query, connection);

			if (galleries != null) {
				List<OldContent> content = new ArrayList<OldContent>();

				for (Gallery gallery : galleries) {

					if (hasUpdateAccess(gallery, user)) {

						Timestamp latestPictureTimestamp = galleryDAO.getLastPictureTimestamp(gallery.getGalleryID(), connection);

						if (latestPictureTimestamp != null && gallery.getPosted().compareTo(latestPictureTimestamp) < 0){
							gallery.setPosted(latestPictureTimestamp);
						}

						if (gallery.getPosted().before(endDate)) {

							Integer pictureCount = getPictureCount(gallery, connection);

							content.add(new OldContent(gallery.getGalleryID(), gallery.getName() + " (" + pictureCount + " %PICTURES%)", getFullAlias(group) + "/showGallery/" + gallery.getGalleryID(), gallery.getPosted(), Gallery.class.getName().toString()));
						}
					}
				}

				Collections.sort(content, OldContentComparator.getInstance(Order.ASC));

				return content;
			}

		} finally {

			DBUtils.closeConnection(connection);
		}

		return null;
	}

	private Integer getPictureCount(Gallery gallery, Connection connection) throws SQLException {

		ObjectQuery<Integer> pictureCountQuery = new ObjectQuery<Integer>(connection, false, "SELECT COUNT(pictureID) FROM picturegallery_pictures p WHERE galleryID = ?", IntegerPopulator.getPopulator());

		pictureCountQuery.setInt(1, gallery.getGalleryID());

		return pictureCountQuery.executeQuery();
	}

	@Override
	public int deleteOldContent(Collection<OldContent> oldContents, CommunityUser user, CommunityGroup group) throws SQLException {

		if (StringUtils.isEmpty(filestore) || migrating) {
			return 0;
		}

		int deletedCount = 0;

		Connection connection = null;

		try {
			connection = dataSource.getConnection();

			for (OldContent oldContent : oldContents) {

				HighLevelQuery<Gallery> query = new HighLevelQuery<Gallery>();
				query.addParameter(galleryIDParamFactory.getParameter(oldContent.getID()));

				Gallery gallery = galleryDAO.get(query, connection);

				if (gallery == null) {
					continue;
				}

				if (hasUpdateAccess(gallery, user) && !gallery.isGlobal()) {

					Integer pictureCount = getPictureCount(gallery, connection);

					if (gallery.getSchools() == null && gallery.getGroups().size() == 1 && gallery.getGroups().get(0).getGroupID().equals(group.getGroupID())) {

						//Our group is the last one, delete
						log.info("Deleting gallery with id " + gallery.getGalleryID());
						galleryDAO.delete(gallery, connection);

					} else {

						//Our group is not the last one, remove ourselves
						for (Iterator<CommunityGroup> it = gallery.getGroups().iterator(); it.hasNext();) {

							CommunityGroup communityGroup = it.next();
							if (communityGroup.getGroupID().equals(group.getGroupID())) {
								it.remove();
								break;
							}
						}

						galleryDAO.update(gallery, connection, null);
					}

					deletedCount += pictureCount;
				}
			}
		} finally {

			DBUtils.closeConnection(connection);
		}
		return deletedCount;
	}

	public Integer uploadPictureForMobile(HttpServletRequest req, CommunityUser user, URIParser uriParser) throws SQLException, AccessDeniedException, URINotFoundException {

		if (StringUtils.isEmpty(filestore)) {
			throw new URINotFoundException("Filestore not set");
		}

		if (migrating) {
			throw new URINotFoundException("Waiting for module restart to migrate data");
		}

		//Fix for jsessionid not being removed from URL in tomcat 6.0.33 and later versions
		Integer galleryID = NumberUtils.toInt(JSessionIDRemover.remove(uriParser, 3));

		Gallery gallery = null;

		if (galleryID == null || (gallery = this.galleryCRUD.getBean(galleryID)) == null) {

			throw new URINotFoundException(uriParser);

		} else if (!AccessUtils.checkAdminAccess(gallery, user, GroupAccessLevel.PUBLISHER)) {

			throw new AccessDeniedException("Picture upload access in gallery " + gallery + " denied!");

		} else {

			MultipartRequest requestWrapper = null;

			InputStream is = null;

			try {

				requestWrapper = new MultipartRequest(this.ramThreshold * BinarySizes.KiloByte, this.diskThreshold * BinarySizes.MegaByte, req);

				FileItem fileItem = requestWrapper.getFile(0);

				if (fileItem != null) {

					is = fileItem.getInputStream();

					TransactionHandler transactionHandler = new TransactionHandler(dataSource);

					Picture picture = createPicture(is, FilenameUtils.getName(fileItem.getName()), gallery, user, transactionHandler);

					transactionHandler.commit();

					log.info("User " + user + " added picture " + picture + " to gallery " + gallery);

					return picture.getPictureID();
				}

			} catch (FileUploadException e) {

				log.info("Error processing image upload by user " + user + " in gallery " + gallery + ", cause: " + e);

			} catch (ValidationException e) {

				log.debug("Error processing image upload by user " + user + " in gallery " + gallery + ", cause: " + e);

			} catch (IOException e) {

				log.info("Error processing image upload by user " + user + " in gallery " + gallery + ", cause: " + e);

			} finally {

				if (requestWrapper != null) {
					requestWrapper.deleteFiles();
				}

				CloseUtils.close(is);

			}
		}

		return null;
	}

	@Override
	public ForegroundModuleResponse processRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (StringUtils.isEmpty(filestore)) {
			throw new ModuleConfigurationException("Filestore not set");
		}

		if (migrating) {
			throw new ModuleConfigurationException("Waiting for module restart to migrate data");
		}

		return super.processRequest(req, res, user, uriParser);
	}

	@Override
	public boolean moduleEnabled(CommunityGroup group) throws SQLException {

		if (StringUtils.isEmpty(filestore) || migrating) {
			return false;
		}

		return super.moduleEnabled(group);
	}

	public boolean moduleReady() {

		return !(StringUtils.isEmpty(filestore) || migrating);
	}

	public void removeGallery(Gallery bean) throws SQLException {

		HighLevelQuery<Picture> query = new HighLevelQuery<Picture>();
		query.addParameter(pictureGalleryParamFactory.getParameter(bean));

		List<Picture> pictures = pictureCRUDDAO.getAnnotatedDAO().getAll(query);

		if (!CollectionUtils.isEmpty(pictures)) {
			for (Picture picture : pictures) {
				removePictureFromDisk(picture);
			}
		}
	}

}
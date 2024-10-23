package se.dosf.communitybase.modules.filearchive;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.annotations.GroupMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.dbcleanup.DBCleaner;
import se.dosf.communitybase.modules.filearchive.beans.File;
import se.dosf.communitybase.modules.filearchive.beans.Section;
import se.dosf.communitybase.modules.filearchive.cruds.FileCRUD;
import se.dosf.communitybase.modules.filearchive.cruds.SectionCRUD;
import se.dosf.communitybase.modules.filearchive.daos.FileDAO;
import se.dosf.communitybase.modules.filearchive.daos.FileEventDAO;
import se.dosf.communitybase.modules.filearchive.daos.SectionDAO;
import se.dosf.communitybase.modules.filearchive.migration.FileArchiveDataMigrator;
import se.dosf.communitybase.modules.oldcontentremover.beans.OldContent;
import se.dosf.communitybase.modules.oldcontentremover.interfaces.OldContentRemover;
import se.dosf.communitybase.utils.AccessUtils;
import se.dosf.communitybase.utils.CommunityBaseAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.User;
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
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.ChainedResultSetPopulator;
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
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.http.enums.ContentDisposition;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class FileArchiveModule extends AnnotatedCommunityModule implements CommunityModule, CRUDCallback<CommunityUser>, DBCleaner, OldContentRemover {

	public static final Map<String, Order> SECTION_SORT_COLUMNS = new HashMap<String, Order>();
	public static final Map<String, Order> FILE_SORT_COLUMNS = new HashMap<String, Order>();
	public static final List<String> SORT_COLUMNS = new ArrayList<String>();

	static{
		SECTION_SORT_COLUMNS.put("name", Order.ASC);
		FILE_SORT_COLUMNS.put("posted", Order.DESC);
		FILE_SORT_COLUMNS.put("filename", Order.ASC);
		SORT_COLUMNS.addAll(SECTION_SORT_COLUMNS.keySet());
		SORT_COLUMNS.addAll(FILE_SORT_COLUMNS.keySet());
	}

	public static boolean isValidSectionSortColumn(String sortBy) {

		return SECTION_SORT_COLUMNS.containsKey(sortBy);
	}

	public static boolean isValidFileSortColumn(String sortBy) {

		return FILE_SORT_COLUMNS.containsKey(sortBy);
	}

	private static enum Route {
		LIST_SECTIONS, LIST_FILES;
	}

	/* Breadcrumbs */
	@XSLVariable
	protected String addSectionBreadCrumb = "Add cateogory";

	@XSLVariable
	protected String updateSectionBreadCrumb = "Update category";

	@XSLVariable
	protected String addFileBreadCrumb = "Add file";

	@XSLVariable
	protected String updateFileBreadCrumb = "Update file";

	@XSLVariable
	protected String newFileText = "New file: ";

	/* Module Settings */
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max upload size", description = "Maxmium upload size in megabytes allowed in a single post request", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer diskThreshold = 100;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "RAM threshold", description = "Maximum size of files in KB to be buffered in RAM during file uploads. Files exceeding the threshold are written to disk instead.", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer ramThreshold = 500;

	@ModuleSetting
	@TextAreaSettingDescriptor(name = "Allowed File Types", description = "Controls which filetypes are to be allowed (put each file extension on a new line ex. \".doc\")", required = false)
	private String allowedFileExtensions = ".doc\n.txt\n.odt\n.xls\n.pdf";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Base file store", description = "Directory where filestore directory is created", required = true)
	protected String filestore;

	private String[] allowedFileTypes;

	private SectionDAO sectionDAO;
	private FileDAO fileDAO;
	private FileEventDAO fileEventDAO;
	private AnnotatedDAOWrapper<File, Integer> fileCRUDDAO;
	
	private QueryParameterFactory<Section, Integer> sectionIDParamFactory;

	private SectionCRUD sectionCRUD;
	private FileCRUD fileCRUD;

	private boolean migrating;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		this.setAllowedFileTypes();

		if (!systemInterface.getInstanceHandler().addInstance(FileArchiveModule.class, this)) {

			log.warn("Another instance has already been registered in instance handler for class " + this.getClass().getName());
		}
	}

	@Override
	public void unload() throws Exception {

		super.unload();

		systemInterface.getInstanceHandler().removeInstance(FileArchiveModule.class, this);
	}

	private void setAllowedFileTypes() {

		this.allowedFileTypes = this.allowedFileExtensions.split("[\\r\\n]+");
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);

		this.setAllowedFileTypes();
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

				FileArchiveDataMigrator migrator = new FileArchiveDataMigrator(dataSource, this.log);
				migrator.run(true);

				transactionHandler.commit();
			}

		}finally{

			TransactionHandler.autoClose(transactionHandler);
		}

		// Run normal upgrade to upgrade to version 3
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, tableGroupName, scriptProvider, null, 3);

		if(upgradeResult.isUpgrade()){

			log.info(upgradeResult.toString());
		}

		currentVersion = TableVersionHandler.getTableGroupVersion(dataSource, tableGroupName);

		if (currentVersion == 3) {

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
			descriptor.setClassname(se.dosf.communitybase.modules.filearchive.migration.FileArchiveDataMigrator2.class.getName());
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

		if (currentVersion >= 4) {

			// Run normal upgrade to upgrade to latest version
			upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, tableGroupName, scriptProvider);

			if (upgradeResult.isUpgrade()) {
				log.info(upgradeResult.toString());
			}
		}

		CommunityBaseAnnotatedDAOFactory daoFactory = new CommunityBaseAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), getSchoolDAO());

		sectionDAO = new SectionDAO(dataSource, daoFactory, daoFactory.getQueryParameterPopulators(), daoFactory.getBeanStringPopulators());
		daoFactory.addDAO(Section.class, sectionDAO);

		fileDAO = new FileDAO(dataSource, daoFactory, daoFactory.getQueryParameterPopulators(), daoFactory.getBeanStringPopulators());
		daoFactory.addDAO(File.class, fileDAO);

		AnnotatedDAOWrapper<Section, Integer> sectionCRUDDAO = new AnnotatedDAOWrapper<Section, Integer>(sectionDAO, "sectionID", Integer.class);
		this.fileCRUDDAO = new AnnotatedDAOWrapper<File, Integer>(fileDAO, "fileID", Integer.class);
		this.sectionCRUD = new SectionCRUD(sectionCRUDDAO, new AnnotatedRequestPopulator<Section>(Section.class), "Section", "section", "", this);
		this.fileCRUD = new FileCRUD(fileCRUDDAO, new AnnotatedRequestPopulator<File>(File.class), "File", "file", "/show", this, sectionCRUDDAO);

		this.fileEventDAO = new FileEventDAO(dataSource, this);
		
		sectionIDParamFactory = sectionDAO.getParamFactory("sectionID", Integer.class);
	}

	/**
	 * Determines and sets user sorting preferences for listing sections or files. Preferences from request take precedence over "sticky" preferences from
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

			// Get "sticky" preferences from session
		}else if(getStickyPreferences && (session = req.getSession(false)) != null){

			sortingCriteria = (String) session.getAttribute("fileArchive.sortingPreferences.criteria");
			reverse = Boolean.parseBoolean((String) session.getAttribute("fileArchive.sortingPreferences.reverse"));
		}

		// Get default preferences
		if(sortingCriteria == null){
			sortingCriteria = "name";
		}

		// Set "sticky" preferences to session
		if(setStickyPreferences){
			this.setStickyPreferences(req, sortingCriteria, reverse);
		}

		if(isValidSectionSortColumn(sortingCriteria)){

			if(reverse){
				sortOrder = FileArchiveModule.SECTION_SORT_COLUMNS.get(sortingCriteria).equals(Order.ASC) ? Order.DESC : Order.ASC;
			}else{
				sortOrder = FileArchiveModule.SECTION_SORT_COLUMNS.get(sortingCriteria);
			}

			this.propagatePreferences(req, sortingCriteria, reverse, sortOrder);
			return Route.LIST_SECTIONS;
		}

		if(isValidFileSortColumn(sortingCriteria)){

			if(reverse){
				sortOrder = FileArchiveModule.FILE_SORT_COLUMNS.get(sortingCriteria).equals(Order.ASC) ? Order.DESC : Order.ASC;
			}else{
				sortOrder = FileArchiveModule.FILE_SORT_COLUMNS.get(sortingCriteria);
			}

			this.propagatePreferences(req, sortingCriteria, reverse, sortOrder);
			return Route.LIST_FILES;
		}

		throw new URINotFoundException(req.getRequestURI());
	}

	private void setStickyPreferences(HttpServletRequest req, String criteria, boolean reverse) {

		req.getSession().setAttribute("fileArchive.sortingPreferences.criteria", criteria);
		req.getSession().setAttribute("fileArchive.sortingPreferences.reverse", Boolean.toString(reverse));
	}

	private void propagatePreferences(HttpServletRequest req, String criteria, boolean reverse, Order order) {

		req.setAttribute("fileArchive.sortingPreferences.criteria", criteria);
		req.setAttribute("fileArchive.sortingPreferences.reverse", reverse);
		req.setAttribute("fileArchive.sortingPreferences.order", order);
	}

	private Route setSortingPreferences(HttpServletRequest req) throws URINotFoundException {

		return this.setSortingPreferences(req, true, true);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		if(this.setSortingPreferences(req).equals(Route.LIST_SECTIONS)){
			return this.sectionCRUD.list(req, res, user, uriParser, null);
		}

		return this.fileCRUD.list(req, res, user, uriParser, null);
	}

	@GroupMethod
	public ForegroundModuleResponse sections(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.setSortingPreferences(req, false, true);
		return this.sectionCRUD.list(req, res, user, uriParser, null);
	}

	@GroupMethod
	public ForegroundModuleResponse files(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.setSortingPreferences(req);
		return this.fileCRUD.list(req, res, user, uriParser, null);
	}

	@GroupMethod
	public ForegroundModuleResponse addSection(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.sectionCRUD.add(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse updateSection(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.sectionCRUD.update(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse deleteSection(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.sectionCRUD.delete(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse addFile(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		Section section = sectionCRUD.getRequestedBean(req, res, user, uriParser, null);

		if(section == null){

			return sectionCRUD.list(req, res, user, uriParser, Collections.singletonList(new ValidationError("RequestedSectionNotFound")));
		}

		req.setAttribute("section", section);

		return this.fileCRUD.add(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse updateFile(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.fileCRUD.update(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse deleteFile(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.fileCRUD.delete(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse downloadFile(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, AccessDeniedException {

		File file = null;

		if((file = this.fileCRUD.getRequestedBean(req, res, user, uriParser, null)) != null){

			if(!AccessUtils.checkReadAccess(file.getSection(), user, GroupAccessLevel.MEMBER)){
				throw new AccessDeniedException("Download  access for file " + file + " denied");
			}

			log.info("User " + user + " downloading file " + file + " from group " + group);

			this.writeFileToWeb(file, user, req, res);

			return null;
		}

		throw new URINotFoundException(uriParser);
	}

	public Element getAllowedFileTypes(Document doc) {

		Element allowedFileTypesElement = doc.createElement("allowedFileTypes");
		for(String fileExtension : this.allowedFileTypes){
			allowedFileTypesElement.appendChild(XMLUtils.createCDATAElement("extension", fileExtension, doc));
		}
		return allowedFileTypesElement;
	}

	public void writeFileToWeb(File file, CommunityUser user, HttpServletRequest req, HttpServletResponse res) throws SQLException {

		try {

			HTTPUtils.sendFile(getFileFromDisk(file), file.getFilename(), req, res, ContentDisposition.ATTACHMENT);

		} catch (IOException e) {

			this.log.info("Error sending attachment " + file + " to user " + user);
		}
	}

	public FileInputStream getFileStreamFromDisk(File file2) {

		java.io.File file = getFileFromDisk(file2);

		if (file != null) {

			try {
				return new FileInputStream(file);

			} catch (Exception e) {

				log.warn("Error reading picture from disk at " + file.getAbsolutePath(), e);
			}
		}

		return null;
	}

	public java.io.File getFileFromDisk(File file2) {

		if (StringUtils.isEmpty(filestore)) {
			return null;
		}

		String path = getFilePath(file2);

		java.io.File file = new java.io.File(path);

		if (file.exists()) {

			return file;

		} else {

			log.warn("Missing picture on disk at " + path);
		}

		return null;
	}

	protected String getFilePath(File file) {

		return getFilePath(filestore, file.getFileID());
	}

	public static String getFilePath(String filestore, Integer fileID) {

		return filestore + java.io.File.separator + "filearchive" + java.io.File.separator + fileID;
	}

	@Override
	public List<? extends Event> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {

		if (StringUtils.isEmpty(filestore) || migrating) {
			return null;
		}

		return fileEventDAO.getGroupResume(group, user, startStamp, newFileText);
	}

	public boolean hasUpdateAccess(Section section, CommunityUser user) {

		return AccessUtils.checkAdminAccess(section, user, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER);
	}

	public boolean hasUpdateAccess(File file, CommunityUser user) {

		return AccessUtils.checkAdminAccess(file.getSection(), user, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER);
	}

	public void appendGroupAndAccess(Document doc, CommunityGroup group, CommunityUser user) {

		Element document = doc.getDocumentElement();

		if(group != null){

			document.appendChild(group.toXML(doc));

		}

		if(AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)){

			XMLUtils.appendNewElement(doc, document, "isAdmin", "true");

		}

		if(user.isAdmin()){

			XMLUtils.appendNewElement(doc, document, "isSysAdmin", "true");

		}

	}

	public Breadcrumb getAddSectionBreadCrumb(CommunityGroup group) {

		return this.getMethodBreadcrumb(this.addSectionBreadCrumb, "addSection", group);
	}

	public Breadcrumb getAddFileBreadCrumb(CommunityGroup group) {

		return this.getMethodBreadcrumb(this.addFileBreadCrumb, "addFile", group);
	}

	public Breadcrumb getUpdateBreadCrumb(CommunityGroup group, Section section) {

		return this.getMethodBreadcrumb(this.updateSectionBreadCrumb, "updateSection" + "/" + section.getSectionID(), group);
	}

	public Breadcrumb getUpdateBreadCrumb(CommunityGroup group, File file) {

		return this.getMethodBreadcrumb(this.updateFileBreadCrumb, "updateFile" + "/" + file.getFileID(), group);
	}

	private Breadcrumb getMethodBreadcrumb(String name, String methodUri, CommunityGroup group) {

		return new Breadcrumb(name, this.getFullAlias() + "/" + group.getGroupID() + "/" + methodUri);
	}

	public Integer getDiskThreshold() {

		return diskThreshold;
	}

	public Integer getRamThreshold() {

		return ramThreshold;
	}

	public String[] getAllowedFileTypes() {

		return allowedFileTypes;
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

	public SectionDAO getSectionDAO() {

		return sectionDAO;
	}

	public FileDAO getFileDAO() {

		return fileDAO;
	}

	private List<Integer> getOrphanedFileSectionsIDs() throws SQLException {

		String sql = "SELECT DISTINCT fs.sectionID FROM filearchive_sections fs LEFT OUTER JOIN filearchive_sectiongroups g ON fs.sectionID = g.sectionID LEFT OUTER JOIN filearchive_sectionschools s ON fs.sectionID = s.sectionID WHERE groupID IS null AND schoolID is null AND global = 0";

		ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(this.dataSource, sql, new IntegerPopulator());

		return query.executeQuery();
	}

	public Section getSectionByID(Integer sectionID) throws SQLException {

		HighLevelQuery<Section> query = new HighLevelQuery<Section>();
		query.addParameter(sectionIDParamFactory.getParameter(sectionID));

		return sectionDAO.get(query);
	}

	@Override
	public void cleanDB() throws SQLException {

		if (StringUtils.isEmpty(filestore) || migrating) {
			return;
		}

		List<Integer> orphans = getOrphanedFileSectionsIDs();

		if(orphans != null && orphans.size() > 0){
			log.warn("There are " + orphans.size() + " orphaned file sections.");
			
			Connection connection = null;

			try {
				connection = dataSource.getConnection();

				for (Integer id : orphans) {
					HighLevelQuery<Section> query = new HighLevelQuery<Section>();
					query.addParameter(sectionIDParamFactory.getParameter(id));

					log.info("Deleting file section with id " + id);
					sectionDAO.delete(query, connection);
				}

			} finally {

				DBUtils.closeConnection(connection);
			}
		}

	}

	@Override
	public int getOldContentCount(CommunityUser user, CommunityGroup group, Date endDate) throws SQLException {

		if (StringUtils.isEmpty(filestore) || migrating) {
			return 0;
		}

		ObjectQuery<Integer> query = new ObjectQuery<Integer>(dataSource, "SELECT COUNT(fileID) FROM filearchive_files f WHERE sectionID IN (SELECT DISTINCT n.sectionID FROM " + sectionDAO.getTableName() + " AS n " +
			"LEFT OUTER JOIN filearchive_sectiongroups AS sg ON n.sectionID = sg.sectionID " +
			"LEFT OUTER JOIN (SELECT sectionID, MAX(posted) AS posted FROM filearchive_files GROUP BY sectionID) AS p ON n.sectionID = p.sectionID " +
				"WHERE sg.groupID = ? AND n.global = 0 AND posted <= ? ORDER BY posted ASC)",
				IntegerPopulator.getPopulator());

		query.setInt(1, group.getGroupID());
		query.setObject(2, new Timestamp(endDate.getTime()));

		return query.executeQuery();
	}

	@Override
	public Collection<OldContent> getOldContent(CommunityUser user, CommunityGroup group, Date endDate) throws SQLException {

		if (StringUtils.isEmpty(filestore) || migrating) {
			return null;
		}

		Connection connection = null;

		try {
			connection = dataSource.getConnection();

			LowLevelQuery<Section> query = new LowLevelQuery<Section>("SELECT DISTINCT n.sectionID, n.name, n.global, p.posted FROM " + sectionDAO.getTableName() + " AS n " +
					"LEFT OUTER JOIN filearchive_sectiongroups AS sg ON n.sectionID = sg.sectionID " +
					"LEFT OUTER JOIN (SELECT sectionID, MAX(posted) AS posted FROM filearchive_files GROUP BY sectionID) AS p ON n.sectionID = p.sectionID " +
					"WHERE sg.groupID = ? AND n.global = 0 AND posted <= ? ORDER BY posted ASC");

			query.addParameters(group.getGroupID(), new Timestamp(endDate.getTime()));
			
			query.addChainedResultSetPopulator(new ChainedResultSetPopulator<Section>() {

				@Override
				public void populate(Section bean, ResultSet resultSet) throws SQLException {

					bean.setPosted(resultSet.getTimestamp("posted"));

				}
			});

			List<Section> sections = sectionDAO.getAll(query, connection);

			if (sections != null) {
				Collection<OldContent> content = new ArrayList<OldContent>();

				for (Section section : sections) {

					if (hasUpdateAccess(section, user)) {

						Integer fileCount = getFileCount(section, connection);

						content.add(new OldContent(section.getSectionID(), section.getName() + " (" + fileCount + " %FILES%)", getFullAlias(group) + "#" + section.getSectionID(), section.getPosted(), Section.class.getName().toString()));
					}
				}

				return content;
			}

		} finally {

			DBUtils.closeConnection(connection);
		}

		return null;
	}

	private Integer getFileCount(Section section, Connection connection) throws SQLException {

		ObjectQuery<Integer> pictureCountQuery = new ObjectQuery<Integer>(connection, false, "SELECT COUNT(fileID) FROM filearchive_files p WHERE sectionID = ?", IntegerPopulator.getPopulator());

		pictureCountQuery.setInt(1, section.getSectionID());

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

				HighLevelQuery<Section> query = new HighLevelQuery<Section>();
				query.addParameter(sectionIDParamFactory.getParameter(oldContent.getID()));

				Section section = sectionDAO.get(query, connection);

				if (section == null) {
					continue;
				}

				if (hasUpdateAccess(section, user) && !section.isGlobal()) {

					Integer fileCount = getFileCount(section, connection);

					if (section.getSchools() == null && section.getGroups().size() == 1 && section.getGroups().get(0).getGroupID().equals(group.getGroupID())) {

						//Our group is the last one, delete
						log.info("Deleting file section with id " + section.getSectionID());
						sectionDAO.delete(section, connection);

					} else {

						//Our group is not the last one, remove ourselves
						for (Iterator<CommunityGroup> it = section.getGroups().iterator(); it.hasNext();) {

							CommunityGroup communityGroup = it.next();
							if (communityGroup.getGroupID().equals(group.getGroupID())) {
								it.remove();
								break;
							}
						}

						sectionDAO.update(section, connection, null);
					}

					deletedCount += fileCount;
				}
			}
		} finally {

			DBUtils.closeConnection(connection);
		}
		return deletedCount;
	}

	@Override
	public boolean moduleEnabled(CommunityGroup group) throws SQLException {

		if (StringUtils.isEmpty(filestore) || migrating) {
			return false;
		}

		return super.moduleEnabled(group);
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

	public void writeFileToDisk(File bean, FileItem fileItem) throws IOException {

		log.info("Writing file " + bean + " to disk from section " + bean.getSection());

		java.io.File file = new java.io.File(getFilePath(bean));
		FileUtils.createMissingDirectories(file);
		FileUtils.writeFile(file, fileItem.getInputStream(), true);
	}

	public void removeFileFromDisk(File file2){
		
		java.io.File file = new java.io.File(getFilePath(file2));
		
		if (file.exists()) {

			if (!FileUtils.deleteFile(file)) {

				log.warn("Unable to delete file " + file + " belonging to file " + file2 + " of section " + file2.getSection());
			}

		} else {

			log.warn("Attempted to delete missing file " + file + " belonging to file " + file2 + " of section " + file2.getSection());
		}

		log.info("Removed file " + file + " from disk belonging to section " + file2.getSection());
	}

	public boolean moduleReady() {

		return !(StringUtils.isEmpty(filestore) || migrating);
	}

}
package se.dosf.communitybase.modules.filearchive.cruds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.cruds.IntegerBasedCommunityBaseCRUD;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.filearchive.FileArchiveModule;
import se.dosf.communitybase.modules.filearchive.beans.File;
import se.dosf.communitybase.modules.filearchive.beans.Section;
import se.dosf.communitybase.utils.AccessUtils;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.validation.ValidationUtils;

public class FileCRUD extends IntegerBasedCommunityBaseCRUD<File, FileArchiveModule> {

	private final AnnotatedDAOWrapper<Section, Integer> sectionDAO;
	private FileArchiveModule fileArchiveModule;

	public FileCRUD(AnnotatedDAOWrapper<File, Integer> fileCRUDDAO, BeanRequestPopulator<File> populator, String typeElementName, String typeLogName, String listMethodAlias, FileArchiveModule fileArchiveModule, AnnotatedDAOWrapper<Section, Integer> sectionDAO) {

		super(fileCRUDDAO, populator, typeElementName, typeLogName, listMethodAlias, fileArchiveModule);

		this.sectionDAO = sectionDAO;
		this.fileArchiveModule = fileArchiveModule;
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws Exception {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		this.fileArchiveModule.appendGroupAndAccess(doc, group, user);

		addTypeElement.appendChild(this.fileArchiveModule.getAllowedFileTypes(doc));

		XMLUtils.appendNewElement(doc, addTypeElement, "diskThreshold", this.fileArchiveModule.getDiskThreshold());

		addTypeElement.appendChild(((Section)req.getAttribute("section")).toXML(doc));
	}

	@Override
	protected void appendUpdateFormData(File bean, Document doc, Element updateTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws Exception {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		this.fileArchiveModule.appendGroupAndAccess(doc, group, user);

		// Get sections that are accessible from any of my schools and groups
		LowLevelQuery<Section> query = new LowLevelQuery<Section>();

		String sql = "SELECT DISTINCT filearchive_sections.* " +
				"FROM filearchive_sections " +
				"LEFT JOIN filearchive_sectiongroups ON filearchive_sections.sectionID = filearchive_sectiongroups.sectionID " +
				"LEFT JOIN filearchive_sectionschools ON filearchive_sections.sectionID = filearchive_sectionschools.sectionID " +
				"WHERE filearchive_sectiongroups.groupID = ? OR filearchive_sectionschools.schoolID = ? OR filearchive_sections.global = ?";

		query.setSql(sql);
		query.addParameters(group.getGroupID(), group.getSchool().getSchoolID(), true);
		query.disableAutoRelations(true);

		List<Section> sections = this.sectionDAO.getAnnotatedDAO().getAll(query);

		if(sections != null) {
			XMLUtils.append(doc, updateTypeElement, sections);
		}
	}

	/**
	 * Sets file attributes from file item on file object, such as filename, size, and binary data
	 * 
	 * @param file
	 * @param req
	 * @return
	 * @throws ValidationException
	 */
	private FileItem setFileAttributes(File file, MultipartRequest req) throws ValidationException {

		if(req.getFileCount() >= 1){

			FileItem fileItem = req.getFile(0);

			if(StringUtils.isEmpty(fileItem.getName())){

				throw new ValidationException(new ValidationError("NoFileAttached"));

			}else{
				
				if (fileItem.getSize() - 1 < 0) {
					throw new ValidationException(new ValidationError("FileTooSmall"));
				}

				file.setFilename(FileUtils.toValidHttpFilename(FilenameUtils.getName(fileItem.getName())));
				file.setSize(fileItem.getSize());

				return fileItem;
			}

		}else{
			throw new ValidationException(new ValidationError("NoFileAttached"));
		}
	}

	private void checkFileExtension(File file) throws ValidationException {

		for(String fileExtension : this.fileArchiveModule.getAllowedFileTypes()){

			if(file.getFilename().trim().toLowerCase().endsWith(fileExtension)){

				return;
			}
		}

		throw new ValidationException(new ValidationError("InvalidFileFormat"));
	}

	/**
	 * Common method for populating file object from request
	 * @param file
	 * @param req
	 * @param user
	 * @return
	 * @throws SQLException
	 * @throws ValidationException
	 */

	@Override
	protected File populateFromAddRequest(HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, Exception {

		File file = super.populateFromAddRequest(req, user, uriParser);

		file.setSection((Section)req.getAttribute("section"));

		FileItem fileItem = setFileAttributes(file, (MultipartRequest) req);

		req.setAttribute("fileItem", fileItem);

		file.setPoster(user);
		
		this.checkFileExtension(file);

		return file;
		
	}

	@Override
	protected File populateFromUpdateRequest(File bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, Exception {
		
		File file = super.populateFromUpdateRequest(bean, req, user, uriParser);

		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		Integer sectionID = ValidationUtils.validateParameter("sectionID", req, true, IntegerPopulator.getPopulator(), errors);
		
		Section section = null;
		
		if(sectionID != null) {
			
			section = sectionDAO.get(sectionID);
			
			if(section == null) {
				errors.add(new ValidationError("RequestedSectionNotFound"));
			}
			
		}
		
		if(!errors.isEmpty()) {
			throw new ValidationException(errors);
		}
		
		file.setSection(section);
		
		return file;
		
	}
	
	@Override
	protected void appendListFormData(Document doc, Element listTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationError) throws SQLException {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		listTypeElement.appendChild(XMLUtils.createElement("userLastLoginInMillis", String.valueOf(user.getLastLogin().getTime()), doc));
		XMLUtils.append(doc, listTypeElement, "SortingCriterias", "Criteria", FileArchiveModule.SORT_COLUMNS);

		// Append sorting preferences as request parameters
		Element requestParametersElement = RequestUtils.getRequestParameters(req, doc);
		Element param1 = doc.createElement("parameter");
		XMLUtils.appendNewElement(doc, param1, "name", "orderby");
		XMLUtils.appendNewElement(doc, param1, "value", req.getAttribute("fileArchive.sortingPreferences.criteria"));
		Element param2 = doc.createElement("parameter");
		XMLUtils.appendNewElement(doc, param2, "name", "reverse");
		XMLUtils.appendNewElement(doc, param2, "value", req.getAttribute("fileArchive.sortingPreferences.reverse"));
		requestParametersElement.appendChild(param1);
		requestParametersElement.appendChild(param2);
		listTypeElement.appendChild(requestParametersElement);

		this.fileArchiveModule.appendGroupAndAccess(doc, group, user);

		super.appendListFormData(doc, listTypeElement, user, req, uriParser, validationError);
	}

	@Override
	protected void appendAllBeans(Document doc, Element listTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationError) throws SQLException {
		
		List<File> files = getAllBeans(user, req, uriParser);
		
		if(!CollectionUtils.isEmpty(files)) {
			
			Element filesElement = XMLUtils.appendNewElement(doc, listTypeElement, this.typeElementPluralName);
			
			for(File file : files) {
				
				Element fileElement = file.toXML(doc);
				
				if(callback.hasUpdateAccess(file, user)) {
					
					XMLUtils.appendNewElement(doc, fileElement, "hasUpdateAccess", "true");
					
				}
				
				filesElement.appendChild(fileElement);
				
			}
			
		}
		
	}
	
	@Override
	protected List<File> getAllBeans(CommunityUser user, HttpServletRequest req, URIParser uriParser) throws SQLException {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return callback.getFileDAO().getFiles(group, (String)req.getAttribute("fileArchive.sortingPreferences.criteria"), (Order)req.getAttribute("fileArchive.sortingPreferences.order"));
	}

	@Override
	protected HttpServletRequest parseRequest(HttpServletRequest req, CommunityUser user) throws ValidationException {

		if(MultipartRequest.isMultipartRequest(req)){

			try {
				return new MultipartRequest(this.fileArchiveModule.getRamThreshold() * BinarySizes.KiloByte, this.fileArchiveModule.getDiskThreshold() * BinarySizes.MegaByte, req);

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
	protected void releaseRequest(HttpServletRequest req, CommunityUser user) {

		if (req instanceof MultipartRequest) {

			((MultipartRequest)req).deleteFiles();
		}
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(File bean, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		return super.beanDeleted(null, req, res, user, uriParser);

	}

	@Override
	protected void redirectToListMethod(HttpServletRequest req, HttpServletResponse res, File bean) throws Exception {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		if(bean != null) {
			res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/" + group.getGroupID() + "#" + bean.getSection().getSectionID());
		} else {
			res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/" + group.getGroupID());
		}

	}

	@Override
	protected List<Breadcrumb> getAddBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser) {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(this.fileArchiveModule.getGroupBreadcrumb(group), this.fileArchiveModule.getModuleBreadcrumb(group), this.fileArchiveModule.getAddFileBreadCrumb(group));
	}

	@Override
	protected List<Breadcrumb> getUpdateBreadcrumbs(File bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws Exception {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(this.fileArchiveModule.getGroupBreadcrumb(group), this.fileArchiveModule.getModuleBreadcrumb(group), this.fileArchiveModule.getUpdateBreadCrumb(group, bean));
	}

	@Override
	protected List<Breadcrumb> getShowBreadcrumbs(File bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(this.fileArchiveModule.getGroupBreadcrumb(group), this.fileArchiveModule.getModuleBreadcrumb(group));
	}

	@Override
	protected List<Breadcrumb> getListBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser, List<ValidationError> validationError) {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(this.fileArchiveModule.getGroupBreadcrumb(group), this.fileArchiveModule.getModuleBreadcrumb(group));
	}

	@Override
	public File getRequestedBean(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, String getMode) throws SQLException, AccessDeniedException {

		File file = super.getRequestedBean(req, res, user, uriParser, getMode);
		this.checkShowAccess(file, user, req, uriParser);

		return file;
	}

	@Override
	protected void checkShowAccess(File bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {

		if(bean != null && !AccessUtils.checkReadAccess(bean.getSection(), user, GroupAccessLevel.MEMBER)) {
			throw new AccessDeniedException("Show access for file denied");
		}
	}

	@Override
	protected void checkAddAccess(CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		Section section = (Section)req.getAttribute("section");

		if(!this.fileArchiveModule.hasUpdateAccess(section, user)) {
			throw new AccessDeniedException("Add file access denied for section " + section);
		}
	}

	@Override
	protected void checkUpdateAccess(File bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {

		if(!this.fileArchiveModule.hasUpdateAccess(bean, user)) {
			throw new AccessDeniedException("Update access for file " + bean + " denied");
		}
	}

	@Override
	protected void checkDeleteAccess(File bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {

		if(!this.fileArchiveModule.hasUpdateAccess(bean, user)) {
			throw new AccessDeniedException("Delete access for file " + bean + " denied");
		}
	}

	@Override
	protected void addBean(File bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws Exception {

		TransactionHandler transactionHandler = ((AnnotatedDAOWrapper<File, Integer>) crudDAO).createTransaction();

		this.crudDAO.add(bean, transactionHandler);

		callback.writeFileToDisk(bean, (FileItem) req.getAttribute("fileItem"));

		transactionHandler.commit();
	}

}
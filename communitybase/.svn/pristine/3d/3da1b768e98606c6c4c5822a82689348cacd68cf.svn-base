package se.dosf.communitybase.modules.filearchive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.annotations.GroupMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.beans.SimpleEvent;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.filearchive.beans.File;
import se.dosf.communitybase.modules.filearchive.beans.Section;
import se.dosf.communitybase.modules.filearchive.populators.FilePopulator;
import se.dosf.communitybase.modules.filearchive.populators.SectionPopulator;
import se.dosf.communitybase.utils.AccessUtils;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.enums.URLType;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.SettingHandler;
import se.unlogic.hierarchy.foregroundmodules.ModuleSetting;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class FileArchiveModule extends AnnotatedCommunityModule implements CommunityModule {

	private static final ArrayList<SettingDescriptor> SETTINGDESCRIPTORS = new ArrayList<SettingDescriptor>();

	static{
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextAreaSetting("allowedFileTypes", "Allowed File Types", "Controls which filetypes are to be allowed(put each file extension on a new line ex. \".doc\")", false, ".doc\n.txt\n.odt\n.xls\n.pdf", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("diskThreshold", "Max upload size", "Maxmium upload size in megabytes allowed in a single post request", false, "100", new StringIntegerValidator(1,null)));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("ramThreshold", "RAM threshold", "How many megabytes of RAM to use as buffer during file uploads. If the threshold is exceeded the files are written to disk instead.", false, "20", new StringIntegerValidator(1,null)));
	}

	@XSLVariable
	protected String addSectionBreadcrumb = "Add cateogory";

	@XSLVariable
	protected String updateGroupCategoryBreadcrumb = "Update groupcategory";

	@XSLVariable
	protected String updateSchoolCategoryBreadcrumb = "Update schoolcategory";

	@XSLVariable
	protected String addFileBreadcrumb = "Add file";

	@XSLVariable
	protected String updateFileBreadcrumb = "Update file";

	@ModuleSetting
	protected Integer diskThreshold = 100;

	@ModuleSetting
	protected Integer ramThreshold = 20;

	private static FilePopulator fileArchiveFilePopulator = new FilePopulator();
	private static SectionPopulator fileArchiveSectionPopulator = new SectionPopulator();
	private FilearchiveDAO filearchiveModuleDAO;
	private UserHandler userHandler;

	private String[] allowedFileTypes = {".doc", ".txt", ".odt", ".xls", ".pdf" };

	@XSLVariable
	protected String newFileText = "New file: ";

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		this.userHandler = sectionInterface.getSystemInterface().getUserHandler();

		this.filearchiveModuleDAO = new FilearchiveDAO(dataSource, userHandler);
		this.checkSettings(moduleDescriptor.getSettingHandler());
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {
		super.update(moduleDescriptor, dataSource);

		this.userHandler = sectionInterface.getSystemInterface().getUserHandler();

		this.filearchiveModuleDAO = new FilearchiveDAO(dataSource, userHandler);
		this.checkSettings(moduleDescriptor.getSettingHandler());
	}

	private void checkSettings(SettingHandler settingHandler) {

		String allowedFileTypesString = settingHandler.getString("allowedFileTypes");

		if(!StringUtils.isEmpty(allowedFileTypesString)){

			String[] allowedFileTypes = allowedFileTypesString.split("\n");

			for(int i = 0; i<allowedFileTypes.length; i++){

				allowedFileTypes[i] = allowedFileTypes[i].replace("\n", "");
				allowedFileTypes[i] = allowedFileTypes[i].replace("\r", "");
			}

			this.allowedFileTypes = allowedFileTypes;
		}

	}

	//TODO cleanup using standard toXML methods...

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		log.info("User " + user + " requesting links for group " + group);

		// TODO skapa kommunalt

		Document doc = this.createDocument(req, uriParser, group);

		Element fileArchiveElement = doc.createElement("fileArchiveModule");

		if(user.getLastLogin() != null){
			fileArchiveElement.appendChild(XMLUtils.createElement("userLastLoginInMillis", String.valueOf(user.getLastLogin().getTime()), doc));
		}

		Element groupArchiveElement = doc.createElement("groupFiles");
		Element schoolArchiveElement = doc.createElement("schoolFiles");

		if(AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)) {
			fileArchiveElement.appendChild(XMLUtils.createElement("admin", "true", doc));
		}

		doc.getFirstChild().appendChild(fileArchiveElement);

		ArrayList<Section> groupSections = this.filearchiveModuleDAO.getGroupSectionName(group.getGroupID());
		Element sectionElement = null;

		if (groupSections != null) {
			for (Section groupSection : groupSections) {
				sectionElement = groupSection.toXML(doc);
				sectionElement.appendChild(XMLUtils.createElement("sectionType", "Group", doc));
				groupArchiveElement.appendChild(sectionElement);

				ArrayList<File> files = this.filearchiveModuleDAO.getGroupFiles(group.getGroupID(), groupSection.getSectionID());
				Element fileElement = null;

				if (files != null) {
					for (File file : files) {
						fileElement = file.toXML(doc);

						if(file.getPoster() != null){

							if(AccessUtils.checkAccess(user, group, GroupAccessLevel.MEMBER) && file.getPoster().getUserID().equals(user.getUserID())){
								XMLUtils.appendNewElement(doc, fileElement, "owner", "");
							}

						}

						sectionElement.appendChild(fileElement);
						files = null;
					}
				} else {
					Element noFileElement = doc.createElement("noFiles");
					sectionElement.appendChild(noFileElement);
				}
			}
			fileArchiveElement.appendChild(groupArchiveElement);
		} else {
			groupArchiveElement.appendChild(XMLUtils.createElement("noSection", "", doc));
			fileArchiveElement.appendChild(groupArchiveElement);
		}

		ArrayList<Section> schoolSections = this.filearchiveModuleDAO.getSchoolSectionName(group.getSchool().getSchoolID());

		if (schoolSections != null) {
			for (Section schoolSection : schoolSections) {
				sectionElement = schoolSection.toXML(doc);
				sectionElement.appendChild(XMLUtils.createElement("sectionType", "School", doc));
				schoolArchiveElement.appendChild(sectionElement);

				ArrayList<File> files = this.filearchiveModuleDAO.getSchoolFiles(group.getSchool().getSchoolID(), schoolSection.getSectionID());
				Element fileElement = null;

				if (files != null) {
					for (File file : files) {
						fileElement = file.toXML(doc);
						sectionElement.appendChild(fileElement);
						files = null;
					}
				} else {
					Element noFileElement = doc.createElement("noFiles");
					sectionElement.appendChild(noFileElement);
				}
			}
			fileArchiveElement.appendChild(schoolArchiveElement);
		} else {
			schoolArchiveElement.appendChild(XMLUtils.createElement("noSection", "", doc));
			fileArchiveElement.appendChild(schoolArchiveElement);
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getDefaultBreadcrumb());
	}

	@GroupMethod
	public ForegroundModuleResponse addGroupSection(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.checkAdminAccess(user, group);

		ValidationException validationException = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {
			try {
				Section section = fileArchiveSectionPopulator.populate(req);

				log.info("User " + user + " adding section " + section + " to group " + group);

				this.filearchiveModuleDAO.addGroupSection(group, section);

				res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID());

			} catch (ValidationException e) {
				validationException = e;
			}

		}

		Document doc = this.createDocument(req, uriParser, group);
		Element addGroupSectionElement = doc.createElement("addGroupSection");
		addGroupSectionElement.appendChild(XMLUtils.createElement("groupID", group.getGroupID().toString(), doc));
		doc.getFirstChild().appendChild(addGroupSectionElement);


		if (validationException != null) {
			addGroupSectionElement.appendChild(validationException.toXML(doc));
			addGroupSectionElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(addSectionBreadcrumb, "", "addGroupSection", group));

	}

	@GroupMethod
	public ForegroundModuleResponse updateGroupSection(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.checkAdminAccess(user, group);

		Section section = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((section = this.filearchiveModuleDAO.getSection(Integer.valueOf(uriParser.get(3)))) != null)) {

			ValidationException validationException = null;

			if (req.getMethod().equalsIgnoreCase("POST")) {
				try {
					section = fileArchiveSectionPopulator.populate(section, req);

					log.info("User " + user + " updating section " + section + " in group " + group);

					this.filearchiveModuleDAO.updateGroupSection(section);

					res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID());

				} catch (ValidationException e) {
					validationException = e;
				}

			}
			Document doc = this.createDocument(req, uriParser, group);

			Element updateGroupSectionElement = doc.createElement("updateGroupSection");
			doc.getFirstChild().appendChild(updateGroupSectionElement);
			updateGroupSectionElement.appendChild(section.toXML(doc));

			if (validationException != null) {
				updateGroupSectionElement.appendChild(validationException.toXML(doc));
				updateGroupSectionElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(updateGroupCategoryBreadcrumb, "", "updateGroupSection", group));

		} else {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
		}

	}

	private Breadcrumb getMethodBreadcrumb(String name, String description, String method, CommunityGroup group) {

		return new Breadcrumb(name, description, this.getFullAlias() + "/" + group.getGroupID() + "/" + method, URLType.FULL);
	}

	@GroupMethod
	public ForegroundModuleResponse deleteGroupSection(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, ValidationException, AccessDeniedException {

		this.checkAdminAccess(user, group);

		Section section = null;
		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((section = this.filearchiveModuleDAO.getSection(Integer.valueOf(uriParser.get(3)))) != null)) {

			log.info("User " + user + " deleting section " + section + " from group " + group);

			this.filearchiveModuleDAO.deleteGroupSection(section.getSectionID());

			res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID());

			return null;

		} else {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
		}
	}

	@GroupMethod
	public ForegroundModuleResponse addGroupFile(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.checkAdminAccess(user, group);

		Section section = null;
		ValidationException validationException = null;
		MultipartRequest requestWrapper = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((section = this.filearchiveModuleDAO.getSection(Integer.valueOf(uriParser.get(3)))) != null)) {
			if (req.getMethod().equalsIgnoreCase("POST")) {
				try {
					requestWrapper = new MultipartRequest(this.ramThreshold * BinarySizes.MegaByte, this.diskThreshold * BinarySizes.MegaByte, req);

					File file = new File();
					file.setSectionID(section.getSectionID());

					file = fileArchiveFilePopulator.populate(file, requestWrapper);

					this.checkFileExtension(file);

					log.info("User " + user + " adding file " + file + " to section " + section + " in group " + group);

					this.filearchiveModuleDAO.addGroupFile(user, file);

					res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID());

				} catch (ValidationException e) {
					validationException = e;
				} catch (FileSizeLimitExceededException e){

					log.info("User " + user + " tried to upload a file that exceeds the limit in configuration");

					validationException = new ValidationException(new ValidationError("FileTooBig"));

				} catch (FileUploadException e) {

					log.info("Error uploading file in section " + section + " caused by read time out, uploaded by user " + user);

				}

			}
		} else {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor, uriParser);
		}

		Document doc = this.createDocument(req, uriParser, group);
		Element addGroupLinkElement = doc.createElement("addGroupFile");
		addGroupLinkElement.appendChild(XMLUtils.createElement("groupID", group.getGroupID().toString(), doc));
		XMLUtils.appendNewElement(doc, addGroupLinkElement, "diskThreshold", diskThreshold);
		doc.getFirstChild().appendChild(addGroupLinkElement);

		addGroupLinkElement.appendChild(getAllowedFileTypes(doc));

		if (validationException != null) {
			addGroupLinkElement.appendChild(validationException.toXML(doc));
			if(requestWrapper != null){
				addGroupLinkElement.appendChild(RequestUtils.getRequestParameters(requestWrapper, doc));
			}
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(addFileBreadcrumb, "", "addGroupFile", group));

	}

	private void checkFileExtension(File file) throws ValidationException {

		for(String fileExtension : allowedFileTypes){

			if(file.getFileName().trim().toLowerCase().endsWith(fileExtension)){

				return;
			}
		}

		throw new ValidationException(new ValidationError("InvalidFileFormat"));
	}

	@GroupMethod
	public ForegroundModuleResponse updateGroupFile(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.checkAdminAccess(user, group);

		Integer groupID = group.getGroupID();
		File file = null;
		ValidationException validationException = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && (file = this.filearchiveModuleDAO.getGroupFile(Integer.valueOf(uriParser.get(3)),group.getGroupID(),false)) != null) {

			if (req.getMethod().equalsIgnoreCase("POST")) {

				try {

					file = fileArchiveFilePopulator.populate(file, req);

					log.info("User " + user + " updating file " + file + " in group " + group);

					this.filearchiveModuleDAO.updateGroupFile(file);

					res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID());

				} catch (ValidationException e) {
					validationException = e;
				}

			}
		} else {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor, uriParser);
		}

		ArrayList<Section> sections = this.filearchiveModuleDAO.getGroupSections(groupID);

		Document doc = this.createDocument(req, uriParser, group);
		Element updateGroupFileElement = doc.createElement("updateGroupFile");
		updateGroupFileElement.appendChild(XMLUtils.createElement("groupID", group.getGroupID().toString(), doc));

		if (sections != null) {
			for (Section section : sections) {
				updateGroupFileElement.appendChild(section.toXML(doc));
			}
		}

		updateGroupFileElement.appendChild(file.toXML(doc));

		doc.getFirstChild().appendChild(updateGroupFileElement);

		if (validationException != null) {
			updateGroupFileElement.appendChild(validationException.toXML(doc));
			updateGroupFileElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(updateFileBreadcrumb, "", "updateGroupFile", group));

	}

	@GroupMethod
	public ForegroundModuleResponse deleteGroupFile(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		this.checkAdminAccess(user, group);

		File file = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((file = this.filearchiveModuleDAO.getGroupFile(Integer.valueOf(uriParser.get(3)),group.getGroupID(),false)) != null)) {

			log.info("User " + user + " deleting file " + file + " from group " + group);

			this.filearchiveModuleDAO.deleteGroupFile(file.getFileID());

			res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID());

			return null;

		} else {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
		}

	}

	@GroupMethod
	public ForegroundModuleResponse downloadGroupFile(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException {

		// Download file
		File file = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && (file = this.filearchiveModuleDAO.getGroupFile(Integer.parseInt(uriParser.get(3)),group.getGroupID(),true)) != null) {

			log.info("User " + user + " downloading file " + file + " from group " + group);

			this.getFile(file, user, res);

			return null;

		} else {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
		}
	}

	@GroupMethod
	public ForegroundModuleResponse addSchoolSection(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.checkAdminAccess(user, group);

		ValidationException validationException = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {
			try {
				Section section = fileArchiveSectionPopulator.populate(req);

				log.info("User " + user + " adding section " + section + " to school " + group.getSchool());

				this.filearchiveModuleDAO.addSchoolSection(group, section);

				res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID());

			} catch (ValidationException e) {
				validationException = e;
			}

		}

		Document doc = this.createDocument(req, uriParser, group);
		Element addGroupSectionElement = doc.createElement("addSchoolSection");
		addGroupSectionElement.appendChild(XMLUtils.createElement("groupID", group.getGroupID().toString(), doc));
		doc.getFirstChild().appendChild(addGroupSectionElement);

		if (validationException != null) {
			addGroupSectionElement.appendChild(validationException.toXML(doc));
			addGroupSectionElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(updateSchoolCategoryBreadcrumb, "", "addSchoolSection", group));

	}

	@GroupMethod
	public ForegroundModuleResponse updateSchoolSection(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.checkAdminAccess(user, group);

		Section section = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((section = this.filearchiveModuleDAO.getSchoolSection(Integer.valueOf(uriParser.get(3)),group.getSchool().getSchoolID())) != null)) {

			ValidationException validationException = null;

			if (req.getMethod().equalsIgnoreCase("POST")) {
				try {
					section = fileArchiveSectionPopulator.populate(section, req);

					log.info("User " + user + " updating section " + section + " in school " + group.getSchool());

					this.filearchiveModuleDAO.updateSchoolSection(section);

					res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID());

				} catch (ValidationException e) {
					validationException = e;
				}

			}
			Document doc = this.createDocument(req, uriParser, group);

			Element updateGroupSectionElement = doc.createElement("updateSchoolSection");
			doc.getFirstChild().appendChild(updateGroupSectionElement);
			updateGroupSectionElement.appendChild(section.toXML(doc));

			if (validationException != null) {
				updateGroupSectionElement.appendChild(validationException.toXML(doc));
				updateGroupSectionElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(updateSchoolCategoryBreadcrumb, "", "updateSchoolSection", group));

		} else {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
		}
	}

	@GroupMethod
	public ForegroundModuleResponse deleteSchoolSection(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.checkAdminAccess(user, group);

		Section section = null;
		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((section = this.filearchiveModuleDAO.getSchoolSection(Integer.valueOf(uriParser.get(3)),group.getSchool().getSchoolID())) != null)) {

			log.info("User " + user + " deleting section " + section + " from school " + group.getSchool());

			this.filearchiveModuleDAO.deleteSchoolSection(section.getSectionID());

			res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID());

			return null;

		} else {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
		}
	}

	@GroupMethod
	public ForegroundModuleResponse addSchoolFile(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.checkAdminAccess(user, group.getSchool());

		Section section = null;
		ValidationException validationException = null;
		MultipartRequest requestWrapper = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((section = this.filearchiveModuleDAO.getSchoolSection(Integer.valueOf(uriParser.get(3)),group.getSchool().getSchoolID())) != null)) {
			if (req.getMethod().equalsIgnoreCase("POST")) {
				try {
					requestWrapper = new MultipartRequest(this.ramThreshold * BinarySizes.MegaByte, this.diskThreshold * BinarySizes.MegaByte, req);

					File file = new File();
					file.setSectionID(section.getSectionID());

					fileArchiveFilePopulator.populate(file, requestWrapper);

					this.checkFileExtension(file);

					log.info("User " + user + " adding file " + file + " to section " + section + " to school " + group.getSchool());

					this.filearchiveModuleDAO.addSchoolFile(user, file);

					res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID());

				} catch (ValidationException e) {
					validationException = e;
				} catch (FileSizeLimitExceededException e){

					log.info("User " + user + " tried to upload a file that exceeds the limit in configuration");

					validationException = new ValidationException(new ValidationError("FileTooBig"));

				} catch (FileUploadException e) {

					log.info("Error uploading file in section " + section + " caused by read time out, uploaded by user " + user);

				}

			}
		} else {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor, uriParser);
		}

		Document doc = this.createDocument(req, uriParser, group);
		Element addGroupLinkElement = doc.createElement("addSchoolFile");
		addGroupLinkElement.appendChild(XMLUtils.createElement("groupID", group.getGroupID().toString(), doc));
		doc.getFirstChild().appendChild(addGroupLinkElement);

		addGroupLinkElement.appendChild(getAllowedFileTypes(doc));

		if (validationException != null) {
			addGroupLinkElement.appendChild(validationException.toXML(doc));
			if(requestWrapper != null){
				addGroupLinkElement.appendChild(RequestUtils.getRequestParameters(requestWrapper, doc));
			}
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(addFileBreadcrumb, "", "addSchollFile", group));

	}

	private Element getAllowedFileTypes(Document doc) {

		Element allowedFileTypesElement = doc.createElement("allowedFileTypes");

		for(String fileExtension : this.allowedFileTypes){
			allowedFileTypesElement.appendChild(XMLUtils.createCDATAElement("extension", fileExtension, doc));
		}

		return allowedFileTypesElement;
	}

	@GroupMethod
	public ForegroundModuleResponse downloadSchoolFile(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException{

		// Download file
		File file = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && (file = this.filearchiveModuleDAO.getSchoolFile(Integer.parseInt(uriParser.get(3)),group.getSchool().getSchoolID(),true)) != null) {

			log.info("User " + user + " downloading file " + file + " from school " + group.getSchool());

			this.getFile(file, user, res);

			return null;

		} else {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
		}

	}

	@GroupMethod
	public ForegroundModuleResponse updateSchoolFile(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.checkAdminAccess(user, group);

		Integer schoolID = group.getSchool().getSchoolID();
		File file = null;
		ValidationException validationException = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && (file = this.filearchiveModuleDAO.getSchoolFile(Integer.valueOf(uriParser.get(3)),group.getSchool().getSchoolID(),false)) != null) {

			if (req.getMethod().equalsIgnoreCase("POST")) {
				try {

					file = fileArchiveFilePopulator.populate(file, req);

					log.info("User " + user + " updating file " + file + " in school " + group.getSchool());

					this.filearchiveModuleDAO.updateSchoolFile(file);

					res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID());

				} catch (ValidationException e) {
					validationException = e;
				}

			}
		} else {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor, uriParser);
		}

		ArrayList<Section> sections = this.filearchiveModuleDAO.getSchoolSections(schoolID);

		Document doc = this.createDocument(req, uriParser, group);
		Element updateSchoolFileElement = doc.createElement("updateSchoolFile");
		updateSchoolFileElement.appendChild(XMLUtils.createElement("groupID", group.getGroupID().toString(), doc));

		if (sections != null) {
			for (Section section : sections) {
				updateSchoolFileElement.appendChild(section.toXML(doc));
			}
		}

		updateSchoolFileElement.appendChild(file.toXML(doc));

		doc.getFirstChild().appendChild(updateSchoolFileElement);

		if (validationException != null) {
			updateSchoolFileElement.appendChild(validationException.toXML(doc));
			updateSchoolFileElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(updateFileBreadcrumb, "", "updateSchoolFile", group));

	}

	@GroupMethod
	public ForegroundModuleResponse deleteSchoolFile(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		this.checkAdminAccess(user, group);

		File file = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((file = this.filearchiveModuleDAO.getSchoolFile(Integer.valueOf(uriParser.get(3)),group.getSchool().getSchoolID(),false)) != null)) {

			log.info("User " + user + " deleting file " + file + " from school " + group.getSchool());

			this.filearchiveModuleDAO.deleteSchoolFile(file.getFileID());

			res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID());

			return null;

		} else {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
		}
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityGroup group) {
		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(group.toXML(doc));
		doc.appendChild(document);

		return doc;
	}

	private void getFile(File file, CommunityUser user, HttpServletResponse res) throws SQLException {

		// Skicka fil
		res.setContentType(file.getContentType());
		res.setContentLength(file.getFileSize().intValue());

		res.setHeader("Content-Disposition", "attachment; filename=\"" + StringUtils.toValidHttpFilename(file.getFileName()) + "\"");

		InputStream in = null;
		OutputStream outstream = null;

		try {
			// Open the file and output streams
			in = file.getBlob().getBinaryStream();
			outstream = res.getOutputStream();

			// Copy the contents of the file to the output stream
			byte[] buf = new byte[1024];
			int count = 0;

			while ((count = in.read(buf)) >= 0) {
				outstream.write(buf, 0, count);
			}
		} catch (IOException e) {
			this.log.info("Error sending attachment " + file + " to user " + user);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}

			if (outstream != null) {
				try {
					outstream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public List<? extends Event> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {

		List<SimpleEvent> fileEvents = this.filearchiveModuleDAO.getEvents(group, startStamp);

		if(fileEvents != null){

			for(SimpleEvent event : fileEvents){
				event.setTitle(this.newFileText + StringUtils.substring(event.getTitle(), 50, "..."));
				event.setDescription(event.getTitle());
				event.setFullAlias(this.getFullAlias(group));
			}
		}

		return fileEvents;
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
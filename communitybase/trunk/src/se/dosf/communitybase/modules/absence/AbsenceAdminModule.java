package se.dosf.communitybase.modules.absence;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.enums.ModuleType;
import se.dosf.communitybase.modules.AnnotatedGlobalModule;
import se.dosf.communitybase.modules.absence.beans.Absence;
import se.dosf.communitybase.modules.absence.cruds.AbsenceAdminCRUD;
import se.dosf.communitybase.modules.absence.cruds.AbsenceCRUDCallback;
import se.dosf.communitybase.utils.AccessUtils;
import se.dosf.communitybase.utils.CommunityBaseAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.foregroundmodules.ModuleSetting;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class AbsenceAdminModule extends AnnotatedGlobalModule implements AbsenceCRUDCallback {

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Last report hour", description = "The last hour of reporting of absence for the current day", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer lastReportHour = 8;

	private AnnotatedDAOWrapper<Absence, Integer> absenceCRUDDAO;

	private AbsenceAdminCRUD absenceAdminCRUD;

	private CommunitySchoolDAO schoolDAO;

	private CommunityGroupDAO groupDAO;

	private CommunityUserDAO userDAO;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		CommunityBaseAnnotatedDAOFactory daoFactory = new CommunityBaseAnnotatedDAOFactory(dataSource, this.sectionInterface.getSystemInterface().getUserHandler());

		this.groupDAO = new CommunityGroupDAO(dataSource);
		this.userDAO = new CommunityUserDAO(dataSource);
		this.groupDAO.setUserDao(this.userDAO);
		this.userDAO.setGroupDao(this.groupDAO);
		this.schoolDAO = new CommunitySchoolDAO(dataSource);
		this.schoolDAO.setGroupDAO(this.groupDAO);

		AnnotatedDAO<Absence> absenceDAO = daoFactory.getDAO(Absence.class);
		this.absenceCRUDDAO = absenceDAO.getWrapper("absenceID", Integer.class);
		this.absenceAdminCRUD = new AbsenceAdminCRUD(absenceCRUDDAO, new AnnotatedRequestPopulator<Absence>(Absence.class), "Absence", "absence", "/", this);

	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		return this.absenceAdminCRUD.list(req, res, user, uriParser, null, null);
	}

	@WebPublic(alias="group")
	public ForegroundModuleResponse listGroupAbsences(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		CommunityGroup group = null;

		if (uriParser.size() > 2 && NumberUtils.isInt(uriParser.get(2)) && (group = this.groupDAO.getGroup(NumberUtils.toInt(uriParser.get(2)), false)) != null && hasAccess(group, user)) {
			return this.absenceAdminCRUD.list(req, res, user, uriParser, null, group);
		}

		throw new URINotFoundException(sectionInterface, moduleDescriptor, uriParser);
	}

	@WebPublic(alias="showabsence")
	public ForegroundModuleResponse showAbsence(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		return this.absenceAdminCRUD.show(req, res, user, uriParser);
	}

	@WebPublic(alias="addabsence")
	public ForegroundModuleResponse addAbsence(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		return this.absenceAdminCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(alias="updateabsence")
	public ForegroundModuleResponse updateAbsence(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		return this.absenceAdminCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(alias="deleteabsence")
	public ForegroundModuleResponse deleteAbsence(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		return this.absenceAdminCRUD.delete(req, res, user, uriParser);
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityUser user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		if(group != null) {
			document.appendChild(group.toXML(doc));

			if (AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)) {
				document.appendChild(XMLUtils.createElement("isAdmin", "true", doc));
			}

		}

		document.appendChild(XMLUtils.createElement("isSysAdmin", String.valueOf(user.isAdmin()), doc));

		doc.appendChild(document);

		return doc;
	}

	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	public Integer getLastReportHour() {

		return this.lastReportHour;
	}

	public Breadcrumb getShowBreadCrumb(CommunityGroup group, Absence absence) {

		return null;
	}

	public Breadcrumb getAddBreadCrumb(CommunityGroup group) {

		return null;
	}

	public Breadcrumb getUpdateBreadCrumb(CommunityGroup group, Absence absence) {

		return null;
	}

	public SectionDescriptor getSectionDescriptor() {

		return this.sectionInterface.getSectionDescriptor();
	}

	public boolean hasAccess(Absence absence, CommunityUser user) {

		return hasAccess(absence.getGroup(), user);
	}

	public boolean hasAccess(CommunityGroup group, CommunityUser user) {

		return AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER);
	}

	public void setSchoolDAO(CommunitySchoolDAO schoolDAO) {
		this.schoolDAO = schoolDAO;
	}

	public CommunitySchoolDAO getSchoolDAO() {
		return schoolDAO;
	}

	public void setGroupDAO(CommunityGroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	public CommunityGroupDAO getGroupDAO() {
		return groupDAO;
	}

	@Override
	public ModuleType getModuleType() {

		return ModuleType.Administration;
	}

	public Breadcrumb getModuleBreadcrumb(CommunityGroup group) {

		return null;
	}

	public List<? extends Event> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {

		return null;
	}

}

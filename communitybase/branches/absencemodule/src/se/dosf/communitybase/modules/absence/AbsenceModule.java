package se.dosf.communitybase.modules.absence;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.annotations.GroupMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.absence.beans.Absence;
import se.dosf.communitybase.modules.absence.cruds.AbsenceCRUD;
import se.dosf.communitybase.modules.absence.cruds.AbsenceCRUDCallback;
import se.dosf.communitybase.utils.AccessUtils;
import se.dosf.communitybase.utils.CommunityBaseAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.foregroundmodules.ModuleSetting;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class AbsenceModule extends AnnotatedCommunityModule implements CommunityModule, AbsenceCRUDCallback {

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Last report hour", description = "The last hour of reporting of absence for the current day", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer lastReportHour = 8;
	
	@XSLVariable
	protected String addAbsenceBreadCrumb = "Add absence";
	
	@XSLVariable
	protected String updateAbsenceBreadCrumb = "Update absence";

	private AnnotatedDAOWrapper<Absence, Integer> absenceCRUDDAO;

	private AbsenceCRUD absenceCRUD;
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {
		
		super.createDAOs(dataSource);
		
		CommunityBaseAnnotatedDAOFactory daoFactory = new CommunityBaseAnnotatedDAOFactory(dataSource, this.sectionInterface.getSystemInterface().getUserHandler());
		
		AnnotatedDAO<Absence> absenceDAO = daoFactory.getDAO(Absence.class);
		this.absenceCRUDDAO = absenceDAO.getWrapper("absenceID", Integer.class);
		this.absenceCRUD = new AbsenceCRUD(absenceCRUDDAO, new AnnotatedRequestPopulator<Absence>(Absence.class), "Absence", "absence", "/", this);
		
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {
		
		return this.absenceCRUD.list(req, res, user, uriParser, null);
	}

	@GroupMethod(alias="showabsence")
	public ForegroundModuleResponse showAbsence(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.absenceCRUD.show(req, res, user, uriParser);
	}
	
	@GroupMethod(alias="addabsence")
	public ForegroundModuleResponse addAbsence(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.absenceCRUD.add(req, res, user, uriParser);
	}

	@GroupMethod(alias="updateabsence")
	public ForegroundModuleResponse updateAbsence(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.absenceCRUD.update(req, res, user, uriParser);
	}

	@GroupMethod(alias="deleteabsence")
	public ForegroundModuleResponse deleteAbsence(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.absenceCRUD.delete(req, res, user, uriParser);
	}
	
	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityUser user) {
		
		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));

		XMLUtils.appendNewElement(doc, document, "lastReportHour", this.lastReportHour);
		
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
	
	public boolean hasAccess(Absence absence, CommunityUser user) {
		
		if(this.checkAdminAccess(absence, user) || absence.getPoster().equals(user)) {
			return true;
		}
		
		return false;		
	}
	
	public boolean checkAdminAccess(Absence absence, CommunityUser user) {

		return AccessUtils.checkAccess(user, absence.getGroup(), GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER);
	}
	
	public String getTitle() {
		
		return this.moduleDescriptor.getName();
	}

	public List<? extends Event> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {
		
		return null;
	}
	
	public Breadcrumb getShowBreadCrumb(CommunityGroup group, Absence absence) {
		return this.getMethodBreadcrumb(absence.toString(), "showabsence", group);
	}
	
	public Breadcrumb getAddBreadCrumb(CommunityGroup group) {
		return this.getMethodBreadcrumb(this.addAbsenceBreadCrumb, "addabsence", group);
	}

	public Breadcrumb getUpdateBreadCrumb(CommunityGroup group, Absence absence) {
		return this.getMethodBreadcrumb(this.updateAbsenceBreadCrumb, "updateabsence" + "/" + absence.getAbsenceID(), group);
	}

	private Breadcrumb getMethodBreadcrumb(String name, String methodUri, CommunityGroup group) {
		return new Breadcrumb(name, this.getFullAlias() + "/" + group.getGroupID() + "/" + methodUri);
	}
	
	public Integer getLastReportHour() {
		return lastReportHour;
	}
	
}

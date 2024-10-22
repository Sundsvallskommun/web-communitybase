package se.dosf.communitybase.modules.links;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.annotations.GroupMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.enums.RelationType;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.dosf.communitybase.utils.AccessUtils;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.enums.URLType;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.StringURLPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class LinksModule extends AnnotatedCommunityModule {

	private static AnnotatedRequestPopulator<Link> LINK_POPULATOR = new AnnotatedRequestPopulator<Link>(Link.class,new StringURLPopulator());
	protected Logger log = Logger.getLogger(this.getClass());
	private LinkDAO linkModuleDAO;
	private UserHandler userHandler;

	@XSLVariable
	protected String newLinkText = "New link: ";

	@XSLVariable
	protected String addGroupLinkBreadcrumb = "Add grouplink";

	@XSLVariable
	protected String addSchoolLinkBreadcrumb = "Add schoollink";

	@XSLVariable
	protected String addGlobalLinkBreadcrumb = "Add globallink";

	@XSLVariable
	protected String updateLinkBreadcrumb = "Update link";

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptorBean, sectionInterface, dataSource);
		this.userHandler = sectionInterface.getSystemInterface().getUserHandler();
		this.linkModuleDAO = new LinkDAO(dataSource, userHandler);

	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {
		super.update(moduleDescriptor, dataSource);
		this.userHandler = sectionInterface.getSystemInterface().getUserHandler();
		this.linkModuleDAO = new LinkDAO(dataSource, userHandler);
	}


	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException {

		log.info("User " + user + " listing links in group " + group);

		Document doc = this.createDocument(req, uriParser, group);
		Element linkModuleElement = doc.createElement("linkModule");

		if(user.getLastLogin() != null){
			linkModuleElement.appendChild(XMLUtils.createElement("userLastLoginInMillis", String.valueOf(user.getLastLogin().getTime()), doc));
		}

		if(user.isAdmin()) {
			XMLUtils.appendNewElement(doc, linkModuleElement, "isGlobalAdmin", "true");
		} else if(AccessUtils.checkAccess(user, group.getSchool())) {
			XMLUtils.appendNewElement(doc, linkModuleElement, "isSchoolAdmin", "true");
		} else if(AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)) {
			XMLUtils.appendNewElement(doc, linkModuleElement, "isGroupAdmin", "true");
		}

		doc.getFirstChild().appendChild(linkModuleElement);

		ArrayList<Link> grouplinks = this.linkModuleDAO.getAllGroupLinks(group.getGroupID());

		XMLUtils.append(doc, linkModuleElement, "groupLinks", grouplinks);

		ArrayList<Link> schoollinks = this.linkModuleDAO.getAllSchoolLinks(group.getSchool().getSchoolID());

		XMLUtils.appendNewElement(doc, linkModuleElement, "schoolName", group.getSchool().getName());
		XMLUtils.append(doc, linkModuleElement, "schoolLinks", schoollinks);

		ArrayList<Link> globallinks = this.linkModuleDAO.getAllGlobalLinks();

		XMLUtils.append(doc, linkModuleElement, "globalLinks", globallinks);

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group));
	}

	@GroupMethod
	public ForegroundModuleResponse addGroupLink(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		this.checkAdminAccess(user, group);

		ValidationException validationException = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {
			try {
				Link link = LINK_POPULATOR.populate(req);

				log.info("User " + user + " adding link " + link + " to group " + group);

				this.linkModuleDAO.addGroupLink(group, user, link);

				res.sendRedirect(uriParser.getCurrentURI(true) + this.getFullAlias(group));

			} catch (ValidationException e) {
				validationException = e;
			}

		}

		Document doc = this.createDocument(req, uriParser, group);
		Element addGroupLinkElement = doc.createElement("addGroupLink");
		addGroupLinkElement.appendChild(XMLUtils.createElement("groupID", group.getGroupID().toString(), doc));
		doc.getFirstChild().appendChild(addGroupLinkElement);

		if (validationException != null) {
			addGroupLinkElement.appendChild(validationException.toXML(doc));
			addGroupLinkElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group),
				this.getMethodBreadcrumb(addGroupLinkBreadcrumb, "", "addGroupLink", group));

	}

	@GroupMethod
	public ForegroundModuleResponse addSchoolLink(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		this.checkAdminAccess(user, group.getSchool());

		ValidationException validationException = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {
			try {
				Link link = LINK_POPULATOR.populate(req);

				log.info("User " + user + " adding link " + link + " to school " + group.getSchool());

				this.linkModuleDAO.addSchoolLink(group, user, link);

				res.sendRedirect(uriParser.getCurrentURI(true) +  this.getFullAlias(group));

			} catch (ValidationException e) {
				validationException = e;
			}

		}

		Document doc = this.createDocument(req, uriParser, group);
		Element addGroupLinkElement = doc.createElement("addSchoolLink");
		doc.getFirstChild().appendChild(addGroupLinkElement);

		if (validationException != null) {
			addGroupLinkElement.appendChild(validationException.toXML(doc));
			addGroupLinkElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group),
				this.getMethodBreadcrumb(addSchoolLinkBreadcrumb, "", "addSchoolLink", group));

	}


	@GroupMethod
	public ForegroundModuleResponse addGlobalLink(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		this.checkAdminAccess(user);

		ValidationException validationException = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {
			try {
				Link link = LINK_POPULATOR.populate(req);

				log.info("User " + user + " adding global link " + link);

				this.linkModuleDAO.addGlobalLink(group, user, link);

				res.sendRedirect(uriParser.getCurrentURI(true) + this.getFullAlias(group));

			} catch (ValidationException e) {
				validationException = e;
			}

		}

		Document doc = this.createDocument(req, uriParser, group);
		Element addGroupLinkElement = doc.createElement("addGlobalLink");
		doc.getFirstChild().appendChild(addGroupLinkElement);

		if (validationException != null) {
			addGroupLinkElement.appendChild(validationException.toXML(doc));
			addGroupLinkElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(addGlobalLinkBreadcrumb, "", "addGlobalLink", group));

	}

	@GroupMethod
	public ForegroundModuleResponse updateGroupLink(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		return this.updateLink(req, res, user, uriParser, group, RelationType.GROUP);
	}

	@GroupMethod
	public ForegroundModuleResponse updateSchoolLink(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		return this.updateLink(req, res, user, uriParser, group, RelationType.SCHOOL);
	}

	@GroupMethod
	public ForegroundModuleResponse updateGlobalLink(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		return this.updateLink(req, res, user, uriParser, group, RelationType.GLOBAL);
	}

	private ForegroundModuleResponse updateLink(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, RelationType type) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		this.checkAdminAccess(user, group, type);

		Link link = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((link = this.linkModuleDAO.getLink(Integer.valueOf(uriParser.get(3)), type)) != null)) {

			ValidationException validationException = null;

			if (req.getMethod().equalsIgnoreCase("POST")) {
				try {
					link = LINK_POPULATOR.populate(link, req);

					log.info("User " + user + " updating link " + link);

					this.linkModuleDAO.updateLink(link);

					res.sendRedirect(uriParser.getCurrentURI(true) + this.getFullAlias(group));

					return null;

				} catch (ValidationException e) {
					validationException = e;
				}
			}

			Document doc = this.createDocument(req, uriParser, group);

			Element updateLinkElement = doc.createElement("updateLink");
			doc.getFirstChild().appendChild(updateLinkElement);
			updateLinkElement.appendChild(link.toXML(doc));

			if (validationException != null) {
				updateLinkElement.appendChild(validationException.toXML(doc));
				updateLinkElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(updateLinkBreadcrumb, "", "update" + type + "Link", group));

		} else {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
		}
	}

	@GroupMethod
	public ForegroundModuleResponse deleteGroupLink(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		return this.deleteLink(req, res, user, uriParser, group, RelationType.GROUP);
	}

	@GroupMethod
	public ForegroundModuleResponse deleteSchoolLink(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		return this.deleteLink(req, res, user, uriParser, group, RelationType.SCHOOL);
	}

	@GroupMethod
	public ForegroundModuleResponse deleteGlobalLink(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		return this.deleteLink(req, res, user, uriParser, group, RelationType.GLOBAL);
	}

	private ForegroundModuleResponse deleteLink(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, RelationType type) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		this.checkAdminAccess(user, group, type);

		Link link = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((link = this.linkModuleDAO.getLink(Integer.valueOf(uriParser.get(3)), type)) != null)) {

			log.info("User " + user + " deleting link " + link);

			this.linkModuleDAO.deleteLink(link.getLinkID());

			this.redirectToDefaultMethod(req, res, group);

			return null;

		} else {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
		}
	}

	private Breadcrumb getMethodBreadcrumb(String name, String description, String method, CommunityGroup group) {
		return new Breadcrumb(name, description, this.getFullAlias() + "/" + group.getGroupID() + "/" + method, URLType.RELATIVE_FROM_CONTEXTPATH);
	}

	public List<? extends Event> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {

		List<IdentifiedEvent> linkEvents = this.linkModuleDAO.getEvents(group, startStamp);

		if(linkEvents != null){

			for(IdentifiedEvent event : linkEvents){
				event.setTitle(this.newLinkText + StringUtils.substring(event.getTitle(), 50, "..."));
				event.setDescription(event.getTitle());
				event.setFullAlias(this.getFullAlias(group));
			}
		}

		return linkEvents;
	}

	private void checkAdminAccess(CommunityUser user, CommunityGroup group, RelationType type) throws AccessDeniedException {

		if(type.equals(RelationType.GLOBAL)) {

			this.checkAdminAccess(user);

		} else if(type.equals(RelationType.SCHOOL)) {

			this.checkAdminAccess(user, group.getSchool());

		} else {

			this.checkAdminAccess(user, group);

		}

	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityGroup group) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(group.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		doc.appendChild(document);

		return doc;
	}

}

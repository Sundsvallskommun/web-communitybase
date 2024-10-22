package se.dosf.communitybase.modules.linkarchive.cruds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.cruds.IntegerBasedCommunityBaseCRUD;
import se.dosf.communitybase.modules.linkarchive.LinkArchiveModule;
import se.dosf.communitybase.modules.linkarchive.beans.Link;
import se.dosf.communitybase.utils.AccessibleFactory;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class LinkCRUD extends IntegerBasedCommunityBaseCRUD<Link, LinkArchiveModule> {

	protected final QueryParameterFactory<Link, Integer> linkIDParamFactory;

	protected final AnnotatedDAOWrapper<Link, Integer> linkCRUDDAO;

	private LinkArchiveModule linkArchiveModule;

	private AccessibleFactory accessibleFactory;

	public LinkCRUD(AnnotatedDAOWrapper<Link, Integer> linkCRUDDAO, BeanRequestPopulator<Link> populator, String typeElementName, String typeLogName, String listMethodAlias, LinkArchiveModule linkArchiveModule) {
		super(linkCRUDDAO, populator, typeElementName, typeLogName, listMethodAlias, linkArchiveModule);

		this.linkCRUDDAO = linkCRUDDAO;
		this.linkArchiveModule = linkArchiveModule;

		this.accessibleFactory = new AccessibleFactory(linkArchiveModule);
		this.linkIDParamFactory = this.linkCRUDDAO.getAnnotatedDAO().getParamFactory("linkID", Integer.class);
	}


	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws Exception {
		this.linkArchiveModule.appendGroupAndAccess(doc, (CommunityGroup) req.getAttribute("group"), user);
		Element accessibleXML = this.accessibleFactory.getAccessibleXML(user, doc);
		if(accessibleXML == null) {
			throw new AccessDeniedException("Permission denied for user " + user);
		}
		addTypeElement.appendChild(user.toXML(doc));
		addTypeElement.appendChild(accessibleXML);
	}


	@Override
	protected void appendUpdateFormData(Link bean, Document doc, Element updateTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws Exception {
		this.appendAddFormData(doc, updateTypeElement, user, req, uriParser);
	}


	protected Link populateRequest(Link link, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws SerialException, SQLException, ValidationException, AccessDeniedException {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		this.accessibleFactory.validateAndSetAccess(req, link, validationErrors, user);
		if(!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}
		return link;
	}


	@Override
	protected Link populateFromAddRequest(HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, Exception {
		Link link = super.populateFromAddRequest(req, user, uriParser);
		this.populateRequest(link, req, user, uriParser);
		link.setPoster(user);
		return link;
	}


	@Override
	protected Link populateFromUpdateRequest(Link bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, Exception {
		Link link = super.populateFromUpdateRequest(bean, req, user, uriParser);
		this.populateRequest(link, req, user, uriParser);
		return link;
	}


	@Override
	protected void appendListFormData(Document doc, Element listTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		listTypeElement.appendChild(XMLUtils.createElement("userLastLoginInMillis", String.valueOf(user.getLastLogin().getTime()), doc));
		List<String> foo = new ArrayList<String>(LinkArchiveModule.SORT_COLUMNS.keySet());
		XMLUtils.append(doc, listTypeElement, "SortingCriterias", "Criteria", foo);

		// Append sorting preferences as request parameters
		Element requestParametersElement = RequestUtils.getRequestParameters(req, doc);
		Element param1 = doc.createElement("parameter");
		XMLUtils.appendNewElement(doc, param1, "name", "orderby");
		XMLUtils.appendNewElement(doc, param1, "value", req.getAttribute("linkArchive.sortingPreferences.criteria"));
		Element param2 = doc.createElement("parameter");
		XMLUtils.appendNewElement(doc, param2, "name", "reverse");
		XMLUtils.appendNewElement(doc, param2, "value", req.getAttribute("linkArchive.sortingPreferences.reverse"));
		requestParametersElement.appendChild(param1);
		requestParametersElement.appendChild(param2);
		listTypeElement.appendChild(requestParametersElement);

		this.linkArchiveModule.appendGroupAndAccess(doc, group, user);

		super.appendListFormData(doc, listTypeElement, user, req, uriParser, validationErrors);
	}

	@Override
	protected void appendAllBeans(Document doc, Element listTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {
		
		List<Link> links = getAllBeans(user, req, uriParser);
		
		if(!CollectionUtils.isEmpty(links)) {
			
			Element linksElement = XMLUtils.appendNewElement(doc, listTypeElement, this.typeElementPluralName);
			
			for(Link link : links) {
				
				Element linkElement = link.toXML(doc);
				
				if(callback.hasUpdateAccess(link, user)) {
					
					XMLUtils.appendNewElement(doc, linkElement, "hasUpdateAccess", "true");
					
				}
				
				linksElement.appendChild(linkElement);
				
			}
			
		}
		
	}
	
	@Override
	protected List<Link> getAllBeans(CommunityUser user, HttpServletRequest req, URIParser uriParser) throws SQLException {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return callback.getLinkDAO().getLinks(group, (String)req.getAttribute("linkArchive.sortingPreferences.criteria"), (Order)req.getAttribute("linkArchive.sortingPreferences.order"));
	}


	@Override
	protected ForegroundModuleResponse beanDeleted(Link bean, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {
		return super.beanDeleted(null, req, res, user, uriParser);
	}


	@Override
	protected void redirectToListMethod(HttpServletRequest req, HttpServletResponse res, Link bean) throws Exception {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		if(bean != null) {
			res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/" + group.getGroupID() + "#" + bean.getLinkID());
		} else {
			res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/" + group.getGroupID());
		}
	}


	@Override
	protected List<Breadcrumb> getAddBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser) {
		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		return CollectionUtils.getList(this.linkArchiveModule.getGroupBreadcrumb(group), this.linkArchiveModule.getModuleBreadcrumb(group), this.linkArchiveModule.getAddLinkBreadCrumb(group));
	}


	@Override
	protected List<Breadcrumb> getUpdateBreadcrumbs(Link bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws Exception {
		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		return CollectionUtils.getList(this.linkArchiveModule.getGroupBreadcrumb(group), this.linkArchiveModule.getModuleBreadcrumb(group), this.linkArchiveModule.getUpdateBreadCrumb(group, bean));
	}


	@Override
	protected List<Breadcrumb> getShowBreadcrumbs(Link bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) {
		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		return CollectionUtils.getList(this.linkArchiveModule.getGroupBreadcrumb(group), this.linkArchiveModule.getModuleBreadcrumb(group));
	}


	@Override
	protected List<Breadcrumb> getListBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser, List<ValidationError> validationErrors) {
		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		return CollectionUtils.getList(this.linkArchiveModule.getGroupBreadcrumb(group), this.linkArchiveModule.getModuleBreadcrumb(group));
	}


	@Override
	public Link getRequestedBean(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, String getMode) throws SQLException, AccessDeniedException {
		Link link = super.getRequestedBean(req, res, user, uriParser, getMode);
		this.checkShowAccess(link, user, req, uriParser);
		return link;
	}


	@Override
	protected void checkShowAccess(Link bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {
		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		if(bean != null && !this.linkArchiveModule.checkAccess(bean, group)) {
			throw new AccessDeniedException("Show access for link denied");
		}
	}


	@Override
	protected void checkUpdateAccess(Link bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {
		if(!this.linkArchiveModule.hasUpdateAccess(bean, user)) {
			throw new AccessDeniedException("Update access for link denied");
		}
	}


	@Override
	protected void checkDeleteAccess(Link bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {
		if(!this.linkArchiveModule.hasUpdateAccess(bean, user)) {
			throw new AccessDeniedException("Delete access for link denied");
		}
	}
}
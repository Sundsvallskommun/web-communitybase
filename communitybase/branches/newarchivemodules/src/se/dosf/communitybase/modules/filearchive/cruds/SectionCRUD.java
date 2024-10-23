package se.dosf.communitybase.modules.filearchive.cruds;

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
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.filearchive.FileArchiveModule;
import se.dosf.communitybase.modules.filearchive.beans.Section;
import se.dosf.communitybase.utils.AccessUtils;
import se.dosf.communitybase.utils.AccessibleFactory;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class SectionCRUD extends IntegerBasedCommunityBaseCRUD<Section, FileArchiveModule> {

	private AccessibleFactory accessibleFactory;

	public SectionCRUD(AnnotatedDAOWrapper<Section, Integer> sectionCRUDDAO, BeanRequestPopulator<Section> populator, String typeElementName, String typeLogName, String listMethodAlias, FileArchiveModule fileArchiveModule) {

		super(sectionCRUDDAO, populator, typeElementName, typeLogName, listMethodAlias, fileArchiveModule);

		this.accessibleFactory = new AccessibleFactory(fileArchiveModule);
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws Exception {

		this.callback.appendGroupAndAccess(doc, (CommunityGroup)req.getAttribute("group"), user);
		Element accessibleXML = this.accessibleFactory.getAccessibleXML(user, doc);
		if(accessibleXML == null){
			throw new AccessDeniedException("Permission denied for user " + user);
		}
		addTypeElement.appendChild(user.toXML(doc));
		addTypeElement.appendChild(accessibleXML);
	}

	@Override
	protected void appendUpdateFormData(Section bean, Document doc, Element updateTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws Exception {

		this.appendAddFormData(doc, updateTypeElement, user, req, uriParser);
	}

	protected Section populateRequest(Section section, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws SerialException, SQLException, ValidationException, AccessDeniedException {

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		this.accessibleFactory.validateAndSetAccess(req, section, validationErrors, user);
		if(!validationErrors.isEmpty()){
			throw new ValidationException(validationErrors);
		}
		return section;
	}

	@Override
	protected Section populateFromAddRequest(HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, Exception {

		Section section = super.populateFromAddRequest(req, user, uriParser);
		this.populateRequest(section, req, user, uriParser);
		return section;
	}

	@Override
	protected Section populateFromUpdateRequest(Section bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, Exception {

		Section section = super.populateFromUpdateRequest(bean, req, user, uriParser);
		this.populateRequest(section, req, user, uriParser);
		return section;
	}

	@Override
	protected void appendListFormData(Document doc, Element listTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");

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

		this.callback.appendGroupAndAccess(doc, group, user);
		
		super.appendListFormData(doc, listTypeElement, user, req, uriParser, validationErrors);
	}

	@Override
	protected List<Section> getAllBeans(CommunityUser user, HttpServletRequest req, URIParser uriParser) throws SQLException {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");

		return callback.getSectionDAO().getSections(group, (String)req.getAttribute("fileArchive.sortingPreferences.criteria"), (Order)req.getAttribute("fileArchive.sortingPreferences.order"));
	}

	@Override
	protected void appendAllBeans(Document doc, Element listTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {
		
		List<Section> sections = getAllBeans(user, req, uriParser);
		
		if(!CollectionUtils.isEmpty(sections)) {
			
			Element sectionsElement = XMLUtils.appendNewElement(doc, listTypeElement, this.typeElementPluralName);
			
			for(Section section : sections) {
				
				Element sectionElement = section.toXML(doc);
				
				if(callback.hasUpdateAccess(section, user)) {
					
					XMLUtils.appendNewElement(doc, sectionElement, "hasUpdateAccess", "true");
					
				}
				
				sectionsElement.appendChild(sectionElement);
				
			}
			
		}
		
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(Section bean, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		return super.beanDeleted(null, req, res, user, uriParser);
	}

	@Override
	protected void redirectToListMethod(HttpServletRequest req, HttpServletResponse res, Section bean) throws Exception {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");
		if(bean != null){
			res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/" + group.getGroupID() + "#" + bean.getSectionID());
		}else{
			res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/" + group.getGroupID());
		}
	}

	@Override
	protected List<Breadcrumb> getAddBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser) {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");
		return CollectionUtils.getList(this.callback.getGroupBreadcrumb(group), this.callback.getModuleBreadcrumb(group), this.callback.getAddSectionBreadCrumb(group));
	}

	@Override
	protected List<Breadcrumb> getUpdateBreadcrumbs(Section bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws Exception {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");
		return CollectionUtils.getList(this.callback.getGroupBreadcrumb(group), this.callback.getModuleBreadcrumb(group), this.callback.getUpdateBreadCrumb(group, bean));
	}

	@Override
	protected List<Breadcrumb> getShowBreadcrumbs(Section bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");
		return CollectionUtils.getList(this.callback.getGroupBreadcrumb(group), this.callback.getModuleBreadcrumb(group));
	}

	@Override
	protected List<Breadcrumb> getListBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser, List<ValidationError> validationErrors) {

		CommunityGroup group = (CommunityGroup)req.getAttribute("group");
		return CollectionUtils.getList(this.callback.getGroupBreadcrumb(group), this.callback.getModuleBreadcrumb(group));
	}

	@Override
	public Section getRequestedBean(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, String getMode) throws SQLException, AccessDeniedException {

		Section section = super.getRequestedBean(req, res, user, uriParser, getMode);
		this.checkShowAccess(section, user, req, uriParser);
		return section;
	}

	@Override
	protected void checkShowAccess(Section bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {

		if(bean != null && !AccessUtils.checkReadAccess(bean, user, GroupAccessLevel.MEMBER)){
			throw new AccessDeniedException("Show access for section denied");
		}
	}

	@Override
	protected void checkUpdateAccess(Section bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {

		if(!this.callback.hasUpdateAccess(bean, user)){
			throw new AccessDeniedException("Update access for section denied");
		}
	}

	@Override
	protected void checkDeleteAccess(Section bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {

		if(!this.callback.hasUpdateAccess(bean, user)){
			throw new AccessDeniedException("Delete access for secion denied");
		}
	}
}
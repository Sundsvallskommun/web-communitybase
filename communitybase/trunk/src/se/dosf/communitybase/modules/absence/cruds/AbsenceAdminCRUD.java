package se.dosf.communitybase.modules.absence.cruds;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.modules.absence.AbsenceAdminModule;
import se.dosf.communitybase.modules.absence.beans.Absence;
import se.dosf.communitybase.modules.absence.beans.AbsenceGroupComparator;
import se.dosf.communitybase.modules.absence.beans.AbsenceSchoolComparator;
import se.dosf.communitybase.modules.absence.beans.AbsenceSearch;
import se.dosf.communitybase.utils.PublishingFactory;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.enums.Order;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.MillisecondTimeUnits;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xml.XMLable;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.validation.ValidationUtils;

public class AbsenceAdminCRUD extends AbsenceCRUD {

	private static final Field GROUPID_FIELD = ReflectionUtils.getField(CommunityGroup.class, "groupID");

	protected final QueryParameterFactory<Absence, Timestamp> startTimeParamFactory;

	protected PublishingFactory publishingFactory;

	protected AbsenceAdminModule absenceAdminModule;

	public AbsenceAdminCRUD(AnnotatedDAOWrapper<Absence, Integer> absenceCRUDDAO, BeanRequestPopulator<Absence> populator, String typeElementName, String typeLogName, String listMethodAlias, AbsenceAdminModule absenceModule) {
		super(absenceCRUDDAO, populator, typeElementName, typeLogName, listMethodAlias, absenceModule);

		this.absenceModule = absenceModule;
		this.absenceAdminModule = absenceModule;
		this.publishingFactory = new PublishingFactory(absenceAdminModule.getGroupDAO(), absenceAdminModule.getSchoolDAO());
		this.startTimeParamFactory = absenceCRUDDAO.getAnnotatedDAO().getParamFactory("startTime", Timestamp.class);
	}

	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, ValidationError validationError, CommunityGroup group) throws Exception {

		AbsenceSearch absenceSearch = (AbsenceSearch) req.getSession().getAttribute("absenceSearch");

		if(req.getMethod().equalsIgnoreCase("POST")) {

			List<ValidationError> errors = new ArrayList<ValidationError>();

			absenceSearch = new AbsenceSearch();

			absenceSearch.setStartDate(ValidationUtils.validateParameter("startDate", req, true,  DATE_POPULATOR, errors));
			absenceSearch.setEndDate(ValidationUtils.validateParameter("endDate", req, true,  DATE_POPULATOR, errors));

			this.publishingFactory.validateAndSetPublishing(req, absenceSearch, errors);

			if(!errors.isEmpty()) {
				req.setAttribute("validationException", new ValidationException(errors));
				return super.list(req, res, user, uriParser, validationError);
			}

			absenceSearch.setOrderBy(req.getParameter("orderBy"));
			absenceSearch.setOrder(req.getParameter("order").equals("DESC") ? Order.DESC : Order.ASC);

			req.setAttribute("absenceSearch", absenceSearch);

			req.getSession().setAttribute("absenceSearch", absenceSearch);

		} else if(group != null) {

			if(absenceSearch == null) {

				absenceSearch = AbsenceSearch.getDefaultAbsenceSearch();

			}

			absenceSearch.setGroups(Collections.singletonList(group));

			req.setAttribute("absenceSearch", absenceSearch);

			req.getSession().setAttribute("absenceSearch", absenceSearch);

		} else if(absenceSearch != null) {

			req.setAttribute("absenceSearch", absenceSearch);

		}

		return super.list(req, res, user, uriParser, validationError);
	}

	@Override
	protected void appendListFormData(Document doc, Element listTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser, ValidationError validationError) throws SQLException {

		listTypeElement.appendChild(user.toXML(doc));

		Element publisherXML = this.publishingFactory.getPublisherXML(user, doc);

		if(publisherXML != null) {
			listTypeElement.appendChild(publisherXML);
		}

		AbsenceSearch search = (AbsenceSearch) req.getAttribute("absenceSearch");

		if(search != null) {
			listTypeElement.appendChild(search.toXML(doc));
		} else {
			XMLUtils.appendNewElement(doc, listTypeElement, "currentDate", DateUtils.DATE_FORMATTER.format(System.currentTimeMillis()));
		}

		ValidationException exception = (ValidationException) req.getAttribute("validationException");

		if(exception != null) {
			listTypeElement.appendChild(exception.toXML(doc));
			listTypeElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

	}

	@Override
	protected Absence populateFromAddRequest(HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, SQLException {

		this.populateRequest(req, user, uriParser);

		return super.populateFromAddRequest(req, user, uriParser);

	}

	@Override
	protected Absence populateFromUpdateRequest(Absence bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, SQLException {

		this.populateRequest(req, user, uriParser);

		return super.populateFromUpdateRequest(bean, req, user, uriParser);
	}

	protected void populateRequest(HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, SQLException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		Integer groupID = ValidationUtils.validateParameter("groupID", req, true, IntegerPopulator.getPopulator(), errors);

		if(!errors.isEmpty()) {
			throw new ValidationException(errors);
		}

		req.setAttribute("group", this.absenceAdminModule.getGroupDAO().getGroup(groupID, false));

	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws Exception {

		Element publisherXML = this.publishingFactory.getPublisherXML(user, doc);

		if(publisherXML != null) {
			addTypeElement.appendChild(publisherXML);
		}

	}

	@Override
	protected void appendUpdateFormData(Absence bean, Document doc, Element updateTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws Exception {

		this.appendAddFormData(doc, updateTypeElement, user, req, uriParser);
	}

	@Override
	protected List<Breadcrumb> getListBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser, ValidationError validationErrors) {

		return CollectionUtils.getList(this.absenceAdminModule.getDefaultBreadcrumb());
	}

	@Override
	protected List<Breadcrumb> getShowBreadcrumbs(Absence bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) {

		return CollectionUtils.getList(this.absenceModule.getDefaultBreadcrumb(), this.absenceModule.getShowBreadCrumb(null, bean));
	}

	@Override
	protected List<Breadcrumb> getAddBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser) {

		return CollectionUtils.getList(this.absenceModule.getDefaultBreadcrumb(), this.absenceModule.getAddBreadCrumb(null));
	}

	@Override
	protected List<Breadcrumb> getUpdateBreadcrumbs(Absence bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws Exception {

		return CollectionUtils.getList(this.absenceModule.getDefaultBreadcrumb(), this.absenceModule.getUpdateBreadCrumb(null, bean));
	}

	@Override
	public Absence getRequestedBean(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, String getMode) throws SQLException, AccessDeniedException {

		Absence bean;

		if (uriParser.size() > 2 && NumberUtils.isInt(uriParser.get(2)) && (bean = getBean(NumberUtils.toInt(uriParser.get(2)))) != null) {

			req.setAttribute("group", bean.getGroup());

			return bean;
		}

		return null;
	}

	@Override
	protected Collection<? extends XMLable> getAllBeans(CommunityUser user, HttpServletRequest req, URIParser uriParser) throws SQLException {

		AbsenceSearch search = (AbsenceSearch) req.getAttribute("absenceSearch");

		if(search != null) {
			return this.getAbsences(search);
		}

		return null;
	}

	@Override
	protected void redirectToListMethod(HttpServletRequest req, HttpServletResponse res, Absence bean) throws Exception {

		res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "#search");
	}

	protected List<Absence> getAbsences(AbsenceSearch absenceSearch) throws SQLException {

		LowLevelQuery<Absence> query = new LowLevelQuery<Absence>();

		StringBuilder sql = new StringBuilder("SELECT * FROM " + this.absenceCRUDDAO.getAnnotatedDAO().getTableName() + " WHERE ");

		if(!absenceSearch.isGlobal()) {

			List<School> schools = absenceSearch.getSchools();

			List<CommunityGroup> allGroups = new ArrayList<CommunityGroup>();

			if(!CollectionUtils.isEmpty(schools)) {

				for(School school : schools) {

					List<CommunityGroup> schoolGroups = absenceAdminModule.getGroupDAO().getGroups(school);

					if(!CollectionUtils.isEmpty(schoolGroups)) {
						allGroups.addAll(schoolGroups);
					}

				}

			}

			List<CommunityGroup> groups = absenceSearch.getGroups();

			if(!CollectionUtils.isEmpty(groups)) {

				for(CommunityGroup group : groups) {

					if(!allGroups.contains(groups)) {

						allGroups.add(group);

					}

				}

			}

			if(!allGroups.isEmpty()) {
				try {
					sql.append("groupID IN(" + StringUtils.toCommaSeparatedString(allGroups, GROUPID_FIELD) + ") AND");
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("An unexpected error occured when getting absences");
				} catch (IllegalAccessException e) {
					throw new RuntimeException("An unexpected error occured when getting absences");
				}
			}

		}

		sql.append("((? BETWEEN startTime AND endTime) OR (? BETWEEN startTime AND endTime) OR (startTime >= ? AND endTime < ?)) ");

		Timestamp startTime = new Timestamp(absenceSearch.getStartDate().getTime());

		query.addParameter(startTime);
		query.addParameter(new Timestamp(absenceSearch.getEndDate().getTime()));
		query.addParameter(startTime);
		query.addParameter(new Timestamp(absenceSearch.getEndDate().getTime() + MillisecondTimeUnits.DAY));

		boolean orderByDone = false;

		String orderBy = absenceSearch.getOrderBy();

		if(!StringUtils.isEmpty(orderBy) && !orderBy.equals("school") && !orderBy.equals("group") && absenceSearch.getOrder() != null) {
			sql.append("ORDER BY " + orderBy + " " + absenceSearch.getOrder());
			orderByDone = true;
		}

		query.setSql(sql.toString());

		List<Absence> absences = this.absenceCRUDDAO.getAnnotatedDAO().getAll(query);

		if(!orderByDone && !CollectionUtils.isEmpty(absences)) {

			if(orderBy.equals("group")) {
				Collections.sort(absences, AbsenceGroupComparator.getInstance(absenceSearch.getOrder()));
			} else if(orderBy.equals("school")) {
				Collections.sort(absences, AbsenceSchoolComparator.getInstance(absenceSearch.getOrder()));
			}

		}

		return absences;

	}

	@Override
	protected void validateLastReportTime(Timestamp startTime, List<ValidationError> errors) { }

}

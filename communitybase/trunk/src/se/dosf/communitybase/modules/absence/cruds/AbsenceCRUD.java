package se.dosf.communitybase.modules.absence.cruds;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.cruds.IntegerBasedCommunityBaseCRUD;
import se.dosf.communitybase.enums.ModuleType;
import se.dosf.communitybase.modules.absence.beans.Absence;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.DatePopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLable;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.validation.ValidationUtils;

public class AbsenceCRUD extends IntegerBasedCommunityBaseCRUD<Absence, AbsenceCRUDCallback> {

	protected static final DatePopulator DATE_POPULATOR = new DatePopulator();

	protected final QueryParameterFactory<Absence, CommunityUser> posterParamFactory;
	protected final QueryParameterFactory<Absence, CommunityGroup> groupParamFactory;

	protected AbsenceCRUDCallback absenceModule;

	protected AnnotatedDAOWrapper<Absence, Integer> absenceCRUDDAO;

	public AbsenceCRUD(AnnotatedDAOWrapper<Absence, Integer> absenceCRUDDAO, BeanRequestPopulator<Absence> populator, String typeElementName, String typeLogName, String listMethodAlias, AbsenceCRUDCallback absenceModule) {

		super(absenceCRUDDAO, populator, typeElementName, typeLogName, listMethodAlias, absenceModule);

		this.absenceModule = absenceModule;
		this.absenceCRUDDAO = absenceCRUDDAO;
		this.posterParamFactory = absenceCRUDDAO.getAnnotatedDAO().getParamFactory("poster", CommunityUser.class);
		this.groupParamFactory = absenceCRUDDAO.getAnnotatedDAO().getParamFactory("group", CommunityGroup.class);
	}

	@Override
	protected Absence populateFromAddRequest(HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, SQLException {

		Absence absence = super.populateFromAddRequest(req, user, uriParser);

		absence = this.populateFromRequest(absence, req, user, uriParser);

		absence.setPosted(new Timestamp(System.currentTimeMillis()));
		absence.setPoster(user);

		return absence;
	}

	@Override
	protected Absence populateFromUpdateRequest(Absence bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, SQLException {

		Absence absence = super.populateFromUpdateRequest(bean, req, user, uriParser);

		absence = this.populateFromRequest(absence, req, user, uriParser);

		absence.setUpdated(new Timestamp(System.currentTimeMillis()));
		absence.setEditor(user);

		return absence;
	}

	protected Absence populateFromRequest(Absence absence, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		String period = ValidationUtils.validateNotEmptyParameter("period", req, errors);

		if(errors.isEmpty()) {

			Timestamp startTime = null;

			Timestamp endTime = null;

			if(period.equals("oneday")) {

				Date date = ValidationUtils.validateParameter("date", req, true,  DATE_POPULATOR, errors);

				String timeMode = ValidationUtils.validateNotEmptyParameter("time", req, errors);

				if(errors.isEmpty()) {

					if(timeMode.equals("partofday")) {

						startTime = this.validateTime(ValidationUtils.validateNotEmptyParameter("startTime", req, errors), date, errors);

						endTime = this.validateTime(ValidationUtils.validateNotEmptyParameter("endTime", req, errors), date, errors);

						if(startTime != null && endTime != null && (endTime.equals(startTime) || endTime.before(startTime))) {
							errors.add(new ValidationError("EndTimeBeforeStartTime"));
						}

					} else {

						startTime = new Timestamp(date.getTime());

						endTime = startTime;

					}

				} else {

					throw new ValidationException(errors);
				}

			} else {

				Date startDate = ValidationUtils.validateParameter("startDate", req, true, DATE_POPULATOR, errors);

				Date endDate = ValidationUtils.validateParameter("endDate", req, true, DATE_POPULATOR, errors);

				if(startDate != null && endDate != null) {

					startTime = new Timestamp(startDate.getTime());

					endTime = new Timestamp(endDate.getTime());

					if(DateUtils.daysBetween(startTime, endTime) < 1) {
						errors.add(new ValidationError("DaysBetweenToSmall"));
					}

				}

			}

			if(errors.isEmpty()) {

				this.validateLastReportTime(startTime, errors);

				if(errors.isEmpty()) {

					absence.setStartTime(startTime);
					absence.setEndTime(endTime);
					absence.setGroup((CommunityGroup) req.getAttribute("group"));

					return absence;

				}

			}

		}

		throw new ValidationException(errors);
	}

	protected Timestamp validateTime(String time, Date date, List<ValidationError> errors) {

		if(!StringUtils.isEmpty(time)) {

			String [] timeParts = time.split(":");

			if(timeParts.length == 2 && NumberUtils.isInt(timeParts[0]) && NumberUtils.isInt(timeParts[1])) {

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.set(Calendar.HOUR, Integer.parseInt(timeParts[0]));
				calendar.set(Calendar.MINUTE, Integer.parseInt(timeParts[1]));
				calendar.set(Calendar.MILLISECOND, 0);

				return new Timestamp(calendar.getTimeInMillis());

			}

			errors.add(new ValidationError("time", ValidationErrorType.InvalidFormat));

		}

		return null;
	}

	protected boolean absenceIsOpen(Timestamp startTime) {

		Calendar currentTime = Calendar.getInstance();

		Calendar lastReportTime = Calendar.getInstance();
		lastReportTime.set(Calendar.HOUR_OF_DAY, this.absenceModule.getLastReportHour());
		lastReportTime.set(Calendar.MINUTE, 0);

		Calendar reportTime = Calendar.getInstance();
		reportTime.setTime(startTime);
		reportTime.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY));
		reportTime.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE));

		long daysBetween = DateUtils.daysBetween(lastReportTime, reportTime);

		if(daysBetween > 0 || (daysBetween == 0 && reportTime.before(lastReportTime))) {
			return true;
		}

		return false;
	}

	protected void validateLastReportTime(Timestamp startTime, List<ValidationError> errors) {

		if(!this.absenceIsOpen(startTime)) {
			errors.add(new ValidationError("TimeBeforeLastReportTime"));
		}

	}

	@Override
	protected List<Breadcrumb> getListBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser, ValidationError validationError) {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(this.absenceModule.getGroupBreadcrumb(group), this.absenceModule.getModuleBreadcrumb(group));
	}

	@Override
	protected List<Breadcrumb> getShowBreadcrumbs(Absence bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(this.absenceModule.getGroupBreadcrumb(group), this.absenceModule.getModuleBreadcrumb(group), this.absenceModule.getShowBreadCrumb(group, bean));
	}


	@Override
	protected List<Breadcrumb> getAddBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser) {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(this.absenceModule.getGroupBreadcrumb(group), this.absenceModule.getModuleBreadcrumb(group), this.absenceModule.getAddBreadCrumb(group));
	}

	@Override
	protected List<Breadcrumb> getUpdateBreadcrumbs(Absence bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws Exception {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(this.absenceModule.getGroupBreadcrumb(group), this.absenceModule.getModuleBreadcrumb(group), this.absenceModule.getUpdateBreadCrumb(group, bean));
	}

	@Override
	protected Collection<? extends XMLable> getAllBeans(CommunityUser user, HttpServletRequest req, URIParser uriParser) throws SQLException {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		HighLevelQuery<Absence> query = new HighLevelQuery<Absence>();

		query.addParameter(this.posterParamFactory.getParameter(user));
		query.addParameter(this.groupParamFactory.getParameter(group));

		List<Absence> absences = this.absenceCRUDDAO.getAnnotatedDAO().getAll(query);

		if(!CollectionUtils.isEmpty(absences)) {

			for(Absence absence : absences) {

				if(!this.absenceIsOpen(absence.getStartTime())) {
					absence.setClosed(true);
				}

			}

		}

		return absences;

	}

	@Override
	protected Absence getBean(Integer beanID) throws SQLException, AccessDeniedException {

		Absence absence = super.getBean(beanID);

		if(absence != null) {
			if(!this.absenceIsOpen(absence.getStartTime())) {
				absence.setClosed(true);
			}
		}

		return absence;
	}

	@Override
	protected void redirectToListMethod(HttpServletRequest req, HttpServletResponse res, Absence bean) throws Exception {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/" + group.getGroupID());
	}

	@Override
	protected void checkUpdateAccess(Absence bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException {

		if(!this.absenceModule.hasAccess(bean, user) || (this.absenceModule.getModuleType().equals(ModuleType.Public) && !this.absenceIsOpen(bean.getStartTime()))) {
			throw new AccessDeniedException(this.absenceModule.getSectionDescriptor(), this.absenceModule.getModuleDescriptor(), "Update access for absence denied");
		}
	}

	@Override
	protected void checkDeleteAccess(Absence bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException {

		if(!this.absenceModule.hasAccess(bean, user) || (this.absenceModule.getModuleType().equals(ModuleType.Public) && !this.absenceIsOpen(bean.getStartTime()))) {
			throw new AccessDeniedException(this.absenceModule.getSectionDescriptor(), this.absenceModule.getModuleDescriptor(), "Delete access for absence denied");
		}
	}

	@Override
	protected void checkShowAccess(Absence bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException {

		if(!this.absenceModule.hasAccess(bean, user)) {
			throw new AccessDeniedException(this.absenceModule.getSectionDescriptor(), this.absenceModule.getModuleDescriptor(), "Show access for absence denied");
		}
	}

}

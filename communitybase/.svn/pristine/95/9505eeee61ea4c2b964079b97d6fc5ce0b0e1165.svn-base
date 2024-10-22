package se.dosf.communitybase.modules.weekmenu.cruds;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.modules.weekmenu.WeekMenuTemplateAdminModule;
import se.dosf.communitybase.modules.weekmenu.beans.WeekMenu;
import se.dosf.communitybase.modules.weekmenu.beans.WeekMenuTemplate;
import se.dosf.communitybase.modules.weekmenu.errors.DateCollissionError;
import se.dosf.communitybase.modules.weekmenu.errors.OutOfRangeError;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.IntegerBasedCRUD;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class WeekMenuTemplateCRUD extends IntegerBasedCRUD<WeekMenuTemplate, WeekMenuTemplateAdminModule> {

	protected final AnnotatedDAOWrapper<WeekMenuTemplate, Integer> weekMenuTemplateCRUDDAO;

	public WeekMenuTemplateCRUD(AnnotatedDAOWrapper<WeekMenuTemplate, Integer> crudDAO, WeekMenuTemplateAdminModule weekMenuAdminModule) {

		super(crudDAO, new AnnotatedRequestPopulator<WeekMenuTemplate>(WeekMenuTemplate.class), "WeekMenuTemplate", "weekMenuTemplate", "/", weekMenuAdminModule);

		this.weekMenuTemplateCRUDDAO = crudDAO;
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		super.appendAddFormData(doc, addTypeElement, user, req, uriParser);

		XMLUtils.appendNewElement(doc, addTypeElement, "CurrentYear", DateUtils.getCurrentYear());
	}

	@Override
	protected WeekMenuTemplate populateFromAddRequest(HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		WeekMenuTemplate weekMenuTemplate = super.populateFromAddRequest(req, user, uriParser);

		validateDates(weekMenuTemplate);

		int startYear = weekMenuTemplate.getStartYear();
		int startWeek = weekMenuTemplate.getStartWeek();

		if (startYear < DateUtils.getCurrentYear() || (startYear == DateUtils.getCurrentYear() && startWeek < DateUtils.getCurrentWeek())) {
			throw new ValidationException(new ValidationError("StartDateIsInThePast"));
		}

		List<WeekMenu> menus = new ArrayList<WeekMenu>();

		for (int i = 0; i < weekMenuTemplate.getWeeks(); i++) {
			WeekMenu m = new WeekMenu();
			m.setWeekMenuTemplate(weekMenuTemplate);
			m.setWeek(1 + i);
			menus.add(m);
		}

		weekMenuTemplate.setMenus(menus);

		weekMenuTemplate.setPoster(user);
		weekMenuTemplate.setLastEdit(new Timestamp(System.currentTimeMillis()));

		return weekMenuTemplate;

	}
	
	private void validateWeek(int week, String fieldName, int min, int max) throws ValidationException{
		if (week < min || week > max) {
			throw new ValidationException(new OutOfRangeError(fieldName, "InvalidWeek", min, max));
		}
	}

	protected void validateDates(WeekMenuTemplate weekMenuTemplate) throws ValidationException, Exception {

		int weeks = weekMenuTemplate.getWeeks();
		int startYear = weekMenuTemplate.getStartYear();
		int startWeek = weekMenuTemplate.getStartWeek();
		int endYear = weekMenuTemplate.getEndYear();
		int endWeek = weekMenuTemplate.getEndWeek();

		validateWeek(weeks, "weeks", 1, 52);
		validateWeek(startWeek, "startWeek", 1, DateUtils.getNumberOfWeeksInYear(startYear));
		validateWeek(endWeek, "endWeek", 1, DateUtils.getNumberOfWeeksInYear(endYear));
		
		if (endYear < DateUtils.getCurrentYear() || (endYear == DateUtils.getCurrentYear() && endWeek < DateUtils.getCurrentWeek())) {
			throw new ValidationException(new ValidationError("EndDateIsInThePast"));
		}

		if ((startYear == endYear && startWeek > endWeek) || startYear > endYear) {
			throw new ValidationException(new ValidationError("StartIsAfterEnd"));
		}

		List<WeekMenuTemplate> templates = weekMenuTemplateCRUDDAO.getAll();

		if (templates != null) {
			for (WeekMenuTemplate other : templates) {

				if (other.getWeekMenuTemplateID() != null && weekMenuTemplate.getWeekMenuTemplateID() != null && other.getWeekMenuTemplateID().equals(weekMenuTemplate.getWeekMenuTemplateID())) {
					continue;
				}

				int OstartYear = other.getStartYear();
				int OstartWeek = other.getStartWeek();
				int OendYear = other.getEndYear();
				int OendWeek = other.getEndWeek();

				// OtherStart < start < OtherEnd
				if ((startYear < OendYear || (startYear == OendYear && startWeek <= OendWeek)) && (startYear > OstartYear || (startYear == OstartYear && startWeek >= OstartWeek))) {
					throw new ValidationException(new DateCollissionError("StartDateOverlapsWithOther", other.getName()));
				}

				// OtherStart < end < OtherEnd
				if ((endYear < OendYear || (endYear == OendYear && endWeek <= OendWeek)) && (endYear > OstartYear || (endYear == OstartYear && endWeek >= OstartWeek))) {
					throw new ValidationException(new DateCollissionError("EndDateOverlapsWithOther", other.getName()));
				}

				// start < OtherStart < OtherEnd < end
				if (((OstartYear < endYear || (OstartYear == endYear && OstartWeek <= endWeek)) && (OstartYear > startYear || (OstartYear == startYear && OstartWeek >= startWeek)))
						|| ((OendYear < endYear || (OendYear == endYear && OendWeek <= endWeek)) && (OendYear > startYear || (OendYear == startYear && OendWeek >= startWeek)))) {
					throw new ValidationException(new DateCollissionError("DateContainsOther", other.getName()));
				}
			}
		}
	}

	@Override
	protected ForegroundModuleResponse beanAdded(WeekMenuTemplate bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/update/" + bean.getWeekMenuTemplateID());

		return null;
	}

	@Override
	protected WeekMenuTemplate populateFromUpdateRequest(WeekMenuTemplate bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		int origWeeks = bean.getWeeks();

		WeekMenuTemplate weekMenuTemplate = super.populateFromUpdateRequest(bean, req, user, uriParser);

		if (origWeeks != weekMenuTemplate.getWeeks()) {
			weekMenuTemplate.setWeeks(origWeeks);
		}

		validateDates(weekMenuTemplate);

		for (int i = 1; i <= weekMenuTemplate.getWeeks(); i++) {
			WeekMenu wk = weekMenuTemplate.getMenus().get(i - 1);

			wk.setMonday(StringUtils.trim(req.getParameter("monday-" + i)));
			wk.setTuesday(StringUtils.trim(req.getParameter("tuesday-" + i)));
			wk.setWednesday(StringUtils.trim(req.getParameter("wednesday-" + i)));
			wk.setThursday(StringUtils.trim(req.getParameter("thursday-" + i)));
			wk.setFriday(StringUtils.trim(req.getParameter("friday-" + i)));
			wk.setSaturday(StringUtils.trim(req.getParameter("saturday-" + i)));
			wk.setSunday(StringUtils.trim(req.getParameter("sunday-" + i)));
		}

		weekMenuTemplate.setPoster(user);
		weekMenuTemplate.setLastEdit(new Timestamp(System.currentTimeMillis()));

		return weekMenuTemplate;

	}

	@Override
	protected String getBeanName(WeekMenuTemplate bean) {

		return bean.getName();
	}

}

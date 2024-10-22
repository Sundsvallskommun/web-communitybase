package se.dosf.communitybase.modules.weekmenu.cruds;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.cruds.IntegerBasedCommunityBaseCRUD;
import se.dosf.communitybase.modules.weekmenu.SchoolMenuModule;
import se.dosf.communitybase.modules.weekmenu.beans.SchoolMenu;
import se.dosf.communitybase.modules.weekmenu.beans.WeekMenu;
import se.dosf.communitybase.modules.weekmenu.beans.WeekMenuTemplate;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.validation.ValidationUtils;

// visa 1 vecka i taget med nuvarande dag markerad

public class SchoolMenuCRUD extends IntegerBasedCommunityBaseCRUD<SchoolMenu, SchoolMenuModule> {

	protected final QueryParameterFactory<SchoolMenu, School> schoolIDParamFactory;
	protected final QueryParameterFactory<SchoolMenu, Integer> yearParamFactory;
	protected final QueryParameterFactory<SchoolMenu, Integer> weekParamFactory;

	protected final AnnotatedDAO<SchoolMenu> schoolMenuDAO;

	public SchoolMenuCRUD(AnnotatedDAO<SchoolMenu> crudDAO, SchoolMenuModule schoolMenuModule) {

		super(crudDAO.getWrapper(Integer.class), new AnnotatedRequestPopulator<SchoolMenu>(SchoolMenu.class), "SchoolMenu", "schoolMenu", "/", schoolMenuModule);

		schoolMenuDAO = crudDAO;

		schoolIDParamFactory = schoolMenuDAO.getParamFactory("school", School.class);
		yearParamFactory = schoolMenuDAO.getParamFactory("year", Integer.class);
		weekParamFactory = schoolMenuDAO.getParamFactory("week", Integer.class);
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws Exception {

		super.appendAddFormData(doc, addTypeElement, user, req, uriParser);

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		if (group != null) {
			doc.getDocumentElement().appendChild(group.toXML(doc));
		}

		Integer year = uriParser.getInt(3);
		Integer week = uriParser.getInt(4);

		if ((year == null || week == null) || year < DateUtils.getCurrentYear() || (year == DateUtils.getCurrentYear() && week < DateUtils.getCurrentWeek())) {
			XMLUtils.appendNewElement(doc, addTypeElement, "InvalidDate", "true");
			XMLUtils.appendNewElement(doc, addTypeElement, "Week", week);
		} else {
			WeekMenuTemplate template = callback.getActiveTemplate(year, week);
			if (template != null) {
				int weeksSkipped = week;
				int startYear = template.getStartYear();

				if (startYear == year) {
					weeksSkipped -= template.getStartWeek();
				} else {
					weeksSkipped += DateUtils.getNumberOfWeeksInYear(startYear) - template.getStartWeek();
					startYear++;

					while (startYear < year) {
						weeksSkipped += DateUtils.getNumberOfWeeksInYear(startYear);
						startYear++;
					}
				}
				
				Element weekMenus = doc.createElement("WeekMenus");
				XMLUtils.append(doc, weekMenus, template.getMenus());
				addTypeElement.appendChild(weekMenus);
				
				XMLUtils.appendNewElement(doc, addTypeElement, "DefaultOffset", weeksSkipped);
				
				int schoolOffset = callback.getSchoolTemplateOffset(template, group.getSchool());
				XMLUtils.appendNewElement(doc, addTypeElement, "SchoolTemplateOffset", schoolOffset);
				
				WeekMenu weekMenu = template.getMenus().get((weeksSkipped + schoolOffset) % template.getWeeks());
				addTypeElement.appendChild(weekMenu.toXML(doc)); //WeekMenu
			}
			XMLUtils.appendNewElement(doc, addTypeElement, "Year", year);
			XMLUtils.appendNewElement(doc, addTypeElement, "Week", week);
		}
	}
	
	protected void appendShowXML(Document doc, Element element, CommunityUser user, HttpServletRequest req, int year, int week){
		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		if (group != null) {
			doc.getDocumentElement().appendChild(group.toXML(doc));
		}

		XMLUtils.appendNewElement(doc, element, "Year", year);
		XMLUtils.appendNewElement(doc, element, "Week", week);
		
		try {
			callback.checkEditAccess(user, group);
			XMLUtils.appendNewElement(doc, element, "Admin", "true");
		} catch (AccessDeniedException e) {
		}
	}

	@Override
	protected void appendShowFormData(SchoolMenu bean, Document doc, Element showTypeElement, CommunityUser user, HttpServletRequest req, HttpServletResponse res, URIParser uriParser) throws SQLException, IOException, Exception {

		super.appendShowFormData(bean, doc, showTypeElement, user, req, res, uriParser);
		
		appendShowXML(doc, showTypeElement, user, req, bean.getYear(), bean.getWeek());
	}

	@Override
	protected void appendListFormData(Document doc, Element listTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {

		super.appendListFormData(doc, listTypeElement, user, req, uriParser, validationErrors);

		YearAndWeek yw = fixYearLoop(NumberUtils.toInt(uriParser.get(3)), NumberUtils.toInt(uriParser.get(4)));

		appendShowXML(doc, listTypeElement, user, req, yw.year, yw.week);
	}

	@Override
	protected void appendUpdateFormData(SchoolMenu bean, Document doc, Element updateTypeElement, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws Exception {

		super.appendUpdateFormData(bean, doc, updateTypeElement, user, req, uriParser);

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		if (group != null) {
			doc.getDocumentElement().appendChild(group.toXML(doc));
		}

		//Only used for UI, not saved
		XMLUtils.appendNewElement(doc, updateTypeElement, "Year", bean.getYear());
		XMLUtils.appendNewElement(doc, updateTypeElement, "Week", bean.getWeek());
	}

	@Override
	protected SchoolMenu populateFromAddRequest(HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, Exception {

		SchoolMenu schoolMenu = super.populateFromAddRequest(req, user, uriParser);

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		Integer year = ValidationUtils.validateParameter("year", req, true, IntegerPopulator.getPopulator(), validationErrors);
		Integer week = ValidationUtils.validateParameter("week", req, true, IntegerPopulator.getPopulator(), validationErrors);
		
		WeekMenuTemplate template = callback.getActiveTemplate(year, week);
		Integer offset = null;
		
		if (template != null) {
			offset = ValidationUtils.validateParameter("offset", req, true, IntegerPopulator.getPopulator(), validationErrors);
		}

		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		if (year < DateUtils.getCurrentYear() || (year == DateUtils.getCurrentYear() && week < DateUtils.getCurrentWeek())) {
			throw new ValidationException(new ValidationError("DateIsInThePast"));
		}

		schoolMenu.setYear(year);
		schoolMenu.setWeek(week);

		School school = ((CommunityGroup) req.getAttribute("group")).getSchool();

		if (school == null) {
			throw new ValidationException(new ValidationError("MissingSchool"));
		}
		
		if (template != null) {
			callback.updateSchoolTemplateOffset(template, school, offset - 1);
		}

		schoolMenu.setSchool(school);
		schoolMenu.setPoster(user);
		schoolMenu.setCreated(new Timestamp(System.currentTimeMillis()));
		schoolMenu.setLastEdit(new Timestamp(System.currentTimeMillis()));

		return schoolMenu;
	}

	@Override
	protected SchoolMenu populateFromUpdateRequest(SchoolMenu bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws ValidationException, Exception {

		SchoolMenu schoolMenu = super.populateFromUpdateRequest(bean, req, user, uriParser);

		schoolMenu.setPoster(user);
		schoolMenu.setLastEdit(new Timestamp(System.currentTimeMillis()));

		return schoolMenu;
	}

	@Override
	public SchoolMenu getRequestedBean(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, String getMode) throws SQLException, AccessDeniedException {

		SchoolMenu bean;
		School school = ((CommunityGroup) req.getAttribute("group")).getSchool();

		if (uriParser.size() > 4) {
			// Year + Week
			if (NumberUtils.isInt(uriParser.get(3)) && NumberUtils.isInt(uriParser.get(4))) {
				YearAndWeek yw = fixYearLoop(NumberUtils.toInt(uriParser.get(3)), NumberUtils.toInt(uriParser.get(4)));

				if ((bean = getBean(school, yw.year, yw.week)) != null) {
					return bean;
				}
			}

		} else if (uriParser.size() > 3) {
			// ID
			if (NumberUtils.isInt(uriParser.get(3)) && (bean = getBean(NumberUtils.toInt(uriParser.get(3)))) != null && bean.getSchool().getSchoolID().equals(school.getSchoolID())) {
				return bean;
			}
		} else if (uriParser.size() == 3 && (bean = getBean(school, DateUtils.getCurrentYear(), DateUtils.getCurrentWeek())) != null) {
			// Current date
			return bean;
		}

		return null;
	}

	public SchoolMenu getBean(School school, Integer year, Integer week) throws SQLException, AccessDeniedException {

		HighLevelQuery<SchoolMenu> query = new HighLevelQuery<SchoolMenu>();
		query.addParameter(schoolIDParamFactory.getParameter(school));
		query.addParameter(yearParamFactory.getParameter(year));
		query.addParameter(weekParamFactory.getParameter(week));

		return schoolMenuDAO.get(query);
	}

	public ForegroundModuleResponse show(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		req.setAttribute("group", group);
		return super.show(req, res, user, uriParser);
	}

	public ForegroundModuleResponse add(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		req.setAttribute("group", group);
		return super.add(req, res, user, uriParser);
	}

	public ForegroundModuleResponse update(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		req.setAttribute("group", group);
		return super.update(req, res, user, uriParser);
	}

	public ForegroundModuleResponse delete(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		req.setAttribute("group", group);
		return super.delete(req, res, user, uriParser);
	}

	public static YearAndWeek fixYearLoop(Integer yearI, Integer weekI) {

		int year, week;

		if (yearI == null || weekI == null) {
			year = DateUtils.getCurrentYear();
			week = DateUtils.getCurrentWeek();
		} else {
			year = yearI;
			week = weekI;

			while (week < 1) {
				year -= 1;
				week += DateUtils.getNumberOfWeeksInYear(year);
			}

			while (week > DateUtils.getNumberOfWeeksInYear(year)) {
				week -= DateUtils.getNumberOfWeeksInYear(year);
				year += 1;
			}
		}

		return new YearAndWeek(year, week);
	}

	public static class YearAndWeek {

		public int year, week;

		public YearAndWeek(int year, int week) {

			this.year = year;
			this.week = week;
		}
	}

	@Override
	protected void addBean(SchoolMenu bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws Exception {

		if (!bean.isEmpty()) {
			super.addBean(bean, req, user, uriParser);
		}
	}

	@Override
	protected void updateBean(SchoolMenu bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws Exception {

		if (!bean.isEmpty()) {
			super.updateBean(bean, req, user, uriParser);
		} else {
			super.deleteBean(bean, req, user, uriParser);
		}
	}

	protected void redirectToShowMethod(SchoolMenu bean, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		res.sendRedirect(req.getContextPath() + callback.getFullAlias() + "/" + group.getGroupID() + "/show/" + bean.getYear() + "/" + bean.getWeek());
	}

	@Override
	protected ForegroundModuleResponse beanAdded(SchoolMenu bean, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {
		
		redirectToShowMethod(bean, req, res, user, uriParser);

		return null;
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(SchoolMenu bean, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		redirectToShowMethod(bean, req, res, user, uriParser);

		return null;
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(SchoolMenu bean, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		redirectToShowMethod(bean, req, res, user, uriParser);

		return null;
	}

	protected String formatYearAndWeek(int year, int week) {

		return year + " " + callback.getWeekShortName() + week;
	}

	@Override
	protected String getBeanName(SchoolMenu bean) {

		return formatYearAndWeek(bean.getYear(), bean.getWeek());
	}
	
	protected Breadcrumb getBreadcrumb(CommunityGroup group, int year, int week){
		
		return callback.getMethodBreadcrumb(callback.getModuleDescriptor().getName() + " " + formatYearAndWeek(year, week), "show/" + year + "/" + week, group);
	}

	protected Breadcrumb getShowBreadcrumb(URIParser uriParser, CommunityGroup group) {

		YearAndWeek yw = fixYearLoop(NumberUtils.toInt(uriParser.get(3)), NumberUtils.toInt(uriParser.get(4)));
		
		return getBreadcrumb(group, yw.year, yw.week);
	}
	
	protected Breadcrumb getUpdateBreadcrumb(SchoolMenu bean, CommunityGroup group) {

		return getBreadcrumb(group, bean.getYear(), bean.getWeek());
	}

	@Override
	protected List<Breadcrumb> getShowBreadcrumbs(SchoolMenu bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(callback.getGroupBreadcrumb(group), getShowBreadcrumb(uriParser, group));
	}

	@Override
	protected List<Breadcrumb> getListBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser, List<ValidationError> validationErrors) {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(callback.getGroupBreadcrumb(group), getShowBreadcrumb(uriParser, group));
	}
	
	@Override
	protected List<Breadcrumb> getAddBreadcrumbs(HttpServletRequest req, CommunityUser user, URIParser uriParser) {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(callback.getGroupBreadcrumb(group), getShowBreadcrumb(uriParser, group), callback.getAddBreadCrumb(group));
	}
	
	@Override
	protected List<Breadcrumb> getUpdateBreadcrumbs(SchoolMenu bean, HttpServletRequest req, CommunityUser user, URIParser uriParser) throws Exception {

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");

		return CollectionUtils.getList(callback.getGroupBreadcrumb(group), getUpdateBreadcrumb(bean, group), callback.getUpdateBreadCrumb(group, bean));
	}
	

	@Override
	protected void checkAddAccess(CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		super.checkAddAccess(user, req, uriParser);

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		callback.checkEditAccess(user, group);
	}

	@Override
	protected void checkUpdateAccess(SchoolMenu bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		super.checkUpdateAccess(bean, user, req, uriParser);

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		callback.checkEditAccess(user, group);
	}

	@Override
	protected void checkDeleteAccess(SchoolMenu bean, CommunityUser user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		super.checkDeleteAccess(bean, user, req, uriParser);

		CommunityGroup group = (CommunityGroup) req.getAttribute("group");
		callback.checkEditAccess(user, group);
	}

}

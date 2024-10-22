package se.dosf.communitybase.modules.weekmenu;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.dbcleanup.DBCleaner;
import se.dosf.communitybase.modules.weekmenu.beans.SchoolMenu;
import se.dosf.communitybase.modules.weekmenu.beans.SchoolTemplateOffset;
import se.dosf.communitybase.modules.weekmenu.beans.WeekMenuTemplate;
import se.dosf.communitybase.modules.weekmenu.cruds.SchoolMenuCRUD;
import se.dosf.communitybase.populators.CommunityUserQueryPopulator;
import se.dosf.communitybase.populators.CommunityUserTypePopulator;
import se.dosf.communitybase.populators.SchoolQueryPopulator;
import se.dosf.communitybase.populators.SchoolTypePopulator;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.OrderByCriteria;
import se.unlogic.standardutils.dao.QueryOperators;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.populators.BeanStringPopulator;
import se.unlogic.standardutils.populators.QueryParameterPopulator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class SchoolMenuModule extends AnnotatedCommunityModule implements CommunityModule, CRUDCallback<CommunityUser>, DBCleaner {

	@XSLVariable
	protected String newSchoolMenuText = "New school menu: ";

	@XSLVariable(name = "i18n.Monday")
	protected String monday = "Monday";

	@XSLVariable(name = "i18n.Tuesday")
	protected String tuesday = "Tuesday";

	@XSLVariable(name = "i18n.Wednesday")
	protected String wednesday = "Wednesday";

	@XSLVariable(name = "i18n.Thursday")
	protected String thursday = "Thursday";

	@XSLVariable(name = "i18n.Friday")
	protected String friday = "Friday";

	@XSLVariable(name = "i18n.Saturday")
	protected String saturday = "Saturday";

	@XSLVariable(name = "i18n.Sunday")
	protected String sunday = "Sunday";
	
	@XSLVariable(name = "i18n.WeekShort")
	protected String weekShort = "V.";
	
	@XSLVariable
	protected String updateBreadCrumb = "Update school menu";

	@XSLVariable
	protected String addBreadCrumb = "Set school menu";

	protected SchoolMenuCRUD schoolMenuCRUD;

	protected AnnotatedDAO<SchoolMenu> schoolMenuDAO;
	protected QueryParameterFactory<SchoolMenu, School> schoolParamFactory;
	protected QueryParameterFactory<SchoolMenu, Timestamp> createdParamFactory;
	protected OrderByCriteria<SchoolMenu> orderByWeek;

	protected AnnotatedDAO<WeekMenuTemplate> weekMenuTemplateDAO;
	protected QueryParameterFactory<WeekMenuTemplate, Integer> startYearParamFactory;
	protected QueryParameterFactory<WeekMenuTemplate, Integer> endYearParamFactory;

	protected AnnotatedDAO<SchoolTemplateOffset> schoolOffsetDAO;
	protected QueryParameterFactory<SchoolTemplateOffset, Integer> schoolIDParamFactory;
	protected QueryParameterFactory<SchoolTemplateOffset, Integer> weekMenuTemplateIDParamFactory;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		SimpleAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		List<QueryParameterPopulator<?>> queryParameterPopulators = new ArrayList<QueryParameterPopulator<?>>();

		queryParameterPopulators.add(CommunityUserQueryPopulator.POPULATOR);
		queryParameterPopulators.add(SchoolQueryPopulator.POPULATOR);

		List<BeanStringPopulator<?>> typePopulators = new ArrayList<BeanStringPopulator<?>>();

		typePopulators.add((new CommunityUserTypePopulator(this.systemInterface.getUserHandler(), false)));
		typePopulators.add(new SchoolTypePopulator(this.getSchoolDAO(), false, false));

		schoolMenuDAO = daoFactory.getDAO(SchoolMenu.class, queryParameterPopulators, typePopulators);
		schoolMenuCRUD = new SchoolMenuCRUD(schoolMenuDAO, this);

		schoolParamFactory = schoolMenuDAO.getParamFactory("school", School.class);
		createdParamFactory = schoolMenuDAO.getParamFactory("created", Timestamp.class);
		orderByWeek = schoolMenuDAO.getOrderByCriteria("week", Order.ASC);

		weekMenuTemplateDAO = daoFactory.getDAO(WeekMenuTemplate.class);
		startYearParamFactory = weekMenuTemplateDAO.getParamFactory("startYear", Integer.class);
		endYearParamFactory = weekMenuTemplateDAO.getParamFactory("endYear", Integer.class);
		
		schoolOffsetDAO = daoFactory.getDAO(SchoolTemplateOffset.class);
		schoolIDParamFactory = schoolOffsetDAO.getParamFactory("schoolID", Integer.class);
		weekMenuTemplateIDParamFactory = schoolOffsetDAO.getParamFactory("weekMenuTemplateID", Integer.class);
	}

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(SchoolMenuModule.class, this)) {

			log.warn("Another instance has already been registered in instance handler for class " + this.getClass().getName());
		}
	}

	@Override
	public void unload() throws Exception {

		super.unload();

		systemInterface.getInstanceHandler().removeInstance(SchoolMenuModule.class, this);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		res.sendRedirect(req.getContextPath() + getFullAlias() + "/" + group.getGroupID() + "/show/" + DateUtils.getCurrentYear() + "/" + DateUtils.getCurrentWeek());
		return null;
	}

	@GroupMethod
	public ForegroundModuleResponse show(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return schoolMenuCRUD.show(req, res, user, uriParser, group);
	}

	@GroupMethod
	public ForegroundModuleResponse add(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return schoolMenuCRUD.add(req, res, user, uriParser, group);
	}

	@GroupMethod
	public ForegroundModuleResponse update(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return schoolMenuCRUD.update(req, res, user, uriParser, group);
	}

	@GroupMethod
	public ForegroundModuleResponse delete(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return schoolMenuCRUD.delete(req, res, user, uriParser, group);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends Event> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {

		HighLevelQuery<SchoolMenu> query = new HighLevelQuery<SchoolMenu>();
		query.addParameter(schoolParamFactory.getParameter(group.getSchool()));
		query.addParameter(createdParamFactory.getParameter(startStamp, QueryOperators.BIGGER_THAN));
		query.setOrderByCriterias(CollectionUtils.getList(orderByWeek));

		List<SchoolMenu> newSchoolMenus = schoolMenuDAO.getAll(query);

		if (newSchoolMenus != null) {
			List<IdentifiedEvent> events = new ArrayList<IdentifiedEvent>();

			for (SchoolMenu menu : newSchoolMenus) {
				IdentifiedEvent event = new IdentifiedEvent();
				event.setTitle(this.newSchoolMenuText + menu.getWeek());

				StringBuilder sb = new StringBuilder();
				sb.append(event.getTitle());
				appendMealToStringBuilder(monday, menu.getMonday(), sb);
				appendMealToStringBuilder(tuesday, menu.getTuesday(), sb);
				appendMealToStringBuilder(wednesday, menu.getWednesday(), sb);
				appendMealToStringBuilder(thursday, menu.getThursday(), sb);
				appendMealToStringBuilder(friday, menu.getFriday(), sb);
				appendMealToStringBuilder(saturday, menu.getSaturday(), sb);
				appendMealToStringBuilder(sunday, menu.getSunday(), sb);

				event.setDescription(sb.toString());
				event.setFullAlias(this.getFullAlias(group) + "/show/" + menu.getYear() + "/" + menu.getWeek());
				event.setAdded(menu.getCreated());
				event.setId(menu.getSchoolMenuID());
				events.add(event);
			}

			return events;
		}

		return null;
	}

	private void appendMealToStringBuilder(String day, String meal, StringBuilder sb) {

		if (meal != null && !meal.equals("")) {
			sb.append("\n");
			sb.append(day);
			sb.append(": ");
			sb.append(meal);
		}
	}

	@Override
	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityUser user) {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));

		doc.appendChild(documentElement);
		return doc;
	}

	public WeekMenuTemplate getActiveTemplate(int year, int week) throws SQLException {

		HighLevelQuery<WeekMenuTemplate> query = new HighLevelQuery<WeekMenuTemplate>();
		query.addParameter(startYearParamFactory.getParameter(year, QueryOperators.SMALLER_THAN_OR_EQUALS));
		query.addParameter(endYearParamFactory.getParameter(year, QueryOperators.BIGGER_THAN_OR_EQUALS));

		List<WeekMenuTemplate> potentialTemplates = weekMenuTemplateDAO.getAll(query);

		if (potentialTemplates != null) {
			for (WeekMenuTemplate template : potentialTemplates) {
				if (template.getStartYear() == year && template.getStartWeek() > week) {
					continue;
				}

				if (template.getEndYear() == year && template.getEndWeek() < week) {
					continue;
				}
				
				if(!template.isEmpty()){
					return template;
				}
			}
		}

		return null;
	}

	public void checkEditAccess(CommunityUser user, CommunityGroup group) throws AccessDeniedException {

		checkAdminAccess(user, group);
	}

	public void updateSchoolTemplateOffset(WeekMenuTemplate template, School school, int offset) throws SQLException{
		SchoolTemplateOffset sto = getSchoolTemplate(template, school);
		
		if(sto == null){
			sto = new SchoolTemplateOffset();
			sto.setOffset(offset);
			sto.setSchoolID(school.getSchoolID());
			sto.setWeekMenuTemplateID(template.getWeekMenuTemplateID());
			schoolOffsetDAO.add(sto);
		} else {
			sto.setOffset(offset);
			schoolOffsetDAO.update(sto);
		}
		
	}

	public SchoolTemplateOffset getSchoolTemplate(WeekMenuTemplate template, School school) throws SQLException {

		HighLevelQuery<SchoolTemplateOffset> query = new HighLevelQuery<SchoolTemplateOffset>();
		query.addParameter(schoolIDParamFactory.getParameter(school.getSchoolID()));
		query.addParameter(weekMenuTemplateIDParamFactory.getParameter(template.getWeekMenuTemplateID()));

		return schoolOffsetDAO.get(query);
	}

	public int getSchoolTemplateOffset(WeekMenuTemplate template, School school) throws SQLException {

		SchoolTemplateOffset offset = getSchoolTemplate(template, school);

		if (offset != null) {
			return offset.getOffset();
		}

		return 0;
	}
	
	public Breadcrumb getAddBreadCrumb(CommunityGroup group) {
		return this.getMethodBreadcrumb(this.addBreadCrumb, "add", group);
	}

	public Breadcrumb getUpdateBreadCrumb(CommunityGroup group, SchoolMenu schoolMenu) {
		return this.getMethodBreadcrumb(this.updateBreadCrumb, "update" + "/" + schoolMenu.getSchoolMenuID(), group);
	}

	public Breadcrumb getMethodBreadcrumb(String name, String methodUri, CommunityGroup group) {
		return new Breadcrumb(name, this.getFullAlias() + "/" + group.getGroupID() + "/" + methodUri);
	}
	
	public String getWeekShortName(){
		return weekShort;
	}

	@Override
	public void cleanDB() throws SQLException {

		// TODO Remove old (> 1 month) school menus
		
	}

}
